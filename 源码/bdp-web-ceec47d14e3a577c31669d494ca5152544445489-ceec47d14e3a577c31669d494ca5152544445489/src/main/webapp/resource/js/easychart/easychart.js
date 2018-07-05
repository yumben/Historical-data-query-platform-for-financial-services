var $table = $('.table');
$(document).ready(function () {
    $table.find('tr').each(function (index, el) {
        $(el).find('td:last').attr('data-formatte', 'operateFormatter"');
    })

    $('#btncheck').on('click',function(){
        window.location.href = '/bdp-web/easychart/easychartdefine.html'
        	
    })
})
//修改
$(document).on('click', '.vue_tableEdit', function (event) {
    var parentTr = $(event.target).parent().parent();
    window.location.href = '/bdp-web/easychart/easychartdefine.html?id=' + parentTr[0].id;
})
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
        ajaxSend('/bdp-web/easyChart/delete', params, callfunction);
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
