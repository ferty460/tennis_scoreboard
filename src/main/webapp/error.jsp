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
    <title>Ошибка ${not empty requestScope.statusCode ? requestScope.statusCode : pageContext.errorData.statusCode} | Табло теннисного матча</title>
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
                <h1 class="error-title">
                    ${not empty requestScope.statusCode ? requestScope.statusCode : pageContext.errorData.statusCode}
                </h1>
                <c:if test="${not empty requestScope.errorMessage}">
                    <p class="error-message">
                            ${requestScope.errorMessage}
                    </p>
                </c:if>
                <c:if test="${empty requestScope.errorMessage and not empty pageContext.errorData.throwable}">
                    <p class="error-message">
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
