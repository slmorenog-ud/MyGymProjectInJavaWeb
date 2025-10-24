package com.agym.controlador;

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

@WebServlet("/marcarCompletada")
public class MarcarCompletadaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        long rutinaId = Long.parseLong(request.getParameter("rutinaId"));

        String realPath = getServletContext().getRealPath("/");
        List<RutinaGuardada> rutinasGuardadas = JsonUtil.leerRutinasGuardadas(realPath);

        // Buscar la rutina y actualizar su estado
        for (RutinaGuardada rg : rutinasGuardadas) {
            if (rg.getId() == rutinaId && rg.getUsuarioId() == usuario.getId()) {
                rg.setEstado("Completada");
                break;
            }
        }

        JsonUtil.escribirRutinasGuardadas(rutinasGuardadas, realPath);

        response.sendRedirect("historial");
    }
}
