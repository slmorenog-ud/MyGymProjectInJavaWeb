package com.agym.controlador;

import com.agym.modelo.Usuario;
import com.agym.util.DatabaseUtil;
import com.agym.util.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet que gestiona el registro de nuevos usuarios.
 * Su única responsabilidad es procesar la solicitud de registro.
 *
 * --- Principios de Diseño Clave ---
 * SRP (Single Responsibility Principle): Solo se encarga del registro.
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para el registro de usuarios.
     */
    // Principio: SRP / SoC
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DatabaseUtil dbUtil = new DatabaseUtil();
        if (dbUtil.getConexion() == null) {
            response.sendRedirect("error.jsp?msg=" + DatabaseUtil.getMensaje());
            return;
        }

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String genero = request.getParameter("genero");

        UsuarioDAO usuarioDAO = new UsuarioDAO(dbUtil.getConexion());

        // Verificar si el usuario ya existe
        if (usuarioDAO.getUsuarioPorEmail(email) != null) {
            dbUtil.desconectar();
            response.sendRedirect("register.html?error=email");
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password); // TODO: Hash passwords
        nuevoUsuario.setFechaNacimiento(fechaNacimiento);
        nuevoUsuario.setGenero(genero);

        usuarioDAO.crearUsuario(nuevoUsuario);

        dbUtil.desconectar();

        response.sendRedirect("login.html");
    }
}
