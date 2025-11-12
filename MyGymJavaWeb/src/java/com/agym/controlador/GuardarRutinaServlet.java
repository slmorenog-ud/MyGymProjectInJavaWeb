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
import java.sql.SQLException;

/**
 * Servlet para guardar una rutina generada en el historial del usuario.
 * Su única responsabilidad es persistir la rutina temporal.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo guarda la rutina, no la crea.
 */
@WebServlet("/guardarRutina")
public class GuardarRutinaServlet extends HttpServlet {

    private RutinaDAO rutinaDAO;

    @Override
    public void init() {
        rutinaDAO = new RutinaDAO();
    }

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
            try {
                RutinaGuardada nuevaRutina = new RutinaGuardada();
                nuevaRutina.setUsuarioId(usuario.getId());
                nuevaRutina.setRutina(rutinaTemporal);
                // El estado y la fecha se establecen por defecto en el constructor de RutinaGuardada

                rutinaDAO.guardarRutina(nuevaRutina);

                session.removeAttribute("rutinaTemporal");
            } catch (SQLException e) {
                throw new ServletException("Error de base de datos al guardar la rutina", e);
            }
        }
        response.sendRedirect("historial");
    }
}
