package com.agym.logic;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase base abstracta para generar rutinas de entrenamiento.
 * Define el algoritmo principal (Template Method) y centraliza la lógica común.
 *
 * --- Principios de Diseño Clave ---
 * OCP (Open/Closed Principle): El sistema es abierto a extensiones (ej. nuevas rutinas)
 * pero cerrado a modificaciones. Para añadir una rutina de 5 días, se crea una nueva
 * subclase sin alterar el código existente.
 *
 * LSP (Liskov Substitution Principle): Cualquier subclase (ej. GeneradorRutina2Dias)
 * puede ser usada en lugar de esta clase base sin que el cliente (GenerarRutinaServlet)
 * se dé cuenta, garantizando un comportamiento consistente.
 */
public abstract class GeneradorRutinaBase {

    /**
     * Método plantilla (Template Method) que orquesta la generación de la rutina.
     * Es final para que las subclases no puedan alterar el algoritmo principal.
     */
    public final Rutina generar(Usuario usuario, List<Ejercicio> todosLosEjercicios) {
        Rutina rutina = new Rutina();
        String seriesYRepeticionesBase = getSeriesYRepeticionesPorObjetivo(usuario.getObjetivo(), usuario.getExperiencia());

        int edad = calcularEdad(usuario.getFechaNacimiento());
        if (edad > 50) {
            if (seriesYRepeticionesBase.startsWith("4")) {
                seriesYRepeticionesBase = "3" + seriesYRepeticionesBase.substring(1);
            } else if (seriesYRepeticionesBase.startsWith("5")) {
                seriesYRepeticionesBase = "4" + seriesYRepeticionesBase.substring(1);
            }
        }

        // Hook para que las subclases construyan la estructura específica
        construirRutina(rutina, usuario, todosLosEjercicios, seriesYRepeticionesBase);

        // Añade un ejercicio extra si hay una prioridad muscular definida
        String prioridad = usuario.getPrioridadMuscular();
        if (prioridad != null && !prioridad.equals("sin_preferencia")) {
            agregarEjercicioPrioritario(rutina, todosLosEjercicios, usuario, seriesYRepeticionesBase);
        }

        return rutina;
    }

    /**
     * Método abstracto que las subclases deben implementar para definir la
     * estructura de días y ejercicios de la rutina.
     */
    protected abstract void construirRutina(Rutina rutina, Usuario usuario, List<Ejercicio> todosLosEjercicios, String seriesYRepeticiones);

    // Principio: KISS (Keep It Simple, Stupid) - Lógica simple y directa.
    protected String getSeriesYRepeticionesPorObjetivo(String objetivo, String experiencia) {
        boolean esPrincipiante = "principiante".equalsIgnoreCase(experiencia);
        boolean esIntermedio = "intermedio".equalsIgnoreCase(experiencia);

        switch (objetivo) {
            case "bajar_peso":
                if (esPrincipiante) return "3 series de 15 repeticiones";
                else return "4 series de 15-20 repeticiones";
            case "subir_peso":
                if (esPrincipiante) return "3 series de 8-12 repeticiones";
                else if (esIntermedio) return "4 series de 8-12 repeticiones";
                else return "5 series de 6-10 repeticiones";
            case "fortalecer":
                if (esPrincipiante) return "3 series de 5-8 repeticiones";
                else if (esIntermedio) return "4 series de 4-6 repeticiones";
                else return "5 series de 3-5 repeticiones";
            default:
                return "3 series de 10 repeticiones";
        }
    }

    // Principio: SRP (Single Responsibility Principle) - Su única responsabilidad es calcular la edad.
    protected int calcularEdad(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) return 0;
        try {
            return Period.between(LocalDate.parse(fechaNacimiento), LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha de nacimiento: " + fechaNacimiento);
            return 0;
        }
    }

    // Principio: SRP - Su única responsabilidad es añadir el ejercicio prioritario.
    protected void agregarEjercicioPrioritario(Rutina rutina, List<Ejercicio> todosLosEjercicios, Usuario usuario, String seriesYRepeticiones) {
        String grupoPrioritario = "";
        switch (usuario.getPrioridadMuscular()) {
            case "tren_superior": grupoPrioritario = "Pecho"; break;
            case "tren_inferior": grupoPrioritario = "Piernas"; break;
            case "brazos": grupoPrioritario = "Biceps"; break;
        }

        if (!grupoPrioritario.isEmpty()) {
            final String finalGrupoPrioritario = grupoPrioritario;
            List<Ejercicio> ejerciciosPrioritarios = todosLosEjercicios.stream()
                .filter(e -> finalGrupoPrioritario.equalsIgnoreCase(e.getGrupoMuscular()))
                .collect(Collectors.toList());

            Collections.shuffle(ejerciciosPrioritarios);

            if (!ejerciciosPrioritarios.isEmpty()) {
                Ejercicio ejercicioExtra = ejerciciosPrioritarios.get(0);
                ejercicioExtra.setSeriesYRepeticiones(seriesYRepeticiones);

                // Intenta añadir el ejercicio a un día relevante
                for (Rutina.DiaRutina dia : rutina.getDias()) {
                    if (dia.getNombre().toLowerCase().contains("superior") && finalGrupoPrioritario.equals("Pecho") ||
                        dia.getNombre().toLowerCase().contains("inferior") && finalGrupoPrioritario.equals("Piernas") ||
                        dia.getNombre().toLowerCase().contains("empuje") && finalGrupoPrioritario.equals("Pecho")) {
                        dia.agregarEjercicio(ejercicioExtra);
                        return; // Añadido y salimos
                    }
                }
            }
        }
    }

    // Principio: SRP y DRY - Centraliza la creación de un día de rutina.
    protected Rutina.DiaRutina crearDiaRutina(String nombreDia, List<Ejercicio> todosEjercicios, String experiencia, String seriesYRepeticiones, int numEjerciciosPorGrupo, String... gruposMusculares) {
        Rutina.DiaRutina dia = new Rutina.DiaRutina(nombreDia);

        List<Ejercicio> ejerciciosFiltrados = todosEjercicios.stream()
            .filter(e -> {
                if ("principiante".equalsIgnoreCase(experiencia)) return "Principiante".equalsIgnoreCase(e.getDificultad());
                if ("intermedio".equalsIgnoreCase(experiencia)) return !"Avanzado".equalsIgnoreCase(e.getDificultad());
                return true;
            })
            .collect(Collectors.toList());

        for (String grupo : gruposMusculares) {
            List<Ejercicio> ejerciciosPorGrupo = ejerciciosFiltrados.stream()
                .filter(e -> grupo.equalsIgnoreCase(e.getGrupoMuscular()))
                .collect(Collectors.toList());
            Collections.shuffle(ejerciciosPorGrupo);
            int ejerciciosAAgregar = Math.min(numEjerciciosPorGrupo, ejerciciosPorGrupo.size());
            for (int i = 0; i < ejerciciosAAgregar; i++) {
                Ejercicio ej = ejerciciosPorGrupo.get(i);
                ej.setSeriesYRepeticiones(seriesYRepeticiones);
                dia.agregarEjercicio(ej);
            }
        }
        return dia;
    }
}
