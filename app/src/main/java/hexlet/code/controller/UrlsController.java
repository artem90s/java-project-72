package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.Unirest;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void getALl(@NotNull Context context) {
        List<Url> urls = UrlRepository.getAll();
        var page = new UrlsPage(urls);
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
        List<UrlCheck> checks = UrlCheckRepository.findByUrlId(id);
        url.setChecks(checks);
        var page = new UrlPage(url);
        if (context.sessionAttribute("flash") != null) {
            page.setFlash(context.sessionAttribute("flash"));
            context.sessionAttribute("flash", null);
        }
        context.render("urls/show.jte", model("page", page));
    }

    public static void getChecks(@NotNull Context context) {
        var id = context.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id).get();
        var response = Unirest.get(url.getName()).asString();
        int status = response.getStatus();
        var page = new UrlPage(url);
        if (HttpStatus.forStatus(status).isClientError() || HttpStatus.forStatus(status).isServerError()) {
            page.setFlash("Произошла ошибка при проверке");
        } else if (HttpStatus.forStatus(status).isSuccess()) {
            var check = new UrlCheck();
            check.setUrlId(id);
            check.setStatusCode(status);
            Document doc = Jsoup.parse(response.getBody());
            check.setTitle(doc.title() != null && !doc.title().isEmpty() ? doc.title() : "");
            Element h1Element = doc.selectFirst("h1");
            check.setH1(h1Element != null && h1Element.hasText() ? h1Element.text() : "");
            Element metaDesc = doc.selectFirst("meta[name=description]");
            check.setDescription(metaDesc != null && metaDesc.hasAttr("content") ? metaDesc.attr("content") : "");
            UrlCheckRepository.save(check);
            page.setFlash("Страница успешно проверена");
        }
        context.render("urls/show.jte", model("page", page));
    }
}
