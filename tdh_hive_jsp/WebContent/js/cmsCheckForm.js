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

	if (obj.value > ""){
		var reg=/13[4,5,6,7,8,9]\d{8}/;
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
	var notareainput = "<>";
    var i;
    for (i = 0; notareainput != null && i < notareainput.length; i++) {
        if (str.indexOf(notareainput.charAt(i)) >= 0) {//����
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
	     			 var noinput = " < > ";
          			msg=name+"�зǷ��ַ���"+noinput+"����";
          			alert(msg);
          			obj.focus();
          			return false;
        			}

		}
		else{
        		if(!checkInputChr(obj.value)){
	     		 var notinput = " \" ' < >";
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
 
 //��֤ͼƬ��ʽ
 function checkFormat4Pic(ss,strPre)
{
      
     var s =trim(ss);
		var str=s.toLowerCase();
		if(!((str=="gif")||(str=="jpg")||(str=="png")))
		{
			alert(strPre + "��ʽ������gif��jpg��png");
			return false;
		}
		return true ;
}

//��ָ����С��ʾͼƬ
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

	//��ԭʼ��ͼƬ����
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
