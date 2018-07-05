Vue.component("el-table", {
    props: ['tabledata'],
    data: function () {
        return {
            tableClass: this.tabledata.tableClassObj,//类名
            tableHead: this.tabledata.tableHead,//表头格式
            allData: [],//全部表数据
            option: this.tabledata.option,//保存操作内容的数组
            param: this.tabledata.param,//分页参数
            totalPage: '',//总页数
            totalCout: '',//总条数
            id: this.tabledata.trId,
            returnId: ''
        }
    },
    created: function () {

    },
    beforeMount: function () {

    },
    mounted: function () {
        var _self = this;
        _self.getJS();
        // console.log(_self._id);
    },
    methods: {
        getJS: function () {
            var _self = this;
            ajaxSendload(this.tabledata.url, this.tabledata.param, function (data) {
                _self.allData = data.list;
                _self.totalPage = data.totalPage;
                _self.totalCout = data.totalCout;
            })
        },
        findType: function (obj) {
            var jsonArr = [];
            for (var key2 in this.tableHead) {
                for (var key1 in obj) {
                    if (this.tableHead[key2].data_field == key1 && this.tableHead[key2]) {
                        jsonArr.push(obj[key1]);
                        break;
                    }
                }
            }
            return jsonArr;
        },
        getInputVal: function (pa) {
            var _self = this;
            _self.param = pa;
            ajaxSendload(this.tabledata.url, _self.param, function (data) {
                _self.allData = data.list;
                _self.totalPage = data.totalPage;
                _self.totalCout = data.totalCout;
                console.log(JSON.stringify(data))
            })

        },
        // editFun: function (id) {
        //     window.location.href = this.editUrl + id;
        // },
        // callfunction: function (data) {
        //     if (data.errorCode != null || data.errorMessage != null) {
        //         alert("操作失败！");
        //     } else {
        //         alert("操作成功！");
        //     }
        // },
        // removeFun: function (id) {
        //     var _self = this;
        //     top.layer.confirm('是否确认删除？', {
        //         btn: ['确定', '取消']
        //     }, function (index) {
        //         var params = {
        //             "id": id
        //         };
        //         ajaxSend(_self.removeUrl, params, _self.callfunction);
        //         top.layer.close(index); //如果设定了yes回调，需进行手工关闭
        //         _self.getJS();
        //         //_json4Table.drewTable({});
        //     }, function () {
        //     });
        // },
        currentPage: function (event) {
            var _self = this;//vue实例
            var lis = _self.$refs.ulP.children;
            var target = event.target;

            if ($(target).text() == '上一页') {    //点击上一页
                if (_self.param.curPage == 1) {
                    //无动作
                } else {
                    _self.param.curPage--;
                }
            } else if ($(target).text() == '下一页') {    //点击下一页
                if (_self.param.curPage == _self.totalPage) {
                    //无动作
                } else {
                    _self.param.curPage++;
                }
            } else if ($(target).text() == '首页') {
                _self.param.curPage = 1;
            } else if ($(target).text() == '尾页') {
                _self.param.curPage = _self.totalPage;
            }
            ajaxSendload(_self.tabledata.url, _self.param, function (data) {
                _self.allData = data.list;
            })
        },
        mosaicString: function (data) {
            var idName = this.id;
            var temp = JSON.stringify(data).split('"');
            var result;
            temp.forEach(function (el, index) {
                if (el == idName) {
                    result = temp[index + 2];
                }
            });
            return result;

        }

    },
    computed: {

    },
    template: `
        <div :class="tableClass.div">
            <table :class="tableClass.table">
                <thead :class="tableClass.thead">
                    <tr :class="tableClass.tr">
                        <th :class="tableClass.th" v-for="key in tableHead" :data_field="key.data_field">{{key.label}}</th>
                    </tr>
                </thead>
                <tbody :class="tableClass.tbody">
                    <template v-for="(item1,key1) of allData">
                        <tr :class="tableClass.tr" :id="mosaicString(item1)" ref="trr">
                            <td v-for="item2 in findType(item1)">{{item2}}</td>
                            <td>
                                <a v-for="item3 in option" :class=item3.className href="javascript:void(0)" :title=item3.label>{{item3.label}}</a>                                
                            </td>
                        </tr>
                    </template>
                </tbody>
                <tfoot :class="tableClass.tfoot">
                </tfoot>
            </table>
            <ul class="pagination" id="table_page_vue" v-on:click="currentPage($event)" ref="ulP">
                <li><a href="#">首页</a></li>
                <li><a href="#">上一页</a></li>
                <li><a href="#">{{param.curPage}}</a></li>
                <li><a href="#">下一页</a></li>
                <li><a href="#">尾页</a></li>
            </ul>
            <p>总共 {{totalCout}} 条数据 &nbsp;&nbsp; 总共 {{totalPage}} 页&nbsp;&nbsp; 每页显示{{param.pageSize}}行</p>
        </div>
        `
})
var vm = new Vue({
    el: '#app',
    data: {
        tableDataOne: tableList,
        searchinput: tableList[0].param,
    },
    mounted: function () {

    },
    computed: {

    },
    methods: {
        getInputVal: function () {
            var _self = this;
            _self.$refs.eltable.getInputVal(_self.searchinput);
        },
        getJSON:function(){
            var _self = this;
            _self.$refs.eltable.getJS();         
        }
    }
})