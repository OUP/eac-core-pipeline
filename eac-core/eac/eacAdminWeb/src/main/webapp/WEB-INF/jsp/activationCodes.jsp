<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<table style="float: left;">
    <thead>
        <tr>
            <th>Code</th>
            <th>Product</th>
            <th>Available</th>
        </tr>
    </thead>
    <c:forEach var="activationCode" items="${activationCodes}" varStatus="status">
        <tr>
            <td class="role_row">${activationCode.code}</td>
            <td class="role_row">${activationCode.productName}</td>
            <td class="role_row"><c:choose><c:when test="${activationCode.available}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
        </tr>  
    </c:forEach>
</table>

