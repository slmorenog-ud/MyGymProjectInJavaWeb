package com.agym.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet que gestiona el cierre de sesión de un usuario.
 * Su única responsabilidad es invalidar la sesión HTTP del usuario.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga del logout.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para el cierre de sesión. Se usa POST
     * porque esta acción cambia el estado del servidor (invalida la sesión).
     */
    // Principio: SRP / SoC
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect("index.html");
    }
}
