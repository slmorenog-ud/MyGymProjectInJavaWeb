package com.agym.controlador;

import com.agym.modelo.Usuario;
import com.agym.util.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet que gestiona el registro de nuevos usuarios.
 * Su única responsabilidad es procesar la solicitud de registro.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga del registro.
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Procesa la solicitud POST para el registro de usuarios.
     */
    // Principio: SRP / SoC
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String genero = request.getParameter("genero");

        try {
            if (usuarioDAO.buscarUsuarioPorEmail(email) != null) {
                // User already exists
                response.sendRedirect("register.html?error=emailExists");
                return;
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password); // TODO: Hash passwords
            nuevoUsuario.setFechaNacimiento(fechaNacimiento);
            nuevoUsuario.setGenero(genero);

            usuarioDAO.crearUsuario(nuevoUsuario);

            response.sendRedirect("login.html");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error de base de datos al procesar el registro", e);
        }
    }
}
