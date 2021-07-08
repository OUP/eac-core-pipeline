<%@ attribute name="sortColumnBy"  %>
<%@ attribute name="formName"  %>
<%@ attribute name="label"  %>
<c:choose>
    <c:when test="${paging.sortColumn == sortColumnBy}">
	    <th class="yui3-datatable-sortable yui3-datatable-<c:out value="${paging.sortDirection.nameLowerCase}"/>">
	        <div class="yui3-datatable-liner" onclick="return sortColumn('${formName}', '${sortColumnBy}', '<c:out value="${paging.sortDirection.opposite}"/>');"><spring:message code="${label}" text="?${label}?"/></div>
	    </th>
    </c:when>
    <c:otherwise>
	    <th class="yui3-datatable-sortable">
	        <div class="yui3-datatable-liner" onclick="return sortColumn('${formName}', '${sortColumnBy}', 'ASC');"><spring:message code="${label}" text="?${label}?"/></div>
	    </th>
    </c:otherwise>
</c:choose>