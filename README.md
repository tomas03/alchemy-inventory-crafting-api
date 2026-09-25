Alchemy & Inventory Crafting REST API

Backend modular de alto rendimiento para gestión de inventarios, apilamiento de objetos (item stacking) y motor transaccional de crafteo/alquimia. Desarrollado con **Java 21** y **Spring Boot**.

Características Principales

    Gestión de Inventarios y Slots: Lógica de apilado automático respetando cantidades máximas y slots independientes.

    Motor de Crafteo Transaccional: Validación atómica de ingredientes y creación de resultados bajo @Transactional (rollback automático ante inconsistencias).

    Manejo Global de Excepciones: Respuestas HTTP limpias y estructuradas (GlobalExceptionHandler) para errores de dominio (400 Bad Request, 404 Not Found).

    Documentación Interactiva: Integración completa con Swagger / OpenAPI UI para pruebas directas en el navegador.

    Pruebas Unitarias Aisladas: Cobertura de lógica de negocio crítica con JUnit 5
    y Mockito.
    
Tecnologías Utilizadas

    Lenguaje: Java 21

    Framework: Spring Boot 3.x / 4.x

    Persistencia: Spring Data JPA / Hibernate ORM

    Base de Datos: MySQL

    Herramientas: Lombok, SpringDoc OpenAPI (Swagger UI)

    Testing: JUnit 5, Mockito
    
Arquitectura del Proyecto

```text
src/main/java/com/RavenDev/inventory_crafting_api/
├── config/         # Configuración de OpenAPI / Swagger
├── controller/     # Endpoints REST (Item, Inventory, Crafting)
├── domain/         # Entidades JPA (Inventory, InventorySlot, Item, Recipe, RecipeIngredient)
├── dto/            # Data Transfer Objects (Records de Java)
├── exception/      # Excepciones custom y GlobalExceptionHandler
├── repository/     # Interfaces de Spring Data JPA
└── service/        # Lógica de negocio (InventoryService, CraftingService)
```

Configuración y Variables de Entorno

La aplicación utiliza variables de entorno con valores por defecto para no exponer credenciales locales:
DB_URL = URL de conexión JDBC a MySQL EJ: jdbc:mysql://localhost:3306/inventory_db
DB_USERNAME = Usuario de base de datos EJ: root
DB_PASSWORD = Contraseña de base de datos EJ:(vacío o local)

Instalación y Ejecución

1. Clonar el repositorio
git clone https://github.com/RavenDev/alchemy-inventory-crafting-api.git
cd alchemy-inventory-crafting-api
2. Configurar la base de datos
Asegurate de tener MySQL corriendo y creada la base de datos:
CREATE DATABASE inventory_db;
3. Ejecutar pruebas unitarias
./mvnw test
4. Iniciar la aplicación
./mvnw spring-boot:run

Documentación de la API (Swagger UI)

Con la aplicación en ejecución, accedé a la documentación interactiva en:
    Swagger UI: http://localhost:8080/swagger-ui.html
    OpenAPI Specs: http://localhost:8080/v3/api-docs
    
Principales Endpoints REST

Items

    POST /api/items - Crear un nuevo ítem/material.
    GET /api/items - Listar todos los ítems.

Inventarios

    POST /api/inventories - Crear un inventario para un usuario.
    GET /api/inventories/{id} - Obtener el inventario con sus slots y contenidos.
    POST /api/inventories/{id}/items - Agregar ítems a un inventario (apilado automático).

Crafteo / Alquimia

    POST /api/recipes - Registrar una nueva receta con ingredientes y producto final.
    POST /api/crafting/inventories/{inventoryId}/craft/{recipeId} - Ejecutar crafteo transaccional.
    
Autor

-Tomas Fantinel
