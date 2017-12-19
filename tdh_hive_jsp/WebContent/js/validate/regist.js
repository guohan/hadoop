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
			
		//判断加密文本
		jQuery.validator.addMethod("checkEncryptText", function(value,element) {
				if(value.startWith("whatever")){
					jQuery.validator.messages["checkEncryptText"] = "内容不合法，请重新输入!";
					return false;
				}
				return true;
		});	
		// 字符验证
		jQuery.validator.addMethod("userName",
				function(value, element) {
					return this.optional(element)
							|| /^[\u0391-\uFFE5\w]+$/.test(value);
				}, "请不要输入特殊字符");

		// 手机号码验证
        //以前验证规则     /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/
		//13[0-9]|15[0|1|2|3|5|6|7|8|9]|18[0|6|7|8|9])\d{8}
		jQuery.validator.addMethod("isMobile", function(value, element) {
			var length = value.length;
			return this.optional(element)
					|| (length == 11 && 
						/^1[3,5,8]\d{9}|147\d{8}$/.test(value));
		}, "请正确填写您的手机号码");

		// 电话号码验证
		jQuery.validator.addMethod("isPhone", function(value, element) {
			var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
			return this.optional(element) || (tel.test(value));
		}, "请正确填写您的电话号码");

		// 邮政编码验证
		jQuery.validator.addMethod("isZipCode", function(value, element) {
			var tel = /^[0-9]{6}$/;
			var flag = this.optional(element) || (tel.test(value));
			return flag;
		}, "请正确填写您的邮政编码");
		
		// 数字验证
		jQuery.validator.addMethod("isNum", function(value, element) {
			var tel = /^[1-9]\d{0,12}$/;
			var flag = this.optional(element) || (tel.test(value));
			return flag;
		}, "请正确填写您的注册资本");
		
		
		//企业性质
		jQuery.validator.addMethod("checkEnterpriseNature", function(value,
					element) {
						 var msg;
						 if(value=="noSelect")
						 {
							msg = "请选择企业性质";
							 $.validator.messages["checkEnterpriseNature"] = msg;
							 return false;
						 }
				return this.optional(element) || (value != "-1" );
		});
		
		//英文半角版本号验证
		jQuery.validator.addMethod("checkHalfangle", function(value, element) {
			var flag = this.optional(element) || ((value.indexOf("(") < 0) && (value.indexOf(")") < 0));
			return flag;
		}, "请改用汉字全角括号字符");
		
		jQuery.validator.addMethod("checklengthA", function(value, element,param) {			
			var sum;
			var ilen;
			var msg;
			var flag = true;
			sum = commonUtil.checkLength(value);
			 if(param>=0&&(sum >param)){
		          ilen=param/2;
		          msg="不能超过"+param+"个英文或"+ilen+"个汉字！";
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
		          msg="不能超过"+param+"个英文或"+ilen+"个汉字！";
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
		          msg="不能超过"+param+"个数字！";
		          $.validator.messages["checklengthC"] = msg;
		          flag = false;
		        }
		return this.optional(element)|| flag;
	});
	
	// 增加企业组织机构代码校验，必须为9位 add by luoying 2012-08-01
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
		          msg="必须为"+param+"位数字或字母！";
		          $.validator.messages["checklengthDD"] = msg;
		          flag = false;
		        }
		return this.optional(element)|| flag;
	});
	
		
		//增加验证身份证和护照的验证
		var identityInfo = "请正确填写身份证信息";
		var passportInfo = "请正确填写护照信息";
		jQuery.validator.addMethod("IdentityInfo",function(value, element) {
			//企业资料页面
			if (element.id == "lowyer_identityId"){	
				var identityType = $("input[@type=radio][name='lowyer_identityType'][checked]").val();
			}else if(element.id == "personal_identityId"){
			//个人资料页面
				var identityType = $("input[@type=radio][name='personal_identityType'][checked]").val();	
			}else{
				return true;
			}
			// 验证身份证
			if (identityType == 1) {
				var flag = this.optional(element)
						|| (idCardNoUtil
								.checkIdCardNo(value));
				$.validator.messages["IdentityInfo"] = identityInfo;
				return flag;

			}// 验证护照
			else if (identityType == 2) {
				var flag = this.optional(element)
						|| checknumber(value);
				$.validator.messages["IdentityInfo"] = passportInfo;
				return flag;
			}
		}, identityInfo);
	});
	//增加个人合作资料下拉框验证	
	jQuery.validator.addMethod("checkComboBox", function(value,
				element) {
			if(element.id == "cityID"){
				$.validator.messages["checkComboBox"] = "请选择收款银行所在地";
			}
			else if(element.id == "bank_branch"){
				$.validator.messages["checkComboBox"] = "请选择收款银行";
				return this.optional(element) || (value != ""  && value != "点击选择支行");
			}
			else if(element.id == "apCityID"){
				$.validator.messages["checkComboBox"] = "请选择通信地址所属省市";
			}
			return this.optional(element) || (value != ""  &&  value != "无");
		});
	//公共校验方法
	//param是传进来的一个json对象
	//用法：checkFormdata : {"name":"名称","length":100,"notnull":true,"notSpecChar":true,"notChinessChar":true,"numOrLetter":true,"pNumber":true}
	jQuery.validator.addMethod("checkFormdata", function(value,
				obj,param) {
			var name = param.name;	var length = param.length;	var notnull = param.notnull;		
			var notSpecChar = param.notSpecChar;  var notChinessChar = param.notChinessChar;		
			var numOrLetter = param.numOrLetter;  var pNumber = param.pNumber;		
			var msg;
			var ilen;
			//检测非空
			if(notnull&&value.trim()==""){
			    jQuery.validator.messages["checkFormdata"] = "请输入"+name+"！";
			    return false;
			}
		    //检测汉字
	        if (notChinessChar&&(commonUtil.checkChinese(value) != 1)){
	           jQuery.validator.messages["checkFormdata"] = name+"不能包含汉字！";
	           return false;
            }
	        //检测特殊字符
	        if(notSpecChar){
	        	if(obj.type=="textarea"){
	       			 if(!commonUtil.checktextareaInput(value)){
		     			var noinput = " < > _ ";
	          			jQuery.validator.messages["checkFormdata"] = name+"有非法字符（"+noinput+"）！";
	          			return false;
        			  }
	
				}else{
	        		if(!commonUtil.checkInputChr(value)){
			     		 var notinput = " \" ' < > @ # $ % ^ & * ( )";
		          		 jQuery.validator.messages["checkFormdata"] = name+"有非法字符（"+notinput+"）！";
		         		 return false;
		        		}
		
      		    }
		    }
	        //检测长度
	        if(length>=0&&(commonUtil.checkLength(value)>length)){
	          ilen=length/2;
	          if(pNumber){
	              msg=name+"不能超过"+length+"个数字！";
	          }else if(notChinessChar){
	              msg=name+"不能超过"+length+"个英文！";
	          }else if(numOrLetter){
	              msg=name+"不能超过"+length+"个英文或数字！";
	          }else{
	              msg=name+"不能超过"+length+"个英文或"+ilen+"个汉字！";
	          }
	          jQuery.validator.messages["checkFormdata"] = msg;
	          return false;
	        }
	        
	        //检测只能为数字或英文字母
	        re = /[\W_]/;
	        if (numOrLetter&&re.exec(obj.value)) {
	           jQuery.validator.messages["checkFormdata"] = name+"只能为数字或英文字母！";
	            return false;
	        }
	        //检测只能为只能为正整数
	        re = /[\D_]/;
	        if (pNumber&&re.exec(obj.value)) {
		    	jQuery.validator.messages["checkFormdata"] = name+"只能为正整数！";
	            return false;
	        }
			return this.optional(obj) || true;
	});
	
	//增加公共下拉框验证	
	//param传两个参数  name：控件名 nullValue：select为空的那个值（这个参数不能在控件上写成""，必须附一个值 ）
	//用法checkSelectCommon : {"name":"收款银行","nullValue":"无"}
	jQuery.validator.addMethod("checkSelectCommon", function(value,
				element,param) {
				var name = param.name;	
				var nullValue = param.nullValue;	
				$.validator.messages["checkSelectCommon"] = "请选择"+name;
			return this.optional(element) || (value != ""  &&  value != nullValue);
		});
	// 敏感字符验证
	jQuery.validator.addMethod("sensitive",
		function(value, element) {
			return this.optional(element)
					|| commonUtil.checkInputChr(value);
		},
		"请不要输入特殊字符");	
	// 附件后缀校验
	jQuery.validator.addMethod("checkFileFormat",
		function(value, element, param) {
			return this.optional(element)
					|| commonUtil.checkFileFormat(value,param);
		},
		"文件格式必须为gif/jpg/png/rar/zip");	
		
	// 检查合作附件内容是否为空
	jQuery.validator.addMethod("checkFileNotEmpty",
		function(value, element, param) {
			jQuery.validator.messages["checkFileNotEmpty"] = "请上传附件！";
			if(jQuery("[name='"+param+"']").val()==""&&value.trim()==""){
				return false;
			}else{
				return true;
			}
		});		
		
var commonUtil = {
	//函数名：checkChinese
	//功能介绍：检查是否含有汉字
	//参数说明：要检查的字符串
	//返回值：0：含有 1：没有
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
	//函数名：checktextareaInput
	//功能介绍：检查是否含有非Input字符<,>
	//参数说明：要检查的字符串
	//返回值：false：含有 true：全部为可Input字符
	//add by liql 2004.12.31
	checktextareaInput : function (str)
	{
		var notareainput = "<>_";
	    var i;
	    for (i = 0; notareainput != null && i < notareainput.length; i++) {
	        if (str.indexOf(notareainput.charAt(i)) >= 0) {//若有
	          return false;
	        }
	      }
	    return true;
	
	},
	//函数名：checkInputChr
	//功能介绍：检查是否含有非Input字符
	//参数说明：要检查的字符串
	//返回值：false：含有 true：全部为可Input字符
	//add by renhj 2004.01.05
	checkInputChr : function (str)
	{
		var notinput = "\"'<>@#$%^&*()";
	    var i;
	    for (i = 0; notinput != null && i < notinput.length; i++) {
	        if (str.indexOf(notinput.charAt(i)) >= 0) {//若有
	          return false;
	        }
	      }
	    return true;
	
	},
	//函数名：checkLength
	//功能介绍：检查字符串的长度
	//参数说明：要检查的字符串
	//返回值：长度值
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
//身份验证类
var idCardNoUtil = {
	provinceAndCitys : {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	},

	powers : [ "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9",
			"10", "5", "8", "4", "2" ],

	parityBit : [ "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" ],

	genders : {
		male : "男",
		female : "女"
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
			return false;// 生日不能大于当前日期
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
		// 15位和18位身份证号码的基本校验
	var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
	if (!check)
		return false;
	// 判断长度为15位或18位
	if (idCardNo.length == 15) {
		return idCardNoUtil.check15IdCardNo(idCardNo);
	} else if (idCardNo.length == 18) {
		return idCardNoUtil.check18IdCardNo(idCardNo);
	} else {
		return false;
	}
},

// 校验15位的身份证号码
	check15IdCardNo : function(idCardNo) {
		// 15位身份证号码的基本校验
		var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/
				.test(idCardNo);
		if (!check)
			return false;
		// 校验地址码
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check)
			return false;
		var birDayCode = '19' + idCardNo.substring(6, 12);
		// 校验日期码
		return idCardNoUtil.checkBirthDayCode(birDayCode);
	},

	// 校验18位的身份证号码
	check18IdCardNo : function(idCardNo) {
		// 18位身份证号码的基本格式校验
		var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/
				.test(idCardNo);
		if (!check)
			return false;
		// 校验地址码
		var addressCode = idCardNo.substring(0, 6);
		check = idCardNoUtil.checkAddressCode(addressCode);
		if (!check)
			return false;
		// 校验日期码
		var birDayCode = idCardNo.substring(6, 14);
		check = idCardNoUtil.checkBirthDayCode(birDayCode);
		if (!check)
			return false;
		// 验证校检码
		return idCardNoUtil.checkParityBit(idCardNo);
	},

	formateDateCN : function(day) {
		var yyyy = day.substring(0, 4);
		var mm = day.substring(4, 6);
		var dd = day.substring(6);
		return yyyy + '-' + mm + '-' + dd;
	},

	// 获取信息
	getIdCardInfo : function(idCardNo) {
		var idCardInfo = {
			gender : "", // 性别
			birthday : "" // 出生日期(yyyy-mm-dd)
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
// 验证护照是否正确
function checknumber(number) {
	//var str = number;
	// 在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
	/*var Expression = /(P\d{7})|(G\d{8})/;
	var objExp = new RegExp(Expression);
	if (objExp.test(str) == true) {
		return true;
	} else {
		return false;
	}*/
	return number.length > 0 ? true : false;
};

