package com.agym.controlador;

import com.agym.modelo.RutinaGuardada;
import com.agym.modelo.Usuario;
import com.agym.util.RutinaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para mostrar el historial de rutinas guardadas del usuario.
 * Su única responsabilidad es recuperar y mostrar el historial.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga de la visualización
 * del historial, no de la lógica de guardado.
 */
@WebServlet("/historial")
public class HistorialServlet extends HttpServlet {

    /**
     * Procesa la solicitud GET para mostrar el historial de rutinas.
     */
    // Principio: SRP / SoC
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        RutinaDAO rutinaDAO = new RutinaDAO();
        List<RutinaGuardada> rutinasGuardadas = rutinaDAO.getRutinasPorUsuario(usuario.getId());

        request.setAttribute("rutinas", rutinasGuardadas);
        request.getRequestDispatcher("historial.jsp").forward(request, response);
    }
}
