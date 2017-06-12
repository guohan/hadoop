var randomData3;
$(document).ready(function() {  
$('#lineDataDayChart').highcharts({
  chart : {
    type : 'line',
    events : {
      load : function() {
        randomData3 = this.series[0];
      }
    }
  },
  title : {
    text : false
  },
  xAxis : {
//    type : 'datetime',
//    minRange : 60 * 1000
	  categories:["报修","报装","咨询","查询","投诉","举报","表扬","建议","意见","日统计"]
  },exporting:{
      enabled:false
  },
  credits: {
      enabled: false
  }, 
  yAxis: {
      title: {
          text: '数量'
      },
      tickPositions: [0, 1000, 2000, 3000], // 指定竖轴坐标点的值
      plotLines: [{
          value: 0,
          width: 1,
          color: '#808080'
      }]
  },
  legend : {
    enabled : false
  },
  plotOptions : {
    series : {
      threshold : 0,
      marker : {
        enabled : false
      }
    }
  },
  series : [{
		name : '数量',
		data :[]
	},]
});

var socket = new SockJS('/bigdata/random');
var client = Stomp.over(socket);

client.connect('user', 'password', function(frame) {
  client.subscribe("/highdataday", function(message) {
//	  alert(message.body);
	  console.log(message.body);
//	  alert("message body"+message.body);
	  var json=JSON.parse(message.body);
//	  jQuery.each(json.ywgdlist, function(i,vh) {
////		  alert(vh);
//		  point.push(vh)
//		});
//	  var point=[];
	  var list=json.ywgdlist;
//	  for (var i = 0; i < list.length; i++) {
//	        point.push(list[i]);
//	    };
//	  alert(list[0]+list[1]+list[2]+list[3]+list[4]+list[5]+list[6]+list[7]+list[8]+list[9]);
//	  alert(list[9]);
    var point = [list[0],list[1],list[2],list[3],list[4],list[5],list[6],list[7],list[8],list[9]];
//    var point = [Math.random()*200,Math.random()*200,Math.random()*200,list[3],Math.random()*200,list[5],list[6],list[7],list[8],Math.random()*200];
//	  var point=[170, 169, 195, 145, 182, 115, 152, 165, 133, 183];
    var shift = randomData3.data.length > 60;
//    , true, shift
//    randomData.addPoint(point);
    randomData3.setData(point);
//    randomData.addPoint([ x, cpu ],
//			true, true);
  });

});
});