<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
	
<div id="help">
	This facility allows you to search for activation codes. 
	 <br/>
       <br/>
       <%--  The following fields support partial matching:
        <ul>
            <li><spring:message code="label.activationCode" /></li>
        </ul> --%>
</div>
	
<div style="float:left; margin-right: 1em">
   	<tiles:insertAttribute name="activationCodeSearchFormTile"/>
</div>

<tiles:insertAttribute name="searchControlTile"/>

