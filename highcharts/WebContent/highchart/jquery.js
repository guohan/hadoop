//参考：http://blog.csdn.net/zhaizu/article/details/17170555
//	http://blog.csdn.net/zhaojw_420/article/details/65657767

$(function() {
	$('#container').highcharts(
			{
				chart : {
					type : 'spline',
					marginRight : 150,
					marginLeft : 150,
					marginBottom : 25,
					animation : Highcharts.svg,

					events : {
						load : function() {

							// 若有第3条线，则添加
							// var series_other_property = this.series[2]
							// 并在 series: [] 中添加相应的 (name, data) 对
							var series_cpu = this.series[0];
							var series_mem = this.series[1];

							// 定时器，每隔1000ms刷新曲线一次
							setInterval(function() {

								// 使用JQuery从后台Servlet获取
								// JSON格式的数据，
								// 如 "{"cpu": 80,"mem": 10}"
								jQuery.getJSON(
										'./SomeServlet?action&parameter', null,
										function(data) {

											// 当前时间，为x轴数据
											var x = (new Date()).getTime();

											// y轴数据
											var cpu = data.cpu;
											var mem = data.mem;

											// 更新曲线数据
											series_cpu.addPoint([ x, cpu ],
													true, true);
											series_mem.addPoint([ x, mem ],
													true, true);
										});
							}, 1000/* 启动间隔，单位ms */
							);
						}
					}
				},
				title : {
					text : '使用率(%)',
					x : -20
				},
				xAxis : {
					type : 'datetime',
					tickPixelInterval : 150
				},
				yAxis : {
					title : {
						text : '使用率(%)'
					},
					plotLines : [ {
						value : 0,
						width : 1,
						color : '#808080'
					} ]
				},
				tooltip : {
					valueSuffix : '%'
				},
				legend : {
					layout : 'vertical',
					align : 'right',
					verticalAlign : 'top',
					x : -10,
					y : 100,
					borderWidth : 0
				},
				series : [
				// 第1条曲线的(name, data)对
				{
					name : 'CPU',
					data : (function() {
						var data = [], time = (new Date()).getTime(), i;

						// 给曲线y值赋初值0
						for (i = -9; i <= 0; i++) {
							data.push({
								x : time + i * 1000,
								cpu : 0
							});
						}
						return data;
					})()
				},

				// 第2条曲线的(name, data)对
				{
					name : '内存',
					data : (function() {
						var data = [], time = (new Date()).getTime(), i;

						// 给曲线y值赋初值0
						for (i = -9; i <= 0; i++) {
							data.push({
								x : time + i * 1000,
								y : 0
							});
						}
						return data;
					})()
				}, ]
			});
});