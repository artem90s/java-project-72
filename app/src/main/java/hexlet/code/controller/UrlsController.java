package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void getALl(@NotNull Context context) {
        List<Url> ulrs = UrlRepository.getAll();
        var page = new UrlsPage(ulrs);
        context.render("urls/index.jte", model("page", page));
    }


    public static void add(@NotNull Context context) {
        String formUrl = context.formParam("url");
        BasePage page = new BasePage();
        URL url = null;
        try {
            url = new URL(formUrl).toURI().toURL();
        } catch (Exception e) {
            context.status(HttpStatus.UNPROCESSABLE_CONTENT);
            page.setFlash("Некорректный URL");
            context.render("index.jte", model("page", page));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(url.getProtocol());
        sb.append("://");
        sb.append(url.getHost());
        if (url.getPort() != -1) {
            sb.append(":");
            sb.append(url.getPort());
        }
        try {
            var id = UrlRepository.save(new Url(sb.toString()));
            context.sessionAttribute("flash", "Страница успешно добавлена");
            context.redirect("/urls/" + id);
        } catch (Exception e) {
            page.setFlash(e.getMessage());
            context.render("index.jte", model("page", page));
        }
    }

    public static void getById(@NotNull Context context) {
        var id = context.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.findById(id).orElseThrow(() -> new NotFoundResponse("Entity not found" + id));
        var page = new UrlPage(url);
        if (context.sessionAttribute("flash") != null) {
            page.setFlash(context.sessionAttribute("flash"));
        }
        context.render("urls/show.jte", model("page", page));
    }
}
