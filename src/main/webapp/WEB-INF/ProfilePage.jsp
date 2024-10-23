<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/ProfileStyle.css">
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


    <div class="container">
        <div class="profile-picture">
            <img src="/images/${userProfile.imagePath}" alt="Profile Image">
        </div>
        <div class="user-info">
            <h2>User Profile</h2>
            <p><strong>Name:</strong> ${userProfile.firstName} ${userProfile.lastName}</p>
            <p><strong>Email:</strong> ${userProfile.email}</p>
            <p><strong>Age:</strong> ${userAge}</p>
            <p><strong>Registred since:</strong> ${userProfile.dateSignUp}</p>
            <c:choose>
    			<c:when test="${canEdit}">
       				 <form action="${request.getContextPath}/ProjetAppDis/profile" method="post">
                <button type="submit" name="button" value="editProfile" >Edit profile</button>
            </form>
   			 </c:when>
   			 </c:choose>
            
            <!-- Add more user information fields as needed -->
        </div>
    </div>
</body>
<script>
    function redirectToAnotherPage() {
        // Replace 'anotherPage.jsp' with the actual URL of the page you want to redirect to
        window.location.href = '${request.getContextPath}/ProjetAppDis/addAnnonce';
    }
</script>
</html>