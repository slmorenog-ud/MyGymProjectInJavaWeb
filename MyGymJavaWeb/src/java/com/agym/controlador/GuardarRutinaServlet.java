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

/**
 * Servlet responsable de guardar una rutina generada en el historial del usuario.
 * <p>
 * Su única función es tomar la rutina temporal almacenada en la sesión,
 * encapsularla en un objeto {@link RutinaGuardada} y persistirla en el
 * archivo JSON correspondiente.
 * </p>
 * <p>
 * <b>Principios de diseño aplicados:</b>
 * - <b>Principio de Responsabilidad Única (SRP):</b> Esta clase tiene una sola
 *   razón para cambiar: la lógica de persistencia de una rutina. No genera
 *   la rutina ni la muestra, solo la guarda.
 * </p>
 */
@WebServlet("/guardarRutina")
public class GuardarRutinaServlet extends HttpServlet {

    // Usamos AtomicLong para garantizar un ID único incluso en un entorno de múltiples hilos
    private AtomicLong idCounter = new AtomicLong(System.currentTimeMillis());

    /**
     * Procesa las solicitudes HTTP <code>POST</code> para guardar la rutina.
     *
     * @param request  objeto que contiene la solicitud del cliente.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException si ocurre un error de entrada/salida.
     */
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
