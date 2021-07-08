<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.ArrayList"%>
    
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%ArrayList Response=(ArrayList)request.getAttribute("ResponseDeatilsList");
pageContext.setAttribute("responses", Response);
%>
<table border="1" >
    <tr>
        <td bgcolor="blue">EAC instance</td>
        <td>EAC wsdl</td>
        <td>Atypon wsdl</td>
        <td>Eac admin</td>
        <td>EAC eac</td>
        <td>EAC accounts</td>
        <td>EAC webservice</td>
        <td>EAC skin</td>
    </tr>
  
      <!--  <td>EAC wsdl resolves</td> -->
      <c:forEach items="${responses}" var="current">
      <tr>
      <td>
      <c:out value="${current.instancid}"/>
      </td>
      <c:if test="${current.response>101 & current.project=='eacwsdl'}">
         <td bgcolor="green"></td>
      </c:if>
      
      </tr>
      </c:forEach>
    
     <%-- <tr>
       <td>Atypon wsdl resolves</td>
       <c:forEach var="i" begin="1" end="4">
        <td bgcolor="green">Item <c:out value="${i}"/></td>
           </c:forEach>
     </tr>
     <tr>
       <td>Each eac application</td>
       <c:forEach var="i" begin="1" end="4">
        
            <td bgcolor="red">Item <c:out value="${i}"/></td>
           
        
      </c:forEach>
     </tr>
         <tr>
       <td>Eac admin resolves</td>
       <c:forEach var="i" begin="1" end="4">
        
            <td bgcolor="red">Item <c:out value="${i}"/></td>
           
        
      </c:forEach>
     </tr>
        <tr>
       <td>Eac eac resolves</td>
       <c:forEach var="i" begin="1" end="4">
        
            <td bgcolor="red">Item <c:out value="${i}"/></td>
           
        
      </c:forEach>
     </tr>
      <tr>
       <td>Eac webservice resolves</td>
       <c:forEach var="i" begin="1" end="4">
        
            <td bgcolor="red">Item <c:out value="${i}"/></td>
           
        
      </c:forEach>
     </tr>
      <tr>
       <td>Eac account resolves</td>
       <c:forEach var="i" begin="1" end="4">
        
            <td bgcolor="red">Item <c:out value="${i}"/></td>
           
        
      </c:forEach>
     </tr>
      <tr>
       <td>Eac skin resolves</td>
       <c:forEach var="i" begin="1" end="4">
        
            <td bgcolor="red">Item <c:out value="${i}"/></td>
           
        
      </c:forEach>
     </tr>
     --%>
</table>
</body>
</html>