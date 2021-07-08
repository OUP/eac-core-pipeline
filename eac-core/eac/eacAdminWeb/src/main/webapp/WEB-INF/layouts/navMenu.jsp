<%@page import="java.util.UUID"%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="navMenuTile">
	<div id="navmenu">
		<security:authorize ifAnyGranted="ROLE_USER">
		        <div class="menutitle ui-corner-top"><h1><spring:message code="navbar.section.search" /></h1></div>
		        <ul>
					<security:authorize ifAllGranted="ROLE_LIST_CUSTOMER">
						<li>
							<a href="<c:url value="/customer/search" />"><spring:message code="navbar.item.customers" /></a>
						</li>
					</security:authorize>
	           		<security:authorize ifAllGranted="ROLE_LIST_ACTIVATION_CODE">
                        <li>
                            <a href="<c:url value="/mvc/activationCode/search.htm" />"><spring:message code="navbar.item.activationCodes" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_LIST_ACTIVATION_CODE_BATCH">
		               	<li>
		                   <a href="<c:url value="/mvc/activationCodeBatch/search.htm" />"><spring:message code="navbar.item.activationCodeBatches" /></a>
		               </li>
	           		</security:authorize>
	           		
	           		<security:authorize ifAllGranted="ROLE_LIST_PRODUCT,ROLE_LIST_REGISTRATION_DEFINITION">
		               	<li>
		                   <a href="<c:url value="/mvc/product/search.htm" />"><spring:message code="navbar.item.products" /></a>
		               </li>
	           		</security:authorize>
	           		<security:authorize ifAllGranted="ROLE_LIST_PRODUCT_GROUP">
	           			<li>
		                   <a href="<c:url value="/mvc/eacGroups/search.htm" />"><spring:message code="navbar.item.groups" /></a>
		               </li>
		            </security:authorize>
	            </ul>                 
	            <br/>       
		        <div class="menutitle ui-corner-top"><h1><spring:message code="navbar.section.create" /></h1></div>
		        <ul>
					<security:authorize ifAllGranted="ROLE_CREATE_CUSTOMER">
						<li>
							<a href="<c:url value="/customer/create" />"><spring:message code="navbar.item.customer" /></a>
						</li>
					</security:authorize>
		            <security:authorize ifAllGranted="ROLE_CREATE_ACTIVATION_CODE_BATCH">
			        	<li>
			            	<a href="<c:url value="/mvc/batch/create.htm" />"><spring:message code="navbar.item.activationCodeBatch" /></a>
		                </li>
		            </security:authorize>
	           		
 					<security:authorize ifAllGranted="ROLE_CREATE_PRODUCT">
						<li>
							
 							<a href="<c:url value="/mvc/product/create.htm?id=" />"><spring:message code="navbar.item.product" /></a>
						</li>
 					</security:authorize>
 					<security:authorize ifAllGranted="ROLE_CREATE_PRODUCT_GROUP">
 						<li>
							<% pageContext.setAttribute("eacGroupId", null); %>
 							<a href="<c:url value="/mvc/eacGroups/create.htm?id=${eacGroupId}" />"><spring:message code="navbar.item.group" /></a>
						</li>
					</security:authorize>
		        </ul>
		        <br/>  		
                <div class="menutitle ui-corner-top"><h1><spring:message code="navbar.section.reports" /></h1></div>
                <ul>
                	<security:authorize ifAllGranted="ROLE_CREATE_REGISTRATION_REPORT">
	                    <li>
	                        <a href="<c:url value="/mvc/reports/registrations/report.htm" />"><spring:message code="navbar.item.registrations" /></a>
	                    </li>
                        <li>
                            <a href="<c:url value="/mvc/reports/registrations/activationCodeBatchReport.htm" />"><spring:message code="navbar.item.activationCodeBatch" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_USER_REPORT_MASTER">
                    	<li>
                            <a href="<c:url value="/mvc/reports/userDetails.htm" />"><spring:message code="navbar.item.userDetails" /></a>
                        </li>
                    </security:authorize>
                </ul>
		        <br/>  		
                <div class="menutitle ui-corner-top"><h1><spring:message code="navbar.section.admin" /></h1></div>
                <ul>
 					<security:authorize ifAllGranted="ROLE_MANAGE_ACCOUNT">
						<li>
							<a href="<c:url value="/mvc/account/manage.htm" />"><spring:message code="navbar.item.accounts" /></a>
						</li>
					</security:authorize>
                    <security:authorize ifAllGranted="ROLE_MANAGE_ROLES">
	                    <li>
	                        <a href="<c:url value="/mvc/roles/manage.htm" />"><spring:message code="navbar.item.roles" /></a>
	                    </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_MANAGE_SKIN">
                        <li>
                            <a href="<c:url value="/mvc/skin/manage.htm" />"><spring:message code="navbar.item.skins" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_MANAGE_EXTERNAL_SYSTEM">
                        <li>
                            <a href="<c:url value="/mvc/externalsystem/manage.htm" />"><spring:message code="navbar.item.externalSystems" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_MANAGE_LANGUAGE">
                        <li>
                            <a href="<c:url value="/mvc/language/manage.htm" />"><spring:message code="navbar.item.languages" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_MANAGE_ORG_UNITS">
                        <li>
                            <a href="<c:url value="/mvc/orgunits/manage.htm" />"><spring:message code="label.org.units.short" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_MANAGE_PAGE">
                        <li>
                            <a href="<c:url value="/mvc/page/manage.htm?key=link.managePages" />"><spring:message code="navbar.item.pages" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_UPLOAD_PRODUCTS">
                    	<li>
                            <a href="<c:url value="/mvc/uploadProducts/formUpload.htm" />"><spring:message code="navbar.item.upload.products" /></a>
                        </li>
                    </security:authorize>
                     <security:authorize ifAllGranted="ROLE_MERGE_CUSTOMER">
                    	<li>
                            <a href="<c:url value="/mvc/mergeCustomer/formMerge.htm" />"><spring:message code="navbar.item.merge.customers" /></a>
                        </li>
                    </security:authorize> 
                    <security:authorize ifAllGranted="ROLE_BULK_CREATE_ACTIVTION_CODE">
                    	<li>
                            <a href="<c:url value="/mvc/BulkCreateActivationCodes/bulkActivationcodeupload.htm" />"><spring:message code="navbar.item.upload.ActivationCodes" /></a>
                        </li>
                    </security:authorize>   
                    <security:authorize ifAllGranted="ROLE_WHITE_LIST_URLS">
                    	<li>
                            <a href="<c:url value="/mvc/WhiteListUrls/whiteListUrls.htm" />"><spring:message code="title.whiteListUrls" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_PLATFORM_MASTER">
                    	<li>
                            <a href="<c:url value="/mvc/PlatformMaster/platformMaster.htm" />"><spring:message code="navbar.item.platforms" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_TRUSTED_SYSTEM_MASTER">
                    	<li>
                            <a href="<c:url value="/mvc/trustedSystems/manage.htm" />"><spring:message code="navbar.item.trustedSystems" /></a>
                        </li>
                    </security:authorize>
                    <security:authorize ifAllGranted="ROLE_INTERNAL_SUPPORT">
                    	<li>
                            <a href="<c:url value="/mvc/support/cacheEviction.htm" />"><spring:message code="navbar.item.cacheEviction" /></a>
                        </li>
                        <li>
                            <a href="<c:url value="/mvc/support/insertToFailFeeder.htm" />"><spring:message code="navbar.item.cloudsearchFeeder" /></a>
                        </li>
                    </security:authorize>
                </ul>
	   </security:authorize>	   	   
   </div>
</div>
<script type="text/javascript">
	$(function() {
		$('#navmenu ul').each(function() {
			var $this = $(this);
			if ($this.find('li').length == 0) {
				$this.hide();
				$this.prev('div.menutitle').hide();
			}
		});
	});
</script>