document.onkeydown = keyboardDown;
document.onkeyup = keyboardUp;

document.oncontextmenu = function(e) {
	var evt = new Object({
		keyCode : 93
	});
	
	stopEvent(e);
	keyboardUp(evt);
}

function stopEvent(event) {
	if (event.preventDefault != undefined)
		event.preventDefault();
	if (event.stopPropagation != undefined)
		event.stopPropagation();
}

function keyboardDown(e) {
	var keycode;

	if (window.event)
		keycode = window.event.keyCode;
	else if (e)
		keycode = e.which;
	else
		return true;
}

function keyboardUp(e) {
	var keycode;

	if (window.event)
		keycode = window.event.keyCode;
	else if (e)
		keycode = e.which;
	else
		return true;
}