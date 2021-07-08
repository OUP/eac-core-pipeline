<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.notFound" text="?title.notFound?" /></title>
</head>
<body>
	<div class="row">
		<div class="col small-12">
			<main class="box">
				<h1><spring:message code="label.notFound" text="?label.notFound?" /></h1>
				<div class="box_inner">
					<p class="message message--error"><spring:message code="label.pageNotFound" text="?label.pageNotFound?" /></p>
				</div>
			</main>
		</div>
	</div>    
</body>
</html>