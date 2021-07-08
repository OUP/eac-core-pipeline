function init() {
	detect();
}

function detect() {
	if (!navigator.cookieEnabled) {
		alert('You need to enable the usage of cookies in your browser to be able to access all the functionality of this OUP website. Full details of the usage of cookies can be found in the Privacy Policy.');
	}
}
