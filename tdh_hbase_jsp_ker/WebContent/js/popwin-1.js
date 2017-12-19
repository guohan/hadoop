$(document).ready(function(){
jQuery.extend({createClass : function(){return function(){this.initialize.apply(this, arguments);}} });
jQuery.extend({OverLay     : $.createClass()});
jQuery.extend({myWindow    : $.createClass()});
jQuery.extend({msn         : $.createClass()});

var isIE6 = $.browser.msie && ([/MSIE (\d)\.0/i.exec(navigator.userAgent)][0][1] == 6);
$.OverLay.prototype = { 
	  initialize: function(options){
		this.SetOptions(options);
		this.Lay = $(document.createElement("div"));
		$('body').append(this.Lay)
		this.Color = this.options.Color;
		this.Opacity = parseInt(this.options.Opacity);
		this.zIndex = parseInt(this.options.zIndex);	
		this.Lay.css({display:"none",zIndex:this.zIndex,left:0,top:0,position:"fixed",width:"100%",height:(Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight)+"px")})
		if(isIE6){
			this.Lay.css({position : "absolute"});
			this._resize =(function(object, fun) {
					return function() {return fun.apply(object, arguments);}
				})(this,function(){
				this.Lay.css({width:Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth) + "px",height:Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) + "px"});
				})
		this.Lay.html('<iframe style="position:absolute;top:0;left:0;width:100%;height:100%;filter:alpha(opacity=0);"></iframe>');
		}
	  },
	  SetOptions: function(options){
		this.options = {Color:"#666",Opacity:50,zIndex:1};
		jQuery.extend(this.options,options || {});
	  },
	  Show: function(){
		if(isIE6){ this._resize(); $(window).resize(this._resize)}
		this.Lay.css({backgroundColor:this.Color,display:"block",position:"absolute"})
		if($.browser.msie) {this.Lay.css({filter : "alpha(opacity:" + this.Opacity + ")"})} else {this.Lay.css({opacity : this.Opacity / 100})}
	  },
	  Close: function(){
		this.Lay.css({display : "none"});
		if(isIE6){ $(window).resize(function(){})}
	  }
};

$.myWindow.Oarrs=[]; 
$.myWindow.setOarrs=function(options){  
    var arr=$.myWindow.Oarrs;
	if(arr.length>1 && options.layer.attr("id")==arr[arr.length-1].layer.attr("id")) return;
    for ( var i=0;i<arr.length;i++ ){
	    if(arr[i].layer.attr("id")==options.layer.attr("id")){	       
		   arr.splice(i,1);
		}
    }
	arr.push(options);
	if(arr.length>=2) arr[arr.length-1].index=Number(arr[arr.length-2].index)+1;
	arr[arr.length-1].layer.css({zIndex:arr[arr.length-1].index})
}
$.myWindow.prototype={ 
	  initialize: function(box, options){
		 this.Box = $("#"+box); 
		 this.SetOptions(options);
		 this.Fixed = !!this.options.Fixed;
		 this.Over = !!this.options.Over;
		 this.Center = !!this.options.Center;
		 this.Isshow = false; 
		 if(this.Over) this.OverLay = new $.OverLay(options); 
		 this.Box.css({
		      zIndex:(($.myWindow.Oarrs.length==0) ? 2 : $.myWindow.Oarrs[$.myWindow.Oarrs.length-1].index+1),display:"none"}
			  );
		 if(isIE6){
			this._top = this._left = 0;
			this._resize =(function(object, fun){
					return function() {	return fun.apply(object, arguments);}
				  })(this,function(){
					this.Center ? this.SetCenter() : this.SetFixed();
				  })
		 };
		 
		 this.Box.mousedown(function(event){
		        $.myWindow.setOarrs({layer:$(this),index:Number($(this).css("z-index"))});
				event.stopPropagation(); 
				});
	  },
	  SetOptions: function(options){
		this.options = {Over:true,Fixed:true,Center:true};
		jQuery.extend(this.options, options || {});
	  },
	  SetFixed: function(){
	    this.Box.css({
	       top:(document.documentElement.scrollTop-this._top+this.Box.top()+"px"),
	       left:(document.documentElement.scrollLeft-this._left+ this.Box.left()+"px")
	     });
		this._top = document.documentElement.scrollTop; this._left = document.documentElement.scrollLeft;
	  },
	  SetCenter: function(){
		this.Box.css({
		   marginTop:(document.documentElement.scrollTop-this.Box.height()/2 +"px"),
		   marginLeft:(document.documentElement.scrollLeft-this.Box.width()/2 +"px")
		});
	  },
	  S:function(options){
        if(this.Isshow) return;   
	    this.Isshow=true;
	    $.myWindow.setOarrs({layer:this.Box,index:Number(this.Box.css("z-index"))}); 

		this.Box.css({position:(this.Fixed && !isIE6 ? "fixed" : "absolute"),display:"block"});
		this.Over && this.OverLay.Show();
		if(this.Center){
			this.Box.css({top:(parent.parent.window.document.documentElement.scrollTop + "px"),left:"50%"});
			if(this.Fixed){
				this.Box.css({marginTop:(-this.Box.height()/2+300+"px"),marginLeft:(-this.Box.width()/2+"px")});
			}else{
				this.SetCenter();
			}
		}		
		if(isIE6){
			this.Center ? this.SetCenter() : this.Fixed && this.SetFixed();
			this._resize();
			window.attachEvent("onscroll", this._resize);
		}
	  },
	  C:function(){
	    this.Isshow=false;
		this.Box.css({display:"none"});
		this.OverLay.Close();
		if(isIE6){
			window.detachEvent("onscroll", this._resize);
		}
	  }
};


var mymsn=function(){
    this.Show=function(){
	    this.S();
	}
	this.Close=function(){
	    this.C();
	}
}
mymsn.prototype=new $.myWindow();


$.msn.prototype=new mymsn(); 
});