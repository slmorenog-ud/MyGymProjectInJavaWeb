package com.agym.controlador;

import com.agym.logic.GeneradorRutinaBase;
import com.agym.logic.GeneradorRutinaFactory;
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
import java.util.List;

/**
 * Servlet que orquesta la generación de rutinas.
 * No contiene lógica de negocio, solo coordina las acciones.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Su única responsabilidad es manejar la
 * solicitud HTTP y delegar la creación de la rutina.
 * DIP (Dependency Inversion Principle): Depende de una abstracción
 * (GeneradorRutinaBase) en lugar de una implementación concreta.
 * LSP (Liskov Substitution Principle): Trata a cualquier generador de rutinas
 * (ej. GeneradorRutina2Dias) como si fuera un GeneradorRutinaBase, sin saber
 * el tipo específico.
 */
@WebServlet("/generarRutina")
public class GenerarRutinaServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para generar una nueva rutina.
     */
    // Principio: SRP / SoC (Separation of Concerns)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        usuario.setAltura(Double.parseDouble(request.getParameter("altura")));
        usuario.setPeso(Double.parseDouble(request.getParameter("peso")));
        usuario.setExperiencia(request.getParameter("experiencia"));
        usuario.setDiasDisponibles(Integer.parseInt(request.getParameter("dias")));
        usuario.setObjetivo(request.getParameter("objetivo"));
        usuario.setPrioridadMuscular(request.getParameter("prioridadMuscular"));

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

        GeneradorRutinaBase generador = GeneradorRutinaFactory.getGenerador(usuario.getDiasDisponibles());
        Rutina rutina = generador.generar(usuario, todosLosEjercicios);

        session.setAttribute("rutinaTemporal", rutina);
        request.setAttribute("rutina", rutina);
        request.setAttribute("objetivoUsuario", usuario.getObjetivo());
        request.getRequestDispatcher("rutina.jsp").forward(request, response);
    }
}
