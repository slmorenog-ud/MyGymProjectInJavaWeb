¡Absolutamente! Aquí tienes una versión más completa y formal del diagrama de casos de uso, creada con la sintaxis de **PlantUML**.

Esta versión es más detallada porque:
1.  Utiliza las relaciones `<<include>>` y `<<extend>>` de UML de una manera más estricta para definir flujos obligatorios y opcionales.
2.  Desglosa el proceso de "Generar Rutina" en "Generar" y "Visualizar" para aclarar dónde ocurren las extensiones.
3.  Añade notas contextuales para explicar las interacciones clave.

Aquí tienes el código PlantUML. Puedes compilarlo en cualquier herramienta compatible (como el servidor online de PlantUML, o en editores como VS Code con la extensión de PlantUML).

```plantuml
@startuml
!theme vibrant

' Dirección del diagrama de izquierda a derecha para mayor claridad
left to right direction

' Definición del actor principal
actor "Usuario del Gimnasio" as user

' Definición del límite del sistema
rectangle "Sistema MyGymJavaWeb" {

    ' Casos de uso de Gestión de Cuenta
    usecase "Registrarse en el sistema" as UC_Register
    usecase "Iniciar Sesión" as UC_Login
    usecase "Cerrar Sesión" as UC_Logout

    ' Casos de uso principales de la aplicación
    usecase "Actualizar Datos de Perfil" as UC_UpdateProfile
    usecase "Generar Rutina" as UC_Generate
    usecase "Visualizar Rutina Generada" as UC_ViewRoutine

    ' Caso de uso opcional que extiende la visualización
    usecase "Guardar Rutina en Historial" as UC_Save

    ' Casos de uso del historial
    usecase "Consultar Historial de Rutinas" as UC_ViewHistory
    usecase "Marcar Rutina como Completada" as UC_Complete
}

' --- Asociaciones primarias del actor con el sistema ---
user -- UC_Register
user -- UC_Login
user -- UC_Logout
user -- UC_UpdateProfile
user -- UC_Generate
user -- UC_ViewHistory

' --- Relaciones entre Casos de Uso ---

' Relaciones de Inclusión (<<include>>): Indican que un caso de uso
' OBLIGATORIAMENTE invoca a otro. La autenticación es una precondición.
UC_UpdateProfile ..> UC_Login : <<include>>
UC_Generate ..> UC_Login : <<include>>
UC_ViewHistory ..> UC_Login : <<include>>
UC_Logout ..> UC_Login : <<include>>

' Relación de Flujo: Un caso de uso lleva directamente a otro.
' Después de generar una rutina, el sistema siempre la muestra.
UC_Generate --> UC_ViewRoutine

' Relaciones de Extensión (<<extend>>): Indican funcionalidad OPCIONAL
' que puede ser invocada desde otro caso de uso.
UC_Save ..> UC_ViewRoutine : <<extend>>
UC_Complete ..> UC_ViewHistory : <<extend>>

' --- Notas aclaratorias para mayor detalle ---
note right of UC_Save
  El usuario solo puede guardar la rutina
  desde la pantalla de visualización,
  siendo una acción opcional.
end note

note right of UC_Complete
  El usuario puede marcar una rutina como
  completada desde la lista de su historial,
  siempre que esta no lo esté ya.
end note

@enduml
```

### Explicación de esta Versión "Más Completa":

*   **`include` vs `extend`:** Este diagrama distingue claramente entre `<<include>>` (una dependencia obligatoria, como tener que iniciar sesión para generar una rutina) y `<<extend>>` (una funcionalidad opcional, como decidir guardar una rutina después de verla). Esto es mucho más preciso desde el punto de vista de UML.
*   **Flujo Lógico:** La flecha sólida `UC_Generate --> UC_ViewRoutine` muestra una secuencia directa e inevitable: el sistema siempre te muestra la rutina justo después de crearla. Esto aclara el flujo de la aplicación.
*   **Contexto con Notas:** Las notas explican en lenguaje natural las reglas de negocio o las condiciones bajo las cuales se activan los casos de uso opcionales, añadiendo una capa de detalle que el diagrama por sí solo no puede transmitir.

Esta versión en PlantUML es un documento de análisis de requisitos mucho más robusto y formal.
