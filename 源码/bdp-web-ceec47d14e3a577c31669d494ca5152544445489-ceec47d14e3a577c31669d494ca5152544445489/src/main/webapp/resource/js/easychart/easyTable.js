var tableList = [];
var Tdata1 = {
    //每一行的唯一标识
    'trId':'id',
    //表的类名
    'tableClassObj': {
        table: 'table table-striped alltable',
        thead: '1',
        tbody: '2',
        tfoot: '3',
        th: '',
        tr: '',
        //包裹table的外层div
        div:'tableBox'
    },
    //表头部信息,label可修改
    'tableHead': [
        {
            'data_field': 'chart_code',
            label: "图表代码"
        },
        {
            'data_field': 'chart_name',
            label: "图表名称"
        },
        {
            'data_field': 'chart_type_name',
            label: "图表分类"
        },
        {
            'data_field': '',
            label: "操作"
        },
    ],
    //ajax请求地址
    'url': '/bdp-web/easyChart/select',
    //操作
    'option':[
        {
            'label':'修改',
            'url':'/bdp-web/easychart/easychartdefine.html?id=',
            'className':'vue_tableEdit'
        },
        {
            'label':'删除',
            'url':'/bdp-web/easyChart/delete',
            'className':'vue_tableRemove'
        },
    ],
    //请求参数
    'param': {
        'curPage': 1,
        'pageSize': 10,
        'chart_code':'',
        'chart_name':'',
    },
}

tableList = [Tdata1];
