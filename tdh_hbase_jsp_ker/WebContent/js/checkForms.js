<script language="javascript">
<!--
   //��������checkMaxLength
	//���ܽ��ܣ�����ַ����ĳ��ȣ�����ʾ������Ϣ
	//����˵����objΪ��Ҫ��֤�Ķ���titleΪ��������ƣ�lengthΪ�������󳤶�
	function checkMaxLength(obj,title,length)
	{
		if(checkLength(obj.value)>length)
			{
				alert(title+"<bean:message key="error.2005"/>"+length);
				obj.focus();
				return false;
			}
			else
			{
				return true;
			}
	}
   //��������checkMaxLengthChar
	//���ܽ��ܣ�����ַ����ĳ��ȣ�����ʾ������Ϣ У���ַ���Ҫȥ���س�
	//����˵����objΪ��Ҫ��֤�Ķ���titleΪ��������ƣ�lengthΪ�������󳤶�
	function checkMaxLengthChar(obj,title,length)
	{
		var strBuffer = obj.value;
		var countr = 0;
		for(var i=0 ;i< strBuffer.length;i++){
           var c = strBuffer.charAt(i);
           if(c=='\r'){
               countr++;
           }
        }
		if(obj.value.length - countr > length)
			{
				alert(title+"<bean:message key="error.2011"/>"+length);
				obj.focus();
				return false;
			}
			else
			{
				return true;
			}
	}
	//��������checkIsNum
	//���ܽ��ܣ�����Ƿ�Ϊ���֣�������������ʾ
	//����˵����objΪ��Ҫ��֤�Ķ���titleΪ���������
 	function checkIsNum(obj,title)
    {
    	if(checkNull(obj,title))
		{
			if(checkIntNum(obj.value)==0){
				alert("<bean:message key="error.WEB_INF_DICCODE_ERR"/>");
		 		obj.focus();
			}
		}
    }

    //��������checkOnlyNum
	//���ܽ��ܣ�����Ƿ�ֻ�������֣�������������ʾ
	//����˵����objΪ��Ҫ��֤�Ķ���titleΪ���������
 	function checkOnlyNum(obj,title)
    {
    	if(checkNull(obj,title))
		{
			if(checkIntNum(obj.value)==0){
				alert(title+"<bean:message key="error.2007"/>");
		 		obj.focus();
		 		return false;
			}
			else{
				return true;
			}
		}
    }
    //��������checkNumSemi
	//���ܽ��ܣ�����Ƿ�ֻ�������ֺ�";"��������������ʾ
	//����˵����objΪ��Ҫ��֤�Ķ���titleΪ���������
 	function checkNumSemi(obj,title)
    {
    	if(checkNull(obj,title))
		{
			if(checkMuliNum(obj.value)==0){
				alert(title+"<bean:message key="error.2012"/>");
		 		obj.focus();
		 		return false;
			}
			else{
				return true;
			}
		}
    }

	function CheckNC(obj){
		var reg = /[a-zA-Z0-9]+-/;
		if(obj.value.match(reg) == null){
			return true;
		}
		return false;
	}

    //��������checkNull
	//���ܽ��ܣ�������������Ƿ�Ϊ��
	//����˵����objΪ��Ҫ��֤�Ķ���titleΪ���������
	function checkNull(obj,title) {
	if(obj==null||obj.value==null){
		alert(title+"<bean:message key="error.2004"/>"+"");
		obj.focus();
		return false;
	}
    if(obj.value==null||obj.value==""||js_trim(obj.value).length==""){
    	alert(title+"<bean:message key="error.2004"/>"+"");
    	obj.focus();
    	return false;
    }
    else
    {
    	return true;
    }
  }

  //��������checkRadioNull
  //���ܽ��ܣ�����Radio�Ƿ�Ϊ��
  //����˵����objΪ��Ҫ��֤�Ķ���titleΪ���������
  function checkRadioNull(obj, title){
	for(var i = 0; i < obj.length; i++){
	  var o = obj[i];
	  if(o.checked){
		//alert(o.value);
		return true;
	  }
	}
	alert(title+"<bean:message key="error.2004"/>"+"");
	return false;
  }

  //��������checkMobile
  //���ܽ��ܣ�����Ƿ�Ϊ�ƶ����ֻ����룬������������ʾ
  //����˵����objΪ��Ҫ��֤�Ķ���
  function checkIsMobile(obj,title)
  {
  	if(checkNull(obj,title)){
    	if(obj.value > ""){
			var reg="/(134[0-8]|((13[5-9]|159)\d{1}))\d{7}/";
  	  	 	if(obj.value.match(reg)== null){
      	      	alert("<bean:message key="error.WEB_INF_MSISDN_NOT_CMCC"/>");
				return false;
		   	}else{
		   		return true;
		   	}
		}else{
			alert("<bean:message key="error.WEB_INF_MSISDN_ERR"/>");
			return false;
   		}
   	}else{
   		return false;
   	}
  }

  function statisticDate(obj1,obj2,title1,title2, monthLength){
  		var date1 = obj1.value.split("-");
  		beginYear = date1[0];
  		beginMonth = date1[1];
  		benginDay = date1[2];
  		var date2 = obj2.value.split("-");
  	    endYear = date2[0];
  		endMonth = date2[1];
  		endDay = date2[2];
  		if(compareMonth(beginYear,beginMonth,benginDay,endYear,endMonth,endDay,monthLength)){
  			alert("<bean:message key="error.WEB_OVER_STATTIME_NULL"/>");
  			return false;
  		}else{
  			return true;
  		}
  }

	function statisticDate2(obj1,obj2,title1,title2, monthLength){
  		var date1 = obj1.value;
  		if(date1.length>=10)
  		{
  			beginYear = date1.substring(0,4);
	  		beginMonth = date1.substring(5,7);
	  		benginDay = date1.substring(8,10);
	  	}
	  	if(date1.length>=13)
  		{
  			beginhour = date1.substring(11,13);
  		}
  		if(date1.length>=16)
  		{
  			beginminute = date1.substring(14,16);
  		}
  		if(date1.length>=19)
  		{
  			beginsecond = date1.substring(17,19);
  		}
  		var date2 = obj2.value;
  		if(date2.length>=10)
  		{
	  	    endYear = date2.substring(0,4);
	  		endMonth = date2.substring(5,7);
	  		endDay = date2.substring(8,10);
	  	}
	  	if(date2.length>=13)
  		{
  			endhour = date2.substring(11,13);
  		}
  		if(date2.length>=16)
  		{
  			endminute = date2.substring(14,16);
  		}
  		if(date2.length>=19)
  		{
  			endsecond = date2.substring(17,19);
  		}
  		if(compareMonth(beginYear,beginMonth,benginDay,endYear,endMonth,endDay,monthLength)){
  			alert("<bean:message key="error.WEB_OVER_STATTIME_NULL"/>");
  			return false;
  		}else{
  		if(((beginYear==endYear)&&
  		   ((parseInt(endMonth)-parseInt(beginMonth))==monthLength))
  			||(((parseInt(endYear)-parseInt(beginYear))==1)&&
  			  (((parseInt(endMonth)+1)+(11-parseInt(beginMonth)))==monthLength)))
	  			{
	  				//alert((parseInt(benginDay))>=(parseInt(endDay)));
	  				if((parseInt(benginDay))<(parseInt(endDay)))
	  				{
	  					alert("<bean:message key="error.WEB_OVER_STATTIME_NULL"/>");
			  			return false;
	  				}
	  				if((parseInt(benginDay))==(parseInt(endDay)))
	  				{
		  				if(beginhour<endhour)
			  			{
			  				alert("<bean:message key="error.WEB_OVER_STATTIME_NULL"/>");
			  				return false;
			  			}
			  			else if(beginhour==endhour)
			  			{
				  			if(beginminute<endminute)
				  			{
				  				alert("<bean:message key="error.WEB_OVER_STATTIME_NULL"/>");
				  				return false;
				  			}
				  			else if(beginminute==endminute)
				  			{
				  				if(beginsecond<=endsecond)
					  			{
					  				alert("<bean:message key="error.WEB_OVER_STATTIME_NULL"/>");
					  				return false;
					  			}
				  			}
			  			}
			  		}
	  			}
	  			return true;
  		}
  }
  
  //��������compareTwoDate
  //���ܽ��ܣ��Ƚ��������ڵĴ�С��������������ʾ
  //����˵����obj1��obj2Ϊ��Ҫ�ȽϵĶ���
  function compareTwoDate(obj1,obj2,title1,title2){
  	if(checkNull(obj1,title1)&&checkNull(obj2,title2))
  	{
  		var date1 = obj1.value.split("-");
  		beginYear = date1[0];
  		beginMonth = date1[1];
  		benginDay = date1[2];
  		var date2 = obj2.value.split("-");
  	    endYear = date2[0];
  		endMonth = date2[1];
  		endDay = date2[2];
  		if(compareDate2(beginYear,beginMonth,benginDay,endYear,endMonth,endDay)){
  			return true;
  		}
  		else{
  			alert(title1+"<bean:message key="error.2013"/>"+title2+"�����������룡");
  		    //obj2.focus();
  		    return false;
  		}
	}
  }

  //��������compareTwoTime
  //���ܽ��ܣ��Ƚ��������ڵĴ�С��������������ʾ
  //����˵����obj1��obj2Ϊ��Ҫ�ȽϵĶ���
  function compareTwoTime(obj1,obj2,title1,title2){
  	if(checkNull(obj1,title1)&&checkNull(obj2,title2))
  	{
  		var date1 = obj1.value;
  		if(date1.length>=10)
  		{
  			beginYear = date1.substring(0,4);
	  		beginMonth = date1.substring(5,7);
	  		benginDay = date1.substring(8,10);
	  	}
	  	if(date1.length>=13)
  		{
  			beginhour = date1.substring(11,13);
  		}
  		if(date1.length>=16)
  		{
  			beginminute = date1.substring(14,16);
  		}
  		if(date1.length>=19)
  		{
  			beginsecond = date1.substring(17,19);
  		}
  		var date2 = obj2.value;
  		if(date2.length>=10)
  		{
	  	    endYear = date2.substring(0,4);
	  		endMonth = date2.substring(5,7);
	  		endDay = date2.substring(8,10);
	  	}
	  	if(date2.length>=13)
  		{
  			endhour = date2.substring(11,13);
  		}
  		if(date2.length>=16)
  		{
  			endminute = date2.substring(14,16);
  		}
  		if(date2.length>=19)
  		{
  			endsecond = date2.substring(17,19);
  		}
  		if((beginsecond!=null)&&(endsecond!=null))
  		  {
	  		if(compareTime2(beginYear,beginMonth,benginDay,beginhour,beginminute,beginsecond,endYear,endMonth,endDay,endhour,endminute,endsecond)){
	  			return true;
	  			}
	  		else{
	  			alert(title1+"<bean:message key="error.2014"/>"+title2+"�����������룡");
	  		    //obj2.focus();
	  		    return false;
  		   		}
		}
		else
		{
			if(compareTime(beginYear,beginMonth,benginDay,beginhour,beginminute,endYear,endMonth,endDay,endhour,endminute)){
	  			return true;
	  			}
	  		else{
	  			alert(title1+"<bean:message key="error.2014"/>"+title2+"�����������룡");
	  		    //obj2.focus();
	  		    return false;
  		   		}
		}

  	}
  }
  //��������checkIsEmail
  //���ܽ��ܣ����������Email�Ƿ���ȷ��������������ʾ
  //����˵����objΪ��Ҫ����Ķ���
  function checkIsEmail(obj,title){
  	if(checkNull(obj,title))
  	{
  		if(checkEmail(obj.value)==1){
  			return true;
  		}
  		else{
  			alert("<bean:message key="error.WEB_INF_EMAIL_ERR"/>");
  			obj.focus();
  			return false;
  		}
	}
  }

  //��������checkIP
  //���ܽ��ܣ����������IP��ַ�Ƿ���ȷ��������������ʾ
  //����˵����objΪ��Ҫ����Ķ���
  function checkIP(obj,title){
  	if(checkNull(obj,title))
  	{
  		if(validateIP(obj.value)){
  			return true;
  		}
  		else{
  			alert("<bean:message key="error.WEB_INF_IP_ERR"/>");
  			obj.focus();
  			return false;
  		}
	}
  }

   //��������checkSubmit
  //���ܽ��ܣ�ȥ�������ӣ���ֹ�ظ��ύ
   function checkSubmit(){
   		var obj = document.getElementById("commit");
		var title = obj.innerHTML;
		replaceContent(obj,title);
   }

  //��������checkLink
  //���ܽ��ܣ�ȥ��������
   function checkLink(linkStr){
   		var obj = document.getElementById(linkStr);
		var title = obj.innerHTML;
		replaceContent(obj,title);
   }
   function checkPWD(obj,title){
   		if(checkIsNull(obj))
   		{
   			if((checkCharOrNum(obj.value)==1)&&(checkLength(obj.value)>=6))
   			{
   				//alert(checkCharOrNum(obj.value));
   				//alert(checkLength(obj.value));
   				if((checkChar1(obj.value)==1)||(checkIntNum(obj.value)==1))
   				{
   					alert("<bean:message key="error.WEB_INF_PWD_ERR"/>");
	   				obj.focus();
	   				return false;
   				}
   				return true;
   			}
   			else
   			{
   				alert("<bean:message key="error.WEB_INF_PWD_ERR"/>");
   				obj.focus();
   				return false;
   			}
   		}
   }
   //��������checkTwoPWD
   //���ܽ��ܣ������������������Ƿ�һ��
   function checkTwoPWD(obj1,obj2){
   		if(checkIsNull(obj1)&&checkIsNull(obj2))
   		{
   			if(obj1.value!=obj2.value)
   			{
   				alert("<bean:message key="error.WEB_INF_PWDSAME_ERR"/>");
   				obj2.focus();
   				return false;
   			}
   			else
   			{
   				return true;
   			}
   		}
   }

   //��������checkSelect
   //���ܽ��ܣ������ѡ���Ƿ�Ϊ��
   function checkSelect(obj,title,behav){
   	if(obj!=null){
   	  if(obj.length>=0)
   	   {
   		for(i=0;i<obj.length;i++){
    		if(obj[i].checked)
    			{
    			return true;
    			}
    		}
    	alert("<bean:message key="error.2008"/>"+behav+"!");
    	return false;
    	}
    else{
    	if(!obj.checked)
    	{
    		alert("<bean:message key="error.2008"/>"+behav+"!");
    		return false;
    	}
    	else{
    		return true;
    	}
     }
    }
    else
    {
    	alert("<bean:message key="error.2009"/>");
    }
   }

    //��������checkIsSelect
   //���ܽ��ܣ������ѡ���Ƿ�Ϊ��
   function checkIsSelect(obj,title){
   	if(obj!=null){
   	  if(obj.length>=0)
   	   {
   		for(i=0;i<obj.length;i++){
    		if(obj[i].checked)
    			{
    			return true;
    			}
    		}
    	alert(title+"<bean:message key="error.2004"/>");
    	return false;
    	}
    else{
    	if(!obj.checked)
    	{
    		alert(title+"<bean:message key="error.2004"/>");
    		return false;
    	}
    	else{
    		return true;
    	}
     }
    }
    else
    {
    	alert(title+"<bean:message key="error.2004"/>");
    }
   }

   //��������exportCheckIsSelect
   //���ܽ��ܣ�����ʱ����б��Ƿ�Ϊ�գ�����б�Ϊ��true
   function exportCheckIsSelect(obj){
   	if(obj!=null){
   	  if(obj.length>=0){
    	return true;
      }else{
      	if(obj.type == "checkbox"){
      	  return true;
      	}else{
    	  alert("��ǰ�б�Ϊ�գ����ܵ�����");
    	  return false;
    	}
      }
	}else{
	  alert("��ǰ�б�Ϊ�գ����ܵ�����");;
	  return false;
	}
   }

   //��������exportCountCheck
   //���ܽ��ܣ�����ʱ����������Ƿ����������60000��
   function exportCountCheck(){
      var pageSizeVar = form.pageSizeVar.value;
      if(pageSizeVar > 60000){
   		  alert("<bean:message key="error.WEB_INF_OUPUT_OVERFLOW"/>");
   		  return false;
   	  }
   	  return true;
   }


  //��������compareNow
  //���ܽ��ܣ��뵱ǰʱ�����Ƚ�
  function compareNow(obj,title){
  			var dateArray;
			timeStr = obj.value;
			dateStr = timeStr.substring(0,10);
			hourStr = timeStr.substring(11,13);
	 		minuteStr = timeStr.substring(14,16);
			dateArray = dateStr.split("-");
			var date1 = new Date(dateArray[0],dateArray[1]-1,dateArray[2],hourStr,minuteStr);
			var date2 = new Date();
			if(date1.getTime()<date2.getTime())
			{
				alert(title+"<bean:message key="error.2010"/>");
				return false;
			}
			return true;
  }
  //��������compareServerTime
  //���ܽ��ܣ����������ʱ����бȽ�
  function compareServerTime(obj,serverDate,enterDate,leaveDate,title){
  			var dateArray;
			timeStr = obj.value;
			dateStr = timeStr.substring(0,10);
			hourStr = timeStr.substring(11,13);
	 		minuteStr = timeStr.substring(14,16);
			dateArray = dateStr.split("-");
			var date1 = new Date(dateArray[0],dateArray[1]-1,dateArray[2],hourStr,minuteStr);
			if((date1-serverDate)<(leaveDate-enterDate))
			{
				alert(title+"<bean:message key="error.2010"/>");
				return false;
			}
			else{
				return true;
			}
	}

   //��������confirmDel
   //���ܽ��ܣ��Ƿ�ȷ��ɾ��
   function confirmDel(){
   		return confirm("<bean:message key="error.WEB_INF_SURE_DEL"/>");
   }
   //��������confirmDel
   //���ܽ��ܣ��Ƿ�ȷ�ϼ���
   function confirmEnable(){
   		return confirm("<bean:message key="error.WEB_INF_SURE_STARTACT"/>");
   }
   //��������confirmDel
   //���ܽ��ܣ��Ƿ�ȷ��ȥ��
   function confirmDisable(){
   		return confirm("<bean:message key="error.WEB_INF_SURE_STOPACT"/>");
   }
   //��������setPageNo
   //���ܽ��ܣ����ò�ѯ�����ʼҳΪ��һҳ
   function setPageNo(){
   		document.forms[0].pageSize.value = 10;
   		document.forms[0].pageNo.value = 1;
   }
   //��������addToList
   //���ܽ��ܣ����б���������ݵ���ѡ�б�
    function addToList(from,to)
    {
    	fromList = eval('document.forms[0].' + from);
		toList = eval('document.forms[0].' + to);
		if(fromList.options.length > 0){
			var sel = false;
			for (i=0;i<fromList.options.length;i++){
				var current = fromList.options[i];
				if (current.selected){
					sel = true;
					txt = current.text;
					val = current.value;
					toList.options[toList.length] = new Option(txt,val);
					fromList.options[i] = null;
					i--;
				}
			}
			if (!sel) alert ("<bean:message key="error.2020"/>");
		}
    }
   //��������delFromList
   //���ܽ��ܣ�����ѡ�б���ɾ����ѡ��
    function delFromList(from,to){
    	fromList = eval('document.forms[0].' + from);
		toList = eval('document.forms[0].' + to);
		if(fromList.options.length > 0){
			var sel = false;
			for (i=0;i<fromList.options.length;i++){
				var current = fromList.options[i];
				if (current.selected){
					sel = true;
					txt = current.text;
					val = current.value;
					toList.options[toList.length] = new Option(txt,val);
					fromList.options[i] = null;
					i--;
				}
			}
			if (!sel) alert ("<bean:message key="error.2018"/>");
		}
    }

	//���ǵ��û������ύ��ťʱ�����г�ѡ���select����ִ��ȫѡ�������õݽ����ĺ�̨������ȡ���������
	function allSelect(chosen) {
		List = eval('document.forms[0].' + chosen);
		if (List.length < 1) return;
		for (i=0;i<List.length;i++){
			List.options[i].selected = true;
			//alert(List.options[i].value + " " + List.options[i].text);
		}
	}

    function OnChangeSelect(target, obj, restore){
		for (i=0;i<obj.options.length;i++){
			var current = obj.options[i];
			if (current.selected){
				txt = current.text;
				val = current.value;
			}
		}
		document.forms(0).action = val;
		document.forms(0).submit();
    }


  //����ҵ��ķ�ҳ������Ϣ 2006-08-21
  function savePager(){    
	    	pageNo = document.forms[0].pageNo.value;
	    	pageSize = document.forms[0].pageSize.value;

	    	if((pageNo!=null)&&(pageSize!=null))
	    	{
	    		document.forms[0].qryPageNo.value = pageNo;
	    		document.forms[0].qryPageSize.value = pageSize;
	    	}
    }
    
   function saveFlag(){
    	form.pageFlag.value = "true";
   }
-->
</script>