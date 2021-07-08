var EAC_TEACHERS_CLUB_LOGIN_SUPPORT = function() {
	
	/*
	 * Copyright (c) 2012 Oxford University Press  
	 */

	//assumes that jquery has already been loaded in the containing page.

	//the ID of the username input field
	var usernameID = 'eacUsername';
	
	//the jquery selector for the username input field
	var usernameSEL = '#'+usernameID;
	
	//the jquery selector for the username label
	var usernameLabelSEL = 'label[for="' + usernameID + '"]';

	//the ID of the password input field
	var passwordID = 'eacPassword';
	
	//the jquery selector for the password input field
	var passwordSEL = '#'+passwordID;
	
	//the jquery selector for the password label
	var passwordLabelSEL = 'label[for="' + passwordID + '"]';
	
	//the properties for both versions of the password input field
	var passwordTemplate = 'maxlength="15" name="password" class="required input-surround" aria-labelledby="Password-ariaLabel"';
	
	//the password text field (used to show the label before password input is entered)
	var $passwordTextField = $('<input type="text"    ' + passwordTemplate +' >').attr('id',passwordID);
	
	//the proper password input field (used to capture password input)
	var $passwordField     = $('<input type="password"' + passwordTemplate +' >').attr('id',passwordID);


	// called when focus goes into password 'text' field for the 1st time.
	function switchPWField () {
		
		//look up the password input field
		var $password = $(passwordSEL);
		
		//remove the event bindings
		$password.unbind('click');
		$password.unbind('focus');
		
		//replace current password input field with a proper password input field.
		$password.replaceWith($passwordField);
		
		//lookup new password input field
		$password2 = $(passwordSEL);
		
		//set the contents of the password input field to empty.
		$password2.val('');
		
		//set focus on new password input field
		$password2.focus();		
		setTimeout(function(){
			$password2.focus();
		},500);
	}
	
	// called when focus goes into username 'text' field for the 1st time.
	function switchUNField () {
		//lookup the username input field
		var $username = $(usernameSEL);
		
		//remove event bindings
		$username.unbind('focus');
		$username.unbind('click');
		
		//set the contents of the input field to empty
		$username.val('');
	}	
	
	
	// adds classes to the divs for teachers club styling.
	function configureDivs(){
		
		//new style teachers club classes for eacLoginWidgetContainer
		$('#eacLoginWidgetContainer').addClass('content-inner with-position-link-2');
		
		//new style teachers club classes for eacLoginBody
		$('#eacLoginBody').addClass('form-wrapper');
	}
	
	function configureLoginElements(titleUsername, titlePassword){
 		
		//variables
		var $username = $(usernameSEL);
		
		var $password = $(passwordSEL);
		
		var $form =  $username.closest('form');
		var $submit = $('input[type="submit"]', $form);
		var $usernameLabel = $(usernameLabelSEL);
		var $passwordLabel = $(passwordLabelSEL);
		
		//classes
		$submit.addClass('fl-right buttons formbutton_3');		
		$username.addClass('required input-surround');
		$usernameLabel.addClass('overlabel offset');
		$passwordLabel.addClass('overlabel offset');
		
		//titles
		$username.attr("title", titleUsername);
		$password.attr("title", titlePassword);
		
		//aria labelling
		$passwordLabel.attr("id","Password-ariaLabel");
		
		$username.attr("aria-labelledby","Username-ariaLabel");
		$usernameLabel.attr("id","Username-ariaLabel");
	}

	//adds some html structure elements to the login widget
	function configureLoginStructure(){
		
		//wrap the username,password and button divs each in an LI element
		var li = '<li class="form-row requiredRow">';		
		$('#eacLoginUsername').wrap(li);
		$('#eacLoginPassword').wrap(li);
		$('#eacLoginButton').wrap(li);

		//put an UL element beneath the FORM to wrap the form's LI children
		
		var $ul = $('<ul/>');
		
		var $form = $(usernameSEL).closest('form');
		
		$form.children().each(function() {
			var child = $(this);
		    $ul.append(child);		    		   
		});
		
		$form.append($ul);
		
		//wrap the eacLoginMessage in an H3 element
		$('#eacLoginMessage').wrap('<H3/>');
		
		$('#eacLoginProblemsLink').attr('href','http://elt.oup.com/help_support?cc=global&selLanguage=en&mode=hub');		
	}
	
	function configureLoginBehaviour(){
		
		//take the login label and place it in login field
		var usernameLabel = $(usernameLabelSEL).text();
		
		var $username = $(usernameSEL);
		$username.val(usernameLabel);
		  
	    //register event handlers on the username field
	    $username.bind('focus click', switchUNField);	    
	    
	    //replace the (input type='password') with (input type='text') so we can see contents in the password field 				
		var $password = $(passwordSEL);
	    $password.replaceWith($passwordTextField);
	    
	  	//take the password label and place it in password field
	  	var passwordLabel = $(passwordLabelSEL).text();
	  	
	    var $password2 = $(passwordSEL);
	  	$password2.val(passwordLabel);
	    
		//register event handlers on the password text field
	    $password2.bind('focus click', switchPWField);		
	}
	
	function configureWelcomeDivs(){
		var li = '<li>';
		$('#eacProfileLink').wrap(li);
		$('#eacLogoutLink').wrap(li);		

		//put an UL element beneath the eacLoginFooter DIV to wrap the form's LI children
		
		var $ul = $('<ul/>');
		
		var $footerDiv = $('#eacLoginFooter');
		
		$footerDiv.children().each(function() {
			var child = $(this);
		    $ul.append(child);
		});
		
		$footerDiv.append($ul);
	}
	
	function configWelcome(){
		//configure the DIVS to match teachers club styling & structure
		configureDivs();
		
		configureWelcomeDivs();
		
		//wrap the eacLoginMessage in an H3 element to match teachers club styling
		$('#eacLoginMessage').wrap('<H3/>');
 
	}
	
	function configLogin(titleUserName, titlePassword){
		//configure the DIVS to match teachers club styling & structure
		configureDivs();
		
		//configure the login form to match teachers structure
		configureLoginStructure();
		
		//configure the login form to match teachers dynamic behaviour
		configureLoginBehaviour();
		
		//configure non-DIV elements to match teachers club styling and structure
		configureLoginElements(titleUserName, titlePassword);
		
	}

	return {
		configureWelcome: function(){
			configWelcome()
		},
		configureLogin: function(titleUserName, titlePassword){
			configLogin(titleUserName,titlePassword)
		}
	};
}();// end EAC_TEACHERS_CLUB_LOGIN
