package com.agym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String BD = "mygym_db";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "admin"; // Cambia esto por tu contraseña
    private static final String URL = "jdbc:mysql://localhost/" + BD;
    private static String mensaje;

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            if (conexion != null) {
                System.out.println("Conexión a base de datos " + BD + " OK");
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
        } catch (ClassNotFoundException e) {
            mensaje = e.getMessage();
        }
        return conexion;
    }

    public static String getMensaje() {
        return mensaje;
    }

    public static void setMensaje(String mensaje) {
        DatabaseUtil.mensaje = mensaje;
    }
}
