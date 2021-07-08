(function($) {
	
	var LOWER = /[a-z]/,
		UPPER = /[A-Z]/,
		DIGIT = /[0-9]/,
		DIGITS = /[0-9].*[0-9]/,
		SPECIAL = /[^a-zA-Z0-9]/,
		SAME = /^(.)\1+$/,
		WHITESPACE = /(\s)/;
		
	function rating(rate, message) {
		return {
			rate: rate,
			messageKey: message
		};
	}
	
	function uncapitalize(str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	$.validator.passwordRating = function(password, username) {
		if (!password || password.length < 1)
			return rating(0, "no-password");
		if (!password || (password.length >= 1 &&  password.length < 6))
			return rating(1, "too-short");
		if (username && password.toLowerCase().match(username.toLowerCase()))
			return rating(1, "similar-to-username");
		if (SAME.test(password))
			return rating(1, "same");	
		if (WHITESPACE.test(password))
			return rating(1, "no-whitespace");
				
		var lower = LOWER.test(password),
			upper = UPPER.test(password),
			digit = DIGIT.test(password),
			digits = DIGITS.test(password),
			special = SPECIAL.test(password);
			
		
		if (password.length >= 6 && ((lower && upper && digit && special) || (lower && upper && digits && special )))
			return rating(4, "strong");
		if (password.length >= 6 && ((lower && upper && (digit || special)) || (lower && upper && (digits || special))))
			return rating(3, "good");		
		if (password.length >= 6 && lower && upper )
			return rating(2, "weak");		
		if (password.length >= 6 && (lower || ((lower && (digit || special)) || (lower && (digits || special)))))
			return rating(1, "only-lower");		
		if (password.length >= 6 && (upper || ((upper && (digit || special)) || (upper & (digits || special)))))
			return rating(1, "only-upper");		
		if (password.length >= 6 && (digit || digits || special) )
			return rating(1, "only-digit");
		return rating(1, "very-weak");
	}
	
	$.validator.passwordRating.messages = {
		"no-password": "Minimum of 6 characters, one or more letters and capital letters",
		"too-short": "Please enter 6 or more characters",
		"similar-to-username": "Similar to username",
		"same": "Please enter a mix of letters",
		"no-whitespace": "No spaces allowed",
		"very-weak": "Invalid Password",
		"only-lower": "Please enter a capital letter (A-Z)",
		"only-upper": "Please enter a letter (a-z)",
		"only-digit": "Please enter some letters",
		"weak": "Poor",
		"good": "Good",
		"strong": "Very good"
	}
	
	$.validator.addMethod("passwordcheck", function(value, element, usernameField) {
		// use untrimmed value
		var password = element.value,
		// get username for comparison, if specified
			username = $(typeof usernameField != "boolean" ? usernameField : []);
			
		var rating = $.validator.passwordRating(password, username.val());
		// update message for this field
		
		var meter = $(".password-meter", element.form);
		
		meter.find(".password-meter-bar").removeClass().addClass("password-meter-bar").addClass("password-meter-" + rating.messageKey);
		meter.find(".password-meter-message").removeClass().addClass("password-meter-message").addClass("password-meter-message-" + rating.messageKey).text($.validator.passwordRating.messages[rating.messageKey]);
		// display process bar instead of error message
		
		if(rating.messageKey == "no-password"){			
			$('.password-meter-bg').css('display','none');
			$('#passwordstrength').css('display','none');
			
		}else if(!(rating.messageKey == "strong" || rating.messageKey == "good" || rating.messageKey == "weak" )){		
			$('.password-meter-bg').css('display','');
			$('#passwordstrength').css('display','none');
			
		}else{
			$('.password-meter-bg').css('display','');
			$('#passwordstrength').css('display','');
		}
		
		
		return rating.rate > 2;
	}, "&nbsp;");
	// manually add class rule, to make username param optional
	$.validator.classRuleSettings.passwordcheck = { passwordcheck: true };
	
})(jQuery);
