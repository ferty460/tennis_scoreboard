<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <c:if test="${!requestScope.isFinished}">
            <h2>Счет текущего матча</h2>
            <div class="match-score">
                <table class="match-score-table">
                    <thead>
                    <tr>
                        <th>Игрок</th>
                        <th>Сет</th>
                        <th>Гейм</th>
                        <th>Счет</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><b>${requestScope.state.matchDto.firstPlayer.name}</b></td>
                        <td>${requestScope.state.matchState.firstPlayerSets}</td>
                        <td>${requestScope.state.matchState.firstPlayerGames}</td>
                        <td>${requestScope.state.firstPlayerPoints}</td>
                        <td>
                            <form action="match-score?uuid=${requestScope.state.matchUuid}" method="post">
                                <input type="hidden" name="playerId" value="${requestScope.state.matchDto.firstPlayer.id}">
                                <input type="submit" value="+ Очко">
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td><b>${requestScope.state.matchDto.secondPlayer.name}</b></td>
                        <td>${requestScope.state.matchState.secondPlayerSets}</td>
                        <td>${requestScope.state.matchState.secondPlayerGames}</td>
                        <td>${requestScope.state.secondPlayerPoints}</td>
                        <td>
                            <form action="match-score?uuid=${requestScope.state.matchUuid}" method="post">
                                <input type="hidden" name="playerId" value="${requestScope.state.matchDto.secondPlayer.id}">
                                <input type="submit" value="+ Очко">
                            </form>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </c:if>

        <c:if test="${requestScope.isFinished}">
            <h2>Итоги матча</h2>
            <div class="match-result">
                <div class="trophy-icon">🏆</div>
                <div class="result-title">МАТЧ ЗАВЕРШЕН</div>

                <div class="players-container">
                    <div class="player-card">
                        <div class="player-name">${requestScope.state.matchDto.firstPlayer.name}</div>
                        <div class="player-score">${requestScope.state.matchState.firstPlayerSets}</div>
                    </div>

                    <div class="vs-separator">VS</div>

                    <div class="player-card">
                        <div class="player-name">${requestScope.state.matchDto.secondPlayer.name}</div>
                        <div class="player-score">${requestScope.state.matchState.secondPlayerSets}</div>
                    </div>
                </div>

                <div class="winner-badge">
                    ПОБЕДИТЕЛЬ: <c:if test="${requestScope.winner != null}">${requestScope.winner.name}</c:if>
                </div>

                <a href="${pageContext.request.contextPath}/matches" class="action-link">
                    К списку матчей
                </a>
            </div>
        </c:if>

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