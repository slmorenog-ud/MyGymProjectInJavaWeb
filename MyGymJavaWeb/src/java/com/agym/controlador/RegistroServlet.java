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
 * Servlet encargado de gestionar el registro de nuevos usuarios.
 * <p>
 * Su única responsabilidad es recibir los datos del formulario de registro,
 * validar la información (como la existencia del email), crear un nuevo objeto
 * {@link Usuario}, y persistirlo utilizando {@link JsonUtil}.
 * </p>
 * <p>
 * <b>Principios de diseño aplicados:</b>
 * - <b>Principio de Responsabilidad Única (SRP):</b> Esta clase tiene una sola
 *   razón para cambiar: la lógica de registro de usuarios. No se encarga del
 *   login, la generación de rutinas ni otras funcionalidades.
 * </p>
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    /**
     * Procesa las solicitudes HTTP <code>POST</code> para el registro de usuarios.
     *
     * @param request  objeto que contiene la solicitud del cliente.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException si ocurre un error de entrada/salida.
     */
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

            // Crear nuevo usuario y asignar un ID único
            int maxId = usuarios.stream()
                                .mapToInt(Usuario::getId)
                                .max()
                                .orElse(0);

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(maxId + 1);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            // TODO: ¡Vulnerabilidad de seguridad! Las contraseñas se guardan en texto plano.
            // Se debe implementar un hash de contraseñas (ej. jBcrypt) antes de pasar a producción.
            nuevoUsuario.setPassword(password);
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
