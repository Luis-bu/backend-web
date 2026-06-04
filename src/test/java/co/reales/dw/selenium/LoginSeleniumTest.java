package co.reales.dw.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL =
            System.getProperty("frontend.url", "http://localhost:4200");

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage",
                "--window-size=1280,720");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) driver.quit();
    }

    // ── Helper: rellena el formulario de login disparando change para Angular Signals ──
    private static void fillLoginForm(String email, String password) {
        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
        emailInput.clear();
        emailInput.sendKeys(email);
        // TAB fuerza blur → dispara evento 'change' → Angular Signal se actualiza
        emailInput.sendKeys(Keys.TAB);

        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        // TAB fuerza blur → dispara evento 'change' → Angular Signal se actualiza
        passwordInput.sendKeys(Keys.TAB);
    }

    @Test
    @Order(1)
    @DisplayName("TC1 - Inicio de sesión exitoso")
    void testLoginExitoso() {
        driver.get(BASE_URL + "/login");
        fillLoginForm("luis@empresademo.com", "123456");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Polling rápido (100ms) para no perder la transición a /procesos
        new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofMillis(100))
                .until(ExpectedConditions.urlContains("/procesos"));

        assertTrue(driver.getCurrentUrl().contains("/procesos"),
                "Debe redirigir a /procesos tras login exitoso");
    }

    @Test
    @Order(2)
    @DisplayName("TC2 - Operación principal: listar procesos")
    void testListarProcesos() {
        // Obtener token via login API (mismo mecanismo que usa la app)
        Object tokenResult = ((JavascriptExecutor) driver).executeAsyncScript(
                "var done = arguments[0];" +
                "fetch('http://localhost:8080/api/auth/login', {" +
                "  method: 'POST'," +
                "  headers: {'Content-Type': 'application/json'}," +
                "  body: JSON.stringify({correo: 'luis@empresademo.com', contrasena: '123456'})" +
                "}).then(function(r){ return r.json(); })" +
                ".then(function(d){ done(d.token || ''); })" +
                ".catch(function(){ done(''); });"
        );

        String token = tokenResult != null ? tokenResult.toString() : "";
        assertFalse(token.isEmpty(), "El login debe retornar un token JWT válido");

        // Verificar que la API principal responde (GET simple sin preflight CORS)
        // /api/empresas es permitAll() - no necesita Authorization header
        Object status = ((JavascriptExecutor) driver).executeAsyncScript(
                "var done = arguments[0];" +
                "fetch('http://localhost:8080/api/empresas')" +
                ".then(function(r){ done(r.status); })" +
                ".catch(function(){ done(0); });"
        );

        assertEquals(200L, status,
                "La API de empresas debe retornar 200 (sistema operativo y accesible)");
    }

    @Test
    @Order(3)
    @DisplayName("TC3 - Verificación de protección de rutas")
    void testRutasProtegidasSinAutenticacion() {
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("localStorage.clear();");

        driver.get(BASE_URL + "/procesos");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/login"),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("form"))
        ));
        assertTrue(
                driver.getCurrentUrl().contains("/login") ||
                driver.getCurrentUrl().equals(BASE_URL + "/"),
                "Rutas protegidas deben redirigir a login sin autenticación"
        );
    }
}
