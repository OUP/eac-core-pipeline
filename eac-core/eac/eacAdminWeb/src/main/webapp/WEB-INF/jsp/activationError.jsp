<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
  <title><spring:message code="title.error" text="?title.error?" /></title>
  <meta name="WT.cg_s" content="Error"/>
  
  <style type="text/css">
.row,
.content {
  width: 100%;
  margin-left: auto;
  margin-right: auto;
  margin-top: 0;
  margin-bottom: 0;
  max-width: 61.25rem;
}
.col {
  padding-left: 0.625rem;
  padding-right: 0.625rem;
  width: 100%;
  float: left;
}
  .large-8 {
    width: 66.66667%;
  }
  .col.large-centered {
    margin-left: auto;
    margin-right: auto;
    float: none;
  }
.message {
  padding: 1.25rem;
  border-radius: 0.3125rem;
  border: 0.0625rem solid rgba(0, 0, 0, 0.1);
  margin-bottom: 1.25rem;
}

.message--error {
  background: #FFE5E5;
  color: #760707;
}

.ui-corner-top
{ -moz-border-radius-topleft: 4px/*{cornerRadius}*/; 
-webkit-border-top-left-radius: 4px/*{cornerRadius}*/; 
-khtml-border-top-left-radius: 4px/*{cornerRadius}*/; 
border-top-left-radius: 4px/*{cornerRadius}*/; 
}
#heading {
	padding: 7px 0 3px 7px;
	border-left: 1px solid #133E5F;
	border-bottom: 6px solid #133E5F;
	background: #133E5F;
	margin-bottom:30px;
	
}
</style>
</head>

<section class="content">
			<div id="heading" class="ui-corner-top">
			<h1></h1>
			</div>
			<div class="row">
				<div class="col large-8 large-centered">
					
					<h1 style="color:black" ><spring:message code="label.errors" text="?label.errors?" /></h1>
						<div class="message message--error">
							<p><spring:message code="label.activation.error" text="?label.activation.error?" />
								<br/>
								</p>
						</div>
					</div>
					</div>
				
			

		</section>
</html>