$(function(){
    //echart数据分析
    var myChart1 = echarts.init(document.getElementById('echart1'),'infographic'); 
    var myChart2 = echarts.init(document.getElementById('echart2'),'infographic'); 
    var myChart3 = echarts.init(document.getElementById('echart3'),'infographic');
    var myChart4 = echarts.init(document.getElementById('echart4'));

       //数据
        option1 = {
        title : {
            text: '标签用户覆盖人数',
            left:'left',
            subtext: ' ',
            textStyle:{
            	fontSize:14
            }
        },
        /*tooltip : {
            
        },
        legend: {
            x:'left',
            data:['人数']
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },*/
        xAxis : [
            {
                type : 'category',
                data : ['18-25岁','31-35岁','36-40岁','40岁以上']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'人数',
                type:'bar',
                data:[70, 330, 200, 180]
            }
        ]
    };   

        option2 = {
        title : {
            text: '标签覆盖率',
            subtext: ' ',
            x:'left',
            textStyle:{
            	fontSize:14
            }
        },
        /*tooltip : {
            trigger: 'item'
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data : ['18-25岁','31-35岁','36-40岁','40岁以上']
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {
                    show: true, 
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: 1548
                        }
                    }
                },
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },*/
        calculable : true,
        series : [
            {
                name:'访问来源',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:70, name:'18-25岁'},
                    {value:330, name:'31-35岁'},
                    {value:200, name:'36-40岁'},
                    {value:180, name:'40岁以上'}
                ]
            }
        ]
    };

    option3 = {
    title : {
        text: '标签间关系结构',
        subtext: ' ',
        x:'left',
        textStyle:{
        	fontSize:14
        }
    },
    /*tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        orient : 'vertical',
        x:'left',
        data:['男同志','经常支付','手游迷','有一定存款','喜欢买理财']
    },*/
    grid:{
        x:'180px'
    },
    /*toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },*/
    calculable : true,
    xAxis : [
        {
            type : 'value'
        }
    ],
    yAxis : [
        {
            type : 'category',
            data : ['18-25岁','31-35岁','36-40岁','40岁以上']
        }
    ],
    series : [
        {
            name:'男同志',
            type:'bar',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[320, 302, 301, 334, 390]
        },
        {
            name:'经常支付',
            type:'bar',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[120, 132, 101, 134, 90]
        },
        {
            name:'手游迷',
            type:'bar',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[220, 182, 191, 234, 290]
        },
        {
            name:'有一定存款',
            type:'bar',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[150, 212, 201, 154, 190]
        },
         {
            name:'喜欢买理财',
            type:'bar',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[320, 302, 301, 334, 390]
        }
    ]
};

 option4 = {
        title : {
            text: '标签使用率',
            x:'left',
            subtext: ' ',
            textStyle:{
            	fontSize:14
            }
        },
        /*tooltip : {
            
        },*/
        /*legend: {
            x:'left',
            data:['标签使用率']
        },*/
        /*toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },*/
        xAxis : [
            {
                type : 'category',
                data : ['11月1日','11月3日','11月5日','11月7日','11月9日','11月11日']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'标签使用率',
                type:'bar',
                data:[24,12,30,14,5,2,24,21,40,36,10]
            }
        ]
    };   

    //赋值                
    myChart1.setOption(option1); 
    myChart2.setOption(option2); 
    myChart3.setOption(option3); 
    myChart4.setOption(option4); 
})
