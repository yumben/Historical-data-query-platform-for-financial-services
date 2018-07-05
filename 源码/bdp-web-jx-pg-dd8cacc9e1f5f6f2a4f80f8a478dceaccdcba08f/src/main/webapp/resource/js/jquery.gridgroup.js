/*
*gridgroup构造参数：
*         head：数组，指定头部行分组，数组值越靠前，分组优先级也高。按头部分组，如果为null，默认分组。
*         column：数组，指定按那列分组，数组值越靠前，分组优先级越高。如果为null，默认分组。
*         注意：指定了head方式分组后，column分组将无效。head优先级高于column。所以如果需要列分组，head值传入null
* @method group(head,column) 分组对象调用方法，传入分组参数，重新绘制分组报表数据。
*         head：同上
*         column：同上
* @作者 四川耗子  
* 说明：在本js的基础修改，请保留作者信息。
*/
(function ($) {
    $.fn.extend({
        gridgroup: function (options) {
            //获取表参数
            var gridConfig = this.data[""];
            //初始化表参数
            if (!gridConfig) {
                //表头
                var heads = [];
                //列数据
                var columns = [];
                var $table = $(this);

                var $trs = $("thead tr", $(this));
                for (var i = 0; i < $("th", $($trs[0])).length; i++) {
                    var ch = [];
                    $.each($trs, function () {
                        ch.push($(this).find("th")[i].innerText);
                    });
                    heads.push(ch);
                }

                $("tbody tr", $table).each(function () {
                    var column = [];
                    $(this).find("td").each(function () {
                        column.push(this.innerText);
                    });
                    columns.push(column);
                });
                this.data["tableData"] = {
                    heads: heads,
                    columns: columns
                }
                gridConfig = this.data["tableData"];
            }else{
            	gridConfig = this.data["tableData"];
            }
            //分析方法
            function analyse(data,hcconfig) {
                var hc = [];
                var hcindex = [];
                if (hcconfig) {
                    $.each(hcconfig.zindex, function (hczindex) {
                        var zconfig = hcconfig.hc[hczindex];
                        var zhcindex = [];
                        var zhc = [];
                        for (var index = 0; index < data.length; index++) {
                            if (checkArray(index, zconfig.index) > -1) {
                                if (checkArray(data[index], zhcindex) < 0) {
                                    zhc.push({
                                        index: [],
                                        parent: hczindex
                                    });
                                    zhcindex.push(data[index]);
                                }
                                zhc[checkArray(data[index], zhcindex)].index.push(index);
                            }
                        }
                        $.merge(hc, zhc);
                        $.merge(hcindex, zhcindex);
                    });
                } else {
                    for (var index = 0; index < data.length; index++) {
                        
                        if (checkArray(data[index], hcindex) < 0) {
                            hc.push({
                                index: []
                            });
                            hcindex.push(data[index]);
                        }
                        hc[checkArray(data[index], hcindex)].index.push(index);
                    }
                }
                var index = [];
                for (var hczindex = 0; hczindex < hcindex.length; hczindex++) {
                    var cfindex=hc[hczindex].index;
                    $.merge(index, cfindex);
                }
                return {
                    index: index,
                    zindex:hcindex,
                    hc:hc
                };
            }
            function defaultanalyse(columnconfig, data) {

                var zhcindex = [];
                var zhc = [];
                if (!columnconfig) {
                    for (var i = 0; i < data.length;) {
                        var title = data[i];
                        var index = [];
                        for (var m = i; m < data.length; m++) {
                            if (title == data[m]) {
                                index.push(m);
                                i++;
                            } else {
                                break;
                            }
                        }
                        zhc.push({ index: index });
                        zhcindex.push(title);
                    }
                } else {
                    for (var i = 0; i < data.length;) {
                        var title = data[i];
                        var index = [];
                        var max = 0;
                        for (var ca = 0; ca < columnconfig.hc.length; ca++) {
                            max += columnconfig.hc[ca].index.length;
                            if (checkArray(i, columnconfig.hc[ca].index) >= 0) {
                                break;
                            }
                        }
                        for (var m = i; m < max; m++) {
                            if (title == data[m]) {
                                i++;
                                index.push(m);
                            } else {
                                break;
                            }
                        }
                        max = 0;
                        zhc.push({ index: index });
                        zhcindex.push(title);
                    }
                }
                
               return {
                    index: null,
                    zindex: zhcindex,
                    hc: zhc
                }
            }
            function checkArray(key, array) {
                var index=-1;
                for (var i = 0; i < array.length; i++) {
                    if (key == array[i]) {
                        index = i;
                        break;
                    }
                }
                return index+"";
            }

            //表头分组方法
            function groupHead(dom,head,headconfig,rowIndex) {
                var columnc;
                /****************头部分组******************/
                var $thead = $("thead", dom);
                $thead.html("");

                if (!head) {
                    for (var h = 0; h < headconfig[0].length; h++) {
                        var hc = [];
                        $.each(rowIndex, function (index) {
                            hc.push(headconfig[this][h]);
                        });
                        columnc = defaultanalyse(columnc, hc);
                        //创建头
                        var $tr = $("<tr></tr>");
                        for (var i = 0; i < columnc.zindex.length; i++) {
                            var $th = $("<th colspan='" + columnc.hc[i].index.length + "' style='text-align:center;vertical-align:middle;'>" + columnc.zindex[i] + "</th>");
                            $tr.append($th);
                        }
                        $thead.append($tr);
                    }
                } else {
                    if (head.length != headconfig[0].length) {
                        for (var i = 0; i < headconfig[0].length; i++) {
                            if (!checkArray(i, head)) {
                                head.push(i);
                            }
                        }
                    }
                    //输出分组头部列
                    $.each(head, function () {
                        var col = this;
                        var hc = [];
                        $.each(headconfig, function (index) {
                            hc.push(this[col]);
                        });
                        columnc = analyse(hc, columnc);
                        //创建头
                        var $tr = $("<tr></tr>");
                        for (var i = 0; i < columnc.zindex.length; i++) {
                            var $th = $("<th colspan='" + columnc.hc[i].index.length + "' style='text-align:center;vertical-align:middle;'>" + columnc.zindex[i] + "</th>");
                            $tr.append($th);
                        }
                        $thead.append($tr);
                    });
                }
                return columnc.index;
            }
            //数据列分组方法
            function groupColumn(dom, column, columnconfig) {
                var columnc;
                /****************头部分组******************/
                var $tbody = $("tbody", dom);
                $tbody.html("");
                var trconfig = [];
                var groupSpan = [];
                //输出分组头部列
                $.each(column, function (zindex) {
                    var col = this;
                    var hc = [];
                    $.each(columnconfig, function (index) {
                        hc.push(this[col]);
                    });
                    columnc = analyse(hc, columnc);
                    var row = 0;
                    $.each(columnc.zindex, function (index) {
                        groupSpan.push({
                            rowspan: columnc.hc[index].index.length,
                            value:this,
                            proint: [row, zindex]
                        });
                        row += columnc.hc[index].index.length;
                    });
                    trconfig.push(columnc);
                });
                for (var i = trconfig.length - 1; i > 0; i--) {
                    var cf = trconfig[i];
                    for (var m = 0; m < cf.hc.length;m++){
                        if (!trconfig[i - 1].hc[cf.hc[m].parent]["child"]) {
                            trconfig[i - 1].hc[cf.hc[m].parent]["child"] = [];
                        }
                        trconfig[i - 1].hc[cf.hc[m].parent]["child"].push({
                            zindex: cf.zindex[m],
                            cf: cf.hc[m]
                        });
                    }
                    
                }
                var columndata = [];
                var cellIndex = [];
                for (var i = 0; i < columnc.index.length; i++) {
                    var data = [];
                    var olddata = columnconfig[columnc.index[i]];
                    for (var m = 0; m < column.length; m++) {
                        data.push(olddata[column[m]]);
                        if(i==0) cellIndex.push(column[m]);
                    }
                    for (var m = 0; m < olddata.length; m++) {
                        if (checkArray(m, column) == -1) {
                            data.push(olddata[m]);
                            if (i == 0) cellIndex.push(m);
                        }
                    }
                    columndata.push(data);
                }
                var index = [];
                $.each(columndata, function (row) {
                    var $tr = $("<tr></tr>");
                    //var 
                    //检查分组列数据
                    $.each(columndata[row], function (cell) {
                        if (cell<column.length) {
                            var checkpoint = checkPoint(row, cell, groupSpan);
                            if (checkpoint) {
                                var $td = $("<td rowspan='" + checkpoint.rowspan + "' style='text-align:center;vertical-align:middle;'>" + this + "</td>");
                                $tr.append($td);
                            }
                        } else {
                            var $td = $("<td>" + this + "</td>");
                            $tr.append($td);
                        }
                    });
                    $tbody.append($tr); 
                });             
                return cellIndex;
            }
            //校验数据坐标
            function checkPoint(row, cell, data) {
                for (var i = 0; i < data.length; i++) {
                    var datainfo = data[i];
                    if (datainfo.proint[0] == row && datainfo.proint[1] == cell) {
                        return datainfo;
                    }
                }
            }
            //分组方法
            function group(head, column) {
                var config = this.data["tableData"];
                //头数据
                var headconfig = config.heads;
                //列数据
                var columnconfig = config.columns;
                //判断是否是头部分组
                if (head && head instanceof Array) {
                    var index = groupHead(this, head, headconfig);
                    //列分组
                    groupColumn(this, index, columnconfig);
                } else {
                    var index=[];
                    if (!column || !(column instanceof Array)) {
                        $.each(headconfig, function (zindex) {
                            index.push(zindex);
                        });
                    } else {
                        index = column;
                        $.each(headconfig, function (zindex) {
                            if (!checkArray(zindex, index)) {
                                index.push(zindex);
                            }
                        });
                    }
                    var rowIndex=groupColumn(this, index, columnconfig);
                    //如果不是头分组，头在不改变列的情况，自动合并
                    //var index = groupHead(this, null, headconfig, rowIndex);
                }
                return this;
            }
            //给对象注册分组函数。
            this.group = group;
            this.group(options["head"], options["column"]);
            return this;
        }
    });
    
})(jQuery);