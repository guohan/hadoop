
// JavaScript Document
var rowId=1;
var platformListAll;
function addIP(){
      if(rowId>initialProgramcount)
      {
         alert("对不起，您已经频繁操作，不能再继续！");
         return;
      }
      var tr = getRow();
      var td1 = getTd();
      //var td2 = getTd();
      var td6 = getTd();
      var td9 = getTd();
      var td10 = getTd();
      var td11 = getTd();
      var input1  = getInitialProgram("initialProgram"+rowId,"");
      //input1.size = 20;
      //var input2  = getFile("textFile"+rowId,"");
      //input2.size = 20;
     
      var input6  = getTerminalDeviceInput("terminalSupport"+rowId,"");
      input6.size = 15;
      
      var input9  = getPlatform("platform"+rowId);
      input9.size = 1;
      var input10  = getInputReadOnly("fileName"+rowId,"");
      var input7  = getHidden("programId"+rowId,"");
     
      var input8  = getHidden("programKey"+rowId,"");
      
      var input11 = getInput("contentUUID"+rowId,"");
      
      //新加：机型系列、分辨率、特性
        var input_series = getHidden("series"+rowId,"","series"+rowId);
      var input_screenSize = getHidden("screenSize"+rowId,"","screenSize"+rowId);
      var input_feature = getHidden("feature"+rowId,"","feature"+rowId);
      var platformSelect = getHidden("platformSelect"+rowId,"","platformSelect"+rowId);
      add(td1,input_series);
      add(td1,input_screenSize);
      add(td1,input_feature);
      add(td1,platformSelect);
      
      add(td1,input6);
      add(td1,input1);
      //add(td2,input2);
     
      add(td6,input6);
      add(td9,input9);
      add(td10,input10);
      add(td11,input11);
      add(tr,td9);
      add(tr,td1);
      add(tr,td10);
      add(tr,td11);
    
      add(tr,td6);
     
      add(parNode,tr);
    }
    
    
function addProgram(platformList,platformId,platformName){    
	  platformListAll = platformList;  
      var tr = getRow();
      var td1 = getTd();
      //var td2 = getTd();
      var td6 = getTd();
      var td9 = getTd();
      var td10 = getTd();
      var td11 = getTd();
      var input1  = getInputReadOnly("initialProgram"+rowId,"");
      //input1.size = 20;
      //var input2  = getFile("textFile"+rowId,"");
      //input2.size = 20;
       var input6  = getTerminalDeviceInput("terminalSupport"+rowId,"");
      input6.size = 15;
      var input9  = getPlatformReadOnly("platform"+rowId,platformId,platformName);
      input9.size = 1;
      var input10  = getInputReadOnly("fileName"+rowId,"");     
      var input7  = getHidden("programId"+rowId,"");    
      var input8  = getHidden("programKey"+rowId,"");
      var input11 = getInput("contentUUID"+rowId,"");
      //新加：机型系列、分辨率、特性
      var input_series = getHidden("series"+rowId,"","series"+rowId);
      var input_screenSize = getHidden("screenSize"+rowId,"","screenSize"+rowId);
      var input_feature = getHidden("feature"+rowId,"","feature"+rowId);
      var platformSelect = getHidden("platformSelect"+rowId,"","platformSelect"+rowId);
      add(td1,input_series);
      add(td1,input_screenSize);
      add(td1,input_feature);   
      add(td1,platformSelect);   
    
      add(td1,input6);
      add(td1,input1);
      //add(td2,input2);
      add(td6,input6);
      add(td9,input9);
      add(td10,input10);
      add(td11,input11);
      add(tr,td9);
      add(tr,td1);
      add(tr,td10);
      add(tr,td11);
      add(tr,td6);
     
      add(parNode,tr);
    }
function getRow(){
	var tr = document.createElement("tr");
	var td = document.createElement("td");
	var input = document.createElement("<input name='rowId'/>");
	/*
	????????:
	The NAME attribute cannot be set at run time on elements dynamically created with the createElement method. To create an element with a name attribute, include the attribute and value when using the createElement method.
	*/
	input.type="checkbox";
	input.value=rowId;
	rowId++;
	td.appendChild(input);
	tr.appendChild(td);
	return tr;
}
function getTd(){
	var td = document.createElement("td");
	td.align="left";
	return td;
}
function getTextarea(name,value){
	var textarea = document.createElement("<textarea name='"+name+"'/>");
	textarea.cols = 20;
	textarea.rows = 2;
	if(value!="")
	{
		textarea.value=value;
	}
	return textarea;
}
function getCheckbox(name,value){
	var checkbox = document.createElement("<input name='"+name+"'/>");
	checkbox.type="checkbox";
	if(value!="")
	{
		checkbox.value=value;
	}
	return checkbox;
}
function getRadio(name,value){
	var radio = document.createElement("<input name='"+name+"'/>");
	radio.type="radio";
	if(value!="")
	{
		radio.value = value;
	}
	return radio;
}

function getSelect(name,size){
	var selectbox = document.createElement("<select name='"+name+"'/>");
	selectbox.size = size;
	//selectbox.name="select1";
	return selectbox;
}
function getOption(text,value){
	var oOption = document.createElement("OPTION");
	oOption.text=text;
	oOption.value=value;
	return oOption;
}
function getCalendar(path){
	var calendar = document.createElement("<a href='#'>");
	cPath = path + 'images/show-calendar.gif';
	calendar.innerHTML="<img id='img' src="+cPath+"  onClick='javascript:getDateString(event.srcElement.parentNode.parentNode.childNodes[0],oCalendarChs);'alt='????????????' width=34 height=21 border=0 align='absmiddle'>";
	return calendar;
}
function getImage(value){
	var Img = document.createElement("img");
	Img.src = value;
	return Img;
}
function getText(value){
	var text = document.createTextNode(value); //????????TextNode????
	return text;
}
/*function addContent(){
	var tr = getRow();
	var input  = getInput();
	var td = getTd();
	var textarea = getTextarea();
	var checkbox = getCheckbox();
	var radio = getRadio();
	var selectbox = getSelect("test");
	var option = getOption("test","test");
	//td.appendChild(input);
	//tr.appendChild(td);
	//parNode.appendChild(tr);
	add(td,input);
	add(td,textarea);
	add(td,checkbox);
	add(td,radio);
	//add(selectbox,option);
	selectbox.add(option);
	add(td,selectbox);
	add(tr,td);
	add(parNode,tr);
	alert(parNode.innerHTML);
}*/

function add(parentNode,childNode){
	parentNode.appendChild(childNode);
}


function deleteRow(parNodeName){
	if(parNodeName == null) parNodeName = 'parNode';
	var rowId = document.all.rowId;
	if(rowId!=null)
	{
		if(rowId.length!=null)
		{
			var removeNodes = new Array();
			j=0;
			for(var i=0;i<rowId.length;i++)
			{
				if(rowId[i].checked)
				{
					removeNodes[j] = rowId[i].parentNode.parentNode;
					j++;
				}
			}
			for(var k=0;k<removeNodes.length;k++)
			{
				eval(parNodeName +".removeChild(removeNodes[k])");
				
			}
			
		}
		else
		{
			 if(rowId.checked)
				{
					var removeNode = rowId.parentNode.parentNode;
					eval(parNodeName +".removeChild(removeNode)");
				}
		}
	}
}
function deleteAllRow(parNodeName){
	if(parNodeName == null) parNodeName = 'parNode';
	var rowNum = document.all.rowId;
	if(rowNum!=null)
	{
		if(rowNum.length!=null)
		{
			var removeNodes = new Array();
			j=0;
			for(var i=0;i<rowNum.length;i++)
			{
				if(rowNum[i].checked)
				{
					removeNodes[j] = rowNum[i].parentNode.parentNode;
					j++;
				}
			}
			for(var k=0;k<removeNodes.length;k++)
			{
				eval(parNodeName +".removeChild(removeNodes[k])");
				
			}
			
		}
		else
		{
			 if(rowNum.checked)
				{
					var removeNode = rowNum.parentNode.parentNode;
					eval(parNodeName +".removeChild(removeNode)");
				}
		}
	}
	rowId=1;
}

function getTimer(objName,path){
	var calendar = document.createElement("<a href='#'>");
	timePath = path + 'images/show-time.gif';
	calendar.innerHTML="<img id='img' src="+timePath+"  onClick='javascript:selectTime(\""+objName+"\");'alt='????????????' width=34 height=21 border=0 align='absmiddle'>";
	return calendar;
}
function getInput(name,value, id){
	var Input;
	if(id == null)
		input = document.createElement("<input name='"+name+"'/>");
	else
		input = document.createElement("<input name='"+name+"' id='" + id + "'/>");
	input.type="text";
	input.size="10";
	if(value!="")
	{
		input.value = value;
	}
	return input;
}



function getFile(name,value, id){
	var Input;
	if(id == null)
		input = document.createElement("<input name='"+name+"' contentEditable='false'/>");
	else
		input = document.createElement("<input name='"+name+"' id='" + id + "' contentEditable='false'/>");
	input.type="File";
	input.size="10";
	if(value!="")
	{
		input.value = value;
	}
	return input;
}
function getInitialProgram(name,value, id){
	var Input;
	if(id == null)
		input = document.createElement("<input name='"+name+"' contentEditable='false' onchange='ShowValue(this.value);'/>");
	else
		input = document.createElement("<input name='"+name+"' id='" + id + "' contentEditable='false' onchange='ShowValue(this.value);'/>");
	input.type="File";
	input.size="10";
	if(value!="")
	{
		input.value = value;
	}
	return input;
}
function getHidden(name,value, id){
	var Input;
	if(id == null)
		input = document.createElement("<input name='"+name+"'/>");
	else
		input = document.createElement("<input name='"+name+"' id='" + id + "'/>");
	input.type="hidden";
	input.size="10";
	if(value!="")
	{
		input.value = value;
	}
	return input;
}
function getTerminalDeviceSyn(name,value,id){
    //alert("deviceValue="+deviceValue);
	var Input;
	if(id == null)
		input = document.createElement("<input name='"+name+"' value='' id='"+rowId+"'onclick='queryTerminalDevice(this.id,this.value);' style='cursor:hand;' readonly/>");
	else
		input = document.createElement("<input name='"+name+"' id='" + rowId + "' value='' onclick='queryTerminalDevice(this.id,this.value);' style='cursor:hand;' readonly/>");
	input.type="text";
	input.size="10";
	if(value!="")
	{
		input.value = value;
	}
	return input;
}

function getPlatform(name){
	var arr = parsePlatform();
	//alert("name="+name);
	var selectbox = document.createElement("<select name='"+name+"' id='"+name+"'/>");
	
	selectbox.size = 1;
	for(var i=0;i<arr.length;i++){
		var platformMap = arr[i];
		var newOption = getOption(platformMap.name, platformMap.id);
		selectbox.options.add(newOption);
	}
	return selectbox;
}

function getPlatformReadOnly(name,platformId,platformName){
	var arr = parsePlatform();
	//alert("name="+name);
	var selectbox = document.createElement("<select name='"+name+"' id='"+name+"'/>");
	
	var newOption = getOption(platformName, platformId);
	selectbox.options.add(newOption);

	return selectbox;
}


function parsePlatform(){
	var newArr = [];
	var platformList = platformListAll.substring(2,platformListAll.length);
	platformList = platformList.substring(0,platformList.length-2);
	var arr = platformList.split("}, {");
	for(i in arr){
		var temp = {};
		temp.name = (arr[i].split(",")[0]).split("=")[1];
		temp.id = (arr[i].split(",")[1]).split("=")[1];
		newArr.push(temp);
	}
	return newArr;
}

