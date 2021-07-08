<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.validateemail" text="?title.validateemail?" /></title>
  <meta name="WT.cg_s" content="Validate Email"/>
</head>
<body class="stickyFooter">
<section class="content">
<eac:progressbar page="ACCOUNT_VALIDATION"/>
			<div class="row">
				<div class="col large-8 large-centered">
					<div class="box">
    
    <div class="message message--info">
	<p><spring:message code="label.emailvalidation1" text="?label.emailvalidation1?"/></p>
	<p><spring:message code="label.emailvalidation2" text="?label.emailvalidation2?"/> <a id="click-here-link" href="#"><spring:message code="label.emailvalidation3" text="?label.emailvalidation3?"/></a> <spring:message code="label.emailvalidation4" text="?label.emailvalidation4?"/> <tags:mailTo email="${email}"/> <spring:message code="label.emailvalidation5" text="?label.emailvalidation5?"/></p>
	<p id="resending-email"><img src="images/progress-indicator.gif" style="vertical-align:middle; margin-right:5px"/><spring:message code="label.resendingEmail" text="?label.resendingEmail?" /></p>							
</div>
</div></div></div></section>
    <script type="text/javascript">
        var urlParams = {};
        (function () {
            var match,
                pl     = /\+/g,  // Regex for replacing addition symbol with a space
                search = /([^&=]+)=?([^&]*)/g,
                decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
                query  = window.location.search.substring(1);
    
            while (match = search.exec(query))
               urlParams[decode(match[1])] = decode(match[2]);
        })();

        $(function() {
            $('#resending-email').hide();
            $('#click-here-link').click(function() {
                $('#resending-email').show();
                $.ajax({
                    url: 'resendValidationEmail.htm',
                    data: 'url=' + urlParams['url'] + "&ajax=true",
                    complete: function() {
                        setTimeout(function() {
                            $('#resending-email').slideUp();
                        }, 3000);
                    }
                });
            });
        });
    </script>
    <script src="js/core.min.js"></script>
</body>
</html>