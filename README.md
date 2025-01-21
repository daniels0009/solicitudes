# Solicitudes API

## Descripción
Este proyecto implementa una API REST para manejar solicitudes de clientes y estados asociados. La API permite realizar operaciones CRUD sobre las entidades `Cliente`, `Estado` y `Solicitud`. Además, incluye validaciones específicas y se ajusta a las preguntas planteadas en el ejercicio.

---

## Preguntas resueltas

### 1. Consulta que retorna descripciones específicas
**Consulta SQL para obtener las solicitudes que cumplen con los criterios establecidos:**
```sql
SELECT e.descripcion, s.id AS id_solicitud, c.id AS id_cliente
FROM t01_solicitud s
INNER JOIN t02_cliente c ON s.cliente_id = c.id
INNER JOIN t00_estado e ON s.estado_id = e.id_estado
WHERE e.id_estado = 1
  AND c.fecha_nacimiento = '1900-01-01'
  AND s.fecha_ingreso BETWEEN '2000-01-01' AND '2000-12-31';
```
Esta consulta devuelve la descripción del estado, el `id_solicitud` y el `id_cliente` de las solicitudes que cumplen con las condiciones del enunciado.

---

### 2. Función para contar solicitudes por cliente
**Función SQL para retornar el número de solicitudes de un cliente:**
```sql
CREATE OR REPLACE FUNCTION contar_solicitudes_por_cliente(cliente_id BIGINT) RETURNS INTEGER AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM t01_solicitud
        WHERE cliente_id = $1
    );
END;
$$ LANGUAGE plpgsql;
```
Llama a esta función en PostgreSQL usando:
```sql
SELECT contar_solicitudes_por_cliente(1);
```

---

### 3. Estructura de la tabla `t01_solicitud`
**Características de la tabla `t01_solicitud`:**
```sql
CREATE TABLE t01_solicitud (
    id BIGSERIAL PRIMARY KEY,
    estado_id BIGINT NOT NULL,
    fecha_ingreso DATE NOT NULL,
    monto NUMERIC(38, 2) NOT NULL,
    cliente_id BIGINT NOT NULL
);
```

---

### 4. Índice en `id_cliente`
**Índice para optimizar búsquedas:**
```sql
CREATE INDEX idx_solicitud_cliente_id ON t01_solicitud(cliente_id);
```
Este índice mejora las búsquedas relacionadas con el campo `cliente_id`.

---

### 5. API REST con Spring Boot
Se desarrolló una API REST en Java con Spring Boot que incluye las siguientes operaciones CRUD:
- **Cliente:** Crear, leer, actualizar y eliminar clientes.
- **Estado:** Crear, leer, actualizar y eliminar estados.
- **Solicitud:** Crear, leer, actualizar y eliminar solicitudes.

El código se encuentra implementado en el proyecto. El API expone los endpoints necesarios para estas operaciones.

---

### 6. Validaciones del API
Se implementaron las siguientes validaciones en el código:
- **Solo una solicitud por cliente:**
  Esto se valida al intentar crear una solicitud.
- **Fecha de ingreso:**
  La fecha de ingreso no puede ser anterior a la fecha actual.
- **Monto mínimo:**
  El monto de la solicitud debe ser superior a 1,000,000.

---

### 7. Servicio de búsqueda con el índice
Un endpoint en el API utiliza el índice creado para buscar todas las solicitudes de un cliente específico de manera optimizada.

---

### 8. Control de errores y excepciones
El proyecto incluye manejo de errores mediante:
- **Excepciones personalizadas:** Para casos como cliente no encontrado, solicitud inválida, etc.
- **Manejo global de excepciones:** Mediante `@ControllerAdvice` para centralizar el control de errores.

---

### 9. Pruebas de consumo
Se realizaron pruebas con Postman para verificar el correcto funcionamiento de todos los endpoints del API:
- Creación de clientes, estados y solicitudes.
- Validaciones implementadas.
- Consultas específicas basadas en los criterios del ejercicio.

---

### 10. Código subido a GitHub
El código fuente completo del proyecto se encuentra en el siguiente repositorio:
[GitHub: solicitudes-api](https://github.com/daniels0009/solicitudes)

---

## Cómo ejecutar el proyecto

### Requisitos
- Java 17
- Maven
- PostgreSQL

### Configuración de la base de datos
1. Crea una base de datos en PostgreSQL:
   ```sql
   CREATE DATABASE solicitudes_db;
   ```
2. Configura las credenciales en `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/solicitudes_db
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

### Ejecutar
1. Navega al directorio del proyecto:
   ```bash
   cd ~/Descargas/solicitudes
   ```
2. Compila y ejecuta:
   ```bash
   mvn clean spring-boot:run
   ```
3. La API estará disponible en: `http://localhost:8080`

---

