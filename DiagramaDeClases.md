classDiagram
    direction TB

    class HttpServlet {
        <<Abstract>>
    }
    class RegistroServlet
    class LoginServlet
    class GenerarRutinaServlet
    class GuardarRutinaServlet
    class HistorialServlet
    class MarcarCompletadaServlet
    class LogoutServlet

    class GeneradorRutinaFactory {
        <<Factory>>
        +getGenerador(int) GeneradorRutinaBase
    }
    class GeneradorRutinaBase {
        <<Abstract>>
        +generar(Usuario, List~Ejercicio~) Rutina
        #construirRutina(Rutina, ...) void
    }
    class GeneradorRutina2Dias
    class GeneradorRutina3Dias
    class GeneradorRutina4Dias

    class JsonUtil {
        <<Utility>>
        +leerUsuarios() List~Usuario~
        +escribirUsuarios(List~Usuario~)
        +leerEjercicios() List~Ejercicio~
        +escribirRutinasGuardadas(...)
    }

    class Usuario {
        -int id
        -String nombre
        -String email
        -String password
        -String fechaNacimiento
        -String genero
        -double altura
        -double peso
        -String experiencia
        -String objetivo
        -int diasDisponibles
        -String prioridadMuscular
    }
    class Rutina {
        -List~DiaRutina~ dias
    }
    class DiaRutina {
        <<ValueObject>>
        -String nombre
        -List~Ejercicio~ ejercicios
    }
    class Ejercicio {
        -int id
        -String nombre
        -String grupoMuscular
        -String dificultad
    }
    class RutinaGuardada {
        -long id
        -int usuarioId
        -Date fechaGuardada
        -Rutina rutina
        -String estado
    }

    %% --- Herencia ---
    HttpServlet <|-- RegistroServlet
    HttpServlet <|-- LoginServlet
    HttpServlet <|-- GenerarRutinaServlet
    HttpServlet <|-- GuardarRutinaServlet
    HttpServlet <|-- HistorialServlet
    HttpServlet <|-- MarcarCompletadaServlet
    HttpServlet <|-- LogoutServlet

    GeneradorRutinaBase <|-- GeneradorRutina2Dias
    GeneradorRutinaBase <|-- GeneradorRutina3Dias
    GeneradorRutinaBase <|-- GeneradorRutina4Dias

    %% --- Relaciones Clave de Diseño ---
    GenerarRutinaServlet ..> GenerarRutinaBase : "uses"
    GenerarRutinaServlet ..> GeneradorRutinaFactory : "uses"

    GeneradorRutinaFactory ..> GeneradorRutina2Dias : "creates"
    GeneradorRutinaFactory ..> GeneradorRutina3Dias : "creates"
    GeneradorRutinaFactory ..> GeneradorRutina4Dias : "creates"

    %% --- Dependencias ---
    RegistroServlet --> JsonUtil
    LoginServlet --> JsonUtil
    GenerarRutinaServlet --> JsonUtil
    GuardarRutinaServlet --> JsonUtil
    HistorialServlet --> JsonUtil
    MarcarCompletadaServlet --> JsonUtil
    GenerarRutinaServlet --> Usuario

    GeneradorRutinaBase --> Usuario
    GeneradorRutinaBase --> Ejercicio
    GeneradorRutinaBase --> Rutina

    %% --- Composición y Agregación ---
    Rutina "1" *-- "1..*" DiaRutina
    DiaRutina "1" *-- "1..*" Ejercicio
    RutinaGuardada o-- "1" Rutina
    RutinaGuardada --> Usuario