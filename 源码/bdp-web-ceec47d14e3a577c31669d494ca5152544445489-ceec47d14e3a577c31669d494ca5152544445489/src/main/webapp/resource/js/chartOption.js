//折线图		
optionline =  {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:[]
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },   
    xAxis: {
        type: 'category',
        boundaryGap: false,
        axisLabel: {  
            interval: 0,  
            formatter:formatField 
        },
        data: []
    },
    yAxis: {
        type: 'value'
    },
    series: []
};
//柱状图		
optionbar = {
	title : {
	  text: '',
	},
	tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
	legend: {
	  data:[]
	},
	xAxis : 
	  {
		  axisLabel: {  
	          interval: 0,  
	          formatter:formatField 
	      },
	      type : 'category',
	      data : [],
	      
	  },
	yAxis : [
	  {
	      type : 'value'
	  }
	],
	series : []
};


//饼状图
optionpie ={
    title : {
        text: '',
        x:'left'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'horizontal',
        left: 'center',
        data: []
    },
    series : [
              {label:{
		        normal:{		        	
		            formatter(v) {
		                let text = v.name
		                text = text.replace(/\S{10}/g, function(match) {
		                    console.log(match)
		                    return match + '\n'
		                })
		                return text
		            }
		        }  
		    }}
    ]
};
//区域图
optionarea = {
    title: {
        text: ''
    },
    tooltip : {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        }
    },
    legend: {
        data:[]
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : 
        {
            type : 'category',
            boundaryGap : false,
            axisLabel: {  
                interval: 0,  
                formatter:formatField 
            },
            data : []
        },
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : []
};
//雷达图
optionradar = {
    title: {
        text: ''
    },
    grid:{
        top:'0',       
        containLabel:false
    },
    tooltip: {},
    legend: {
        data: []
    },
    radar: {
        // shape: 'circle',
        name: {
            textStyle: {
                color: '#000',
                backgroundColor: '#999',
                borderRadius: 3,
                padding: [3, 5]
           },   
		 	formatter: formatField
        },
        indicator: []
    },
    series: []
};

//柱形图
optioncolumnar = {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data: []
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'value',
        boundaryGap: [0, 1]
    },
    yAxis: {
        type: 'category',
        axisLabel: {  
            interval: 0,  
            formatter:formatField
        },
        data: []
    },
    series: []
};
function formatField(value){ 
	if(value && value !=''){
		value = value.replace(/\S{10}/g, function(match) {
	        //console.log(match)
	        return match + '\n'
	    })
	    return value
	}    
}  
function chartBack(chartVal,chartObj,data){
	//console.log(JSON.stringify(data)+'返回的数据');
	chartObj.clear();
	var type ={};		
	if(chartVal == 'line-chart'){
		optionline.title.text ='';
		optionline.legend.data =[];
		optionline.xAxis.data =[];
		optionline.series=[];
		type = optionline;		
	}else if(chartVal == 'pie-chart'){
		optionpie.title.text ='';
		optionpie.legend.data =[];
		optionpie.series=[{label:{
	        normal:{		        	
	            formatter(v) {
	                let text = v.name
	                text = text.replace(/\S{10}/g, function(match) {
	                    console.log(match)
	                    return match + '\n'
	                })
	                return text
	            }
	        }  
	    }}];	
		type = optionpie;		
	}else if(chartVal == 'bar-chart'){
		optionbar.title.text ='';
		optionbar.legend.data =[];
		optionbar.xAxis.data =[];
		optionbar.series=[];			
		type = optionbar;		
	}else if(chartVal == 'area-chart'){
		optionarea.title.text ='';
		optionarea.legend.data =[];
		optionarea.xAxis.data =[];
		optionarea.series=[];
		type = optionarea;		
	}else if(chartVal == 'radar-chart'){
		optionradar.title.text ='';
		optionradar.legend.data =[];
		optionradar.radar.indicator =[];
		optionradar.series=[];
		type = optionradar;
	}else if(chartVal == 'columnar-chart'){
		optioncolumnar.title.text ='';
		optioncolumnar.legend.data =[];
		optioncolumnar.yAxis.data =[];
		optioncolumnar.series=[];
		type = optioncolumnar;		
	}
	//合并json生成新的json
	//console.log(JSON.stringify(data)+'新的数据')
	//console.log(JSON.stringify(type)+'旧的数据')
	var obj ={};
	obj = $.extend(true,type, data);
	//console.log(JSON.stringify(obj)+'合成后的数据')
	//赋值    	
	chartObj.setOption(obj,true);
}