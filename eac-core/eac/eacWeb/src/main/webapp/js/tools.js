function init() {
	detect();
}

function detect() {
	if (!navigator.cookieEnabled) {
		alert('You need to enable the usage of cookies in your browser to be able to access all the functionality of this OUP website. Full details of the usage of cookies can be found in the Privacy Policy.');
	}
}

function detectTestCookie() {
	var testCookie = getCookie("TEST_COOKIE");
	if (navigator.cookieEnabled && (testCookie == null || testCookie == "")) {
		alert('You need to enable the usage of cookies in your browser to be able to access all the functionality of this OUP website. Full details of the usage of cookies can be found in the Privacy Policy.');
	}
}

function getCookie(c_name) {
	var i,x,y,ARRcookies=document.cookie.split(";");
	for (i=0;i<ARRcookies.length;i++) {
		x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
		y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
		x=x.replace(/^\s+|\s+$/g,"");
		if (x==c_name) {
			return unescape(y);
		}
	}
	return null;
}

function keepPageAlive(durationObj) {
	var hours = durationObj.hours;
	var interval = 900000; // 900000ms == 15 minutes
	var duration = hours * 60 * 60 * 1000;
	maxReqCnt = Math.floor(duration / interval);
	
	keepAliveInternal(0);
	
	function keepAliveInternal(reqCnt) {
		setTimeout(function() {
			var success = true;
			$.ajax({
				url: 'keepAlive.htm?ajax=true',
				type: 'GET',
				async: false
			}).error(function() {
				success = false;
			});
			if (!success) {
				return;
			}
			var newReqCnt = reqCnt + 1;
			if (newReqCnt >= maxReqCnt) {
				return;
			}
			keepAliveInternal(newReqCnt);
		}, interval);
	}
}

$(function() {
	// Prevent double form submissions
	var submitted = false;
	var $form = $('form');
	if ($form.length > 0) {
		$form.submit(function() {
			if (submitted) {
				return false;
			} 
			submitted = true;
		});
	}
});

var EAC_PASSWORD_VALIDATOR = (function () {

	var msgBadPassword = 'Invalid Password';
	var msgPasswordsDoNotMatch = 'Passwords do not match';
	
	var setError = function($badImage, $badError, message) {
		$badImage.attr('alt',   message);
		$badImage.attr('title', message);
		$badImage.show();
		//$badError.text(message);
		//$badError.show();
	};
	
	
	var requestContext='/eac/';
	var onEvent = function(){
		var $target = $(this);
		var $good = $target.nextAll('.passwordGood:first');
		var $bad  = $target.nextAll('.passwordBad:first');
		var $error  = $target.nextAll('.passwordError:first');		
		$target.addClass('current');
		var $other = $(':password').not('.current');
		$target.removeClass('current');
		
		
		$.ajax({
			type: 'GET',
			dataType: 'json',
		url: requestContext+'validatePassword.htm',
		data: 'password=' + encodeURIComponent($target.val()),
		success: function(data, textStatus, jqXHR) {
			var otherCss = $other.nextAll('.passwordGood:first:').css('display')
			var otherIsGood = otherCss !== 'none';
			if(otherIsGood){
	            if (data.valid && $other.val() == $target.val()) {
					//password valid and passwords match
	       			$bad.hide();
	       			$error.hide();
	           		$good.show();
				} else {
					//password invalid OR passwords don't match
					$good.hide();
					//we don't show errors if the password is blank
					if($target.val() !== '') {
						if(data.valid){
							//=>passwords don't match
							setError($bad,$error,msgPasswordsDoNotMatch);
						}else{
							//=>password invalid
							setError($bad,$error,msgBadPassword);
						}
					}else{
						$bad.hide();
						$error.hide();
					}
				};
			}else{
				//we only care if this password is valid
	            if (data.valid) {
	       			$bad.hide();
	       			$error.hide();
	           		$good.show();
	           		if($other.val() === $target.val()){
	           			//Make the other Green
	           			var $otherGood = $other.nextAll('.passwordGood:first');
	           			var $otherBad  = $other.nextAll('.passwordBad:first');
	           			$otherBad.hide();
	           			$otherGood.show();
	           		}
				} else {
					$good.hide();
					//we don't show errors if the password is blank
					if($target.val() !== ''){
						setError($bad,$error,msgBadPassword);
					}else{
						$bad.hide();
						$error.hide();
					}
				};
			}
		}
		});
	};

	var pInit = function(requestContextValue){
		$(':password').each(function(){
			$div = $(this).closest('div');
			$div.append('<img class="passwordGood" src="images/tick.png" alt=""  width="14" height="14" style="display:none;"/>');
			$div.append('<img class="passwordBad"  src="images/cross.jpg" alt=""  width="14" height="14" style="display:none;"/>');
			$div.append('<span class="passwordError"/>');
		});
		requestContext = requestContextValue;
		$('form').delegate(':password', 'change keyup', onEvent);
	};

	return {
		init : pInit
	};
}());


