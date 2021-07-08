<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!-- <div id="site-info"> -->
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
						<li class="horizontalNav_dropdownItem"><tags:logout /></li>
					</ul>
				</li>
				<li class="horizontalNav_item"></li>
			</ul>
		</nav>
            </c:when>
        </c:choose>     
