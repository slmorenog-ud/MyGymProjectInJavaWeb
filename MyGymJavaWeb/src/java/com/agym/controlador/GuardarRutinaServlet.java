package com.agym.controlador;

import com.agym.modelo.Rutina;
import com.agym.modelo.RutinaGuardada;
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
import java.util.concurrent.atomic.AtomicLong;

@WebServlet("/guardarRutina")
public class GuardarRutinaServlet extends HttpServlet {

    // Usamos AtomicLong para garantizar un ID único incluso en un entorno de múltiples hilos
    private AtomicLong idCounter = new AtomicLong(System.currentTimeMillis());

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
            String realPath = getServletContext().getRealPath("/");

            // Crear la nueva rutina guardada
            RutinaGuardada nuevaRutina = new RutinaGuardada();
            nuevaRutina.setId(idCounter.getAndIncrement());
            nuevaRutina.setUsuarioId(usuario.getId());
            nuevaRutina.setRutina(rutinaTemporal);

            // Leer, añadir y escribir
            List<RutinaGuardada> rutinasGuardadas = JsonUtil.leerRutinasGuardadas(realPath);
            rutinasGuardadas.add(nuevaRutina);
            JsonUtil.escribirRutinasGuardadas(rutinasGuardadas, realPath);

            // Limpiar la rutina temporal de la sesión
            session.removeAttribute("rutinaTemporal");
        }

        // Redirigir al historial para que el usuario vea su rutina guardada
        response.sendRedirect("historial");
    }
}
