isIE = (document.all ? true : false);
var ckt = null; //Added by WangHui 2003-10-09 用于时间精确至月
function getIEPosX(elt) { return getIEPos(elt,"Left"); }
function getIEPosY(elt) { return getIEPos(elt,"Top"); }
function getIEPos(elt,which) {
 iPos = 0
 while (elt!=null) {
  iPos += elt["offset" + which]
  elt = elt.offsetParent
 }
 return iPos
}

function getXBrowserRef(eltname) {
 return (isIE ? document.all[eltname].style : document.layers[eltname]);
}

function hideElement(eltname) { getXBrowserRef(eltname).visibility = 'hidden';
	}

// 按不同的浏览器进行处理元件的位置
function moveBy(elt,deltaX,deltaY) {
 if (isIE) {
  elt.left = elt.pixelLeft + deltaX;
  elt.top = elt.pixelTop + deltaY;
 } else {
  elt.left += deltaX;
  elt.top += deltaY;
 }
}

function toggleVisible(eltname) {
 elt = getXBrowserRef(eltname);
 if (elt.visibility == 'visible' || elt.visibility == 'show') {
   elt.visibility = 'hidden';
 } else {
   fixPosition(eltname);
   elt.visibility = 'visible';
 }

}

function setPosition(elt,positionername,isPlacedUnder) {
 positioner = null;
 daysGrid='';
 if (isIE) {
  positioner = document.all[positionername];
  elt.left = getIEPosX(positioner);
  elt.top = getIEPosY(positioner);
 } else {
  positioner = document.images[positionername];
  elt.left = positioner.x;
  elt.top = positioner.y;
 }
 if (isPlacedUnder) { moveBy(elt,0,positioner.height); }
}




         // 判断浏览器        isIE = (document.all ? true : false);

         // 初始月份及各月份天数数组
     var months = new Array("一月", "二月", "三月", "四月", "五月", "六月", "七月",
     "八月", "九月", "十月", "十一月", "十二月");
     var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31,
            30, 31, 30, 31);
     var displayMonth = new Date().getMonth();
     var displayYear = new Date().getFullYear();
     var displayDivName;
     var displayElement;

         function getDays(month, year) {
            //测试选择的年份是否是润年？
            if (1 == month)
               return ((0 == year % 4) && (0 != (year % 100))) ||
                  (0 == year % 400) ? 29 : 28;
            else
               return daysInMonth[month];
         }

         function getToday() {
            // 得到今天的日期
            this.now = new Date();
            this.year = this.now.getFullYear();
            this.month = this.now.getMonth();
            this.day = this.now.getDate();
         }

         // 并显示今天这个月份的日历
         today = new getToday();

         function newCalendar(eltName,attachedElement) {
        if (attachedElement) {
           if (displayDivName && displayDivName != eltName) hideElement(displayDivName);
           displayElement = attachedElement;
        }
        displayDivName = eltName;
            today = new getToday();
            var parseYear = parseInt(displayYear + '');
            var newCal = new Date(parseYear,displayMonth,1);
            var day = -1;
            var startDayOfWeek = newCal.getDay();
            if ((today.year == newCal.getFullYear()) &&
                  (today.month == newCal.getMonth()))
        {
               day = today.day;
            }
            var intDaysInMonth =
               getDays(newCal.getMonth(), newCal.getFullYear());
               
 //Edited by WangHui 2003-10-09 如果checktype为month,则只精确到月,intDaysInMonth为1,反之显示该月所有天数.            
           if(attachedElement != null && attachedElement.checktype == "month"){
           	ckt = attachedElement;
           	var daysGrid = makeDaysGrid(startDayOfWeek,day,1,newCal,eltName)
           }
	   else{
	   	ckt = null;
	        var daysGrid = makeDaysGrid(startDayOfWeek,day,intDaysInMonth,newCal,eltName)
	   }   
       
        
        if (isIE) {
           var elt = document.all[eltName];
          var b="<iframe name='" + elt.id + "_tabcalerdar' width=142 height=142 frameborder=0 marginheight=0 marginwidth=0 hspace=0 vspace=0 scrolling=no ></iframe>";
		   //window.document.write(b)
		  //elt.document.write(b);
		   elt.innerHTML = b;
			eval(elt.id + "_tabcalerdar.document.write(daysGrid)");

			//tabcalerdar.document.write(daysGrid);

		   //elt.innerHTML = daysGrid;
        } else {
           var elt = document.layers[eltName].document;
           elt.open();
           elt.write(daysGrid);
           
		   elt.close();
        }
     }

     function incMonth(delta,eltName) {
       displayMonth += delta;
       if (displayMonth >= 12) {
         displayMonth = 0;
         incYear(1,eltName);
       } else if (displayMonth <= -1) {
         displayMonth = 11;
         incYear(-1,eltName);
       } else {
         newCalendar(eltName,ckt);
       }
     }

     function incYear(delta,eltName) {
       displayYear = parseInt(displayYear + '') + delta;
       newCalendar(eltName,ckt);
     }

     function makeDaysGrid(startDay,day,intDaysInMonth,newCal,eltName) {
        var daysGrid;
        var month = newCal.getMonth();
        var year = newCal.getFullYear();
        var isThisYear = (year == new Date().getFullYear());
        var isThisMonth = (day > -1)
		daysGrid = '<table align=center cellSpacing=0 cellPadding=0 width=140 border=0 height=140 style="border:1px solid #dddddd; font:宋体;font-size:9pt "><tr bgcolor=#cccccc height=20><td align=center>';
        //daysGrid += '<font face="courier new, courier" size=2>';
        daysGrid += '<a href="javascript:parent.hideElement(\'' + eltName + '\')">x</a></td>';
        //daysGrid += '&nbsp;&nbsp;';
        
        daysGrid += '<td colspan=3 align=left><a href="javascript:parent.incMonth(-1,\'' + eltName + '\')">&laquo;</a>&nbsp;';

        daysGrid += '';
        if (isThisMonth) { daysGrid += '<font color=red>' + months[month] + '</font>'; }
        else { daysGrid += months[month]; }
        //daysGrid += '</td>';

        daysGrid += '&nbsp;<a href="javascript:parent.incMonth(1,\'' + eltName + '\')">&raquo;</a></td>';
        //daysGrid += '&nbsp;&nbsp;&nbsp;';
        daysGrid += '<td colspan=3 align=right><a href="javascript:parent.incYear(-1,\'' + eltName + '\')">&laquo; </a>&nbsp;';

        daysGrid += '';
        if (isThisYear) { daysGrid += '<font color=red>' + year + '</font>'; }
        else { daysGrid += ''+year; }
        daysGrid += '&nbsp;';

        daysGrid += '<a href="javascript:parent.incYear(1,\'' + eltName + '\')"> &raquo;</a></td></tr>';
        daysGrid += '<tr height=20><td width=20 align=center>日</td><td width=20 align=center>一</td><td width=20 align=center>二</td><td width=20 align=center>三</td><td width=20 align=center>四</td><td width=20 align=center>五</td><td width=20 align=center>六</td></tr>';
        var dayOfMonthOfFirstSunday = (7 - startDay + 1);
        for (var intWeek = 0; intWeek < 6; intWeek++) {
           var dayOfMonth;
            daysGrid += '<tr height=20>';
        
	
           for (var intDay = 0; intDay < 7; intDay++) {
           	
             dayOfMonth = (intWeek * 7) + intDay + dayOfMonthOfFirstSunday - 7;
         if (dayOfMonth <= 0) {
               daysGrid += "<td>&nbsp;</td> ";
         } else if (dayOfMonth <= intDaysInMonth) {
           var color = "blue";
           if (day > 0 && day == dayOfMonth) color="red";
           daysGrid += '<td align=center><a href="javascript:parent.setDay(';
           daysGrid += dayOfMonth + ',\'' + eltName + '\')" '
           daysGrid += 'style="color:' + color + '">';
           var dayString = dayOfMonth + "</a></td> ";
           if (dayString.length == 6) dayString = '0' + dayString;
           daysGrid += dayString ;
         }
           }
           if (dayOfMonth < intDaysInMonth) daysGrid += "</tr>";
        }
       //alert(daysGrid);
        return daysGrid + "</tr></table>";
       
             //daysGrid +='</td></tr></table>';
			 //d='<table><tr><td>aaa</td></tr></table>'
			 //daysGrid1 ='<iframe frameborder=0 marginheight=0 marginwidth=0 hspace=0 vspace=0 scrolling=no this.innerHTML='+daysGrid+'></iframe>';
			
			//return daysGrid1;
}

     function setDay(day,eltName) {
     //  displayElement.value = (displayMonth + 1) + "/" + day + "/" + displayYear;
              displayElement.value = displayYear+"-"+(displayMonth + 1) + "-" + day  ;

       hideElement(eltName);
     }


//——————————————————————————————————————


// fixPosition() 这个函数和前面所讲的那个函数一样//
function fixPosition(eltname) {
 elt = getXBrowserRef(eltname);
 positionerImgName = eltname + 'Pos';
 // hint: try setting isPlacedUnder to false
 isPlacedUnder = false;
 if (isPlacedUnder) {
  setPosition(elt,positionerImgName,true);
 } else {
  setPosition(elt,positionerImgName)
 }
}

function toggleDatePicker(eltName,formElt) {
  var x = formElt.indexOf('.');
  var formName = formElt.substring(0,x);
  var formEltName = formElt.substring(x+1);
  newCalendar(eltName,document.forms[formName].elements[formEltName]);
  toggleVisible(eltName);
}

function MoveUp(SelectList)
	{
	var nIndex = SelectList.selectedIndex;
	if (nIndex == -1)
		{
		alert("请先选择一项！");
		return;
		}

	if (nIndex == 0)
		{
		return;
		}

	var objSelected = new Option(SelectList[nIndex].text, SelectList[nIndex].value);
	var objPrevious = new Option(SelectList[nIndex-1].text, SelectList[nIndex-1].value);
	SelectList.options[nIndex] = objPrevious;
	SelectList.options[nIndex-1] = objSelected;

	SelectList.options[nIndex-1].selected = true;
	}

function MoveDown(SelectList)
	{
	var nIndex = SelectList.selectedIndex;
	if (nIndex == -1)
		{
		alert("请先选择一项！");
		return;
		}

	if (nIndex == SelectList.options.length-1)
		{
		return;
		}

	var objSelected = new Option(SelectList[nIndex].text, SelectList[nIndex].value);
	var objPrevious = new Option(SelectList[nIndex+1].text, SelectList[nIndex+1].value);
	SelectList.options[nIndex] = objPrevious;
	SelectList.options[nIndex+1] = objSelected;

	SelectList.options[nIndex+1].selected = true;
	}

function Move(SourceList,TargetList)
	{
	var nIndex = SourceList.selectedIndex;
	if (nIndex == -1)
		{
		alert("请先选择一项！");
		return;
		}

	var objSelected = new Option(SourceList[nIndex].text, SourceList[nIndex].value);
	TargetList.options[TargetList.length] = objSelected;
	SourceList.options[nIndex] = null;

	TargetList.options[TargetList.length-1].selected = true;
	if(navigator.appName=="Netscape")
		{
		history.go(0)
		}
	}

function MoveAll(SourceList,TargetList)
	{
	var nIndex = SourceList.selectedIndex;
	if (nIndex == -1){
		alert("请先选择一项！");
		return;
	}

	var len = SourceList.length;
	var start = 0;
	for( i=0;i<len;i++)
	{
		nIndex = SourceList.selectedIndex;
		if (nIndex == -1){
 		     return;
   	    }
		var objSelected = new Option(SourceList[nIndex].text, SourceList[nIndex].value);
		TargetList.options[TargetList.length] = objSelected;
		SourceList.options[nIndex] = null;
		TargetList.options[TargetList.length-1].selected = true;
	}

	if(navigator.appName=="Netscape")
		{
		history.go(0)
		}
	}

function SaveLayout(SelectList,strResult)
	{
	var result="0";
	for (i=0;i<SelectList.length;i++)
		{
		result = result+","+SelectList.options[i].value;
		}
	strResult.value = result;
	}
//用IE登录FTP服务器,added by tanyi at 2004-1-6
//打开FTP，用于上传销号文件。
function goFtpSite(ftp) {
	//ftp://userid:password@ftp
	var newwinOpen = window.open(ftp,"","scrollbars=yes, width=800,height=600");
}