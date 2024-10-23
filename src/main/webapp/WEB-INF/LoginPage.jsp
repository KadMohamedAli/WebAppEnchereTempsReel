<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/style.css">
</head>
<body>
    <div class="login-container">
        <form class="login-form" action="${request.getContextPath}/ProjetAppDis/login" method="post">
            <h1>Login</h1>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <button type="submit">Login</button>
            <c:out value="${serverError}"/>
            <p class="signup-link">Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign up</a></p>
        </form>
    </div>
</body>
</html>