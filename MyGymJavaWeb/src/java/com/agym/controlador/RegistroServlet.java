package com.agym.controlador;

import com.agym.modelo.Usuario;
import com.agym.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String genero = request.getParameter("genero");

        String realPath = getServletContext().getRealPath("/");

        try {
            List<Usuario> usuarios = JsonUtil.leerUsuarios(realPath);

            // Verificar si el email ya existe
            for (Usuario u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    // Aquí podríamos enviar un mensaje de error a la vista
                    response.sendRedirect("register.html"); // Redirección simple por ahora
                    return;
                }
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(usuarios.size() + 1); // ID simple basado en el tamaño de la lista
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password); // En una app real, aquí se debería hashear la contraseña
            nuevoUsuario.setFechaNacimiento(fechaNacimiento);
            nuevoUsuario.setGenero(genero);

            usuarios.add(nuevoUsuario);
            JsonUtil.escribirUsuarios(usuarios, realPath);

            response.sendRedirect("login.html");

        } catch (IOException e) {
            // Manejo de errores
            e.printStackTrace();
            throw new ServletException("Error al procesar el registro", e);
        }
    }
}
