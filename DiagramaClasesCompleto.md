```mermaid
classDiagram
    direction TB

    subgraph "1. Presentation Layer (Servlets)"
        class HttpServlet {
            <<Abstract>>
        }
        class RegistroServlet { +doPost() }
        class LoginServlet { +doPost() }
        class GenerarRutinaServlet { +doPost() }
        class GuardarRutinaServlet { +doPost() }
        class HistorialServlet { +doGet() }
        class MarcarCompletadaServlet { +doPost() }
        class LogoutServlet { +doPost() }

        HttpServlet <|-- RegistroServlet
        HttpServlet <|-- LoginServlet
        HttpServlet <|-- GenerarRutinaServlet
        HttpServlet <|-- GuardarRutinaServlet
        HttpServlet <|-- HistorialServlet
        HttpServlet <|-- MarcarCompletadaServlet
        HttpServlet <|-- LogoutServlet
    end

    subgraph "2. Business Logic Layer"
        class GeneradorRutinaFactory {
            <<Factory>>
            +getGenerador(int) GeneradorRutinaBase
        }
        class GeneradorRutinaBase {
            <<Abstract>>
            +generar(Usuario, List~Ejercicio~) Rutina
            #construirRutina(...)
        }
        class GeneradorRutina2Dias
        class GeneradorRutina3Dias
        class GeneradorRutina4Dias

        GeneradorRutinaBase <|-- GeneradorRutina2Dias
        GeneradorRutinaBase <|-- GeneradorRutina3Dias
        GeneradorRutinaBase <|-- GeneradorRutina4Dias
    end

    subgraph "3. Data Access & Utility"
        class JsonUtil {
            <<Utility>>
            +leerUsuarios() List~Usuario~
            +escribirUsuarios(List~Usuario~)
            +leerEjercicios() List~Ejercicio~
            +leerRutinasGuardadas() List~RutinaGuardada~
            +escribirRutinasGuardadas(List~RutinaGuardada~)
        }
    end

    subgraph "4. Model Layer"
        class Usuario {
            -int id
            -String nombre
            -String email
            -String password
            -int diasDisponibles
            -String objetivo
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
        }
    end

    ' --- Relationships ---
    GenerarRutinaServlet ..> GeneradorRutinaFactory : uses
    GeneradorRutinaFactory ..> GeneradorRutinaBase : creates

    ' --- Servlet Dependencies ---
    RegistroServlet ..> JsonUtil : uses
    LoginServlet ..> JsonUtil : uses
    GenerarRutinaServlet ..> JsonUtil : uses
    GuardarRutinaServlet ..> JsonUtil : uses
    HistorialServlet ..> JsonUtil : uses
    MarcarCompletadaServlet ..> JsonUtil : uses

    GenerarRutinaServlet ..> Usuario : updates
    GenerarRutinaServlet ..> Rutina : creates
    GuardarRutinaServlet ..> RutinaGuardada : creates
    HistorialServlet ..> RutinaGuardada : reads

    ' --- Logic Dependencies ---
    GeneradorRutinaBase ..> Usuario : reads data from
    GeneradorRutinaBase ..> Ejercicio : selects
    GeneradorRutinaBase ..> Rutina : populates

    ' --- Model Composition ---
    Rutina "1" *-- "1..*" DiaRutina : contains
    DiaRutina "1" *-- "1..*" Ejercicio : contains
    RutinaGuardada o-- "1" Rutina : encapsulates
    RutinaGuardada "1" -- "1" Usuario : belongs to
```
