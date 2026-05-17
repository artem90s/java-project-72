package hexlet.code.dto;

import hexlet.code.model.Url;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlPage extends BasePage {
    private Url url;

    public UrlPage(Url url) {
        this.url = url;
    }
}
