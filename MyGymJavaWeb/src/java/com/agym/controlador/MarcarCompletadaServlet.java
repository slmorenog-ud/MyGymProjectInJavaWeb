package com.agym.controlador;

import com.agym.util.RutinaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet para marcar una rutina como completada.
 * Su única responsabilidad es actualizar el estado de una rutina.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga de marcar
 * la rutina, no de la lógica de negocio asociada.
 */
@WebServlet("/marcarCompletada")
public class MarcarCompletadaServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para marcar una rutina como completada.
     */
    // Principio: SRP / SoC
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long rutinaId = Long.parseLong(request.getParameter("rutinaId"));

        RutinaDAO rutinaDAO = new RutinaDAO();
        rutinaDAO.marcarComoCompletada(rutinaId);

        response.sendRedirect("historial");
    }
}
