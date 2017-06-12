$.ajax({
            type:'POST',
            url:'<%=request.getContextPath()%>/c?action=media.report.AdFromByMdaReportAction',
            data:'',
            dataType:'json',
            success:function(data){
                console.log(data);
                showHIghcharts(data);
            }
        });
        function showHIghcharts(data){
            $('#zhexiantu').highcharts({
                chart:{

                    },
                title:{
                     text:'来源分析—广告（媒体）',
                     x:-20
                    },
                subtitle:{
                     text:'数据来源：afa',
                     x:-20
                    },
                xAxis:{
                    categories:['0:00','4:00','8:00','12:00','16:00','20:00','24:00'],//维度--时间
                    },
                yAxis:{
                    title:{
                        text:'指标'
                    },
                    plotLines:[{
                        value:0,
                        width:1,
                        color:'#eeeee'
                        }]
                    },
                legend:{
                    layout:'vertical',
                    align:'right',
                    verticalAlign:'middle',
                    borderWidth:0
                    },
                series:[{
                    name:'浏览量(PV)',
                    data:eval("[" + data.show + "]")
                    },
                    {
                    name:'独立访客(UV)',
                    data:eval("[" + data.IndependentAccess + "]")
                    },
                    {
                    name:'IP',
                    data:eval("[" + data.ip + "]")
                    },
                    {
                    name:'访问次数',
                    data:eval("[" + data.VisitTime + "]")
                    }, 
                    ]
                })
        }