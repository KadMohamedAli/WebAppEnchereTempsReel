<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/AdminStyle.css">
</head>
<body>
<div class="navigation">
    <table>
        <tr>
            <td>
                <a href="${request.getContextPath()}/ProjetAppDis/login">
                    <img class="home-icon" src="${pageContext.request.contextPath}/res/icons/home_icon.png" alt="Home Icon">
                </a>
            </td>
            <td class="user-info">
                <span class="user-name">${userProfileName}</span>
            </td>
            <td class="right-section">
                <button class="add-annonce" onclick="redirectToAnotherPage()">Add Annonce</button>
                <a class="profile" href="${request.getContextPath()}/ProjetAppDis/profile?id=${selfId}">
                    <img src="/images/${userProfileImagePath}" alt="Profile Image">
                </a>
                <a class="logout" href="${request.getContextPath()}/ProjetAppDis/logout">Log Out</a>
                
            </td>
        </tr>
    </table>
</div>


    <div class="user-container">
        <h2>User List</h2>
        <form action="${request.getContextPath}/ProjetAppDis/AdminServlet" method="post">
            <input type="text" name="searchKeyword" placeholder="Search by name or email"  >
            <button type="submit" name="button" value="search">Search</button>
        </form>
        <table class="user-table">
            <thead>
                <tr>
                    <th>Picture</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Birthday</th>
                    <th>Inscription</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr class="user-row">
                        <td class="user-picture">
                            <img src="/images/${user.imagePath}" alt="${user.firstName}'s Picture">
                        </td>
                        <td class="user-info">
						    <a href="${request.getContextPath}/ProjetAppDis/profile?id=${user.id}">
						        <p class="user-name">${user.firstName} ${user.lastName}</p>
						    </a>
						</td>
                        <td class="user-info">
                            <p class="user-email">${user.email}</p>
                        </td>
                        <td class="user-info">
                            <p class="user-birthday">${user.birthDay}</p>
                        </td>
                        <td class="user-info">
                            <p class="user-dateSignUp">${user.dateSignUp}</p>
                        </td>
                        <td class="user-actions">
                            <form action="${request.getContextPath}/ProjetAppDis/AdminServlet" method="post">
                                <input type="hidden" name="userId" value="${user.id}">
                                <button type="submit" name="button" value="edit">Edit</button>
                            </form>
                            <form action="${request.getContextPath}/ProjetAppDis/AdminServlet" method="post">
                                <input type="hidden" name="userId" value="${user.id}">
                                <button type="submit" name="button" value="remove">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
<script>
    function redirectToAnotherPage() {
        // Replace 'anotherPage.jsp' with the actual URL of the page you want to redirect to
        window.location.href = '${request.getContextPath}/ProjetAppDis/addAnnonce';
    }
</script>
</html>