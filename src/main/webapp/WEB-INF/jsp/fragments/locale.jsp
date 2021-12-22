<%@page contentType="text/html" pageEncoding="UTF-8" %>

<div class="dropdown">
    <a class="dropdown-toggle" href="#" id="lang" data-toggle="dropdown">${pageContext.response.locale}</a>

    <div class="dropdown-menu">
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">English</a>
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Русский</a>
    </div>
</div>