<%@ include file="/WEB-INF/jsp/include.jsp" %>
<eac:skinUrl var="homeURL" />
<eac:skinSiteName var="homeSiteName" />
<div id="header">
    <div id="header-top">
        <div id="header-top-left">
        </div>
        <div id="header-top-middle">
        </div>
        <div id="header-top-right">
            <%@ include file="/WEB-INF/jsp/siteInfo.jsp" %>
        </div>
    </div>
    <div id="header-middle">
        <div id="header-middle-left">
            <a title="${homeSiteName}" href="${homeURL}">${homeSiteName}</a>
        </div>
        <div id="header-middle-middle">
        </div>
        <div id="header-middle-right">
        </div>
    </div>
    <div id="header-home">
		<tags:navbar />
    </div>
    <div id="header-bottom">
        <div id="header-bottom-left"> 
        </div>
        <div id="header-bottom-middle">
        </div>
        <div id="header-bottom-right">
        </div>
    </div> 
</div>
