//tables
var dataList = jQuery("#dataList");

//建立和工程相关的连接 工程名称 endpoint
var socket = new SockJS('/abc/random');
var client = Stomp.over(socket);
//添加用户名 密码进行鉴权连接 订阅连接主题 画图
client.connect('user', 'password', function(frame) {
	  console.log('Connected: ' + frame);
	  //订阅主题
  client.subscribe("/data", function(message) {
	  var data = JSON.parse(message.body);
	  var totalOutput='';
		jQuery.each(data.dataList, function(i,vh) {
			 totalOutput +="<tbody><tr><td>"+ vh.color+"</td><td>"+vh.status+"</td><td>"+vh.area+"</td><td>"+vh.type+"</td></tr></tbody>";
		});
		var t_tabl_start = "<table class='table table-bordered table-condensed table-hover innerTable'><thead><tr><th>color</th><th>status</th><th>area</th><th>type</th></tr></thead>";
		var t_tabl_end = "</table>";
		dataList.html(t_tabl_start+totalOutput+t_tabl_end);
  });

  
 
});

