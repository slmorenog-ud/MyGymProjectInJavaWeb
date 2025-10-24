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

        request.setAttribute("rutina", rutina);
        request.getRequestDispatcher("rutina.jsp").forward(request, response);
    }

    private Rutina generarRutinaLogica(Usuario usuario, List<Ejercicio> ejercicios) {
        Rutina rutina = new Rutina();
        String seriesYRepeticiones = getSeriesYRepeticionesPorObjetivo(usuario.getObjetivo());

        switch (usuario.getDiasDisponibles()) {
            case 2:
                // 2 días: Full Body (2 ejercicios por grupo muscular principal)
                rutina.agregarDia(crearDiaRutina("Día 1: Cuerpo Completo", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Piernas"));
                rutina.agregarDia(crearDiaRutina("Día 2: Cuerpo Completo", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Piernas"));
                break;
            case 3:
                // 3 días: Push/Pull/Legs
                rutina.agregarDia(crearDiaRutina("Día 1: Empuje", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Hombros", "Triceps"));
                rutina.agregarDia(crearDiaRutina("Día 2: Tirón", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Espalda", "Biceps"));
                rutina.agregarDia(crearDiaRutina("Día 3: Pierna", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
                break;
            case 4:
                // 4 días: Superior/Inferior
                rutina.agregarDia(crearDiaRutina("Día 1: Tren Superior", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Hombros"));
                rutina.agregarDia(crearDiaRutina("Día 2: Tren Inferior", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
                rutina.agregarDia(crearDiaRutina("Día 3: Tren Superior", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 2, "Pecho", "Espalda", "Biceps", "Triceps"));
                rutina.agregarDia(crearDiaRutina("Día 4: Tren Inferior", ejercicios, usuario.getExperiencia(), seriesYRepeticiones, 3, "Piernas"));
                break;
        }
        return rutina;
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

    private String getSeriesYRepeticionesPorObjetivo(String objetivo) {
        switch (objetivo) {
            case "bajar_peso":
                return "3 series de 12-15 repeticiones";
            case "subir_peso":
                return "4 series de 8-12 repeticiones";
            case "fortalecer":
                return "5 series de 5-8 repeticiones";
            default:
                return "3 series de 10 repeticiones";
        }
    }
}
