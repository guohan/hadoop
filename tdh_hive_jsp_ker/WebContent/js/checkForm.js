//���
function ClearFormEelemnts(frmObj, contentCode)
{
  frmObj.reset();	  
  for (var i=0; i< frmObj.elements.length; i++)
  {
		var objTag = frmObj.elements(i);
		if((objTag.tagName.toLowerCase() == "input") || (objTag.tagName.toLowerCase() == "textarea"))
		{		
      if (objTag.type.toUpperCase()=="FILE")
			{
				objTag.fireEvent("onchange");
			}
		}
	}
}


//�����ֻ�����
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

//�����ƶ��ֻ�����
//modify by x_tangrc  ���Ӷ��ƶ�147�ºŶε�ʶ��
function validatePhone(value)
{
/*
   //���ֻ��Ϊֻ��Ϊ������
   //var reg=/13[5,6,7,8,9]\d{8}|134[0,1,2,3,4,5,6,7,8]\d{7}|15[8,9]\d{8}/;
   var reg= /[\D_]/;   
   if(!(reg.exec(value))&&value.length==11){
  	 return true;
   }else{
  	 return false;
   }*/
   
   //var reg=/1[3,5,8]\d{9}/;
   var reg=/1[3,5,8]\d{9}|147\d{8}/;
   if(reg.test(value)&&value.length==11){
    return true;
   }else{
  	 return false;
   }
}

//2��10λ���֣����ڷ�������У��
//add by liql
function servidalias(element_value){

	re = /^[\d]{2,10}$/;
	if(!re.test(element_value)){
		return false;
	}
	return true;
}
//@CheckItem@-liql-20041215-���ݶ���У���ֻ�����
function checkCMMobile(obj,name,notNull)
{
    if(obj==null||obj.value==null){
        alert("���󲻴��ڣ�");
        return false;
    }
    if(notNull==true){
        if(obj.value==null||obj.value==""){
            alert("������"+name+"�ƶ��ֻ����룡");
            obj.focus();
            return false;
        }
    }
    if (obj.value.length > 0){
        var reg=/^13[4,5,6,7,8,9]\d{8}$/;
        if (obj.value.match(reg)== null){
                 alert("������"+ name +"��ȷ���ƶ��ֻ����룡");
                 obj.focus();
            return false;
       }
    }
    return true;
}
function toChnDigit(num)
{
  var t = parseInt(num);
  if(t==0) return "��";
  if(t==1) return "һ";
  if(t==2) return "��";
  if(t==3) return "��";
  if(t==4) return "��";
  if(t==5) return "��";
  if(t==6) return "��";
  if(t==7) return "��";
  if(t==8) return "��";
  if(t==9) return "��";
  return "";
}
//@CheckItem@ OPT-HuTie-20031208 �Ż�������������а�ť�Ĺ��ú���

function disableAllButtons(){
	for(var i=0;i<document.all.tags("input").length;i++){
		var tmp = document.all.tags("input")[i];
		if(tmp.type=="button" || tmp.type=="submit" ||tmp.type=="reset"){
			tmp.disabled = true;
		}
	}
}


//����trim����
String.prototype.trim = function()
{
return this.replace(/(^\s*)|(\s*$)/g, "");
}

//��������checkNUM
//���ܽ��ܣ�����Ƿ�Ϊ����
//����˵����Ҫ��������
//����ֵ��1Ϊ�����֣�0Ϊ��������
function checkNum(Num) {
	var i,j,strTemp;
	strTemp = "0123456789.";
	if ( Num.length == 0)
		return 0
	for (i = 0;i < Num.length; i++) {
		j = strTemp.indexOf(Num.charAt(i));
		if (j == -1) {
			//˵�����ַ���������
			return 0;
		}
	}
	//˵��������
	return 1;
}


//��������checkNUM
//���ܽ��ܣ�����Ƿ�Ϊ����
//����˵����Ҫ��������
//����ֵ��1Ϊ�����֣�0Ϊ��������
function checkIntNum(Num) {
	var i,j,strTemp;
	strTemp = "0123456789";
	if ( Num.length == 0)
		return 0
	for (i = 0;i < Num.length; i++) {
		j = strTemp.indexOf(Num.charAt(i));
		if (j == -1) {
			//˵�����ַ���������
			return 0;
		}
	}
	//˵��������
	return 1;
}


//��������checkEmail
//���ܽ��ܣ�����Ƿ�ΪEmail Address
//����˵����Ҫ�����ַ���
//����ֵ��0������ 1����
/**@CheckItem@ zhou_jx-20041231 У����'.'���ж�
���жϵĹ����ǲ�����'@'��'.'��ͷ��'@'��������Ҫ�������ַ���������'.'������
*/
//ADD BY LIQL ��SP������ӵ�һ��
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

//��������checkTEL
//���ܽ��ܣ�����Ƿ�Ϊ�绰����
//����˵����Ҫ�����ַ���
//����ֵ��1Ϊ�ǺϷ���0Ϊ���Ϸ�
function checkTel(tel)
{
	var i,j,strTemp;
	strTemp = "0123456789-��";
	for (i=0;i<tel.length;i++)
	{
		j = strTemp.indexOf(tel.charAt(i));
		if (j==-1)
		{
			//˵�����ַ����Ϸ�
			return 0;
		}
	}
	//˵���Ϸ�
	return 1;
}

//��������checkLength
//���ܽ��ܣ�����ַ����ĳ���
//����˵����Ҫ�����ַ���
//����ֵ������ֵ
function checkLength(strTemp)
{
	var i,sum;
	sum = 0;
	for(i=0;i<strTemp.length;i++)
	{
//@CheckItem@ BUG-Renhj-20040604 �Ż�������֤�ĺ����ĳ�128�����Ϊ���ַ������⡰��������
//		if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=255))
		if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=128))
			sum = sum + 1;
		else
			sum = sum + 2;
	}
	return sum;
}

//��������checkSafe
//���ܽ��ܣ�����Ƿ���"'", '"',"<", ">"
//����˵����Ҫ�����ַ���
//����ֵ��0���� 1������
function checkSafe(a)
{
	//fibdn = new Array ("'" ,'"',">", "<", "��", ",", ";");
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

//��������checkChar
//���ܽ��ܣ�����Ƿ��з���ĸ�ַ�
//����˵����Ҫ�����ַ���
//����ֵ��0������ 1��ȫ��Ϊ��ĸ
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
* ���ֻ�����ֺ�26����ĸ
//����ֵ��0������ 1��ȫ��Ϊ���ֻ���ĸ
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
* ���ҵ�����
//����ֵ��0������ȷ 1����ȷ
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
/*
* ���Ƿ��ַ�
//����ֵ��0������ȷ 1����ȷ
*/
function checkFormChar(str)
{
	var strSource = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
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
//��������checkCharOrDigital
//���ܽ��ܣ�����Ƿ��з����ֻ���ĸ
//����˵����Ҫ�����ַ���
//����ֵ��0������ 1��ȫ��Ϊ���ֻ���ĸ
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

//��������checkPackageName
//���ܽ��ܣ�����Ƿ��з����ֻ���ĸ��С���㣬���ţ��»���
//����˵����strҪ�����ַ���
//����ֵ��0������ 1��ȫ��Ϊ���ֻ���ĸ��С���㣬���ţ��»���
function checkPackageName(str)
{
	var strSource = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-_";
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

//��������checkParenthesis
//���ܽ��ܣ�����Ƿ��а������
//����˵����strҪ�����ַ���
//����ֵ��0������������� 1�����а������
function checkParenthesis(str)
{
	if((str.indexOf('(')==-1)&&(str.indexOf(')')==-1)){
	return 0;
	}
	else{
	return 1;
	}
}

//��������checkChinese
//���ܽ��ܣ�����Ƿ��к���
//����˵����Ҫ�����ַ���
//����ֵ��0������ 1��û��
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

//��������compareTime()
//���ܽ��ܣ� �Ƚ�ʱ���С
//����˵����beginYear��ʼ�꣬beginMonth��ʼ��,benginDay��ʼ��,beginH��ʼСʱ��beginM��ʼ���ӣ�
//          endYear�����꣬endMonth�����£�endMonth������,endH����Сʱ��endM��������
//����ֵ��true ��ʾ ��ʼʱ����ڽ���ʱ�䣬false �෴
//@CheckItem@ SELBUG-Tanyi-20030707 �޸�ʱ��У��
function compareTime(beginYear,beginMonth,benginDay,beginH,beginM,endYear,endMonth,endDay,endH,endM){
  var date1 = new Date(beginYear,beginMonth-1,benginDay,beginH,beginM);
  var date2 = new Date(endYear,endMonth-1,endDay,endH,endM);
  if(date1.getTime()>=date2.getTime()){
    return false;
  }
  return true;
}

//��������compareDate()
//���ܽ��ܣ� �Ƚ����ڴ�С
//����˵����beginYear��ʼ�꣬beginMonth��ʼ��,benginDay��ʼ��
//          endYear�����꣬endMonth�����£�endMonth������
//����ֵ��0��true ��ʾ ��ʼʱ����ڽ���ʱ�䣬false �෴
//@CheckItem@ BUG320-TANYI-20040304 �޸����ڱȽϷ�������CS��PSͳһ
//@CheckItem@ BUG320-renhj-20040310 �޸����ڱȽϷ�������CS��PSͳһ
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
//��������checkURL
//���ܽ��ܣ����Url�Ƿ�Ϸ�
//����˵����Ҫ�����ַ���
//����ֵ��true���Ϸ� false�����Ϸ���
function checkURL(strTemp)
{
	if(strTemp.length==0) return false;
	if(checkChinese(strTemp)==0) return false;
	if (strTemp.toUpperCase().indexOf("HTTP://") != 0 && strTemp.toUpperCase().indexOf("HTTPS://") != 0){
		return false;
	}
	return true;
}


// @CheckItem@ OPT-Renhj-20030704 �ṩ������ȥ���ո�ķ���
//�����߿ո�
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
//����ұ߿ո�
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
//�����ߺ��ұ߿ո�
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
//��ʽ����������ڴ���������磺"2003-9-12" �����"2003-09-12"
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

//obj:���ݶ���
//dispStr :ʧ����ʾ������ʾ�ַ���
function checkUrlValid( obj,  dispStr)
{
	if(obj  == null)
	{
		alert("�������Ϊ��");
		return false;
	}
	var str = obj.value;

	var  urlpatern0 = /^https?:\/\/.+$/i;
	if(!urlpatern0.test(str))
	{
		alert(dispStr+"���Ϸ���������'http:\/\/'��'https:\/\/'��ͷ!");
		obj.focus();
		return false;
	}

	var  urlpatern2= /^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?.+$/i;
	if(!urlpatern2.test(str))
	{
		alert(dispStr+"�˿ںű���Ϊ������Ӧ��1��65535֮��!");
		obj.focus();
		return false;
	}


	var	urlpatern1 =/^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?(\/((\.)?(\?)?=?&?[a-zA-Z0-9_-](\?)?)*)*$/i;

	if(!urlpatern1.test(str))
	{
		alert(dispStr+"���Ϸ�,����!");
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
			alert(dispStr+"�˿ںŲ�����0��ͷ!");
			obj.focus();
			return false;
		}

		t = parseInt(s);
		if(t<1 || t >65535)
		{
			alert(dispStr+"�˿ںű���Ϊ������Ӧ��1��65535֮��!");
			obj.focus();
			return false;
		}


	}
	return true;
}

//��������checkVisibleEnglishChr
//���ܽ��ܣ�����Ƿ�Ϊ����ʾӢ���ַ�(  !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~)
//����˵����Ҫ�����ַ���
//����ֵ��true|false
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
//��������checkareaInput
//���ܽ��ܣ�����Ƿ��з�textarea�ַ�
//����˵����Ҫ�����ַ���
//����ֵ��false������ true��ȫ��Ϊ��textarea�ַ�
//add by liql 2005.01.05
function checkareaInput(str)
{
	var notinput = "<>";
    var i;
    for (i = 0; notinput != null && i < notinput.length; i++) {
        if (str.indexOf(notinput.charAt(i)) >= 0) {//����
          return false;
        }
      }
    return true;

}
//��������checkInputChr
//���ܽ��ܣ�����Ƿ��з�Input�ַ�
//����˵����Ҫ�����ַ���
//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
//add by renhj 2004.01.05
function checkInputChr(str)
{
	var notinput = "\"'<>@#$%^&*()";
    var i;
    for (i = 0; notinput != null && i < notinput.length; i++) {
        if (str.indexOf(notinput.charAt(i)) >= 0) {//����
          return false;
        }
      }
    return true;

}
//��������checktextareaInput
//���ܽ��ܣ�����Ƿ��з�Input�ַ�<,>
//����˵����Ҫ�����ַ���
//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
//add by liql 2004.12.31
function checktextareaInput(str)
{
	var notareainput = "<>_";
    var i;
    for (i = 0; notareainput != null && i < notareainput.length; i++) {
        if (str.indexOf(notareainput.charAt(i)) >= 0) {//����
          return false;
        }
      }
    return true;

}

//��������checkHalfangle
//���ܽ��ܣ�����Ƿ��а���ַ�()
//����˵����Ҫ�����ַ���
//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
//add by x_tianbei 2010.5.24
function checkHalfangle(str)
{
	var notareainput = "()";
    var i;
    for (i = 0; notareainput != null && i < notareainput.length; i++) {
        if (str.indexOf(notareainput.charAt(i)) >= 0) {//����
          return false;
        }
      }
    return true;

}

//��������checkNotHalfangleCha
//���ܽ��ܣ�����Ƿ��з�Input�ַ�
//����˵����Ҫ�����ַ���
//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
//add by renhj 2004.01.05
function checkNotHalfangleCha(str)
{
	var notinput = "<>_\"'<>@#$%^&*";
    var i;
    for (i = 0; notinput != null && i < notinput.length; i++) {
        if (str.indexOf(notinput.charAt(i)) >= 0) {//����
          return false;
        }
      }
    return true;

}


//@CheckItem@ BUG550-liujun-20040528 ����cs��checkForm.js�е�checkFormdate����
//��������checkFormdata
//���ܽ��ܣ����Form����
//����˵����
//obj��Ҫ���Ķ���
//name��Ҫ���Ķ�����������ƣ�
//length�����Ķ���ĳ��ȣ�<0����飩��
//notnull:Ϊtrue����ǿգ�
//notSpecChar:Ϊtrue�������������ַ���
//notChinessChar:Ϊtrue�������������ַ���
//numOrLetter:Ϊtrue����ֻ��Ϊ���ֻ�Ӣ����ĸ��
//pNumber:Ϊtrue����ֻ��Ϊ��������
//����ֵ��false����鲻ͨ�� true��ȫ��Ϊ��Input�ַ�
//add by renhj 2004.03.19
//@CheckItem@ BUG:1641:718-Renhj-20040902-Add5 �޸�У�����ֵ���Ϣ
function checkFormdata(obj,name,length,notnull,notSpecChar,notChinessChar,numOrLetter,pNumber){
	//������
	if (!obj) {alert("Ŀ�겻�Ƕ��󣬴���ʧ��!");return false;}
	var msg;
	var ilen;
	//��⺺��
        if (notChinessChar&&(checkChinese(obj.value) != 1)){
           msg=name+"���ܰ������֣�";
           alert(msg);
           obj.focus();
           return false;
          }
        //��������ַ�
         if(notSpecChar){
        	if(obj.type=="textarea"){
       			 if(!checktextareaInput(obj.value)){
	     			 var noinput = " < > _ ";
          			msg=name+"�зǷ��ַ���"+noinput+"����";
          			alert(msg);
          			obj.focus();
          			return false;
        			}

		}
		else{
        		if(!checkInputChr(obj.value)){
	     		 var notinput = " \" ' < > @ # $ % ^ & * ( )";
          		msg=name+"�зǷ��ַ���"+notinput+"����";
          		alert(msg);
        		  obj.focus();
         		 return false;
        		}

      		  }
	}
        //��ⳤ��
        if(length>=0&&(checkLength(obj.value)>length)){
          ilen=length/2;
          if(pNumber){
              msg=name+"���ܳ���"+length+"�����֣�";
          }else if(notChinessChar){
              msg=name+"���ܳ���"+length+"��Ӣ�ģ�";
          }else if(numOrLetter){
              msg=name+"���ܳ���"+length+"��Ӣ�Ļ����֣�";
          }else{
              msg=name+"���ܳ���"+length+"��Ӣ�Ļ�"+ilen+"�����֣�";
          }
          alert(msg);
          obj.focus();
          return false;
        }
        //���ǿ�
	if(notnull&&obj.value.trim()==""){
            msg="������"+name+"��";
            alert(msg);
	    obj.focus();
	    return false;
	}
        //���ֻ��Ϊ���ֻ�Ӣ����ĸ
        re = /[\W_]/;
        if (numOrLetter&&re.exec(obj.value)) {
            msg=name+"ֻ��Ϊ���ֻ�Ӣ����ĸ��";
            alert(msg);
	    obj.focus();
            return false;
        }
        //���ֻ��Ϊֻ��Ϊ������
        re = /[\D_]/;
        if (pNumber&&re.exec(obj.value)) {
            msg=name+"ֻ��Ϊ��������";
            alert(msg);
	    obj.focus();
            return false;
        }

	return true;

}
//@CheckItem@ BUG550-liujun-20040528 ����cs��checkForm.js�е�checkFormdate����
//��������checkFormdata111
//���ܽ��ܣ����Form����
//����˵����
//obj��Ҫ���Ķ���
//name��Ҫ���Ķ�����������ƣ�
//num: Ҫ���Ķ��󺬰�ǵĸ�����
//length�����Ķ��󲻺���ǵ��ַ����ȣ�<0����飩��
//notnull:Ϊtrue����ǿգ�
//notSpecChar:Ϊtrue�������������ַ���
//notChinessChar:Ϊtrue�������������ַ���
//numOrLetter:Ϊtrue����ֻ��Ϊ���ֻ�Ӣ����ĸ��
//pNumber:Ϊtrue����ֻ��Ϊ��������
//����ֵ��false����鲻ͨ�� true��ȫ��Ϊ��Input�ַ�
//add by renhj 2004.03.19
//@CheckItem@ BUG:1641:718-Renhj-20040902-Add5 �޸�У�����ֵ���Ϣ
function checkFormdata111(obj,name,num,length,notnull,notSpecChar,notChinessChar,numOrLetter,pNumber){
	//������
	if (!obj) {alert("Ŀ�겻�Ƕ��󣬴���ʧ��!");return false;}
	var msg;
	var ilen;
	//��⺺��
        if (notChinessChar&&(checkChinese(obj.value) != 1)){
           msg=name+"���ܰ������֣�";
           alert(msg);
           obj.focus();
           return false;
          }
        //��������ַ�
         if(notSpecChar){
        	if(obj.type=="textarea"){
       			 if(!checktextareaInput(obj.value)){
	     			 var noinput = " < > _ ";
          			msg=name+"�зǷ��ַ���"+noinput+"����";
          			alert(msg);
          			obj.focus();
          			return false;
        			}

		}
		else{
        		if(!checkInputChr(obj.value)){
	     		 var notinput = " \" ' < > @ # $ % ^ & * ( )";
          		msg=name+"�зǷ��ַ���"+notinput+"����";
          		alert(msg);
        		  obj.focus();
         		 return false;
        		}

      		  }
	}
        //��ⳤ��
        if(length>=0&&(checkLength(obj.value)>length)){
          ilen=(length+num)/2;
          total=length+num;
          if(pNumber){
              msg=name+"���ܳ���"+total+"�����֣�";
          }else if(notChinessChar){
              msg=name+"���ܳ���"+total+"��Ӣ�ģ�";
          }else if(numOrLetter){
              msg=name+"���ܳ���"+total+"��Ӣ�Ļ����֣�";
          }else{
              msg=name+"���ܳ���"+total+"��Ӣ�Ļ�"+ilen+"�����֣�";
          }
          alert(msg);
          obj.focus();
          return false;
        }
        //���ǿ�
	if(notnull&&obj.value.trim()==""){
            msg="������"+name+"��";
            alert(msg);
	    obj.focus();
	    return false;
	}
        //���ֻ��Ϊ���ֻ�Ӣ����ĸ
        re = /[\W_]/;
        if (numOrLetter&&re.exec(obj.value)) {
            msg=name+"ֻ��Ϊ���ֻ�Ӣ����ĸ��";
            alert(msg);
	    obj.focus();
            return false;
        }
        //���ֻ��Ϊֻ��Ϊ������
        re = /[\D_]/;
        if (pNumber&&re.exec(obj.value)) {
            msg=name+"ֻ��Ϊ��������";
            alert(msg);
	    obj.focus();
            return false;
        }

	return true;

}
//////////////////////////////////////////////////////////////////////////////////////
///checkFormdata1120
///imall.oms 1.1.2.0�޸� bywangjuntao
//////////////////////////////////////////////////////////////////////////////////////
//@CheckItem@ BUG550-liujun-20040528 ����cs��checkForm.js�е�checkFormdate����
//��������checkFormdata
//���ܽ��ܣ����Form����
//����˵����
//obj��Ҫ���Ķ���
//name��Ҫ���Ķ�����������ƣ�
//length�����Ķ���ĳ��ȣ�<0����飩��
//notnull:Ϊtrue����ǿգ�
//notSpecChar:Ϊtrue�������������ַ���
//notChinessChar:Ϊtrue�������������ַ���
//numOrLetter:Ϊtrue����ֻ��Ϊ���ֻ�Ӣ����ĸ��
//pNumber:Ϊtrue����ֻ��Ϊ��������
//����ֵ��false����鲻ͨ�� true��ȫ��Ϊ��Input�ַ�
//add by renhj 2004.03.19
//@CheckItem@ BUG:1641:718-Renhj-20040902-Add5 �޸�У�����ֵ���Ϣ
function checkFormdata1120(obj,name,length,notnull,notSpecChar,notChinessChar,numOrLetter,pNumber){
	//������
	if (!obj) {alert("Ŀ�겻�Ƕ��󣬴���ʧ��!");return false;}
	var msg;
	var ilen;
	//��⺺��
        if (notChinessChar&&(checkChinese(obj.value) != 1)){
           msg=name+"���ܰ������֣�";
           alert(msg);
           obj.focus();
           return false;
          }
        //��������ַ�
         if(notSpecChar){
        	if(obj.type=="textarea"){
       			 if(!checktextareaInput(obj.value)){
	     			 var noinput = " < > ";
          			msg=name+"�зǷ��ַ���"+noinput+"����";
          			alert(msg);
          			obj.focus();
          			return false;
        			}

		}
		else{
        		if(!checkInputChr(obj.value)){
	     		 var notinput = " \" ' < > @ # $ % ^ & * ( )";
          		msg=name+"�зǷ��ַ���"+notinput+"����";
          		alert(msg);
        		  obj.focus();
         		 return false;
        		}

      		  }
	}
        //��ⳤ��
        if(length>=0&&(checkLength(obj.value)>length)){
          ilen=length/2;
          if(pNumber){
              msg=name+"���ܳ���"+length+"�����֣�";
          }else if(notChinessChar){
              msg=name+"���ܳ���"+length+"��Ӣ�ģ�";
          }else if(numOrLetter){
              msg=name+"���ܳ���"+length+"��Ӣ�Ļ����֣�";
          }else {
              msg=name+"���ܳ���"+length+"��Ӣ�Ļ�"+ilen+"�����֣�";
          }
          alert(msg);
          obj.focus();
          return false;
        }
        //���ǿ�
	if(notnull&&obj.value.trim()==""){
            msg="������"+name+"��";
            alert(msg);
	    obj.focus();
	    return false;
	}
        //���ֻ��Ϊ���ֻ�Ӣ����ĸ
        re = /[\W_]/;
        if (numOrLetter&&re.exec(obj.value)) {
            msg=name+"ֻ��Ϊ���ֻ�Ӣ����ĸ��";
            alert(msg);
	    obj.focus();
            return false;
        }
        //���ֻ��Ϊֻ��Ϊ������
        re = /[\D_]/;
        if (pNumber&&re.exec(obj.value)) {
            msg=name+"ֻ��Ϊ��������";
            alert(msg);
	    obj.focus();
            return false;
        }

	return true;

}



/**
 * *********************************************************
 * *******���º���Ϊ sp���飬 ��Ϣ���� ������֪ͨ��������Ϣ ר��***
 ***********************************************************
 ************by liuxiaogang 2005-03-03 *********************
 */


 //�����߹ؼ��ַ�
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
//�����߹ؼ��ַ�
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
//�����ߺ��ұ߹ؼ��ַ�
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

   if(obj.value=='������ҵ����,�Զ��Ÿ���'||obj.value=='����SP���,�Զ��Ÿ���'||obj.value=='����SP�������,�Զ��Ÿ���')
   {
     obj.value="";
   }
}


 //�ַ������ַ������ݣ������ؼ��ַ�key_char
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
 * �ɹ� ��ҵ���������sp��ƣ������������� ����
 *��sp�б����Ҳ��ֽ��д���
 *call method:
 * btnCheck_onClick(this.form,this,"��ҵ����");
 *
 */
function btnCheck_onClick(form,value,type){

        var tmp1 = "," + value + ",";
        var strAarry = stringToArray(tmp1,",");
        var errorList="";
       for(var j=0 ; j<strAarry.length ;j++)
       {
         var error="0"; //�����ڱ�־
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
            if(type=="��ҵ����")
            {
              destValue = text.substring(index1+1,index2);
            }
            if(type=="SP���")
            {
             destValue = text.substring((index2+1),text.length);
            }
            if(type=="�������")
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
         alert("��ʾ:"+type+"Ϊ"+errorList+" ��SP���ܲ����ڻ����Ѿ�����ѡ��SP�б���!");
       }

    }


//��������ValidateIP
//���ܽ��ܣ���֤ip��ַ��ʽ
//����˵����
//obj��Ҫ���Ķ���
//name��Ҫ���Ķ�����������ƣ�
function ValidateIP(name,obj,notnull){
  var msg="";
  if(obj==null){
    msg="������Ϊ�գ�";
    return false;
  }
  //���ǿ�
  if(notnull&&obj.value.trim()==""){
    msg="������"+name+"��";
    alert(msg);
    obj.focus();
    return false;
  }
  right_title="��һ���Ƿ���IP��ַ�Σ�\nIP��Ϊ��xxx.xxx.xxx.xxx��xxxΪ0-255)��";
  re=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;

  if(!re.test(obj.value)){
    msg=name+right_title;
    alert(msg);
    obj.focus();
    return false;
  }
  return true;
}

function StrToDate(strDate){
var splitArray;
var str="";
str=strDate;
splitArray=str.split("-");
if(splitArray.length!=3)
  return new Date();
//for(var i=0; i<splitArray.length; i++)
//{
//  if( isNaN(splitArray) ) return new Date();
//}
return new Date(splitArray[0],splitArray[1]-1,splitArray[2]);
}

/* Added by bocen.li for enable/unable edition for form elements */
function ChgFormEdition(frmObj, enable)
{
  for (var i=0; i< frmObj.elements.length; i++)
  {
		var objTag = frmObj.elements(i);
		if((objTag.tagName.toLowerCase() == "input") || (objTag.tagName.toLowerCase() == "textarea"))
		{

			if (objTag.type.toUpperCase()!="BUTTON" && 
	                        objTag.type.toUpperCase()!="HIDDEN" && 
	                           objTag.type.toUpperCase()!="IMAGE" &&
	                             objTag.type.toUpperCase()!="SUBMIT" &&
																objTag.type.toUpperCase()!="RESET" &&
																	objTag.type.toUpperCase()!="FILE")
					objTag.readOnly = !enable;			
			if(objTag.type.toUpperCase()=="FILE" && !enable)
					objTag.style.display = "none";

			if(objTag.tagName.toUpperCase()=="TEXTAREA")
					objTag.readOnly = !enable;
	  }
	  if(objTag.tagName.toLowerCase()=="select")
	  	objTag.disabled = !enable;
	}
}

function checkFormatt(ss)
{
      
     var s =trim(ss);
		var str=s.toLowerCase();
		if(!((str=="gif")||(str=="jpg")||(str=="png")))
		{
			alert("����Ԥ��ͼƬ��ʽ������gif��jpg��png");
			return false;
		}
		return true ;
}
function checkFormatt1(ss)
{
      
     var s =trim(ss);
		var str=s.toLowerCase();
		if(!((str=="gif")||(str=="jpg")||(str=="png")))
		{
			alert("ͼƬ��ʽ������gif��jpg��png");
			return false;
		}
		return true ;
}

function checkRingFormatt(ss)
{
      
     var s =trim(ss);
		var str=s.toLowerCase();
		if(!((str=="mp3")||(str=="mid")||(str=="wav")||(str=="wma")))
		{
			alert("�����������ͱ�����mp3��mid��wav��wma�е�һ��");
			return false;
		}
		return true ;
}
function checkSymbianFormatt(ss,platform)
{
      
    
		var str=ss.toLowerCase();
		
		if(!((str=="sis")||(str=="sisx")||(str=="jat")))
		{
			alert(platform+"ƽ̨��ԭʼ�����������sis��sisx��jat��ʽ���ļ���");
			
			return false;
		}
		return true ;
}
function checkMobileFormatt(ss,platform)
{
      
		var str=ss.toLowerCase();
		if(!((str=="cab")||(str=="exe")))
		{
			alert(platform+"ƽ̨��ԭʼ�����������cab��exe��ʽ���ļ���");
			return false;
		}
		return true ;
}
function checkOMSFormatt(ss,platform)
{
      
    
		var str=ss.toLowerCase();
		
		if(!(str=="apk"))
		{
			alert(platform+"ƽ̨��ԭʼ�����������apk��ʽ���ļ���");
			
			return false;
		}
		return true ;
}

function checkKJavaFormat(ss,platform)
{
		
		if(!(ss=="zip" || ss=="rar"))
		{
			alert(platform+"ƽ̨��ԭʼ�����������zip��rar��ʽ���ļ���");
			
			return false;
		}
		return true ;
}

function checkKJavaThemeFormat(ss,platform)
{
		
		if(!(ss=="thm"))
		{
			alert(platform+"ƽ̨��ԭʼ�����������thm��ʽ���ļ���");
			
			return false;
		}
		return true ;
}

//add keywords
function checkKeywords(str) {
        var notinput ="\"'<>_~`!@#$%^&*()+-?\/\\";
      var i;
      for (i = 0; i < notinput.length; i++) {
          if (str.indexOf(notinput.charAt(i)) >= 0) {//����
            return false;
          }
        }
    return true;
    }
    
    function trimAll(s) {
	 if(s == null || s == "") {
	  return "";
	 }
	 
	 while (s.charAt(0) == " " || s.charAt(0) == "��" || s.charAt(0) == "\t" || s.charAt(0) == "\r" || s.charAt(0) == "\n") {
	  s = s.substring(1,s.length);
	 }
	 
	 while(s.charAt(s.length-1) == " " || s.charAt(s.length-1) == "��" || s.charAt(s.length-1) == "\t" || s.charAt(s.length-1) == "\r" || s.charAt(s.length-1) == "\n") {
	  s = s.substring(0,s.length-1);
	 }
	 return s;
   }
   
   
   
   function checkImageFormat(obj,name,format,pictureSize){
   try{
   //ǰ̨����У���� ��̨��
   //У�������
   /*
   if(navigator.appName == "Microsoft Internet Explorer") 
    { 
           if(navigator.appVersion.match(/7./i)=='7.') 
           {
                  //alert( '��ǰ�������IE 7' )
           }
           if(navigator.appVersion.match(/6./i)=='6.') 
           {
                 // alert( '��ǰ�������IE 6' )
                  //У��ͼƬ��С
                 if(!getFileSize(obj,name,pictureSize))
                 {
                   return false;
                  }
           }


      }*/

	var s = new String(obj);
	var t = s.lastIndexOf(".");
	
	var objStr=obj;
    var separate=objStr.lastIndexOf("\\");
    if(separate<0)
    {
    	separate=objStr.lastIndexOf("/");
    }
    objStr=objStr.substring(separate+1);
    
    /*  ȡ����ͼƬ������������  
    if(checkChinese(objStr)!= 1)
    {
    	alert(name+"���ļ����в��ܺ��к���");
    	return false;//�ļ����д������ģ�������
    }
    */
         
	if(t > 0 && t == s.length - 4)
	{
		var ss=s.substring(t+1).toLowerCase();
		if (!checkFormaPicture(name,ss,format)) return false;
	}
	else
	{
	   alert(name+"�ĸ�ʽ����");
	   return false;
	}
	return true;
}catch(e){}
}
function getFileSize(filePath,name,pictureSize)   
{   
  if(pictureSize == 0)
  {
    return true;
  }
   var image=new Image();   
   image.dynsrc=filePath;  
   //alert(image.fileSize);
   if(image.fileSize > 1024 * pictureSize) 
   {
      alert(name+"��С���ܴ���"+pictureSize+"K!");   
      return false;
   }
    return true;
 
 
} 
function checkFormaPicture(name,ss,format)
{
     var strAarry = stringToArray(format,"/");
     var s =ss.trim();
	 var str=s.toLowerCase();
	 for(var j=0 ; j<strAarry.length ;j++)
     {
		if(str==strAarry[j])
		{
			return true ;
		}
	 }
	    alert(name+"��ʽ������"+format);
		return false ;
}

function  Checkdecimal(decimal,obj){    
   	var thisdecimal = decimal.trim(); 
    var re = /^\d*\.?\d{0,2}$/; 
    if(thisdecimal== ""){
      	alert("�����������ʷѣ�");
       	obj.focus();
       	return false;
     }
     else{ 
      	if(re.exec(thisdecimal)   ==   null){                     
           	alert( "�����ʷ����������������2λС����"); 
            obj.focus(); 
            return false;
        }
        
        var dec = 0+decimal;
        if (dec < 0 || dec > 999.99) {
			alert('�����ʷѱ�����0.00-15.00֮��');
			obj.focus(); 
			return false;
		}
            
        }    
     return true;
}

//��������checkOnlyChinese
//���ܽ��ܣ����ֻ�ܺ��к���
//����˵����Ҫ�����ַ���
//����ֵ��0�����������ַ�  1��ֻ�к��� 
function checkOnlyChinese(strTemp)
{
	var i,sum;
	for(i=0;i<strTemp.length;i++)
	{
		if ((strTemp.charCodeAt(i)<0) || (strTemp.charCodeAt(i)>255)){
	      continue;
	      } else {
	      return 0;
	      }
	}
	return 1;
}  
//����AP���
//����˵��
//obj��Ҫ���Ķ���
//name��Ҫ���Ķ�����������ƣ�
//length�����Ķ���ĳ��ȣ�<0����飩��
//notnull:Ϊtrue����ǿգ�
//onlyChinese:Ϊtrue����ֻ�ܰ�������
function checkShortName(obj,name,length,notnull,onlyChinese) {
//������
	if (!obj) {alert("Ŀ�겻�Ƕ��󣬴���ʧ��!");return false;}
	var msg;
	var ilen=length*2;
	//��⺺��
        if (onlyChinese&&(checkOnlyChinese(obj.value) != 1)){
           msg=name+"ֻ�ܰ������֣�";
           alert(msg);
           obj.focus();
           return false;
          }
       
        //��ⳤ��
        if(ilen>=0&&(checkLength(obj.value)>ilen)){
          msg=name+"���ܳ���"+length+"�����֣�";
          alert(msg);
          obj.focus();
          return false;
        }
        //���ǿ�
	if(notnull&&obj.value.trim()==""){
            msg="������"+name+"��";
            alert(msg);
	    obj.focus();
	    return false;
	}
	return true;

}
//���ѡ�б�ǩ������
function checkLabel(name,labelCount){
	j=0;
	for(i=0;i<document.all(name).length;i++){      
		if(document.all(name)(i).checked){    
			j++; 
		}
		if (j>labelCount){
			return false;
		}
	}
		return true;
}

//��������checkKeyWordsInput
//���ܽ��ܣ����ֻ�ܰ������ġ���ĸ������
//����˵����Ҫ�����ַ���
//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
function checkKeyWordsInput(str)
{
	var value = str.value;
	
    var reg = /^[\ra-zA-Z0-9\u4e00-\u9fa5]+$/g;
    if(reg.test(value))
    {
      return true;
    }else{
	  return false;
    }
}
/**
 * ****************************END!*************************
 */
