<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=VT323&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <title>Счет текущего матча | Табло теннисного матча</title>
</head>
<body>

<div class="wrapper">

    <header class="header">
        <div>
            <div class="logo">
                <h1><a href="${pageContext.request.contextPath}/">TABLO</a></h1>
            </div>
            <nav class="navigation-bar">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/new-match">Новый матч</a></li>
                    <li><a href="${pageContext.request.contextPath}/matches">Завершенные матчи</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="main">
        <h2>Счет текущего матча</h2>
        <div class="match-score">
            <table class="match-score-table">
                <thead>
                <tr>
                    <th>Игрок</th>
                    <th>Сет</th>
                    <th>Гейм</th>
                    <th>Очки</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><b>Player name 1</b></td>
                    <td>0</td>
                    <td>1</td>
                    <td>15</td>
                    <td><a href="" class="action-link">+</a></td>
                </tr>
                <tr>
                    <td><b>Player name 2</b></td>
                    <td>1</td>
                    <td>2</td>
                    <td>15</td>
                    <td><a href="" class="action-link">+</a></td>
                </tr>
                </tbody>
            </table>
        </div>
    </main>

    <footer class="footer">
        <div class="footer-inner">
            <h4 class="footer-info">
                <a href="https://github.com/ferty460/tennis_scoreboard">The source code</a>
            </h4>
        </div>
    </footer>

</div>

</body>
</html>