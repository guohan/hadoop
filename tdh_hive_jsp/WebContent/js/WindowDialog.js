
// JavaScript Document
	function getModelDialog(url){
	var sOption = 'dialogHeight:500px; dialogWidth: 750px; scroll: no; status: no;';
		//alert(url);
		if(window.showModalDialog(url, window, sOption)) {
		} else {
			//window.parent=null;
			window.opener=null;
			window.close();
		}
	}
	
	function OpenDialogWindow(url,option){ 
		if(window.showModalDialog(url, window, option)) {

		} else {
			window.opener=null;
			window.close();
		}
	}