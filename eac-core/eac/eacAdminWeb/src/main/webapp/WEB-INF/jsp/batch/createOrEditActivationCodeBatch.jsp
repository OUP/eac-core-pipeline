<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<div id="bodyTile">
	
	<script type="text/javascript">
	    $(document).ready(function() {
	    	$('#pagetabs').tabs({ cookie: { name: 'createOrEditActCodeBth', expires: 1 } });
	        
  	        //Hide buttons after form has been submitted (setting disabled does not work)
	        $('form').submit(function(){
	            $('button[type=submit]', this).attr('style', 'display:none');
	        });
	    });
	</script>
	
	
	<div>
	<div id="heading" class="ui-corner-top">
		<c:choose>
			<c:when test="${activationCodeBatchBean.editMode}">
				<spring:message code="title.editBatch" var="title"/>
			</c:when>
			<c:otherwise>
				<spring:message code="title.createBatch" var="title"/>
			</c:otherwise>
		</c:choose>
		<h1>${title}</h1>
	</div>
		<c:if test="${statusMessageKey ne null}">
			 <div class="success">
				<spring:message code="${statusMessageKey}"/>
				<c:if test="${adminEmail ne null}">
					<c:out value="${adminEmail}"/>
				</c:if>
			</div>
		</c:if>
		<c:forEach items="${status.errorMessages}" var="error">
			<span><c:out value="${error}"/></span><br>
		</c:forEach>
					
		<spring:hasBindErrors name="activationCodeBatchBean">
			<div class="error">
				<spring:bind path="activationCodeBatchBean.*">
					<c:forEach items="${status.errorMessages}" var="error">
						<span><c:out value="${error}"/></span><br>
					</c:forEach>
				</spring:bind>
			</div>
		</spring:hasBindErrors>
		<div>
			<form:form modelAttribute="activationCodeBatchBean" id="activationCodeBatchBean" method="POST">
		        <div id="pagetabs">
				    <ul>
				        <li class="ui-state-hover"><a href="#pagetab-1">Licence</a></li>
				        <li><a href="#pagetab-2">Batch</a></li>
				    </ul>
				    <div id="pagetab-1" class="ui-tabs-hide">
		                <tiles:insertAttribute name="productAndLicenceTile"/>
				    </div>
				    <div id="pagetab-2" class="ui-tabs-hide">
		                <tiles:insertAttribute name="activationCodeBatchTile"/>
				    </div>
		        </div>		    
        <div id="buttons">
        	<p>
        		<c:set var="eacLabel" value="${activationCodeBatchBean.activationCodeBatch.batchId}" />
	        	<c:choose>
	        		<c:when test="${activationCodeBatchBean.editMode}">
	        			<eac-common:isBatchUsed var="batchUsed" batchId="${activationCodeBatchBean.activationCodeBatch.batchId}" />
	        			<c:if test="${not batchUsed}" >
	        			<button type="submit" id="delete" eacLabel="${eacLabel}" value="${activationCodeBatchBean.activationCodeBatch.batchId}"><spring:message code="button.delete" /></button>
	        			</c:if>
	        			<button type="submit" id="edit"   eacLabel="${eacLabel}" value="edit"><spring:message code="button.edit" /></button>
	        			<button type="button" id="cancel" eacLabel="${eacLabel}" value="cancel"><spring:message code="button.cancel" /></button>
	        		</c:when>
	        		<c:otherwise>
		        		<button type="submit" id="save"><spring:message code="button.save.batch" /></button>
	        		</c:otherwise>
	        	</c:choose>
            </p>
        </div>
	        </form:form>
		</div>
	</div>
	<spring:message code="confirm.title.delete.batch" text="" var="message" />
	<spring:message code="confirm.title.create.batch" text="" var="createBatchMessage" />
	<spring:message code="confirm.title.update.batch" text="" var="updateBatchMessage" />
	<script type="text/javascript">
		$(function() {			
			$('#cancel').click(function() {
				window.location.replace('<c:url value="/mvc/activationCodeBatch/search.htm"/>');
			});
			var deleteUrl = '<c:url value="/mvc/batch/delete.htm?id=${activationCodeBatchBean.activationCodeBatch.batchId}"/>';
			$('#bodyTile').undelegate('#delete', 'click');<%--was getting 2 button clicks--%>
			$('#bodyTile').delegate  ('#delete', 'click', eacConfirm({ title:'${message}', url:deleteUrl}));

			var clickHandler = function(action){
				var fn1 = function(e) {
					e.preventDefault();
					var overlapProblem = false;
					$.ajax({
	    		    	url:      '<c:url value="/mvc/batch/checkOverlap.htm"/>',
	    		    	async:false, 
	    		    	type:     'POST',	
	    		    	dataType: 'json',
	    		    	data:     $('#activationCodeBatchBean').serialize()						
					}).done(function(data) {
						overlapProblem = data.overlapProblem;
						var message1 = '<spring:message code="button.confirmSave" />';
						var message2 = '';
						if(overlapProblem){
							message2 = '<spring:message code="label.noOverlap" />';
						}
						var callbackYes = function(e) {
							$button = $('#'+e.target.id);					
							$form = $button.closest('form');							
							$form.submit();
						};
						var $contents = $('<div class="confirmDialog">\
<div class="confirmDialogLine"><span class="confirmDialogLabel"></span></div>\
<div class="confirmDialogLine">'+message2+'</div>\
<div class="confirmDialogLine">'+message1+'</div>\
</div>');
						var handler = eacConfirm({title:action,contents:$contents, callbackYes:callbackYes});
						handler(e);
					});
					return false;
				};
				return fn1;
			};
			$('#bodyTile').undelegate('#save', 'click');<%--was getting 2 button clicks--%>
			$('#bodyTile').delegate  ('#save', 'click', clickHandler('${createBatchMessage}'));
			$('#bodyTile').undelegate('#edit', 'click');<%--was getting 2 button clicks--%>
			$('#bodyTile').delegate  ('#edit', 'click', clickHandler('${updateBatchMessage}'));
		});
	</script>
</div>
