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
 * <p>
 * Su única responsabilidad es invalidar la sesión HTTP actual del usuario
 * y redirigirlo a la página de inicio.
 * </p>
 * <p>
 * <b>Principios de diseño aplicados:</b>
 * - <b>Principio de Responsabilidad Única (SRP):</b> Esta clase tiene una sola
 *   razón para cambiar: la lógica de cierre de sesión.
 * </p>
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Procesa las solicitudes HTTP <code>POST</code> para el cierre de sesión.
     * Se utiliza POST en lugar de GET por seguridad y buenas prácticas, ya que
     * esta acción cambia el estado del servidor (invalida la sesión).
     *
     * @param request  objeto que contiene la solicitud del cliente.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // No crear una nueva sesión si no existe
        if (session != null) {
            session.invalidate(); // Invalida la sesión
        }

        response.sendRedirect("index.html"); // Redirige a la página de inicio
    }
}
