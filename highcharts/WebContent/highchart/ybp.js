var randomData4=0;
var randomData5=0;
var randomData6=0;
 var socket = new SockJS('/bigdata/random');
 var client = Stomp.over(socket);

 client.connect('user', 'password', function(frame) {
   client.subscribe("/highdataybp", function(message) {
 	  var json=JSON.parse(message.body);
 	  var list=json.ybplist;
// //     var point = [Math.random()*200,Math.random()*200,Math.random()*200,list[3],Math.random()*200,list[5],list[6],list[7],list[8],Math.random()*200];
// //     var shift = randomData3.data.length > 60;
 	 randomData4=list[2]*100;
 	  randomData6=list[0]*100;
 	  randomData5=list[1]*100;
// 		 Math.random()*100;
 	 
   });

 });


	canvas = document.getElementById('canvas');
	saveCirqueInfos = [] //保存圆环的数据

	//模拟输入数据
	setInterval(function(){
		var infos = [
			{
				percentage : parseInt(randomData4)/100
			},
			{
				percentage : parseInt(randomData5)/100
			},
			{
//				num : parseInt(randomData3)+"%",
				percentage : parseInt(randomData6)/100
			}
		];
		update(infos);
	},3000);

	//更新圆环
	function update(cirqueInfos){
		var ctx=canvas.getContext("2d");
		
		if(saveCirqueInfos.length == 0){
			ctx.clearRect(0,0,canvas.width,canvas.height);
			saveCirqueInfos[0]=cirqueInfos[0];
			saveCirqueInfos[1]=cirqueInfos[1];
			saveCirqueInfos[2]=cirqueInfos[2];
			drawCirque(canvas,60,60,'green',cirqueInfos[0].percentage,'用时正常');
			drawCirque(canvas,260,60,'red',cirqueInfos[1].percentage,"用时预警");
//			drawCirque(canvas,260,60,'yellow',cirqueInfos[2].num,cirqueInfos[2].percentage,"用时超时");
			drawCirque(canvas,460,60,'yellow',cirqueInfos[2].percentage,"用时超时");
		}else{
			var frameTime = 20,i=1;
			var tmp0 = cirqueInfos[0].percentage - saveCirqueInfos[0].percentage,
				tmp1 = cirqueInfos[1].percentage - saveCirqueInfos[1].percentage,
				tmp2 = cirqueInfos[2].percentage - saveCirqueInfos[2].percentage;
			function animate(){
				ctx.clearRect(0,0,canvas.width,canvas.height);
				var tmp = saveCirqueInfos[0].percentage + tmp0/frameTime*i;
//				drawCirque(canvas,60,60,'green',cirqueInfos[0].num,tmp,'用时正常');
				drawCirque(canvas,60,60,'green',tmp,'用时正常');
				console.log(tmp)
				drawCirque(canvas,260,60,'red',saveCirqueInfos[1].percentage + tmp1/frameTime*i,"用时预警");
				drawCirque(canvas,460,60,'yellow',saveCirqueInfos[2].percentage + tmp2/frameTime*i,"用时超时");
				i = i+1;

				if(frameTime+1 != i)requestAnimationFrame(animate);
				else{
					saveCirqueInfos[0]=cirqueInfos[0];
					saveCirqueInfos[1]=cirqueInfos[1];
					saveCirqueInfos[2]=cirqueInfos[2];
				}
			}
			requestAnimationFrame(animate);
		}
	}

	
	

	//画圆环
	//num 圆内的数字
	//内环的百分比
	function drawCirque(canvas,x,y,color,percentage,title){
		//console.log(percentage);
		var ctx=canvas.getContext("2d");
		ctx.save();
		ctx.beginPath();
		ctx.arc(x,y,40,0,Math.PI*2);
		ctx.strokeStyle = '#00AAFF';
		ctx.lineWidth=9.0
		ctx.stroke();
		ctx.restore();
		ctx.save();
		ctx.beginPath();
		ctx.arc(x,y,26,0,2*Math.PI*percentage);
		ctx.strokeStyle = color;
		ctx.lineWidth=8.0;
		ctx.stroke();

		ctx.font="15px Arial";
		ctx.textAlign = 'center'
		ctx.textBaseline='middle';//文本垂直方向，基线位置 
		ctx.fillText(percentage*100+"%",x,y);
		ctx.fillText(title,x,y+70);
		ctx.restore();
	}