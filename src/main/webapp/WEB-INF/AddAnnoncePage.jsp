<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Announcement</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/AddAnnonceStyle.css">
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
        <h2>Create Bid (all prices are in Million Dinars Algeriens)</h2>
        <form action="${request.contextPath}/ProjetAppDis/addAnnonce" method="post" enctype="multipart/form-data">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" required>

            <label for="description">Description:</label>
            <textarea id="description" name="description" rows="4" required></textarea>

            <label for="number">Number:</label>
            <input type="tel" id="number" name="number" maxlength="10" required>

            <label for="place">Place:</label>
            <input type="text" id="place" name="place" required>

            <label for="images">Upload Images (up to 10):</label>
            <input type="file" id="images" name="images" accept="image/*" multiple required>
            
            <label for="base_price">Base price(start point of the bid):</label>
            <input type="number" id="base_price" name="base_price" step=".01" required>
            <label id="base_price_indication"></label>
            
            <label for="immediate_price">Immediate price(if set to 0, disabled):</label>
            <input type="number" id="immediate_price" step=".01" value="0" name="immediate_price" >
            
            <label for="reserve_price">Reserve price(the minimum price you are willing to sell for):</label>
            <input type="number" id="reserve_price" name="reserve_price" step=".01" required>
            <label id="reserve_price_indication"></label>
            
            <label for="end_date">When the bid will end (in days):</label>
            <input type="number" id="end_date" name="end_date" required>   
	
			<c:out value="${param.error}"/>
            <button type="submit">Submit</button>
        </form>
    </div>
</body>
<script>
access_key = '0a0db350ce20531993582170732c6fba';
url= 'http://api.ipstack.com/check?access_key=' +access_key;

var city;

fetch(url)	
.then(response => response.json())
.then(data => {
  // Extract the exchange rate from DZD to EUR
	city=data.city;
  	console.log(city);
  	var place=document.getElementById('place');
  	place.value=city;
})
.catch(error => {
  console.error('Error fetching exchange rates:', error);
});

var titleInput = document.getElementById("title");
var basePriceIndication = document.getElementById("base_price_indication");
var reservePriceIndication = document.getElementById("reserve_price_indication");
titleInput.addEventListener("input", function() {
    updatePrices();
});

function updatePrices() {
	keyword=titleInput.value;
	const urlBase='http://'+window.location.hostname+':'+window.location.port+'/ProjetAppDis/rest';
	url= urlBase +'/searchAnnonce?'+'keyword='+keyword+'&type=2';
	
	if(keyword.length==0	){
		basePriceIndication.textContent="";
		reservePriceIndication.textContent="";
	}else{
		fetch(url)		
    .then(response => response.json())
    .then(data => {
    	console.log(url);
    	var nombreAnnonceCorespondante = data.Statistiques["nombre d'annonce corespondante"];
		
    	if(nombreAnnonceCorespondante>0){
	    	var annonceExpireMin = data.Statistiques["Statistiques annonce expire"]["Stat annonce vendu"]["Annonce expire prix de vente minimum"];
	    	var annonceExpireMax = data.Statistiques["Statistiques annonce expire"]["Stat annonce vendu"]["Annonce expire prix de vente maximum"];
	    	var annonceExpireMoyen = data.Statistiques["Statistiques annonce expire"]["Stat annonce vendu"]["Annonce expire prix de vente moyen"];
	    	
	    	if(annonceExpireMin!=0){
	    		basePriceIndication.textContent="Price range are between : ["+annonceExpireMin/10000+","+annonceExpireMax/10000+"] , the average is : "+annonceExpireMoyen/10000+"(Price in millions DA, data collected on selled product only)";
	    	}
		
	    	var encherisMin = data.Statistiques["Statistiques annonce toujours en vente"]["Annonce toujours en vente prix encheris minimum"];
	    	var encherisMax = data.Statistiques["Statistiques annonce toujours en vente"]["Annonce toujours en vente prix encheris maximum"];
	    	var encherisMoyen = data.Statistiques["Statistiques annonce toujours en vente"]["Annonce toujours en vente prix encheris moyen"];
	    	var expireMin = data.Statistiques["Statistiques annonce expire"]["Stat annonce vendu"]["Annonce expire prix de vente minimum"];
	    	var expireMax = data.Statistiques["Statistiques annonce expire"]["Stat annonce vendu"]["Annonce expire prix de vente maximum"];
	    	var expireMoyen = data.Statistiques["Statistiques annonce expire"]["Stat annonce vendu"]["Annonce expire prix de vente moyen"];
			
	  
			
	    	
	    	var min;
	    	var max;
	    	var moyenne;
	    	
	    	if(encherisMin!=0 || expireMin!=0){
	    		if(encherisMin==0){
	    			min=expireMin;
	    			max=expireMax;
	    			moyenne=expireMoyen;
	    		}
	    		if(expireMin==0){
	    			min=encherisMin;
	    			max=encherisMax;
	    			moyenne=encherisMoyen;
	    		}
	    		if(encherisMin!=0 && expireMin!=0){
	    			min=getMinimum(encherisMin,expireMin);
	    			max=getMaximum(expireMax,encherisMax);
	    			moyenne=(expireMoyen+encherisMoyen)/2;
	    		}	    	
	    		//reservePriceIndication.textContent="Price range are between : ["+min/10000+","+max/10000+"] , the average is : "+moyenne/10000+"(Price in millions DA, data collected on bid on all products)";

	    	}
	    	
	    	
	    	
	    	
	    	
	    	
    	}
    	else{
    		basePriceIndication.textContent="";
    		reservePriceIndication.textContent="";
    	}
    	
    	})
      .catch(error => {
        console.error('Error fetching prices recommendation:', error);
      });
	}
	
	
	
	
	
    
    
}
function getMinimum(value1, value2) {
    return Math.min(value1, value2);
}

function getMaximum(value1, value2) {
    return Math.max(value1, value2);
}
    function redirectToAnotherPage() {
        // Replace 'anotherPage.jsp' with the actual URL of the page you want to redirect to
        window.location.href = '${request.getContextPath}/ProjetAppDis/addAnnonce';
    }
</script>
</html>