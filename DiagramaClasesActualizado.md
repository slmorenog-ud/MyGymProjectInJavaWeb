```mermaid
classDiagram
    direction TB

    subgraph "Servlet Layer"
        class HttpServlet {
            <<Abstract>>
        }
        class GenerarRutinaServlet {
            +doPost(req, res) void
        }
        HttpServlet <|-- GenerarRutinaServlet
    end

    subgraph "Business Logic Layer (New)"
        class GeneradorRutinaFactory {
            <<Factory>>
            +getGenerador(int dias) GeneradorRutinaBase
        }

        class GeneradorRutinaBase {
            <<Abstract>>
            +generar(Usuario, List~Ejercicio~) Rutina
            #construirRutina(...) void
            #calcularEdad(String) int
            #getSeriesYRepeticionesPorObjetivo(String, String) String
        }

        class GeneradorRutina2Dias {
            #construirRutina(...) void
        }
        class GeneradorRutina3Dias {
            #construirRutina(...) void
        }
        class GeneradorRutina4Dias {
            #construirRutina(...) void
        }
    end

    subgraph "Model Layer"
        class Usuario {
          -int id
          -String nombre
          -int diasDisponibles
          +getters/setters()
        }
        class Rutina {
            -List~DiaRutina~ dias
            +agregarDia(DiaRutina) void
        }
        class DiaRutina {
            <<ValueObject>>
            -List~Ejercicio~ ejercicios
            +agregarEjercicio(Ejercicio) void
        }
        class Ejercicio {
            -int id
            -String nombre
            -String grupoMuscular
        }
    end

    ' --- Relationships ---
    GenerarRutinaServlet ..> GeneradorRutinaFactory : "uses"
    GenerarRutinaServlet ..> Usuario : "updates"

    GeneradorRutinaFactory ..> GeneradorRutinaBase : "creates"
    GeneradorRutinaFactory ..> GeneradorRutina2Dias : "creates"
    GeneradorRutinaFactory ..> GeneradorRutina3Dias : "creates"
    GeneradorRutinaFactory ..> GeneradorRutina4Dias : "creates"

    GeneradorRutinaBase <|-- GeneradorRutina2Dias
    GeneradorRutinaBase <|-- GeneradorRutina3Dias
    GeneradorRutinaBase <|-- GeneradorRutina4Dias

    GeneradorRutinaBase ..> Usuario : "reads data from"
    GeneradorRutinaBase ..> Ejercicio : "uses"
    GeneradorRutinaBase ..> Rutina : "creates"

    Rutina "1" *-- "1..*" DiaRutina : "contiene"
    DiaRutina "1" *-- "1..*" Ejercicio : "contiene"

```
