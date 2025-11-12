package com.agym.controlador;

import com.agym.modelo.Usuario;
import com.agym.util.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet que maneja la autenticación de usuarios.
 * Su única responsabilidad es procesar el inicio de sesión.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga del login.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Procesa la solicitud POST para la autenticación de usuarios.
     */
    // Principio: SRP / SoC
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorEmail(email);

            if (usuario != null && usuario.getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendRedirect("login.html?error=true");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error de base de datos al procesar el login", e);
        }
    }
}
