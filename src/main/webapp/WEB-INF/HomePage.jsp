<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
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

 <div id="announcements-container"></div>



</body>
<script>
    
    const d='ws://'+window.location.hostname+':'+window.location.port+'/ProjetAppDis/annonceEndPoint';
    socket = new WebSocket(d);
    targetDates=null;
    
    socket.addEventListener("open", (event) => {
		const listId=[0,[]];
		var jsonList = JSON.stringify(listId);
        socket.send(jsonList);
        console.log(jsonList);
    });
    
    socket.onmessage = function (event) {
        const announcements = JSON.parse(event.data);
        targetDates = announcements.map(announcement => announcement.endDate);
        renderAnnouncements(announcements);
        setInterval(updateRemainingTimes, 1000);
    };
    
    function createAnnouncementElement(announcement) {
        const announcementDiv = document.createElement('div');
        announcementDiv.classList.add('announcement');

        const leftContent = document.createElement('div');
        leftContent.classList.add('left-content');

        const title = document.createElement('div');
        title.classList.add('title');
        title.textContent = announcement.title;
        

        const image = document.createElement('img');
        image.classList.add('image');
        image.src = '/images/'+ announcement.imagePaths[0];
        image.alt = 'Announcement Image';

        leftContent.appendChild(title);
        leftContent.appendChild(image);

        const rightContent = document.createElement('div');
        rightContent.classList.add('right-content');

        if(announcement.currentPrice==-1){
        	displayedPrice=announcement.basePrice/10000;
        }else{
        	displayedPrice=announcement.currentPrice/10000;
        }
        
        const price = document.createElement('div');
        price.classList.add('price');
        price.textContent = 'Current price : '+displayedPrice+' millions DA';

        const time = document.createElement('div');
        time.classList.add('time');
        time.textContent = 'Remaining Time: '+ calculateRemainingTime(announcement.endDate);

        rightContent.appendChild(price);
        rightContent.appendChild(time);

        announcementDiv.appendChild(leftContent);
        announcementDiv.appendChild(rightContent);
        
        announcementDiv.addEventListener("click", function() {
            window.location.href = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/ProjetAppDis/annonce?id='+announcement.id;
        });
        
        const now = new Date().getTime();
        const targetTime = new Date(announcement.endDate).getTime();
        const timeDifference = targetTime - now;
		
        let bidPrice=displayedPrice*10000+announcement.step;
        if(timeDifference<0){
        	if(announcement.currentPrice>=announcement.reservePrice){
        		price.textContent = 'Selled price : '+displayedPrice+' millions DA';
    	}
    		else{
    			price.textContent = 'Not selled';
    		}
        }
        

        return announcementDiv;
    }
    function renderAnnouncements(annoncements) {
        const announcementsContainer = document.getElementById('announcements-container');

        // Clear existing content
        announcementsContainer.innerHTML = '';

        // Iterate through announcementsData and create elements
        annoncements.forEach(announcement => {
            const announcementElement = createAnnouncementElement(announcement);
            announcementsContainer.appendChild(announcementElement);
        });
    }
    
    function calculateRemainingTime(targetDate) {
        const now = new Date().getTime();
        const targetTime = new Date(targetDate).getTime();
        const timeDifference = targetTime - now;

        if (timeDifference < 0) {
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