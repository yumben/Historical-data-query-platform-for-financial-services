var tableList = [];
var Tdata1 = {
    //每一行的唯一标识
    'trId':'target_template_id',
    //表的类名
    'tableClassObj': {
        table: 'table table-striped alltable',
        thead: '',
        tbody: '',
        tfoot: '',
        th: '',
        tr: '',
        //包裹table的外层div
        div:'tableBox'
    },
    //表头部信息,label可修改
    'tableHead': [
        {
            'data_field': 'target_code',
            label: "指标代码"
        },
        {
            'data_field': 'target_name',
            label: "指标名称"
        },
        {
            'data_field': 'target_desc',
            label: "指标描述"
        },
        {
            'data_field': '',
            label: "操作"
        },
    ],
    //ajax请求地址
    'url': '/bdp-web/easyTarget/select',
    //操作
    'option':[
        {
            'label':'修改',
            'url':'/bdp-web/easytarget/easytargetdefine.html?id=',
            'className':'vue_tableEdit'
        },
        {
            'label':'删除',
            'url':'/bdp-web/easyTarget/delete',
            'className':'vue_tableRemove'
        },
        {
            'label':'配置',
            'url':'/bdp-web/easytargetconfig/easytargetconfig.html?id=',
            'className':'vue_tableConfig'
        },
        {
            'label':'执行',
            'url':'/bdp-web/easyTarget/execTarget',
            'className':'vue_implement'
        }
    ],
    //请求参数
    'param': {
        'curPage': 1,
        'pageSize': 10,
        'target_code':'',
        'target_name':'',
    },
}

tableList = [Tdata1];
