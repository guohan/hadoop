
var dragobject = null;
var tx;
var ty;

function getReal(el) {
	temp = el;

	while ((temp != null) && (temp.tagName != "BODY")) {
		if ((temp.className == "moveme") || (temp.className == "handle")){
			el = temp;
			return el;
		}
		temp = temp.parentElement;
	}
	return el;
}


function moveme_onmousedown() {
	el = getReal(window.event.srcElement)
	if (el.className == "moveme") {
		dragobject = el;
		ty = (window.event.clientY - dragobject.style.pixelTop);
		tx = (window.event.clientX - dragobject.style.pixelLeft);
		window.event.returnValue = false;
		window.event.cancelBubble = true;
	}
	else if (el.className == "handle") {
		tmp = el.getAttribute("for");
		if (tmp != null) {
			el = eval(tmp);
			dragobject = el;
			ty = (window.event.clientY - dragobject.style.pixelTop);
			tx = (window.event.clientX - dragobject.style.pixelLeft);
			window.event.returnValue = false;
			window.event.cancelBubble = true;
		}
		else {
			dragobject = null;
		}
	}
	else {
		dragobject = null;
	}
}

function moveme_onmouseup() {
	if(dragobject) {
		dragobject = null;
	}
}

function moveme_onmousemove() {
	if (dragobject) {
		if(window.event.clientX >= 0) {
			dragobject.style.left = window.event.clientX - tx;
			dragobject.style.top = window.event.clientY - ty;
		}
		window.event.returnValue = false;
		window.event.cancelBubble = true;
	}
}

if (document.all) { //This only works in IE4 or better
	document.onmousedown = moveme_onmousedown;
	document.onmouseup = moveme_onmouseup;
	document.onmousemove = moveme_onmousemove;
}

document.write("<style>");
document.write(".moveme		{cursor: move;}");
document.write(".handle		{cursor: move;}");
document.write("</style>");