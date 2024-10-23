<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit your informations</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/SignUpStyle.css">
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

	 

    <div class="signup-container">
        <img class="profile-image" src="/images/${user.imagePath}" alt="Profile Image" width="300" height="300"
             onmouseover="this.style.transform='scale(1.1)'" onmouseout="this.style.transform='scale(1)'">    
        <form action="${request.getContextPath}/ProjetAppDis/edit" method="post" enctype="multipart/form-data"  >
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${user.email}" readonly><br>

            <label for="firstname">First Name:</label>
            <input type="text" id="firstname" name="firstname" value="${user.firstName}" ><br>

            <label for="lastname">Last Name:</label>
            <input type="text" id="lastname" name="lastname" value="${user.lastName}" ><br>

            <label for="image">Image:</label>
            <input type="file" id="image" name="image" accept="image/*"><br>

            <label for="birthday">Age</label>
            <input type="text" id="birthday" name="birthday" value="${userAge}" readonly><br>

			<label for="creationdate">Registered since</label>
            <input type="text" id="creationdate" name="creationdate" value="${user.dateSignUp}" readonly><br>

			<c:out value="${serverError}"/>
			<input type="hidden" name="button" value="change">
            <button type="submit" name="button" value="change">Change</button>

            
        </form>
    </div>
</body>
<script>
    function redirectToAnotherPage() {
        // Replace 'anotherPage.jsp' with the actual URL of the page you want to redirect to
        window.location.href = '${request.getContextPath}/ProjetAppDis/addAnnonce';
    }
</script>
</html>