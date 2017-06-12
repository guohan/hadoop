var radius = 5;//定义小球的半径
var idlist = {};//key代表工单id，value代表小球id
var orderidlist={};
var arealist = {};
var gdlist=[];

var clickbody=null;//保存点击的物体实例
var boxes = null;
var digitOfBoxes = new Array(0,0,0,0,0,0,0,0,0,0)//长度为10的数组，保存每个归档区的工单数量

var gdBoxes = new Array("报修","报装","咨询","查询","投诉","举报","表扬","建议","意见","其他")//长度为10的数组，保存每个归档区的工单数量
var Engine = Matter.Engine,
Render = Matter.Render,
Runner = Matter.Runner,
Body = Matter.Body,
Events = Matter.Events,
Composite = Matter.Composite,
Composites = Matter.Composites,
Common = Matter.Common,
MouseConstraint = Matter.MouseConstraint,
Mouse = Matter.Mouse,
World = Matter.World,
Bodies = Matter.Bodies;
//create an engine
var engine = Engine.create();

// create a renderer
//var render = Render.create({
//    element: document.body,
//    engine: engine,
//	options:{
//	wireframes:false,
//	width:1200,
//	height:600
//}
//});

// create renderer 创建界面大小
var render = Render.create({
//    element: document.body,
    element:document.getElementById("myCanvas"),
    engine: engine,
    options: {
    	wireframes:false,
        width: Math.min(document.documentElement.clientWidth, 500),
        height: Math.min(document.documentElement.clientHeight, 500)
    }
});


$(function(){
	//建立和工程相关的连接 工程名称 endpoint
	var socket = new SockJS('/bigdata/random');
	var client = Stomp.over(socket);
	var count =0;
	//添加用户名 密码进行鉴权连接 订阅连接主题 画图
	client.connect('user', 'password', function(frame) {
	//订阅主题
	  client.subscribe("/data1", function(message) {
		var data = JSON.parse(message.body);
		var currentversion = 0;
		jQuery.each(data.list, function(i,vh) {
//			alert("id"+vh.workorder_id);
//			 alert("id"+vh.workorder_id+"color"+vh.color+"flow"+vh.current_flow+"area"+vh.depart_id+"bid"+vh. business_classifyid);
			 add(vh.workorder_id,vh.color,vh.current_flow,vh.depart_id,vh.business_classifyid,vh.task_instance_id); //创建动画
		});
		if(count<1){
			jQuery.each(data.gdlist, function(i,vh) {
					 digitOfBoxes = new Array(vh.repair_qty,vh.install_qty,vh.consult_qty,
							 vh.query_qty,vh.complaint_qty,vh.report_qty,
							 vh.praise_qty,vh.suggest_qty,vh.opinion_qty,
							 vh.other_qty);//长度为10的数组，保存每个归档区的工单数量
			});
		}
		count=1;
	//
	  });
	});

	
var defaultCategory = 0x0001,
        ballCategory = 0x0002,
        filterCategory = 0x0004,
        finishCategory = 0x0008;
var redColor = '#FF0000',
//redColor = '#C44D58',
        yellowColor = '#FFFF00',
//        greenColor = '#C7F464';
        greenColor = '#008800';

//解决小球穿透问题
var redoptions={continuous:1,collisionFilter: {
    category: ballCategory
},render: {
	strokeStyle:'transparent',
fillStyle: redColor
}},
yellowoptions={continuous:1,collisionFilter: {
    category: ballCategory
},render: {
	strokeStyle:'transparent',
fillStyle: yellowColor
}},
greenoptions={continuous:1,collisionFilter: {
    category: ballCategory
},render: {
	strokeStyle:'transparent',
fillStyle: greenColor
}};




//创建归档区域
var boxA = Bodies.circle(150, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
//var boxB = Bodies.rectangle(450, 50, 80, 80,blueoptions);
var boxB =Bodies.circle(250, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var boxC = Bodies.circle(350, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var boxD = Bodies.circle(450, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
//var boxB = Bodies.rectangle(450, 50, 80, 80,blueoptions);
var boxE =Bodies.circle(550, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
    
} });



var boxF = Bodies.circle(650, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var boxG = Bodies.circle(750, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
//    ,sprite: {
//	      texture: './1.png',
//	      xScale: 2,
//	      yScale: 2
//	    }
} });
//var boxB = Bodies.rectangle(450, 50, 80, 80,blueoptions);
var boxH =Bodies.circle(850, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var boxI = Bodies.circle(950, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var boxJ = Bodies.circle(1050, 650, 20,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

//++++++++++++++++++++++++++++++++++++++++++渠道漏斗 start++++++++++++++++++++++++++++++++++++++++++++++++++==
//四根竖线
var ld2 = Bodies.rectangle(50, 110, 10,150, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
var ld5 = Bodies.rectangle(450, 110, 10,150, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });


var ld8 = Bodies.rectangle(50, 310, 10,150, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
var ld11 = Bodies.rectangle(450, 310, 10,150, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

//画渠道漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var ld1= Bodies.rectangle(100, 25, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var ld12 = Bodies.rectangle(400, 25, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second
var ld3 = Bodies.rectangle(100, 200, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var ld10=Bodies.rectangle(400, 200, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });
var ld4= Bodies.rectangle(100, 225, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var ld9 = Bodies.rectangle(400, 225, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second
var ld6 = Bodies.rectangle(100, 400, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var ld7=Bodies.rectangle(400, 400, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });
//漏斗挡板
var lddb1 = Bodies.rectangle(245, 210, 240, 10, {
            collisionFilter: {
                mask: ballCategory
            },isStatic: true ,render: {
            fillStyle: 'transparent',
            lineWidth: 0
        } });
var lddb2 = Bodies.rectangle(245, 410, 240, 10, {
            collisionFilter: {
                mask: filterCategory | ballCategory
            },isStatic: true ,render: {
            fillStyle: 'transparent',
            lineWidth: 0
        } });

//++++++++++++++++++++++++++++++++++++++渠道漏斗 end++++++++++++++++++++++++++++++++++++++++++++++++


//区域部分漏斗

//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var area1= Bodies.rectangle(900, 25, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var area6 = Bodies.rectangle(1100, 25, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var area3 = Bodies.rectangle(900, 150, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var area4=Bodies.rectangle(1100, 150, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });


var area2 = Bodies.rectangle(850, 90, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
var area5 = Bodies.rectangle(1150, 90, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var areadb1 = Bodies.rectangle(1000, 160, 150, 10, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });



//create pic
//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var area12= Bodies.rectangle(600, 25, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var area62 = Bodies.rectangle(750, 25, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var area32 = Bodies.rectangle(600, 150, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var area42=Bodies.rectangle(750, 150, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });


var area22 = Bodies.rectangle(550, 90, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
var area52 = Bodies.rectangle(800, 90, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var areadb12 = Bodies.rectangle(650, 160, 150, 10, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });





//create pic
//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var area13= Bodies.rectangle(600, 200, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var area63 = Bodies.rectangle(750, 200, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var area33 = Bodies.rectangle(600, 310, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var area43=Bodies.rectangle(750, 310, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });


var area23 = Bodies.rectangle(550, 260, 10,100, {
  collisionFilter: {
      mask: ballCategory
  },isStatic: true ,render: {
  fillStyle: 'transparent',
  lineWidth: 0
} });
var area53 = Bodies.rectangle(800, 260, 10,100, {
  collisionFilter: {
      mask: ballCategory
  },isStatic: true ,render: {
  fillStyle: 'transparent',
  lineWidth: 0
} });

var areadb13 = Bodies.rectangle(650, 320, 150, 10, {
  collisionFilter: {
      mask: ballCategory
  },isStatic: true ,render: {
  fillStyle: 'transparent',
  lineWidth: 0
} });



//create pic
//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var area14= Bodies.rectangle(900, 200, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var area64 = Bodies.rectangle(1100, 200, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var area34 = Bodies.rectangle(900, 310, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var area44=Bodies.rectangle(1100, 310, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });


var area24 = Bodies.rectangle(850, 260, 10,100, {
collisionFilter: {
    mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });
var area54 = Bodies.rectangle(1150, 260, 10,100, {
collisionFilter: {
    mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });

var areadb14 = Bodies.rectangle(1000, 320, 150, 10, {
collisionFilter: {
    mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });




//create pic
//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var area15= Bodies.rectangle(600, 350, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var area65 = Bodies.rectangle(750, 350, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var area35 = Bodies.rectangle(600, 450, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var area45=Bodies.rectangle(750, 450, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });


var area25 = Bodies.rectangle(550, 400, 10,100, {
collisionFilter: {
    mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });
var area55 = Bodies.rectangle(800, 400, 10,100, {
collisionFilter: {
    mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });

var areadb15 = Bodies.rectangle(650, 460, 150, 10, {
collisionFilter: {
    mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });



//create pic
//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var area16= Bodies.rectangle(900, 350, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var area66 = Bodies.rectangle(1100, 350, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var area36 = Bodies.rectangle(900, 450, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var area46=Bodies.rectangle(1100, 450, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });


var area26 = Bodies.rectangle(850, 400, 10,100, {
collisionFilter: {
  mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });
var area56 = Bodies.rectangle(1150, 400, 10,100, {
collisionFilter: {
  mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });

var areadb16 = Bodies.rectangle(1000, 460, 150, 10, {
collisionFilter: {
  mask: ballCategory
},isStatic: true ,render: {
fillStyle: 'transparent',
lineWidth: 0
} });

// add all of the bodies to the world
//World.add(engine.world, [boxA, boxB, ground1,ground2,funnel,groundLeft,groundRight,groundTop,boxC]);
World.add(engine.world, [lddb1,lddb2,ld1,ld2,ld3,ld4,ld5,ld6,ld7,ld8,ld9,ld10,ld11,ld12
                         ]);
//ground1,ground2,groundLeft,groundRight,groundTop
// run the engine
//engine.pair.isActive = false;
//engine.world.gravity.y=0;

//engine.world.gravity.x = 355;
engine.world.gravity.y = 0.3;
//engine.world.gravity.isPoint = true;
Engine.run(engine);



// run the renderer

Render.run(render);

//监听鼠标点击
var mouse = Mouse.create(render.canvas),
mouseConstraint = MouseConstraint.create(engine, {
    mouse: mouse,
    constraint: {
        stiffness: 0.0,
        render: {
            visible: false
        }
    }
});
render.mouse = mouse;
boxes = new Array(boxA,boxB,boxC,boxD,boxE,boxF,boxG,boxH,boxI,boxJ)
//松开鼠标事件
Events.on(mouseConstraint, 'mouseup', function(event) {
if(clickbody != null){
	if(typeof(clickbody.continuous) != "undefined")
//		alert(clickbody.id);
//	alert(getorderid(clickbody.id));
//	alert( document.getElementById("workorder_id"));
	window.open('http://localhost:8080/bigdata1/queryAll.do?workorder_id='+getorderid(clickbody.id)[0]+'&task_instance_id='+getorderid(clickbody.id)[1]);
}
});

function getorderid(id){
	var orderid=orderidlist[id];
	return orderid;
	
}
//按下鼠标事件
Events.on(mouseConstraint, 'mousedown', function(event) {
//	alert("just test!");
clickbody = event.source.constraint.bodyB;

});
//绘制归档区的数字
Events.on(render, 'afterRender', function(event) {
    var context = render.context;
    context.font = "20px 'Cabin Sketch'";
    context.textAlign='center';
    context.fillStyle='black';
    context.textBaseline='middle';
//    context.fillText("白云区", byqlddb2.position.x, byqlddb2.position.y+20);
//    context.fillText("用电公司", ydgslddb2.position.x, ydgslddb2.position.y+20);
//    context.fillText("番禺区", pyqlddb2.position.x, pyqlddb2.position.y+20);
//    context.fillText("天河区", thqlddb2.position.x, thqlddb2.position.y+20);
//    context.fillText("海珠区",hzqlddb2.position.x, hzqlddb2.position.y+20);
//    context.fillText("越秀区", yxqlddb2.position.x, yxqlddb2.position.y+20);
    context.fillText("95598监控",  lddb2.position.x,  lddb2.position.y+20);
    
//   
//    for(i=0;i<boxes.length;i++){
//    	context.fillText(gdBoxes[i], boxes[i].position.x, boxes[i].position.y+30);
//    	context.fillText(digitOfBoxes[i], boxes[i].position.x, boxes[i].position.y);
//    }
    
    
    
    
});
World.add(engine.world, mouseConstraint);
//Events.on(engine, 'afterRender', function(event) {
//    var context = boxE.render.context;
//    context.font = "45px 'Cabin Sketch'";
//    context.fillText("THROW OBJECT HERE", 150, 80);
//});


//+++++++++++++++++++++++++++++++++++++

 
 
 
//+++++++++++++++++++++++++++++++++++

//小球下落 不阻挡小球掉落 filterCategory;//|
function down(ball){
	ball.collisionFilter.category=finishCategory|filterCategory;
	ball.collisionFilter.mask=filterCategory|defaultCategory;
}
//小球归档
function finish(id,ball,businessType){
//	var addBox = getAreaBody(name);
//	if(addBox==null){
	var home;
	if(businessType=="01"){
		home=boxA;
	}else if(businessType=="02"){
		home=boxB;
	}else if(businessType=="03"){
		home=boxC;
	}else if(businessType=="04"){
		home=boxD;
	}else if(businessType=="05"){
		home=boxE;
	}else if(businessType=="06"){
		home=boxF;
	}else if(businessType=="07"){
		home=boxG;
	}else if(businessType=="08"){
		home=boxH;
	}else if(businessType=="09"){
		home=boxI;
	}else{
		home=boxJ;
	}
	parabola(id,document.body,ball,{x:home.position.x , y:home.position.y},0,home);
	delete idlist[id];
	delete arealist[id];
	
	//Matter.World.remove(engine.world,ball);
}
//新增小球 流程状态 诉求审核  |受理诉求 | 业务处理    | 归档  
function add(id,color,status,area,businessType,instanceid){
//	alert("id"+id);
	var addBox = getBody(engine.world,id);
	if(addBox == null){
		var options;
		if(color == "yellow")options=yellowoptions;
		else if(color == "red")options=redoptions;
		else options = greenoptions;
		
		if(status == "诉求审核"){
			var numx = Math.random()*40 + 180;
			numx = parseInt(numx, 10);
			var numy = Math.random()*40 + 370;
			numy = parseInt(numy, 10);
			addBox = Bodies.circle(numx, numy, radius,options);
			orderidlist[addBox.id]=[id,instanceid];
		}
		else if(status == "受理诉求"){
			
			
			var numx = Math.random()*100 + 80;
			numx = parseInt(numx, 10);
			var numy = Math.random()*40 + 80;
			numy = parseInt(numy, 10);
			addBox = Bodies.circle(numx, numy, radius,options);
			orderidlist[addBox.id]=[id,instanceid];
		}
		idlist[id]=addBox.id;
		arealist[id]=status;
		World.add(engine.world,addBox);
	}else{
		var preColor = addBox.render.fillStyle;
//		alert(arealist[id]);
//		alert("area123"+area);
		
		var preStatus=arealist[id];
			arealist[id]=status;
//		if((status == "业务处理"&&typeof(preStatus) != "undefined"&&preStatus=="诉求审核")){
//			down(addBox);
//			
//		}
		if(status == "业务处理"){
			
//			ball.collisionFilter.category=finishCategory|filterCategory;
//			ball.collisionFilter.mask=filterCategory|defaultCategory;
			addBox.collisionFilter.group=-1;
//			down(addBox);
			var home=boxA;
			parabola(id,document.body,addBox,{x:home.position.x , y:home.position.y},0,home);
			delete idlist[id];
//			alert("真实的结束");
			
		}
		else if(status == "归档"){
			gdlist.push(id);
			finish(id,addBox,businessType);
			return;
		}
		else if(status == "诉求审核"){
			down(addBox);
		}
		if(color == "yellow")color = yellowColor;
		else if(color == "red")color = redColor;
		else color = greenColor;
		addBox.render.fillStyle = color;
		
		
	}
}





//根据id获得body
function getBody(world,id){
	if(typeof(idlist[id]) == "undefined")return null;
	return Matter.Composite.get(world,idlist[id],"body");
}

//根据id获得area body
function getAreaBody(world,id){
	if(typeof(arealist[id]) == "undefined")return null;
//	return Matter.Composite.get(world,arealist[id],"body");
}


//根据id获得area body
function getGDBody(world,btype){
	if(typeof(gdlist[btype]) == "undefined")return null;
//	return Matter.Composite.get(world,arealist[id],"body");
}

//获取窗口滚动的偏移量
//http://stackoverflow.com/questions/19618545/body-scrolltop-vs-documentelement-scrolltop-vs-window-pagyoffset-vs-window-scrol
function getScrollOffsets() {
      var x = window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0
      ,y = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
      
      return {x:x , y:y};
}

/**
 * parent source的父元素
 * source 要移动的小球
 * target 最终位置
 * flag 1则移动完成后产生小球(Matter.Body); 0则不产生小球。
 */
function parabola(id,parent,source,target,flag,home) {
	var pos = parent.getBoundingClientRect(),scrollOffset = getScrollOffsets();
	Body.setVelocity(source, { x: 0, y: 0 });

	source.collisionFilter.category=0x0000;
  var
    // 这里注意，因为位移使用了 left 和 top
    // 定位点是位于页面最左上角
    // 但是为了效果将中心点移到了页面的 (960,200)
    // 故起始点和终点的坐标都需要减去 (960,300)
//    startX = parseInt(ball.style.left),
//    startY = parseInt(ball.style.top),
    startX = source.position.x,
    startY = source.position.y,
    isMove = false;
    
  /**
   * 抛物线运动函数 y = a*x*x + b*x + c
   * @param  endX 终止坐标
   * @param  endY 起始坐标
   * @param  time 持续时间 ms
   * @return      [description]
   */
  function ballParabolaMove(endX, endY, time) {
    var a = 0.003,
      b = 0,
      c = 0,
      startTime = Date.now();
    /*
     * 抛物线公式：
     * y = a * x*x + b*x;
     * y1 = a * x1*x1 + b*x1;
     * y2 = a * x2*x2 + b*x2;
     * 假设已知参数 a ，利用两点坐标可得 b，c：
     * b = ((y2-y1)+ a(x2*x2-x1*x1)) / (x2-x1)
     */
    if(endX != startX){
    	b = ((endY - startY) - a * (endX * endX - startX * startX)) / (endX - startX);
    	c = endY - a * endX * endX - b * endX;
    }
    /**
     * 利用抛物线公式进行运动 y = a*x*x + b*x + c
     * @function parabolaRAF RAF句柄
     * @return {[type]}  [description]
     */
    requestAnimationFrame(function parabolaRAF() {
      var t = Math.min(1.0, (Date.now() - startTime) / time),
        tx = (endX - startX) * t + startX,
        ty = a * tx * tx + b * tx + c;
      if(endX == startX)ty=(endY - startY) * t + startY;
      Body.setPosition(source, { x: parseInt(tx), y: parseInt(ty) });
      Body.setVelocity(source, { x: 0, y: 0 });
      if (t < 1.0) requestAnimationFrame(parabolaRAF);
      else{
          //抛物线结束之后，在目标漏斗创建小球并移除临时小球。
          //document.body.removeChild(ball);
          if(flag==1){
        	  Body.setVelocity(source, { x: 0, y: 0 });
        	  source.render.fillStyle=greenColor;
        	  down(source);
//        	  source.collisionFilter.category=ballCategory;
//        	  source.collisionFilter.mask=0xffff;
          }
          else if(flag==0){
        	  for(i=0;i<boxes.length;i++){
        			if(boxes[i].id==home.id){
        				digitOfBoxes[i]++;
        				break;
        			}
        		}
        	  World.remove(engine.world,source);
          }
      }
    })
  }
  ballParabolaMove(target.x,target.y,4000);
};
});