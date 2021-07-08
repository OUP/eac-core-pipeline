<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%-- The returnFieldId is the id of a hidden form field into which the selected GUID will be set.
		This is a mandatory attribute for this tag. --%>
<%@ attribute name="returnFieldId" required="true" %>

<%-- This is the id of a hidden form field into which the selected product name will
		be set. This is an optional attribute. --%>
<%@ attribute name="returnNameFieldId" required="false" %>

<%-- This is the RegistrationDefinitionType to filter by - one of ACTIVATION_CODE_REGISTRATION
		or PRODUCT_REGISTRATION. If not set then both RegistrationDefinitionTypes are searched. --%>
<%-- <%@ attribute name="type" required="false" %> --%>

<%-- This is the GUID of the product which will be used to initialise the product finder with
		when it first loads. If not specified, the product finder will render in an uninitialised 
		state with the text 'Please select...' displayed. --%>
<%@ attribute name="initGuid" required="false" %>

<%-- This is the id of a hidden form field which is monitored for onchange events. 
		If this attribute is set, then when this hidden field changes, the product finder 
		will reinitialise itself based on the value of the field (which should be the 
		GUID of the product). --%>
<%@ attribute name="initOnChangeId" required="false" %>

<%-- This is the id of the button which is used to reset the form in the page within which the
		product finder exists. This allows the product finder to be reset along with all of
		the other form components. This is an optional attribute. --%>
<%@ attribute name="resetButtonId" required="false" %>

<%-- optional comma separated list of Product States : ACTIVE, SUSPENDED, REMOVED, RETIRED --%>
<%-- <%@ attribute name="productStates"   required="false" %> --%>

<jsp:include page="/WEB-INF/jsp/abstractEacGroupFinder.jsp">
	<jsp:param name="mode" value="eacGroups"/>
	<jsp:param name="returnFieldId" value="${returnFieldId}"/>
	<jsp:param name="returnNameFieldId" value="${returnNameFieldId}"/>
	<jsp:param name="initGuid" value="${initGuid}"/>
	<jsp:param name="initOnChangeId" value="${initOnChangeId}"/>
	<jsp:param name="resetButtonId" value="${resetButtonId}"/>
</jsp:include>