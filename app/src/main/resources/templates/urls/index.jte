@import hexlet.code.dto.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <h1>Сайты</h1>
    @if(!page.getUrls().isEmpty())
        <table class="table table-bordered table-hover" data-test="urls">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(var url : page.getUrls())
                <tr>
                    <td>${url.getId()}</td>
                    <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                    <td>
                        @if(url.getChecks() != null && !url.getChecks().isEmpty())
                            ${url.getChecks().get(0).getCreatedAt().toLocalDate().toString()}
                        @endif
                    </td>

                    <td>
                        @if(url.getChecks() != null && !url.getChecks().isEmpty())
                            ${url.getChecks().get(0).getStatusCode()}
                        @endif
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    @endif
`
)