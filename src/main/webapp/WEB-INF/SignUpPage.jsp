<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/SignUpStyle.css">
</head>
<body>
    <div class="signup-container">
        <form action="${request.getContextPath}/ProjetAppDis/signup" method="post" enctype="multipart/form-data" >
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required><br>

            <label for="firstname">First Name:</label>
            <input type="text" id="firstname" name="firstname" required><br>

            <label for="lastname">Last Name:</label>
            <input type="text" id="lastname" name="lastname" required><br>

            <label for="image">Image:</label>
            <input type="file" id="image" name="image" accept="image/*"><br>

            <label for="birthday">Birthday:</label>
            <input type="date" id="birthday" name="birthday" required><br>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required><br>

			<c:out value="${confirmPasswordError}"/>

            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required><br>

			<c:out value="${serverError}"/>
            <button type="submit">Sign Up</button>

            <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Log In</a></p>
        </form>
    </div>
</body>
</html>