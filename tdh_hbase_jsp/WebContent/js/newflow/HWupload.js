function debug(msg){
   try{
	var target = document.getElementById("debug");
	target.innerHTML = msg + "</br>" + target.innerHTML; 
	}catch(e){
	}
}


/**
 * 上传图片次序在非顺序的情况下，其它已经上图片的选择域的遮罩会产生错位。
 * resetSelectArea可以对原来所有已上传图片进行一次重选操作，避免出现错位问题
 * @param eventFieldName
 * @return
 */
function resetSelectArea(eventFieldName){
	var fieldName = new Array();
	fieldName[0] = "logo4";
	fieldName[1] = "picture1";
	fieldName[2] = "picture2";
	fieldName[3] = "tbPicture";
	fieldName[4] = "picture3";
	fieldName[5] = "picture4";
	
	var begin = false;
	for(var i=0; i<fieldName.length; i++){
		var fn = fieldName[i];
		var xy = null;
		var ias = null;
		try{

			//按顺序检查，只对排位在eventFieldName后的图片做重选
			if(eventFieldName == fn){
				begin = true;
				continue;
			}
			if(begin == true){
				ias = $('#' + fn + '_src').imgAreaSelect({ instance: true });
				if(ias == undefined)
					continue;
				xy = hiddeMask(fn);
				ias.setSelection(xy.x1, xy.y1, xy.x2, xy.y2, true); 
				ias.setOptions({ show: true }); 
//				window.status = fn;
				ias.update();
			}
		}catch(e){
			//alert(e.message);
		}
	}
}

//隐藏半透明选择域,并返回隐藏前的所选区域座标
//解决第二次上传图片是叠加问题
function hiddeMask(fieldName){
	try{
		var ias = $('#' + fieldName + '_src').imgAreaSelect({ instance: true });
		var xy = ias.getSelection();
		ias.setSelection(0, 0, 1, 1, false); 
		ias.setOptions({ hide: true }); 
		ias.update();
		ias.setOptions({ hide: true }); 
		ias.update();
		return xy;
		return xy;
	}catch(e){;}
}


//图片上传完成，进入编辑模式
function editImg(obj){
try{ 
		var fieldName = $(obj)[0].fieldName;
		var result = $(obj)[0].code;
		var type = $(obj)[0].type;
		var size = $(obj)[0].size;	//生成目标图片的尺寸
		var edit = document.getElementById(fieldName + "_edit");
		var error = document.getElementById(fieldName + "_error");
		
		
		if(result != 0){
			error.innerHTML = $(obj)[0].description;
			$(error).show();
			var f = $("#"+fieldName+"_form")[0].image;
			var html = f.outerHTML;
			$(f).hide();
			f.outerHTML = html;
			$(f).show();
			if(fieldName=="cartoonPicture"){
				$("#cartoonPicture_hand_flag")[0].value="";
			}else
				$("#" +fieldName+"_flag")[0].value="";
			return;
		}
		
		if(fieldName=="cartoonPicture"){
			$(error).hide();
			$("#cartoonPicture_hand_flag")[0].value="0";
			$("#cartoonPicture_error").html("<img src='/oms/images/ajax/ok.gif'/><span style='color:gray'>自定义动画上传成功</span>");
			$("#cartoonPicture_error").show();
			return;
		}else{
			edit.innerHTML = "";
			$(edit).hide();
			error.innerHTML = "";
			$(error).hide();
			var imgurl = "/oms/newflow/AjaxImgUpload.action?act=viewImg&url="+$(obj)[0].url;
			var src_img_size = $(obj)[0].src_img_size;	//用户上传图片的尺寸
			var demo_img_size = $(obj)[0].demo_img_size;	//演示图片图片的尺寸
			var w = demo_img_size.split("x")[0];
			var h = demo_img_size.split("x")[1];
			
			var src_img_width = parseInt(src_img_size.split("x")[0]);
			var src_img_height = parseInt(src_img_size.split("x")[1]);
			debug(src_img_width + " x " + src_img_height);
			var _zoom = 1
			if(src_img_width>240){
				//按比例调整图片显示尺寸
				_zoom = 240/src_img_width;//图片显示的缩放率
				src_img_height = src_img_height * 240 /src_img_width;
				src_img_width = 240;
			}
			//debug(src_img_width + " x " + src_img_height);
			//演示图片定位
			var demo_img_left = $(obj)[0].demo_left;
			var demo_img_top = $(obj)[0].demo_top;
			var demo_img = "/oms/newflow/usecasepic/"+fieldName+".jpg";
			debug(demo_img);
			 var a11 ="<tr><td colspan='2'>"
			 		//+"<div id='"+fieldName+"_HWFlag_div' style='display: none'>" 
					+"<input type='radio' name='"+fieldName+"_HWFlag' id='"+fieldName+"'_HWFlag_h' checked='checked' onclick='HWclickRadio(\""+fieldName+"\")' value='h'><span style='vertical-align:bottom;'>截竖屏图</span>"
					+"<input type='radio' name='"+fieldName+"_HWFlag' id='"+fieldName+"'_HWFlag_w' onclick='HWclickRadio(\""+fieldName+"\")' value='w'><span style='vertical-align:bottom;'>截横屏图</span>"
					//+"</div>"
					+"</td></tr>";
			if(!(fieldName=="picture1"||fieldName=="picture2"||fieldName=="picture3"||fieldName=="picture4")){
				a11='';
			}			
			 var a1 = "<table class='editImgTable' width='510'>" 
					+ "<tr><td colspan='2'>鼠标拖动框可以选择裁剪区域大小和位置。</td></tr>"
					+a11
					+ "<tr>"
					+	"<td class='editImgLeft' valign='top' align='center'>"
					+	"<img id='"+fieldName + "_src' fieldName='"+fieldName+"' _zoom='"+_zoom+"' src='"+imgurl+"' width='"+src_img_width+"' height='"+src_img_height+"'>"
					+	"<div style='width:240px;overflow-y:hidden;'>"
					+ 		"<div id='" +fieldName +"_load' style='display:none'><img src='/oms/images/ajax/2-0.gif'/>正在处理..&nbsp;&nbsp;&nbsp;<span style='color:blue;cursor:pointer' title='等太久了? 点这再试一次' onclick='redo(\""+fieldName+"\")'>重试</span></div>"
					+ 		"<div id='" + fieldName + "_submit' style='display:block;margin:10px 20px 0px 20px;padding:5px'><img id='"+fieldName + "_confirm' src='/oms/images/ajax/confirmcut1.jpg' value='确定上传' onclick='editConfirm(\""+fieldName+"\")' style='border:none;cursor:hand'/></div>"
					+ 		"<div id='" + fieldName + "_redo' style='display:none;margin-top:10px;'><div style='margin-left:15px;margin-top: 5px;margin-bottom:5px;color: #5BA100;'><img src='/oms/style/newstyle/images/error_right.gif'/>&nbsp;裁剪上传成功!<input name=\"\" type=\"button\" value=\"重新裁剪\" class=\"btn_gray\" onclick='redo(\""+fieldName+"\")'></div></div>"
					+	"</div>";
			    	+	"</td>";
	        var a2 = "";	
			if(fieldName=="logo4"){
			if(type==""){
			a2 = "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='300' height='380'>"
					+	"<div class='frame' style='width:300px; height:400px;'>"
					+		"<div id='preview1' style='position: absolute;overflow: hidden;background-color:white; width:30px; height:30px;margin-top:0px;margin-left:0px;'>"
					+		"<img key='logo1_demo' style='width:30px; height:30px;'/>"
					+		"</div>"
					+		"<div id='preview2' style='position: absolute;overflow: hidden;background-color:white; width:34px; height:34px;margin-top:0px;margin-left:40px;'>"
					+		"<img key='logo2_demo' style='width:34px; height:34px;'/>"
					+		"</div>"
					+		"<div id='preview3' style='position: absolute;overflow: hidden;background-color:white; width:50px; height:50px;margin-top:0px;margin-left:85px;'>"
					+		"<img key='logo3_demo' style='width:50px; height:50px;'/>"
					+		"</div>"
					+		"<div id='preview4' style='position: absolute;overflow: hidden;background-color:white; width:65px; height:65px;margin-top:0px;margin-left:146px;'>"
					+		"<img key='logo4_demo' style='width:65px; height:65px;'/>"
					+		"</div>"
					+		"<div id='preview5' style='position: absolute;overflow: hidden;background-color:white; width:70px; height:70px;margin-top:70px;margin-left:0px;'>"
					+		"<img key='WWWPropaPicture2_demo' style='width:70px; height:70px;'/>"
					+		"</div>"
					+		"<div id='preview6' style='position: absolute;overflow: hidden;background-color:white; width:140px; height:140px;margin-top:70px;margin-left:75px;'>"
					+		"<img key='logo5_demo' style='width:140px; height:140px;'/>"
					+		"</div>"
					
				/**	+		"<div id='effectImg' style='display:none;position: absolute;overflow: hidden;background-color:white; width:300px; height:230px;margin-top:210px;margin-left:0px;'>"
						+		"优化效果：<br/>"
						+		"<div style='position: absolute;overflow: hidden;background-color:white; width:30px; height:30px;margin-top:0px;margin-left:0px;'>"
						+		"<img id='logo1_effect' style='width:30px; height:30px; background-color:white'/>"
						+		"</div>"
						+		"<div style='position: absolute;overflow: hidden;background-color:white; width:34px; height:34px;margin-top:0px;margin-left:40px;'>"
						+		"<img id='logo2_effect' style='width:34px; height:34px; background-color:white'/>"
						+		"</div>"
						+		"<div style='position: absolute;overflow: hidden;background-color:white; width:50px; height:50px;margin-top:0px;margin-left:85px;'>"
						+		"<img id='logo3_effect' style='width:50px; height:50px; background-color:white'/>"
						+		"</div>"
						+		"<div style='position: absolute;overflow: hidden;background-color:white; width:65px; height:65px;margin-top:0px;margin-left:146px;'>"
						+		"<img id='logo4_effect' style='width:65px; height:65px; background-color:white'/>"
						+		"</div>"
						+		"<div style='position: absolute;overflow: hidden;background-color:white; width:70px; height:70px;margin-top:70px;margin-left:0px;'>"
						+		"<img id='WWWPropaPicture2_effect' style='width:70px; height:70px; background-color:white'/>"
						+		"</div>"
						+		"<div style='position: absolute;overflow: hidden;background-color:white; width:140px; height:140px;margin-top:70px;margin-left:75px;'>"
						+		"<img id='logo5_effect' style='width:140px; height:140px; background-color:white'/>"
						+		"</div>"
					+		"</div>"
					*/
					+	"</div>"
					+	"</td>";
					}
			else if(type=="contest"){
			a2 = "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='240' height='"+src_img_height+"'>"
					+	"<div class='frame' style='width:240px; height:"+src_img_height+"px;margin-left:20px;'>"
                    +		"<div style='position: absolute;overflow: hidden;background-color:white; width:70px; height:30px;margin-top:-13px;margin-left:0px;'>"
					+		"预览图："
					+		"</div>"					
					+		"<div id='preview1' style='position: absolute;overflow: hidden;background-color:white; width:30px; height:30px;margin-top:0px;margin-left:0px;'>"
					+		"<img key='logo1_demo' style='width:30px; height:30px;'/>"
					+		"</div>"
					+		"<div id='preview2' style='position: absolute;overflow: hidden;background-color:white; width:34px; height:34px;margin-top:0px;margin-left:50px;'>"
					+		"<img key='logo2_demo' style='width:34px; height:34px;'/>"
					+		"</div>"
					+		"<div id='preview3' style='position: absolute;overflow: hidden;background-color:white; width:50px; height:50px;margin-top:0px;margin-left:105px;'>"
					+		"<img key='logo3_demo' style='width:50px; height:50px;'/>"
					+		"</div>"
					+		"<div id='preview4' style='position: absolute;overflow: hidden;background-color:white; width:65px; height:65px;margin-top:70px;margin-left:0px;'>"
					+		"<img key='logo4_demo' style='width:65px; height:65px;'/>"
					+		"</div>"
					+		"<div id='preview5' style='position: absolute;overflow: hidden;background-color:white; width:70px; height:70px;margin-top:70px;margin-left:85px;'>"
					+		"<img key='WWWPropaPicture2_demo' style='width:70px; height:70px;'/>"
					+		"</div>"	
				+"</td>"
				+"</tr>"	
				+"<tr height='150'>"	
				+"<td>&nbsp;</td>"	
				+"<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='240' height='100'>"	
				    +	 "<div id='effectImg' style='display:none;position:absolute;overflow:hidden;background-color:white; width:400px; height:80px;margin-top:70px;margin-left:-250px;'>"
                    +		"<div style='position: absolute;overflow: hidden;background-color:white; width:70px; height:30px;margin-top:0px;margin-left:0px;'>"
					+		"效果图："
					+		"</div>"
					+		"<div style='position: absolute;overflow: hidden;background-color:white; width:30px; height:30px;margin-top:0px;margin-left:70px;'>"
					+		"<img id='logo1_effect' style='width:30px; height:30px; background-color:white'/>"
					+		"</div>"
					+		"<div style='position: absolute;overflow: hidden;background-color:white; width:34px; height:34px;margin-top:0px;margin-left:110px;'>"
					+		"<img id='logo2_effect' style='width:34px; height:34px; background-color:white'/>"
					+		"</div>"
					+		"<div style='position: absolute;overflow: hidden;background-color:white; width:50px; height:50px;margin-top:0px;margin-left:155px;'>"
					+		"<img id='logo3_effect' style='width:50px; height:50px; background-color:white'/>"
					+		"</div>"
					+		"<div style='position: absolute;overflow: hidden;background-color:white; width:65px; height:65px;margin-top:0px;margin-left:215px;'>"
					+		"<img id='logo4_effect' style='width:65px; height:65px; background-color:white'/>"
					+		"</div>"
					+		"<div style='position: absolute;overflow: hidden;background-color:white; width:70px; height:70px;margin-top:0px;margin-left:290px;'>"
					+		"<img id='WWWPropaPicture2_effect' style='width:70px; height:70px; background-color:white'/>"
					+		"</div>"
			    	+	"</div>"
			    +"</td>"
					}					
			} else if(fieldName=="picture1"){
				  a2 = "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='240' height='510'>"
						+	"<div class='frame' style='width:240px; height:510px;'>"
						+		"<div id='preview21' style='position: absolute;overflow: hidden;background-color:white; width:240px; height:320px;margin-top:0px;margin-left:0px;'>"
						+		"<img key='picture1_demo' style='width:240px; height:320px;'/>"
						+		"</div>"
						+		"<div id='preview22' style='position: absolute;overflow: hidden;background-color:white; width:135px; height:180px;margin-top:330px;margin-left:0px;'>"
						+		"<img key='WWWPropaPicture3_demo' style='width:135px; height:180px;'/>"
						+		"</div>"
						+	"</div>"
						+	"</td>"				
			}
			
			else {
				
			   a2= "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='240' height='320'>"
					+	"<div class='frame' style='width: "+w+"px; height: "+320+"px;'>"
					+		"<div id='preview' style='position: absolute;overflow: hidden;background-color:white; width: "+w+"px; height: "+h+"px;margin-top:"+demo_img_top+"px;margin-left:"+demo_img_left+"px;'>"
					+		"<img key='"+fieldName+"_demo' style='width: "+w+"px; height: "+h+"px;'/>"
					+		"</div>"
					+	"</div>"
					+	"</td>"
			
			}
					
			    var a3 =  "</tr>"
					+ "<tr><td align='center'>"
					+ "<input type='hidden' id='" + fieldName + "_x1' name='" + fieldName + "_x1' />"
					+ "<input type='hidden' id='" + fieldName + "_y1' name='" + fieldName + "_y1' />"
					+ "<input type='hidden' id='" + fieldName + "_x2' name='" + fieldName + "_x2' />"
					+ "<input type='hidden' id='" + fieldName + "_y2' name='" + fieldName + "_y2' />"
					+ "<input type='hidden' id='" + fieldName + "_w' name='" + fieldName + "_w' />"
					+ "<input type='hidden' id='" + fieldName + "_h' name='" + fieldName + "_h' />"
					+ "<input type='hidden' id='" + fieldName + "_demosize' value='" + demo_img_size + "' />"
					+ "</td><td>&nbsp;</td></tr></table>";
			edit.innerHTML = a1+a2+a3;
							//初始化横竖转换参数 
				HWfieldName = fieldName;
				HWimgurl = imgurl;
				HWsize = size;
				HWdemo_img_size = demo_img_size;
				HWsrcImagSize = src_img_width+"x" +src_img_height;
				HW_zoom = _zoom;
				HWsrc_img_height = src_img_height;
				HWsrc_img_width = src_img_width;				
				//初始化横竖转换参数 	
			try{
				$(edit).show();
				//alert(imgurl);
				initEdit(fieldName,imgurl,size,demo_img_size,src_img_width+"x" +src_img_height);
				
		
			}catch(e){
				//alert(e);
			}

			$("#" +fieldName+"_flag")[0].value="1";//标记1，图片已上传但尚未裁切
			 		
			resetSelectArea(fieldName);
			//alert($("#" +fieldName+"_flag")[0].value);
			var ias = $('#' + fieldName + '_src').imgAreaSelect({ instance: true });

			ias.setOptions({show:true});
//			ias.setOptions({persistent:true})
			ias.update();
			
		}
		//$(edit).html="<img src='/oms/newflow/AjaxImgUpload.action?act=viewImg&url="+ $(obj)[0].url +"/>";
	}catch(e){
//		alert(e.message);
	}
}

//切换动画上传方式: 手工上传/自动合成
function swapUploadType(type){
	if(type=='1'){
		$('#cartoonPicture_auto').show();
		$('#cartoonPicture_hand').hide();
		$("#cartoonPicture_hand_flag")[0].value="";	//切换时标记为未上传
		var f = $("#cartoonPicture_form")[0].image;
		var html = f.outerHTML;
		$(f).hide();
		f.outerHTML = html;
		$(f).show();
		$("#cartoonPicture_error").html("");
	}else if(type=='2'){
		hiddeMask("cartoonPicture_1");
		hiddeMask("cartoonPicture_2");
		hiddeMask("cartoonPicture_3");
		$('#cartoonPicture_auto').hide();
		$("#cartoonPicture_1_flag")[0].value="";//切换时标记为未上传
		$("#cartoonPicture_2_flag")[0].value="";//切换时标记为未上传
		$("#cartoonPicture_3_flag")[0].value="";//切换时标记为未上传
		$('#cartoonPicture_hand').show();
	}
	
}
//提交裁切
/*
function editConfirm(fieldName){
	var x1=$('#'+ fieldName + '_x1')[0].value;
	var y1=$('#'+ fieldName + '_y1')[0].value;
	var x2=$('#'+ fieldName + '_x2')[0].value;
	var y2=$('#'+ fieldName + '_y2')[0].value;
	var w=$('#'+ fieldName + '_w')[0].value;
	var h=$('#'+ fieldName + '_h')[0].value;
	$('#' + fieldName + '_submit').hide();
	$('#' + fieldName + '_load').show();
	var _zoom = $('#'+ fieldName + '_src').attr("_zoom");
	//url后加时间戳，避免缓存
	document.getElementById("uploadFrame_"+fieldName).src="/oms/newflow/AjaxImgUpload.action?act=cutImg&fieldName="+fieldName+"&x1=" + x1 +"&x2=" + x2 +"&y1=" + y1 +"&y2=" + y2+"&w=" + w+"&h=" + h + "&_zoom=" + _zoom + "&timestamp=" + new Date().getTime()+"&picture1_hwflag="+picture1_hwflag+"&picture2_hwflag="+picture2_hwflag+"&picture3_hwflag="+picture3_hwflag+"&picture4_hwflag="+picture4_hwflag;
	try{
		resetRadio(fieldName);
	}catch(err){
	}
	
	return false;
}*/
//提交裁切
function editConfirm(fieldName){
	var x1=$('#'+ fieldName + '_x1')[0].value;
	var y1=$('#'+ fieldName + '_y1')[0].value;
	var x2=$('#'+ fieldName + '_x2')[0].value;
	var y2=$('#'+ fieldName + '_y2')[0].value;
	var w=$('#'+ fieldName + '_w')[0].value;
	var h=$('#'+ fieldName + '_h')[0].value;
	$('#' + fieldName + '_submit').hide();
	$('#' + fieldName + '_load').show();
	var _zoom = $('#'+ fieldName + '_src').attr("_zoom");
	
	//截图质量控制参数
	var qualityFlag = $('#'+ fieldName + '_FN').parent().find("[name='quality']").val();
	var quality ='';
	if(qualityFlag!=undefined){
	 quality = qualityImg(fieldName);
	}
	
	
	//url后加时间戳，避免缓存
	document.getElementById("uploadFrame_"+fieldName).src="/oms/newflow/AjaxImgUpload.action?act=cutImg&fieldName="
		+fieldName+"&x1=" + x1 +"&x2=" + x2 +"&y1=" + y1 +"&y2=" + y2+"&w=" + w+"&h=" + h + "&_zoom=" + _zoom + "&timestamp=" 
		+ new Date().getTime()+"&picture1_hwflag="+picture1_hwflag+"&picture2_hwflag="+picture2_hwflag+"&picture3_hwflag="
		+picture3_hwflag+"&picture4_hwflag="+picture4_hwflag+"&quality="+quality+"&org.apache.struts.taglib.html.TOKEN="+$("[name='org.apache.struts.taglib.html.TOKEN']").val();

	try{
		resetRadio(fieldName);
	}catch(err){
	}
	var labelMsgName = "#" + fieldName + "_error";
	if($(labelMsgName))
	{
	   $(labelMsgName).hide();
	}
	return false;
}


//重新裁切
function redo(fieldName){
	$('#' + fieldName + '_load').hide();
	$("#" +fieldName+"_flag")[0].value="1";//标记1，图片已上传但尚未裁切
	var select = { x1: 0, y1: 0, x2: 30, y2: 30 };
//	$('#'+fieldName+"_src").imgAreaSelect(select);
	$('#' + fieldName + '_redo').hide();
	$('#' + fieldName + '_submit').show();
	resetSelectArea(fieldName);
//	$('#'+ fieldName + '_x1').val(select.x1);
//	$('#'+ fieldName + '_y1').val(select.y1);
//	$('#'+ fieldName + '_x2').val(select.x2);
//	$('#'+ fieldName + '_y2').val(select.y2);
//	$('#'+ fieldName + '_w').val(30);
//	$('#'+ fieldName + '_h').val(30);
	
	$('#'+fieldName+"_src").imgAreaSelect({
		onSelectChange: preview
	});
	
}

//裁切完成回调函数
function editDone(obj){
	var fieldName = $(obj)[0].fieldName;
	var result = $(obj)[0].code;
	
	if(result != 0){
		alert("图片裁剪失败：" + $(obj)[0].description);
		$('#' + fieldName + '_submit').show();
		$('#' + fieldName + '_load').hide();
		$('#' + fieldName + '_redo').hide();
		$("#" +fieldName+"_flag")[0].value="1";	//设置为未裁剪
		resetSelectArea(fieldName);
	}else{	
		$('#' + fieldName + '_submit').hide();
		$('#' + fieldName + '_load').hide();
		$('#' + fieldName + '_redo').show();
		$("#" +fieldName+"_flag")[0].value="0";	//设置为裁剪成功
		if(fieldName == 'logo4'){
			$('#WWWPropaPicture2_effect').attr("src","/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].WWWPropaPicture2_effect);
			$('#logo1_effect').attr("src","/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].logo1_effect);
			$('#logo2_effect').attr("src","/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].logo2_effect);
			$('#logo3_effect').attr("src","/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].logo3_effect);
			$('#logo4_effect').attr("src","/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].logo4_effect);
			$('#logo5_effect').attr("src","/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].logo5_effect);
			$('#effectImg').fadeIn();
		}
		resetSelectArea(fieldName);
		if(fieldName.indexOf("cartoonPicture") >= 0){
			var gif_url =  "/oms/newflow/AjaxImgUpload.action?act=viewImg&url=" + $(obj)[0].gif_url;
			$("#cartoonPicture_preview_src")[0].value=gif_url;
		}
		
		$('#'+fieldName+"_src").imgAreaSelect({
			onSelectChange: preview1
		});
		
	}
}
function preview1(){
}

function preview(image, selection){
	var id = $(image).attr("fieldName");
	var demosize = $("#" + id + "_demosize").attr("value");
	//alert(selection+"\n"+$("img[key='"+id+"_demo']"));
	//alert(id+"\n" + demosize);
	if (!selection.width || !selection.height) 
		return;

	$('#'+ id + '_x1').val(selection.x1);
	$('#'+ id + '_y1').val(selection.y1);
	$('#'+ id + '_x2').val(selection.x2);
	$('#'+ id + '_y2').val(selection.y2);
	$('#'+ id + '_w').val(selection.width);
	$('#'+ id + '_h').val(selection.height);
	
	
	if(id=="logo4"){
		changImage($("img[key='logo1_demo']"), image.width, image.height, 30, 30, selection);
		changImage($("img[key='logo2_demo']"), image.width, image.height, 34,34, selection);
		changImage($("img[key='logo3_demo']"), image.width, image.height, 50,50, selection);
		changImage($("img[key='logo4_demo']"), image.width, image.height, 65,65, selection);
		changImage($("img[key='logo5_demo']"), image.width, image.height, 140,140, selection);
		changImage($("img[key='WWWPropaPicture2_demo']"), image.width, image.height, 70,70, selection);
	} else if(id=="picture1"){
		changImage($("img[key='picture1_demo']"), image.width, image.height, 240,320, selection);
		changImage($("img[key='WWWPropaPicture3_demo']"), image.width, image.height, 135,180, selection);
	} else {
	    changImage($("img[key='"+id+"_demo']"), image.width, image.height, demosize.split("x")[0], demosize.split("x")[1], selection);
	}
	
//	window.status = image.height + ":" + demosize.split("x")[0] + ":" + demosize.split("x")[1];
}

function changImage(image, width, height, needWidth, needHeight, selection){
	try{
		var scaleX = needWidth / selection.width;
		var scaleY = needHeight / selection.height;
		image.css({
			width: Math.round(scaleX * width),
			height: Math.round(scaleY * height),
			marginLeft: -Math.round(scaleX * selection.x1),
			marginTop: -Math.round(scaleY * selection.y1)
		});
	}catch(e){
	
	}
}

//初始化在线编辑组件
function initEdit(id,imgurl,size,demosize,srcImgSize){
			(function inits(){
				var imageUrl = imgurl;
				$("#"+id+"_src").attr("src",imageUrl);
				
				if(id=="logo4"){
					$("img[key='logo1_demo']").attr("src",imageUrl);
					$("img[key='logo2_demo']").attr("src",imageUrl);
					$("img[key='logo3_demo']").attr("src",imageUrl);
					$("img[key='logo4_demo']").attr("src",imageUrl);
					$("img[key='logo5_demo']").attr("src",imageUrl);
					$("img[key='WWWPropaPicture2_demo']").attr("src",imageUrl);
					size = '70x70';
				} else if(id=="picture1"){
					$("img[key='picture1_demo']").attr("src",imageUrl);
					$("img[key='WWWPropaPicture3_demo']").attr("src",imageUrl);
				} else {
				    $("img[key='"+id+"_demo']").attr("src",imageUrl);
				}
				
				
			})();
			var parameters = {};
			(function inits1(){
				//w,h目标尺寸
				var w = parseInt(size.split("x")[0]);
				var h =  parseInt(size.split("x")[1]);
				//src_w,src_h源图尺寸
				var src_w = parseInt(srcImgSize.split("x")[0]);
				var src_h = parseInt(srcImgSize.split("x")[1]);
				
				//默认选区范围
				var sel_x1 = 0;
				var sel_y1 = 0;
				var sel_x2 = 30;
				var sel_y2 = 30;
				
				if(src_w==w && src_h ==h){
					sel_x2 = w;
					sel_y2 = h;
					sel_x1 = 0;
					sel_y1 = 0;
				}
				else{
					//选区范围水平居中,宽度为src_w的1/2, -5作为微调,避免溢出
					sel_x1 = src_w/4 - 5;
					sel_x2 = 3*src_w/4 - 5;
					//根据目标比例计算选区高度
					sel_y2 = h*(sel_x2-sel_x1)/w;
					
					//选区高度在src_h范围内,调整为垂直居中
					if(sel_y2<src_h){
						sel_y1 = (src_h - sel_y2)/2;
						sel_y2 = sel_y1 + sel_y2;
//						alert(sel_x1 + "::" + sel_x2 + "\n" + sel_y1 + "::" + sel_y2);
					}
					//超过了src_h的高度,以src_h -10 为最大高度为基准,按比例调整宽度
					else{
						sel_y1 = 5;
						sel_y2 = src_h - 10;
						sel_x2 = w*(sel_y2-sel_y1)/h;
						sel_x1 = (src_w - sel_x2)/2;
						sel_x2 = sel_x1+sel_x2;
//						alert(sel_x1 + ":" + sel_x2 + "\n" + sel_y1 + ":" + sel_y2);
					}
//					alert((sel_x2-sel_x1) + ":" + (sel_y2-sel_y1));
//					sel_y1 =src_h/4;
//					sel_x2 = 20+w;
//					sel_y2 = 30+h;
//					sel_x1 = 20;
//					sel_y1 = 30;
				}
				var a = 50;
//				debug("sel_x1:" + sel_x1);
//				debug("sel_y1:" + sel_y1);
//				debug("sel_x2:" + sel_x2);
//				debug("sel_y2:" + sel_y2);
				var select = { x1: sel_x1, y1: sel_y1, x2: sel_x2, y2: sel_y2,width:sel_x2-sel_x1,height:sel_y2-sel_y1 };
				$('#'+id+"_src").imgAreaSelect({ x1: sel_x1, y1: sel_y1, x2: sel_x2, y2: sel_y2 });
//				preview($("#"+id+"_src"),{ x1: sel_x1, y1: sel_y1, x2: sel_x2, y2: sel_y2 });
				
				
				if(id=="logo4"){
					changImage($("img[key='logo1_demo']"),  src_w, src_h, 30, 30, select);
					changImage($("img[key='logo2_demo']"),  src_w, src_h, 34,34, select);
					changImage($("img[key='logo3_demo']"),  src_w, src_h, 50,50, select);
					changImage($("img[key='logo4_demo']"),  src_w, src_h, 65,65, select);
					changImage($("img[key='logo5_demo']"),  src_w, src_h, 140,140, select);
					changImage($("img[key='WWWPropaPicture2_demo']"),  src_w, src_h, 70,70, select);
				} else if(id=="picture1"){
					changImage($("img[key='picture1_demo']"),  src_w, src_h, 240,320, select);
					changImage($("img[key='WWWPropaPicture3_demo']"),  src_w, src_h, 135,180, select);
				} else {
				    changImage($("img[key='"+id+"_demo']"), src_w, src_h, demosize.split("x")[0], demosize.split("x")[1], select);
				}
				defaultSelect($('#'+id+"_src"),select);
				var error = document.getElementById(id + "_error");
				error.innerHTML = "";
				$(error).hide();
			})();

			//初始化选区的时候给坐标参数赋值 
			function defaultSelect(image, selection){
				if (!selection.width || !selection.height) 
					return;
				$('#'+ id + '_x1').val(selection.x1);
				$('#'+ id + '_y1').val(selection.y1);
				$('#'+ id + '_x2').val(selection.x2);
				$('#'+ id + '_y2').val(selection.y2);
				$('#'+ id + '_w').val(selection.width);
				$('#'+ id + '_h').val(selection.height);
//				debug(image.width+"::" + image.height)
			}
			

			

			
			$(function(){
				var w = parseInt(size.split("x")[0]);
				var h =  parseInt(size.split("x")[1]);
				$('#'+id+"_src").imgAreaSelect({
					aspectRatio: w+":" +h,
					handles: true,
//					persistent:true,
					fadeSpeed: 200,
					onSelectChange: preview
				});
			});
			
			function tijiao(){
				parameters.name = "flower2.jpg";
				$.ajax({
					type: "POST",
					url: "/cutImageAction.do",
					data: parameters,
					success: function(json){
						//alert(json.ret);
					},
					error: function(){
						alert("提交失败");
					}
				});
			}

}

//上传图片
function uploadImg(obj){
	var fieldName = $(obj).prev()[0].value;
	var error = document.getElementById(fieldName + "_error");
	var returnValue = checkImageFormat2(obj.value,"图片","jpg/gif/png","");
	if(returnValue == true){
		//隐藏上一次上传此图片生成的选择域
		hiddeMask(fieldName);
		error.innerHTML = "<span style='color:black'><img src='/oms/images/ajax/2-0.gif'/>正在加载...</span>"
			$(error).show();
		$(obj).parent().submit();
		try
		   {
		   		resetRadio(fieldName);
		   }catch(err)
		   {}
	}else{
		//隐藏上一次上传此图片生成的选择域
		hiddeMask(fieldName);
		error.innerHTML = "<span><img src='/oms/images/unchecked.gif'/>" + returnValue + "</span>"
		$(error).show();
		obj.focus();
		return
	}
}

function checkImageFormat2(obj,name,format,pictureSize){
	   try{

		var s = new String(obj);
		var t = s.lastIndexOf(".");
		
		var objStr=obj;
	    var separate=objStr.lastIndexOf("\\");
	    if(separate<0)
	    {
	    	separate=objStr.lastIndexOf("/");
	    }
	    objStr=objStr.substring(separate+1);
	         
		if(t > 0 && t == s.length - 4)
		{
			var ss=s.substring(t+1).toLowerCase();
			return checkFormaPicture2(name,ss,format);
		}
		else
		{
		   return name+"的格式不对";
		}
		return true;
	}catch(e){}
	}

function checkFormaPicture2(name,ss,format)
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
	return (name+"格式必须是"+format);
}

//上传程序包
function uploadProgram(obj){
		var fieldName = $(obj).prev()[0].value;
		var error = document.getElementById(fieldName + "_error");
		error.innerHTML = "<span style='color:black'><img src='/oms/images/ajax/2-0.gif'/>正在上传...</span>"
			$(error).show();
		$("#uploadPkgBt").hide();
		$(obj).parent().submit();
}


function uploadVideo(obj){
		var fieldName = $(obj).prev()[0].value;
		var error = document.getElementById(fieldName + "_error");
		error.innerHTML = "<span style='color:black'><img src='/oms/images/ajax/2-0.gif'/>正在上传...</span>"
			$(error).show();
		$("#uploadVideoBt").hide();
		$(obj).parent().submit();
}

function program(obj){
    var fieldName = $(obj)[0].fieldName;
	var result = $(obj)[0].code;
	var error = document.getElementById(fieldName + "_error");
	   $(error).hide();
	if(result!=0){ 
	   $("#" +fieldName+"_flag")[0].value="1";//上传失败置为1
	   alert("程序包上传："+$(obj)[0].description);
	   $("#uploadPkgBt").show();
	}else{ 
	   $("#" +fieldName+"_flag")[0].value="0";//上传成功置为0
	   var success = document.getElementById(fieldName + "_success");
	   success.innerHTML = "<span style='color:black'><img src='/oms/images/ajax/ok.gif'/>&nbsp;上传成功</span>"
			$(success).show();
	   $("#uploadPkgBt").hide();
	}
}

function video(obj){
    var fieldName = $(obj)[0].fieldName;
	var result = $(obj)[0].code;
	var error = document.getElementById(fieldName + "_error");
	   $(error).hide();
	if(result!=0){ 
	   $("#" +fieldName+"_flag")[0].value="1";//上传失败置为1
	   alert("Flash上传："+$(obj)[0].description);
	   $("#uploadVideoBt").show();
	}else{ 
	   $("#" +fieldName+"_flag")[0].value="0";//上传成功置为0
	   var success = document.getElementById(fieldName + "_success");
	   success.innerHTML = "<span style='color:black'><img src='/oms/images/ajax/ok.gif'/>&nbsp;上传成功</span>"
			$(success).show();
	   $("#uploadVideoBt").hide();
	}
}

function clearProgram(obj){
       var fieldName = $(obj).prev()[0].value;
       $("#" +fieldName+"_flag")[0].value="1";//置为1，表示未上传
	   var success = document.getElementById(fieldName + "_success");
		   $(success).hide();
	$("#uploadPkgBt").show();
}


function clearProgramMany(obj,pkgid){
//alert("clearProgramAdd");
       var fieldName = $(obj).prev()[0].value;
       //alert("fieldName="+fieldName);
       $("#" +fieldName+"_flag")[0].value="1";//置为1，表示未上传
	   var success = document.getElementById(fieldName+ "_success");
		   $(success).hide();
	$("#uploadPkgBt"+pkgid).show();
	//alert("clearProgramAdd2");
}

function uploadProgramMany(obj,pkgid){
	//alert("uploadProgramMany s");
	//alert("indexPg"+pkgid);
		var fieldName = $(obj).prev()[0].value;
		var error = document.getElementById(fieldName+ "_error");
		error.innerHTML = "<span style='color:black'><img src='/oms/images/ajax/2-0.gif'/>正在上传...</span>"
			$(error).show();
		$("#uploadPkgBt"+pkgid).hide();
		$(obj).parent().submit();
		//alert("uploadProgramMany e");
}

function clearVideo(obj){
       var fieldName = $(obj).prev()[0].value;
       $("#" +fieldName+"_flag")[0].value="1";//置为1，表示未上传
	   var success = document.getElementById(fieldName + "_success");
		   $(success).hide();
	$("#uploadVideoBt").show();
}

function editGeneralImg(obj){
	 var fieldName = $(obj)[0].fieldName;
		var result = $(obj)[0].code;
		if(result!=0){ 
		   $("#" +fieldName+"_flag")[0].value="1";//上传失败置为1
		   $("#" + fieldName + "_error").hide();
		   var error = document.getElementById(fieldName + "_error");
		   error.innerHTML = "<span><img src='/oms/images/unchecked.gif'/>" + "图片上传："+$(obj)[0].description + "</span>";
		   $(error).show();
		   //alert("图片上传："+$(obj)[0].description);
		}else{ 
		   var url = $(obj)[0].url;
		   $("#" +fieldName+"_flag")[0].value="0";//上传成功置为0
		   var success = document.getElementById(fieldName + "_success");
		   success.innerHTML = "<img src='" + url + "' style='width:630px; height:240px;'/>";
	       $(success).show();
	       $("#" + fieldName + "_error").hide();
	       var advertPicDiv = $("#advertPicDiv");
	       if(advertPicDiv){
	    	   advertPicDiv.hide();
	       }
		}
}

//横竖截图
var objHW;
var picture1_hwflag='';
var picture2_hwflag='';
var picture3_hwflag='';
var picture4_hwflag='';
var HWfieldName='';
var HWimgurl='';
var HWsize='';
var HWdemo_img_size='';
var HWsrcImagSize='';
var HW_zoom = '';
var	HWsrc_img_height = '';
var	HWsrc_img_width = '';
var HWdemo_img_left = '0';
var HWdemo_img_top = '0';

var	picture1_needWith='';
var	picture1_needHight='';	
var	WWWPropaPicture3_needWith='';
var	WWWPropaPicture3_needHight='';

//不展示切换按钮时，初始化为空
function resetRadio(fieldName){
		$("#"+fieldName+"_HWFlag_div").hide();
		$("#"+fieldName+"_HWFlag_h").attr("checked","");
		$("#"+fieldName+"_HWFlag_w").attr("checked","");
}

//选中切换按钮时，执行切图流程
function HWclickRadio(fieldName){
hiddeMask(fieldName);
	var hwflagObj = document.getElementsByName(fieldName+"_HWFlag");
	for(var i=0;i<hwflagObj.length;i++)
	{
        if(hwflagObj[i].checked==true)
		{
			if(fieldName=='picture1'){
	           picture1_hwflag= hwflagObj[i].value;
				if(picture1_hwflag=='h'){
					HWsize='240x320';
					HWdemo_img_size='240x320';	
				}else if(picture1_hwflag=='w'){
					HWsize='320x240';
					HWdemo_img_size='320x240';	
				}	           
	           break;		
			}
			if(fieldName=='picture2'){
	           picture2_hwflag= hwflagObj[i].value;
				if(picture2_hwflag=='h'){
					HWsize='240x320';
					HWdemo_img_size='240x320';	
				}else if(picture2_hwflag=='w'){
					HWsize='320x240';
					HWdemo_img_size='320x240';	
				}		           
	           break;		
			}
			if(fieldName=='picture3'){
	           picture3_hwflag= hwflagObj[i].value;
				if(picture3_hwflag=='h'){
					HWsize='240x320';
					HWdemo_img_size='240x320';	
				}else if(picture3_hwflag=='w'){
					HWsize='320x240';
					HWdemo_img_size='320x240';	
				}		           
	           break;		
			}	
			if(fieldName=='picture4'){
	           picture4_hwflag= hwflagObj[i].value;
				if(picture4_hwflag=='h'){
					HWsize='240x320';
					HWdemo_img_size='240x320';	
				}else if(picture4_hwflag=='w'){
					HWsize='320x240';
					HWdemo_img_size='320x240';	
				}		           
	           break;		
			}					
        }
    }
	HWinitEdit(fieldName,HWimgurl,HWsize,HWdemo_img_size,HWsrcImagSize);
}


//横竖初始化在线编辑组件
function HWinitEdit(id,imgurl,size,demosize,srcImgSize){
			var fieldName = id;
			var edit = document.getElementById(fieldName + "_edit");
		    edit.innerHTML = "";
			$(edit).hide();	
			
			//初始化横切按钮是否选中
			var Hchecked ='';
			var Wchecked = '';
			if(fieldName=='picture1'){
				if(picture1_hwflag=='h'){
					Hchecked =" checked='checked'";
				}else if(picture1_hwflag=='w'){
					Wchecked=" checked='checked'";
				}
			}else if(fieldName=='picture2'){
				if(picture2_hwflag=='h'){
					Hchecked =" checked='checked'";
				}else if(picture2_hwflag=='w'){
					Wchecked=" checked='checked'";
				}
			}else if(fieldName=='picture3'){
				if(picture3_hwflag=='h'){
					Hchecked =" checked='checked'";
				}else if(picture3_hwflag=='w'){
					Wchecked=" checked='checked'";
				}
			}else if(fieldName=='picture4'){
				if(picture4_hwflag=='h'){
					Hchecked =" checked='checked'";
				}else if(picture4_hwflag=='w'){
					Wchecked=" checked='checked'";
				}
			}
			
			  			
			var w = demosize.split("x")[0];
			var h = demosize.split("x")[1];

			
			 var a11 ="<tr><td colspan='2'>"
			 		//+"<div id='"+fieldName+"_HWFlag_div' style='display: none'>"
					+"<input type='radio' name='"+fieldName+"_HWFlag' id='"+fieldName+"'_HWFlag_h'  onclick='HWclickRadio(\""+fieldName+"\")' value='h'"+Hchecked+"><span style='vertical-align:bottom;'>截竖屏图</span>"
					+"<input type='radio' name='"+fieldName+"_HWFlag' id='"+fieldName+"'_HWFlag_w' onclick='HWclickRadio(\""+fieldName+"\")' value='w' "+Wchecked+"><span style='vertical-align:bottom;'>截横屏图</span>"
					//+"</div>"
					+"</td></tr>";
			 var a1 = "<table class='editImgTable' width='510'>" 
					+ "<tr><td colspan='2'>鼠标拖动框可以选择裁剪区域大小和位置。</td></tr>"
					+a11
					+ "<tr>"
					+	"<td class='editImgLeft' valign='top' align='center'>"
					+	"<img id='"+fieldName + "_src' fieldName='"+fieldName+"' _zoom='"+HW_zoom+"' src='"+imgurl+"' width='"+HWsrc_img_width+"' height='"+HWsrc_img_height+"'>"
					+	"<div style='width:240px;overflow-y:hidden;'>"
					+ 		"<div id='" +fieldName +"_load' style='display:none'><img src='/oms/images/ajax/2-0.gif'/>正在处理..&nbsp;&nbsp;&nbsp;<span style='color:blue;cursor:pointer' title='等太久了? 点这再试一次' onclick='redo(\""+fieldName+"\")'>重试</span></div>"
					+ 		"<div id='" + fieldName + "_submit' style='display:block;margin:10px 20px 0px 20px;padding:5px'><img id='"+fieldName + "_confirm' src='/oms/images/ajax/confirmcut1.jpg' value='确定上传' onclick='editConfirm(\""+fieldName+"\")' style='border:none;cursor:hand'/></div>"
					+ 		"<div id='" + fieldName + "_redo' style='display:none;margin-top:10px;'><div style='margin-left:15px;margin-top: 5px;margin-bottom:5px;color:gray;'><img src='/oms/images/ajax/ok.gif'/>&nbsp;裁剪上传成功!</div><input name=\"\" type=\"button\" value=\"重新裁剪\" class=\"btn_gray\" onclick='redo(\""+fieldName+"\")'></div>"
					+	"</div>";
			    	+	"</td>";
	        var a2 = "";	
			if(fieldName=="picture1"){
				if(picture1_hwflag=="w"){
				  a2 = "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='320px' height='510'>"
						+	"<div class='frame' style='width:320px; height:510px;'>"
						+		"<div id='preview21' style='position: absolute;overflow: hidden;background-color:white; width:320px; height:240px;margin-top:0px;margin-left:0px;'>"
						+		"<img key='picture1_demo' style='width:320px; height:240px;'/>"
						+		"</div>"
						+		"<div id='preview22' style='position: absolute;overflow: hidden;background-color:white; width:180px; height:135px;margin-top:330px;margin-left:0px;'>"
						+		"<img key='WWWPropaPicture3_demo' style='width:180px; height:135px;'/>"
						+		"</div>"
						+	"</div>"
						+	"</td>"				
				}else{
				  a2 = "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='240' height='510'>"
						+	"<div class='frame' style='width:240px; height:510px;'>"
						+		"<div id='preview21' style='position: absolute;overflow: hidden;background-color:white; width:240px; height:320px;margin-top:0px;margin-left:0px;'>"
						+		"<img key='picture1_demo' style='width:240px; height:320px;'/>"
						+		"</div>"
						+		"<div id='preview22' style='position: absolute;overflow: hidden;background-color:white; width:135px; height:180px;margin-top:330px;margin-left:0px;'>"
						+		"<img key='WWWPropaPicture3_demo' style='width:135px; height:180px;'/>"
						+		"</div>"
						+	"</div>"
						+	"</td>"				
				}
			}
			
			else {
				
			   a2= "<td class='editImgRight' style='margin-left:10px;background-color:white;' valign='top' width='240' height='320'>"
					+	"<div class='frame' style='width: "+w+"px; height: "+320+"px;'>"
					+		"<div id='preview' style='position: absolute;overflow: hidden;background-color:white; width: "+w+"px; height: "+h+"px;margin-top:"+HWdemo_img_top+"px;margin-left:"+HWdemo_img_left+"px;'>"
					+		"<img key='"+fieldName+"_demo' style='width: "+w+"px; height: "+h+"px;'/>"
					+		"</div>"
					+	"</div>"
					+	"</td>"
			
			}
					
			    var a3 =  "</tr>"
					+ "<tr><td align='center'>"
					+ "<input type='hidden' id='" + fieldName + "_x1' name='" + fieldName + "_x1' />"
					+ "<input type='hidden' id='" + fieldName + "_y1' name='" + fieldName + "_y1' />"
					+ "<input type='hidden' id='" + fieldName + "_x2' name='" + fieldName + "_x2' />"
					+ "<input type='hidden' id='" + fieldName + "_y2' name='" + fieldName + "_y2' />"
					+ "<input type='hidden' id='" + fieldName + "_w' name='" + fieldName + "_w' />"
					+ "<input type='hidden' id='" + fieldName + "_h' name='" + fieldName + "_h' />"
					+ "<input type='hidden' id='" + fieldName + "_demosize' value='" + HWdemo_img_size + "' />"
					+ "</td><td>&nbsp;</td></tr></table>";
			edit.innerHTML = a1+a2+a3;
			$(edit).show();			
			
			
			//初始化截图框 	
			//(function initsHW(){
				var imageUrl = imgurl;
				$("#"+id+"_src").attr("src",imageUrl);
				
				if(id=="picture1"){
					$("img[key='picture1_demo']").attr("src",imageUrl);
					$("img[key='WWWPropaPicture3_demo']").attr("src",imageUrl);
				} else {
				    $("img[key='"+id+"_demo']").attr("src",imageUrl);
				}
			//})();
			var parameters = {};
			//(function inits1HW(){
				//w,h目标尺寸
				var w = parseInt(size.split("x")[0]);
				var h =  parseInt(size.split("x")[1]);
				//src_w,src_h源图尺寸
				var src_w = parseInt(srcImgSize.split("x")[0]);
				var src_h = parseInt(srcImgSize.split("x")[1]);
				
				//默认选区范围
				var sel_x1 = 0;
				var sel_y1 = 0;
				var sel_x2 = 30;
				var sel_y2 = 30;
				
				if(src_w==w && src_h ==h){
					sel_x2 = w;
					sel_y2 = h;
					sel_x1 = 0;
					sel_y1 = 0;
				}
				else{
					//选区范围水平居中,宽度为src_w的1/2, -5作为微调,避免溢出
					sel_x1 = src_w/4 - 5;
					sel_x2 = 3*src_w/4 - 5;
					//根据目标比例计算选区高度
					sel_y2 = h*(sel_x2-sel_x1)/w;
					
					//选区高度在src_h范围内,调整为垂直居中
					if(sel_y2<src_h){
						sel_y1 = (src_h - sel_y2)/2;
						sel_y2 = sel_y1 + sel_y2;
					}
					//超过了src_h的高度,以src_h -10 为最大高度为基准,按比例调整宽度
					else{
						sel_y1 = 5;
						sel_y2 = src_h - 10;
						sel_x2 = w*(sel_y2-sel_y1)/h;
						sel_x1 = (src_w - sel_x2)/2;
						sel_x2 = sel_x1+sel_x2;
					}
				}
				var a = 50;
				var select = { x1: sel_x1, y1: sel_y1, x2: sel_x2, y2: sel_y2,width:sel_x2-sel_x1,height:sel_y2-sel_y1 };
				$('#'+id+"_src").imgAreaSelect({ x1: sel_x1, y1: sel_y1, x2: sel_x2, y2: sel_y2 });
				
				if(picture1_hwflag=='h'){
					picture1_needWith='240';
					picture1_needHight='320';	
					WWWPropaPicture3_needWith='135';
					WWWPropaPicture3_needHight='180';
				}else if(picture1_hwflag=='w'){
					picture1_needWith='320';
					picture1_needHight='240';	
					WWWPropaPicture3_needWith='180';
					WWWPropaPicture3_needHight='135';					
				}
								
				if(id=="picture1"){
					changImage($("img[key='picture1_demo']"),  src_w, src_h,picture1_needWith,picture1_needHight, select);
					changImage($("img[key='WWWPropaPicture3_demo']"),  src_w, src_h, WWWPropaPicture3_needWith,WWWPropaPicture3_needHight, select);
				} else {
				    changImage($("img[key='"+id+"_demo']"), src_w, src_h, demosize.split("x")[0], demosize.split("x")[1], select);
				}
				defaultSelect($('#'+id+"_src"),select);
				var error = document.getElementById(id + "_error");
				error.innerHTML = "";
				$(error).hide();
			//})();

			//初始化选区的时候给坐标参数赋值 
			function defaultSelect(image, selection){
				if (!selection.width || !selection.height) 
					return;
				$('#'+ id + '_x1').val(selection.x1);
				$('#'+ id + '_y1').val(selection.y1);
				$('#'+ id + '_x2').val(selection.x2);
				$('#'+ id + '_y2').val(selection.y2);
				$('#'+ id + '_w').val(selection.width);
				$('#'+ id + '_h').val(selection.height);
			}
			$(function(){
				var w = parseInt(size.split("x")[0]);
				var h =  parseInt(size.split("x")[1]);
				$('#'+id+"_src").imgAreaSelect({
					aspectRatio: w+":" +h,
					handles: true,
					fadeSpeed: 200,
					onSelectChange: HWpreview
				});
			});
			
			function tijiao(){
				parameters.name = "flower2.jpg";
				$.ajax({
					type: "POST",
					url: "/cutImageAction.do",
					data: parameters,
					success: function(json){
						//alert(json.ret);
					},
					error: function(){
						alert("提交失败");
					}
				});
			}
}

function HWpreview(image, selection){
	var id = $(image).attr("fieldName");
	var demosize = $("#" + id + "_demosize").attr("value");
	//alert(selection+"\n"+$("img[key='"+id+"_demo']"));
	//alert(id+"\n" + demosize);
	if (!selection.width || !selection.height) 
		return;

	$('#'+ id + '_x1').val(selection.x1);
	$('#'+ id + '_y1').val(selection.y1);
	$('#'+ id + '_x2').val(selection.x2);
	$('#'+ id + '_y2').val(selection.y2);
	$('#'+ id + '_w').val(selection.width);
	$('#'+ id + '_h').val(selection.height);
	
	if(picture1_hwflag=='h'){
		picture1_needWith='240';
		picture1_needHight='320';	
		WWWPropaPicture3_needWith='135';
		WWWPropaPicture3_needHight='180';
	}else if(picture1_hwflag=='w'){
		picture1_needWith='320';
		picture1_needHight='240';	
		WWWPropaPicture3_needWith='180';
		WWWPropaPicture3_needHight='135';					
	}
	
	if(id=="logo4"){
		changImage($("img[key='logo1_demo']"), image.width, image.height, 30, 30, selection);
		changImage($("img[key='logo2_demo']"), image.width, image.height, 34,34, selection);
		changImage($("img[key='logo3_demo']"), image.width, image.height, 50,50, selection);
		changImage($("img[key='logo4_demo']"), image.width, image.height, 65,65, selection);
		changImage($("img[key='logo5_demo']"), image.width, image.height, 140,140, selection);
		changImage($("img[key='WWWPropaPicture2_demo']"), image.width, image.height, 70,70, selection);
	} else if(id=="picture1"){
		changImage($("img[key='picture1_demo']"), image.width, image.height, picture1_needWith,picture1_needHight, selection);
		changImage($("img[key='WWWPropaPicture3_demo']"), image.width, image.height, WWWPropaPicture3_needWith,WWWPropaPicture3_needHight, selection);
	} else {
	    changImage($("img[key='"+id+"_demo']"), image.width, image.height, demosize.split("x")[0], demosize.split("x")[1], selection);
	}
	
//	window.status = image.height + ":" + demosize.split("x")[0] + ":" + demosize.split("x")[1];
}

function qualityImg(fieldName){
	var quality = $('#'+ fieldName + '_FN').parent().find("[name='quality']").val(); 
	$('#'+ fieldName + '_FN').parent().find("[name='quality']").next().html('');
	if(quality!=''){
		if(!(quality>=0&&quality<=1)){
			var aa = $('#'+ fieldName + '_FN').parent().find("[name='quality']");
			$(aa).next().append("<img src='/oms/style/newstyle/images/error_wrong.gif'/>截图质量参数在0.0-1.0之间");
			aa.focus();
			return false;
		}
	}
	return quality;
}

