package com.agym.controlador;

import com.agym.modelo.Rutina;
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

/**
 * Servlet para guardar una rutina generada en el historial del usuario.
 * Su única responsabilidad es persistir la rutina temporal.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo guarda la rutina, no la crea.
 */
@WebServlet("/guardarRutina")
public class GuardarRutinaServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para guardar la rutina.
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

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Rutina rutinaTemporal = (Rutina) session.getAttribute("rutinaTemporal");

        if (rutinaTemporal != null) {
            RutinaGuardada nuevaRutina = new RutinaGuardada();
            nuevaRutina.setUsuarioId(usuario.getId());
            nuevaRutina.setRutina(rutinaTemporal);

            RutinaDAO rutinaDAO = new RutinaDAO();
            rutinaDAO.guardarRutina(nuevaRutina);

            session.removeAttribute("rutinaTemporal");
        }
        response.sendRedirect("historial");
    }
}
