<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<c:set var="titleKeyInfo" value=""/>
<c:choose>
<c:when test="${updatingProductRegistration}">
    <c:choose>
        <c:when test="${not registrationDto.readOnly}">
            <c:set var="titleKey" value="title.productregistration.update.sign.up"/>
            <c:set var="submitKey" value="submit.productregistration.update"/>
        </c:when>
        <c:otherwise>
            <c:set var="titleKey"     value="title.productregistration.read.only.sign.up"/>            
            <c:set var="submitKey"    value=""/>
            <c:set var="titleKeyInfo" value="title.productregistration.read.only.info"/>
        </c:otherwise>
    </c:choose>
</c:when>
<c:otherwise>
    <c:set var="titleKey" value="title.productregistration.create.sign.up"/>
    <c:set var="submitKey" value="submit.productregistration"/>
</c:otherwise>
</c:choose>
	<spring:message code="${titleKey}" text="?${titleKey}?" var="titleKeyMessage" />
	<c:set var="titlePart1" value="${titleKeyMessage}" />
	<c:set var="titlePart2" value="${registrationDto.productDescription}" />
	<c:set var="title" value="${titlePart1} ${titlePart2}" />
	<head>
	  <title>${title}</title>
	  <meta name="WT.cg_s" content="Registration"/>
	  
	  <script type="text/javascript">
	  	/* <![CDATA[ */
	  	            $(function(){  
	  		 keepPageAlive({ hours: 2 }); 
	  	            });
	  	 /* ]]> */
	  </script>
	  
	</head>
	<body class="stickyFooter">
	<section class="content">
			<eac:progressbar page="PRODUCT_REGISTRATION"/>
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
						<tags:errorsAccountProductRegistration name="registrationDto"/>
						<h1>${titlePart1} ${titlePart2}</h1>
						<p>	<c:if test="${registrationDto.readOnly}">
                    			<spring:message code="${titleKeyInfo}" text="?${titleKeyInfo}?"/>
                			</c:if>
                		</p>
						<p><spring:message code="label.form.mandatory" text="Fields marked with * are mandatory" /></p>
						<div class="message message--info">
						<eac:loginurl var="loginUrl" propertyName="eac.login.url"/>
							<p><spring:message code="${registrationDto.preamble}" text="?${registrationDto.preamble}?"/></p>
						</div>
						<form:form method="post" commandName="registrationDto" cssClass="form" id="register-form">
							<tags:inSync/>
								<%-- <eac-common:eacform regDto="${registrationDto}"/> --%>
								<c:set var="regDto" value="${registrationDto}"></c:set>
								<%@include file="/WEB-INF/jsp/eacFormTag.jsp"%>
				                <c:if test="${not registrationDto.readOnly}">
									<div class="form_row">
										<button type="submit" class="button" id="submitButton"><spring:message code="${submitKey}" text="?${submitKey}?" /></button>
									</div>
								</c:if>
					</form:form>
					</div>
				</div>
			</div>
		</section>
		<script src="js/core.min.js"></script>
		<script>

		function removeText(arg){
						//console.log($(arg).attr("id"));
						var id=$(arg).attr("id");
						$('#'+id).removeClass("error");
						$($('#'+id).next()).hide();
						}

		

function removeCheckBoxText(arg){
//console.log($(arg).attr("id"));
var id=$(arg).attr("id");;
var classTemp=$($($('#'+id).parent()).next()).attr("class");
if(classTemp=='error')
$($($('#'+id).parent()).next()).hide();

}

function removeCheckBoxTextMulti(arg)
{

var classTemp=$($('.form_checkboxWrap_tmp.error').prev()).attr("class")
if(classTemp=='error')
$($('.form_checkboxWrap_tmp.error').prev()).hide()

}
		</script>
			<script>
							$(document).ready(function(){

								
									// $($('.error')[1]).siblings().addClass("error");
									/* var errorSpans = $('.error');
										
										$.each(errorSpans, function (index, value){
											console.log('$(' + \'value\') +'); */
											var errorSpans = $("span[class='error']");
											$($("span[class='error']")[0]).siblings().focus();
											for(var ctr =0 ; ctr < errorSpans.length ; ctr ++) 
												//$($('.error')[ctr]).siblings().addClass("error");
												$($("span[class='error']")[ctr]).siblings().addClass("error");
											
									
										
									});
		 				</script>
	</body>
</html>