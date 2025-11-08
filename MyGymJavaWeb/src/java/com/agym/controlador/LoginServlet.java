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

/**
 * Servlet que maneja la autenticación de usuarios.
 * Su única responsabilidad es procesar el inicio de sesión.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga del login.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para la autenticación de usuarios.
     */
    // Principio: SRP / SoC
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
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario);
                    response.sendRedirect("dashboard.jsp");
                    return;
                }
            }
            response.sendRedirect("login.html?error=true");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar el login", e);
        }
    }
}
