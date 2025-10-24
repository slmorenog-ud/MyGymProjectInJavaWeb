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
import java.util.stream.Collectors;

@WebServlet("/historial")
public class HistorialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String realPath = getServletContext().getRealPath("/");

        // Leer todas las rutinas y filtrar por el ID del usuario
        List<RutinaGuardada> todasLasRutinas = JsonUtil.leerRutinasGuardadas(realPath);
        List<RutinaGuardada> rutinasDelUsuario = todasLasRutinas.stream()
                .filter(rg -> rg.getUsuarioId() == usuario.getId())
                .sorted((r1, r2) -> r2.getFechaGuardada().compareTo(r1.getFechaGuardada())) // Ordenar por fecha, más reciente primero
                .collect(Collectors.toList());

        request.setAttribute("historialRutinas", rutinasDelUsuario);
        request.getRequestDispatcher("historial.jsp").forward(request, response);
    }
}
