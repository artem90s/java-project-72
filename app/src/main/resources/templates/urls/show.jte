@import hexlet.code.dto.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="mb-5">
        <h1 class="mb-4">Сайт: ${page.getUrl().getName()}</h1>
        <table class="table table-striped table-bordered" data-test="url">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${page.getUrl().getCreatedAt().toLocalDate().toString()}</td>
            </tr>
            </tbody>
        </table>
    </div>

    <div>
        <h1 class="mb-4">Проверки</h1>
        <form  class="mb-4" method="post" action="/urls/${page.getUrl().getId()}/checks">
            <input class="btn btn-primary" type="submit" value="Запустить проверку">
        </form>

        <table  class="table table-striped table-bordered" data-test="checks">
            <thead>
            <tr>
                <th>ID</th>
                <th>Код ответа</th>
                <th>h1</th>
                <th>title</th>
                <th>description</th>
                <th>Дата создания</th>
            </tr>
            </thead>
            <tbody>
            @for(var check : page.getUrl().getChecks())
            <tr>
                <td>${check.getId()}</td>
                <td>${check.getStatusCode()}</td>
                <td >${check.getH1()}</td>
                <td>${check.getTitle()}</td>
                <td>${check.getDescription()}</td>
                <td>${check.getCreatedAt().toLocalDate().toString()}</td>
            </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)