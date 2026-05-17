package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import kong.unirest.core.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private Javalin app;
    private static MockWebServer mockServer;
    @BeforeAll
    public static void start() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
    }
    @AfterAll
    public static void shutdown() throws Exception {
        mockServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws Exception {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    public void testUrlPageSuccess() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://hexlet.io");
            var id = UrlRepository.save(url);
            var response = client.get("/urls/" + id);
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    public void testUrlIdSuccess() {
        JavalinTest.test(app, (server, client) -> {
            var name = "https://hexlet.io";
            var url = new Url(name);
            var id = UrlRepository.save(url);
            var response = client.get("/urls/" + id);
            var urlFromBd = UrlRepository.findById(id).get();
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(urlFromBd.getName()).isEqualTo(name);
        });
    }

    @Test
    public void testUrlPageNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999");
            assertThat(response.code()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        });
    }

    @Test
    public void testCreateUrlSuccess() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=http://ya.ru");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    public void testCreateUrlNegative() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=ya.ru");
            assertThat(response.code()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
        });
    }

    @Test
    public void checkUrlSuccess() {
        MockResponse mockResponse  = new MockResponse().setResponseCode(HttpStatus.OK.getCode());
        mockServer.enqueue(mockResponse);
        var urlName = mockServer.url("/").toString();
        Url url = new Url(urlName);
        Long id = UrlRepository.save(url);
        var response = Unirest.get(urlName).asString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.getCode());

        UrlCheck check = new UrlCheck();
        check.setUrlId(id);
        check.setStatusCode(response.getStatus());

        UrlCheckRepository.save(check);

        var checks = UrlCheckRepository.findByUrlId(id);

        assertThat(checks).hasSize(1);

        assertThat(checks.get(0).getStatusCode()).isEqualTo(HttpStatus.OK.getCode());
    }

    @Test
    public void checkUrlNegative() {
        var mockResponse = new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        mockServer.enqueue(mockResponse);
        var urlName = mockServer.url("/").toString();
        var response = Unirest.get(urlName).asString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
    }
}

