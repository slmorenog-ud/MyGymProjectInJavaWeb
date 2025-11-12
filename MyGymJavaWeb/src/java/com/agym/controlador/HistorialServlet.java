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
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet para mostrar el historial de rutinas guardadas del usuario.
 * Su única responsabilidad es leer y mostrar el historial.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo muestra el historial.
 */
@WebServlet("/historial")
public class HistorialServlet extends HttpServlet {

    private RutinaDAO rutinaDAO;

    @Override
    public void init() {
        rutinaDAO = new RutinaDAO();
    }

    /**
     * Procesa la solicitud GET para mostrar el historial.
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

        try {
            List<RutinaGuardada> rutinasDelUsuario = rutinaDAO.obtenerRutinasPorUsuario(usuario.getId());
            request.setAttribute("historialRutinas", rutinasDelUsuario);
            request.getRequestDispatcher("historial.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error de base de datos al obtener el historial de rutinas", e);
        }
    }
}
