@import gg.jte.Content
@import hexlet.code.dto.BasePage
@param Content content
@param BasePage page = null

<!doctype html>
<html lang="ru">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
            crossorigin="anonymous"></script>
</head>
<body>
<nav class="navbar navbar-expand navbar-dark bg-dark">
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" href="/">Анализатор страниц</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/urls">Сайты</a>
        </li>
    </ul>
</nav>
<main class="container">
    @if(page != null && page.getFlash() != null)
        <div class="alert alert-success" role="alert">
            ${page.getFlash()}
        </div>
    @endif
    ${content}
</main>
</body>
</html>