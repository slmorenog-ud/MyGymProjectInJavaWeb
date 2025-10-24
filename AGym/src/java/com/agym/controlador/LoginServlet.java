package com.agym.controlador;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String realPath = getServletContext().getRealPath("/");

        try {
            List<Usuario> usuarios = JsonUtil.leerUsuarios(realPath);

            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equalsIgnoreCase(email) && usuario.getPassword().equals(password)) {
                    // Credenciales correctas
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario);
                    response.sendRedirect("dashboard.jsp");
                    return;
                }
            }

            // Si no se encuentra el usuario, redirigir al login
            response.sendRedirect("login.html");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar el login", e);
        }
    }
}
