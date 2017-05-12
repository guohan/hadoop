var radius = 4;//定义小球的半径
var idlist = {};//key代表工单id，value代表小球id
$(function(){
	var Engine = Matter.Engine,
    Render = Matter.Render,
    World = Matter.World,
    Bodies = Matter.Bodies;
    Events = Matter.Events;
var defaultCategory = 0x0001,
        ballCategory = 0x0002,
        filterCategory = 0x0004,
        finishCategory = 0x0008;
var redColor = '#FF0000',
        yellowColor = '#FFFF00',
        greenColor = '#008800';

var redoptions={collisionFilter: {
                        category: ballCategory
                    },render: {
                    	strokeStyle:'transparent',
            fillStyle: redColor
        }},
yellowoptions={collisionFilter: {
                        category: ballCategory
                    },render: {
                    	strokeStyle:'transparent',
            fillStyle: yellowColor
        }},
greenoptions={collisionFilter: {
                        category: ballCategory
                    },render: {
                    	strokeStyle:'transparent',
            fillStyle: greenColor
        }};

// create an engine
var engine = Engine.create();

// create a renderer
var render = Render.create({
    element: document.body,
    engine: engine,
	options:{
	wireframes:false,
	width:1200,
	height:600
}
});



// create two boxes and a ground
//var funnel = Bodies.fromVertices(300,0,[{ x: 0, y: 0 },{ x: 10, y: 0 },{ x: 10, y:100 },{ x: 100, y: 100 }],redoptions);

var funnel = Bodies.circle(200, 100, 10,yellowoptions);

var boxA = Bodies.circle(350, 500, 50,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
//var boxB = Bodies.rectangle(450, 50, 80, 80,blueoptions);
var boxB =Bodies.circle(650, 500, 50,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

var boxC = Bodies.circle(950, 500, 50,{
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });



//四根竖线
var ld2 = Bodies.rectangle(250, 90, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
var ld5 = Bodies.rectangle(500, 90, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });


var ld8 = Bodies.rectangle(250, 240, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
var ld11 = Bodies.rectangle(500, 240, 10,100, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

//画漏斗 第一位 平移位置  第二位 竖位置 第三位 漏斗长短 第四位 大小
var ld1= Bodies.rectangle(300, 25, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var ld12 = Bodies.rectangle(450, 25, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second

var ld3 = Bodies.rectangle(300, 150, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var ld10=Bodies.rectangle(450, 150, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });




var ld4= Bodies.rectangle(300, 175, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });//first
var ld9 = Bodies.rectangle(450, 175, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });//second



var ld6 = Bodies.rectangle(300, 300, 100, 10, { isStatic: true, angle: Math.PI * 0.06 });
var ld7=Bodies.rectangle(450, 300, 100, 10, { isStatic: true, angle: -Math.PI * 0.06 });

//漏斗挡板
var lddb1 = Bodies.rectangle(400, 160, 100, 4, {
            collisionFilter: {
                mask: ballCategory
            },isStatic: true ,render: {
            fillStyle: 'transparent',
            lineWidth: 0
        } });

var lddb2 = Bodies.rectangle(400, 310, 100, 4, {
            collisionFilter: {
                mask: filterCategory | ballCategory
            },isStatic: true ,render: {
            fillStyle: 'transparent',
            lineWidth: 0
        } });


var ground1 = Bodies.rectangle(400, 310, 800, 4, {
            collisionFilter: {
                mask: ballCategory
            },isStatic: true ,render: {
            fillStyle: 'transparent',
            lineWidth: 0
        } });

var ground2 = Bodies.rectangle(400, 510, 800, 4, {
            collisionFilter: {
                mask: filterCategory | ballCategory
            },isStatic: true ,render: {
            fillStyle: 'transparent',
            lineWidth: 0
        } });
        //生成头部遮挡
var groundTop = Bodies.rectangle(400, 0, 800, 4, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
//生成左边遮挡
var groundLeft = Bodies.rectangle(0, 0, 3,1200, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });
//生成右边遮挡
var groundRight = Bodies.rectangle(800, 0, 3,1200, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });




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

var areadb1 = Bodies.rectangle(1000, 160, 100, 4, {
    collisionFilter: {
        mask: ballCategory
    },isStatic: true ,render: {
    fillStyle: 'transparent',
    lineWidth: 0
} });

// add all of the bodies to the world
//World.add(engine.world, [boxA, boxB, ground1,ground2,funnel,groundLeft,groundRight,groundTop,boxC]);
World.add(engine.world, [lddb1,lddb2,ld1,ld2,ld3,ld4,ld5,ld6,ld7,ld8,ld9,ld10,ld11,ld12,
                         area1,area2,area3,area4,area5,area6,areadb1,
                         boxA, boxB,boxC]);
//ground1,ground2,groundLeft,groundRight,groundTop
// run the engine
Engine.run(engine);

// run the renderer

Render.run(render);


//建立和工程相关的连接 工程名称 endpoint
var socket = new SockJS('/abc/random');
var client = Stomp.over(socket);
//添加用户名 密码进行鉴权连接 订阅连接主题 画图
client.connect('user', 'password', function(frame) {
//订阅主题
  client.subscribe("/data", function(message) {
	var data = JSON.parse(message.body);
	jQuery.each(data.dataList, function(i,vh) {
		 add(vh.id,vh.color,vh.status); //创建动画
	});
  });

});
//小球下落
function down(ball){
	ball.collisionFilter.category=filterCategory;
}
//小球归档
function finish(id,ball){
	var home;
	switch(ball.id%3){
	case 0:home=boxA;
		break;
	case 1:home=boxB;
		break;
	case 2:home=boxC;
		break;
	}
	parabola(document.body,ball,{x:home.position.x , y:home.position.y},0);
	delete idlist[id];
	//Matter.World.remove(engine.world,ball);
}
//新增小球
function add(id,color,status){
	var addBox = getBody(engine.world,id);
	if(addBox == null){
		var options;
		if(color == "yellow")options=yellowoptions;
		else if(color == "red")options=redoptions;
		else options = greenoptions;
		
		if(status == "ing"){
			addBox = Bodies.circle(400, 221, radius,options);
		}
		else if(status == "new" || status == "finish"){
			addBox = Bodies.circle(450, 100, radius,options);
		}
		idlist[id]=addBox.id;
		World.add(engine.world,addBox);
	}else{
		var preColor = addBox.render.fillStyle;
		if(status == "ing"){
			if(preColor == redColor){
				parabola(document.body,addBox,{x:880,y:90},1);
				return;
			}
//			window.setTimeout(function(){
//				parabola(document.body,addBox,{x:880,y:90});
//			},1000);
				
		}
		else if(status == "finish"){
			finish(id,addBox);
			return;
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
function parabola(parent,source,target,flag) {
  var ball = document.createElement('div');
  //var canvas = document.getElementById('canvas');
  var pos = parent.getBoundingClientRect(),scrollOffset = getScrollOffsets();
  ball.setAttribute('id','ball');
  ball.setAttribute('flag',flag);
  ball.setAttribute('bodyId',source.id);
  ball.style.position = "absolute"
  ball.style.top = scrollOffset.y + pos.top+source.bounds.min.y+'px';
  ball.style.left = scrollOffset.x + pos.left+source.bounds.min.x+'px';
  ball.style.width = 2*radius+'px';
  ball.style.height = 2*radius+'px';
  ball.style.background = source.render.fillStyle;
  document.body.appendChild(ball);
  World.remove(engine.world,source);

  var 
    // 这里注意，因为位移使用了 left 和 top
    // 定位点是位于页面最左上角
    // 但是为了效果将中心点移到了页面的 (960,200)
    // 故起始点和终点的坐标都需要减去 (960,300)
    startX = parseInt(ball.style.left),
    startY = parseInt(ball.style.top),
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
    b = ((endY - startY) - a * (endX * endX - startX * startX)) / (endX - startX);
    c = endY - a * endX * endX - b * endX;

    /**
     * 利用抛物线公式进行运动 y = a*x*x + b*x + c
     * @function parabolaRAF RAF句柄
     * @return {[type]}  [description]
     */
    requestAnimationFrame(function parabolaRAF() {
      var t = Math.min(1.0, (Date.now() - startTime) / time),
        tx = (endX - startX) * t + startX,
        ty = a * tx * tx + b * tx + c;

      ball.style.left = parseInt(tx) + 'px';
      ball.style.top = parseInt(ty) + 'px';

      if (t < 1.0) requestAnimationFrame(parabolaRAF);
      else{
          //抛物线结束之后，在目标漏斗创建小球并移除临时小球。
          document.body.removeChild(ball);
          if(ball.attributes["flag"].nodeValue=="1"){
        	  var tmp = Bodies.circle(target.x, target.y, radius,greenoptions);
        	  tmp.id = parseInt(ball.attributes["bodyId"].nodeValue);
        	  World.add(engine.world , tmp);
          }
        	  
      }
    })
  }
  ballParabolaMove(target.x+radius,target.y+radius,1000);
};
});