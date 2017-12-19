var pops = new Array();
var MAX_DEGREE = 5;
function CreatePopup(degree) 
{ 
    if (degree < 0)
        return null; 
    if (pops[degree] != null)
        return pops[degree]; 

    if (degree == 0) 
        pops[0] = window.createPopup();
    else{ 
        if (pops[degree - 1] == null) 
            pops[degree - 1] = CreatePopup(degree - 1);
        pops[degree] = pops[degree - 1].document.parentWindow.createPopup();
    } 
    pops[degree].document.body.setAttribute("degree", degree); 
    return pops[degree]; 
} 
CreatePopup(MAX_DEGREE);

var canHideSubMenu = new Array(MAX_DEGREE);
for(var i = 0; i < MAX_DEGREE; i++){
	canHideSubMenu[i]=true;
}

var mouseon = new Array(MAX_DEGREE);
for(var i = 0; i < MAX_DEGREE; i++){
	mouseon[i]=false;
}

var _targetPage;

function MenuItem(title,target,href, hasChild){
	  this.title=title;
	  this.target=target;
	  this.href=href;
	  this.hasChild=hasChild;
}

var m_strMainMenuStyle = ""; 
var m_strMainMenuItemStyle = ""; 
var m_strMMItemProBase = {}; 
var m_strMMItemProMOver = {}; 
var m_strMMItemSplitImg; 
var m_strTitle = "aaa"; 
var m_strSubMenuStyle = "font-size:9pt;border:1 solid black;cursor:hand;filter:alpha(opacity=40);";
var m_strSubMenuItemStyle = "background-color:#3399FF;color:#006699;filter:alpha(opacity=40);"; //var m_strSubMenuItemStyle = "background-color:#3399FF;color:#006699;filter:alpha(opacity=40);"; 
var m_strSMItemProBase = {}; 
var m_strSMItemProMOver = {}; 
var m_strSMItemBorderB = ""; 

var m_strContextPath = ""; 

var m_doc; 

var STR_POS_FRONT = "bbb"; 
setDefaultPopStyle();
function chgMainMenuStyle(obj,bolChg) 
{ 
    if ( bolChg ) {
        for( var pro in m_strMMItemProMOver ) { 
            obj.style[pro] = m_strMMItemProMOver[pro]; 
        } 
    } else {
        obj.style.border = "0"; 
        for( var pro in m_strMMItemProBase ) { 
            obj.style[pro] = m_strMMItemProBase[pro]; 
        } 
    } 
}
function chgSubMenuStyle(obj,bolChg) { 	
    if ( bolChg ) {
	    
        for( var pro in m_strSMItemProMOver ) { 
            obj.style[pro] = m_strSMItemProMOver[pro]; 
			
        } 
    } else {
        for( var pro in m_strSMItemProBase ) { 
            obj.style[pro] = m_strSMItemProBase[pro]; 
        } 
    } 
}


function showSubMenu1(objShow,strGetID,bShow,targetPage,degree) { 
	_targetPage=targetPage;
	var objGet = eval(document.getElementById(strGetID)); 
	var oPopup = pops[degree-1]; 
	if(objGet==null && objShow.id.indexOf("Bar_")>=0){
		objGet=eval(document.getElementById(objShow.id.substring(4)));
	}
	var index=0;
	if(objGet==null && objShow.id.indexOf("panel")>=0){
		var s = objShow.id.replace("_b", "_").replace("_sm", "_b");
		index = s.substring(s.indexOf("_b")+2);
		objGet=eval(document.getElementById(s));
	}

		if(objGet==null) return;
		oPopup.document.body.style["background-color"]="transparent";
	oPopup.document.body.innerHTML = objGet.innerHTML; 

    oPopup.show(0,0,1,1,objShow); 
    var intWidth = oPopup.document.body.scrollWidth; 
    var intHeight = oPopup.document.body.scrollHeight; 
    oPopup.hide(); 

	if(degree>1){
		oPopup.show(pops[degree-2].document.body.scrollWidth,index*20,intWidth,intHeight,objShow); //?????????
	}
	else
		oPopup.show(objShow.offsetWidth-level_3_rightspace,objShow.offsetHeight-35,intWidth,intHeight,objShow); //?????????
} 

function hideSubMenu1(degree){ 
	
	if(canHideSubMenu[degree-1]==true){
		pops[0].hide();
	}
} 

function hideSubMenuForce(){ 
		var onpop = false;
		for(var i = 0; i < mouseon.length; i++){
			if(mouseon[i]==true){
				onpop = true;
				break;
			}
		}
		if(onpop) return;
		for(var i = 0; i < canHideSubMenu.length; i++){
			canHideSubMenu[i]=true;	
		}		
		hideSubMenu1(1);
} 


function setDefaultPopStyle(){
//m_strSMItemProBase["background"]="url(images/popbg.gif)";//"#3399FF";
	m_strSMItemProBase["backgroundColor"]="#BFEDFF";//"#3399FF";
	m_strSMItemProBase["color"]="#000066";//"#ff0000";
	
	//m_strSMItemProMOver["background"]="url(images/popbg_over.gif)";//"#3366FF";
	m_strSMItemProMOver["backgroundColor"]="#003399";//"#3366FF";
	m_strSMItemProMOver["color"]="#ff0000";//"#000066";

    
	//??????? 
	m_strSubMenuStyle ="bordercolor:#dddddd;borderstyle:solid;borderwidth:1;font-size:9pt;cursor:hand;"; //"font-size:9pt;border:1 solid black;cursor:hand";
	//???????attribute //background-color:#DCE9F0;color:#006699;
	m_strSubMenuItemStyle = "background-color:#BFEDFF;color:#000066; font-size:9pt;";//"background-color:#999999;color:#006699"; //#3399FF
	//m_strSubMenuItemStyle = "background:url(images/popbg.gif);color:#006699; ";//"background-color:#999999;color:#006699"; //#3399FF
    m_strSMItemBorderB="border-bottom-width:1;border-bottom-color:#ffffff;font-size:9pt;border-bottom-style:solid";	
	
	
	//m_strSMItemProBase["backgroundColor"]="#DCE9F0";//"#3399FF";
	//m_strSMItemProBase["color"]="#006699";//"#006699";
	
	//m_strSMItemProMOver["backgroundColor"]="#4F8EB6";//"#3366FF";
	//m_strSMItemProMOver["color"]="#DCE9F0";//"#000066";

	//m_strSubMenuStyle ="bordercolor:#006699;borderstyle:solid;borderwidth:1;font-size:9pt;cursor:hand;"; //"font-size:9pt;border:1 solid black;cursor:hand";
	//子菜单项的样式attribute //background-color:#DCE9F0;color:#006699;
	//m_strSubMenuItemStyle = "background-color:#DCE9F0;color:#006699; ";//"background-color:#999999;color:#006699"; //#3399FF
    //m_strSMItemBorderB="border-bottom-width:1;border-bottom-color:#A6CAF0;border-bottom-style:solid";
}

function getNodeStyle(strPath) { 
    var strStyle = ""; 
    if( m_doc.selectSingleNode(strPath) ) { 
        var nodeList = m_doc.selectSingleNode(strPath).childNodes; 
        for( var i = 0;i < nodeList.length;i ++ ) { 
            var nodeName = nodeList[i].nodeName; 
            var nodeText = nodeList[i].text; 
            strStyle += nodeName.toLowerCase() + ":" + nodeText + ";"; 
        } 
    } 
    return strStyle; 
} 

function getNodeProperty(strPath) { 
    var arrProperty = {}; 
    if( m_doc.selectSingleNode(strPath) ) { 
        var nodeList = m_doc.selectSingleNode(strPath).childNodes; 
        for( var i = 0;i < nodeList.length;i ++ ) { 
            var nodeName = nodeList[i].nodeName; 
            nodeName = nodeName.toLowerCase(); 
            var nodeText = nodeList[i].text; 
            var aName = nodeName.split("-"); 
            var strProperty = aName[0]; 
            for( var j = 1;j < aName.length;j ++ ) { 
                strProperty += aName[j].substring(0,1).toUpperCase() + aName[j].substr(1); 
            } 
            arrProperty[strProperty] = nodeText; 
        } 
    } 
    return arrProperty; 
} 


function endHideSubMenu(degree){
	canHideSubMenu[degree-1]=false;
	//alert(canHideSubMenu[0]);
}
function beginHideSubMenu(degree){
	canHideSubMenu[degree-1]=true;

}

function printSubMenu(arrMenuItem,menuID){
	    var i=0;
        var strSubMenuID = menuID;//"sm" + i; 
		var degree = 1;
		for(var i = 0; i < strSubMenuID.length; i++){
			if(strSubMenuID.charAt(i)=='_') degree++;
		}
 			var parent = "";
			for(var i = 1; i < degree; i++){
				parent += "parent.";
			}
         
        var strSubMenuHtml = "<div style='display:none;border:0;' id='" + strSubMenuID + "'>" + 
                    " <table border='0'   cellspacing='0' cellpadding='0' borderColorLight='#FFFFFF' style='	border-top-width: 2px;	border-right-width: 2px;	border-bottom-width: 2px;	border-left-width: 2px;	border-top-style: solid;	border-right-style: solid;	border-bottom-style: solid;	border-left-style: solid;	border-top-color: #FFFFFF;	border-right-color: #666666;	border-bottom-color: #666666;	border-left-color: #FFFFFF;	font-family:??;	   cursor:hand;'>"; 
                   // " <table  cellspacing='0' cellpadding='4' style='" + m_strSubMenuStyle + "' id='tbl1'>"; 
		var tdwidth = 0;
        for ( var j = 0;j < arrMenuItem.length;j ++ ) { 
            var menuText =arrMenuItem[j].title; 
			if(menuText.length * 20 > tdwidth)
				tdwidth = menuText.length * 20;
		}
		for ( var j = 0;j < arrMenuItem.length;j ++ ) { 
            var menuText =arrMenuItem[j].title; 
            //var scriptDot1="\"";
            var menuHref = arrMenuItem[j].href;//"http://www.csdn.net";//"javascript:alert()"; //"+scriptDot1+"hello world!"+scriptDot1+"//http://www.csdn.net
			menuHref = menuHref.replace(/\"/g, "\\\"");
            var menuTarget = arrMenuItem[j].target;

			var vmenuTarget = "_self"; 
	
			var strRightRow = "&nbsp"; 

			if(arrMenuItem[j].hasChild){
				strRightRow = "<img src='"+level_3_arrow+"'>";
			}
            
			
            var strSubMenu2ID = strSubMenuID + "_sm" + j; 

            var strMouseOverEvent ="javascript:"+ parent +"chgSubMenuStyle(this,true);"+ parent +"endHideSubMenu("+ (degree-1) +");" + parent + "mouseon["+ (degree-1) +"]=true;"; 

            var strMouseOutEvent = "javascript:"+ parent +"chgSubMenuStyle(this,false);"+ parent +"beginHideSubMenu("+ (degree) +");" + parent + "mouseon["+ (degree-1) +"]=false;"; 


            var strPosition = STR_POS_FRONT +  menuText +  "-=" 
                            + menuText; 
			var scriptDot1="\"";
			var scriptDot2=",\"";
            var strOnClickEvent = "javascript:"+ parent +"onClickEvent("+scriptDot1 + menuHref + scriptDot1+scriptDot2+menuTarget +scriptDot1+scriptDot2+ strPosition + scriptDot1+",parent);"+ parent +"beginHideSubMenu(1);"+parent+"hideSubMenu1(1);" ;
			//"javascript:parent.onClickEvent("+scriptDot1+menuHref+scriptDot1+scriptDot2+menuTarget+scriptDot1+scriptDot2+strPosition+",parent"+");";//"javascript:parent.onClickEvent(\"" + menuHref + \","+ "\""+menuTarget + \","+"\"" + strPosition + "\");" ;

            strSubMenuHtml += "<tr id='"+ strSubMenu2ID +"' style='" + m_strSubMenuItemStyle + "' " + 
                " onmouseover='"+ strMouseOverEvent + parent +"b_mOver_ie5p(\""+ strSubMenu2ID +"\",top.bottomFrame,"+ degree +");'" + 
                " onmouseout='" + strMouseOutEvent +"' ";
            strSubMenuHtml += " onclick= '" + strOnClickEvent + " '";
            strSubMenuHtml +=" > <td width='30' height='20' nowrap align='center' style='" + m_strSMItemBorderB + "'><img src='"+level_3_star+"'></td>"+ 			
				//strSubMenuHtml += " > <td width='10' nowrap style='" + m_strSMItemBorderB + "'>&nbsp;</td>"+ 
                "    <td nowrap style='" + m_strSMItemBorderB + "'>" + menuText + "</td>"+ 
                "    <td width='30' align='center' nowrap style='" + m_strSMItemBorderB + "'>" + strRightRow + "</td>" + 
                "</tr>"; 

        }

        strSubMenuHtml += "</table></div>";
        document.write(strSubMenuHtml); 
} 


function onClickEvent(strUrl,strTarget,strViewText,parentwin) 
{ 
    m_strTitle = ""; 
    var arrText = strViewText.split("->"); 
    if ( arrText.length >0 ) 
        m_strTitle = arrText[0]; 
    for (var i = 1; i < arrText.length ; i ++ ) 
    { 
        m_strTitle += "－" + arrText[i]; 
    } 
    m_strTitle = m_strTitle.substring(STR_POS_FRONT.length); 
	

    if (m_strTitle != "") 
    { 

    } 
     
    if ( strUrl == "#" ) 
        return; 

    if(strUrl.toLowerCase().indexOf("javascript") != -1) { 
        executeScript(strUrl); 
        return; 
    } 

    strUrl = m_strContextPath + strUrl;



	_targetPage.location=strUrl;


    
} 
function executeScript(strScript) 
{ 
    eval(strScript); 
} 
