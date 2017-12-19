/*!
 * 应用版本升级
 * @author tianbei
 */
 jQuery(document).ready(function(){
	jQuery( "#dialog:ui-dialog" ).dialog( "destroy" );
	jQuery( "#uidUniqueness_div" ).dialog({
		modal: true,
		autoOpen: false,
		width:"350px",
		bgiframe: true//解决IE下select框在遮罩层上的问题

	});
	
});
 
 //展示包信息
 function showPackageInfo(serverData,platform){
 	jQuery("#packageInfoTr").hide();
	var clientId = eval('('+serverData+')')[0].clientId; var version = eval('('+serverData+')')[0].version;
	var versionName = eval('('+serverData+')')[0].versionName;
	if(clientId!="null"&&clientId!=undefined){
		if(platform=="9"||platform=="3"){//android or ophone
			jQuery("#packageInfo").html("<span style='width:100px;'>Package name</span>: "+clientId+"<br/>"+"<span style='width:100px;'>Version</span>: "+version+"<br/>"+"<span style='width:100px;'>Version name</span>: "+versionName);
			jQuery("#packageInfoTr").show();
		}else if(platform=="1"){//s60
			jQuery("#packageInfo").html("<span style='width:100px;'>SISUid</span>: "+clientId+"<br/>"+"<span style='width:100px;'>SISVersion</span>: "+version);
			jQuery("#packageInfoTr").show();
		}else if(platform=="2"){//wm
			jQuery("#packageInfo").html("<span style='width:100px;'>AppName</span>: "+clientId);
			jQuery("#packageInfoTr").show(); 
		}
	}
}
/**
//展示提取的机型信息
function showDeviceInfo(serverData,platform){
	var series = eval('('+serverData+')')[0].series; var devicedesc = eval('('+serverData+')')[0].devicedesc;
	var feature = eval('('+serverData+')')[0].feature; var initialProgram = eval('('+serverData+')')[0].initialProgram;
 	jQuery("[name='series1']").val(series); jQuery("[name='terminalSupport1']").val(devicedesc);
    jQuery("[name='feature1']").val(feature); jQuery("#initialProgram").val(initialProgram);
    jQuery("[name='platformSelect1']").val(platform);
    if(devicedesc!=""&&devicedesc!=undefined){ $("#terminalSupportEr").html("<br/><span style='color:#3184d1;'>系统根据您的程序包类型，已自动勾选适配的机型。</span><br/><span style='color:#3184d1;'>点击“选择”按钮进行补充和修改</span>");}
}
*/
//展示提取的机型信息
function showDeviceInfo(serverData,id,idOrEmpty){
	/**选机型流程
	var series = eval('('+serverData+')')[0].series; var devicedesc = eval('('+serverData+')')[0].devicedesc;
	var feature = eval('('+serverData+')')[0].feature; var initialProgram = eval('('+serverData+')')[0].initialProgram;
    jQuery("[name='series"+id+"']").val(series); jQuery("[name='terminalSupport"+id+"']").val(devicedesc);
    jQuery("[name='feature"+id+"']").val(feature); jQuery("#initialProgram"+idOrEmpty).val(initialProgram);
    if(devicedesc!=""&&devicedesc!=undefined){
    	jQuery("[name='platformSelect"+id+"']").val(jQuery("#platform"+idOrEmpty).val());
    	$("#terminalSupportEr"+idOrEmpty).html("<br/><span style='color:#3184d1;'>系统根据您的程序包类型，已自动勾选适配的机型。</span><br/><span style='color:#3184d1;'>点击“选择”按钮进行补充和修改</span>");
	}
	*/
	var initialProgram = eval('('+serverData+')')[0].initialProgram;
	jQuery("#initialProgram"+idOrEmpty).val(initialProgram);
	//选系列流程
	var seriesNames = eval('('+serverData+')')[0].seriesNames;
	if(seriesNames!=""&&seriesNames!=undefined){
		var series = seriesNames.split("_");
		//新增程序包
		if(jQuery("[name='seriesName']")[0]){
			jQuery("[name='seriesName']").each(function(){jQuery(this).attr("checked",false);})
			jQuery("[name='seriesName']").each(function(){
				for(var j=0;j<series.length;j++){
					if(jQuery(this).val()==series[j]){
						jQuery(this).attr("checked",true);
					}
				}
				
			})
		}
		//应用发布
		if(jQuery("[name='SeriesName"+id+"']")[0]){
			jQuery("[name='SeriesName"+id+"']").each(function(){jQuery(this).attr("checked",false);})
			jQuery("[name='SeriesName"+id+"']").each(function(){
				for(var j=0;j<series.length;j++){
					if(jQuery(this).val()==series[j]){
						jQuery(this).attr("checked",true);
					}
				}
				
			})
		}
	}
	
}

//检查唯一性并提示相关信息
//platform暂时不用，后期可兼容其他平台的提示
function uidUniqueness(serverData,platform,id){
	
	var isMine = eval('('+serverData+')')[0].isMine; var contentName = eval('('+serverData+')')[0].contentName;
	var contentId = eval('('+serverData+')')[0].contentId;
	if(isMine==true){//同一ap重复
		//alert("同一ap重复,contentName = "+contentName+",contentId = "+contentId);
		jQuery("#uidUniqueness_div").html(
			'<div style="text-align:center;color:red;">抱歉，上传失败</div>'
			+'<div style="">重复Package Name的应用为 ：<font style="color:red;">'+contentName+'</font> <br/>应用ID为 ：<font style="color:red;">'+contentId+'</font></div>'
			//+'<div style=""><a href="http://dev.10086.cn" target="_blank">点击查看应用地址</a>（超链MM对应的应用）</div>'
			+'<div style="margin-top:20px;">请选择为该应用进行版本升级或在修改后重新提交。</div>'
			+'<div style="">注：版本升级的程序包版本不得小于前一程序包。</div>'
		);
		//显示遮罩层
		jQuery("#uidUniqueness_div").dialog("open");  
		jQuery("#uidUniquenessFlag"+id).val("false");
	}else if(isMine==false){//不同ap重复
		jQuery("#uidUniqueness_div").html(
			'<div style="text-align:center;color:red;">抱歉，上传失败</div>'
			+'<div style="">重复Package Name的应用为 ：<font style="color:red;">'+contentName+'</font> <br/>应用ID为 ：<font style="color:red;">'+contentId+'</font></div>'
			//+'<div style=""><a href="http://dev.10086.cn" target="_blank">点击查看应用地址</a>（超链MM对应的应用）</div>'
			+'<div style="margin-top:20px;">请审核或在修改后重新提交。</div>'
			+'<div style="">如您的应用遭到他人盗用，请进入申诉通道进行申诉。</div>'
			+'<div style="color:red;margin-top:20px;">请确认您是该产品的版权所有者，恶意申诉所造成的一切法律后果将由申诉人承担。</div>'
			+'<div style="margin-top:20px;">请联系我们的客服，并将所有能证明您实为该应用版权所有者的资料邮件发送至客服邮箱</div>'
			+'<div style="margin-top:20px;">客服联系方式：400-10086-20</div>'
			+'<div style="">客服邮箱：apservice@chinamobile.com</div>'
			+'<div style="">飞信: 855552270</div>'
			+'<div style="margin-top:20px;">邮件格式请按照如下格式：</div>'
			+'<div style="">标题：申诉邮件——《应用名称》</div>'
			+'<div style="">附件：证明材料，应用程序包</div>'
			+'<div style="">邮件内容：xxxxxxxxxxx</div>'
			+'<div style="">邮件内容请附上您的详细联系方式</div>'
			
		);
		//显示遮罩层
		jQuery("#uidUniqueness_div").dialog("open");
		jQuery("#uidUniquenessFlag"+id).val("false");
	}else{
		jQuery("#uidUniquenessFlag"+id).val("true");
	}	
}

//检查是否版本太低并提示
function showVersionTolower(serverData,id){
	var toLower = eval('('+serverData+')')[0].toLower;
	if(toLower==true){//版本太低
		jQuery("#uidUniqueness_div").html(
			'<div style="text-align:center;color:red;">程序包上传失败</div>'
			+'<div style="margin-top:20px;">程序包版本小于此应用的其他程序包，请核实或在修改程序包后重新提交</div>'
		);
		jQuery("#uidUniqueness_div").dialog("open"); 
		jQuery("#versionTolowerFlag"+id).val("false");
	}else{
		jQuery("#versionTolowerFlag"+id).val("true");
	}
}
//展开程序包图片
function showProgramPicture(){
	jQuery("#picture1_show").show();
	jQuery("#picture2_show").show();
	jQuery("#picture3_show").show();
	jQuery("#picture4_show").show();
	jQuery("#showPicture").hide();
	jQuery("#hidePicture").show();
	var value = jQuery("#platform").val();
	var isPad = false;
	if(jQuery("#isPad").is(":checked")){
		isPad = true;
	}
	if(value=="9"){
		jQuery("#pad_show").show();
		if(isPad){
			jQuery("#logo4_show").show();
		}
	}
	
    
	
}
//收起程序包图片
function hideProgramPicture(){
	hiddeMask("picture1");
	hiddeMask("picture2");
    hiddeMask("picture3");
	hiddeMask("picture4");
	jQuery("#picture1_show").hide();
	jQuery("#picture2_show").hide();
	jQuery("#picture3_show").hide();
	jQuery("#picture4_show").hide();
	jQuery("#hidePicture").hide();
	jQuery("#showPicture").show();
	$("#pad_show").hide();
	$("#logo4_show").hide();
}

function countLen(obj){
try{	
	var tstr = obj.value;	
	var j=0;
	for (var i=0;i<tstr.length;i++) 
	{ 
		if(tstr.charCodeAt(i)>255) 
			j++; 
		j++;
	} 
	i=1200-j;
	document.getElementById("countmsg1").innerHTML = "您已输入"+j+"个字符，"+"还能输入" ;
	document.getElementById("countmsg2").innerHTML = "个字符";
	document.getElementById("countmsgnum").innerHTML =  i;
	if(i<20)
	{
	document.getElementById("countmsgnum").style.color="red";
	}
	else
	{
	document.getElementById("countmsgnum").style.color="#666666";
	}
	document.getElementById("countmsg1").style.color="#666666";
	document.getElementById("countmsg2").style.color="#666666";
}catch(e){;}
}
//----------------二期改造-------start
var paramUtils = {
	androidPadPicShow : "450x282",
	androidPadLogoShow : "140x140",
	androidPicShow : "480x800、144x240",
	otherPicShow : "240x320、72x96"
}
/**
 * 根据平台改变图片的尺寸显示 
 * 
 */
function changePicShow(){
	var htmlShow ="";
	var value = $("#platform").val();
	var isPad = false;
	if($("#isPad").is(":checked")){
		isPad = true;
	}
	//ophone
	if(value=="3"){
		htmlShow = paramUtils.androidPicShow;
		$("#pad_show").hide();
		$("#logo4_show").hide();
	}
	//android
	else if(value=="9"){
		if($("#showPicture").is(":hidden")){
			$("#pad_show").show();
		}
		if(isPad){
			htmlShow = paramUtils.androidPadPicShow;
			$("#logo4_show").show();
			
		}else{
			htmlShow = paramUtils.androidPicShow;
			$("[name='logoSizeShow']").html("上传格式：gif/jpg/png ，&lt; 2M ，自动生成尺寸:"+paramUtils.androidPadLogoShow);
			$("#logo4_show").hide();
		}
	}
	//其他平台
	else{
		htmlShow = paramUtils.otherPicShow;
		$("#pad_show").hide();
		$("#logo4_show").hide();
	}
	
	$("[name='pictureSizeShow']").html("上传格式：gif/jpg/png ，&lt; 2M ，自动生成尺寸:"+htmlShow);
}
/**
 * 隐藏图片展示区域的div
 */
function hidePicEditDiv(){
	hiddeMask('logo4'); $("#logo4_edit").html(""); 
	hiddeMask('picture1'); $("#picture1_edit").html(""); 
	hiddeMask('picture2'); $("#picture2_edit").html(""); 
	hiddeMask('picture3'); $("#picture3_edit").html(""); 
	hiddeMask('picture4'); $("#picture4_edit").html(""); 
	
}

$(function(){
	//初始化
	changePicShow();
	$("#platform").change(function(){
		//根据平台改变图片的尺寸显示
		changePicShow($(this).val());
		hidePicEditDiv();
		
	});
	$("#isPad").click(function(){
		//根据平台改变图片的尺寸显示
		changePicShow($("#platform").val());
		hidePicEditDiv();
		
	});

});

//----------------二期改造-------end
