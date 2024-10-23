<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Annonce</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/AnnonceStyle.css">
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

<div id="announcement-container"></div>
    
<%
  int userId =Integer.valueOf(request.getSession().getAttribute("selfId").toString());
%>        
   
</body>
<script>
	const d='ws://'+window.location.hostname+':'+window.location.port+'/ProjetAppDis/annonceEndPoint';
	socket = new WebSocket(d);
	socket.addEventListener("open", (event) => {
		const listId=[1,[getParameterByName('id')]];
		var jsonList = JSON.stringify(listId);
        socket.send(jsonList);
        console.log(jsonList);
    });
	
	var userIdFromSession = '<%= userId %>';
	
	
	socket.onmessage = function (event) {
        const announcement = JSON.parse(event.data);
        targetDates = announcement.map(announcement => announcement.endDate);
        displayAnnouncement(announcement[0]);
        setInterval(updateRemainingTimes, 1000);
    };
    
    
    
    function displayAnnouncement(announcement) {
    	console.log(announcement);
        const container = document.getElementById("announcement-container");
        container.innerHTML = "";
        const announcementElement = document.createElement("div");
        announcementElement.className = "announcement";

        // Build the announcement content
        const titleElement = document.createElement("h3");
        titleElement.textContent = announcement.title;

        const imagesContainer = document.createElement("div");
        for (const imagePath of announcement.imagePaths) {
            const imageElement = document.createElement("img");
            imageElement.src = '/images/'+imagePath;
            imagesContainer.appendChild(imageElement);
        }

        const descriptionElement = document.createElement("p");
        descriptionElement.textContent = announcement.description;
        
        if(announcement.currentPrice==-1){
        	displayedPrice=announcement.basePrice/10000;
        }else{
        	displayedPrice=announcement.currentPrice/10000;
        }

        const priceElement = document.createElement("p");
        priceElement.textContent = 'Current price : '+displayedPrice+' millions DA';
        
        priceElement.addEventListener('mouseover', function () {
        	endpoint = 'pair';
        	access_key = '4e49f7a1950bd0288f900493';
        	from = 'DZD';
        	to = 'EUR';
        	amount = displayedPrice*10000;
        	
        	url= 'https://v6.exchangerate-api.com/v6/' +access_key+'/'+endpoint+'/'+from+'/'+to;
        	
        	
        	var conversionRate=0.0;
        	fetch(url)	
            .then(response => response.json())
            .then(data => {
              // Extract the exchange rate from DZD to EUR
            	conversionRate=parseFloat(data.conversion_rate);
            	priceElement.style.color = 'red'; // For example, change text color to red
            	priceElement.textContent='Current price : '+parseFloat(amount*conversionRate)+' €';
            })
            .catch(error => {
              console.error('Error fetching exchange rates:', error);
            });
        	
        	
          });
        priceElement.addEventListener('mouseout', function () {
            // Your onmouseover function logic goes here
            priceElement.style.color = '#333'; // For example, change text color to red
            priceElement.textContent = 'Current price : '+displayedPrice+' millions DA';
          });

        const viewElement = document.createElement("p");
        viewElement.textContent = 'Views: '+announcement.view;
        
        const remainingTime = document.createElement("p");
        remainingTime.classList.add('time');
        remainingTime.textContent = 'RemainingTime :' +calculateRemainingTime(announcement.endDate);
        
     // Create the price display element
     
     	
     	
        
        // Append buttons to the body        
        

        // Add elements to the announcement container
        announcementElement.appendChild(titleElement);
        announcementElement.appendChild(imagesContainer);
        announcementElement.appendChild(descriptionElement);
        announcementElement.appendChild(priceElement);
        announcementElement.appendChild(viewElement);
        announcementElement.appendChild(remainingTime);
        
        const now = new Date().getTime();
        const targetTime = new Date(announcement.endDate).getTime();
        const timeDifference = targetTime - now;
		
        let bidPrice=displayedPrice*10000+announcement.step;
        if(timeDifference>=0){
	     	
	        const priceDisplay = document.createElement('div');
	        priceDisplay.classList.add('bidPrice');
	        priceDisplay.id = 'priceDisplay';
	        priceDisplay.textContent = 'Starting Price : ' + bidPrice/10000+' millions DA';
	
	        // Create the buttons
	        const decreaseButton =  document.createElement("button");
	        decreaseButton.textContent = "-";
	        decreaseButton.id='decreaseButton';
	        decreaseButton.onclick = () => {
	        	if(bidPrice-2*announcement.step>=displayedPrice*10000){
	        		bidPrice -= announcement.step; 
	                updatePriceDisplay();
	        	}
	        };
	        const increaseButton =  document.createElement("button");
	        increaseButton.textContent = "+";
	        increaseButton.id='increaseButton';
	        increaseButton.onclick = () => {
	        	bidPrice += announcement.step;
	            updatePriceDisplay();
	        };
	        const bidButton =  document.createElement("button");
	        bidButton.textContent = "bid";
	        bidButton.id='bidButton';
	        bidButton.onclick = () => {
	        	const listId=[2,[userIdFromSession,getParameterByName('id'),bidPrice,0,0]];
	    		var jsonList = JSON.stringify(listId);
	            socket.send(jsonList);
	            console.log(jsonList);
	        };	
	        announcementElement.appendChild(priceDisplay);
        	announcementElement.appendChild(decreaseButton);
        	announcementElement.appendChild(increaseButton);
        	announcementElement.appendChild(bidButton);
     	}
        else{
        	if(announcement.currentPrice>=announcement.reservePrice){
        		priceElement.textContent = 'Selled price : '+displayedPrice+' millions DA';
        	}
        	else{
        		priceElement.textContent = 'Not selled';
        	}
        }
        
        
        container.appendChild(announcementElement);
        function updatePriceDisplay() {
    		const countdownsElements = document.getElementsByClassName('bidPrice');
    		countdownsElements[0].textContent='Bid Price : ' + bidPrice/10000+' millions DA';
    	}
    };
    
    
    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }



    function calculateRemainingTime(targetDate) {
        const now = new Date().getTime();
        const targetTime = new Date(targetDate).getTime();
        const timeDifference = targetTime - now;

        if (timeDifference < 0) {
        	if(document.getElementById('priceDisplay')!==null){
        		document.getElementById('priceDisplay').remove();
        		document.getElementById('decreaseButton').remove();
        		document.getElementById('increaseButton').remove();
        		document.getElementById('bidButton').remove();
        	}
            return 'Expired';
        }

        const days = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
        const hours = Math.floor((timeDifference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

        return days+' days, '+hours+' hours, '+minutes+' minutes, '+seconds+' seconds';
    }
    
    function updateRemainingTimes() {
        const countdownsElements = document.getElementsByClassName('time');
        i=0;
        targetDates.forEach(targetDate => {
            const remainingTime = calculateRemainingTime(targetDate);
            countdownsElements[i].textContent='Remaining Time: '+remainingTime;
            i=i+1;
        });
    }





    function redirectToAnotherPage() {
        // Replace 'anotherPage.jsp' with the actual URL of the page you want to redirect to
        window.location.href = '${request.getContextPath}/ProjetAppDis/addAnnonce';
    }
</script>
</html>