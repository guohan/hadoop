/*!
 * Ӧ�ð汾����
 * @author tianbei
 */
 jQuery(document).ready(function(){
	jQuery( "#dialog:ui-dialog" ).dialog( "destroy" );
	jQuery( "#uidUniqueness_div" ).dialog({
		modal: true,
		autoOpen: false,
		width:"350px",
		bgiframe: true//���IE��select�������ֲ��ϵ�����

	});
	
});
 
 //չʾ����Ϣ
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
//չʾ��ȡ�Ļ�����Ϣ
function showDeviceInfo(serverData,platform){
	var series = eval('('+serverData+')')[0].series; var devicedesc = eval('('+serverData+')')[0].devicedesc;
	var feature = eval('('+serverData+')')[0].feature; var initialProgram = eval('('+serverData+')')[0].initialProgram;
 	jQuery("[name='series1']").val(series); jQuery("[name='terminalSupport1']").val(devicedesc);
    jQuery("[name='feature1']").val(feature); jQuery("#initialProgram").val(initialProgram);
    jQuery("[name='platformSelect1']").val(platform);
    if(devicedesc!=""&&devicedesc!=undefined){ $("#terminalSupportEr").html("<br/><span style='color:#3184d1;'>ϵͳ�������ĳ�������ͣ����Զ���ѡ����Ļ��͡�</span><br/><span style='color:#3184d1;'>�����ѡ�񡱰�ť���в�����޸�</span>");}
}
*/
//չʾ��ȡ�Ļ�����Ϣ
function showDeviceInfo(serverData,id,idOrEmpty){
	/**ѡ��������
	var series = eval('('+serverData+')')[0].series; var devicedesc = eval('('+serverData+')')[0].devicedesc;
	var feature = eval('('+serverData+')')[0].feature; var initialProgram = eval('('+serverData+')')[0].initialProgram;
    jQuery("[name='series"+id+"']").val(series); jQuery("[name='terminalSupport"+id+"']").val(devicedesc);
    jQuery("[name='feature"+id+"']").val(feature); jQuery("#initialProgram"+idOrEmpty).val(initialProgram);
    if(devicedesc!=""&&devicedesc!=undefined){
    	jQuery("[name='platformSelect"+id+"']").val(jQuery("#platform"+idOrEmpty).val());
    	$("#terminalSupportEr"+idOrEmpty).html("<br/><span style='color:#3184d1;'>ϵͳ�������ĳ�������ͣ����Զ���ѡ����Ļ��͡�</span><br/><span style='color:#3184d1;'>�����ѡ�񡱰�ť���в�����޸�</span>");
	}
	*/
	var initialProgram = eval('('+serverData+')')[0].initialProgram;
	jQuery("#initialProgram"+idOrEmpty).val(initialProgram);
	//ѡϵ������
	var seriesNames = eval('('+serverData+')')[0].seriesNames;
	if(seriesNames!=""&&seriesNames!=undefined){
		var series = seriesNames.split("_");
		//���������
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
		//Ӧ�÷���
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

//���Ψһ�Բ���ʾ�����Ϣ
//platform��ʱ���ã����ڿɼ�������ƽ̨����ʾ
function uidUniqueness(serverData,platform,id){
	
	var isMine = eval('('+serverData+')')[0].isMine; var contentName = eval('('+serverData+')')[0].contentName;
	var contentId = eval('('+serverData+')')[0].contentId;
	if(isMine==true){//ͬһap�ظ�
		//alert("ͬһap�ظ�,contentName = "+contentName+",contentId = "+contentId);
		jQuery("#uidUniqueness_div").html(
			'<div style="text-align:center;color:red;">��Ǹ���ϴ�ʧ��</div>'
			+'<div style="">�ظ�Package Name��Ӧ��Ϊ ��<font style="color:red;">'+contentName+'</font> <br/>Ӧ��IDΪ ��<font style="color:red;">'+contentId+'</font></div>'
			//+'<div style=""><a href="http://dev.10086.cn" target="_blank">����鿴Ӧ�õ�ַ</a>������MM��Ӧ��Ӧ�ã�</div>'
			+'<div style="margin-top:20px;">��ѡ��Ϊ��Ӧ�ý��а汾���������޸ĺ������ύ��</div>'
			+'<div style="">ע���汾�����ĳ�����汾����С��ǰһ�������</div>'
		);
		//��ʾ���ֲ�
		jQuery("#uidUniqueness_div").dialog("open");  
		jQuery("#uidUniquenessFlag"+id).val("false");
	}else if(isMine==false){//��ͬap�ظ�
		jQuery("#uidUniqueness_div").html(
			'<div style="text-align:center;color:red;">��Ǹ���ϴ�ʧ��</div>'
			+'<div style="">�ظ�Package Name��Ӧ��Ϊ ��<font style="color:red;">'+contentName+'</font> <br/>Ӧ��IDΪ ��<font style="color:red;">'+contentId+'</font></div>'
			//+'<div style=""><a href="http://dev.10086.cn" target="_blank">����鿴Ӧ�õ�ַ</a>������MM��Ӧ��Ӧ�ã�</div>'
			+'<div style="margin-top:20px;">����˻����޸ĺ������ύ��</div>'
			+'<div style="">������Ӧ���⵽���˵��ã����������ͨ���������ߡ�</div>'
			+'<div style="color:red;margin-top:20px;">��ȷ�����Ǹò�Ʒ�İ�Ȩ�����ߣ�������������ɵ�һ�з��ɺ�����������˳е���</div>'
			+'<div style="margin-top:20px;">����ϵ���ǵĿͷ�������������֤����ʵΪ��Ӧ�ð�Ȩ�����ߵ������ʼ��������ͷ�����</div>'
			+'<div style="margin-top:20px;">�ͷ���ϵ��ʽ��400-10086-20</div>'
			+'<div style="">�ͷ����䣺apservice@chinamobile.com</div>'
			+'<div style="">����: 855552270</div>'
			+'<div style="margin-top:20px;">�ʼ���ʽ�밴�����¸�ʽ��</div>'
			+'<div style="">���⣺�����ʼ�������Ӧ�����ơ�</div>'
			+'<div style="">������֤�����ϣ�Ӧ�ó����</div>'
			+'<div style="">�ʼ����ݣ�xxxxxxxxxxx</div>'
			+'<div style="">�ʼ������븽��������ϸ��ϵ��ʽ</div>'
			
		);
		//��ʾ���ֲ�
		jQuery("#uidUniqueness_div").dialog("open");
		jQuery("#uidUniquenessFlag"+id).val("false");
	}else{
		jQuery("#uidUniquenessFlag"+id).val("true");
	}	
}

//����Ƿ�汾̫�Ͳ���ʾ
function showVersionTolower(serverData,id){
	var toLower = eval('('+serverData+')')[0].toLower;
	if(toLower==true){//�汾̫��
		jQuery("#uidUniqueness_div").html(
			'<div style="text-align:center;color:red;">������ϴ�ʧ��</div>'
			+'<div style="margin-top:20px;">������汾С�ڴ�Ӧ�õ���������������ʵ�����޸ĳ�����������ύ</div>'
		);
		jQuery("#uidUniqueness_div").dialog("open"); 
		jQuery("#versionTolowerFlag"+id).val("false");
	}else{
		jQuery("#versionTolowerFlag"+id).val("true");
	}
}
//չ�������ͼƬ
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
//��������ͼƬ
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
	document.getElementById("countmsg1").innerHTML = "��������"+j+"���ַ���"+"��������" ;
	document.getElementById("countmsg2").innerHTML = "���ַ�";
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
//----------------���ڸ���-------start
var paramUtils = {
	androidPadPicShow : "450x282",
	androidPadLogoShow : "140x140",
	androidPicShow : "480x800��144x240",
	otherPicShow : "240x320��72x96"
}
/**
 * ����ƽ̨�ı�ͼƬ�ĳߴ���ʾ 
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
			$("[name='logoSizeShow']").html("�ϴ���ʽ��gif/jpg/png ��&lt; 2M ���Զ����ɳߴ�:"+paramUtils.androidPadLogoShow);
			$("#logo4_show").hide();
		}
	}
	//����ƽ̨
	else{
		htmlShow = paramUtils.otherPicShow;
		$("#pad_show").hide();
		$("#logo4_show").hide();
	}
	
	$("[name='pictureSizeShow']").html("�ϴ���ʽ��gif/jpg/png ��&lt; 2M ���Զ����ɳߴ�:"+htmlShow);
}
/**
 * ����ͼƬչʾ�����div
 */
function hidePicEditDiv(){
	hiddeMask('logo4'); $("#logo4_edit").html(""); 
	hiddeMask('picture1'); $("#picture1_edit").html(""); 
	hiddeMask('picture2'); $("#picture2_edit").html(""); 
	hiddeMask('picture3'); $("#picture3_edit").html(""); 
	hiddeMask('picture4'); $("#picture4_edit").html(""); 
	
}

$(function(){
	//��ʼ��
	changePicShow();
	$("#platform").change(function(){
		//����ƽ̨�ı�ͼƬ�ĳߴ���ʾ
		changePicShow($(this).val());
		hidePicEditDiv();
		
	});
	$("#isPad").click(function(){
		//����ƽ̨�ı�ͼƬ�ĳߴ���ʾ
		changePicShow($("#platform").val());
		hidePicEditDiv();
		
	});

});

//----------------���ڸ���-------end
