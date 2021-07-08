<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<table style="float: left; width: 650px;">
    <c:forEach var="permissionSelection" items="${permissions}" varStatus="status">
        <tr>
            <td class="role_row" style="width: 30px;">
                <input id="${permissionSelection.permission.id}" type="checkbox" style="display: inline;float: left;position: static;" value="true" name="permissionSelections[${status.index}].access" <c:if test="${permissionSelection.access}">checked="checked"</c:if>>
                <input type="hidden" value="on" name="_permissionSelections[${status.index}].access">
                <input id="permissionSelections${status.index}.permission.id" type="hidden" value="${permissionSelection.permission.id}" name="permissionSelections[${status.index}].permission.id">
                <input id="permissionSelections${status.index}.permission.name" type="hidden" value="${permissionSelection.permission.name}" name="permissionSelections[${status.index}].permission.name">
            </td>
            <td class="role_row" style="width: 500px;"><label for="${permissionSelection.permission.id}" style="font-weight: normal"><c:out value="${permissionSelection.permission.name}"/></label></td>
        </tr>  
    </c:forEach>
</table>
<div>
    <input type="hidden" name="deletable" id="deletable" value="${deletable}"/>
</div>

