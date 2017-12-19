/*
*Jquery �򵥵�������
*��Ҫjquery1.3.2�������ϰ汾֧��
*By ��ǿ 2011.01
*1.3��
* ���һ����ʱ�Զ��رյ�����Ĺ���
* ����˵��������ʾλ�õĸ���ѡ��
*����ٷ���ַ��http://www.skygq.com/2010/01/05/jquery-simple-popup-overlay-1-3/
*/
;(function($){
	//��ҳ��װ��CSS��ʽ
	var css = '<style type="text/css">#skygqOverlay{position:fixed;z-index:2000;left:0;top:0;width:100%;height:100%;background:black;}.wrap_out{border:5px solid #000 opacity: 0.2;padding:5px;background:#eee;box-shadow:0 0 6px rgba(0,0,0,.5);-moz-box-shadow:0 0 6px rgba(0,0,0,0.5);-webkit-box-shadow:0 0 6px rgba(0,0,0,.5);position:fixed;z-index:2001;}.wrap_in{background:#fafafa;border:1px solid #ccc;}.wrap_bar{width:100%;background:#f7f7f7;border-top:3px solid #f9f9f9;border-bottom:4px solid #eee;margin-top:2px;}.wrap_title{line-height:5px;background:#f3f3f3;border-top:4px solid #f5f5f5;border-bottom:5px solid #f1f1f1;margin-top:3px;}.wrap_title span{position:relative;margin-left:10px;cursor:text;}.wrap_body{display:inline-block;border-top:1px solid #ddd;background:white;}.wrap_close{margin-top:-18px;color:#34538b;font-weight:bold;margin-right:10px;font-family:Tahoma;text-decoration:none;cursor:pointer;float:right;}.wrap_close:hover{text-decoration:none;color:#f30;}.submit_btn{display:inline-block;padding:3px 12px 1.99px;background:#486aaa;border:1px solid;border-color:#a0b3d6 #34538b #34538b #a0b3d6;color:#f3f3f3;line-height:16px;cursor:pointer;overflow:visible;}.submit_btn:hover{text-decoration:none;color:#ffffff;}.cancel_btn{display:inline-block;padding:3px 12px 1.99px;background:#eee;border:1px solid;border-color:#f0f0f0 #bbb #bbb #f0f0f0;color:#333;line-height:16px;cursor:pointer;overflow:visible;}</style>';
	$("head").append(css);
	var ie6 = ($.browser.msie && $.browser.version < 7);
	var timeout;
	$.fn.skygqbox = function(options){
		if (!this.length) {	return this;}
		var s = $.extend({}, $.fn.skygqbox.Default, options || {});
		return this.each(function(){
			$.skygqbox($(this),s)
		});
	};

	$.skygqbox = function(elements,s){
		if ($(elements).length == 0){
			elements = $("<span></span>").append(elements);
		}
		var s = $.extend({}, $.fn.skygqbox.Default, s || {});
		if ($("#skygqOverlay").length > 0){
			return;
		}
		//�������ʾ��ʼ��
		var WRAP = '<div id="skygqOverlay"></div><div class="wrap_out" id="wrapOut"><div class="wrap_in" id="wrapIn"><div id="wrapBar" class="wrap_bar"  onselectstart="return false;"><div class="wrap_title"><span>'+s.title+'</span></div><a href="javascript:void(0);" class="wrap_close" id="wrapClose">'+s.shut+'</a></div><div class="wrap_body" id="wrapBody"></div></div></div>';
		$("body").append(WRAP);
		if (typeof (timeout) != "undefined"){
			clearTimeout(timeout);
		}

		//һЩԪ�ض���
		$.o = {
			s: s,
			ele: elements,
			bg: $("#skygqOverlay"),
			out: $("#wrapOut"),
			bar: $("#wrapBar"),
			clo: $("#wrapClose"),
			bd: $("#wrapBody")
		};
		elements.show();
		$.o.bd.append(elements);
		//�ߴ�
		$.skygqbox.setSize();
		//��λ
		$.skygqbox.setPosition();
		$.o.clo.click(function(){
			$.skygqbox.hide();
		});

		if(s.autoClose > 0){
			timeout = setTimeout($.skygqbox.hide, s.autoClose);
		}
	};
	$.skygqbox.getSize = function(o){
		//��ȡ����Ԫ�صĸ߿�
		var w_h = {};
		$('<div id="wrapClone" style="position:absolute;left:-6000px;"></div>').appendTo("body").append(o.clone());
		w_h.w = $("#wrapClone").width();
		w_h.h = $("#wrapClone").height();
		$("#wrapClone").remove();
		return w_h;
	};
	$.skygqbox.setSize = function(){
		if(!$.o.bd.size() || !$.o.ele.size() || !$.o.bd.size()){
			return;
		}
		//�������ݵĳߴ�
		var bd_w = parseInt($.o.s.width, 10), bd_h = parseInt($.o.s.height, 10);
		if(!bd_w || bd_w <= 0 ){
			var x_size = $.skygqbox.getSize($.o.ele), w = $(window).width();
			//����Զ�
			bd_w = x_size.w;
			if(bd_w < 50){
				bd_w = 120;
			}else if(bd_w > w){
				bd_w = w - 120;
			}
		}
		$.o.bd.css("width", bd_w);
		$.o.out.css("width", bd_w+2);
		if(bd_h > 0){
			$.o.bd.css("height", bd_h);
		}
		return $.o.bd;
	};
	$.skygqbox.setPosition = function(){
		if(!$.o.bg.size() || !$.o.ele.size() || !$.o.out.size()){
			return;
		}
		var w = $(window).width(),
		h = $(window).height(),
		ph = $("body").height();
		if(ph < h){
			ph = h;
		}
		$.o.bg.css("opacity", $.o.s.opacity);
		if (ie6){
			$.o.bg.css({
				position:"absolute",
				width:w,
				height:ph
			});
			$.o.out.css("position","absolute");
		}
		//�������ݵ�λ��
		//��ȡ��ǰ����Ԫ�صĳߴ�
		var xh = $.o.out.outerHeight(), xw = $.o.out.outerWidth();

		//�����㶨λ��
		switch($.o.s.position){
			case "middle":
				$.o.out.css({
					"top":"50%",
					"left":"50%",
					"marginLeft": '-' + parseInt((xw / 2),10) + 'px',
					"width": xw + 'px',zIndex:$.o.s.index
				});
				if ( !($.browser.msie && $.browser.version < 7)) { // ����IE6
					$.o.out.css({marginTop: '-' + parseInt((xh / 2),10) + 'px'});
				}
				break;
			case "left_top":
				$.o.out.css({
					"top":0,
					"left":0,
					"width": xw + 'px',zIndex:$.o.s.index
				});
				break;
			case "left_middle":
				$.o.out.css({
					"top":"50%",
					"left":0,
					"width": xw + 'px',zIndex:$.o.s.index
				});
				if ( !($.browser.msie && $.browser.version < 7)) { // ����IE6
					$.o.out.css({marginTop: '-' + parseInt((xh / 2),10) + 'px'});
				}
				break;
			case "left_bottom":
				$.o.out.css({
					"bottom":0,
					"left":0,
					"width": xw + 'px',zIndex:$.o.s.index
				});
				break;
			case "top_middle":
				$.o.out.css({
					"top":"0",
					"left":"50%",
					"marginLeft": '-' + parseInt((xw / 2),10) + 'px',
					"width": xw + 'px',zIndex:$.o.s.index
				});
				break;
			case "right_top":
				$.o.out.css({
					"top":0,
					"right":0,
					"width": xw + 'px',zIndex:$.o.s.index
				});
				break;
			case "right_middle":
				$.o.out.css({
					"top":"50%",
					"right":0,
					"width": xw + 'px',zIndex:$.o.s.index
				});
				if ( !($.browser.msie && $.browser.version < 7)) { // ����IE6
					$.o.out.css({marginTop: '-' + parseInt((xh / 2),10) + 'px'});
				}
				break;
			case "right_bottom":
				$.o.out.css({
					"bottom":0,
					"right":0,
					"width": xw + 'px',zIndex:$.o.s.index
				});
				break;
			case "bottom_middle":
				$.o.out.css({
					"bottom":"0",
					"left":"50%",
					"marginLeft": '-' + parseInt((xw / 2),10) + 'px',
					"width": xw + 'px',zIndex:$.o.s.index
				});
				break;
			default:
				$.o.out.css({
					"top":"50%",
					"left":"50%",
					"marginLeft": '-' + parseInt((xw / 2),10) + 'px',
					"width": xw + 'px',zIndex:$.o.s.index
				});
				if ( !($.browser.msie && $.browser.version < 7)) { // ����IE6
					$.o.out.css({marginTop: '-' + parseInt((xh / 2),10) + 'px'});
				}
				break;
		}
		return $.o.out;
	};
	$.skygqbox.hide = function(){
		if($.o.ele && $.o.out.size() && $.o.bg.size()){
			$.o.ele.clone(true).appendTo($("body")).hide();
			$.o.out.fadeOut("fast", function(){
				$(this).remove();
			});
			$.o.bg.fadeOut("fast", function(){
				$(this).remove();
			});
		}
		return false;
	};
	$.fn.skygqbox.Default = {
		title		: "�Ի���",
		shut		: "�ر�",
		index		: 2000,
		opacity		: 0.5,
		width		: "auto",
		height		: "auto",
		autoClose	: 0,//������ȴ��೤ʱ���Զ��ر�(��λ������) 0���߸����������Զ��ر�
		position	: "middle"
	};
})(jQuery);