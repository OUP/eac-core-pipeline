<%@include file="/WEB-INF/jsp/taglibs.jsp"%>
<spring:message code="label.pleaseSelect" var="pleaseSelect"/>
<div id="managePageTile">
	<div id="heading" class="ui-corner-top">
		<h1><spring:message code="title.managePages"/></h1>
	</div>

	<c:if test="${not empty param['statusMessageKey']}">
		 <div class="success">
			<spring:message code="${param['statusMessageKey']}"/>
		</div>
	</c:if>
	
	<spring:hasBindErrors name="pageDefinitionBean">
		<div class="error">
			<spring:bind path="pageDefinitionBean.*">
				<c:forEach items="${status.errorMessages}" var="error">
					<span><c:out value="${error}"/></span><br>
				</c:forEach>
			</spring:bind>
		</div>
	</spring:hasBindErrors>	

	<tags:breadcrumb/>
	
	<fieldset style="padding-top: 0px\9;">
		<legend><spring:message code="label.pages"/></legend>
		<div class="field" style="position: relative">
			<div class="fieldLabel">
				<label><spring:message code="label.pageName"/></label>
			</div>
			<div class="fieldValue">
				<select id="selectedPageDefinition" name="selectedPageDefinition">
					<c:if test="${empty pageDefinitionBean.selectedPageDefinition}"><option value="pleaseSelect"><spring:message code="label.pleaseSelect"/></option></c:if>
					<c:forEach var="pageDefinition" items="${pageDefinitionBean.pageDefinitions}">
						<c:set var="selected" value=""/>
						<c:if test="${pageDefinition.id == pageDefinitionBean.selectedPageDefinition.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${pageDefinition.id}" ${selected}><c:out value="${pageDefinition.name}"/></option>
					</c:forEach>
					<option value="newAccountPage"<c:if test="${pageDefinitionBean.newAccountPageDefinition}"> selected</c:if>><spring:message code="label.newAccountPageDefinition" htmlEscape="true"/></option>
					<option value="newProductPage"<c:if test="${pageDefinitionBean.newProductPageDefinition}"> selected</c:if>><spring:message code="label.newProductPageDefinition" htmlEscape="true"/></option>
				</select>
			</div>
			<c:url value="/images/progress-indicator.gif" var="progressIconUrl"/>
			<c:url value="/images/magnifying_glass.png" var="previewIconUrl"/>
			<c:url value="/images/help.png" var="helpIconUrl"/>
			<c:url value="/help/page_structure.pdf" var="helpPdfUrl"/>
			<spring:message code="label.pageHelp" text="Click for PDF" var="helpTitle"/>
			<img id="progressIndicator" src="${progressIconUrl}" style="margin-left: 10px; margin-top: 2px; display:none"/>		
			<div id="preview" style="float: left; display: none; margin-left: 60px;"><img style="width: 20px; height: 20px; vertical-align: middle" src="${previewIconUrl}"/><a href="#" id="previewLink"><spring:message code="link.preview"/></a></div>
			<!--Start Added for responsive design -->
			<div id="previewResponsive"
				style="float: left; display: none; margin-left: 10px;">
				<img style="width: 20px; height: 20px; vertical-align: middle"
					src="${previewIconUrl}" /><a href="#" id="previewLinkR">Preview
					</a>
			</div>
			<!--End Responsive design -->
			<div id="pageHelp" title="${helpTitle}" style="position: absolute; top: -1px; top: 3px\9; right: 10px"><a href="${helpPdfUrl}" target="_blank"><img style="width: 20px; height: 20px; vertical-align: middle" src="${helpIconUrl}"/></a></div>	
		</div>
	</fieldset>
	
	<spring:message code="title.pagePreview" text="Page Preview" var="pagePreviewTitle"/>
	<div id="pagePreviewPopup" title="${pagePreviewTitle}"></div>
	
    <div class="localeSelection" style="display: none; float: left; margin-top: 10px; margin-left: 10px">
    	<label style="margin-right: 5px"><spring:message code="label.locale"/></label>
    	<select name="selectedLocale">
    		<option value=""><spring:message code="label.default"/></option>
    		<c:forEach var="locale" items="${supportedLocales}" varStatus="status">
    			<option value="${locale}">${locale}</option>
    		</c:forEach>
    	</select>
    	<img id="changeLocaleIndicator" src="${progressIconUrl}" style="margin-left: 5px; vertical-align: middle; "/>	
    </div>	
	
	<div id="managePageFormTile">
		<c:if test="${not empty pageDefinitionBean.selectedPageDefinition || pageDefinitionBean.newPageDefinition}">
			<tiles:insertAttribute name="managePageFormTile"/>
		</c:if>
	</div>
		
	<!-- <script type="text/javascript">
		$(function() {
			var $selectedPageDefinition = $('#selectedPageDefinition');
			var $progressIndicator = $('#progressIndicator');
			var $preview = $('#preview');
			var $localeSelection = $('.localeSelection');
			var waiting = false;
			
			$selectedPageDefinition.change(function() {
				waiting = true;
				$preview.hide();
				$(this).find('option').each(function() {
					if ($(this).val() == 'pleaseSelect') {
						$(this).remove();
					}
				});				
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/page/manage.htm"/>',
					data: 'id=' + $(this).val()
				}).done(function(html) {
					$('#managePageFormTile').empty().append(html);
					$progressIndicator.hide();
					togglePreviewLinkVisibility();
					waiting = false;					
				});
				$('.success').remove();
				$('.error').remove();
				setTimeout(function() {
					if (waiting) {
						$progressIndicator.show();
					}
				}, 100);				
			}).keypress(function() {
				$(this).trigger('change');
			});
			
			$pagePreviewPopup = $('#pagePreviewPopup');
			
			 $pagePreviewPopup.dialog({ 
			      buttons: [ 
			      {	
			    	  text: '<spring:message code="button.close"/>',
			    	  click: function() { 
			    		  $(this).dialog('close');
			    		   
			    	  } 
			      }], 
				  autoOpen: false, 
			      open: function(event, ui) {
					  $pagePreviewPopup.empty();
					  $pagePreviewPopup.append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
			    	  $localeSelection.hide();
			    	  $localeSelection.find('select').prop('selectedIndex', 0);	
			    	  renderPagePreviewPopup();
			    	  $('.ui-dialog-titlebar-close').hide();
			    	  $('.ui-widget-overlay').css('width','100%');},
			      close: function(event, ui) {
			    	  $pagePreviewPopup.empty();
			      },
			      dialogClass: 'dialog', 
			      closeOnEscape: true,
			      modal: true,
			      height: 600,
			      width: 500,
			      resizable: false
			 });
			 
		    $('#previewLink').click(function() {
				$pagePreviewPopup.dialog('open');
				 //$("link[rel=stylesheet]").attr({href : "/resources/styles/style.css"});
				return false;
			});
		    function renderPagePreviewPopup(locale) {
		    	var localeParam = '';
		    	var acceptLang = 'en-gb';
		    	if (locale) {
		    		localeParam = '&locale=' + locale;
		    		acceptLang = locale.replace('_', '-');
		    	}
		    	
				$.ajax({
					url: '<c:url value="/mvc/page/preview.htm"/>',
					type: 'GET',
					data: 'id=' + $selectedPageDefinition.val() + localeParam,
					beforeSend: function(xhr) {
						xhr.setRequestHeader('Accept-Language', acceptLang);
					}
				}).done(function(html) {
					setTimeout(function() {
						$pagePreviewPopup.empty();
						$pagePreviewPopup.append(html); 
						$localeSelection.show();
						$('#changeLocaleIndicator').hide()
					}, 750);
 	  			});
				
		    }
		    
		    function togglePreviewLinkVisibility() {
		    	var newPage = $selectedPageDefinition.val() == 'newAccountPage' || $selectedPageDefinition.val() == 'newProductPage';
		    	if ($selectedPageDefinition.val() && $selectedPageDefinition.val() != 'pleaseSelect' && !newPage) {
		    		$preview.show();
		    	} else {
		    		$preview.hide();
		    	}
		    }
		    
		    togglePreviewLinkVisibility();
		    
		    function addLocaleSelectionToButtonPane() {
	    		var $buttonPane = $('.ui-dialog-buttonpane');
	    		var onButtonPane = false;
	    		if ($localeSelection.parents($buttonPane.selector).length > 0) {
	    			onButtonPane = true;
	    		}
	    		if (!onButtonPane) {
	    			$buttonPane.prepend($localeSelection);
	    		}
		    }
		    
		    addLocaleSelectionToButtonPane();

    		var $localeDropdown = $localeSelection.find('select');
    		$localeDropdown.change(function() {
    			$('#changeLocaleIndicator').show();
    			renderPagePreviewPopup($localeDropdown.val());
    		}).keypress(function() {
    			$(this).trigger('change');
    		});
		});
	</script>
 -->	
	<!-- Added for Responsive Work -->
		<script type="text/javascript">
		$(function() {
			var $selectedPageDefinition = $('#selectedPageDefinition');
			var $progressIndicator = $('#progressIndicator');
			var $preview = $('#previewResponsive');
			var $localeSelection = $('.localeSelection');
			var waiting = false;
			
			$selectedPageDefinition.change(function() {
				waiting = true;
				$preview.hide();
				$(this).find('option').each(function() {
					if ($(this).val() == 'pleaseSelect') {
						$(this).remove();
					}
				});				
				$.ajax({
					type: 'GET',
					url: '<c:url value="/mvc/page/manage.htm"/>',
					data: 'id=' + $(this).val()
				}).done(function(html) {
					$('#managePageFormTile').empty().append(html);
					$progressIndicator.hide();
					togglePreviewLinkVisibility();
					waiting = false;					
				});
				$('.success').remove();
				$('.error').remove();
				setTimeout(function() {
					if (waiting) {
						$progressIndicator.show();
					}
				}, 100);				
			}).keypress(function() {
				$(this).trigger('change');
			});
			
			$pagePreviewPopup = $('#pagePreviewPopup');
			
			 $pagePreviewPopup.dialog({ 
			      buttons: [ 
			      {	
			    	  text: '<spring:message code="button.close"/>',
			    	  click: function() { 
			    		  $("#eacStyle").removeAttr("disabled");
			    		  $("#responsiveCSS").attr("disabled", "disabled");
			    		  $(this).dialog('close');
			    		   
			    	  } 
			      }], 
				  autoOpen: false, 
			      open: function(event, ui) {
					  $pagePreviewPopup.empty();
					  $pagePreviewPopup.append('<div class="progressMessageContainer"><h2 class="progressMessage"><spring:message code="label.pleaseWait"/></h2><img src="<c:url value="/images/progress-indicator-large.gif"/>"/></div>');
			    	  $localeSelection.hide();
			    	  $localeSelection.find('select').prop('selectedIndex', 0);	
			    	  renderPagePreviewPopup();
			    	  $('.ui-dialog-titlebar-close').hide(); 
			    	  $('.ui-widget-overlay').css('width','100%');},
			      close: function(event, ui) {
			    	  $pagePreviewPopup.empty();
			      },
			      dialogClass: 'dialog', 
			      closeOnEscape: true,
			      modal: true,
			      height: 600,
			      width: 500,
			      resizable: true
			 });
			 
		    $('#previewLinkR').click(function() {
				$pagePreviewPopup.dialog('open');
				 $("#eacStyle").attr("disabled", "disabled");
				 $("#responsiveCSS").removeAttr("disabled");
				return false;
			});
		    function renderPagePreviewPopup(locale) {
		    	var localeParam = '';
		    	var acceptLang = 'en-gb';
		    	if (locale) {
		    		localeParam = '&locale=' + locale;
		    		acceptLang = locale.replace('_', '-');
		    	}
		    	
				$.ajax({
					url: '<c:url value="/mvc/page/preview.htm"/>',
					type: 'GET',
					data: 'id=' + $selectedPageDefinition.val() + localeParam,
					beforeSend: function(xhr) {
						xhr.setRequestHeader('Accept-Language', acceptLang);
					}
				}).done(function(html) {
					
					setTimeout(function() {
						$pagePreviewPopup.empty();
						$pagePreviewPopup.append(html); 
						$localeSelection.show();
						$('#changeLocaleIndicator').hide()
					}, 750);
 	  			});
				
		    }
		    
		    function togglePreviewLinkVisibility() {
		    	var newPage = $selectedPageDefinition.val() == 'newAccountPage' || $selectedPageDefinition.val() == 'newProductPage';
		    	if ($selectedPageDefinition.val() && $selectedPageDefinition.val() != 'pleaseSelect' && !newPage) {
		    		$preview.show();
		    	} else {
		    		$preview.hide();
		    	}
		    }
		    
		    togglePreviewLinkVisibility();
		    
		    function addLocaleSelectionToButtonPane() {
	    		var $buttonPane = $('.ui-dialog-buttonpane');
	    		var onButtonPane = false;
	    		if ($localeSelection.parents($buttonPane.selector).length > 0) {
	    			onButtonPane = true;
	    		}
	    		if (!onButtonPane) {
	    			$buttonPane.prepend($localeSelection);
	    		}
		    }
		    
		    addLocaleSelectionToButtonPane();

    		var $localeDropdown = $localeSelection.find('select');
    		$localeDropdown.change(function() {
    			$('#changeLocaleIndicator').show();
    			renderPagePreviewPopup($localeDropdown.val());
    		}).keypress(function() {
    			$(this).trigger('change');
    		});
		});
	</script>
</div>