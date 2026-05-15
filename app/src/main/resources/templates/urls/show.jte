@import hexlet.code.dto.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
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

`
)