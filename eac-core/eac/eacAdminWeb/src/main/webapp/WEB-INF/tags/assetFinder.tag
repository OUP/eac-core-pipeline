<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%-- This is the id of a hidden form field into which the selected GUID will be set.
		This is a mandatory attribute for this tag. --%>
<%@ attribute name="returnFieldId" required="true" %>

<%-- This is the id of a hidden form field into which the selected product name will
		be set. This is an optional attribute. --%>
<%@ attribute name="returnNameFieldId" required="false" %>

<%-- This is the GUID of the asset which will be used to initialise the asset finder with
		when it first loads. If not specified, the asset finder will render in an uninitialised 
		state with the text 'Please select...' displayed. --%>
<%@ attribute name="initGuid" required="false" %>

<%-- This is the id of a hidden form field which is monitored for onchange events. 
		If this attribute is set, then when this hidden field changes, the asset finder 
		will reinitialise itself based on the value of the field (which should be the 
		GUID of the asset). --%>
<%@ attribute name="initOnChangeId" required="false" %>

<%-- This is the id of the button which is used to reset the form in the page within which the
		asset finder exists. This allows the asset finder to be reset along with all of
		the other form components. This is an optional attribute. --%>
<%@ attribute name="resetButtonId" required="false" %>

<%-- optional comma separated list of Asset States : ACTIVE, SUSPENDED, REMOVED, RETIRED --%>
<%@ attribute name="assetStates"   required="false" %>

<jsp:include page="/WEB-INF/jsp/abstractAssetProductFinder.jsp">
	<jsp:param name="mode" value="asset"/>
	<jsp:param name="returnFieldId" value="${returnFieldId}"/>
	<jsp:param name="returnNameFieldId" value="${returnNameFieldId}"/>
	<jsp:param name="initGuid" value="${initGuid}"/>
	<jsp:param name="initOnChangeId" value="${initOnChangeId}"/>
	<jsp:param name="resetButtonId" value="${resetButtonId}"/>
	<jsp:param name="assetStates" value="${assetStates}"/>
</jsp:include>