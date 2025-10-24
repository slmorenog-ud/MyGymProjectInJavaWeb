package com.agym.controlador;

import com.agym.modelo.Ejercicio;
import com.agym.modelo.Rutina;
import com.agym.modelo.Usuario;
import com.agym.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/generarRutina")
public class GenerarRutinaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Actualizar datos del usuario desde el formulario
        usuario.setAltura(Double.parseDouble(request.getParameter("altura")));
        usuario.setPeso(Double.parseDouble(request.getParameter("peso")));
        usuario.setExperiencia(request.getParameter("experiencia"));
        usuario.setDiasDisponibles(Integer.parseInt(request.getParameter("dias")));
        usuario.setObjetivo(request.getParameter("objetivo"));
        usuario.setPrioridadMuscular(request.getParameter("prioridadMuscular"));

        // Guardar los datos actualizados del usuario
        String realPath = getServletContext().getRealPath("/");
        List<Usuario> usuarios = JsonUtil.leerUsuarios(realPath);
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                break;
            }
        }
        JsonUtil.escribirUsuarios(usuarios, realPath);


        List<Ejercicio> todosLosEjercicios = JsonUtil.leerEjercicios(realPath);

        Rutina rutina = generarRutinaLogica(usuario, todosLosEjercicios);

        // Guardar la rutina en la sesión para poder guardarla en el historial después
        session.setAttribute("rutinaTemporal", rutina);

        request.setAttribute("rutina", rutina);
        request.setAttribute("objetivoUsuario", usuario.getObjetivo()); // <-- Añadido
        request.getRequestDispatcher("rutina.jsp").forward(request, response);
    }

    private Rutina generarRutinaLogica(Usuario usuario, List<Ejercicio> todosLosEjercicios) {
        Rutina rutina = new Rutina();
        int edad = calcularEdad(usuario.getFechaNacimiento());
        String seriesYRepeticionesBase = getSeriesYRepeticionesPorObjetivo(usuario.getObjetivo(), usuario.getExperiencia());

        // Ajuste por edad
        if (edad > 50 && seriesYRepeticionesBase.startsWith("4")) {
            seriesYRepeticionesBase = "3" + seriesYRepeticionesBase.substring(1);
        } else if (edad > 50 && seriesYRepeticionesBase.startsWith("5")) {
             seriesYRepeticionesBase = "4" + seriesYRepeticionesBase.substring(1);
        }

        switch (usuario.getDiasDisponibles()) {
            case 2:
                rutina.agregarDia(crearDiaRutina("Día 1: Cuerpo Completo", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 2, "Pecho", "Espalda", "Piernas"));
                rutina.agregarDia(crearDiaRutina("Día 2: Cuerpo Completo", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 2, "Pecho", "Espalda", "Piernas"));
                break;
            case 3:
                rutina.agregarDia(crearDiaRutina("Día 1: Empuje", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 2, "Pecho", "Hombros", "Triceps"));
                rutina.agregarDia(crearDiaRutina("Día 2: Tirón", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 2, "Espalda", "Biceps"));
                rutina.agregarDia(crearDiaRutina("Día 3: Pierna", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 3, "Piernas"));
                break;
            case 4:
                rutina.agregarDia(crearDiaRutina("Día 1: Tren Superior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 2, "Pecho", "Espalda", "Hombros"));
                rutina.agregarDia(crearDiaRutina("Día 2: Tren Inferior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 3, "Piernas"));
                rutina.agregarDia(crearDiaRutina("Día 3: Tren Superior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 2, "Pecho", "Espalda", "Biceps", "Triceps"));
                rutina.agregarDia(crearDiaRutina("Día 4: Tren Inferior", todosLosEjercicios, usuario.getExperiencia(), seriesYRepeticionesBase, 3, "Piernas"));
                break;
        }

        // Ajuste por prioridad muscular
        String prioridad = usuario.getPrioridadMuscular();
        if (prioridad != null && !prioridad.equals("sin_preferencia")) {
            agregarEjercicioPrioritario(rutina, todosLosEjercicios, usuario, seriesYRepeticionesBase);
        }

        return rutina;
    }

    private void agregarEjercicioPrioritario(Rutina rutina, List<Ejercicio> todosLosEjercicios, Usuario usuario, String seriesYRepeticiones) {
        String grupoPrioritario = "";
        switch (usuario.getPrioridadMuscular()) {
            case "tren_superior":
                grupoPrioritario = "Pecho"; // O Espalda, Hombros
                break;
            case "tren_inferior":
                grupoPrioritario = "Piernas";
                break;
            case "brazos":
                grupoPrioritario = "Biceps"; // O Triceps
                break;
        }

        if (!grupoPrioritario.isEmpty()) {
            // Filtrar ejercicios adecuados para el grupo prioritario
            String finalGrupoPrioritario = grupoPrioritario;
            List<Ejercicio> ejerciciosPrioritarios = todosLosEjercicios.stream()
                .filter(e -> finalGrupoPrioritario.equalsIgnoreCase(e.getGrupoMuscular()))
                .collect(Collectors.toList());

            Collections.shuffle(ejerciciosPrioritarios);

            if (!ejerciciosPrioritarios.isEmpty()) {
                Ejercicio ejercicioExtra = ejerciciosPrioritarios.get(0);
                ejercicioExtra.setSeriesYRepeticiones(seriesYRepeticiones);

                // Buscar un día adecuado para añadir el ejercicio
                for (Rutina.DiaRutina dia : rutina.getDias()) {
                    if (dia.getNombre().toLowerCase().contains("superior") && finalGrupoPrioritario.equals("Pecho")) {
                        dia.agregarEjercicio(ejercicioExtra);
                        break;
                    }
                    if (dia.getNombre().toLowerCase().contains("inferior") && finalGrupoPrioritario.equals("Piernas")) {
                        dia.agregarEjercicio(ejercicioExtra);
                        break;
                    }
                     if (dia.getNombre().toLowerCase().contains("empuje") && finalGrupoPrioritario.equals("Pecho")) {
                        dia.agregarEjercicio(ejercicioExtra);
                        break;
                    }
                }
            }
        }
    }

    private Rutina.DiaRutina crearDiaRutina(String nombreDia, List<Ejercicio> todosEjercicios, String experiencia, String seriesYRepeticiones, int numEjerciciosPorGrupo, String... gruposMusculares) {
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
                Ejercicio ejercicioSeleccionado = ejerciciosPorGrupo.get(i);
                ejercicioSeleccionado.setSeriesYRepeticiones(seriesYRepeticiones);
                dia.agregarEjercicio(ejercicioSeleccionado);
            }
        }
        return dia;
    }

    private String getSeriesYRepeticionesPorObjetivo(String objetivo, String experiencia) {
        boolean esPrincipiante = "principiante".equalsIgnoreCase(experiencia);
        boolean esIntermedio = "intermedio".equalsIgnoreCase(experiencia);

        switch (objetivo) {
            case "bajar_peso":
                if (esPrincipiante) {
                    return "3 series de 15 repeticiones";
                } else { // Intermedio o Avanzado
                    return "4 series de 15-20 repeticiones";
                }
            case "subir_peso":
                if (esPrincipiante) {
                    return "3 series de 8-12 repeticiones";
                } else if (esIntermedio) {
                    return "4 series de 8-12 repeticiones";
                } else { // Avanzado
                    return "5 series de 6-10 repeticiones";
                }
            case "fortalecer":
                if (esPrincipiante) {
                    return "3 series de 5-8 repeticiones";
                } else if (esIntermedio) {
                    return "4 series de 4-6 repeticiones";
                } else { // Avanzado
                    return "5 series de 3-5 repeticiones";
                }
            default:
                return "3 series de 10 repeticiones";
        }
    }

    private int calcularEdad(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            return 0;
        }
        try {
            LocalDate fechaNac = LocalDate.parse(fechaNacimiento);
            LocalDate ahora = LocalDate.now();
            return Period.between(fechaNac, ahora).getYears();
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha de nacimiento: " + fechaNacimiento);
            return 0; // Retornar 0 si el formato es incorrecto
        }
    }
}
