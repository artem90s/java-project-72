package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        var app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() throws Exception {
        var app = Javalin.create();
        app.get("/", ctx -> ctx.result("Hello World"));
        log.info("Cоздан обьект Javalin");
        return app;
    }
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
//    public static String readResourceFile(String fileName) throws Exception {
//        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
//            return reader.lines().collect(Collectors.joining("\n"));
//        }
//    }
}
