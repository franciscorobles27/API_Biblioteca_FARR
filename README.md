# API REST Biblioteca — API_Biblioteca_FARR

Proyecto académico de Programación Orientada a Objetos. Es una API REST para administrar **libros, usuarios y préstamos** de una biblioteca. Fue desarrollada en Java con Spring Boot y persiste los datos en MySQL. **No implementa inicio de sesión.**

## Funcionalidades

- **Libros:** registrar, listar, consultar por ISBN, buscar por autor, actualizar y eliminar (si no tienen historial de préstamos).
- **Usuarios:** registrar, listar, consultar, actualizar y eliminar (si no tienen historial de préstamos).
- **Préstamos:** registrar, listar todos o solo activos, consultar por identificador y devolver un libro. Al prestar, los ejemplares disponibles disminuyen en uno; al devolver, aumentan en uno.
- **Errores:** respuestas HTTP para datos inválidos, registros inexistentes y conflictos.

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Maven (incluye Maven Wrapper: `mvnw.cmd`)
- Spring Web, Spring Data JPA / Hibernate
- MySQL 8
- Postman, para probar los endpoints

## Requisitos

- JDK 21 instalado.
- MySQL Server iniciado y credenciales de un usuario con permisos sobre la base de datos.
- Puerto `1500` disponible (o definir otro con `PORT`).
- Conexión a Internet la primera vez que Maven descargue dependencias.

## Instalación y ejecución en Windows

1. Abrir MySQL Workbench y ejecutar:

   ```sql
   CREATE DATABASE IF NOT EXISTS libreria;
   ```

2. Abrir una terminal **PowerShell** en la carpeta que contiene `pom.xml`.
3. Definir la conexión a MySQL para esta sesión de PowerShell:

   ```powershell
   $env:DB_USER = "root"
   $env:DB_PASSWORD = Read-Host "Contraseña de MySQL"
   ```

   Escribir la contraseña local al solicitarla. **No guardarla en archivos del repositorio.**

4. Compilar e iniciar:

   ```powershell
   .\mvnw.cmd clean compile
   .\mvnw.cmd spring-boot:run
   ```

5. La API queda disponible en `http://localhost:1500`. La dirección raíz `/` puede mostrar `404`; para comprobar el servicio, utilizar **`http://localhost:1500/libros`**.

La configuración usa `spring.jpa.hibernate.ddl-auto=update`, por lo que Hibernate puede crear o actualizar las tablas al arrancar. No se requiere importar un archivo SQL de tablas para una base de datos nueva.

### Variables de entorno

| Variable | Función | Valor predeterminado |
|---|---|---|
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | Vacía (definir localmente) |
| `DB_URL` | Cadena de conexión JDBC | `jdbc:mysql://localhost:3306/libreria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
| `PORT` | Puerto HTTP de Spring Boot | `1500` |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/libros` | Listar libros |
| GET | `/libros/{isbn}` | Consultar libro por ISBN |
| GET | `/libros/buscar?autor=garcia` | Buscar libros por autor |
| POST | `/libros` | Registrar libro |
| PUT | `/libros/{isbn}` | Actualizar libro |
| DELETE | `/libros/{isbn}` | Eliminar libro sin historial de préstamos |
| GET | `/usuarios` | Listar usuarios |
| GET | `/usuarios/{id}` | Consultar usuario |
| POST | `/usuarios` | Registrar usuario |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| DELETE | `/usuarios/{id}` | Eliminar usuario sin historial de préstamos |
| GET | `/prestamos` | Listar préstamos |
| GET | `/prestamos/activos` | Listar préstamos pendientes de devolución |
| GET | `/prestamos/{id}` | Consultar préstamo |
| POST | `/prestamos` | Registrar préstamo |
| PUT | `/prestamos/{id}/devolver` | Registrar devolución |

## Ejemplos de peticiones y respuestas

En Postman, configurar `Body → raw → JSON` para peticiones con cuerpo. Usar la URL base `http://localhost:1500`.

### 1. Registrar un libro

**POST** `/libros`

```json
{
  "isbn": 9786073151221,
  "titulo": "El Principito",
  "autor": "Antoine de Saint-Exupéry",
  "editorial": "Editorial de ejemplo",
  "añoPublicacion": "1943",
  "numejemplares": 5
}
```

**Ejemplo de respuesta, HTTP 201 Created** (para ese registro):

```json
{
  "isbn": 9786073151221,
  "titulo": "El Principito",
  "autor": "Antoine de Saint-Exupéry",
  "editorial": "Editorial de ejemplo",
  "añoPublicacion": "1943",
  "numejemplares": 5
}
```

**GET** `/libros/9786073151221` recupera el libro registrado. **PUT** `/libros/9786073151221` admite el mismo JSON con cambios en sus datos. **DELETE** `/libros/9786073151221` elimina el libro únicamente si no tiene historial de préstamos.

> Importante: los nombres de las propiedades son exactamente `añoPublicacion` y `numejemplares`, tal como están en `LibroModel.java`.

### 2. Registrar un usuario

**POST** `/usuarios`

```json
{
  "nombre": "Francisco",
  "apellidos": "Robles",
  "correo": "francisco.prueba@example.com",
  "contrasena": "ClavePrueba123!",
  "rol": "SOCIO",
  "estado": "ACTIVO",
  "telefono": "5551234567",
  "direccion": "Calle Principal 123",
  "fechaNacimiento": "2002-05-15"
}
```

**Respuesta, HTTP 201 Created:** incluye un `id` generado por MySQL y `fechaCreacion` asignada automáticamente. El valor de `contrasena` no se muestra en la respuesta JSON. Los datos son **ficticios**, salvo el nombre utilizado como ejemplo.

### 3. Registrar un préstamo

Registrar primero un libro y un usuario. Sustituir los IDs por los que realmente existan en la base de datos.

**POST** `/prestamos`

```json
{
  "isbn": 9786073151221,
  "usuarioId": 1
}
```

**Respuesta, HTTP 201 Created:** objeto con `id`, `fechaPrestamo`, `fechaDevolucion: null`, `libro` y `usuario`. Si hay 5 ejemplares disponibles, al prestar quedarán 4.

### 4. Devolver el préstamo

**PUT** `/prestamos/1/devolver` — **sin Body**.

**Respuesta, HTTP 200 OK:** devuelve el préstamo con su `fechaDevolucion` y el número de ejemplares restaurado, por ejemplo de 4 a 5.

## Validaciones y códigos HTTP

| Estado | Significado / situación |
|---|---|
| `200 OK` | Consulta o actualización correcta |
| `201 Created` | Libro, usuario o préstamo creado |
| `204 No Content` | Eliminación correcta, sin cuerpo de respuesta |
| `400 Bad Request` | Datos obligatorios incorrectos o usuario inactivo |
| `404 Not Found` | Libro, usuario o préstamo inexistente |
| `409 Conflict` | ISBN/correo duplicado, falta de ejemplares, devolución duplicada o intento de borrar un registro con historial de préstamos |

## Estructura del código

```text
src/main/java/com/api/libreria/
├── controllers/    # Endpoints HTTP
├── models/         # Entidades de MySQL
├── repositories/   # Consultas mediante Spring Data JPA
├── services/       # Reglas de negocio
└── LibreriaApplication.java
```

Esta organización sigue el ejemplo de clase con separación de responsabilidades entre controladores, modelos, repositorios y servicios.

## Pruebas realizadas

Durante el desarrollo, en Postman se comprobaron estos resultados:

| Petición | Resultado observado |
|---|---|
| POST `/libros` | `201 Created` |
| GET `/libros/{isbn}` | `200 OK` |
| PUT `/libros/{isbn}` | `200 OK` |
| DELETE `/libros/{isbn}` (libro de prueba) | `204 No Content` |
| GET del libro de prueba eliminado | `404 Not Found` |
| POST `/usuarios` | `201 Created` |
| POST `/prestamos` | `201 Created`, ejemplares de 5 a 4 |
| PUT `/prestamos/1/devolver` | `200 OK`, ejemplares de 4 a 5 |

**Comprobación de la versión final `API_Biblioteca_FARR` (13/10/2026):** `clean compile` completó con `BUILD SUCCESS`, Spring Boot inició en el puerto 1500 y una consulta GET al libro registrado devolvió `200 OK`. Las demás pruebas enumeradas se realizaron previamente durante el desarrollo de Biblioteca; no se afirma aquí que todas hayan sido repetidas en la última carpeta.

Se incluye `Biblioteca.postman_collection.json` para importar las solicitudes de ejemplo en Postman. Las capturas se presentan por separado en el PDF de entrega.

## Notas importantes de seguridad y base de datos

- La práctica **no incluye login**.
- El campo `contrasena` se oculta en las respuestas JSON, **pero no está cifrado en la implementación mostrada**. No usar contraseñas reales ni información personal sensible.
- No subir contraseñas, respaldos privados, credenciales o archivos de compilación (`target/`) a GitHub.
- El `isbn` se asigna en las peticiones y es la clave primaria del libro; **no se genera automáticamente**. En instalaciones existentes creadas con una versión anterior puede ser necesario quitar `AUTO_INCREMENT` después de respaldar la base de datos y cuidar las relaciones con préstamos. En una base nueva, utilizar directamente el modelo actual.
- En un entorno real, la seguridad de usuarios y la gestión de concurrencia requerirían medidas adicionales.
