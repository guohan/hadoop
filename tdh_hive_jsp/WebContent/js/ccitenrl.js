
/******************************************************/
// 文件名:ccitenrl.js
// 功能:
// 作者:张学智 
// 日期:2009-5-10
// 版本:V1.0

// 修改记录
// 版本:V1.1
// 日期:2009-6-12
// 作者:曹伟
// 修改功能：1、支持NetWorldCA7.0控件需求，增加接口

// 修改记录
// 版本:V1.2
// 日期:2010-6-9
// 作者:赵辉
// 修改功能：1. 提供两种控件安装方式: CAB形式 或 EXE方式(只能选其中一种)
//	     2. 增加接口checkComSetuped检测控件是否安装接口
//	     3. 增加接口downloadSingleEx安装单证书(含一级和二级根证书)   ----注意：此接口为卓望定制接口
/******************************************************/

var localstr, rootpath, i;
var cabpath, cabversion;

cabversion = "2.0.0.6";

localstr = document.location.pathname;
rootpath = "/";
i = 1;
while ((i < localstr.length) && (localstr.charAt(i) != '/'))
    i ++;
if (localstr.charAt(i) == '/')
	rootpath = localstr.substring(0, i);

// -------------------------------------安装方式-------------------------------------

// -------------------------------------第一种-------------------------------------
// CAB包安装方式
if(typeof(UniContrl) != 'object')
{
	cabpath = "codebase=\"" + rootpath + "/sign/CMCAEnrl.cab#version=" + cabversion + "\"";
	document.write("<object classid=\"CLSID:1256C72B-2043-47FE-B328-4E2801C7BEA6\" " + cabpath + " id=\"UniContrl\">");
	document.write("</object>");
}
// -------------------------------------第二种-------------------------------------
// EXE安装方式
//if(typeof(UniContrl) != 'object')
//{
//	document.write("<object classid=\"CLSID:1256C72B-2043-47FE-B328-4E2801C7BEA6\" id=\"UniContrl\">");
//  document.write("</object>");
//}

//---------------------------------------------------------------------------------------------------------------

/********************************
第一部分：CA系统专用接口部分
***********************************/
var DT_UNENCODING = 0;
var DT_BASE64 = 1;

var KT_SIGNATURE = 0;
var KT_EXCHANGE = 1;

var KET_UNENCRYPT = 0;
var KET_ENCBYSIGNKEY = 1;

//
var Err_SaveSingle = 1000;
var Err_SaveDouble = 2000;
var Err_SaveSign = 2001;  // 签名证书保存失败
var Err_SaveEnc = 2002;   // 加密证书保存失败

var OLE_E_FIRST = 0x80040000;    // 错误码或操作

var ERR_HAVENOUSERDEVICE = 26;		//没有用户设备
var ERR_HAVENOMANAGERDEVICE = 27;		//没有管理员设备
var ERR_TOOMANYUSERDEVICE = 28;		//无法判断哪支是管理员设备,请确保您的电脑上只插有一支用户设备
var ERR_USERKEYINVALID = 29;	//用户设备无效
var ERR_ADMINKEYINVALID = 30;   //操作员设备无效

//错误返回代码
var ERR_CANCEL        =	25;
var ERR_LAST		  =	29;

var OLE_E_FIRST = 0x80040000;    // 错误码或操作

function dec2hex(decnum)
{
    var hexcode;
    var hexnum = "0123456789ABCDEF";
    var n;
    n = 0;
    hexcode = "";
    while (n < 16)
    {
    	i =decnum & 0x0000000F;
        hexcode = hexnum.substring(i, i + 1) + hexcode;
        decnum = decnum >> 4;
        n ++;
    }

    hexcode = "0x" + hexcode;
    return hexcode;
}

// 检测控件是否已安装
function checkComSetuped()
{
   try
   {
      UniContrl.test();
  
   }
   catch(ex)
   {
        alert("您没有安装客户端控件!");
  	return false;
   }
}




//下载证书换发
/*********************************
输入：packdata:服务器端发来的证书及加密私钥数据
			//transactionId:交易号
***********************************/
function downloadRenewalCert(packdata,transactionId)
{
    try{
    	var ret = 0;
        var signCert, encCert;
    	var encPriKey;
    	var start, end;
    	var pack;
    	var isSoftLib;
    	isSoftLib = isCCITSoftLib();
		// 软证书不支持,直接返回
    	if (isSoftLib) {
			return false;
		}

    	// 分解包,分解为两部分：签名证书 + "    " + 加密证书
    	pack = packdata;
    	start = 0;
    	end = pack.indexOf("    ", start);
		if (end <= 0)   // 单证书
		{
			//签名证书
			signCert = pack;
			UniContrl.keySpec = KT_SIGNATURE;
			UniContrl.saveCertWithMatchPriKey(signCert);  //安装签名证书
			return true;	
		}
		else   // 双证书
		{
	
			//签名证书
			signCert = pack.substring(start, end);
			UniContrl.keySpec = KT_SIGNATURE;
			UniContrl.saveCertWithMatchPriKey(signCert);  //安装签名证书

		
			//去掉字符串分隔符“    ”
			start = end + 4;
			encCert = pack.substring(start, pack.length);
		
			UniContrl.keySpec = KT_SIGNATURE;		
			ret = UniContrl.selectSignCert(signCert);   // 查找相匹配的签名证书,目的是为了 设置 容器索引
			if( ret )
			{
				return false;
			}
		
			//加密证书
			UniContrl.keySpec = KT_EXCHANGE;
			UniContrl.saveCertWithMatchPriKey(encCert); //安装加密证书
		
		}
		
    }
    catch(e){
        if (e.description != "")
        {
      		alert("证书换发发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
		alert("证书换发失败.");
        }
	    return false;
    }
	
    return true;
}

// 列举设备，显示到listctl中
/***************************
该函数使用了控件中的三个接口，枚举CSP、获取CSP描述、获取CSP数量
功能：将CSP描述列举到页面下拉列表中
******************************/
function enumProvider(listctl)
{
    try{
        var i;
        var element;
        while (listctl.length > 0)
        {
           listctl.remove(0);
        }

        for (i = 0; i < UniContrl.providersCount; i ++)
        {
            element = document.createElement("OPTION");

            element.value = UniContrl.enumProviders(i);
            element.text = UniContrl.getProvidersDesciption(element.value);
            listctl.add(element);
        }
        if (listctl.length > 0)
        {
            listctl.selectedIndex = 0;
        }
	if(listctl.length == 0)
	{
		element = document.createElement("OPTION");

            	element.value = -1;
            	element.text = "请安装控件";
            	listctl.add(element);         	
	}
    }catch(e)
    {
        if (e.description != "")
        {
            //alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
		alert("列举设备发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            //alert("错误码:" + dec2hex(e.number));
		alert("列举设备失败!");
        }
	    return false;
    }
    return true;
}

// 列举设备，显示到listctl中
function enumProvider2(listctl)
{
    try{
        var i;
        var element;
        while (listctl.length > 0)
        {
           listctl.remove(0);
        }

        for (i = 0; i < UniContrl.providersCount; i ++)
        {
        		if( UniContrl.enumProviders(i) != "")
						{
	            element = document.createElement("OPTION");

  	          element.value = UniContrl.enumProviders(i);
    	        element.text = UniContrl.getProvidersDesciption(element.value);
      	      listctl.add(element);
      	    }
        }
        if (listctl.length > 0)
        {
            listctl.selectedIndex = 0;
        }
    }catch(e)
    {
        if (e.description != "")
        {
      		alert("列举设备发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
		alert("枚举设备失败!");
        }
	    return false;
    }
    return true;
}
//初始化连接设备，设备名provName
//设置当前CSP,将页面CSP下拉列表中当前CSP设置为当前CSP
function initProv(provName)
{
    try{
        UniContrl.providerName = provName;
    }
    catch(e){
        if ((e.number & 0xF000) == 0x6000)
        {
            //alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "请确定USB Key已经正确插入，然后再重试！");
		alert("发生错误!\r\n请确定USB Key已经正确插入，然后再重试!");
		return false;
        }
        if (e.description != "")
        {
        	alert("初始化连接设备发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
      		alert("初始化连接设备失败.");
        }
	    return false;
    }
    return true;
}

/***********************
[in]provName CSP名称
		certData 管理员证书内容 --base64编码，本接口根据certData内容选择相应设备进行操作。
		UserType 0 用户  1 管理员
[retval]1--成功  其它失败
[comments]根据管理员证书内容区分管理员key和用户key,当UserType传入0时初将用户设备置为当前设备，
当UserType为1时置管理员设备为当前设备。调用本接口前不需要调用initProv接口
***************************************/
function initProvEx(provName, certData, UserType)
{
		var RetVal = 0;
    try{
        UniContrl.providerNameEx = provName;
        RetVal = UniContrl.SetCurrentDevice(certData, UserType);
	
    return RetVal;
    }
    catch(e){
    
		  var  L111 = e.number;
          var  L222 = OLE_E_FIRST | L111;
          var  ErrRet = OLE_E_FIRST ^ L222;
		  
        	return ErrRet;
    }
  
}

/***********************
[in]provName CSP名称
		certData 管理员证书内容 --base64编码，本接口根据certData内容选择相应设备进行操作。
		UserType 0 用户  1 管理员
[retval]1--成功  其它失败
[comments]根据管理员证书内容区分管理员key和用户key,当UserType传入0时初将用户设备置为当前设备，
当UserType为1时置管理员设备为当前设备。调用本接口前不需要调用initProv接口
***************************************/
function initProvExCheckKeyIsInvalid(provName, certData, UserType)
{
		var RetVal = 0;
    try{
        UniContrl.providerNameEx = provName;
        RetVal = UniContrl.SetCurrentDeviceEx(certData, UserType);
	
    return RetVal;
    }
    catch(e){
    
		  var  L111 = e.number;
          var  L222 = OLE_E_FIRST | L111;
          var  ErrRet = OLE_E_FIRST ^ L222;
		  
        	return ErrRet;
    }
  
}


/*
功能:利用管理员证书判断当前设备是否管理员设备
[in]signCert 管理证书
[return]RetVal 1当前设备是管理员设备
								0 当前设备不是管理员设备
*/
function IsManagerDevice(signCert)
{
	var RetVal = 0;
	try{
        RetVal = UniContrl.IsManagerDevice(signCert);
        return RetVal;
    }
    catch(e)
    {
        if (e.description != "")
        {
            alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            alert("错误码:" + dec2hex(e.number));
        }
        return 0;
    }
}



//生成密钥对，返回base64 编码公钥
/*
function createPubKey()
{
    try{
        	var pubkey;
        	UniContrl.keySpec = KT_SIGNATURE;
	 	pubkey = UniContrl.genRSAKeyPair(1024);
		if( (pubkey == "") || (pubkey == null) || ( typeof(pubkey) == "undefined" ) ) 
		{
			alert("生成密钥对失败");
			return "";
		}
        	return pubkey;
    }
    catch(e){
   	    if (e.description != "")
        {
      		alert("生成密钥对发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            //alert("错误码:" + dec2hex(e.number));
		alert("生成密钥对失败");
        }
	    return "";
    }
}
*/
/*
module: 1024   or 2048
*/
function createPubKey(module)
{
    try{
        	var pubkey;
        	UniContrl.keySpec = KT_SIGNATURE;
	 	pubkey = UniContrl.genRSAKeyPair(module);
		if( (pubkey == "") || (pubkey == null) || ( typeof(pubkey) == "undefined" ) ) 
		{
			alert("生成密钥对失败");
			return "";
		}
        	return pubkey;
    }
    catch(e){
   	    if (e.description != "")
        {
      		alert("生成密钥对发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            //alert("错误码:" + dec2hex(e.number));
		alert("生成密钥对失败");
        }
	    return "";
    }
}




//密钥恢复接口
function restoreKey(packdata, transactionId)
{
    try{
        var encCert;
        var encPriKey;
        var start, end;
        var pack;
        var isSoftLib;
    	isSoftLib = isCCITSoftLib();
		// 软证书不支持,直接返回
    	if (isSoftLib) {
			return false;
		}
       // isSoftLib = isCCITSoftLib();
        //if (isSoftLib)
        //    UniContrl.OnPreDownloadCert(transactionId);
    	// 分解包，分解为三部分
        pack = packdata;
        start = 0;
        end = pack.indexOf("    ", start);
        if (end <= 0)
	 	{
	 	alert("密钥恢复失败.");
            	return false;
        }
	 encCert = pack.substring(start, end);
        start = end + 4;
        encPriKey = pack.substring(start, pack.length);
 		
	
        UniContrl.keySpec = KT_EXCHANGE;
        //UniContrl.saveKeyPairEx(UniContrl.GetCertPubKey(encCert), encPriKey, KET_ENCBYSIGNKEY);
        //UniContrl.saveCertWithMatchPriKey(encCert);
          ret = UniContrl.saveCertAndPrivateKeyWithTempKeyEx(0, encCert, encPriKey, "CCITTempCont1");
		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
		}
    }
    catch(e){
        if (e.description != "")
        {
      		alert("密钥恢复发生错误!" + "\r\n" + "错误描述：" + e.description);
		
        }
        else
        {
		alert("密钥恢复失败.");
        }
        return false;
    }
    return true;
}
//单证书下载
/************************
输入：packdata：服务器端发来的证书数据
			transactionId :交易号

************************/
function downloadSingle(packdata, transactionId)
{
    try{
		var isSoftLib;
    	isSoftLib = isCCITSoftLib();

        UniContrl.keySpec = KT_SIGNATURE;
        if (isSoftLib){
        		onTransactionId(transactionId);
			UniContrl.OnPreDownloadCert(transactionId);
		}

		UniContrl.saveCertWithMatchPriKey(packdata);

		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
		}

    }
    catch(e){
        if (e.description != "")
        {
		alert("单证书下载发生错误!" + "\r\n" + "错误描述：" + e.description);		
        }
        else
        {
	  	alert("单证书下载失败.");
        }
	    return false;
    }
    return true;
}


//单证书下载,不需要
/************************
输入：packdata：服务器端发来的证书数据
			transactionId :
************************/
function downloadSingleCert(packdata)
{
    try{

        UniContrl.keySpec = KT_SIGNATURE;
      
	      UniContrl.saveCertWithMatchPriKey(packdata);

		
    }
    catch(e){
        if (e.description != "")
        {
		alert("单证书下载发生错误!" + "\r\n" + "错误描述：" + e.description);		
        }
        else
        {
	  	alert("单证书下载失败.");
        }
	    return false;
    }
    return true;
}


// 安装单证书(带根证书链)
function downloadSingleEx(packdata, transactionId)
{

// class6二级根证书
var class6Cert = "MIIDdzCCAl+gAwIBAgIRAN+YBayXboDfWre0ib4eRPEwDQYJKoZIhvcNAQEFBQAwdzELMAkGA1UEBhMCQ04xLzAtBgNVBAoMJuWNk+acm+aVsOeggeaKgOacryjmt7HlnLMp5pyJ6ZmQ5YWs5Y+4MTcwNQYDVQQDDC7ljZPmnJvmlbDnoIHmioDmnK8o5rex5ZyzKeaciemZkOWFrOWPuCBSb290IENBMB4XDTA3MTIzMTE3MDAwMFoXDTI4MDEwMTE1NTk1OVowgZsxCzAJBgNVBAYTAkNOMQ8wDQYDVQQIDAbmuZbljZcxDzANBgNVBAcMBumVv+aymTEvMC0GA1UECgwm5Y2T5pyb5pWw56CB5oqA5pyvKOa3seWcsynmnInpmZDlhazlj7gxOTA3BgNVBAMMMOWNk+acm+aVsOeggeaKgOacryjmt7HlnLMp5pyJ6ZmQ5YWs5Y+4IENsYXNzNiBDQTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAyhRyi1mMIIu80XW8ETdeHQPXoBeNbvL4mEJFd9RleNefR/ak0mCge5f+Yp+BbQ/4vvoHP9AulT1hOwCivBVa3kiL5m5wyFA5Q0K630r/QwL9Q88IIGluPxXga9tOd/Q/ORo7BGVXq1nkSc1/JHfAj1h+drHrQBVSaH6BmbxWgIkCAwEAAaNdMFswCwYDVR0PBAQDAgEGMAwGA1UdEwQFMAMBAf8wHQYDVR0OBBYEFNa9zkOw+VCAcbuDdb41PAafXm+LMB8GA1UdIwQYMBaAFNKyl3uhpEjLdT3t4EkrMFKQLXD9MA0GCSqGSIb3DQEBBQUAA4IBAQA1y6mw3l7dDlLiIlykAnUVy/ZOZcz5f2or5f4WCv1gYdALiK+RHMWR7/HpYpxUtWKE5ZW4mLrmSnfUGtS+sG+w6TYdHGS2MGtPiTiTaFW1nbbF+Kia1fDIItV6p3mw5fBFbDXKKT2pyrRr70tuVZxeh/6gQeKVWhZixVMzRtG111+CxZLeY76TWWY6H2joUcNO8xzsMaQvTh7JqD7KcJB7p/r4H5clK0TnZsbuzuZMcZLQAXgiGVnj/braQRT+Qw6BqJMIScFASbNExMPHBpDtX2+XjhGoLNCSNi6GW2ucHSFaONZQXIdI0bygRn1FYLGMGvpW1FACRzk8k2vXevxp$MIID1jCCAr6gAwIBAgIRAI3SCJMwSFDM7FQYRqoDXS8wDQYJKoZIhvcNAQEFBQAwdzELMAkGA1UEBhMCQ04xLzAtBgNVBAoMJuWNk+acm+aVsOeggeaKgOacryjmt7HlnLMp5pyJ6ZmQ5YWs5Y+4MTcwNQYDVQQDDC7ljZPmnJvmlbDnoIHmioDmnK8o5rex5ZyzKeaciemZkOWFrOWPuCBSb290IENBMB4XDTA3MTIzMTE2MDAwMFoXDTM3MTIzMTE2MDAwMFowdzELMAkGA1UEBhMCQ04xLzAtBgNVBAoMJuWNk+acm+aVsOeggeaKgOacryjmt7HlnLMp5pyJ6ZmQ5YWs5Y+4MTcwNQYDVQQDDC7ljZPmnJvmlbDnoIHmioDmnK8o5rex5ZyzKeaciemZkOWFrOWPuCBSb290IENBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA00HQTYgC6J8p9CPiWrBmrrLzmuJgweKMu7Y50IbcPCzytF2h1pNel2aTOpv5hlsrw7tTla/g/2LVU+mRfVWwdtq1CgAuyt41RIbbclqR4OWRdtmAl7oSPbsSrqaBYbqceVjUutyNS3pAdsdERA9uk59uRbCoO/Y6YolMimqT6o0ob0VRyw0oJi+DObd4uYkqo+a3y2OfqJ8GjmeVpSZ5WNTKaLNxJ50yuEwBvm9TjNVEPw00zIgincpRhc2oPtqe5Vvb7VwvFUkJ3US1yLXFsVkeqsM+XCTh49C0eB4LACAW6dG3muoo2+ItMS4/scpSesPk8B0W3UB5MMIX0ynwswIDAQABo10wWzALBgNVHQ8EBAMCAQYwDAYDVR0TBAUwAwEB/zAdBgNVHQ4EFgQU0rKXe6GkSMt1Pe3gSSswUpAtcP0wHwYDVR0jBBgwFoAU0rKXe6GkSMt1Pe3gSSswUpAtcP0wDQYJKoZIhvcNAQEFBQADggEBAJOp1I2OB0myb/GnmFqaZZ1aJVHRPFWLAMb/UXpdvEdE2vKi9aDCND/p8vKM8bV797Ot4XqNFMeZLUqQOoZTbMDJ6cyUcUIQfeXUrnJEqBXETuP0bwR283ccOnShPMKpNbj1wayB+XyDc3Ie+HZgmlFdGKn9PiuNU6i4LF9YdZuDL656Y5Catlq/M5v8od452Vxj/tQBXM36yzAd7FE2jD7c/FJqteZEEGcGEWibUNBU9r+vp+MYwVKn2o7qTPrwHivqctM7g7bdenFhsKTNPd3nY1nHZFfiqqpM/rr5JeswEQijN8wHZBisX1A9vKG/z0NEzerOKhAWgKuTxY2x9EU="

    try{
		var isSoftLib;
    	isSoftLib = isCCITSoftLib();

        UniContrl.keySpec = KT_SIGNATURE;
        if (isSoftLib){
        		onTransactionId(transactionId);
			UniContrl.OnPreDownloadCert(transactionId);
		}

		UniContrl.saveCertUserAndRootWithMatchPriKey( packdata, class6Cert );

		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);    


           
			UniContrl.OnDownloadCert(transactionId);
		}


    }
    catch(e){
        if (e.description != "")
        {
            alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            alert("错误码:" + dec2hex(E.number));
        }
	    return false;
    }
	
    return true;
}




//管理员证书下载
/************************
输入：packdata：服务器端发来的证书数据
************************/
function downloadManagerCert(packdata)
{
    try{

        UniContrl.keySpec = KT_SIGNATURE;
	 UniContrl.saveCertWithMatchPriKey(packdata);
    }
    catch(e){
        if (e.description != "")
        {
           	alert("管理员证书下载发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            	alert("管理员证书下载失败.");
        }
	    return false;
    }
    return true;
}
//双证书下载
/*********************************
输入：packdata:服务器端发来的证书及加密私钥数据
			transactionId:交易号
***********************************/
function downloadDouble(packdata, transactionId)
{
    try{
        var signCert, encCert;
    	var encPriKey;
    	var start, end;
    	var pack;
    	var isSoftLib;
    	isSoftLib = isCCITSoftLib();
    	if (isSoftLib) {
    			onTransactionId(transactionId);
			UniContrl.OnPreDownloadCert(transactionId);
		}

    	// 分解包，分解为三部分
    	pack = packdata;
    	start = 0;
    	end = pack.indexOf("    ", start);
    	if (end <= 0)
			return false;
		signCert = pack.substring(start, end);
		start = end + 4;
		end = pack.indexOf("    ", start);
	    if (end <= 0)
	    {
		alert("双证书下载失败.");
	    	return false;
	    }
		encCert = pack.substring(start, end);
		start = end + 4;
		encPriKey = pack.substring(start, pack.length);

		UniContrl.keySpec = KT_SIGNATURE;
		UniContrl.saveCertWithMatchPriKey(signCert);
		if (isSoftLib)
		{
			UniContrl.SetPrompt(0);
			UniContrl.OnDownloadCert(transactionId);
		}

		UniContrl.keySpec = KT_EXCHANGE;
		UniContrl.saveKeyPairEX(UniContrl.getCertPubKey(encCert), encPriKey, KET_ENCBYSIGNKEY);

		UniContrl.saveCertWithMatchPriKey(encCert);
		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
		}
    }
    catch(e){
        if (e.description != "")
        {
      		alert("双证书下载发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
		alert("双证书下载失败.");
        }
	    return false;
    }
    return true;
}

function signature(srcData)
{
    try{
		var signdata="";
		UniContrl.keySpec = KT_SIGNATURE;
		signdata = UniContrl.signature(srcData, DT_UNENCODING, "");
		if( (signdata == "") || (signdata == null) || ( typeof(signdata) == "undefined" ) ) 
		{
			alert("数字签名失败.");
			return "";
		}
  
		return signdata;
    }
    catch(e){
        if (e.description != "")
        {
		
		var  L111 = e.number;
          var  L222 = OLE_E_FIRST | L111;
          var  L333 = OLE_E_FIRST ^ L222;
		if( ERR_CANCEL == L333 )
		{
			return	"";
		}
		

		alert("数字签名发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
		alert("数字签名失败.");
        }
        return "";
    }
}

function verify(srcData, signData, pubKey)
{
	try{
		var pass;
		UniContrl.keySpec = KT_SIGNATURE;
		pass = UniContrl.verify(srcData, DT_UNENCODING, pubKey, signData);
		if( (pass == false) || (pass == null) || ( typeof(pass) == "undefined" ) ) 
		{
			alert("验证签名失败.");
			return false;
		}
        	
		return pass;
    }
    catch(e){
        if (e.description != "")
        {
		var  L111 = e.number;
          var  L222 = OLE_E_FIRST | L111;
          var  L333 = OLE_E_FIRST ^ L222;
		if( ERR_CANCEL == L333 )
		{
			return	"";
		}

		alert("验证签名发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
          	alert("验证签名失败.");
        }
        return false;
    }
}

function encryptWithCert(srcData, cert)
{
    try{
        var encData;
        encData = UniContrl.rsaEncrypt(srcData,DT_UNENCODING, UniContrl.GetCertPubKey(cert));//UniContrl.getCertPubKey(cert)
	if( (encData == "") || (encData == null) || ( typeof(encData) == "undefined" ) ) 
	{
		alert("证书RSA加密失败.");
		return "";
	}
   
        return encData;
    }
    catch(e){
        if (e.description != "")
        {
		var  L111 = e.number;
          var  L222 = OLE_E_FIRST | L111;
          var  L333 = OLE_E_FIRST ^ L222;
		if( ERR_CANCEL == L333 )
		{
			return	"";
		}


          	alert("证书RSA加密发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            	alert("证书RSA加密失败.");
        }
        return "";
    }
}


function isCCITSoftLib()
{
	if (UniContrl.needPartner == 0)
		return false;
	else
		return true;

}


function onTransactionId(transactionId)
{
	try{
		if (isCCITSoftLib())
			UniContrl.OnApplyCert(transactionId);
	}catch(e){
		return false;
	}
}


function getSerialNumber()
{
    try{
        var cert;
        var certSN;
        UniContrl.keySpec = KT_SIGNATURE;
        cert = UniContrl.readCert();
        certSN = UniContrl.getCertSN(cert);
	if( (certSN == "") || (certSN == null) || ( typeof(certSN) == "undefined" ) ) 
	{
		alert("获取证书序列号失败.");
		return "";
	}
        return certSN;
    }catch(e){
    	if (e.description != "")
        {
          var  L111 = e.number;
          var  L222 = OLE_E_FIRST | L111;
          var  L333 = OLE_E_FIRST ^ L222;
		if( ERR_CANCEL == L333 )
		{
			return	"";
		}

           	alert("获取证书序列号发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
              alert("获取证书序列号失败.");
        }
        return "";
	}
}


function getUserName()
{
	var userName = 0;
	userName = UniContrl.GetUserName();
	return userName;
}

function getCert()
{
     try{
        var cert;
        UniContrl.keySpec = KT_SIGNATURE;
        cert = UniContrl.readCert();
	if( (cert == "") || (cert == null) || ( typeof(cert) == "undefined" ) ) 
	{
		alert("获取证书失败.");
		return "";
	}
        return cert;
    }catch(e){
    	if (e.description != "")
        {
		if( ERR_CANCEL == e.number )
		{
			return "";
		}

            	alert("获取证书发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            	alert("获取证书失败.");
        }
        return "";
	}
}

function saveCert(cert)
{
	try{       
        	UniContrl.saveCert(cert);
		return true;
    	}catch(e){
    		if (e.description != "")
        	{
            		alert("保存证书发生错误!" + "\r\n" + "错误描述：" + e.description);
        	}
        	else
        	{
            		alert("保存证书失败.");
        	}
        	return false;
	}
}

function getSignCert()
{
	try{  
		var signCert;
		signCert = UniContrl.getSignCert();
		if( (signCert == "") || (signCert == null) || ( typeof(signCert) == "undefined" ) ) 
		{
			alert("获取签名证书失败.");
			return "";
		}   
        	return signCert;
    	}catch(e){
    		if (e.description != "")
        	{
            		alert("获取签名证书发生错误!" + "\r\n" + "错误描述：" + e.description);
        	}
        	else
        	{
            		alert("获取签名证书失败.");
        	}
        	return "";
	}
	
}
function getAuditopMsg(id,type)
{
	
	var signdata=null;
	var opMsg=null;
	
	if( type==0){
	 	opMsg="ApplyID="+id;
	}
	if (type==1){
		opMsg="ApplyID=0";
	}
	if (type==2){
		opMsg="CertNum="+id;
	}
	return opMsg;
 
}

function auditSignature(cryptProv,srcData)
{
	
	var signdata=null;
     	var opMsg=null;
     
    	try{
        
	    	if(!initProv(cryptProv))
		{
	    		return "";	
	   	}
			
	     	signdata = signature(srcData);
		return signdata;
    	   }
    catch(e){
        	if (e.description != "")
        	{
      			alert("数字签名发生错误!" + "\r\n" + "错误描述：" + e.description);
        	}
        	else
        	{
			alert("数字签名失败.");
        	}
        	return "";
    	}
}

function CloseClientWindow()
{
	UniContrl.CloseWindow();
}

function unblockUKPin(puk,newPin)
{
	var ret;
	ret = UniContrl.blockUKPin(puk,newPin);
	return ret;
}


function changeUKPin(oldPin,newPin)
{
	var ret;
	ret = UniContrl.changePin(oldPin,newPin);
	return ret;
}


function ChangeAdminPin(oldPin,newPin)
{
	var ret;
	ret = UniContrl.ChangeAdminPin(oldPin,newPin);
	return ret;
}


function saveCert(certData)
{
	var ret;
	ret = UniContrl.saveCert(certData);
	return ret;
}
function readDeviceCert()
{
	var ret;
	ret = UniContrl.readDeviceCert();
	return ret;
}

function CheckUKey(apppened)
{
	return 1;
}


function Check_UKey(provName, apppened)
{
	return 1;
}

function getCertPubKey(certData)
{
	var pubKey;
	try
	{
		pubKey = UniContrl.getCertPubKey(certData);
		if( (pubKey == "") || (pubKey == null) || ( typeof(pubKey) == "undefined" ) ) 
		{
			alert("获取证书公钥失败.");
			return "";
		}
        	return pubKey;
    	}
    	catch(e)
    	{
        	if (e.description != "")
        	{
			alert("获取证书公钥发生错误!" + "\r\n" + "错误描述：" + e.description);
        	}
        	else
        	{
      			alert("获取证书公钥失败.");
        	}
         	return "";
    	}
}


function getCertIssuer(certData)
{
	var certIssuer;
	try{
		certIssuer = UniContrl.getCertIssuer(certData);
		if( (certIssuer == "") || (certIssuer == null) || ( typeof(certIssuer) == "undefined" ) ) 
		{
			return "";
		}
        	return certIssuer;
		
    	}
    	catch(e)
    	{
        	if (e.description != "")
        	{
            		alert("获取证书颁发者发生错误!" + "\r\n" + "错误描述：" + e.description);
        	}
        	else
        	{
    			alert("获取证书颁发者失败.");
        	}
         return "";
    }
}


function getCertSubject(certData)
{
	var certSubject;
	try{
		certSubject = UniContrl.getCertSubject(certData);
		if( (certSubject == "") || (certSubject == null) || ( typeof(certSubject) == "undefined" ) ) 
		{
			return "";
		}
        	return certSubject;
    }
    catch(e)
    {
        if (e.description != "")
        {
            	alert("获取证书主题项发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
          	alert("获取证书主题项失败.");
        }
         return "";
    }
}


function getCertNotBefore(certData)
{
	var certNotBefore;
	try{
		certNotBefore = UniContrl.getCertNotBefore(certData);
    }
    catch(e)
    {
        if (e.description != "")
        {
           alert("获取证书有效起始日期发生错误!" + "\r\n" + "错误描述：" + e.description);

        }
        else
        {
        	alert("获取证书有效起始日期失败.");
        }
         return "";
    }  
	return certNotBefore;
}


function getCertNotAfter(certData)
{
	var certNotAfter;
	try{
		certNotAfter = UniContrl.getCertNotAfter(certData);
    }
    catch(e)
    {
        if (e.description != "")
        {
            	alert("获取证书有效终止日期发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
      		alert("获取证书有效终止日期失败.");
        }
         return "";
    }
	return certNotAfter;
}

function getCertSN(certData)
{
	var certSN;
	try{
		certSN = UniContrl.getCertSN(certData);
    }
    catch(e)
    {
        if (e.description != "")
        {
           	alert("获取证书序列号发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            	alert("获取证书序列号失败.");
        }
         return "";
    }
	return certSN;
}


function getCertExtension(certData, extensionID)
{
	var certExtension;
	try{
		certExtension = UniContrl.getCertExtension(certData, extensionID);
    }
    catch(e)
    {
        if (e.description != "")
        {
           	alert("获取证书扩展项信息发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
      		alert("获取证书扩展项信息失败.");
        }
         return "";
    }
	return certExtension;
}


function symEncrypt(symAlg, srcData, keyData)
{
	var ret;
	try
    {
        UniContrl.cryptAlgorithm = symAlg;
	      ret = UniContrl.encrypt(srcData, DT_UNENCODING, keyData, DT_UNENCODING);
    }catch(e){
        if (e.description != "")
        {
      		alert("对称加密发生错误！" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
      		alert("对称加密失败.");
        }
         return "";
    }
	return ret;
}


function symDecrypt(symAlg, encData, keyData)
{
	var ret;
	try
    {
        UniContrl.cryptAlgorithm = symAlg;
	      ret = UniContrl.decrypt(encData, keyData, DT_UNENCODING, DT_UNENCODING);
	  }catch(e){
        if (e.description != "")
        {
           	alert("对称解密发生错误！" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
		alert("对称解密失败.");
        }
         return "";
    }
	return ret;
}

function RSAEncrypt(srcData, KeySpec)
{
	try{
		var ret;
		UniContrl.keySpec = KeySpec;
		ret = UniContrl.rsaEncrypt(srcData, DT_UNENCODING, "");
		return ret;
	}catch(e)
	{
			if (e.description != "")
			{
				var  L111 = e.number;
          			var  L222 = OLE_E_FIRST | L111;
          			var  L333 = OLE_E_FIRST ^ L222;
				if( ERR_CANCEL == L333 )
				{
					return	"";
				}

				alert("RSA加密发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("RSA加密失败.");
			}
			return "";
	}
}

function RSADecrypt(encdata, KeySpec)
{
	try{
		var ret;
		UniContrl.keySpec = KeySpec;
		ret = UniContrl.rsaDecrypt(encdata, "", DT_UNENCODING);
		return ret;
	}catch(e)
	{
			if (e.description != "")
			{
				var  L111 = e.number;
          			var  L222 = OLE_E_FIRST | L111;
          			var  L333 = OLE_E_FIRST ^ L222;
				if( ERR_CANCEL == L333 )
				{
					return	"";
				}


				alert("RSA解密发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("RSA解密失败.");
			}
			return "";
	}
}


function setoperatorflag(operatorCert)
{
	try{
		var ret;		
		ret = UniContrl.setOperatorFlag(operatorCert);
		return ret;
	}catch(e)
	{
			if (e.description != "")
			{
				alert("发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("检验证书失败.");
			}
			return "";
	}
}


function setoperatorswitch (switchflag)
{
	try{
		var ret;		
		ret = UniContrl.setOperatorSwitch(switchflag);
		return ret;
	}catch(e)
	{
			if (e.description != "")
			{
				alert(e.description);
			}
			else
			{
				alert("Error");
			}
			return "";
	}
}


function signatureWithAlg(srcData,tpe)
{
	try{
		var signdata="";
		UniContrl.keySpec = KT_SIGNATURE;
		signdata = UniContrl.signatureWithAlg(srcData, DT_UNENCODING, "", tpe);
		return signdata;
	}catch(e)
	{
			if (e.description != "")
			{
				
				var  L111 = e.number;
          			var  L222 = OLE_E_FIRST | L111;
          			var  L333 = OLE_E_FIRST ^ L222;
				if( ERR_CANCEL == L333 )
				{
					return	"";
				}
				
				alert("多算法数字签名发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("多算法数字签名失败.");
			}
			return "";
	}
}


/*
[in]modulus:目前为1024  
	  randStr：随机数
[return]:成功返回公钥
          失败返回空串
*/
function GenTempRsaKeyPair()
{
    try{
    var pubkey;
		UniContrl.keySpec = KT_SIGNATURE;
		pubkey = UniContrl.genTempRSAKeyPair(1024, "CCITTempCont1");
    return pubkey;
    }
    catch(e){
   	    if (e.description != "")
        {
      		alert("生成公钥和POP项发生错误！" + "\r\n" + "错误描述：" + e.description);

        }
        else
        {
		alert("生成公钥和POP项失败.");
        }
	    return "";
    }
}

//功能：产生指定类型(签名或加密)和模式的临时RSA密钥对，输出相应的临时公钥
function GenTempRsaKeyPairWithKeySpec(KeySpec, modulus)
{
    try{
    var pubkey;
		UniContrl.keySpec = KeySpec;
		pubkey = UniContrl.genTempRSAKeyPair(modulus, "CCITTempCont1");
    return pubkey;
    }
    catch(e){
   	    if (e.description != "")
        {
      		alert("生成公钥和POP项发生错误！" + "\r\n" + "错误描述：" + e.description);

        }
        else
        {
		alert("生成公钥和POP项失败.");
        }
	    return "";
    }
}


//删除临时密钥对
/*
[return] 0 --成功 其它失败
*/
function DelTempRsaKeyPair()
{
    try
    {
    var RetVal = -1;
		RetVal = UniContrl.deleteTempRSAKeyPair("CCITTempCont1");
		return RetVal;
   }
    catch(e){
   	    	if (e.description != "")
        	{
      			alert("删除临时密钥对发生错误！" + "\r\n" + "错误描述：" + e.description);
        	}
        	else
        	{
			alert("删除临时密钥对失败.");
        	}
	    return -1;
    }
}


// 证书代办
 /*
[in]CertData :待保存的数据
		ContainerName：临时容器名
[return]:0--成功，其它失败
*/
function saveCertAndPrivateKeyWithTempKey(certAndPriKeyPackdata, transactionId)
{
	try{
	  	var ret = 0;
		var retSign = 0;
		var retEnc = 0;	
		var signCertAndPriKey;
		var encCertAndPriKey;
   		var start, end;
    		var pack;
				
		var isSoftLib;
	    	isSoftLib = isCCITSoftLib();

    
    // 分解包，分解为两部分
    pack = certAndPriKeyPackdata;
    
    //签名证书和签名证书私钥
    start = 0;
    end = pack.indexOf("    ", start);
    if (end <= 0)   // 单证书
    {
    		UniContrl.keySpec = KT_SIGNATURE;
		ret = UniContrl.saveCertAndPrivateKeyWithTempKey(certAndPriKeyPackdata, "CCITTempCont1");
		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
		}
		
		if( 0 != ret )
		{
			alert("证书保存失败.");
			
		}
		
		return ret;

    }
    else   //双证书
    {
    
		signCertAndPriKey = pack.substring(start, end);
				
		//加密证书和加密证书私钥
		start = end + 4;
		encCertAndPriKey = pack.substring(start, pack.length);
				
		UniContrl.keySpec = KT_SIGNATURE;
		ret = UniContrl.saveCertAndPrivateKeyWithTempKey(signCertAndPriKey, "CCITTempCont1");
		if (isSoftLib)
		{
			UniContrl.SetPrompt(0);
			UniContrl.OnDownloadCert(transactionId);
		}

		if( 0 != ret )
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
			alert("证书保存失败.");
			return ret;
		}

		UniContrl.keySpec = KT_EXCHANGE;
		ret = UniContrl.saveCertAndPrivateKeyWithTempKey(encCertAndPriKey, "CCITTempCont1");

		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
		}
		
		if( 0 != ret )
		{
			alert("证书保存失败.");
			
		}

		return ret;
		
	}  // End  if (end <= 0) 
}
catch(e)
	{
			if (e.description != "")
			{
				var  L111 = e.number;
          			var  L222 = OLE_E_FIRST | L111;
          			var  L333 = OLE_E_FIRST ^ L222;
				if( ERR_CANCEL == L333 )
				{
					return	-1;
				}

				alert("保存证书发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			return -1;
	}

}

//功能：产生指定类型(签名或加密)的RSA密钥对，输出相应的公钥
function genRSAKeyPair(keySpec, modulus)
{
    try{
        	var pubkey;
        	UniContrl.keySpec = keySpec; // 密钥类型  //KT_SIGNATURE 或 KT_EXCHANGE;
	 		UniContrl.keymode = modulus;
	 	pubkey = UniContrl.genRSAKeyPair(modulus);   // 1024  或 2048
		if( (pubkey == "") || (pubkey == null) || ( typeof(pubkey) == "undefined" ) ) 
		{
			alert("生成密钥对失败");
			return "";
		}
        	return pubkey;
    }
    catch(e){
   	    if (e.description != "")
        {
      		alert("生成密钥对发生错误!" + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            //alert("错误码:" + dec2hex(e.number));
		alert("生成密钥对失败");
        }
	    return "";
    }
}


/*
[in]modulus:目前为1024  
	  randStr：随机数
[return]:成功返回公钥+POP 失败返回空串
*/
function genRSAKeyPairPOP(modulus,randStr)
{
	try{
		var ret;	
		UniContrl.keySpec = KT_SIGNATURE;
		ret = UniContrl.genRSAKeyPairPOP(modulus, randStr);
		if( (ret == "") || (ret == null) || ( typeof(ret) == "undefined" ) ) 
		{
			alert("获取公钥和POP项失败.");
			return "";
		}
        	return ret;
	}catch(e)
	{
			if (e.description != "")
			{
				
				var  L111 = e.number;
          			var  L222 = OLE_E_FIRST | L111;
          			var  L333 = OLE_E_FIRST ^ L222;
				if( ERR_CANCEL == L333 )
				{
					return	"";
				}
				
				alert("获取公钥和POP项发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("获取公钥和POP项失败.");
			}
			return "";
	}
}

//选择签名证书，将签名证书所在容器置为当前容器。
function selectSignCert(signCert)
{
	try{
		var ret = 0;
		UniContrl.keySpec = KT_SIGNATURE;		
		ret = UniContrl.selectSignCert(signCert);
		return ret;
	}catch(e)
	{
			if (e.description != "")
			{
				alert("选择签名证书发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("选择签名证书失败.");
			}
			return -1;
	}
}

//保存加密证书(单变双)
function saveCertAndPrivateKeyWithSignKey(packdata, transactionId)
{
	try{

	var  encCert;
    	var encPriKey;
    	var start, end;
    	var pack;
    	var isSoftLib;

    	isSoftLib = isCCITSoftLib();

    	if (isSoftLib) 
		{
    	 	onTransactionId(transactionId);
			UniContrl.OnPreDownloadCert(transactionId);
		}

    	// 分解包，分解为三部分
    	pack = packdata;
    	start = 0;
    	end = pack.indexOf("    ", start);
    	if (end <= 0)
	{
		alert("保存证书失败.");
		return false;
	}
	encCert = pack.substring(start, end);

	start = end + 4;
	encPriKey = pack.substring(start, pack.length);


	UniContrl.keySpec = KT_EXCHANGE;
	UniContrl.saveKeyPairEx(UniContrl.getCertPubKey(encCert), encPriKey, KET_ENCBYSIGNKEY);

	UniContrl.saveCertWithMatchPriKey(encCert);

		if (isSoftLib)
		{
			UniContrl.SetPrompt(1);
			UniContrl.OnDownloadCert(transactionId);
		}
	
	}catch(e)
	{
			if (e.description != "")
			{
				alert("保存证书发生错误！" + "\r\n" + "错误描述：" + e.description);
			}
			else
			{
				alert("保存证书失败.");
			}
			return false;
	}
	
	return true;
}

//判断是否插入Key
function ISUKConnectbak()
{ 
	return 1;
	     
}
//判断是否插入Key
function ISUKConnect()
{ 
	var ret = 0;		
	      try
	      {
	      	ret = UniContrl.ISUKConnect();
	      	
	      	 return ret;    
	      }
	      catch(e)
	      {
		        if (e.description != "")
		        {
		            //alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
				alert("判断是否插入Key发生错误！" + "\r\n" + "错误描述：" + e.description);
		        }
		        else
		        {
		            alert("判断是否插入Key失败.");
		        }
		        return 0;
	      }
	     
}



/*
功能:利用管理员证书判断当前设备是否管理员设备
[in]signCert 管理证书
[return]RetVal 1当前设备是管理员设备
								0 当前设备不是管理员设备
*/
function IsManagerDevice(signCert)
{
	var RetVal = 0;
	try{
        RetVal = UniContrl.IsManagerDevice(signCert);
        return RetVal;
    }
    catch(e)
    {
        if (e.description != "")
        {
            alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            alert("错误码:" + dec2hex(e.number));
        }
        return 0;
    }
}

/***********************
[in]provName CSP名称
		certData 管理员证书内容 --base64编码，本接口根据certData内容选择相应设备进行操作。
		UserType 0 用户  1 管理员
[retval]1--成功  其它失败
[comments]根据管理员证书内容区分管理员key和用户key,当UserType传入0时初将用户设备置为当前设备，
当UserType为1时置管理员设备为当前设备。调用本接口前不需要调用initProv接口
***************************************/
function SetCurrentDevice(provName, certData, UserType)
{
		var RetVal = 0;
    try{
        UniContrl.providerNameEx = provName;
        RetVal = UniContrl.SetCurrentDevice(certData, UserType);
	if( 1 == RetVal)
	{
		alert("成功设置");	
	}
        return RetVal;
    }
    catch(e){
        if (e.description != "")
        {
            alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            alert("错误码:" + dec2hex(e.number));
        }
	    return false;
    }
    return true;
}
/***********************
查看证书
***************************************/
 function ViewCertInfo(cert)
{
   try{

  	UniContrl.ViewCertInfo(cert);
	return true;
   }catch(e)
   {
	if (e.description != "")
	{
		alert("查看证书信息发生错误！" + "\r\n" + "错误描述：" + e.description);
	}
	else
	{
		alert("查看证书信息失败.");
	}
	return false;
   }

}

/**************************
功能：对数据进行 SHA1 hash
输入:
	[in]srcData 待hash数据(base64编码)
返回值: 
	hash值(base64编码)
	
说明: HASH摘要算法为SHA1	
*****************/
function HashData(srcData)
{
   try{
		var ret; 
		
  		ret = UniContrl.hashData(srcData);
		alert("aaa:---" + ret);
		return ret;
	
   }catch(e) {
   	
	if (e.description != "")
	{
		alert("获取摘要失败!" + "\r\n" + "错误描述：" + e.description);
	}
	else
	{
		alert("获取摘要失败.");
	}
	
		return "";
   }

}


/**************************
功能：对文件内容作 SHA1 hash
输入:
	[in]filePath 待hash文件存放路径
返回值: 
	hash值(base64编码)
	
说明: HASH摘要算法为SHA1	
*****************/
function HashFile(filePath)
{
   try{
		var ret; 
		
  		ret = UniContrl.hashFile(filePath);
		
		return ret;
	
   }catch(e) {
   	
	if (e.description != "")
	{
		alert("获取摘要失败!" + "\r\n" + "错误描述：" + e.description);
	}
	else
	{
		alert("获取摘要失败.");
	}
	
		return "";
   }

}

function downP12Cert(cerdata)
{
    try{
		 UniContrl.saveP12cert(cerdata);
    }
    catch(e){
        if (e.description != "")
        {
            alert("发生错误！\r\n错误码:" + dec2hex(e.number) + "\r\n" + "错误描述：" + e.description);
        }
        else
        {
            alert("错误码:" + dec2hex(E.number));
        }
	    return false;
    }
    return true;
}
