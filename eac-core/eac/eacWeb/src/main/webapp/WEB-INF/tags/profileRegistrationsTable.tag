<%@ tag import="org.joda.time.format.DateTimeFormat"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ attribute name="id" required="true" type="java.lang.String" rtexprvalue="true" %>
<%@ attribute name="profileRegistrations" required="true" type="java.util.List" rtexprvalue="true" %>
<%@ attribute name="heightPx" required="true" type="java.lang.Integer" rtexprvalue="true" %>
<%@ attribute name="title" required="true" type="java.lang.String" rtexprvalue="true" %>
<%@ attribute name="readOnly" required="true" type="java.lang.Boolean" rtexprvalue="true" %>
        <H2 id="tableTitle">${title}</H2>
        <table id="${id}" class="eacTable">            
            <thead>
            <tr>
                <th><spring:message code="profile.basic.product.name" text="?profile.basic.product.name?" /></th>
                <th><spring:message code="profile.basic.status" text="?profile.basic.status?" /></th>
                <th><spring:message code="profile.basic.licence" text="?profile.basic.licence?" /></th>
                <th><spring:message code="profile.basic.manage" text="?profile.basic.manage?" /></th>
            </tr>
            </thead>
            <!-- <tfoot/> -->
            <tbody>            
            <c:forEach var="profileRegistration" items="${profileRegistrations}">
               <c:set var="regCode" value="${profileRegistration.registrationStatus.messageCode}" />
               <c:set var="licCode" value="${profileRegistration.licenceStatus.messageCode}" />
               <c:set var="statusCode" value="${regCode}" />
                <c:if test="${profileRegistration.registrationStatus == 'ACTIVATED'}">
                    <c:set var="statusCode" value="${licCode}" />
                </c:if>
                <tr >
                	<eac:productHomeUrl var="xProductHomeURL" product="${profileRegistration.registration.registrationDefinition.product}"/>
                	<c:set var="productName" value="${profileRegistration.productName}" />
                    <td >
                    	<c:choose>
                    		<c:when test="${empty xProductHomeURL}">
                    			<strong>${productName}</strong>
                			</c:when>
                			<c:otherwise>
                				<strong><a href="${xProductHomeURL}">${productName}</a></strong>
                			</c:otherwise>
                		</c:choose>
                    </td>
                    <td><span class="hideForLarge hideForMedium"><strong>Status:</strong></span><spring:message code="${statusCode}" text="?${statusCode}?" /></td>
                    <eac-common:licenceDescription licenceDto="${profileRegistration.licenceDto}" var="desc" generateHtml="${true}"/>
                    <td><c:if test="${not empty desc }"><span class="hideForLarge hideForMedium"><strong>Licence:</strong></span></c:if>${desc}</td>
                    <td>
                        <c:choose>
                            <c:when test="${profileRegistration.isManageable}">
                                <c:choose>
                                  <c:when test="${readOnly}">
                                    <spring:message var="updateRegistrationTitle" code="title.productregistration.read.only" text="?title.productregistration.read.only?" />
                                  </c:when>
                                  <c:otherwise>
                            	    <spring:message var="updateRegistrationTitle" code="profile.basic.update.registration" text="?profile.basic.update.registration?" />
                            	  </c:otherwise>
                            	</c:choose>
                                <a href="modifyProductRegistration.htm?registrationId=${profileRegistration.registrationId}"><%-- <img id="updateRegistrationIcon" title="${updateRegistrationTitle}" alt="${updateRegistrationTitle}" src="images/update-reg.png"/> --%>Edit</a>
                            </c:when>
                            <c:otherwise>&nbsp;</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
         </table>
<%-- <spring:message var="sInfo"       code="profile.basic.table.info"      text="?profile.basic.table.info?" />
<spring:message var="sInfoEmpty"  code="profile.basic.table.info.none" text="?profile.basic.table.info.none?" />
<spring:message var="sEmptyTable" code="profile.basic.table.empty"     text="?profile.basic.table.empty?" /> --%>
<!-- <script type="text/javascript">
    /* <![CDATA[ */
    $(function(){    	
        $('#${id}').dataTable({
            "aoColumnDefs":[
            {"aTargets":[0],"bSortable":true},
            {"aTargets":[1],"bSortable":true},
            {"aTargets":[2],"bSortable":false},
            {"aTargets":[3],"bSortable":true}
            ],
            "sDom": '<"wrapper">tS<>',
            "sScrollY": "${heightPx}px",
            "bScrollCollapse": true,
            "bPaginate": false,
            "bDeferRender": false,
            "bAutoWidth": false,
            "oLanguage": {
                "sInfo": "${sInfo}",
                "sInfoEmpty": "${sInfoEmpty}",
                "sEmptyTable": "${sEmptyTable}"
              }
            });
        });
    /* ]]> */
</script>
 -->