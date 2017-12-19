jQuery(document).ready(function() {
		String.prototype.startWith = function(str) {
			if (str == null || str == "" || this.length == 0 || str.length > this.length)
				return false;
			if (this.substr(0, str.length) == str)
				return true;
			else
				return false;
			return true;
		}
			
		//�жϼ����ı�
		jQuery.validator.addMethod("checkEncryptText", function(value,element) {
				if(value.startWith("whatever")){
					jQuery.validator.messages["checkEncryptText"] = "���ݲ��Ϸ�������������!";
					return false;
				}
				return true;
		});	
		// �ַ���֤
		jQuery.validator.addMethod("userName",
				function(value, element) {
					return this.optional(element)
							|| /^[\u0391-\uFFE5\w]+$/.test(value);
				}, "�벻Ҫ���������ַ�");

		// �ֻ�������֤
        //��ǰ��֤����     /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/
		//13[0-9]|15[0|1|2|3|5|6|7|8|9]|18[0|6|7|8|9])\d{8}
		jQuery.validator.addMethod("isMobile", function(value, element) {
			var length = value.length;
			return this.optional(element)
					|| (length == 11 && 
						/^1[3,5,8]\d{9}|147\d{8}$/.test(value));
		}, "����ȷ��д�����ֻ�����");

		// �绰������֤
		jQuery.validator.addMethod("isPhone", function(value, element) {
			var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
			return this.optional(element) || (tel.test(value));
		}, "����ȷ��д���ĵ绰����");

		// ����������֤
		jQuery.validator.addMethod("isZipCode", function(value, element) {
			var tel = /^[0-9]{6}$/;
			var flag = this.optional(element) || (tel.test(value));
			return flag;
		}, "����ȷ��д������������");
		
		// ������֤
		jQuery.validator.addMethod("isNum", function(value, element) {
			var tel = /^[1-9]\d{0,12}$/;
			var flag = this.optional(element) || (tel.test(value));
			return flag;
		}, "����ȷ��д����ע���ʱ�");
		
		
		//��ҵ����
		jQuery.validator.addMethod("checkEnterpriseNature", function(value,
					element) {
						 var msg;
						 if(value=="noSelect")
						 {
							msg = "��ѡ����ҵ����";
							 $.validator.messages["checkEnterpriseNature"] = msg;
							 return false;
						 }
				return this.optional(element) || (value != "-1" );
		});
		
		//Ӣ�İ�ǰ汾����֤
		jQuery.validator.addMethod("checkHalfangle", function(value, element) {
			var flag = this.optional(element) || ((value.indexOf("(") < 0) && (value.indexOf(")") < 0));
			return flag;
		}, "����ú���ȫ�������ַ�");
		
		jQuery.validator.addMethod("checklengthA", function(value, element,param) {			
			var sum;
			var ilen;
			var msg;
			var flag = true;
			sum = commonUtil.checkLength(value);
			 if(param>=0&&(sum >param)){
		          ilen=param/2;
		          msg="���ܳ���"+param+"��Ӣ�Ļ�"+ilen+"�����֣�";
		          $.validator.messages["checklengthA"] = msg;
		          flag = false;
		        }
		return this.optional(element)|| flag;
	});
		
		jQuery.validator.addMethod("checklengthB", function(value, element,param) {			
			var sum;
			var ilen;
			var msg;
			var flag = true;
			if(value!=""){
				value = value.substring(value.lastIndexOf("\\")+1);
			}
			sum = checkLength(value);
			 if(param>=0&&(sum >param)){
		          ilen=param/2;
		          msg="���ܳ���"+param+"��Ӣ�Ļ�"+ilen+"�����֣�";
		          $.validator.messages["checklengthB"] = msg;
		          flag = false;
		        }
		return this.optional(element)|| flag;
	});
		
		jQuery.validator.addMethod("checklengthC", function(value, element,param) {			
			var sum;
			var ilen;
			var msg;
			var flag = true;
			if(value!=""){
				value = value.substring(value.lastIndexOf("\\")+1);
			}
			sum = checkLength(value);
			 if(param>=0&&(sum >param)){
		          ilen=param/2;
		          msg="���ܳ���"+param+"�����֣�";
		          $.validator.messages["checklengthC"] = msg;
		          flag = false;
		        }
		return this.optional(element)|| flag;
	});
	
	// ������ҵ��֯��������У�飬����Ϊ9λ add by luoying 2012-08-01
	jQuery.validator.addMethod("checklengthDD", function(value, element,param) {			
			var sum;
			var ilen;
			var msg;
			var flag = true;
			if(value!=""){
				value = value.substring(value.lastIndexOf("\\")+1);
			}
			sum = checkLength(value);
			 if(sum != param){
		          ilen=param/2;
		          msg="����Ϊ"+param+"λ���ֻ���ĸ��";
		          $.validator.messages["checklengthDD"] = msg;
		          flag = false;
		        }
		return this.optional(element)|| flag;
	});
	
		
		//������֤���֤�ͻ��յ���֤
		var identityInfo = "����ȷ��д���֤��Ϣ";
		var passportInfo = "����ȷ��д������Ϣ";
		jQuery.validator.addMethod("IdentityInfo",function(value, element) {
			//��ҵ����ҳ��
			if (element.id == "lowyer_identityId"){	
				var identityType = $("input[@type=radio][name='lowyer_identityType'][checked]").val();
			}else if(element.id == "personal_identityId"){
			//��������ҳ��
				var identityType = $("input[@type=radio][name='personal_identityType'][checked]").val();	
			}else{
				return true;
			}
			// ��֤���֤
			if (identityType == 1) {
				var flag = this.optional(element)
						|| (idCardNoUtil
								.checkIdCardNo(value));
				$.validator.messages["IdentityInfo"] = identityInfo;
				return flag;

			}// ��֤����
			else if (identityType == 2) {
				var flag = this.optional(element)
						|| checknumber(value);
				$.validator.messages["IdentityInfo"] = passportInfo;
				return flag;
			}
		}, identityInfo);
	});
	//���Ӹ��˺���������������֤	
	jQuery.validator.addMethod("checkComboBox", function(value,
				element) {
			if(element.id == "cityID"){
				$.validator.messages["checkComboBox"] = "��ѡ���տ��������ڵ�";
			}
			else if(element.id == "bank_branch"){
				$.validator.messages["checkComboBox"] = "��ѡ���տ�����";
				return this.optional(element) || (value != ""  && value != "���ѡ��֧��");
			}
			else if(element.id == "apCityID"){
				$.validator.messages["checkComboBox"] = "��ѡ��ͨ�ŵ�ַ����ʡ��";
			}
			return this.optional(element) || (value != ""  &&  value != "��");
		});
	//����У�鷽��
	//param�Ǵ�������һ��json����
	//�÷���checkFormdata : {"name":"����","length":100,"notnull":true,"notSpecChar":true,"notChinessChar":true,"numOrLetter":true,"pNumber":true}
	jQuery.validator.addMethod("checkFormdata", function(value,
				obj,param) {
			var name = param.name;	var length = param.length;	var notnull = param.notnull;		
			var notSpecChar = param.notSpecChar;  var notChinessChar = param.notChinessChar;		
			var numOrLetter = param.numOrLetter;  var pNumber = param.pNumber;		
			var msg;
			var ilen;
			//���ǿ�
			if(notnull&&value.trim()==""){
			    jQuery.validator.messages["checkFormdata"] = "������"+name+"��";
			    return false;
			}
		    //��⺺��
	        if (notChinessChar&&(commonUtil.checkChinese(value) != 1)){
	           jQuery.validator.messages["checkFormdata"] = name+"���ܰ������֣�";
	           return false;
            }
	        //��������ַ�
	        if(notSpecChar){
	        	if(obj.type=="textarea"){
	       			 if(!commonUtil.checktextareaInput(value)){
		     			var noinput = " < > _ ";
	          			jQuery.validator.messages["checkFormdata"] = name+"�зǷ��ַ���"+noinput+"����";
	          			return false;
        			  }
	
				}else{
	        		if(!commonUtil.checkInputChr(value)){
			     		 var notinput = " \" ' < > @ # $ % ^ & * ( )";
		          		 jQuery.validator.messages["checkFormdata"] = name+"�зǷ��ַ���"+notinput+"����";
		         		 return false;
		        		}
		
      		    }
		    }
	        //��ⳤ��
	        if(length>=0&&(commonUtil.checkLength(value)>length)){
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
	          jQuery.validator.messages["checkFormdata"] = msg;
	          return false;
	        }
	        
	        //���ֻ��Ϊ���ֻ�Ӣ����ĸ
	        re = /[\W_]/;
	        if (numOrLetter&&re.exec(obj.value)) {
	           jQuery.validator.messages["checkFormdata"] = name+"ֻ��Ϊ���ֻ�Ӣ����ĸ��";
	            return false;
	        }
	        //���ֻ��Ϊֻ��Ϊ������
	        re = /[\D_]/;
	        if (pNumber&&re.exec(obj.value)) {
		    	jQuery.validator.messages["checkFormdata"] = name+"ֻ��Ϊ��������";
	            return false;
	        }
			return this.optional(obj) || true;
	});
	
	//���ӹ�����������֤	
	//param����������  name���ؼ��� nullValue��selectΪ�յ��Ǹ�ֵ��������������ڿؼ���д��""�����븽һ��ֵ ��
	//�÷�checkSelectCommon : {"name":"�տ�����","nullValue":"��"}
	jQuery.validator.addMethod("checkSelectCommon", function(value,
				element,param) {
				var name = param.name;	
				var nullValue = param.nullValue;	
				$.validator.messages["checkSelectCommon"] = "��ѡ��"+name;
			return this.optional(element) || (value != ""  &&  value != nullValue);
		});
	// �����ַ���֤
	jQuery.validator.addMethod("sensitive",
		function(value, element) {
			return this.optional(element)
					|| commonUtil.checkInputChr(value);
		},
		"�벻Ҫ���������ַ�");	
	// ������׺У��
	jQuery.validator.addMethod("checkFileFormat",
		function(value, element, param) {
			return this.optional(element)
					|| commonUtil.checkFileFormat(value,param);
		},
		"�ļ���ʽ����Ϊgif/jpg/png/rar/zip");	
		
	// ���������������Ƿ�Ϊ��
	jQuery.validator.addMethod("checkFileNotEmpty",
		function(value, element, param) {
			jQuery.validator.messages["checkFileNotEmpty"] = "���ϴ�������";
			if(jQuery("[name='"+param+"']").val()==""&&value.trim()==""){
				return false;
			}else{
				return true;
			}
		});		
		
var commonUtil = {
	//��������checkChinese
	//���ܽ��ܣ�����Ƿ��к���
	//����˵����Ҫ�����ַ���
	//����ֵ��0������ 1��û��
	checkChinese : function(strTemp)
	{
		var i,sum;
		for(i=0;i<strTemp.length;i++)
		{
			if ((strTemp.charCodeAt(i)<0) || (strTemp.charCodeAt(i)>255))
		      return 0;
		}
		return 1;
	},
	//��������checktextareaInput
	//���ܽ��ܣ�����Ƿ��з�Input�ַ�<,>
	//����˵����Ҫ�����ַ���
	//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
	//add by liql 2004.12.31
	checktextareaInput : function (str)
	{
		var notareainput = "<>_";
	    var i;
	    for (i = 0; notareainput != null && i < notareainput.length; i++) {
	        if (str.indexOf(notareainput.charAt(i)) >= 0) {//����
	          return false;
	        }
	      }
	    return true;
	
	},
	//��������checkInputChr
	//���ܽ��ܣ�����Ƿ��з�Input�ַ�
	//����˵����Ҫ�����ַ���
	//����ֵ��false������ true��ȫ��Ϊ��Input�ַ�
	//add by renhj 2004.01.05
	checkInputChr : function (str)
	{
		var notinput = "\"'<>@#$%^&*()";
	    var i;
	    for (i = 0; notinput != null && i < notinput.length; i++) {
	        if (str.indexOf(notinput.charAt(i)) >= 0) {//����
	          return false;
	        }
	      }
	    return true;
	
	},
	//��������checkLength
	//���ܽ��ܣ�����ַ����ĳ���
	//����˵����Ҫ�����ַ���
	//����ֵ������ֵ
	checkLength :function (strTemp)
	{
		var i,sum;
		sum = 0;
		for(i=0;i<strTemp.length;i++)
		{
			if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=128))
				sum = sum + 1;
			else
				sum = sum + 2;
		}
		return sum;
	},
	checkFileFormat :  function(obj,format){
	   	try{
			var s = new String(obj);
			var t = s.lastIndexOf(".");
			
			if(t > 0 && t == s.length - 4){
				var ss=s.substring(t+1).toLowerCase();
				if (!commonUtil.checkFormaPicture(name,ss,format)) return false;
			}else {
			   return false;
			}
			return true;
		}catch(e){}
	},
	checkFormaPicture : function(name,ss,format)
	{
	     var strAarry = stringToArray(format,"/");
	     var s =ss.trim();
		 var str=s.toLowerCase();
		 for(var j=0 ; j<strAarry.length ;j++) {
			if(str==strAarry[j]){
				return true ;
			}
		 }
		 return false ;
	}
	
}
//�����֤��
var idCardNoUtil = {
	provinceAndCitys : {
		11 : "����",
		12 : "���",
		13 : "�ӱ�",
		14 : "ɽ��",
		15 : "���ɹ�",
		21 : "����",
		22 : "����",
		23 : "������",
		31 : "�Ϻ�",
		32 : "����",
		33 : "�㽭",
		34 : "����",
		35 : "����",
		36 : "����",
		37 : "ɽ��",
		41 : "����",
		42 : "����",
		43 : "����",
		44 : "�㶫",
		45 : "����",
		46 : "����",
		50 : "����",
		51 : "�Ĵ�",
		52 : "����",
		53 : "����",
		54 : "����",
		61 : "����",
		62 : "����",
		63 : "�ຣ",
		64 : "����",
		65 : "�½�",
		71 : "̨��",
		81 : "���",
		82 : "����",
		91 : "����"
	},

	powers : [ "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9",
			"10", "5", "8", "4", "2" ],

	parityBit : [ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" ],

	genders : {
		male : "��",
		female : "Ů"
	},

	checkAddressCode : function(addressCode) {
		var check = /^[1-9]\d{5}$/.test(addressCode);
		if (!check)
			return false;
		if (idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0, 2))]) {
			return true;
		} else {
			return false;
		}
	},

	checkBirthDayCode : function(birDayCode) {
		var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/
				.test(birDayCode);
		if (!check)
			return false;
		var yyyy = parseInt(birDayCode.substring(0, 4), 10);
		var mm = parseInt(birDayCode.substring(4, 6), 10);
		var dd = parseInt(birDayCode.substring(6), 10);
		var xdata = new Date(yyyy, mm - 1, dd);
		if (xdata > new Date()) {
			return false;// ���ղ��ܴ��ڵ�ǰ����
		} else if ((xdata.getFullYear() == yyyy)
				&& (xdata.getMonth() == mm - 1) && (xdata.getDate() == dd)) {
			return true;
		} else {
			return false;
		}
	},

	getParityBit : function(idCardNo) {
		var id17 = idCardNo.substring(0, 17);

		var power = 0;
		for ( var i = 0; i < 17; i++) {
			power += parseInt(id17.charAt(i), 10)
					* parseInt(idCardNoUtil.powers[i]);
		}

		var mod = power % 11;
		return idCardNoUtil.parityBit[mod];
	},

	checkParityBit : function(idCardNo) {
		var parityBit = idCardNo.charAt(17).toUpperCase();
		if (idCardNoUtil.getParityBit(idCardNo) == parityBit) {
			return true;
		} else {
			return false;
		}
	},

	checkIdCardNo : function(idCardNo) {
		// 15λ��18λ���֤����Ļ���У��
	var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
	if (!check)
		return false;
	// �жϳ���Ϊ15λ��18λ
	if (idCardNo.length == 15) {
		return idCardNoUtil.check15IdCardNo(idCardNo);
	} else if (idCardNo.length == 18) {
		return idCardNoUtil.check18IdCardNo(idCardNo);
	} else {
		return false;
	}
},

// У��15λ�����֤����
	check15IdCardNo : function(idCardNo) {
		// 15λ���֤����Ļ���У��
		var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/
				.test(idCardNo);
		if (!check)
			return false;
		// У���ַ��
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check)
			return false;
		var birDayCode = '19' + idCardNo.substring(6, 12);
		// У��������
		return idCardNoUtil.checkBirthDayCode(birDayCode);
	},

	// У��18λ�����֤����
	check18IdCardNo : function(idCardNo) {
		// 18λ���֤����Ļ�����ʽУ��
		var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/
				.test(idCardNo);
		if (!check)
			return false;
		// У���ַ��
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check)
			return false;
		// У��������
		var birDayCode = idCardNo.substring(6, 14);
		check = idCardNoUtil.checkBirthDayCode(birDayCode);
		if (!check)
			return false;
		// ��֤У����
		return idCardNoUtil.checkParityBit(idCardNo);
	},

	formateDateCN : function(day) {
		var yyyy = day.substring(0, 4);
		var mm = day.substring(4, 6);
		var dd = day.substring(6);
		return yyyy + '-' + mm + '-' + dd;
	},

	// ��ȡ��Ϣ
	getIdCardInfo : function(idCardNo) {
		var idCardInfo = {
			gender : "", // �Ա�
			birthday : "" // ��������(yyyy-mm-dd)
		};
		if (idCardNo.length == 15) {
			var aday = '19' + idCardNo.substring(6, 12);
			idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
				idCardInfo.gender = idCardNoUtil.genders.female;
			} else {
				idCardInfo.gender = idCardNoUtil.genders.male;
			}
		} else if (idCardNo.length == 18) {
			var aday = idCardNo.substring(6, 14);
			idCardInfo.birthday = idCardNoUtil.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
				idCardInfo.gender = idCardNoUtil.genders.female;
			} else {
				idCardInfo.gender = idCardNoUtil.genders.male;
			}

		}
		return idCardInfo;
	},

	getId15 : function(idCardNo) {
		if (idCardNo.length == 15) {
			return idCardNo;
		} else if (idCardNo.length == 18) {
			return idCardNo.substring(0, 6) + idCardNo.substring(8, 17);
		} else {
			return null;
		}
	},

	getId18 : function(idCardNo) {
		if (idCardNo.length == 15) {
			var id17 = idCardNo.substring(0, 6) + '19' + idCardNo.substring(6);
			var parityBit = idCardNoUtil.getParityBit(id17);
			return id17 + parityBit;
		} else if (idCardNo.length == 18) {
			return idCardNo;
		} else {
			return null;
		}
	}
};
// ��֤�����Ƿ���ȷ
function checknumber(number) {
	//var str = number;
	// ��JavaScript�У�������ʽֻ��ʹ��"/"��ͷ�ͽ���������ʹ��˫����
	/*var Expression = /(P\d{7})|(G\d{8})/;
	var objExp = new RegExp(Expression);
	if (objExp.test(str) == true) {
		return true;
	} else {
		return false;
	}*/
	return number.length > 0 ? true : false;
};

