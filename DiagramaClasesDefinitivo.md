```mermaid
classDiagram
    direction TB

    subgraph "1. Presentation Layer (Servlets)"
        class HttpServlet { <<Abstract>> }
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
        class Usuario
        class Rutina
        class DiaRutina { <<ValueObject>> }
        class Ejercicio
        class RutinaGuardada
    end

    ' --- Relationships ---

    ' Presentation -> Logic
    GenerarRutinaServlet ..> GeneradorRutinaFactory : "uses"

    ' Logic -> Logic
    GeneradorRutinaFactory ..> GeneradorRutinaBase : "creates"

    ' Presentation -> Data/Utility
    RegistroServlet ..> JsonUtil : "uses for persistence"
    LoginServlet ..> JsonUtil : "uses for authentication"
    GenerarRutinaServlet ..> JsonUtil : "uses to read/write"
    GuardarRutinaServlet ..> JsonUtil : "uses for persistence"
    HistorialServlet ..> JsonUtil : "uses to read"
    MarcarCompletadaServlet ..> JsonUtil : "uses to update"

    ' Presentation -> Model
    GenerarRutinaServlet ..> Usuario : "updates"
    GuardarRutinaServlet ..> RutinaGuardada : "creates"
    HistorialServlet ..> RutinaGuardada : "reads"
    MarcarCompletadaServlet ..> RutinaGuardada : "updates"

    ' Logic -> Model
    GeneradorRutinaBase ..> Usuario : "reads data from"
    GeneradorRutinaBase ..> Ejercicio : "selects"
    GeneradorRutinaBase ..> Rutina : "creates & populates"

    ' Model -> Model (Composition & Aggregation)
    Rutina "1" *-- "1..*" DiaRutina : "is composed of"
    DiaRutina "1" *-- "1..*" Ejercicio : "is composed of"
    RutinaGuardada o-- "1" Rutina : "aggregates"
    RutinaGuardada "1" -- "1" Usuario : "belongs to"

```
