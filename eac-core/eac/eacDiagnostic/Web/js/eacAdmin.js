
ajax = $.ajax;

/**
 * This function wraps JQuery's ajax() function and does two things:
 * 
 * 1) It ensures that the 'ajaxSource' parameter is present in the request. This
 * 		parameter tells Spring that the request is an ajax request so that Spring
 * 		can respond with the Spring-Redirect-URL header.
 * 2) It adds an always() handler which looks for the Spring-Redirect-URL header and
 * 		will redirect the whole page to this URL in the event of the ajax call failing.
 * 		If the response does not contain a Spring-Redirect-URL header but did have
 * 		a status code of 403 Forbidden, then the whole page will redirect to login.
 * 
 * Clients can thus use $.ajax() as normal without having to worry about error handling
 * as this wrapper will just deal with it all transparently.
 * 
 */
$.ajax = function(args) {
	var type = args.type;
	if (!type || type.toUpperCase() == "GET") {
		var url = args.url;
		if (url != null && url.indexOf("ajaxSource") == -1) {
			if (url.substr(url.length-1) === '#') {
				url = url.substr(0, url.length-1);
			}
			if (url.indexOf("?") == -1) {
				url = url + "?";
			} else {
				url = url + "&";
			}
			url = url + "ajaxSource=1"; 
		}
		args.url = url;
	} else if (type.toUpperCase() == "POST") {
		var data = args.data;
		if (!data) {
			data = "ajaxSource=1";
		} else if (data.indexOf("ajaxSource") == -1) {
			data = data + "&ajaxSource=1";
		}
		args.data = data;
	}
	var jqXhr = ajax(args);
	jqXhr.always(function() {
		var redirect = jqXhr.getResponseHeader("Spring-Redirect-URL");
		if (redirect == null && jqXhr.status == 403) {
			redirect = "/eacAdmin/login";
		}
		if (redirect != null) {
			window.location.replace(redirect);
		}
	});
	return jqXhr;
};

$(function() {
	$('.shorten').shorten();
	$(document).delegate('.actionImg', 'mouseenter', function() {
		$(this).css({'border': '2px solid #CADBE9'});
	});
	$(document).delegate('.actionImg', 'mouseleave', function() {
		$(this).css({'border': '2px solid #FFF'});
	});
});


/**
 * Asset/Product Finder
 */
function initialiseFinder($jqFinder, properties) {
	if (!initialiseFinder.finderIds) {
		initialiseFinder.finderIds = [];
	}
	for (var i = 0; i < initialiseFinder.finderIds.length; i++) {
		var finderId = initialiseFinder.finderIds[i];
		if (finderId == $jqFinder.attr('id')) {
			// Don't reinitialise existing finders
			return;
		}
	}
	var $finderPopup = $jqFinder.find('.finderPopup');
	var $displayField = $jqFinder.find('div input:disabled');
	var $openButton = $jqFinder.find('.openButton');
	var $searchLink = $jqFinder.find('.searchLink');
	var $finderForm = $jqFinder.find('.finderForm');
	var $finderResults = $jqFinder.find('.finderResults');
	
	function openPopup() {
		$finderPopup.css('visibility', 'visible');
		$finderPopup.find('.shorten').css('visibility', 'visible');
		$finderPopup.find('input:text').focus();
		$openButton.addClass('buttonPressed');
		$openButton.blur();
		if ($.browser.msie) {
			// Workaround to hide lower stacked checkboxes in IE8
			$(document).find('input[type="checkbox"]').each(function() {
				$(this).css('visibility', 'hidden');
			});		
			$(document).find('input[type="radio"]').each(function() {
				if ($(this).closest('.finder').length == 0) {
					$(this).css('visibility', 'hidden');
				}
			});		
			// Workaround to hide lower stacked JQuery dialog buttons in IE8
			$(document).find('.ui-button').each(function() {
				if ($('div.ui-dialog-content').css('overflow') == 'visible') {
					$(this).css('visibility', 'hidden');
				}
			});		
		}
	}
	
	function closePopup() {
		$finderPopup.css('visibility', 'hidden');
		$finderPopup.find('.shorten').css('visibility', 'hidden');
		$openButton.removeClass('buttonPressed');
		if ($.browser.msie) {
			$(document).find('input[type="checkbox"]').each(function() {
				$(this).css('visibility', 'visible');
			});
			$(document).find('input[type="radio"]').each(function() {
				if ($(this).closest('.finder').length == 0) {
					$(this).css('visibility', 'visible');
				}
			});
			$(document).find('.ui-button').each(function() {
				if ($('div.ui-dialog-content').css('overflow') == 'visible') {
					$(this).css('visibility', 'visible');
				}
			});
		}
	}
	
	function find() {
		if ($finderForm.find('input:text').val() != '') {
			$finderResults.empty().append('<div class="progressMessageContainer"><h2 class="progressMessage">' + properties.pleaseWaitMessage + '</h2><img src="' + properties.contextPath + '/images/progress-indicator-large.gif"/></div>');
			$.ajax({
				url: properties.contextPath + '/mvc/' + properties.mode + '/finder.htm',
				type: 'POST',
				data: createPostParams()
			}).done(function(html) {
				setTimeout(function() {
					$finderResults.empty().append(html);
				}, 500);
			});
		}
	}
	
	function createPostParams() {
		var postParams = 'search_term=';
		postParams +=  encodeURIComponent($finderForm.find('input[name="search_term"]').val());
		postParams += '&search_by=';
		postParams += $finderForm.find('input[type="radio"]:checked').val();
		
		if( properties.assetStates ) {
			var states = properties.assetStates.split(',');
			for ( var i = 0 ; i < states.length ; i++) {
				var state = jQuery.trim(states[i]);
				if ( state ) {
					postParams += '&asset_state=' + state;
				}	
			}
		}
		
		var type = $finderForm.find('input[name="type"]').val();
		if (type) {
			postParams += '&type=' + type;
		}
		
		return postParams;
	}
	
	function resultClicked(id, name) {
		closePopup();
		$displayField.val(name).attr('title', name).trigger("change");
		var returnField = $('#' + properties.returnFieldId);
		if (returnField.length > 0) {
			returnField.val(id).trigger('change');
		}
		if (properties.returnNameFieldId) {
			var returnNameField = $('#' + properties.returnNameFieldId);
			if (returnNameField.length > 0) {
				returnNameField.val(name).trigger('change');
			}
		}
	}		
	
	function initialiseFromGuid(guid) {
		$.ajax({
			url: properties.contextPath + '/mvc/' + properties.mode + '/finder.htm',
			type: 'POST',
			data: 'guid=' + guid,
			dataType: 'json'
		}).done(function(data) {
			resultClicked(data.id, data.name);
		});
	}
	
	$openButton.click(function(e) {
		if ($finderPopup.css('visibility') == 'hidden') {
			openPopup();
		} else {
			closePopup();
		}
	});
	
	$searchLink.click(function() {
		find();
	});
	
	$finderForm.keypress(function(e) {
		if (e.keyCode == 13 && $finderPopup.css('visibility') == 'visible') { 
			find(); 
			e.preventDefault();
			e.stopPropagation();
		}
	});
	
	$finderResults.delegate('.result', 'mouseover', function() {
		$(this).css('background-color', '#CADBE9');
	});
	
	$finderResults.delegate('.result', 'mouseout', function() {
		$(this).css('background-color', '#FFFFFF');
	});
	
	$finderResults.delegate('.result', 'click', function() {
		var $result = $(this);
		resultClicked($result.attr('id'), $result.attr('title'));
	});
	
	$(document).click(function(e) {
		var targetClass = $(e.target).attr('class');
		if (targetClass == undefined) {
			targetClass = '';
		}
		if (targetClass.indexOf('openButton') < 0 && 
				$(e.target).parents('.finderPopup').length == 0 && 
				targetClass.indexOf('finderPopup') < 0) {
			closePopup();
		}
	});
	
	$(document).keydown(function(e) {
		if (e.keyCode == 27 && $finderPopup.css('visibility') == 'visible') { closePopup(); }
	});
	
	if (properties.initGuid) {
		initialiseFromGuid(properties.initGuid);
	}	
	
	if (properties.initOnChangeId) {
		$('#' + properties.initOnChangeId).change(function() {
			initialiseFromGuid($(this).val());
		});
	}
	
	var $iconClear = $jqFinder.find('.iconClear');
	
	$displayField.change(function() {
	    if ($(this).val().length) {
	    	$iconClear.stop().show();
	    }
	});
	
	$iconClear.click(function() {
		$(this).hide();
		resultClicked('', '');
		$finderResults.empty();
		$finderForm.find('.textField').val('');
		$displayField.val(properties.initText);
	});
	
	if (properties.resetButtonId) {
		$('#' + properties.resetButtonId).click(function() {
			$iconClear.click();
		});
	}
	
	$finderPopup.css('visibility', 'hidden');
	$displayField.val(properties.initText);
	
	initialiseFinder.finderIds.push($jqFinder.attr('id')); // Keep a record of finders we've initialised
}

var showPleaseWait = function(selector){
	var message = selector;
	if(typeof selector === 'undefined'){
		message = $('<div style="progressMessageContainer"><h2>Please Wait ...</h2><img src="/eacAdmin/images/progress-indicator-large.gif"/></div>');
	} else {
		message = selector;
	}
	$.blockUI({ 
		message: message, 
			css: { 
        		border: '2px solid #AAA', 
        		padding: '15px', 
        		backgroundColor: '#fff', 
        		'-webkit-border-radius': '10px', 
        		'-moz-border-radius': '10px', 
        		borderRadius: '5px',
        		opacity: 1, 
        		color: '#000' 
    		}
	});
};

var eacConfirm = function(paramObject){
	var contents = paramObject['contents'];
	var titleText = paramObject['title'];
	var overrideURL= paramObject['url'];
	var callbackYES = paramObject['callbackYes'];
	var callbackNO = paramObject['callbackNo'];
	var width = paramObject['width'];
	if(typeof width === 'undefined'){
		width = 400;
	}
	var $dialog;
	if(typeof contents === 'undefined'){
		$dialog = $('<div class="confirmDialog"><div class="confirmDialogLabel"/><div class="confirmDialogWarning">This action can\'t be un-done</div></div>');
	} else {
		$dialog = contents;
	}
	var handler = function(e) {
        e.preventDefault();
        
        var $target = $('#'+e.target.id);
        /*
         * To make it work for buttons
         * 
         * if($target.is('button') == false){
        	$target = $(this);
        }*/
        if(true){
        	$target = $(this);
        }
        
        var theHREF;
        if(overrideURL){
        	theHREF = overrideURL;
        }else{
        	theHREF = $target.attr("href");
        }
        
        var label   = $target.attr("eacLabel");
        if(label){        	
        	$dialog.find('.confirmDialogLabel').text(label);
        }
        $dialog.dialog({
            modal: true,
            bgiframe: true,
            width: width,
            height: "auto",
      		autoOpen: false,
      		title: titleText,
      		resizable: false,
	        open: function(event, ui) {
				$(".ui-dialog-titlebar-close").hide(); 
				$('.ui-widget-overlay').css('width','100%');
	        },      		
      		buttons : {
                "Yes" : function() {
                	$dialog.dialog("close");
                	if(callbackYES){
                		callbackYES(e);
                	}else{
                		window.location.href = theHREF;
                	}
            		showPleaseWait();
                },//yes
                "No" : function() {
                    $dialog.dialog("close");
                    if(callbackNO){
                    	callbackNO(e);
                    }
                }//no 
           }//buttons
		});//dialog
        $dialog.dialog("open");	        
	};//handler
	return handler;
};

function addAutocompleteMessageTo(selector) {
	$(selector).autocomplete({
		open: function(event, ui) {
			var $autocomplete = $('ul.ui-autocomplete'); 
			$autocomplete.css('width', '460px');
			$autocomplete.find('li a').addClass('shorten');
			$('.shorten').shorten();
		},
		source: '/eacAdmin/mvc/autocomplete/messages.htm'
	});
}

function createLinkedIcon(selector, entityName, linkedNames, autoOpen) {
	var $selector = $(selector);
	$selector.css('position', 'relative');
	var $img = $('<img id="linkedToIcon" style="position: absolute; top: 30px; right: 15px" src="/eacAdmin/images/linkedto.png"/>');
	$selector.append($img);
	var version = 0;
	if ($.browser.msie) {
		version = $.browser.version;
	}
	if (autoOpen) {
		if (!version || version >= 9) {
			setTimeout(function() {
				$img.animate({boxShadow: '0px 0px 20px rgba(255, 0, 0, 1)', backgroundColor: '#FF9C9C'}, 200);
				setTimeout(function() {
					$img.animate({boxShadow: '0px 0px 0px rgba(255, 0, 0, 0)', backgroundColor: 'transparent'}, 200);
				}, 4700);
			}, 400);
		}
	}
	var text = '\'' + entityName + '\' used by:<br/><br/><ul>';
	for (var i = 0; i < linkedNames.length; i++) {
		var name =  linkedNames[i];
		if (name) {
			text += '<li>' + name + '</li>';
		}
	}
	text += '</ul>';
	$img.attr('title', text);
	$img.tipTip({edgeOffset: 7, maxWidth: '400px', defaultPosition: 'right'});
	if (autoOpen) {
		$img.trigger('mouseover');
		setTimeout(function() {
			if ($('#linkedToIcon').length == 0) {
				$('#tiptip_holder').hide();
			} 
			$img.trigger('mouseout');
		}, 5000);
	}
}