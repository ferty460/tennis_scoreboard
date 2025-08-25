<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=VT323&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <title>Ошибка ${pageContext.errorData.statusCode} | Табло теннисного матча</title>
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
        <div class="error-container">
            <div class="error-content">
                <h1 class="error-title">${pageContext.errorData.statusCode}</h1>
                <p class="error-message">
                    <c:choose>
                        <c:when test="${pageContext.errorData.statusCode == 404}">
                            Страница не найдена.
                        </c:when>
                        <c:when test="${pageContext.errorData.statusCode == 500}">
                            Ошибка на стороне сервера.
                        </c:when>

                        <c:otherwise>
                            Произошла непредвиденная ошибка. Пожалуйста, не пробуйте позже.
                        </c:otherwise>
                    </c:choose>
                </p>
                <c:if test="${not empty pageContext.errorData.throwable}">
                    <p class="error-description">
                            ${pageContext.errorData.throwable.message}
                    </p>
                </c:if>
                <a href="${pageContext.request.contextPath}/" class="action-link">Вернуться на главную</a>
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
