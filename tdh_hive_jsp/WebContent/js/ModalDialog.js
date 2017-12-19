
// JavaScript Document "serviceadmin/broadcastservice/customer_import.jsp"
var MyClient=new Array();
	var sOption = 'dialogHeight:550px; dialogWidth: 750px; scroll: yes; status: no;';
	function getCustomerImport(url){ 
		//alert(url);
		if(window.showModalDialog(url, window, sOption)) {

		} else {
			window.opener=null;
			window.close();
		}
	}
	
	function openDialog(url,option){ 
	
		if(window.showModalDialog(url, window, option)) {

		} else {
			window.opener=null;
			window.close();
		}
	}