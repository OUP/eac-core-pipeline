<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/cookieBanner.jsp" %>
<eac:skinUrl var="homeURL" />
<eac:skinSiteName var="homeSiteName" />

<header class="minimalHeader">
			
			<div class="row">
				<div class="col">
					<div class="topNav">
						<c:if test="${applicationScope.availableLanguages != null && applicationScope.availableLanguages.size > 1}">
    <ul>
		<li>
            <spring:message code="label.language" text="?label.language?" /> :
        </li>        
    	<c:forEach items="${applicationScope.availableLanguages}" var="availableLang">
	        <li> 
		        <c:choose>
		           <c:when test="${pageContext['response'].locale == availableLang.locale}">
	                   <spring:message code="${availableLang.labelCode}" text="${availableLang.defaultLabel}" />
		           </c:when>
		           <c:otherwise>
		               <a href="?locale=${availableLang.locale}">
		                   <spring:message code="${availableLang.labelCode}" text="${availableLang.defaultLabel}" />
		               </a>
		           </c:otherwise>
		        </c:choose>
	        </li>	        
	    </c:forEach>
    </ul>
    </c:if>
        <eac:isLoggedIn var="isLoggedIn" />
        <eac:isError var="isError" />
        <c:choose >
            <c:when test="${ (not isError) and isLoggedIn}">
            <spring:message var="hello" code="label.hello" text="?label.hello?" />
             <spring:message var="viewProfile" code="label.view.profile" text="?label.view.profile?" />
		<nav class="flRight">
			<ul class="horizontalNav flLeft">
				<li class="horizontalNav_item horizontalNav_item--hasDropdown">
					${hello} <strong>${fn:escapeXml(webUserName)}</strong>
					<ul class="horizontalNav_dropdown list list--lined">
						<li class="horizontalNav_dropdownItem"><a class="arrowBefore"
							href="/eac/profile.htm">${viewProfile}</a></li>
										<li class="horizontalNav_dropdownItem"><spring:message
												var="logout" code="label.logout" text="?label.logout?" /> <c:if
												test="${lower eq 'true'}">
												<c:set var="logout" value="${fn:toLowerCase(logout)}" />
											</c:if> <eac:logoutLandingUrl var="logoutLandingUrl" /> <a
											class="arrowBefore" href="logout.htm?url=${logoutLandingUrl}">${logout}</a></li>
									</ul>
				</li>
				<li class="horizontalNav_item"></li>
			</ul>
		</nav>

            </c:when>
            <c:otherwise>
					<nav class="flRight">
						<ul class="horizontalNav flLeft">
							<li class="horizontalNav_item"><a href="#">&nbsp;</a>
							</li>
							</ul>
						</nav>
            </c:otherwise>
        </c:choose>
					</div>
					<div class="row">
						<div class="col large-7 siteTitleCol">
							<div class="siteTitle">
								<a class="siteTitle_logo" href="${homeURL}"> <img class="siteTitle_logoImg" src="images/oup-logo-blue.svg" alt="Oxford University Press" width="170" height="48" /> </a> <span class="siteTitle_headings">
										<%-- <span class="siteTitle_division"><a title="${homeSiteName}" href="${homeURL}">${homeSiteName}<!-- </a> --></span> 
										<span class="siteTitle_country">&nbsp;</span> </span> --%>
										<c:choose>
										 <c:when test="${homeSiteName eq 'Oxford University Press'}">
										<span class="siteTitle_division">&nbsp;</span>
										</c:when>
										<c:otherwise>
										<span class="siteTitle_division">${homeSiteName}</span>
										</c:otherwise>
										</c:choose>
										 <span class="siteTitle_country">&nbsp;</span> </span>
							</div>
						</div>
					</div>
					<eac:homeMenuLinkGenerator var="homeMenuLinks"/>
			
			
				<nav class="hideForSmall">
					<ul class="mainNav">
						<li class="mainNav_item mainNav_item--hasDropdownMenu"><a
							href="javascript:void(0);"><spring:message var="homeLabel"
									code="label.home" text="?label.home?" /></a>
									
								<c:choose>
								<c:when test="${fn:length(homeMenuLinks) <= 1}">
									<a class="mainNav_link" href="${homeMenuLinks[0].url}">${homeLabel}</a>
								</c:when>
								<c:when test="${not empty url}">
									<p>&nbsp;</p>
								</c:when>
								<c:otherwise>
									<a class="mainNav_link mainNav_link--hasDropdownMenu" href="#">${homeLabel}</a>
									<div class="mainNav_dropdownMenu">
										<ul class="list list--lined">
											<c:forEach items="${homeMenuLinks}" var="menuLink">
												<li class=""><a class="arrowBefore"
													href="${menuLink.url}"><strong>${menuLink.siteName}</strong></a></li>
											</c:forEach>
										</ul>
									</div>
								</c:otherwise>
							</c:choose>
							</li>
					</ul>
				</nav>
			
		</div>
			</div>
		</header>
		<nav class="topBar">
		<ul class="topBar_menu">
		<c:choose >
            <c:when test="${ (not isError) and isLoggedIn}">
            <spring:message var="hello" code="label.hello" text="?label.hello?" />
             <spring:message var="viewProfile" code="label.view.profile" text="?label.view.profile?" />
			<li class="topBar_menuItem topBar_menuItem--userMenu">
				<a class="topBar_menuLink topBar_menuLink--userMenu" data-top-bar-menu-trigger="topBar_dropdownMenuUser"> <span class="topBar_icon topBar_icon--user">User Menu</span> <span class="topBar_welcomeMessage">${hello} <strong>${fn:escapeXml(webUserName)}</strong></span> </a>
				<ul id="topBar_dropdownMenuUser" class="topBar_dropdownMenu">
					<li class="topBar_dropdownMenuItem"> 
					<c:if test="${lower eq 'true'}">
												<c:set var="logout" value="${fn:toLowerCase(logout)}" />
					</c:if> <eac:logoutLandingUrl var="logoutLandingUrl" />
					<a class="topBar_dropdownMenuLink topBar_dropdownMenuLink--secondary" href="logout.htm?url=${logoutLandingUrl}">${logout}</a> 
					
					</li>
					<li class="topBar_dropdownMenuItem"> <a class="topBar_dropdownMenuLink topBar_dropdownMenuLink--secondary" href="/eac/profile.htm">${viewProfile}</a> </li>
					<li class="topBar_dropdownMenuItem"> TEST</li>
				</ul>
			</li>
			</c:when>
			</c:choose>
			<li class="topBar_menuItem topBar_menuItem--mainMenu">
				<a class="topBar_menuLink" data-top-bar-menu-trigger="topBar_dropdownMenuMain"> <span class="topBar_icon topBar_icon--menu">Menu</span> </a>
				<ul id="topBar_dropdownMenuMain" class="topBar_dropdownMenu">
					<c:choose>
			<c:when test="${fn:length(homeMenuLinks) <= 1}">
					<li class="topBar_dropdownMenuItem"> <a class="topBar_dropdownMenuLink" href="${homeMenuLinks[0].url}">${homeLabel}</a> 
					</li>
				</c:when>
					<c:otherwise>
					<li class="topBar_dropdownMenuItem"> <a class="topBar_dropdownMenuLink" href="#">${homeLabel}</a> 
					</li>
					<c:forEach items="${homeMenuLinks}" var="menuLink">
							<li class="topBar_dropdownMenuItem"><a class="topBar_dropdownMenuLink" href="${menuLink.url}">${menuLink.siteName}</a></li>
						</c:forEach>
			 	</c:otherwise>
				</c:choose>		
				</ul>
			</li>
		</ul>
	</nav> 
