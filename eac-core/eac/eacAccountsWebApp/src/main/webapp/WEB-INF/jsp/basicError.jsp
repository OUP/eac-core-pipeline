<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js" lang="en">
<head>
<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="description" content="">
	
	<!--[if (gt IE 8)]><!-->
		<link rel="stylesheet" href="css/style.min.css">
		<link rel="stylesheet" href="css/footer-style.css" type="text/css" />
	<title>Unexpected error</title>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/header.jsp" %>
	<div class="row">
		<div class="col small-12">
			<main class="box">
				<h1>Unexpected error</h1>
				<div class="box_inner">
					<p class="message message--error">An unexpected error has occurred. Please go back and try again.<br/>If this continues to happen please contact the system administrator.</p>
				</div>
			</main>
		</div>
	</div>
	<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>