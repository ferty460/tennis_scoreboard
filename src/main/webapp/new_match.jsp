<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=VT323&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <title>Новый матч | Табло теннисного матча</title>
</head>
<body>

<div class="wrapper">

    <header class="header">
        <div>
            <div class="logo">
                <h1><a href="#">TABLO</a></h1>
            </div>
            <nav class="navigation-bar">
                <ul>
                    <li><a href="">Новый матч</a></li>
                    <li><a href="">Завершенные матчи</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="main">
        <h2>Новый матч</h2>
        <div class="new-match">
            <div class="new-match__content">
                <div class="new-match__form">
                    <form action="" method="post">
                        <div class="form-group">
                            <label for="firstPlayerName">Первый игрок</label>
                            <input type="text" name="firstPlayerName" id="firstPlayerName">
                        </div>

                        <div class="form-group">
                            <label for="secondPlayerName">Второй игрок</label>
                            <input type="text" name="secondPlayerName" id="secondPlayerName">
                        </div>

                        <button type="submit" class="submit-btn">Начать матч</button>
                    </form>
                </div>

                <div class="new-match__image">
                    <img src="${pageContext.request.contextPath}/images/tennis.webp" alt="tennis">
                </div>
            </div>
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