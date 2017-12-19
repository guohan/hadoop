var author1 = "lfgoal";
var author2 = "yujing";
//控件版本号
var version = "2.1.7Beta";
var about  = "Version:&nbsp;"+version+"&#13;Author:&nbsp;&nbsp;李峰&#13;MAIL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;lfgoal@sina.com";
//================================================ WEB 页面显示部分 =================================================//

//以下是时间控件的各项属性，可根据需要修改

//时间控件宽度 (推荐值：270 最小值：240)
var TC_WIDTH = 245;
var MIN_TC_WIDTH = 240;

//时间控件高度 (推荐值：210 最小值：180)
var TC_HEIGHT = 185;
var MIN_TC_HEIGHT = 180;

//时间控件是否有边框 是:1 否:0
var TC_BORDER = 0;
//时间控件背景色
var TC_BGCOLOR = "green";

//控件头底色
var TC_HEAD_COLOR = "#555555";
//控件体底色
var TC_BODY_COLOR = "#efefef";
//控件阴影宽度。如果控件显示过慢，可调小此参数，参数为负数时，表示取消控件阴影
var TC_SHADOW_WIDTH = 4;

//控件中显示星期几的地方的背景色(缺省蓝色)
var TC_DAY_BGCOLOR = "#0080FF";
//控件中显示星期几的地方的字体色(缺省白色)
var TC_DAY_COLOR = "#FFFFFF";
//控件中日历面板的背景色
var TC_CALENDAR_BGCOLOR = "#fafafa";

// 控件中日历面板的颜色
//当前月日期字体的颜色(前景色)
var TC_CALENDAR_COLOR = "#330066";

//面板上其他月份的日期字体的颜色(前景色)
var TC_CALENDAR_COLOR_O = "#d3d3d3";

//鼠标移动到控件上时显示的颜色
var TC_CALENDAR_BGCOLOR_OVER = "#cccccc";

//控件中系统日期的颜色(背景色,缺省粉红色)
var TC_CALENDAR_SYSDATE_BGCOLOR = "#ffc0cb";
//控件中选中的日期的颜色(背景色,缺省绿色)
var TC_CALENDAR_SELECTED_BGCOLOR = "#00aa00";

//控件底部跳转的图标按钮的颜色
var TC_NEXT_BOTTOM = "#00aa00";

//月份选择div的宽度
var TC_DIV_MONTH_WIDTH = 50;

////////////////////////////////////////////////////////////////////////////////////////////////
//以下代码不推荐修改
document.write("<IFRAME id=FrmTimeControl MARGINHEIGHT=0 SCROLLING=no FRAMEBORDER="+TC_BORDER+" style='width:"+TC_WIDTH+";height:"+TC_HEIGHT+";display:none'></IFRAME>");

//画月份选择关联菜单
document.writeln("<div oncontextmenu='window.event.returnValue=false' id='DivMonth' style='width:"+TC_DIV_MONTH_WIDTH+";BACKGROUND-COLOR:Menu;BORDER-BOTTOM:white 2px outset;BORDER-LEFT:white 2px outset;BORDER-RIGHT:white 2px outset;BORDER-TOP:white 2px outset;POSITION:absolute;display:none'>");
document.writeln("<table onselectstart='return false;' style='font-size:12px;WIDTH:100%;BACKGROUND-COLOR: #d6d3ce;BORDER-BOTTOM:#d6d3ce 1px solid;BORDER-LEFT: #d6d3ce 1px solid;BORDER-RIGHT: #d6d3ce 1px solid;BORDER-TOP: #d6d3ce 1px solid;CURSOR:hand;' border=0 cellspacing=0 cellpadding=0>");
for (var i = 0; i < 12; i++) {
	document.writeln("<tr id='TrMonth_"+i+"' height=18 onclick='getMonthValue(this)' onMouseOut=changeColor(this,'0') onMouseOver=changeColor(this,'1')><td Author='"+author2+"' align=left>&nbsp;&nbsp;"+(i+1)+"月</td></tr>");
}
document.writeln("</table></div>");

//延时画出空面板
setTimeout("drawTimeControl()", 0);

//为事件返回false值
function returnFalse() {
	return(false);
}

//画对应时间的时间控件(由上至下)
function drawTimeControl() {
	//在控件面板上屏蔽掉关联菜单
	FrmTimeControl.document.body.attachEvent("oncontextmenu",returnFalse);
	//屏蔽掉面板被鼠标拖动选中的事件
	FrmTimeControl.document.body.attachEvent("onselectstart",returnFalse);
	//画控件头
	var frameHTML = "<table border=0 cellspacing=1 cellpadding=0 width=100% height=12% bgcolor="+TC_HEAD_COLOR+" Author="+author1+">";
	frameHTML += "<tr><td align=left><font onclick='parent.preMonth()' title='上一月' style='cursor:hand;color:white;font:12px webdings'>3</font></td>";
	frameHTML += "<td align=center style='FONT-WEIGHT:bold;color:white;font:15px'>";
	frameHTML += "<input name=TxtYear readOnly style='FONT-WEIGHT:bold;color:white;background-color:"+TC_HEAD_COLOR+";border=0;width:30' onkeydown=parent.changeYear() onfocus=parent.focusTxtYear(this) onblur=parent.blurTxtYear(this)>";
	frameHTML += "<span style='FONT-SIZE: 11pt;COLOR: #ffffff'><b>年</span>";
	frameHTML += "<input name=TxtMonth readOnly style='FONT-WEIGHT:bold;color:white;background-color:"+TC_HEAD_COLOR+";border=0;width:15' onclick=parent.selectMonth()>";
	frameHTML += "<span style='FONT-SIZE: 11pt;COLOR: #ffffff'><b>月</span></td>";
	frameHTML += "<td align=right><font onclick='parent.nextMonth()' title='下一月' style='cursor:hand;color:white;font:12px webdings'>4</font></td>";
	frameHTML += "</tr></table>";
    //画控件显示星期的地方
    frameHTML += "<table height=12% onselectstart='return false;' style='border-bottom:1px solid #000000;width:100%;' COLOR=#FFFFFF bgcolor="+TC_DAY_BGCOLOR+" Author="+author1+">";
    frameHTML += "<tr align=center style='FONT-SIZE: 10pt;FONT-WEIGHT: BOLD;COLOR:"+TC_DAY_COLOR+"'>";
    frameHTML += "<TD>日</TD><TD>一</TD><TD>二</TD><TD>三</TD><TD>四</TD><TD>五</TD><TD>六</TD>";
    frameHTML += "</tr></table>";
    //画日历面板
    frameHTML += "<table id=TabCalendar onselectstart='return false;' style='border:0px solid #000000;' width=100% height=76% bgcolor="+TC_CALENDAR_BGCOLOR+" Author="+author1+">";

    for (var i = 0; i < 6; i++) {
    	frameHTML += "<tr height=11% align=center style='FONT-SIZE: 10pt;FONT-WEIGHT: BOLD;COLOR:"+TC_CALENDAR_COLOR+"'>";
    	for (var j = 0; j < 7; j++) {
    		frameHTML += "<td onmouseover=parent.changeCalendarStyle(this,'hand',parent.TC_CALENDAR_BGCOLOR_OVER) onmouseout=parent.changeCalendarStyle(this,'hand',parent.TC_CALENDAR_BGCOLOR) onclick=parent.selectDate(this)></td>";
    	}
    	frameHTML += "</tr>";
    }
    //显示页脚的HTML
    frameHTML += "<tr height=10% align=center style='FONT-SIZE: 10pt;FONT-WEIGHT: BOLD;COLOR:"+TC_CALENDAR_COLOR+"'>";
    frameHTML += "<td colspan=7>";
    frameHTML += "<font onclick=parent.preYear()  title='上一年' style='color:"+TC_NEXT_BOTTOM+";font:15px Webdings;cursor:hand'>9</font>";
    frameHTML += "<font onclick=parent.preMonth() title='上一月' style='color:"+TC_NEXT_BOTTOM+";font:15px Webdings;cursor:hand'>3</font>";
    frameHTML += "<font title='今天' onclick='parent.today()' style='color:black;font:15px webdings;cursor:hand'>1</font>";
    frameHTML += "今日：" + _sysYear + "-" + (_sysMonth+1) + "-" + _sysDate;
    frameHTML += "<font onclick=parent.nextMonth() title='下一月' style='color:"+TC_NEXT_BOTTOM+";font:15px webdings;cursor:hand'>4</font>";
    frameHTML += "<font onclick=parent.nextYear()  title='下一年' style='color:"+TC_NEXT_BOTTOM+";font:15px Webdings;cursor:hand'>:</font>";
    frameHTML += "&nbsp;&nbsp;&nbsp;&nbsp;<span onclick=parent.setTimeControlVisible(false) style='font-size:12px;cursor: hand' Author="+author1+" title="+about+"><u>关闭</u><font style='color:black;font:15px Webdings;'>r</font></span>";
    frameHTML += "&nbsp;</td></tr></table>";
    
    FrmTimeControl.document.body.style.backgroundColor = TC_BGCOLOR;
	FrmTimeControl.document.body.innerHTML = frameHTML;
	setTimeControlStyle();
	//确定控件输出主面板对象，以便后面的函数操作控件面板
	_objCalendar = FrmTimeControl.TabCalendar.children[0];
}
//================================================ WEB 页面显示部分 =================================================//

//全局变量
//年，月，日
var _year, _month, _date;
//系统时间
var _sysYear = new Date().getYear(), _sysMonth = new Date().getMonth(), _sysDate = new Date().getDate();
//存放当前年份的所有月的天数
var _arrMonthDay = new Array();
//控件输出对象(控件返回值将显示在这个对象上)
var _outSrc;
//存放对象的日期
var _outDate="";
//是否选择了日期
var _isSelected = false;
//控件输出主面板
var _objCalendar;

//控件入口函数
function JSTimeControl(src) {
	//以下两个判断是为了避免参数调整不正确而引起控件显示问题 2003-7-12 14:38	
	if (TC_WIDTH < MIN_TC_WIDTH)
		if (!confirm("您的控件宽度参数值过小，可能导致控件不能完全显示，是否继续？"))
			return;
	if (TC_HEIGHT < MIN_TC_HEIGHT)
		if (!confirm("您的控件高度度参数值过小，可能导致控件不能完全显示，是否继续？"))
			return;

	_outSrc = src;

	//根据当前控件的日期显示日历的年月
	var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/; 
	//将控件上的值匹配成正则表达式格式，放入数组中
	var r = src.value.match(reg);
	// r 不为空的情况表示时间格式符合reg正则表达式
	if (r != null) {
		//获取月份
		r[2] = r[2]-1;
		setCalenderValue(r[1],r[2],r[3]);
		_outDate = new Date(r[1],r[2],r[3]);
	}
    else {
    	setCalenderValue(_sysYear,_sysMonth,_sysDate);
    	_outDate = new Date(_sysYear,_sysMonth,_sysDate);
    }
	setTimeControlPosition(src);
	setTimeControlVisible(true);
}

//取得加减运算后的时间 入参：1. 运算前的时间 2. 偏移时间(天)
function getRelativeTime(inTime,offsetTime) {
	return new Date(inTime.valueOf() + offsetTime * 24 * 60 * 60 * 1000);
}

//初始化时间控件，使其全部显示inDate时间，如果inDate时间为空，则显示系统时间。在window.onload()事件里面加上此函数
function initTimeControl(TC_OBJ, inDate) {
	var dYear, dMonth, dDate;
	try {
		dYear  = inDate.getFullYear();
		dMonth = inDate.getMonth();
		dDate  = inDate.getDate();
	}
	catch (E) {
		dYear  = _sysYear;
		dMonth = _sysMonth;
		dDate  = _sysDate;
	}
	finally {
		if (TC_OBJ.length > 1) {
			for (var i = 0; i < TC_OBJ.length; i++) 
				TC_OBJ[i].value = dYear + "-" + changeNumber(dMonth + 1) + "-" + changeNumber(dDate);
		}
    	else 
    		TC_OBJ.value = dYear + "-" + changeNumber(dMonth + 1) + "-" + changeNumber(dDate);
	}
}

var curColor;
function changeColor(srcObject,flag)
{
	//选中的颜色:深蓝色
	var selectColor = "#191970";
	//选中状态
	if (flag == 1) {
		curColor = srcObject.style.backgroundColor;
		srcObject.style.color = "#ffffff";
		srcObject.style.cursor = 'hand';
	  	srcObject.style.backgroundColor = selectColor;
	  	return;
	}
	//离开状态
	if (flag == 0) {
		srcObject.style.color = "#000000";
		srcObject.style.cursor = 'default';
	  	srcObject.style.backgroundColor = curColor;
	  	return;
	}
}

function getMonthValue(src) {
	_month = parseInt(src.id.substring(8,src.id.length));
	setCalenderValue(_year,_month,_date);
	//隐藏月份选择div
	setDivMonthVisible(false);
}

//获取控件对象在网页上的位置。入参：对象名称或ID。
//返回值:  getObjX() 返回X坐标值 getObjY() 返回Y坐标值 
var objX = 0;	//控件的X坐标
var objY = 0;	//控件的Y坐标
function getObjPosition(obj){
  	objX = obj.offsetLeft;
  	objY = obj.offsetTop;
  	while (obj  = obj.offsetParent){
    	objX += obj.offsetLeft;
    	objY += obj.offsetTop;
    }
}

//返回X坐标值
function getObjX() {
	return this.objX;
}

//返回Y坐标值
function getObjY() {
	return this.objY;
}

//定位控件面板的输出位置
function setTimeControlPosition(src) {
	var objTC = document.all("FrmTimeControl");
	//取得当前对象的位置
	getObjPosition(src);
	var objX = getObjX();
	var objY = getObjY();
	
	//向下显示偏移高度
	var tcTopDown = objY + src.clientHeight + 6;
	//向上显示偏移高度
	var tcTopUp   = objY - TC_HEIGHT;
	
	//当控件在页面右部并且控件宽度小于页面自身宽度时，控件与右边对齐显示，否则与左边对齐显示 2003-8-6 10:43
	objTC.style.left = (objX + TC_WIDTH > document.body.offsetWidth && TC_WIDTH < document.body.offsetWidth) ? objX + src.offsetWidth - TC_WIDTH : objX;
	
	//当控件在页面底部并且控件高度小于页面自身高度时，控件向上显示，否则向下显示
	objTC.style.top = ((tcTopDown + TC_HEIGHT) > document.body.offsetHeight && TC_HEIGHT < document.body.offsetHeight) ? tcTopUp : tcTopDown;
}

//定位月份选择div的输出位置
function setDivMonthPosition() {
	var objTC = document.all("FrmTimeControl");
	var divTop = FrmTimeControl.event.clientY + objTC.offsetTop;
	//当前页面高度
	var pageHeight = document.body.offsetHeight;
    
	DivMonth.style.left = FrmTimeControl.event.clientX + objTC.offsetLeft;
	DivMonth.style.top = divTop;
}

function selectMonth() {
	setDivMonthPosition();
	setDivMonthVisible(true);
}

//焦点聚在控件头部控件年份处
function focusTxtYear(src) {
	src.style.backgroundColor = "white";
	src.style.color = "blue";
}
//控件头部控件年份处失去焦点
function blurTxtYear(src) {
	src.style.backgroundColor = TC_HEAD_COLOR;
	src.style.color = "white";
}

//改变控件头年份
function changeYear() {
	//向上翻年（上一年）
	if (FrmTimeControl.window.event.keyCode == 38) {
		setCalenderValue(parseInt(_year)-1,_month,_date);
	}
	//向下翻年（下一年）
	else if (FrmTimeControl.window.event.keyCode == 40) {
		setCalenderValue(parseInt(_year)+1,_month,_date);
	}
	//向左翻月（上一月）2003-7-26 9:41
	else if (FrmTimeControl.window.event.keyCode == 39) {
		nextMonth();
	}
	//向右翻月（下一月）2003-7-26 9:41
	else if (FrmTimeControl.window.event.keyCode == 37) {
		preMonth();
	}
	
	//使得按键事件失效，防止页面滚动 2003-7-26 9:41
	FrmTimeControl.window.event.returnValue = false;
}

//给控件头部的年月赋值
function setHead(inYear,inMonth) {
	FrmTimeControl.TxtYear.value  = inYear;
	FrmTimeControl.TxtMonth.value = inMonth + 1;
}

//清空日历面板
function cleanCalendar() {
	for (var i=0;i<6;i++) {
		for (var j=0;j<7;j++) {
			_objCalendar.children[i].children[j].style.backgroundColor = TC_CALENDAR_BGCOLOR;
			_objCalendar.children[i].children[j].style.color = TC_CALENDAR_COLOR;
		}
	}
	
}

//根据传入的参数往日历面板中填值
function setCalenderValue(inYear,inMonth,inDate) {
	_year = inYear;
	if (_year > 9999 || _year < 1000)
		return;
	_month = inMonth;
	setHead(_year,_month);
	setMonthDay(inYear);
	_date = (inDate <= 0) ? 1 : inDate;
	_date = (inDate > _arrMonthDay[_month]) ? _arrMonthDay[_month] : inDate;
	//本月的第一天
	var firstDay = new Date(inYear,inMonth,1).getDay();
	//清空日历面板
    cleanCalendar();
    //如果本月第一天不是周日，那么前面用上个月的后几天补上，颜色加以区分
	if (firstDay != 0) {
		//上一个月最后一天的日期
		var lastMonthDate = _arrMonthDay[getLastMonth(inMonth)];
		//用其他颜色区分其他月份日期的字体色
		for (var i = 0; i < firstDay; i++) {
		    _objCalendar.children[0].children[i].innerText = lastMonthDate - firstDay + i + 1;
		    _objCalendar.children[0].children[i].style.color = TC_CALENDAR_COLOR_O;
		}
	}
	var isCurrentMonth = (_year == _sysYear && _month == _sysMonth) ? true : false;
	var dDate = 1,nextDate = 1;
	//填充第一行的当前月份的日期值
	for (var i = firstDay; i < 7; i++) {
		if (isCurrentMonth && dDate == _sysDate) 
			_objCalendar.children[0].children[i].style.backgroundColor = TC_CALENDAR_SYSDATE_BGCOLOR;
		if (dDate == _date)
			_objCalendar.children[0].children[i].style.backgroundColor = TC_CALENDAR_SELECTED_BGCOLOR;
		
		_objCalendar.children[0].children[i].innerText = dDate++;
		_objCalendar.children[0].children[i].style.color = TC_CALENDAR_COLOR;
	}
	for (var i = 1; i < 6; i++) {
		for (var j = 0; j < 7; j++) {
			if (dDate <= _arrMonthDay[inMonth]) {
				//当前系统时间区分颜色显示
				if (isCurrentMonth && dDate == _sysDate) 
					_objCalendar.children[i].children[j].style.backgroundColor = TC_CALENDAR_SYSDATE_BGCOLOR;
				if (dDate == _date)
					_objCalendar.children[i].children[j].style.backgroundColor = TC_CALENDAR_SELECTED_BGCOLOR;
			    else 
			    	_objCalendar.children[i].children[j].style.color = TC_CALENDAR_COLOR;
			    _objCalendar.children[i].children[j].innerText = dDate++;
			}
			else {
				_objCalendar.children[i].children[j].innerText = nextDate++;
				_objCalendar.children[i].children[j].style.color = TC_CALENDAR_COLOR_O;
			}
		}
	}
}

//鼠标移到日期上，该日期变色显示
//鼠标移出日期上，该日期变回原来的颜色
function changeCalendarStyle(src,cursor,color) {
	src.style.cursor = cursor;
	var isCurrentMonth = (_year == _sysYear && _month == _sysMonth) ? true : false;
	window.status = _date;
	//不是选中或者系统时间的选择日期就不变色
	if (!(src.innerText == _sysDate && isCurrentMonth) && src.innerText != parseInt(_date/1))
		src.style.backgroundColor = color;
}

//选择日历
function selectDate(src) {
	_date = src.innerText;
	//选中其他月份
	if (src.style.color == TC_CALENDAR_COLOR_O) {
		//选中其他月份的日期,判断是否大于15是为了区分选中的上一月的日期还是下一月的日期
		//下一月的日期不管怎样，在控件面板上都不会超过15，这个判断条件在面板改变的情况下可能不适用 2003-8-2 16:40
		_month = (_date > 15) ? getLastMonth(_month) : getNextMonth(_month);
	}
	_isSelected = true;
	output();
	setTimeControlVisible(false);
}

//控件输出
function output() {
	if (_isSelected) 
		_outSrc.value = _year + "-" + changeNumber(_month+1) + "-" + changeNumber(_date);
	else 
	    _outSrc.value = _outDate.getFullYear() + "-" + changeNumber(_outDate.getMonth()+1) + "-" + changeNumber(_outDate.getDate());
	_isSelected = false;
}

//上一年
function preYear() {
	setCalenderValue(parseInt(_year)-1,_month,_date);
	output();
}

//上一月
function preMonth() {
	setLastMonth(_month);
	setCalenderValue(_year, _month, _date);
	output();
}

//快捷方式选中当前系统时间
function today() {
	setCalenderValue(_sysYear,_sysMonth,_sysDate);
	_outSrc.value = _sysYear + "-" + changeNumber(_sysMonth + 1) + "-" + changeNumber(_sysDate);
	setTimeControlVisible(false);
	
}

//下一月（控件底部翻下一月份快捷方式）
function nextMonth() {
	setNextMonth(_month);
	setCalenderValue(_year,_month,_date);
	output();
}

//下一年（控件底部翻下一年份快捷方式）
function nextYear() {
	setCalenderValue(parseInt(_year)+1,_month,_date);
	output();
}

//取得上一月
function getLastMonth(month) {
	return (month == 0) ? 11 : month - 1;
}

//取得下一月
function getNextMonth(month) {
	return (month == 11) ? 0 : month + 1;
}

//当前时间加一月
function setNextMonth(month) {
	_month = getNextMonth(month);
	_year = (_month == 0 && _year < 9999) ? parseInt(_year) + 1 : _year;
}

//当前时间减一月
function setLastMonth(month) {
	_month = getLastMonth(month);
	_year = (_month == 11 && _year >1000) ? parseInt(_year) - 1 : _year;
}

//小于10的数字前面补零
function changeNumber(num) {
	return (num < 10) ? "0" + num : num;
}

function setTimeControlStyle() {
	var objTC = document.all("FrmTimeControl");
	objTC.style.position = "absolute";
	//TC_SHADOW_WIDTH不小于0时，增加控件阴影效果
	if (TC_SHADOW_WIDTH >= 0)
	    objTC.style.filter = "progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#aaaaaa,strength=" + TC_SHADOW_WIDTH + ")";
}

function setTimeControlVisible(isVisible) {
	setDivMonthVisible(false);
	document.all("FrmTimeControl").style.display = (isVisible) ? "inline" : "none";
}

function setDivMonthVisible(isVisible) {
	DivMonth.style.display = (isVisible) ? "inline" : "none";
}

//判断平闰年，给每月的天数数组赋值
function setMonthDay(selectYear) {
	if (((selectYear % 4 == 0) && (selectYear % 100 != 0)) || (selectYear % 400 == 0)) {
		_arrMonthDay = new Array(31,29,31,30,31,30,31,31,30,31,30,31);
	}
	else 
		_arrMonthDay = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
}

//任意点击时关闭该控件
function document.onclick() {
	with (window.event.srcElement) {
		if (getAttribute("Author") != author2 && DivMonth.style.display == "inline")
		    setDivMonthVisible(false);
		if (getAttribute("Author") != author1 && getAttribute("Author") != author2 && tagName != "INPUT" && document.all("FrmTimeControl").style.display == "inline") {
    		setTimeControlVisible(false);
    	}
	}
}