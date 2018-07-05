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
            'data_field': 'id',
            label: "用户ID"
        },
        {
            'data_field': 'operation_code',
            label: "操作代码"
        },
        {
            'data_field': 'operation_name',
            label: "操作名称"
        },
        {
            'data_field': 'operation_type',
            label: "操作类型"
        },
        {
            'data_field': 'operation_desc',
            label: "备注"
        },
        {
            'data_field': '',
            label: "操作"
        },
    ],
    //ajax请求地址
    'url': '/bdp-web/authOperation/selectAuthOperation',
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
        'operation_code':'',
    },
}

tableList = [Tdata1];
