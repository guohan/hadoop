
// JavaScript Document
var MyClient=new Array();
	function getAd6(){
	var sOption = 'dialogHeight:500px; dialogWidth: 522px; scroll: yes; status: no;';
		if(window.showModalDialog("serviceadmin/periodicbcroutine/policy_explain.htm", MyClient, sOption)) {
			tempStr = "";
			if(MyClient.length>0){
			  for(var i=0;i<MyClient.length;i++)
			 	{
					tempStr += ("["+MyClient[i]+"]")
				}
			}
			//document.all.adIds.value=tempStr;
		} else {
			//window.parent=null;
			window.opener=null;
			window.close();
		}
	}