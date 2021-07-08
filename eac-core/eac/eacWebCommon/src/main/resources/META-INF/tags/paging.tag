<%@ attribute name="formName"  %>
<c:if test="${paging.numberOfPages > 1}">
<div class="paging">
    <ul>
        <c:choose>
            <c:when test="${paging.requestedPage != paging.firstPage}">
                <li><a href="#" onclick="return changePage('${formName}', <c:out value="${paging.firstPage}"/>);">&lt;&lt;</a></li>
            </c:when>
            <c:otherwise>
                <li>&lt;&lt;</li>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${paging.previousPage != null}">
                <li><a href="#" onclick="return changePage('${formName}', <c:out value="${paging.previousPage}"/>);">&lt;</a></li>
            </c:when>
            <c:otherwise>
                <li>&lt;</li>
            </c:otherwise>
        </c:choose>
        <c:forEach items="${paging.pages}" var="page" varStatus="status">
	        <c:choose>
	            <c:when test="${page == paging.requestedPage}">
	                <li class="current"><c:out value="${page}"/></li>>
	            </c:when>
	            <c:otherwise>
	                <li><a href="#" onclick="return changePage('${formName}', <c:out value="${paging.nextPage}"/><c:out value="${page}"/>);"><c:out value="${page}"/></a></li>
	            </c:otherwise>
	        </c:choose>        
        </c:forEach>
        <c:choose>
            <c:when test="${paging.nextPage != null}">
                <li><a href="#" onclick="return changePage('${formName}', <c:out value="${paging.nextPage}"/>);">&gt;</a></li>
            </c:when>
            <c:otherwise>
                <li>&gt;</li>
            </c:otherwise>
        </c:choose> 
        <c:choose>
            <c:when test="${paging.requestedPage != paging.lastPage}">
                <li><a href="#" onclick="return changePage('${formName}', <c:out value="${paging.lastPage}"/>);">&gt;&gt;</a></li>
            </c:when>
            <c:otherwise>
                <li>&gt;&gt;</li>
            </c:otherwise>
        </c:choose> 
    </ul>
</div>
</c:if>
<form:hidden id="pagingCriteria.sortColumn" path="pagingCriteria.sortColumn"/>
<form:hidden id="pagingCriteria.sortDirection" path="pagingCriteria.sortDirection"/>
<form:hidden id="pagingCriteria.requestedPage" path="pagingCriteria.requestedPage"/>
