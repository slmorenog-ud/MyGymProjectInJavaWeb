package com.agym.controlador;

import com.agym.util.RutinaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet para marcar una rutina como "Completada".
 * Su única responsabilidad es actualizar el estado de una rutina.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo actualiza el estado.
 */
@WebServlet("/marcarCompletada")
public class MarcarCompletadaServlet extends HttpServlet {

    private RutinaDAO rutinaDAO;

    @Override
    public void init() {
        rutinaDAO = new RutinaDAO();
    }

    /**
     * Procesa la solicitud POST para marcar una rutina como completada.
     */
    // Principio: SRP / SoC
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        long rutinaId = Long.parseLong(request.getParameter("rutinaId"));

        try {
            rutinaDAO.actualizarEstadoRutina(rutinaId, "Completada");
            response.sendRedirect("historial");
        } catch (SQLException e) {
            throw new ServletException("Error de base de datos al marcar la rutina como completada", e);
        }
    }
}
