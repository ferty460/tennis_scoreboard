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
  <title>Завершенные матчи | Табло теннисного матча</title>
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
    <h2>Завершенные матчи</h2>
    <div class="finished-matches">
      <div class="filter-block">
        <form action="" method="get">
          <label for="filterByName">Фильтр по имени</label>
          <input type="search" name="filterByName" id="filterByName" placeholder="Фильтр по имени">
          <button type="reset"></button>
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
          <tr>
            <td>asdsaasd</td>
            <td>dfxzvxzcv</td>
            <td>asdsaasd</td>
          </tr>
          <tr>
            <td>asdsaasd</td>
            <td>dfxzvxzcv</td>
            <td>asdsaasd</td>
          </tr>
          <tr>
            <td>asdsaasd</td>
            <td>dfxzvxzcv</td>
            <td>asdsaasd</td>
          </tr>
          <tr>
            <td>asdsaasd</td>
            <td>dfxzvxzcv</td>
            <td>asdsaasd</td>
          </tr>
          <tr>
            <td>asdsaasd</td>
            <td>dfxzvxzcv</td>
            <td>asdsaasd</td>
          </tr>
          </thead>
        </table>
      </div>
      <div class="pagination">
        <button class="pagination-arrow" disabled>
          &laquo;
        </button>
        <button class="pagination-page active">1</button>
        <button class="pagination-page">2</button>
        <button class="pagination-page">3</button>
        <span class="pagination-dots">...</span>
        <button class="pagination-page">8</button>
        <button class="pagination-arrow">
          &raquo;
        </button>
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