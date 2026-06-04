# ENTREGA FINAL — ProcessFlow

## Checklist de cumplimiento

| Criterio | Estado | Implementación |
|---|---|---|
| **AUTENTICACIÓN (25%)** | | |
| Login con JWT | ✅ | `POST /api/auth/login` → `AuthService.login()` |
| Generación de JWT | ✅ | `JwtUtil.generateToken()` (HS384, 24h) |
| Validación de JWT | ✅ | `JwtUtil.validateToken()` + `JwtAuthFilter` |
| Protección de endpoints | ✅ | `SecurityConfig` — solo `/api/auth/**` y GET `/api/empresas` son públicos |
| Expiración de token | ✅ | `isAuthenticated()` valida `exp` en el frontend |
| Filtro de autenticación | ✅ | `JwtAuthFilter` (OncePerRequestFilter) |
| **AUTORIZACIÓN (20%)** | | |
| Usuario accede solo a su empresa | ✅ | `SecurityUtils.validarAccesoEmpresa()` en cada endpoint sensible |
| No puede modificar recursos de otra empresa | ✅ | Validación en `ProcesoService`, `ActividadService`, `ArcoService`, etc. |
| Validación de empresa en endpoints | ✅ | Todos los servicios de negocio llaman `validarAccesoEmpresa()` |
| ADMIN puede acceder a cualquier empresa | ✅ | Bypass por rol `ADMINISTRADOR` en `SecurityUtils` |
| **FUNCIONALIDAD (20%)** | | |
| Frontend y backend integrados | ✅ | Angular llama a `http://localhost:8080/api/**` con JWT |
| JWT enviado automáticamente | ✅ | `JwtInterceptor` añade `Authorization: Bearer` en cada request |
| Rutas protegidas requieren auth | ✅ | `AuthGuard` (`canActivate`) en todas las rutas salvo `/login` |
| CRUD de procesos | ✅ | Actividades, gateways, arcos, roles de proceso |
| Control por rol en el frontend | ✅ | `SOLO_LECTURA` no ve botones de creación/edición/eliminación |
| ADMIN puede cambiar de empresa | ✅ | Selector de empresa solo interactivo para `ADMINISTRADOR` |
| **MÉTRICAS (10%)** | | |
| Total de usuarios | ✅ | `GET /api/metricas` → campo `totalUsuarios` |
| Total de procesos | ✅ | campo `totalProcesos` |
| Procesos por estado | ✅ | campo `procesosPorEstado` (BORRADOR / PUBLICADO) |
| Últimas modificaciones | ✅ | campo `ultimasModificaciones` (últimos 5 procesos) |
| **PRUEBAS (10%)** | | |
| Prueba unitaria: Login | ✅ | `AuthServiceTest` (4 casos) |
| Prueba unitaria: JWT | ✅ | `JwtUtilTest` (6 casos) |
| Prueba unitaria: Restricción empresa | ✅ | `ProcesoServiceIsolacionTest` (3 casos) |
| Selenium TC1: Login exitoso | ✅ | Formulario → redirige a `/procesos` |
| Selenium TC2: Operación principal | ✅ | Login API + GET `/api/empresas` → 200 |
| Selenium TC3: Rutas protegidas | ✅ | Sin token → `/procesos` redirige a `/login` |
| **INGENIERÍA (5%)** | | |
| Separación de responsabilidades | ✅ | Capas: Controller → Service → Repository |
| Manejo de errores | ✅ | `GlobalExceptionHandler`, `BadRequestException` |
| Código sin duplicación | ✅ | `SecurityUtils` centraliza validaciones de acceso |

---

## Arquitectura

```
Frontend (Angular 17)          Backend (Spring Boot 4)
localhost:4200                 localhost:8080
─────────────────              ─────────────────────────
AuthGuard                      SecurityConfig (JWT stateless)
JwtInterceptor          ←→     JwtAuthFilter
AuthService                    AuthController → AuthService
EmpresaService                 EmpresaController → EmpresaService
ProcesoService                 ProcesoController → ProcesoService
                               SecurityUtils.validarAccesoEmpresa()
                               MetricasController → /api/metricas
```

---

## Cómo ejecutar

### Backend
```bash
export DB_URL='jdbc:postgresql://190.146.2.119:2345/grupo24'
export DB_USERNAME='grupo24_user'
export DB_PASSWORD='G24@Qw8#Lm2!Rt7$Px9V'
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend-web/mi-app
npm start
```

### Pruebas unitarias (sin servidores)
```bash
cd BackendWeb/backend-web
mvn test -Dexclude="**/selenium/**"
# 122 tests, 0 fallos
```

### Pruebas Selenium (requiere backend + frontend corriendo)
```bash
mvn test -Dtest="co.reales.dw.selenium.LoginSeleniumTest"
# 3 tests, 0 fallos
```

### Endpoint de métricas
```
GET http://localhost:8080/api/metricas
Authorization: Bearer <token>
```

---

## Archivos clave creados/modificados

| Archivo | Descripción |
|---|---|
| `security/SecurityUtils.java` | Validación de empresa + bypass para ADMIN |
| `security/JwtAuthFilter.java` | Filtro JWT en cada request |
| `config/SecurityConfig.java` | Reglas de acceso y CORS |
| `controllers/MetricasController.java` | Endpoint `/api/metricas` |
| `dtos/MetricasDTO.java` | DTO de métricas |
| `services/AuthService.java` | Login con BCrypt + generación JWT |
| `selenium/LoginSeleniumTest.java` | 3 casos de prueba automatizados |
| `services/AuthServiceTest.java` | Pruebas unitarias de autenticación |
| `security/JwtUtilTest.java` | Pruebas unitarias de JWT |
| `services/ProcesoServiceIsolacionTest.java` | Pruebas de aislamiento por empresa |
| `guards/auth.guard.ts` (frontend) | Guard de rutas protegidas |
| `interceptors/jwt.interceptor.ts` (frontend) | Adjunta token a cada request |
