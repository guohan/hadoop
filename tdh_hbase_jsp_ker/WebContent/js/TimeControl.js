var author1 = "lfgoal";
var author2 = "yujing";
//�ؼ��汾��
var version = "2.1.7Beta";
var about  = "Version:&nbsp;"+version+"&#13;Author:&nbsp;&nbsp;���&#13;MAIL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;lfgoal@sina.com";
//================================================ WEB ҳ����ʾ���� =================================================//

//������ʱ��ؼ��ĸ������ԣ��ɸ�����Ҫ�޸�

//ʱ��ؼ���� (�Ƽ�ֵ��270 ��Сֵ��240)
var TC_WIDTH = 245;
var MIN_TC_WIDTH = 240;

//ʱ��ؼ��߶� (�Ƽ�ֵ��210 ��Сֵ��180)
var TC_HEIGHT = 185;
var MIN_TC_HEIGHT = 180;

//ʱ��ؼ��Ƿ��б߿� ��:1 ��:0
var TC_BORDER = 0;
//ʱ��ؼ�����ɫ
var TC_BGCOLOR = "green";

//�ؼ�ͷ��ɫ
var TC_HEAD_COLOR = "#555555";
//�ؼ����ɫ
var TC_BODY_COLOR = "#efefef";
//�ؼ���Ӱ��ȡ�����ؼ���ʾ�������ɵ�С�˲���������Ϊ����ʱ����ʾȡ���ؼ���Ӱ
var TC_SHADOW_WIDTH = 4;

//�ؼ�����ʾ���ڼ��ĵط��ı���ɫ(ȱʡ��ɫ)
var TC_DAY_BGCOLOR = "#0080FF";
//�ؼ�����ʾ���ڼ��ĵط�������ɫ(ȱʡ��ɫ)
var TC_DAY_COLOR = "#FFFFFF";
//�ؼ����������ı���ɫ
var TC_CALENDAR_BGCOLOR = "#fafafa";

// �ؼ�������������ɫ
//��ǰ�������������ɫ(ǰ��ɫ)
var TC_CALENDAR_COLOR = "#330066";

//����������·ݵ������������ɫ(ǰ��ɫ)
var TC_CALENDAR_COLOR_O = "#d3d3d3";

//����ƶ����ؼ���ʱ��ʾ����ɫ
var TC_CALENDAR_BGCOLOR_OVER = "#cccccc";

//�ؼ���ϵͳ���ڵ���ɫ(����ɫ,ȱʡ�ۺ�ɫ)
var TC_CALENDAR_SYSDATE_BGCOLOR = "#ffc0cb";
//�ؼ���ѡ�е����ڵ���ɫ(����ɫ,ȱʡ��ɫ)
var TC_CALENDAR_SELECTED_BGCOLOR = "#00aa00";

//�ؼ��ײ���ת��ͼ�갴ť����ɫ
var TC_NEXT_BOTTOM = "#00aa00";

//�·�ѡ��div�Ŀ��
var TC_DIV_MONTH_WIDTH = 50;

////////////////////////////////////////////////////////////////////////////////////////////////
//���´��벻�Ƽ��޸�
document.write("<IFRAME id=FrmTimeControl MARGINHEIGHT=0 SCROLLING=no FRAMEBORDER="+TC_BORDER+" style='width:"+TC_WIDTH+";height:"+TC_HEIGHT+";display:none'></IFRAME>");

//���·�ѡ������˵�
document.writeln("<div oncontextmenu='window.event.returnValue=false' id='DivMonth' style='width:"+TC_DIV_MONTH_WIDTH+";BACKGROUND-COLOR:Menu;BORDER-BOTTOM:white 2px outset;BORDER-LEFT:white 2px outset;BORDER-RIGHT:white 2px outset;BORDER-TOP:white 2px outset;POSITION:absolute;display:none'>");
document.writeln("<table onselectstart='return false;' style='font-size:12px;WIDTH:100%;BACKGROUND-COLOR: #d6d3ce;BORDER-BOTTOM:#d6d3ce 1px solid;BORDER-LEFT: #d6d3ce 1px solid;BORDER-RIGHT: #d6d3ce 1px solid;BORDER-TOP: #d6d3ce 1px solid;CURSOR:hand;' border=0 cellspacing=0 cellpadding=0>");
for (var i = 0; i < 12; i++) {
	document.writeln("<tr id='TrMonth_"+i+"' height=18 onclick='getMonthValue(this)' onMouseOut=changeColor(this,'0') onMouseOver=changeColor(this,'1')><td Author='"+author2+"' align=left>&nbsp;&nbsp;"+(i+1)+"��</td></tr>");
}
document.writeln("</table></div>");

//��ʱ���������
setTimeout("drawTimeControl()", 0);

//Ϊ�¼�����falseֵ
function returnFalse() {
	return(false);
}

//����Ӧʱ���ʱ��ؼ�(��������)
function drawTimeControl() {
	//�ڿؼ���������ε������˵�
	FrmTimeControl.document.body.attachEvent("oncontextmenu",returnFalse);
	//���ε���屻����϶�ѡ�е��¼�
	FrmTimeControl.document.body.attachEvent("onselectstart",returnFalse);
	//���ؼ�ͷ
	var frameHTML = "<table border=0 cellspacing=1 cellpadding=0 width=100% height=12% bgcolor="+TC_HEAD_COLOR+" Author="+author1+">";
	frameHTML += "<tr><td align=left><font onclick='parent.preMonth()' title='��һ��' style='cursor:hand;color:white;font:12px webdings'>3</font></td>";
	frameHTML += "<td align=center style='FONT-WEIGHT:bold;color:white;font:15px'>";
	frameHTML += "<input name=TxtYear readOnly style='FONT-WEIGHT:bold;color:white;background-color:"+TC_HEAD_COLOR+";border=0;width:30' onkeydown=parent.changeYear() onfocus=parent.focusTxtYear(this) onblur=parent.blurTxtYear(this)>";
	frameHTML += "<span style='FONT-SIZE: 11pt;COLOR: #ffffff'><b>��</span>";
	frameHTML += "<input name=TxtMonth readOnly style='FONT-WEIGHT:bold;color:white;background-color:"+TC_HEAD_COLOR+";border=0;width:15' onclick=parent.selectMonth()>";
	frameHTML += "<span style='FONT-SIZE: 11pt;COLOR: #ffffff'><b>��</span></td>";
	frameHTML += "<td align=right><font onclick='parent.nextMonth()' title='��һ��' style='cursor:hand;color:white;font:12px webdings'>4</font></td>";
	frameHTML += "</tr></table>";
    //���ؼ���ʾ���ڵĵط�
    frameHTML += "<table height=12% onselectstart='return false;' style='border-bottom:1px solid #000000;width:100%;' COLOR=#FFFFFF bgcolor="+TC_DAY_BGCOLOR+" Author="+author1+">";
    frameHTML += "<tr align=center style='FONT-SIZE: 10pt;FONT-WEIGHT: BOLD;COLOR:"+TC_DAY_COLOR+"'>";
    frameHTML += "<TD>��</TD><TD>һ</TD><TD>��</TD><TD>��</TD><TD>��</TD><TD>��</TD><TD>��</TD>";
    frameHTML += "</tr></table>";
    //���������
    frameHTML += "<table id=TabCalendar onselectstart='return false;' style='border:0px solid #000000;' width=100% height=76% bgcolor="+TC_CALENDAR_BGCOLOR+" Author="+author1+">";

    for (var i = 0; i < 6; i++) {
    	frameHTML += "<tr height=11% align=center style='FONT-SIZE: 10pt;FONT-WEIGHT: BOLD;COLOR:"+TC_CALENDAR_COLOR+"'>";
    	for (var j = 0; j < 7; j++) {
    		frameHTML += "<td onmouseover=parent.changeCalendarStyle(this,'hand',parent.TC_CALENDAR_BGCOLOR_OVER) onmouseout=parent.changeCalendarStyle(this,'hand',parent.TC_CALENDAR_BGCOLOR) onclick=parent.selectDate(this)></td>";
    	}
    	frameHTML += "</tr>";
    }
    //��ʾҳ�ŵ�HTML
    frameHTML += "<tr height=10% align=center style='FONT-SIZE: 10pt;FONT-WEIGHT: BOLD;COLOR:"+TC_CALENDAR_COLOR+"'>";
    frameHTML += "<td colspan=7>";
    frameHTML += "<font onclick=parent.preYear()  title='��һ��' style='color:"+TC_NEXT_BOTTOM+";font:15px Webdings;cursor:hand'>9</font>";
    frameHTML += "<font onclick=parent.preMonth() title='��һ��' style='color:"+TC_NEXT_BOTTOM+";font:15px Webdings;cursor:hand'>3</font>";
    frameHTML += "<font title='����' onclick='parent.today()' style='color:black;font:15px webdings;cursor:hand'>1</font>";
    frameHTML += "���գ�" + _sysYear + "-" + (_sysMonth+1) + "-" + _sysDate;
    frameHTML += "<font onclick=parent.nextMonth() title='��һ��' style='color:"+TC_NEXT_BOTTOM+";font:15px webdings;cursor:hand'>4</font>";
    frameHTML += "<font onclick=parent.nextYear()  title='��һ��' style='color:"+TC_NEXT_BOTTOM+";font:15px Webdings;cursor:hand'>:</font>";
    frameHTML += "&nbsp;&nbsp;&nbsp;&nbsp;<span onclick=parent.setTimeControlVisible(false) style='font-size:12px;cursor: hand' Author="+author1+" title="+about+"><u>�ر�</u><font style='color:black;font:15px Webdings;'>r</font></span>";
    frameHTML += "&nbsp;</td></tr></table>";
    
    FrmTimeControl.document.body.style.backgroundColor = TC_BGCOLOR;
	FrmTimeControl.document.body.innerHTML = frameHTML;
	setTimeControlStyle();
	//ȷ���ؼ�������������Ա����ĺ��������ؼ����
	_objCalendar = FrmTimeControl.TabCalendar.children[0];
}
//================================================ WEB ҳ����ʾ���� =================================================//

//ȫ�ֱ���
//�꣬�£���
var _year, _month, _date;
//ϵͳʱ��
var _sysYear = new Date().getYear(), _sysMonth = new Date().getMonth(), _sysDate = new Date().getDate();
//��ŵ�ǰ��ݵ������µ�����
var _arrMonthDay = new Array();
//�ؼ��������(�ؼ�����ֵ����ʾ�����������)
var _outSrc;
//��Ŷ��������
var _outDate="";
//�Ƿ�ѡ��������
var _isSelected = false;
//�ؼ���������
var _objCalendar;

//�ؼ���ں���
function JSTimeControl(src) {
	//���������ж���Ϊ�˱��������������ȷ������ؼ���ʾ���� 2003-7-12 14:38	
	if (TC_WIDTH < MIN_TC_WIDTH)
		if (!confirm("���Ŀؼ���Ȳ���ֵ��С�����ܵ��¿ؼ�������ȫ��ʾ���Ƿ������"))
			return;
	if (TC_HEIGHT < MIN_TC_HEIGHT)
		if (!confirm("���Ŀؼ��߶ȶȲ���ֵ��С�����ܵ��¿ؼ�������ȫ��ʾ���Ƿ������"))
			return;

	_outSrc = src;

	//���ݵ�ǰ�ؼ���������ʾ����������
	var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/; 
	//���ؼ��ϵ�ֵƥ���������ʽ��ʽ������������
	var r = src.value.match(reg);
	// r ��Ϊ�յ������ʾʱ���ʽ����reg������ʽ
	if (r != null) {
		//��ȡ�·�
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

//ȡ�üӼ�������ʱ�� ��Σ�1. ����ǰ��ʱ�� 2. ƫ��ʱ��(��)
function getRelativeTime(inTime,offsetTime) {
	return new Date(inTime.valueOf() + offsetTime * 24 * 60 * 60 * 1000);
}

//��ʼ��ʱ��ؼ���ʹ��ȫ����ʾinDateʱ�䣬���inDateʱ��Ϊ�գ�����ʾϵͳʱ�䡣��window.onload()�¼�������ϴ˺���
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
	//ѡ�е���ɫ:����ɫ
	var selectColor = "#191970";
	//ѡ��״̬
	if (flag == 1) {
		curColor = srcObject.style.backgroundColor;
		srcObject.style.color = "#ffffff";
		srcObject.style.cursor = 'hand';
	  	srcObject.style.backgroundColor = selectColor;
	  	return;
	}
	//�뿪״̬
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
	//�����·�ѡ��div
	setDivMonthVisible(false);
}

//��ȡ�ؼ���������ҳ�ϵ�λ�á���Σ��������ƻ�ID��
//����ֵ:  getObjX() ����X����ֵ getObjY() ����Y����ֵ 
var objX = 0;	//�ؼ���X����
var objY = 0;	//�ؼ���Y����
function getObjPosition(obj){
  	objX = obj.offsetLeft;
  	objY = obj.offsetTop;
  	while (obj  = obj.offsetParent){
    	objX += obj.offsetLeft;
    	objY += obj.offsetTop;
    }
}

//����X����ֵ
function getObjX() {
	return this.objX;
}

//����Y����ֵ
function getObjY() {
	return this.objY;
}

//��λ�ؼ��������λ��
function setTimeControlPosition(src) {
	var objTC = document.all("FrmTimeControl");
	//ȡ�õ�ǰ�����λ��
	getObjPosition(src);
	var objX = getObjX();
	var objY = getObjY();
	
	//������ʾƫ�Ƹ߶�
	var tcTopDown = objY + src.clientHeight + 6;
	//������ʾƫ�Ƹ߶�
	var tcTopUp   = objY - TC_HEIGHT;
	
	//���ؼ���ҳ���Ҳ����ҿؼ����С��ҳ��������ʱ���ؼ����ұ߶�����ʾ����������߶�����ʾ 2003-8-6 10:43
	objTC.style.left = (objX + TC_WIDTH > document.body.offsetWidth && TC_WIDTH < document.body.offsetWidth) ? objX + src.offsetWidth - TC_WIDTH : objX;
	
	//���ؼ���ҳ��ײ����ҿؼ��߶�С��ҳ������߶�ʱ���ؼ�������ʾ������������ʾ
	objTC.style.top = ((tcTopDown + TC_HEIGHT) > document.body.offsetHeight && TC_HEIGHT < document.body.offsetHeight) ? tcTopUp : tcTopDown;
}

//��λ�·�ѡ��div�����λ��
function setDivMonthPosition() {
	var objTC = document.all("FrmTimeControl");
	var divTop = FrmTimeControl.event.clientY + objTC.offsetTop;
	//��ǰҳ��߶�
	var pageHeight = document.body.offsetHeight;
    
	DivMonth.style.left = FrmTimeControl.event.clientX + objTC.offsetLeft;
	DivMonth.style.top = divTop;
}

function selectMonth() {
	setDivMonthPosition();
	setDivMonthVisible(true);
}

//������ڿؼ�ͷ���ؼ���ݴ�
function focusTxtYear(src) {
	src.style.backgroundColor = "white";
	src.style.color = "blue";
}
//�ؼ�ͷ���ؼ���ݴ�ʧȥ����
function blurTxtYear(src) {
	src.style.backgroundColor = TC_HEAD_COLOR;
	src.style.color = "white";
}

//�ı�ؼ�ͷ���
function changeYear() {
	//���Ϸ��꣨��һ�꣩
	if (FrmTimeControl.window.event.keyCode == 38) {
		setCalenderValue(parseInt(_year)-1,_month,_date);
	}
	//���·��꣨��һ�꣩
	else if (FrmTimeControl.window.event.keyCode == 40) {
		setCalenderValue(parseInt(_year)+1,_month,_date);
	}
	//�����£���һ�£�2003-7-26 9:41
	else if (FrmTimeControl.window.event.keyCode == 39) {
		nextMonth();
	}
	//���ҷ��£���һ�£�2003-7-26 9:41
	else if (FrmTimeControl.window.event.keyCode == 37) {
		preMonth();
	}
	
	//ʹ�ð����¼�ʧЧ����ֹҳ����� 2003-7-26 9:41
	FrmTimeControl.window.event.returnValue = false;
}

//���ؼ�ͷ�������¸�ֵ
function setHead(inYear,inMonth) {
	FrmTimeControl.TxtYear.value  = inYear;
	FrmTimeControl.TxtMonth.value = inMonth + 1;
}

//����������
function cleanCalendar() {
	for (var i=0;i<6;i++) {
		for (var j=0;j<7;j++) {
			_objCalendar.children[i].children[j].style.backgroundColor = TC_CALENDAR_BGCOLOR;
			_objCalendar.children[i].children[j].style.color = TC_CALENDAR_COLOR;
		}
	}
	
}

//���ݴ���Ĳ����������������ֵ
function setCalenderValue(inYear,inMonth,inDate) {
	_year = inYear;
	if (_year > 9999 || _year < 1000)
		return;
	_month = inMonth;
	setHead(_year,_month);
	setMonthDay(inYear);
	_date = (inDate <= 0) ? 1 : inDate;
	_date = (inDate > _arrMonthDay[_month]) ? _arrMonthDay[_month] : inDate;
	//���µĵ�һ��
	var firstDay = new Date(inYear,inMonth,1).getDay();
	//����������
    cleanCalendar();
    //������µ�һ�첻�����գ���ôǰ�����ϸ��µĺ��첹�ϣ���ɫ��������
	if (firstDay != 0) {
		//��һ�������һ�������
		var lastMonthDate = _arrMonthDay[getLastMonth(inMonth)];
		//��������ɫ���������·����ڵ�����ɫ
		for (var i = 0; i < firstDay; i++) {
		    _objCalendar.children[0].children[i].innerText = lastMonthDate - firstDay + i + 1;
		    _objCalendar.children[0].children[i].style.color = TC_CALENDAR_COLOR_O;
		}
	}
	var isCurrentMonth = (_year == _sysYear && _month == _sysMonth) ? true : false;
	var dDate = 1,nextDate = 1;
	//����һ�еĵ�ǰ�·ݵ�����ֵ
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
				//��ǰϵͳʱ��������ɫ��ʾ
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

//����Ƶ������ϣ������ڱ�ɫ��ʾ
//����Ƴ������ϣ������ڱ��ԭ������ɫ
function changeCalendarStyle(src,cursor,color) {
	src.style.cursor = cursor;
	var isCurrentMonth = (_year == _sysYear && _month == _sysMonth) ? true : false;
	window.status = _date;
	//����ѡ�л���ϵͳʱ���ѡ�����ھͲ���ɫ
	if (!(src.innerText == _sysDate && isCurrentMonth) && src.innerText != parseInt(_date/1))
		src.style.backgroundColor = color;
}

//ѡ������
function selectDate(src) {
	_date = src.innerText;
	//ѡ�������·�
	if (src.style.color == TC_CALENDAR_COLOR_O) {
		//ѡ�������·ݵ�����,�ж��Ƿ����15��Ϊ������ѡ�е���һ�µ����ڻ�����һ�µ�����
		//��һ�µ����ڲ����������ڿؼ�����϶����ᳬ��15������ж����������ı������¿��ܲ����� 2003-8-2 16:40
		_month = (_date > 15) ? getLastMonth(_month) : getNextMonth(_month);
	}
	_isSelected = true;
	output();
	setTimeControlVisible(false);
}

//�ؼ����
function output() {
	if (_isSelected) 
		_outSrc.value = _year + "-" + changeNumber(_month+1) + "-" + changeNumber(_date);
	else 
	    _outSrc.value = _outDate.getFullYear() + "-" + changeNumber(_outDate.getMonth()+1) + "-" + changeNumber(_outDate.getDate());
	_isSelected = false;
}

//��һ��
function preYear() {
	setCalenderValue(parseInt(_year)-1,_month,_date);
	output();
}

//��һ��
function preMonth() {
	setLastMonth(_month);
	setCalenderValue(_year, _month, _date);
	output();
}

//��ݷ�ʽѡ�е�ǰϵͳʱ��
function today() {
	setCalenderValue(_sysYear,_sysMonth,_sysDate);
	_outSrc.value = _sysYear + "-" + changeNumber(_sysMonth + 1) + "-" + changeNumber(_sysDate);
	setTimeControlVisible(false);
	
}

//��һ�£��ؼ��ײ�����һ�·ݿ�ݷ�ʽ��
function nextMonth() {
	setNextMonth(_month);
	setCalenderValue(_year,_month,_date);
	output();
}

//��һ�꣨�ؼ��ײ�����һ��ݿ�ݷ�ʽ��
function nextYear() {
	setCalenderValue(parseInt(_year)+1,_month,_date);
	output();
}

//ȡ����һ��
function getLastMonth(month) {
	return (month == 0) ? 11 : month - 1;
}

//ȡ����һ��
function getNextMonth(month) {
	return (month == 11) ? 0 : month + 1;
}

//��ǰʱ���һ��
function setNextMonth(month) {
	_month = getNextMonth(month);
	_year = (_month == 0 && _year < 9999) ? parseInt(_year) + 1 : _year;
}

//��ǰʱ���һ��
function setLastMonth(month) {
	_month = getLastMonth(month);
	_year = (_month == 11 && _year >1000) ? parseInt(_year) - 1 : _year;
}

//С��10������ǰ�油��
function changeNumber(num) {
	return (num < 10) ? "0" + num : num;
}

function setTimeControlStyle() {
	var objTC = document.all("FrmTimeControl");
	objTC.style.position = "absolute";
	//TC_SHADOW_WIDTH��С��0ʱ�����ӿؼ���ӰЧ��
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

//�ж�ƽ���꣬��ÿ�µ��������鸳ֵ
function setMonthDay(selectYear) {
	if (((selectYear % 4 == 0) && (selectYear % 100 != 0)) || (selectYear % 400 == 0)) {
		_arrMonthDay = new Array(31,29,31,30,31,30,31,31,30,31,30,31);
	}
	else 
		_arrMonthDay = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
}

//������ʱ�رոÿؼ�
function document.onclick() {
	with (window.event.srcElement) {
		if (getAttribute("Author") != author2 && DivMonth.style.display == "inline")
		    setDivMonthVisible(false);
		if (getAttribute("Author") != author1 && getAttribute("Author") != author2 && tagName != "INPUT" && document.all("FrmTimeControl").style.display == "inline") {
    		setTimeControlVisible(false);
    	}
	}
}