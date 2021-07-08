<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="com.oup.eac.common.utils.EACSettings"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>EAC Test Site - <%=EACSettings.getProperty("deploy.host")%></title>
		
		<link rel="stylesheet" href="<c:url value="/styles/blueprint/screen.css" />" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="<c:url value="/styles/blueprint/print.css" />" type="text/css" media="print" />
		<!--[if lt IE 8]>
		        <link rel="stylesheet" href="<c:url value="/resources/blueprint/ie.css" />" type="text/css" media="screen, projection" />
		<![endif]-->
		<link rel="stylesheet" href="<c:url value="/styles/eacuat.css" />" type="text/css" media="screen, projection" />
        <script type="text/javascript" src="<c:url value="/js/eac.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/cookies.js" />"></script>
	</head>
	<body onload="init('<%=EACSettings.getProperty("cookie.host")%>')" style="background:<%=EACSettings.getProperty("deploy.bgcolor")%>">
		<div id="page" class="container">
			<div id="header" class="span-24" style="padding-top:30px;">
				<div class="span-16">
					<h3>EAC Test Site - <b><%=EACSettings.getProperty("deploy.host")%></b></h3>
				</div>
				<div class="span-8 last">
					<div id="loginLogout" style="display:none">			
					    <h3>
					    	<a href="javascript:logout('<%=EACSettings.getProperty("deploy.protocol")%>','<%=EACSettings.getProperty("deploy.host")%>','<%=EACSettings.getProperty("deploy.eac.host")%>','<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>')">Logout</a>
					    	&nbsp;&nbsp;
							<a href="<c:url value="/index.jsp" />">Home</a>
						</h3>
					</div>						
				</div>	
			</div>	
			<div id="left" class="span-4">
				&#160;
			</div>
			<div id="main" class="span-17">
				<fieldset>
					<legend>Release 1</legend>
					<div class="span-3 border">
						<a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">
							<img src="images/success-addmath.jpg" width="79" height="106" alt="Additional Maths" />
						</a>
					</div>
					<div class="span-10 append-3 last">
						<h3><a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">Student Content <br/>(Covers all products in Release 1)</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Product Registration Page: YES</li>
							<li>Activation Type: SELF</li>
							<li>Linked Products: None</li>
						</ul>
					</div>					
				</fieldset>
				<fieldset>
					<legend>Release 2</legend>
					<div class="span-3 border">
						<a href="protected/release2/oxfordfajar/spm-success-science.jsp" class="tt">
							<img src="images/success-science.jpg" width="79" height="106" alt="Science" />
						</a>
					</div>
					<div class="span-8">
						<h3><a href="protected/release2/oxfordfajar/spm-success-science.jsp" class="tt">Teacher Content</a></h3>
						<br/>
						<h3>Features</h3>
						<ul>
							<li>Product Registration Page: YES</li>
							<li>Activation Type: VALIDATED</li>
							<li>Linked Products: Student Content (pre-validation)</li>
						</ul>
					</div>
					<div class="span-5 last">
						<fieldset>
							<legend>Linked Products</legend>
							<a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">
								<img src="images/success-addmath.jpg" width="79" height="106" alt="Additional Maths" />
							</a>
						</fieldset>
					</div>
					<div class="span-3 clear border">
						<a href="protected/release2/oxfordfajar/spm-success-bio.jsp" class="tt">
							<img src="images/success-bio.jpg" width="79" height="106" alt="sample1" />
						</a>
					</div>
					<div class="span-8">
						<h3><a href="protected/release2/oxfordfajar/spm-success-bio.jsp" class="tt">HE Student Content 1<br/>(Will be Principle of Management)</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Product Registration Page: YES</li>
							<li>Activation Type: SELF</li>
							<li>Redeem Codes: <a href="/eacAdmin/token/create">Generate</a></li>
							<li>Linked Products: Student Content (pre-validation)</li>
						</ul>
					</div>
					<div class="span-5 last">
						<fieldset>
							<legend>Linked Products</legend>
							<a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">
								<img src="images/success-addmath.jpg" width="79" height="106" alt="Additional Maths" />
							</a>
						</fieldset>
					</div>
					<div class="span-3 border">
						<a href="protected/release2/oxfordfajar/spm-success-english.jsp" class="tt">
							<img src="images/success-english.jpg" width="79" height="106" alt="English" />
						</a>
					</div>
					<div class="span-8">
						<h3><a href="protected/release2/oxfordfajar/spm-success-english.jsp" class="tt">HE Student Content 2<br/>(Will be Business Statistics)</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Product Registration Page: YES</li>
							<li>Activation Type: SELF</li>
							<li>Redeem Codes: <a href="/eacAdmin/token/create">Generate</a></li>
							<li>Linked Products: Student Content (pre-validation)</li>
						</ul>
					</div>
					<div class="span-5 last">
						<fieldset>
							<legend>Linked Products</legend>
							<a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">
								<img src="images/success-addmath.jpg" width="79" height="106" alt="Additional Maths" />
							</a>
						</fieldset>
					</div>
					<div class="span-3 clear border">
						<a href="protected/release2/oxfordfajar/spm-success-math.jsp" class="tt">
							<img src="images/success-math.jpg" width="79" height="106" alt="sample1" />
						</a>
					</div>
					<div class="span-8">
						<h3><a href="protected/release2/oxfordfajar/spm-success-math.jsp" class="tt">HE Lecturer Content 1</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Product Registration Page: YES</li>
							<li>Activation Type: VALIDATED</li>
							<li>Linked Products: Student Content (pre-validation), HE Student Content 1 (post-validation)</li>
						</ul>
					</div>
					<div class="span-5 last">
						<fieldset>
							<legend>Linked Products</legend>
							<a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">
								<img src="images/success-addmath.jpg" width="79" height="106" alt="Additional Maths" />
							</a>
							<a href="protected/release2/oxfordfajar/spm-success-bio.jsp" class="tt">
								<img src="images/success-bio.jpg" width="79" height="106" alt="Biology (HE Student Content 1)" />
							</a>
						</fieldset>
					</div>
					<div class="span-3 clear border">
						<a href="protected/release2/oxfordfajar/spm-success-physics.jsp" class="tt">
							<img src="images/success-physics.jpg" width="79" height="106" alt="sample1" />
						</a>
					</div>
					<div class="span-8">
						<h3><a href="protected/release2/oxfordfajar/spm-success-physics.jsp" class="tt">HE Lecturer Content 2</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Product Registration Page: YES</li>
							<li>Activation Type: VALIDATED</li>
							<li>Linked Products: Student Content (pre-validation), HE Student Content 2 (post-validation)</li>
						</ul>
					</div>
					<div class="span-5 last">
						<fieldset>
							<legend>Linked Products</legend>
							<a href="protected/release1/oxfordfajar/spm-success-addmath.jsp" class="tt">
								<img src="images/success-addmath.jpg" width="79" height="106" alt="Additional Maths" />
							</a>
							<a href="protected/release2/oxfordfajar/spm-success-english.jsp" class="tt">
								<img src="images/success-english.jpg" width="79" height="106" alt="Biology (HE Student Content 2)" />
							</a>
						</fieldset>
					</div>
					<div class="span-3 border">
						<a href="<%=EACSettings.getProperty("deploy.protocol")%>://<%=EACSettings.getProperty("deploy.eac.host")%>/eac/activationCode.htm?url=http%3A%2F%2F<%=EACSettings.getProperty("deploy.host")%>%2F<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>unprotected%2Frelease2%2Foxfordfajar%2Fspm-success-bio.jsp" class="tt">
							<img src="images/success-bio.jpg" width="79" height="106" alt="Biology" />
						</a>
					</div>
					<div class="span-10 append-3 last">
						<h3><a href="<%=EACSettings.getProperty("deploy.protocol")%>://<%=EACSettings.getProperty("deploy.eac.host")%>/eac/activationCode.htm?url=http%3A%2F%2F<%=EACSettings.getProperty("deploy.host")%>%2F<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>unprotected%2Frelease2%2Foxfordfajar%2Fspm-success-bio.jsp" class="tt">HE Student Content 1 <br/>(Direct Activation Code - No Product Validation)</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Direct link to activation code page</li>
							<li>Passes url parameter to enable skinning</li>
							<li>Goes to product landing page</li>
							<li>Redeem Codes: <a href="/eacAdmin/token/create">Generate</a></li>
							<li>Linked Products: None</li>
						</ul>
					</div>					
					<div class="span-3 border">
						<a href="<%=EACSettings.getProperty("deploy.protocol")%>://<%=EACSettings.getProperty("deploy.eac.host")%>/eac/activationCode.htm?url=http%3A%2F%2F<%=EACSettings.getProperty("deploy.host")%>%2F<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>protected%2Frelease1%2Foxfordfajar%2Fspm-sucjsp-bio.jsp" class="tt">
							<img src="images/success-bio.jpg" width="79" height="106" alt="Biology" />
						</a>
					</div>
					<div class="span-10 append-3 last">
						<h3><a href="<%=EACSettings.getProperty("deploy.protocol")%>://<%=EACSettings.getProperty("deploy.eac.host")%>/eac/activationCode.htm?url=http%3A%2F%2F<%=EACSettings.getProperty("deploy.host")%>%2F<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>protected%2Frelease2%2Foxfordfajar%2Fspm-success-bio.jsp" class="tt">HE Student Content 1 <br/>(Direct Activation Code with Product Validation)</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Direct link to activation code page</li>
							<li>Passes url parameter of protected resource</li>
							<li>Shows customised page including "Product Home" link</li>
							<li>Redeem Codes: <a href="/eacAdmin/token/create">Generate</a></li>
							<li>Validates redeem code is correct for protected resource</li>
							<li>Goes to product landing page</li>
							<li>Linked Products: None</li>
						</ul>
					</div>
					<div class="span-3 border">
						<a href="<%=EACSettings.getProperty("deploy.protocol")%>://<%=EACSettings.getProperty("deploy.eac.host")%>/eac/activationCode.htm?url=http%3A%2F%2F<%=EACSettings.getProperty("deploy.host")%>%2F<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>protected%2Frelease1%2Foxfordfajar%2Fspm-success-english.jsp" class="tt">
							<img src="images/success-english.jpg" width="79" height="106" alt="English" />
						</a>
					</div>
					<div class="span-10 append-3 last">
						<h3><a href="<%=EACSettings.getProperty("deploy.protocol")%>://<%=EACSettings.getProperty("deploy.eac.host")%>/eac/activationCode.htm?url=http%3A%2F%2F<%=EACSettings.getProperty("deploy.host")%>%2F<%=EACSettings.getProperty("deploy.sampleSite.context.path")%>protected%2Frelease2%2Foxfordfajar%2Fspm-success-english.jsp" class="tt">HE Student Content 2 <br/>(Direct Activation Code with Product Validation)</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Direct link to activation code page</li>
							<li>Passes url parameter of protected resource</li>
							<li>Shows customised page including "Product Home" link</li>
							<li>Redeem Codes: <a href="/eacAdmin/token/create">Generate</a></li>
							<li>Validates redeem code is correct for protected resource</li>
							<li>Goes to product landing page</li>
							<li>Linked Products: None</li>
						</ul>
					</div>
				</fieldset>

                <fieldset>
                    <legend>OFCW Extras</legend>
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/admin-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="OFCW Admin Product" />
                        </a>
                    </div>    
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-malaysian-perspective-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Business Management: A Malaysian Perspective (2nd Edition)" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/lec-malaysian-perspective-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="Lecturer Business Management: A Malaysian Perspective (2nd Edition)" />
                        </a>
                    </div> 
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-finance-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Fundamentals of Finance with Microsoft Excel" />
                        </a>
                    </div>      
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/lec-finance-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="Lecturer Fundamentals of Finance with Microsoft Excel" />
                        </a>
                    </div>  
                </fieldset>
				
                <fieldset>
                    <legend>OFCW Extras 2</legend>
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-tamadun-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Tamadun Islam dan Tamadun Asia (TITAS) Edisi" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/lec-tamadun-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="Lecturer Tamadun Islam dan Tamadun Asia (TITAS) Edisi" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-biology-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Biology for Matriculation Semester 1 Fourth Edition" />
                        </a>
                    </div> 
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-chemistry-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Chemistry for Matriculation Semester 1 Fourth Edition" />
                        </a>
                    </div>      
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-finance-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Fundamentals of Finance with Microsoft Excel" />
                        </a>
                    </div>  
                </fieldset>
                
                <fieldset>
                    <legend>OFCW Extras 3</legend>
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-matriculation-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Mathematics for Matriculation Semester 1 Fourth Edition" />
                        </a>
                    </div>      
                    <div class="span-3 border">
                        <a href="protected/release3/oxfordfajar/he-physics-product.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Physics for Matriculation Semester 1 Fourth Edition" />
                        </a>
                    </div>  
                </fieldset>
				
                <fieldset>
                    <legend>OFCW HE</legend>
                    <div class="span-3 border">
                        <a href="protected/release5/oxfordfajar/he-essentials-of-managerial-economics-la.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Essentials of Managerial Economics (Lecturer Account)" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release5/oxfordfajar/he-essentials-of-managerial-economics.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Essentials of Managerial Economics" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release5/oxfordfajar/he-business-research-methods-la.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Business Research Methods (Lecturer Account)" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release5/oxfordfajar/he-business-research-methods.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Business Research Methods" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release5/oxfordfajar/he-principles-of-economics2e-la.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Principles of Economics 2e (Lecturer Account)" />
                        </a>
                    </div>
                </fieldset>
                
				<fieldset>
					<legend>DLP</legend>
					<div class="span-3 border">
						<a href="protected/release3/elt/elementary-sb.jsp" class="tt">
							<img src="" width="79" height="106" alt="Elementary SB" />
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/pre-intermediate-sb.jsp" class="tt">
							<img src="" width="79" height="106" alt="Pre Intermediate SB" />
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/intermediate-sb.jsp" class="tt">
							<img src="" width="79" height="106" alt="Intermediate SB" />
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/upper-intermediate-sb.jsp" class="tt">
							<img src="" width="79" height="106" alt="Upper Intermediate SB" />
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/advanced-sb.jsp" class="tt">
							<img src="" width="79" height="106" alt="Advanced SB" />
						</a>
					</div>								
				</fieldset>
				
				<fieldset>
					<legend>DLP 2</legend>
					<div class="span-3 border">
						<a href="protected/release3/elt/network-1.jsp" class="tt">
							<img src="" width="79" height="106" alt="Network 1 Online Practice (PK/C)" /> 
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/network-2.jsp" class="tt">
							<img src="" width="79" height="106" alt="Network 2 Online Practice (PK/C)" /> 
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/network-starter.jsp" class="tt">
							<img src="" width="79" height="106" alt="Network Starter Online Practice (PK/C)" />
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/speak-now.jsp" class="tt">
							<img src="" width="79" height="106" alt="Speak Now 1 Online Practice (PK/C)" />
						</a>
					</div>									
				</fieldset>
				
				<fieldset>
					<legend>DLP 3</legend>
					<div class="span-3 border">
						<a href="protected/release3/elt/headway-beginner.jsp" class="tt">
							<img src="" width="79" height="106" alt="New Headway Plus SE Beginner online practice (PK/C)" /> 
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/headway-elem.jsp" class="tt">
							<img src="" width="79" height="106" alt="New Headway Plus SE Elem online practice (PK/C)" /> 
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/headway-preint.jsp" class="tt">
							<img src="" width="79" height="106" alt="New Headway Plus SE pre-Int online practice (PK/C)" />
						</a>
					</div>	
					<div class="span-3 border">
						<a href="protected/release3/elt/headway-int.jsp" class="tt">
							<img src="" width="79" height="106" alt="New Headway Plus SE Int online practice (PK/C)" />
						</a>
					</div>									
				</fieldset>
				
				<fieldset>
					<legend>Teachers Club</legend>
					<div class="span-3 border">
						<a href="protected/release3/elt/teachers-club.jsp" class="tt">
							<img src="" width="79" height="106" alt="Teachers Club" /> 
						</a>
					</div>									
				</fieldset>

                <fieldset>
                    <legend>Site Specific Redirection</legend>
                    <div class="span-3 border">
                        <a href="protected/testredirect/anything.html" class="tt">
                            <img src="" width="79" height="106" alt="Site specific redirection" />
                        </a>
                    </div>              
                </fieldset>
                
				<fieldset>
					<legend>Admin Product</legend>
					<div class="span-3 border">
						<a href="protected/release6/admin-product.jsp" class="tt">
							<img src="" width="79" height="106" alt="Admin Prod" /> 
						</a>
					</div>									
				</fieldset>

                <fieldset>
                    <legend>Login Widget</legend>
                    <div class="span-3 border">
                        <a href="loginwidget/setup.jsp" class="tt">
                            <img src="" width="79" height="106" alt="Login Widget Setup Page" /> 
                        </a>
                    </div>                                  
                    <div class="span-3 border">
                        <a href="unprotected/release2/teachersclub/loginWidgetTestStart.jsp" class="tt">
                            <img src="" width="79" height="106" alt="Login Widget Test Page" /> 
                        </a>
                    </div>                                  
                </fieldset>
                
                <fieldset>
                    <legend>OFCW Release 7 - a</legend>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/financial-mgmt.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Financial Management" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/financial-mgmt-le.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Financial Management (Lecturer Account)" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/chemistry-for-matriculation.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Chemistry for Matriculation" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/biology-for-matriculation.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Biology for Matriculation" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/maths-for-matriculation.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Maths for Matriculation" />
                        </a>
                    </div>
                </fieldset>
                <fieldset>
                    <legend>OFCW Release 7 - b</legend>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/physics-for-matriculation.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Physics for Matriculation" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/cost-accounting.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Cost Accounting" />
                        </a>
                    </div>
                    <div class="span-3 border">
                        <a href="protected/release7/oxfordfajar/cost-accounting-le.jsp" class="tt">
                            <img src="" width="79" height="106" alt="HE Cost Accounting (Lecturer Account)" />
                        </a>
                    </div>
                </fieldset>
                <fieldset>
                    <legend>ORCS</legend>
					<div class="span-4 clear border" style="height: 40em;">
                        <img src="images/orc_logo.jpg" width="142" height="55" alt="ORCS Logo"/>
					</div>
					<div class="span-8" style="margin-bottom: 2em">
						<h3><a href="protected/release8/orcs/orcs-test-student.jsp" class="tt">ORCS Test Student</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Admin Registerable</li>
							<li>Activation Type: INSTANT</li>
							<li>Product Registration Page: NO</li>
						</ul>
					</div>
					<div class="span-8" style="margin-bottom: 2em">
						<h3><a href="protected/release8/orcs/orcs-test-lecturer1.jsp" class="tt">ORCS Test Lecturer 1</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Self Registerable</li>
							<li>Activation Type: VALIDATED</li>
							<li>Product Registration Page: YES</li>
							<li>Linked Products: NONE</li>
						</ul>
					</div>
					<div class="span-8">
						<h3><a href="protected/release8/orcs/orcs-test-lecturer2.jsp" class="tt">ORCS Test Lecturer 2</a></h3>

						<h3>Features</h3>
						<ul>
							<li>Self Registerable</li>
							<li>Activation Type: VALIDATED</li>
							<li>Product Registration Page: YES</li>
							<li>Linked Products: ORCS Test Student (post validation)</li>
						</ul>
					</div>
                </fieldset>
                <fieldset>
                    <legend>ORCS 2</legend>
					<div class="span-4 clear border" style="height: 40em;">
                        <img src="images/orc_logo.jpg" width="142" height="55" alt="ORCS Logo"/>
					</div>
					<div class="span-8" style="margin-bottom: 2em">
						<h3><a href="uk/orc/hidden.jsp" class="tt">ORCS Test Staff</a></h3>
						<h3>Features</h3>
						<ul>
							<li>Admin Registerable</li>
							<li>Activation Type: INSTANT</li>
							<li>Product Registration Page: NO</li>
						</ul>
					</div>
					<div class="span-8" style="margin-bottom: 2em">
						<h3><a href="uk/orc/law/intl/abass/lecturer/orcs-lecturer-1.jsp" class="tt">ORCS Test Lecturer 1</a></h3>
						<h3>Features</h3>
						<ul>
							<li>Self Registerable</li>
							<li>Activation Type: VALIDATED</li>
							<li>Product Registration Page: YES</li>
							<li>Linked Products: NONE</li>
						</ul>
					</div>
					<div class="span-8">
						<h3><a href="uk/orc/nursing/bullock/lecturer/orcs-lecturer-2.jsp" class="tt">ORCS Test Lecturer 2</a></h3>
						<h3>Features</h3>
						<ul>
							<li>Self Registerable</li>
							<li>Activation Type: VALIDATED</li>
							<li>Product Registration Page: YES</li>
							<li>Linked Products: ORCS Test Student (post validation)</li>
						</ul>
					</div>
					<div class="span-8">
						<h3><a href="uk/orc/nursing/bullock/studentsecure/01student/orcs-student-1.jsp" class="tt">ORCS Test Student linked product 2</a></h3>
					</div>
                </fieldset>
                <fieldset>
                    <legend>Oxford Owl</legend>
                    <div class="span-4 clear border">
                        <img src="images/oxowl_logo.png" width="142" height="55" alt="Oxford Owl Logo"/>
                    </div>
                    <div class="span-8" style="margin-bottom: 2em">
                        <h3><a href="protected/release11/oxed/oxed-test-owl.jsp" class="tt">OXED Oxford Owl Test</a></h3>
                    </div>
                </fieldset>
			</div>			
		 	<div id="footer" class="span-24">
		 		&#160;
	        </div>
		</div>
	</body>
</html>