$(function(){
	var myChart1 = echarts.init(document.getElementById('chartType'));
    $.getJSON("/bdp-web/resource/dataJson/lineJSON.json", "", function(data) {
    	//合并json生成新的json
    	var obj = $.extend(option1, data[0]);
    	//赋值                
        myChart1.setOption(obj);
    	
    })
})