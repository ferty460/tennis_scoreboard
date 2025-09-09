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
  <title>Завершенные матчи | Табло теннисного матча</title>
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
    <h2>Завершенные матчи</h2>
    <div class="finished-matches">
      <div class="filter-block">
          <form action="" method="get">
            <label for="filterByName">Фильтр по имени</label>
            <input
                    type="search"
                    name="filter_by_player_name"
                    id="filterByName"
                    placeholder="Фильтр по имени"
                    value="${not empty param.filter_by_player_name ? param.filter_by_player_name : ''}"
            >
            <button type="submit"></button>
            <button type="reset" id="reset-button"></button>
          </form>
      </div>
      <div class="finished-matches-block">
        <table class="finished-matches-table">
          <thead>
          <tr>
            <th>Первый игрок</th>
            <th>Второй игрок</th>
            <th>Победитель</th>
          </tr>
          </thead>
          <tbody>
          <c:if test="${requestScope.matches.isEmpty()}">
            <tr>
              <td colspan="3" style="text-align: center;">Матчи не найдены</td>
            </tr>
          </c:if>
          <c:forEach items="${requestScope.matches}" var="match">
            <tr>
              <td>${match.firstPlayerName()}</td>
              <td>${match.secondPlayerName()}</td>
              <td>${match.winnerName()}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <c:if test="${requestScope.pagination.totalPages > 1}">
        <div class="pagination">
          <c:choose>
            <c:when test="${requestScope.pagination.hasPrevious()}">
              <a href="?page=${requestScope.pagination.previousPage}<c:if test="${not empty requestScope.currentFilter}">&filter_by_player_name=${requestScope.currentFilter}</c:if>"
                 class="pagination-arrow">&laquo;</a>
            </c:when>
            <c:otherwise>
              <span class="pagination-arrow disabled">&laquo;</span>
            </c:otherwise>
          </c:choose>

          <c:forEach begin="1" end="${requestScope.pagination.totalPages}" var="i">
            <c:choose>
              <c:when test="${i == requestScope.pagination.currentPage}">
                <span class="pagination-page active">${i}</span>
              </c:when>
              <c:otherwise>
                <a href="?page=${i}<c:if test="${not empty requestScope.currentFilter}">&filter_by_player_name=${requestScope.currentFilter}</c:if>"
                   class="pagination-page">${i}</a>
              </c:otherwise>
            </c:choose>
          </c:forEach>

          <c:choose>
            <c:when test="${requestScope.pagination.hasNext()}">
              <a href="?page=${requestScope.pagination.nextPage}<c:if test="${not empty requestScope.currentFilter}">&filter_by_player_name=${requestScope.currentFilter}</c:if>"
                 class="pagination-arrow">&raquo;</a>
            </c:when>
            <c:otherwise>
              <span class="pagination-arrow disabled">&raquo;</span>
            </c:otherwise>
          </c:choose>
        </div>
      </c:if>

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

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const resetButton = document.getElementById('reset-button');

    resetButton.addEventListener('click', function(e) {
      e.preventDefault();

      const url = new URL(window.location.href);
      url.searchParams.delete('filter_by_player_name');
      url.searchParams.delete('page');
      window.location.href = url.toString();
    });
  });
</script>
</body>
</html>