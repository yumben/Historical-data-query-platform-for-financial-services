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
            'data_field': 'dashboard_catalog_id',
            label: "仪表盘目录ID"
        },
        {
            'data_field': 'dashboard_catalog_code',
            label: "仪表盘目录编号"
        },
        {
            'data_field': 'dashboard_catalog_name',
            label: "仪表盘目录名称"
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
    'url': '/bdp-web/dashboardCatalog/select',
    //修改和删除的url
    'editUrl':'/bdp-web/dashboardCatalog/edit?dashboard_catalog_id=',
    'removeUrl':'/bdp-web/dashboardCatalog/delete',
    //添加的url
    'addUrl':'/bdp-web/dashboardCatalog/add',
    //请求参数
    'param': {
        'curPage': 1,
        'pageSize': 10,
        'dashboard_catalog_code':'',
        'dashboard_catalog_name':'',
    },
}

tableList = [Tdata1];
