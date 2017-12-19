/*tab*/
var tab=function(oT,oC,t){
	var a=setTimeout(function(){
		$(oT).bind(t,function(){
			$(oT).removeClass("current");
			$(oC).hide();
			$(this).addClass("current");
			$(oC).eq($(this).index()).fadeIn().show();
		})
	},300);
	clearTimeout(this.a);
}

/*tip*/
var mytip=function(){
	
}

function eoddTble(obj,t1,t2,t3,n){
   	var o=$(obj);
	var m=0;
	var tempBg;
	if(n!=""){m=n;}
	for (i=m;i<o.length;i++) { 
		var bgc= (i%2==0)? t1:t2; 
		o.eq(i).css("background-color",bgc); 
	}
	o.hover(function(){
			tempBg=$(this).css("background-color");
			$(this).css("background-color",t3);
		},function(){
			$(this).css("background",tempBg);
	});
}