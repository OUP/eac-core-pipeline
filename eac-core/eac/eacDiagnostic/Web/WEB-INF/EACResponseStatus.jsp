

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<link type="text/css" rel="stylesheet" href="styles/style.css" id="responsiveCSS" >
<%-- <link type="text/css" rel="stylesheet" href="css/screen.css" id="responsivescreenCSS" > --%>
<link type="text/css" rel="stylesheet" href="styles/eacdiagnostic.css" id="responsiveDiagCSS">
<link rel="icon" type="image/x-icon" href="images/favicon.ico"/>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<fmt:setBundle var="mybundle" basename="resources.Labels"/>
<fmt:message var="toolrefresh" key="label.eac.diagnostic.refresh" bundle="${mybundle}"/>
<meta http-equiv="refresh" content="${toolrefresh}" >


<fmt:message var="eacdiagnostictool" key="label.eac.diagnostic.tool" bundle="${mybundle}"/>
<fmt:message var="eacdiagnosticheading" key="label.eac.diagnostic.heading" bundle="${mybundle}"/>
<fmt:message var="concateenvironment" key="label.eac.diagnostic.concate.heading.environment" bundle="${mybundle}"/>
<fmt:message var="logout" key="label.eac.diagnostic.logout" bundle="${mybundle}"/>
<fmt:message var="welcome" key="label.eac.diagnostic.welcome" bundle="${mybundle}"/>



<fmt:message var="eacwsdl" key="label.eac.wsdl" bundle="${mybundle}"/>
<fmt:message var="eacweb" key="label.eac.web" bundle="${mybundle}"/>
<fmt:message var="eacinstance" key="label.eac.instance" bundle="${mybundle}"/>
<fmt:message var="atyponwsdl" key="label.atypon.wsdl" bundle="${mybundle}"/>
<fmt:message var="eacaccounts" key="label.eac.accounts" bundle="${mybundle}"/>
<fmt:message var="eacwebservice" key="label.eac.webservice" bundle="${mybundle}"/>
<fmt:message var="eacskin" key="label.eac.skin" bundle="${mybundle}"/>
<fmt:message var="eacadmin" key="label.eac.admin" bundle="${mybundle}"/>
<fmt:message var="eaccheckauth" key="label.eac.checkauth" bundle="${mybundle}"/>
<fmt:message var="atyponadmin" key="label.atypon.admin" bundle="${mybundle}"/>
<fmt:message var="atyponadapter" key="label.atypon.adapter" bundle="${mybundle}"/>
<fmt:message var="atyponinstance" key="label.atypon.instance" bundle="${mybundle}"/>
<fmt:message var="atyponwsdl" key="label.atypon.wsdl" bundle="${mybundle}"/>
<fmt:message var="atyponcheckauth" key="label.atypon.checkauth" bundle="${mybundle}"/>
<fmt:message var="dbconnectioneac" key="label.connection.eac" bundle="${mybundle}"/>
<fmt:message var="dbconnectionatypon" key="label.connection.atypon" bundle="${mybundle}"/>





<title><c:out value="${eacdiagnostictool}"></c:out></title>
<style>
 .container{width:950px; margin:0px auto;}
 table{margin-bottom: 50px;}
</style>
</head>
<body>




<c:if test="${empty sessionScope.diagnosticSessionId}">
<c:redirect url="/"/>
</c:if>

<%ArrayList Response=(ArrayList)request.getAttribute("ResponseDeatilsList");
pageContext.setAttribute("responses", Response);
ArrayList responseListAtypon=(ArrayList)request.getAttribute("responseListAtypon");
pageContext.setAttribute("responseListAtypon", responseListAtypon);
ArrayList responseListMiscellaneous=(ArrayList)request.getAttribute("responseListMiscellaneous");
pageContext.setAttribute("responseListMiscellaneous", responseListMiscellaneous);

%>
<div class="container">
<div id="topbar">
					
						
						<dl class="dropdown">
							<dt><a href="#" style="background: url(&quot;images/arrow.png&quot;) 100% 50% no-repeat scroll;"><span class="shorten" style="visibility: visible; display: block; white-space: nowrap;"><c:out value="${welcome}"/> <c:out value="${sessionScope.username}"/></span></a></dt>
							<dd>
								<ul style="display: none;">
									<li><a href="/eacDiagnostic/logout"><c:out value="${logout}"/></a></li>
								</ul>
							</dd>
						</dl>
					
	</div>
<div id="logo">
	                <div class="sub-heading">
						
							<img src="images/logo.gif" alt="Oxford University Press">
						
                        <span class="sub-heading-1"><c:out value="${eacdiagnosticheading}"/>
                         <% if (request.getParameter("Environment") != null && !request.getParameter("Environment").equals("")) {
						        out.println(request.getParameter("Environment"));
						        }
						  %>
                         <c:out value="${concateenvironment}"/></span>
	                </div>
</div>

<table align="left" border="0" bordercolor="black" style="width: 100%">

    <tr class="menutitle">
        <td><h1><c:out value="${eacinstance}"/></h1></td>
        <td ><h1><c:out value="${eacweb}"/></h1></td>
        <td ><h1><c:out value="${eacadmin}"/></h1></td>
        <td ><h1><c:out value="${eacaccounts}"/></h1></td>
        <td ><h1><c:out value="${eacwsdl}"/></h1></td>
        <!-- <td ><h1><c:out value="${eaccheckauth}"/></h1></th> -->
        <!-- <th ><c:out value="${eacskin}"/></th> --> 
     </tr>
     
     <c:forEach items="${responses}" var="current">
      <tr>
      <td bgcolor="#133E5F"><h1>
      <c:out value="${current.instanceName}"/>
      </h1>
      </td>
      <%--For eac web --%>      
      <c:if test="${current.eacWebResponse eq 'PASS'}">
         <td bgcolor="#66CD00"><b>${current.eacWebResponse}</b></td>
      </c:if>
       <c:if test="${current.eacWebResponse eq 'FAIL'}">
      <td bgcolor="red"><b>${current.eacWebResponse}</b></td>
      </c:if>
      <%--For eac web end --%>
      <%--For eac Admin --%>
       <c:if test="${current.eacAdminResponse eq 'PASS'}">
         <td bgcolor="#66CD00"><b>${current.eacAdminResponse}</b></td>
      </c:if>
       <c:if test="${current.eacAdminResponse eq 'FAIL'}">
      <td bgcolor="red"><b>${current.eacAdminResponse}</b></td>
      </c:if>
      <%--For eac Admin end--%>
      <%--For eac Account--%>
       <c:if test="${current.eacAccountResponse eq 'PASS'}">
         <td bgcolor="#66CD00"><b>${current.eacAccountResponse}</b></td>
      </c:if>
       <c:if test="${current.eacAccountResponse eq 'FAIL'}">
      <td bgcolor="red"><b>${current.eacAccountResponse}</b></td>
      </c:if>
      <%--For eac Account end--%>
      <%--For eac WSDL--%>
       <c:if test="${current.eacWSDLResponse eq 'PASS'}">
         <td bgcolor="#66CD00"><b>${current.eacWSDLResponse}</b></td>
      </c:if>
       <c:if test="${current.eacWSDLResponse eq 'FAIL'}">
      <td bgcolor="red"><b>${current.eacWSDLResponse}</b></td>
      </c:if>
      <%--For eac WSDLend--%>
      <!-- <td bgcolor="orange"><b>NOT CHECKED</b></td> -->
      </tr>
       
     </c:forEach> 

	
</table>


<table align="left" border="0" bordercolor="black" style="width: 100%">
    <tr class="menutitle">
        <td><h1><c:out value="${atyponinstance}"/></h1></td>
        <td><h1><c:out value="${atyponwsdl}"/></h1></td>
        <td><h1><c:out value="${atyponadmin}"/></h1></td>
        <!--  <td><h1>Atypon Adapter</h1></td> -->
    </tr>
     <c:forEach items="${responseListAtypon}" var="current">
      <tr>
      <td bgcolor="#133E5F"><h1>
      <c:out value="${current.instanceName}"/>
      </h1>
      </td>
      <%--For Atypon wsdl --%>      
      <c:if test="${current.eacWSDLResponse eq 'PASS'}">
         <td bgcolor="#66CD00"><b>${current.eacWSDLResponse}</b></td>
      </c:if>
       <c:if test="${current.eacWSDLResponse eq 'FAIL'}">
      <td bgcolor="red"><b>${current.eacWSDLResponse}</b></td>
      </c:if>
      <%--For  Atypon wsdl end --%>
      <%--For Atypon Admin --%>
       <c:if test="${current.eacAdminResponse eq 'PASS'}">
         <td bgcolor="#66CD00"><b>${current.eacAdminResponse}</b></td>
      </c:if>
       <c:if test="${current.eacAdminResponse eq 'FAIL'}">
      <td bgcolor="red"><b>${current.eacAdminResponse}</b></td>
      </c:if>
      <%--For Atypon Admin end--%>
      <!-- <td bgcolor="orange"><b>NOT CHECKED</b></td> -->
      </tr>
     </c:forEach>  
   
</table>

<table align="left" border="0" bordercolor="black" style="width: 100%">

 	<c:forEach items="${responseListMiscellaneous}" var="current">
	      <tr>
	      <td bgcolor="#133E5F"><h1><c:out value="${atyponcheckauth}"/> </h1></td>
	      <%--For Atypon wsdl --%>      
	      <c:if test="${current.eacCheckAuthResponse eq 'PASS'}">
	         <td bgcolor="#66CD00"><b>${current.eacCheckAuthResponse}</b></td>
	      </c:if>
	       <c:if test="${current.eacCheckAuthResponse eq 'FAIL'}">
	      <td bgcolor="red"><b>${current.eacCheckAuthResponse}</b></td>
	      </c:if>
	      <%--For  Atypon wsdl end --%>
	      </tr>
     
     <tr>    
        <td bgcolor="#133E5F"><h1><c:out value="${dbconnectioneac}"/></h1></td>
        <c:if test="${current.eacDBStatus eq 'PASS'}">
	         <td bgcolor="#66CD00"><b>${current.eacDBStatus}</b></td>
	      </c:if>
	       <c:if test="${current.eacDBStatus eq 'FAIL'}">
	      <td bgcolor="red"><b>${current.eacDBStatus}</b></td>
	      </c:if>
      </tr>
     <tr>
        <td bgcolor="#133E5F"><h1><c:out value="${dbconnectionatypon}"/></h1></td>
        <c:if test="${current.atyponDBStatus eq 'PASS'}">
	         <td bgcolor="#66CD00"><b>${current.atyponDBStatus}</b></td>
	      </c:if>
	       <c:if test="${current.atyponDBStatus eq 'FAIL'}">
	      <td bgcolor="red"><b>${current.atyponDBStatus}</b></td>
	      </c:if>
    </tr>
    </c:forEach>  
</table>
</div>
</body>
<script type="text/javascript">
        	$(function() {
        		var $dropdownTitle = $('.dropdown').find('dt').find('a'); 
        		var $dropdownList = $('.dropdown').find('dd').find('ul');
        		var ddTitleHideCss = {'background-color': 'none',
        							  'background': 'url(images/arrow.png) no-repeat scroll right center'};
        		var ddTitleShowCss = {'background': '#CCCCCC url(images/arrow_dark.png) no-repeat scroll right center'}
        		
                $dropdownTitle.mouseenter(function() {
                	showDropDown();
                });
                
                $dropdownTitle.mouseleave(function(e) {
                	if (!$(e.relatedTarget).parents().hasClass('dropdown')) {
                		hideDropDown();
                	}
                });
                
                $dropdownList.mouseleave(function(e) {
                	hideDropDown();
                });
                
                $('.dropdown dd ul li a').click(function() {
                	hideDropDown();
                });
                
                function showDropDown() {
                	$dropdownList.show();
                   	$dropdownTitle.css(ddTitleShowCss);
                }
                
                function hideDropDown() {
            		$dropdownList.hide();
               		$dropdownTitle.css(ddTitleHideCss);
                }
        	});
        </script>
</html>