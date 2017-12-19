function openModalWindow(url, options){
	if(options==null)
		options='dialogHeight:400px; dialogWidth: 330px; scroll: no; status: no;';
	window.showModalDialog(url,'', options);
}

function openNormalWindow(url, options){
	if(options==null)
		options='Height=550, Width=630, top=80, left=150, menubar=no,resizeable=yes, scrollbars=yes, status=no';
	window.open(url,'newwindow',options);
}

function hiddenOrExpand(obj) {
	if (obj.style.display == "none") {
		obj.style.display = "block";
	}
	else {
		obj.style.display = "none";
	}
}