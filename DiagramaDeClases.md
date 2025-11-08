```mermaid
classDiagram
    direction TB

    subgraph "Controlador (Capa de Presentación)"
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

        HttpServlet <|-- RegistroServlet
        HttpServlet <|-- LoginServlet
        HttpServlet <|-- GenerarRutinaServlet
        HttpServlet <|-- GuardarRutinaServlet
        HttpServlet <|-- HistorialServlet
        HttpServlet <|-- MarcarCompletadaServlet
        HttpServlet <|-- LogoutServlet
    end

    subgraph "Lógica de Negocio"
        class GeneradorRutinaFactory {
            <<Factory>>
            +getGenerador(int) GeneradorRutinaBase
        }
        class GeneradorRutinaBase {
            <<Abstract>>
            +generar(Usuario, List~Ejercicio~) Rutina
            #construirRutina(Rutina, ...) void
        }
        note for GeneradorRutinaBase "generar() es el <<Template Method>>"
        class GeneradorRutina2Dias
        class GeneradorRutina3Dias
        class GeneradorRutina4Dias

        GeneradorRutinaBase <|-- GeneradorRutina2Dias
        GeneradorRutinaBase <|-- GeneradorRutina3Dias
        GeneradorRutinaBase <|-- GeneradorRutina4Dias
    end

    subgraph "Utilidades y Acceso a Datos"
        class JsonUtil {
            <<Utility>>
            +leerUsuarios() List~Usuario~
            +escribirUsuarios(List~Usuario~)
            +leerEjercicios() List~Ejercicio~
            +escribirRutinasGuardadas(...)
        }
    end

    subgraph "Modelo (Capa de Datos)"
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
    end

    %% --- Relaciones Clave de Diseño ---

    %% Presentation -> Logic (DIP & Factory Usage)
    GenerarRutinaServlet ..> GeneradorRutinaBase : "<< uses >> (DIP)"
    GenerarRutinaServlet ..> GeneradorRutinaFactory : "<< uses >>"

    %% Logic -> Logic (Factory Creates Concrete Implementations)
    GeneradorRutinaFactory ..> GeneradorRutina2Dias : "<< creates >>"
    GeneradorRutinaFactory ..> GeneradorRutina3Dias : "<< creates >>"
    GeneradorRutinaFactory ..> GeneradorRutina4Dias : "<< creates >>"

    %% Generic Dependencies (Controller -> Utility/Model)
    RegistroServlet --> JsonUtil
    LoginServlet --> JsonUtil
    GenerarRutinaServlet --> JsonUtil
    GuardarRutinaServlet --> JsonUtil
    HistorialServlet --> JsonUtil
    MarcarCompletadaServlet --> JsonUtil
    GenerarRutinaServlet --> Usuario

    %% Logic -> Model
    GeneradorRutinaBase --> Usuario
    GeneradorRutinaBase --> Ejercicio
    GeneradorRutinaBase --> Rutina

    %% Model -> Model (Composition & Aggregation)
    Rutina "1" *-- "1..*" DiaRutina : contiene
    DiaRutina "1" *-- "1..*" Ejercicio : contiene
    RutinaGuardada o-- "1" Rutina : guarda
    RutinaGuardada --> Usuario : "pertenece a"

```
