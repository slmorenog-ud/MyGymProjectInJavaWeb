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

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String genero = request.getParameter("genero");

        String realPath = getServletContext().getRealPath("/");

        try {
            List<Usuario> usuarios = JsonUtil.leerUsuarios(realPath);

            for (Usuario u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    response.sendRedirect("register.html");
                    return;
                }
            }

            int maxId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0);

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(maxId + 1);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password); // TODO: Hash passwords
            nuevoUsuario.setFechaNacimiento(fechaNacimiento);
            nuevoUsuario.setGenero(genero);

            usuarios.add(nuevoUsuario);
            JsonUtil.escribirUsuarios(usuarios, realPath);

            response.sendRedirect("login.html");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar el registro", e);
        }
    }
}
