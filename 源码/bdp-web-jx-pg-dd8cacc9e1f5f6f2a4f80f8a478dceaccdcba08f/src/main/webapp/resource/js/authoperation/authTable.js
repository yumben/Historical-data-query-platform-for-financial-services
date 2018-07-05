
//修改
//$(document).on('click', '.vue_tableEdit', function (event) {
//    var parentTr = $(event.target).parent().parent();
//    window.location.href = '/bdp-web/authOperation/editAuthOperation?id=' + parentTr[0].id;
//})
//删除
$(document).on('click', '.vue_tableRemove', function (event) {
    var parentTr = $(event.target).parent().parent();
    var id = parentTr[0].id;
    top.layer.confirm('是否确认删除？', {
        btn: ['确定', '取消']
    }, function (index) {
        var params = {
            "id": id
        };
        ajaxSend('/bdp-web/authOperation/delAuthOperation', params, callfunction);
        top.layer.close(index); //如果设定了yes回调，需进行手工关闭
        //调用vue里的函数
        vm.getJSON();
    }, function () {

    });
})
function callfunction(data) {
    if (data.errorCode != null || data.errorMessage != null) {
        alert("操作失败！");
    } else {
        alert("操作成功！");
    }
}

$("#inputDiv").dialog({
	autoOpen: false,
	// 模态开启  
	modal: true,
	// 是否可拖拽  
	draggable: true,
	width:300,
	buttons: {
		"保存": function () {
            var jsonDate = {
                "id": "",
                "operation_code": "",
                "operation_name": "",
                "operation_type": "",
                "operation_desc":""
            };
            var tds;
            $('.alltable>tbody>tr').each(function(index,el){
                if($(el).attr('id') == $('#inputDiv>input')[0].value){
                    tds = $(el).find('td');
                }
            })
            $('#inputDiv>input').each(function(index,el){
                if(index == 0){
                    jsonDate.id = $(el)[0].value;
                    $(tds[0]).text($(el)[0].value) ;
                }else if (index == 1){
                    jsonDate.operation_code = $(el)[0].value;
                    $(tds[1]).text($(el)[0].value);                                        
                }else if (index == 2){
                    jsonDate.operation_name = $(el)[0].value; 
                    $(tds[2]).text($(el)[0].value);  
                }else if (index == 3){
                    jsonDate.operation_type = $(el)[0].value; 
                    $(tds[3]).text($(el)[0].value);  
                }else if (index == 4){
                    jsonDate.operation_desc = $(el)[0].value; 
                    $(tds[4]).text($(el)[0].value);  
                }
            })
            console.log(jsonDate)
            ajaxSend('/bdp-web/authOperation/editAuthOperation',{"param":JSON.stringify(jsonDate)},function(data){
                console.log(data);
            })
			$(this).dialog("close");
		},
		"取消": function () {

			$(this).dialog("close");
		}
	},
	close: function (e, ui) {
        //e:值，ui:当前dialog
	}
});
$(document).on('click', '.vue_tableEdit', function (event) {
    var tds = $(event.target).parent().parent().find('td');
    $('#inputDiv>input')[0].value =$(tds[0]).text();
    $('#inputDiv>input')[1].value =$(tds[1]).text();
    $('#inputDiv>input')[2].value =$(tds[2]).text();
    $('#inputDiv>input')[3].value =$(tds[3]).text();
    $('#inputDiv>input')[4].value =$(tds[4]).text();
    $("#inputDiv").dialog("open"); 
})