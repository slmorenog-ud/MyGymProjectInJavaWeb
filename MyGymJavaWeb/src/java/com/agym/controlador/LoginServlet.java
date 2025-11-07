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
 * <p>
 * Su responsabilidad se centra en verificar las credenciales (email y contraseña)
 * contra la lista de usuarios persistidos. Si la autenticación es exitosa,
 * crea una sesión HTTP para el usuario.
 * </p>
 * <p>
 * <b>Principios de diseño aplicados:</b>
 * - <b>Principio de Responsabilidad Única (SRP):</b> Esta clase tiene una única
 *   responsabilidad: la lógica de inicio de sesión. No se ocupa del registro
 *   de usuarios ni de ninguna otra funcionalidad de la aplicación.
 * </p>
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     * Procesa las solicitudes HTTP <code>POST</code> para la autenticación de usuarios.
     *
     * @param request  objeto que contiene la solicitud del cliente.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException si ocurre un error de entrada/salida.
     */
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

            // Si no se encuentra el usuario, redirigir al login con un mensaje de error
            response.sendRedirect("login.html?error=true");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar el login", e);
        }
    }
}
