<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<c:url value="/mvc/language/manage.htm" var="formUrl"/>
<c:url value="/images/pencil.png" var="pencilUrl"/>
<c:url value="/images/delete.png" var="deleteUrl"/>
<c:url value="/images/add.png" var="addIconUrl"/>
<spring:message code="label.edit" var="edit"/>	
<spring:message code="label.remove" var="remove"/>
<form:form modelAttribute="languageBean" action="${formUrl}">
	<c:if test="${not empty languageBean.selectedLocale or languageBean.newLocale}">
		<fieldset>
			<legend><spring:message code="label.locale"/></legend>
			<label style="margin-right: 10px"><spring:message code="label.language"/></label><form:input id="language" path="language" maxlength="2" cssClass="locale"/>
			<label style="margin-right: 10px"><spring:message code="label.country"/></label><form:input id="country" path="country" maxlength="2" cssClass="locale"/>
			<label style="margin-right: 10px"><spring:message code="label.variant"/></label><form:input id="variant" path="variant" cssClass="locale"/>
		</fieldset>
	</c:if>
	<fieldset>
		<legend><spring:message code="label.messages"/></legend>
			<table class="summary" style="margin-bottom: 0px">
				<thead>
					<tr>
						<th style="width: 250px"><spring:message code="label.key" /></th>
						<th style="width: 355px"><spring:message code="label.message" /></th>							
						<th><spring:message code="label.actions" /></th>						
					</tr>
				</thead>
			</table>		
		<div id="messagesPanel" style="max-height: 450px; overflow-x: hidden; overflow-y: auto; margin-bottom: 15px">
			<table class="summary" style="margin-bottom: 0px">
				<tbody>
					<c:choose>
						<c:when test="${not empty languageBean.messages or languageBean.newMessageCount gt 0}">
							<c:forEach var="newMessage" items="${languageBean.newMessages}" varStatus="status">
								<c:if test="${not empty newMessage.basename}">
									<tr>
										<td style="width: 254px;"><form:input path="newMessages[${status.index}].key" cssClass="key" readonly="true"/></td><td style="width: 359px"><form:input path="newMessages[${status.index}].message" cssClass="message" readonly="true"/><input type="hidden" name="newMessages[${status.index}].basename" value="messages"/></td><td><a class="updateMessageLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>&#160;<a class="removeMessageLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a></td>
									</tr>
								</c:if>
							</c:forEach>
							<c:forEach var="message" items="${languageBean.messages}" varStatus="status">
								<tr id="${message.id}">
									<td style="width: 254px"><form:input path="messages[${status.index}].key" cssClass="key" readonly="true"/></td><td style="width: 359px"><form:input path="messages[${status.index}].message" cssClass="message" readonly="true"/></td><td><a class="updateMessageLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>&#160;<a class="removeMessageLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr><td id="noMessages" colspan="3" style="text-align:center"><spring:message code="label.noMessages" /></td></tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<a href="#bottom" id="bottom"></a>
		</div>
		<div>
			<img src="${addIconUrl}" style="vertical-align: middle; margin-right: 8px; margin-bottom: 4px;"/><a href="#" id="addMessageLink"><spring:message code="link.addMessage" /></a>
		</div>		
	</fieldset>
    <div id="buttons">
    	<p>                 
    		<button type="button" eacLabel="${languageBean.selectedLocale}" id="delete"><spring:message code="button.delete"/></button>
    		<button type="submit" id="save"><spring:message code="button.save" /></button>
    		<button type="button" id="cancel"><spring:message code="button.cancel"/></button>
        </p>
    </div>		
    <c:choose>
    	<c:when test="${languageBean.newLocale}">
		    <input type="hidden" name="locale" value="new"/>
    	</c:when>
    	<c:otherwise>
		    <input type="hidden" name="locale" value="${languageBean.selectedLocale}"/>
    	</c:otherwise>
    </c:choose>
</form:form>
<div id="messagePopup">
	<div class="field">
		<div class="fieldLabelNarrow">
			<label><spring:message code="label.key" />&#160;<span class="mandatory">*</span></label>
		</div>
		<div class="fieldValue">
			<input id="messageKey" type="text" value="" class="wide"/>
		</div>	
	</div>	
	<div class="field">
		<div class="fieldLabelNarrow">
			<label><spring:message code="label.message" /></span></label>
		</div>
		<div class="fieldValue">
			<textarea id="messageData" style="width: 410px; width: 413px\9; height: 150px; padding: 0" maxlength="1000"></textarea>
		</div>	
	</div>	
</div>
<spring:message code="confirm.title.delete.language" var="confirmDelete" text="" />
<script type="text/javascript">
	$(function() {
		var $messagePopup = $('#messagePopup');
		var $messagePopupKey = $messagePopup.find('input');
		var $messagePopupMessage = $messagePopup.find('textarea');

		// Have to remove existing popups from the body first
		// or each Ajax request will append a new popup
		$('body #messagePopup').remove();
		$('body div.ui-dialog').remove();
		
		$messagePopup.dialog({ 
			  autoOpen: false, 
		      closeOnEscape: true, 
		      open: function(event, ui) {
					$(".ui-dialog-titlebar-close").hide(); 
					$('.ui-widget-overlay').css('width','100%');
		      },
		      dialogClass: 'dialog', 
		      modal: true,
		      height: 'auto',
		      width: 550,
		      resizable: false
		 });
		
		var messagesToRemove = '';
		
		$('form').delegate('a', 'click', function() {
			var $this = $(this);
			if ($this.hasClass('updateMessageLink')) {
				var $selectedKey = $this.parents('tr').find('.key');
				var $selectedMessage = $this.parents('tr').find('.message');
				$messagePopup.dialog({ 
					title: '<spring:message code="title.updateMessage"/>',
			        buttons: createButtons('<spring:message code="button.edit"/>', function() {
			        	msWordCharacterCheck($messagePopupMessage.val());
			        	$selectedKey.val($messagePopupKey.val());
			        	$selectedMessage.val($messagePopupMessage.val());
			        	$messagePopup.dialog('close'); 
			    	  })
				});			
				$messagePopupKey.val($selectedKey.val());
				$messagePopupMessage.val($selectedMessage.val());
				$messagePopup.dialog('open');
				return false;
			} else if ($this.hasClass('removeMessageLink')) {
				var $tbody = $('tbody');
				var $parentRow = $this.closest('tr');
				if ($parentRow.attr('id')) {
					messagesToRemove += $parentRow.attr('id') + ',';
				}
				$parentRow.remove();
				if ($tbody.find('tr').length == 0) {
					$tbody.append('<tr><td id="noMessages" style="text-align:center" colspan="3"><spring:message code="label.noMessages" /></td></tr>');
				}
			}
		});
		
		var newMessageCount = 0;
		
		$('#addMessageLink').click(function() {
			var $tbody = $('tbody');
			$messagePopup.dialog({ 
				title: '<spring:message code="title.addMessage"/>',
		        buttons: createButtons('<spring:message code="button.add"/>', function() {
		        	if ($messagePopupKey.val().length > 0) {
			        	if (!isDuplicate($messagePopupKey.val(), $tbody)) { 
			        		msWordCharacterCheck($messagePopupMessage.val());
				        	$('#noMessages').parent().remove();
				        	var newRow = $('<tr><td><input type="text" name="newMessages[' + newMessageCount + '].key" value="' + $messagePopupKey.val() + '" class="key" readonly></td><td><input type="text" name="newMessages[' + newMessageCount + '].message" value="' + $messagePopupMessage.val() + '" class="message" readonly><input type="hidden" name="newMessages[' + newMessageCount + '].basename" value="messages"/></td><td><a class="updateMessageLink actionLink" href="#"><img class="actionImg" src="${pencilUrl}" title="${edit}" alt="${edit}"/></a>&#160;<a class="removeMessageLink actionLink" href="#"><img class="actionImg" src="${deleteUrl}" title="${remove}" alt="${remove}"/></a></td></tr>');
				        	if ($tbody.find('tr').length > 0) {
				        		newRow.insertBefore('table > tbody > tr:first')
				        	} else {
				        		newRow.appendTo($tbody);
				        	}
				        	$('#messagesPanel').scrollTop(0);
				        	newMessageCount++;
			        		$messagePopup.dialog('close'); 
			        	} else {
			        		alert('<spring:message code="error.locale.message.duplicateKey" arguments="' + $messagePopupKey.val() + '" />')
			        	}
		        	}
		    	  })
			});	
			$messagePopupKey.val('');
			$messagePopupMessage.val('');
			$messagePopup.dialog('open');
			return false;
		});
		
		function isDuplicate(key, $tbody) {
			return $tbody.find('input.key[value="' + key + '"]').length > 0;
		}
		
		function msWordCharacterCheck(message) {
			var specialWordChars = [8211, 8220, 8221, 8216]; // Numeric character codes for long hyphen, curly quotes and curly apostrophe
			var displayAlert = false;
			for (var i = 0; i < specialWordChars.length; i++) {
				for (var j = 0; j < message.length; j++) {
					var numeric = parseInt(message.charCodeAt(j));
					if (numeric == specialWordChars[i]) {
						displayAlert = true;
						break;
					}
				}
				if (displayAlert) {
					break;
				}
			}
			if (displayAlert) {
				alert('<spring:message code="error.locale.message.containsWordChars" text="The message contains one or more Microsoft Word special characters (curley quotes, curly apostrophe or long hyphen). Make sure that the message renders correctly on its intended page."/>');
			}
		}
		
		function createButtons(actionName, clickAction) {
			return [{ 
		    	  text: actionName, 
		    	  id: 'saveMessage', 
		    	  click: clickAction
		      }, 
		      {	
		    	  text: '<spring:message code="button.cancel"/>',
		    	  click: function() { 
		    		  $(this).dialog('close'); 
		    	  } 
		      }];
		}
		
		$('form').submit(function() {
			$('<input type="hidden" name="messageIdsToRemove" value="' + messagesToRemove + '"/>').appendTo($(this));
		});
		
		var submitForm = function() {
			var $form = $('form');
			$form.append('<input type="hidden" name="delete" value="1" />');
			$form.submit();
		};			
		
		var clickHandler = eacConfirm({callbackYes:submitForm, title:'${confirmDelete}'});
		$('#delete').click(clickHandler);
		
		var hiddenLocaleVal = $('form input:hidden[name="locale"]').val();
		if (!hiddenLocaleVal || hiddenLocaleVal === 'new') {
			$('#delete').hide();
		}
		
		$('#cancel').click(function() {
			window.location.replace('<c:url value="/mvc/language/manage.htm?locale="/>' + $('#selectedLocale').val());
		});
		
		$('#language').keyup(function() {
			$(this).val($(this).val().toLowerCase());
		});
		
		$('#country').keyup(function() {
			$(this).val($(this).val().toUpperCase());
		});
	});
</script>