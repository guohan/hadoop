//检验手机号码
function checkMobile(value)
{

	if (value > ""){
		var reg=/13[0,1,2,3,4,5,6,7,8,9]\d{8}/;
  	   	if (value.match(reg)== null){
			return false;
	   }
	}
    else
    {
		return false;
    }
	return true;
}



//2到10位数字，用于服务代码的校验
//add by liql
function servidalias(element_value){

	re = /^[\d]{2,10}$/;
	if(!re.test(element_value)){
		return false;
	}
	return true;
}
//@CheckItem@-liql-20041215-根据对象校验手机号码
function checkCMMobile(obj,name,notNull)
{
	if(obj==null||obj.value==null){
		alert("对象不存在！");
		return false;
	}
	if(notNull==true){
		if(obj.value==null||obj.value==""){
			alert("请输入"+name+"移动手机号码！");
			obj.focus();
			return false;
		}
	}

	if (obj.value > ""){
		var reg=/13[4,5,6,7,8,9]\d{8}/;
  	   	if (obj.value.match(reg)== null){
                       alert("请输入"+ name +"正确的移动手机号码！");
                       obj.focus();
		       return false;
	   }
	}

	return true;
}
function toChnDigit(num)
{
  var t = parseInt(num);
  if(t==0) return "零";
  if(t==1) return "一";
  if(t==2) return "二";
  if(t==3) return "三";
  if(t==4) return "四";
  if(t==5) return "五";
  if(t==6) return "六";
  if(t==7) return "七";
  if(t==8) return "八";
  if(t==9) return "九";
  return "";
}
//@CheckItem@ OPT-HuTie-20031208 优化：添加屏蔽所有按钮的公用函数

function disableAllButtons(){
	for(var i=0;i<document.all.tags("input").length;i++){
		var tmp = document.all.tags("input")[i];
		if(tmp.type=="button" || tmp.type=="submit" ||tmp.type=="reset"){
			tmp.disabled = true;
		}
	}
}


//增加trim函数
String.prototype.trim = function()
{
return this.replace(/(^\s*)|(\s*$)/g, "");
}


//函数名：checkNUM
//功能介绍：检查是否为数字
//参数说明：要检查的数字
//返回值：1为是数字，0为不是数字
function checkNum(Num) {
	var i,j,strTemp;
	strTemp = "0123456789.";
	if ( Num.length == 0)
		return 0
	for (i = 0;i < Num.length; i++) {
		j = strTemp.indexOf(Num.charAt(i));
		if (j == -1) {
			//说明有字符不是数字
			return 0;
		}
	}
	//说明是数字
	return 1;
}


//函数名：checkNUM
//功能介绍：检查是否为数字
//参数说明：要检查的数字
//返回值：1为是数字，0为不是数字
function checkIntNum(Num) {
	var i,j,strTemp;
	strTemp = "0123456789";
	if ( Num.length == 0)
		return 0
	for (i = 0;i < Num.length; i++) {
		j = strTemp.indexOf(Num.charAt(i));
		if (j == -1) {
			//说明有字符不是数字
			return 0;
		}
	}
	//说明是数字
	return 1;
}


//函数名：checkEmail
//功能介绍：检查是否为Email Address
//参数说明：要检查的字符串
//返回值：0：不是 1：是
/**@CheckItem@ zhou_jx-20041231 校正对'.'的判断
（判断的规则是不能以'@'或'.'开头，'@'后面至少要有三个字符，不能以'.'结束）
*/
//ADD BY LIQL 与SP资料添加的一致
function checkEmail(a){

		re = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
		if (!re.test(a)){

			return 0;
			}
		return 1;
	}

/*function checkEmail(a) {
	var i=a.length;
	var temp = a.indexOf('@');
	var tempd = a.indexOf('.');
	if (temp >= 1 && tempd >= 5) {
		if ((i-temp) > 3){
			if ((i-tempd)>1){
				return 1;
			}
		}
	}
	return 0;
}*/

//函数名：checkTEL
//功能介绍：检查是否为电话号码
//参数说明：要检查的字符串
//返回值：1为是合法，0为不合法
function checkTel(tel)
{
	var i,j,strTemp;
	strTemp = "0123456789-－";
	for (i=0;i<tel.length;i++)
	{
		j = strTemp.indexOf(tel.charAt(i));
		if (j==-1)
		{
			//说明有字符不合法
			return 0;
		}
	}
	//说明合法
	return 1;
}

//函数名：checkLength
//功能介绍：检查字符串的长度
//参数说明：要检查的字符串
//返回值：长度值
function checkLength(strTemp)
{
	var i,sum;
	sum = 0;
	for(i=0;i<strTemp.length;i++)
	{
//@CheckItem@ BUG-Renhj-20040604 优化：将验证的函数改成128以类的为单字符。避免“·”符号
//		if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=255))
		if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=128))
			sum = sum + 1;
		else
			sum = sum + 2;
	}
	return sum;
}

//函数名：checkSafe
//功能介绍：检查是否含有"'", '"',"<", ">"
//参数说明：要检查的字符串
//返回值：0：是 1：不是
function checkSafe(a)
{
	//fibdn = new Array ("'" ,'"',">", "<", "、", ",", ";");
	fibdn = new Array ("'" ,'"',">", "<");
	i = fibdn.length;
	j = a.length;
	for (ii=0;ii<i;ii++)
	{
		for (jj=0;jj<j;jj++)
		{
			temp1 = a.charAt(jj);
			temp2 = fibdn[ii];
			if (temp1==temp2)
			{
				return 0;
			}
		}
	}
	return 1;
}

//函数名：checkChar
//功能介绍：检查是否含有非字母字符
//参数说明：要检查的字符串
//返回值：0：含有 1：全部为字母
function checkChar(str)
{
	var strSource ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.()& ";
	var ch;
	var i;
	var temp;

	for (i=0;i<=(str.length-1);i++)
	{
		ch = str.charAt(i);
		temp = strSource.indexOf(ch);
		if (temp==-1)
		{
			return 0;
		}
	}
	return 1;
}
/*
* 检查只有数字和26个字母
//返回值：0：含有 1：全部为数字或字母
*/
function checkCharOrNum(str)
{
	var strSource = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	var ch;
	var i;
	var temp;

	for (i = 0;i<=(str.length-1);i++)
	{

		ch = str.charAt(i);
		temp = strSource.indexOf(ch);
		if (temp == -1)
		{
			return 0;
		}
	}
	return 1;
}
/*
* 检查业务代码
//返回值：0：不正确 1：正确
*/
function checkIcpServId(str)
{
	var strSource = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-";
	var ch;
	var i;
	var temp;

	for (i = 0;i<=(str.length-1);i++)
	{

		ch = str.charAt(i);
		temp = strSource.indexOf(ch);
		if (temp == -1)
		{
			return 0;
		}
	}
	return 1;
}
//函数名：checkCharOrDigital
//功能介绍：检查是否含有非数字或字母
//参数说明：要检查的字符串
//返回值：0：含有 1：全部为数字或字母
function checkCharOrDigital(str)
{
	var strSource = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.()& ";
	var ch;
	var i;
	var temp;

	for (i = 0;i<=(str.length-1);i++)
	{

		ch = str.charAt(i);
		temp = strSource.indexOf(ch);
		if (temp == -1)
		{
			return 0;
		}
	}
	return 1;
}

//函数名：checkChinese
//功能介绍：检查是否含有汉字
//参数说明：要检查的字符串
//返回值：0：含有 1：没有
function checkChinese(strTemp)
{
	var i,sum;
	for(i=0;i<strTemp.length;i++)
	{
		if ((strTemp.charCodeAt(i)<0) || (strTemp.charCodeAt(i)>255))
	      return 0;
	}
	return 1;
}

//函数名：compareTime()
//功能介绍： 比较时间大小
//参数说明：beginYear开始年，beginMonth开始月,benginDay开始日,beginH开始小时，beginM开始分钟，
//          endYear结束年，endMonth结束月，endMonth结束日,endH结束小时，endM结束分钟
//返回值：true 表示 开始时间大于结束时间，false 相反
//@CheckItem@ SELBUG-Tanyi-20030707 修改时间校验
function compareTime(beginYear,beginMonth,benginDay,beginH,beginM,endYear,endMonth,endDay,endH,endM){
  var date1 = new Date(beginYear,beginMonth-1,benginDay,beginH,beginM);
  var date2 = new Date(endYear,endMonth-1,endDay,endH,endM);
  if(date1.getTime()>=date2.getTime()){
    return false;
  }
  return true;
}

//函数名：compareDate()
//功能介绍： 比较日期大小
//参数说明：beginYear开始年，beginMonth开始月,benginDay开始日
//          endYear结束年，endMonth结束月，endMonth结束日
//返回值：0：true 表示 开始时间大于结束时间，false 相反
//@CheckItem@ BUG320-TANYI-20040304 修改日期比较方法，将CS和PS统一
//@CheckItem@ BUG320-renhj-20040310 修改日期比较方法，将CS和PS统一
function compareDate(beginYear,beginMonth,benginDay,endYear,endMonth,endDay){
  var date1 = new Date(beginYear,beginMonth-1,benginDay);
  var date2 = new Date(endYear,endMonth-1,endDay);
  if(date1.getTime()>=date2.getTime()){
    return false;
  }
  return true;
}
function compareDate2(beginYear,beginMonth,benginDay,endYear,endMonth,endDay){
  var date1 = new Date(beginYear,beginMonth-1,benginDay);
  var date2 = new Date(endYear,endMonth-1,endDay);
  if(date1.getTime()>date2.getTime()){
    return false;
  }
  return true;
}
//函数名：checkURL
//功能介绍：检查Url是否合法
//参数说明：要检查的字符串
//返回值：true：合法 false：不合法。
function checkURL(strTemp)
{
	if(strTemp.length==0) return false;
	if(checkChinese(strTemp)==0) return false;
	if (strTemp.toUpperCase().indexOf("HTTP://") != 0 && strTemp.toUpperCase().indexOf("HTTPS://") != 0){
		return false;
	}
	return true;
}


// @CheckItem@ OPT-Renhj-20030704 提供公共的去处空格的方法
//清除左边空格
function js_ltrim(deststr)
{
	if(deststr==null)return "";
	var pos=0;
	var retStr=new String(deststr);
	if (retStr.lenght==0) return retStr;
	while (retStr.substring(pos,pos+1)==" ") pos++;
	retStr=retStr.substring(pos);
	return(retStr);
}
//清除右边空格
function js_rtrim(deststr)
{
	if(deststr==null)return "";
	var retStr=new String(deststr);
	var pos=retStr.length;
	if (pos==0) return retStr;
	while (pos && retStr.substring(pos-1,pos)==" " ) pos--;
	retStr=retStr.substring(0,pos);
	return(retStr);
}
//清除左边和右边空格
function js_trim(deststr)
{
	if(deststr==null)return "".trim();
	var retStr=new String(deststr);
	var pos=retStr.length;
	if (pos==0) return retStr.trim();
	retStr=js_ltrim(retStr);
	retStr=js_rtrim(retStr);
	return retStr.trim();
}
//格式化输入的日期串，输入的如："2003-9-12" 输出："2003-09-12"
function formatDateStr(inDate){
  if (inDate==null||inDate=="") return "";
  var beginDate = inDate.split("-");
  var mYear=beginDate[0];
  var mMonth=beginDate[1];
  var mDay=beginDate[2];
  mMonth=((mMonth.length==1)?("0"+mMonth):mMonth);
  mDay=((mDay.length==1)?("0"+mDay):mDay);
  return mYear+"-"+mMonth+"-"+mDay;
  }

//obj:数据对象
//dispStr :失败提示内容显示字符串
function checkUrlValid( obj,  dispStr)
{
	if(obj  == null)
	{
		alert("传入对象为空");
		return false;
	}
	var str = obj.value;

	var  urlpatern0 = /^https?:\/\/.+$/i;
	if(!urlpatern0.test(str))
	{
		alert(dispStr+"不合法：必须以'http:\/\/'或'https:\/\/'开头!");
		obj.focus();
		return false;
	}

	var  urlpatern2= /^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?.+$/i;
	if(!urlpatern2.test(str))
	{
		alert(dispStr+"端口号必须为数字且应在1－65535之间!");
		obj.focus();
		return false;
	}


	var	urlpatern1 =/^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?(\/((\.)?(\?)?=?&?[a-zA-Z0-9_-](\?)?)*)*$/i;

	if(!urlpatern1.test(str))
	{
		alert(dispStr+"不合法,请检查!");
		obj.focus();
		return false;
	}

	var s = "0";
	var t =0;
        var re = new RegExp(":\\d+","ig");
	while((arr = re.exec(str))!=null)
	{

		s = str.substring(RegExp.index+1,RegExp.lastIndex);

		if(s.substring(0,1)=="0")
		{
			alert(dispStr+"端口号不能以0开头!");
			obj.focus();
			return false;
		}

		t = parseInt(s);
		if(t<1 || t >65535)
		{
			alert(dispStr+"端口号必须为数字且应在1－65535之间!");
			obj.focus();
			return false;
		}


	}
	return true;
}

//函数名：checkVisibleEnglishChr
//功能介绍：检查是否为可显示英文字符(  !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~)
//参数说明：要检查的字符串
//返回值：true|false
//add by renhj 2004.01.05
function checkVisibleEnglishChr(strTemp)
{
	var i;
	for(i=0;i<strTemp.length;i++)
	{
		if ((strTemp.charCodeAt(i)<32) || (strTemp.charCodeAt(i)>126))
			return false;
	}
	return true;
}
//函数名：checkareaInput
//功能介绍：检查是否含有非textarea字符
//参数说明：要检查的字符串
//返回值：false：含有 true：全部为可textarea字符
//add by liql 2005.01.05
function checkareaInput(str)
{
	var notinput = "<>";
    var i;
    for (i = 0; notinput != null && i < notinput.length; i++) {
        if (str.indexOf(notinput.charAt(i)) >= 0) {//若有
          return false;
        }
      }
    return true;

}
//函数名：checkInputChr
//功能介绍：检查是否含有非Input字符
//参数说明：要检查的字符串
//返回值：false：含有 true：全部为可Input字符
//add by renhj 2004.01.05
function checkInputChr(str)
{
	var notinput = "\"'<>@#$%^&*()";
    var i;
    for (i = 0; notinput != null && i < notinput.length; i++) {
        if (str.indexOf(notinput.charAt(i)) >= 0) {//若有
          return false;
        }
      }
    return true;

}
//函数名：checktextareaInput
//功能介绍：检查是否含有非Input字符<,>
//参数说明：要检查的字符串
//返回值：false：含有 true：全部为可Input字符
//add by liql 2004.12.31
function checktextareaInput(str)
{
	var notareainput = "<>";
    var i;
    for (i = 0; notareainput != null && i < notareainput.length; i++) {
        if (str.indexOf(notareainput.charAt(i)) >= 0) {//若有
          return false;
        }
      }
    return true;

}
//@CheckItem@ BUG550-liujun-20040528 拷贝cs的checkForm.js中的checkFormdate函数
//函数名：checkFormdata
//功能介绍：检查Form对象
//参数说明：
//obj：要检查的对象，
//name：要检查的对象的中文名称，
//length：检查的对象的长度（<0不检查），
//notnull:为true则检查非空，
//notSpecChar:为true则检查有无特殊字符，
//notChinessChar:为true则检查有无中文字符，
//numOrLetter:为true则检查只能为数字或英文字母，
//pNumber:为true则检查只能为正整数，
//返回值：false：检查不通过 true：全部为可Input字符
//add by renhj 2004.03.19
//@CheckItem@ BUG:1641:718-Renhj-20040902-Add5 修改校验数字的信息
function checkFormdata(obj,name,length,notnull,notSpecChar,notChinessChar,numOrLetter,pNumber){
	//检查对象
	if (!obj) {alert("目标不是对象，处理失败!");return false;}
	var msg;
	var ilen;
	//检测汉字
        if (notChinessChar&&(checkChinese(obj.value) != 1)){
           msg=name+"不能包含汉字！";
           alert(msg);
           obj.focus();
           return false;
          }
        //检测特殊字符
         if(notSpecChar){
        	if(obj.type=="textarea"){
       			 if(!checktextareaInput(obj.value)){
	     			 var noinput = " < > ";
          			msg=name+"有非法字符（"+noinput+"）！";
          			alert(msg);
          			obj.focus();
          			return false;
        			}

		}
		else{
        		if(!checkInputChr(obj.value)){
	     		 var notinput = " \" ' < >";
          		msg=name+"有非法字符（"+notinput+"）！";
          		alert(msg);
        		  obj.focus();
         		 return false;
        		}

      		  }
	}
        //检测长度
        if(length>=0&&(checkLength(obj.value)>length)){
          ilen=length/2;
          if(pNumber){
              msg=name+"不能超过"+length+"个数字！";
          }else if(notChinessChar){
              msg=name+"不能超过"+length+"个英文！";
          }else{
              msg=name+"不能超过"+length+"个英文或"+ilen+"个汉字！";
          }
          alert(msg);
          obj.focus();
          return false;
        }
        //检测非空
	if(notnull&&obj.value.trim()==""){
            msg="请输入"+name+"！";
            alert(msg);
	    obj.focus();
	    return false;
	}
        //检测只能为数字或英文字母
        re = /[\W_]/;
        if (numOrLetter&&re.exec(obj.value)) {
            msg=name+"只能为数字或英文字母！";
            alert(msg);
	    obj.focus();
            return false;
        }
        //检测只能为只能为正整数
        re = /[\D_]/;
        if (pNumber&&re.exec(obj.value)) {
            msg=name+"只能为正整数！";
            alert(msg);
	    obj.focus();
            return false;
        }

	return true;

}
/**
 * *********************************************************
 * *******以下函数为 sp分组， 信息公告 ，会议通知和销号信息 专用***
 ***********************************************************
 ************by liuxiaogang 2005-03-03 *********************
 */


 //清除左边关键字符
function js_ltrim_key(deststr,key_char)
{
	if(deststr==null)return "";
	var pos=0;
	var retStr=new String(deststr);
	if (retStr.lenght==0) return retStr;
	while (retStr.substring(pos,pos+1)==key_char) pos++;
	retStr=retStr.substring(pos);
	return(retStr);
}
//清除左边关键字符
function js_rtrim_key(deststr,key_char)
{
	if(deststr==null)return "";
	var retStr=new String(deststr);
	var pos=retStr.length;
	if (pos==0) return retStr;
	while (pos && retStr.substring(pos-1,pos)==key_char ) pos--;
	retStr=retStr.substring(0,pos);
	return(retStr);
}
//清除左边和右边关键字符
function js_trim_key(deststr,key_char)
{
	if(deststr==null)return "".trim();
	var retStr=new String(deststr);
	var pos=retStr.length;
	if (pos==0) return retStr.trim();
	retStr=js_ltrim_key(retStr,key_char);
	retStr=js_rtrim_key(retStr,key_char);
	return retStr.trim();
}

function checkLength2(strTemp,key_char)
{
	var sum = 0;
        var c ='';
	for(var i=0;i<strTemp.length;i++)
	{
          c = strTemp.charAt(i);

          if(c==key_char)
          {
            sum = sum + 1;
          }

        }
        	return sum;
}

function  clearInitText(obj)
{

   if(obj.value=='输入企业代码,以逗号隔开'||obj.value=='输入SP简称,以逗号隔开'||obj.value=='输入SP服务代码,以逗号隔开')
   {
     obj.value="";
   }
}


 //字符串变字符串数据，除掉关键字符key_char
function stringToArray(src,key_char)
    {
      var source =js_trim_key(src,key_char);
      if(source != null && source !="")
      {
        var num = checkLength2(source,key_char);
        var srcArray = new Array(num+1);
        for(var i=0;i<num+1;i++)
        {
          srcArray[i]=stringToArray2(source,key_char);
          source = source.substring(srcArray[i].length,source.length);
          source = js_trim_key(source,key_char);
        }
        return srcArray;
      }
      return "";

    }

function  stringToArray2(src,key_char)
    {
       var s =src.indexOf(key_char);
       if(s> -1)return src.substring(0,s);
       else return src;

    }

/**
 * 可供 企业代码输入框，sp简称，服务代码输入框 调用
 *对sp列表左右布局进行处理
 *call method:
 * btnCheck_onClick(this.form,this,"企业代码");
 *
 */
function btnCheck_onClick(form,value,type){

        var tmp1 = "," + value + ",";
        var strAarry = stringToArray(tmp1,",");
        var errorList="";
       for(var j=0 ; j<strAarry.length ;j++)
       {
         var error="0"; //不存在标志
         if(strAarry[j]== null || strAarry[j]=="")
         {
           continue;
         }
	for(var i=0;i<form.UnSelectICP.length;i++){
            var text = form.UnSelectICP.options[i].text;
            var value = form.UnSelectICP.options[i].value;
            var index1 = text.indexOf(",");
            var index2 = text.lastIndexOf(",");

            var destValue="";
            if(type=="企业代码")
            {
              destValue = text.substring(index1+1,index2);
            }
            if(type=="SP简称")
            {
             destValue = text.substring((index2+1),text.length);
            }
            if(type=="服务代码")
            {
              destValue = text.substring(0,index1);
            }
//            alert("destValue:"+destValue+":"+"strAarry[j]:"+strAarry[j]+"");
            var objSelected = new Option(text,value);
            if(destValue==strAarry[j])
            {
               error="1";
              	form.SelectICP.options[form.SelectICP.length] = objSelected;
               form.SelectICP.options[form.SelectICP.length-1].selected = true;
		form.UnSelectICP.options[i] = null;

            }

	 }
         if(error ==0)
         {
           errorList =errorList+","+strAarry[j]+"";
         }

       }
       if(errorList !="")
       {
         errorList =errorList.substring(1,errorList.length);
         alert("提示:"+type+"为"+errorList+" 的SP可能不存在或者已经在已选择SP列表中!");
       }

    }
 
 //验证图片格式
 function checkFormat4Pic(ss,strPre)
{
      
     var s =trim(ss);
		var str=s.toLowerCase();
		if(!((str=="gif")||(str=="jpg")||(str=="png")))
		{
			alert(strPre + "格式必须是gif或jpg或png");
			return false;
		}
		return true ;
}

//按指定大小显示图片
function load_img4Size(picturePreview,write_id,v_width,v_height){
	var t_html;
	if(picturePreview!=''){
		t_html="<img src='"+picturePreview+
			"' onLoad='javascript:if(this.width>"+v_width+"){this.width="+v_width+";}if(this.height>"+v_height+"){this.height="+v_height+";}'>";
	}else{
		t_html="";
	}
	write_id.innerHTML=t_html;
}

function load_img2(picturePreview,write_id,OldPicName,v_width,v_height){

	//将原始的图片隐藏
	document.getElementById(OldPicName).style.display="none";
	
	var t_html;
	if(picturePreview!=''){
		t_html="<img src='"+picturePreview+
			"' onLoad='javascript:if(this.width>"+v_width+"){this.width="+v_width+";}if(this.height>"+v_height+"){this.height="+v_height+";}'>";
	}else{
		t_html="";
	}
	write_id.innerHTML=t_html;
}
/**
 * ****************************END!*************************
 */
