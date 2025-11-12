# Instrucciones para la Configuración de la Base de Datos MySQL

A continuación se detallan los pasos para configurar y utilizar la base de datos MySQL con el proyecto MyGymJavaWeb.

## 1. Requisitos Previos

Necesitarás tener instalado lo siguiente:

1.  **Servidor de MySQL:** Puedes descargarlo desde la [página oficial de MySQL](https://dev.mysql.com/downloads/mysql/). Durante la instalación, se te pedirá que establezcas una contraseña para el usuario `root`. **Recuerda esta contraseña.**
2.  **MySQL Connector/J:** Es el controlador JDBC que permite a las aplicaciones Java conectarse a una base de datos MySQL.
    *   Descarga el archivo `.jar` desde [aquí](https://dev.mysql.com/downloads/connector/j/).
    *   Copia el archivo `.jar` descargado (por ejemplo, `mysql-connector-j-8.x.x.jar`) en el directorio `MyGymJavaWeb/web/WEB-INF/lib/` de tu proyecto.
    *   **Importante:** También debes añadirlo a la ruta de compilación de tu proyecto en NetBeans. Haz clic derecho en la carpeta "Libraries" de tu proyecto, selecciona "Add JAR/Folder" y busca el archivo `.jar` que acabas de copiar.

## 2. Creación de la Base de Datos y las Tablas

1.  Abre tu cliente de MySQL (como MySQL Workbench o la línea de comandos de MySQL).
2.  Ejecuta el siguiente script SQL para crear la base de datos y las tablas necesarias.

```sql
CREATE DATABASE IF NOT EXISTS mygym_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mygym_db;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    fecha_nacimiento VARCHAR(20),
    genero VARCHAR(20),
    altura DOUBLE DEFAULT 0.0,
    peso DOUBLE DEFAULT 0.0,
    experiencia VARCHAR(50),
    objetivo VARCHAR(50),
    dias_disponibles INT DEFAULT 0,
    prioridad_muscular VARCHAR(50)
);

-- Tabla de Ejercicios
-- (Esta tabla debe ser poblada con los datos de ejercicios que tenías en `ejercicios.json`)
CREATE TABLE IF NOT EXISTS ejercicios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    grupo_muscular VARCHAR(100),
    dificultad VARCHAR(50),
    imagen_url VARCHAR(255)
);

-- Tabla de Rutinas Guardadas
CREATE TABLE IF NOT EXISTS rutinas_guardadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    fecha_guardada DATE NOT NULL,
    estado VARCHAR(50) NOT NULL,
    rutina_json TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Ejemplo de cómo insertar datos en la tabla de ejercicios:
INSERT INTO ejercicios (nombre, descripcion, grupo_muscular, dificultad, imagen_url) VALUES
('Press de Banca', 'Acuéstate en un banco plano, baja la barra hasta el pecho y luego empújala hacia arriba.', 'Pecho', 'Intermedio', 'images/press-banca.jpg'),
('Sentadillas', 'Baja las caderas desde una posición de pie y luego vuelve a levantarte.', 'Piernas', 'Intermedio', 'images/sentadillas.jpg');
-- ... (añade aquí el resto de tus ejercicios)

```

## 3. Configuración de la Conexión en la Aplicación

Los detalles de la conexión a la base de datos se encuentran en la clase `DatabaseUtil.java` (`MyGymJavaWeb/src/java/com/agym/util/DatabaseUtil.java`).

```java
public class DatabaseUtil {
    // CAMBIA ESTOS VALORES PARA QUE COINCIDAN CON TU CONFIGURACIÓN
    private static final String URL = "jdbc:mysql://localhost:3306/mygym_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // <-- ¡¡CAMBIA ESTA CONTRASEÑA!!
    // ...
}
```

-   **URL:** Asegúrate de que el puerto (`3306`) y el nombre de la base de datos (`mygym_db`) son correctos.
-   **USER:** El usuario de tu base de datos (normalmente `root` para desarrollo local).
-   **PASSWORD:** **La contraseña que estableciste** durante la instalación de MySQL.

## 4. Cómo Leer y Escribir en la Base de Datos (Explicación del Código)

Toda la lógica para interactuar con la base de datos se ha centralizado en las clases **DAO (Data Access Object)** dentro del paquete `com.agym.util`. Ya no necesitas interactuar con los archivos JSON.

### `UsuarioDAO.java`

-   **`crearUsuario(Usuario usuario)`:** Inserta un nuevo usuario en la tabla `usuarios`.
-   **`buscarUsuarioPorEmail(String email)`:** Busca un usuario por su email. Devuelve un objeto `Usuario` si lo encuentra, o `null` si no.
-   **`actualizarUsuario(Usuario usuario)`:** Actualiza los datos de un usuario existente.

**Ejemplo de uso (como en `RegistroServlet.java`):**

```java
// Se crea una instancia del DAO
UsuarioDAO usuarioDAO = new UsuarioDAO();

// Se comprueba si el usuario ya existe
if (usuarioDAO.buscarUsuarioPorEmail("test@test.com") != null) {
    // El usuario ya existe...
} else {
    // Se crea un nuevo objeto Usuario y se guarda
    Usuario nuevo = new Usuario();
    nuevo.setEmail("test@test.com");
    // ... (se establecen los demás datos)
    usuarioDAO.crearUsuario(nuevo);
}
```

### `EjercicioDAO.java`

-   **`obtenerTodosLosEjercicios()`:** Devuelve una lista con todos los ejercicios de la tabla `ejercicios`.

### `RutinaDAO.java`

-   **`guardarRutina(RutinaGuardada rutina)`:** Guarda una nueva rutina en la tabla `rutinas_guardadas`.
-   **`obtenerRutinasPorUsuario(int usuarioId)`:** Devuelve una lista de todas las rutinas guardadas por un usuario específico.
-   **`actualizarEstadoRutina(long id, String estado)`:** Cambia el estado de una rutina (por ejemplo, a "Completada").

Con esta nueva estructura, si en el futuro decides cambiar de MySQL a otra base de datos, solo necesitarías modificar las clases DAO y `DatabaseUtil.java`, sin tener que tocar la lógica de los servlets.
