package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private Javalin app;

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
}

