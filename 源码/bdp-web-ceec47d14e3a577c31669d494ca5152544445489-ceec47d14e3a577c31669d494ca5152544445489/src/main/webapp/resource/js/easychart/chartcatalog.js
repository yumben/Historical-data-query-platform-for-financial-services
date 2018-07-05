var tableList = [];
var Tdata1 = {
    //每一行的唯一标识
    'trId':'chart_catalog_id',
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
            'data_field': 'chart_catalog_id',
            label: "图表ID"
        },
        {
            'data_field': 'chart_catalog_code',
            label: "目录编号"
        },
        {
            'data_field': 'chart_catalog_name',
            label: "目录名称"
        },
        {
            'data_field': 'parent_id',
            label: "上一级ID"
        },
        {
            'data_field': '',
            label: "操作"
        },
    ],
    //ajax请求地址
    'url': '/bdp-web/chartCatalog/select',
    //操作
    'option':[
        {
            'label':'修改',
            'className':'vue_tableEdit'
        },
        {
            'label':'删除',
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
