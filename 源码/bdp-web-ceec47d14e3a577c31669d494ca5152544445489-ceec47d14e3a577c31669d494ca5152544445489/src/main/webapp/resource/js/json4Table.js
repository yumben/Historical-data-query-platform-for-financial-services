(function($) {
	$.json4Table = function(options) {
		_options = {
			tableid : "table",
			action : undefined,
			pageIndex : 0,
			pageSize : 10,
			totalPage : undefined,
			totalCount : undefined,
			formatter : undefined,
			events : undefined,
			callback : undefined
		};

		var opt = $.extend(_options, options);
		//保存一份初始化起始查询页数和条数
		_optionsFinalDef = {
				pageIndex : _options.pageIndex,
				pageSize : _options.pageSize,	
			};
		
		var userParams;
		this.drewTable = function(_params) {
			//进入页面查询、点击查询按钮，恢复起始查询页数和条数
			opt.pageIndex = _optionsFinalDef.pageIndex;
			opt.pageSize = _optionsFinalDef.pageSize;
			//保存用户查询条件
			userParams = _params;
			var params = $.extend(_params, {
				pageIndex : opt.pageIndex,
				pageSize : opt.pageSize
			});
			ajaxSend(opt.action, params, function(data) {
				var $table = $("#" + opt.tableid);
				var data_id = $table.attr('data-id-field');
				$table.find("tbody").html("");// alert(JSON.stringify(data.list));
				$.each(data.list, function(i, obj) {
					var row = "<tr>";
					$table.find("th").each(function() {
						var field = obj[$(this).attr('data-field')];
						var formatter = $(this).attr('data-formatter');
						var id = obj[data_id];
						row += "<td>";
						if (formatter === undefined) {
							if(field==null){
								
							}else{
							
							if (getLength(field) > 20) {
								row += '<p style="text-decoration:none;color:black;" title="'+field+'">'
										+ cutstr(field, 20) + '</p>';
							} else {
								row += '<p style="text-decoration:none;color:black;" title="'+field+'">'
										+ field + '</p>';
							}}
							//row += field;
						} else {
							row += opt.formatter(id);
						}
						row += "</td>";
					});
					row += "</tr>";
					$table.append(row);
				});
				opt.totalPage = data.totalPage;
				opt.totalCount = data.totalCount;
				$table.next().remove();
				$table.parent().append(this.intiPage());
				if (opt.callback) {
					opt.callback(data);
				}
			});
		};

		updateTable = function() {
			var params = $.extend(userParams, {
				pageIndex : opt.pageIndex,
				pageSize : opt.pageSize
			});
			ajaxSend(opt.action, params, function(data) {
				var $table = $("#" + opt.tableid);
				var data_id = $table.attr('data-id-field');
				$table.find("tbody").html("");
				$.each(data.list, function(i, obj) {
					var row = "<tr>";
					$table.find("th").each(function() {
						var field = obj[$(this).attr('data-field')];
						var formatter = $(this).attr('data-formatter');
						var id = obj[data_id];
						row += "<td>";
						if (formatter === undefined) {
							if(field==null){
								
							}else{
							if (getLength(field) > 20) {
								row += '<p style="text-decoration:none;color:black;" title="'+field+'">'
										+ cutstr(field, 20) + '</p>';
							} else {
								row += '<p style="text-decoration:none;color:black;" title="'+field+'">'
										+ field + '</p>';
							}}
						} else {
							row += opt.formatter(id);
						}
						row += "</td>";
					});
					row += "</tr>";
					$table.append(row);
				});
				opt.totalPage = data.totalPage;
				opt.totalCount = data.totalCount;
				$table.next().remove();
				$table.parent().append(this.intiPage());
			});
		};

		intiPage = function() {
			var pagehtml = "<div class='page'><nav><ul class='pagination'>";
			if (opt.pageIndex == 0) {
				pagehtml += "<li><a aria-label='first'><span aria-hidden='true'>首页</span></a></li>";
				pagehtml += "<li><a aria-label='Previous'><span aria-hidden='true'>上一页</span></a></li>";
			} else {
				pagehtml += "<li><a href='javascript:onPageFirst();' aria-label='first'><span aria-hidden='true'>首页</span></a></li>";
				pagehtml += "<li><a href='javascript:onPagePrevious();' aria-label='Previous'><span aria-hidden='true'>上一页</span></a></li>";
			}
			var j = 0;
			var e = opt.totalPage;
			for ( var i = 1; i <= opt.totalPage; i++) {
				if (opt.pageIndex - 3 >= 0 && i <= opt.pageIndex - 3) {
					if (i == opt.pageIndex - 3) {
						pagehtml += "<li><a href='javascript:onPageIndex("
								+ (i - 1) + ");'>...</a></li>";
					}
				} else {
					if (i <= opt.pageIndex + 4) {
						if (opt.pageIndex + 1 == i) {
							pagehtml += "<li class='active'><a href='#'>" + i
									+ "</a></li>";
						} else {
							pagehtml += "<li><a href='javascript:onPageIndex("
									+ (i - 1) + ");'>" + i + "</a></li>";
						}
					}else{
						if (i == opt.pageIndex + 5) {
							pagehtml += "<li><a href='javascript:onPageIndex("
									+ (i - 1) + ");'>...</a></li>";
						}
					}
				}
			}
			if (opt.pageIndex + 1 == opt.totalPage) {
				pagehtml += "<li><a aria-label='Next'><span aria-hidden='true'>下一页</span></a></li>";
				pagehtml += "<li><a aria-label='last'><span aria-hidden='true'>最后一页</span></a></li>";
			} else {
				pagehtml += "<li><a href='javascript:onPageNext();' aria-label='Next'><span aria-hidden='true'>下一页</span></a></li>";
				pagehtml += "<li><a href='javascript:onPageLast();' aria-label='last'><span aria-hidden='true'>最后一页</span></a></li>";
			}
			pagehtml += "</ul>";
			pagehtml += "<span class='count'>总共 " + opt.totalCount + "条记录，每页显示"
					+ opt.pageSize + "条记录，共" + opt.totalPage + "页</span>";
			pagehtml += "</nav></div>";
			return pagehtml;
		};
		onPageFirst = function() {
			opt.pageIndex = 0;
			updateTable();
		};
		onPagePrevious = function() {
			opt.pageIndex = opt.pageIndex - 1;
			updateTable();
		};
		onPageIndex = function(i) {
			opt.pageIndex = i;
			updateTable();
		};
		onPageNext = function() {
			opt.pageIndex = opt.pageIndex + 1;
			updateTable();
		};
		onPageLast = function() {
			opt.pageIndex = opt.totalPage - 1;
			updateTable();
		};
	};
})(jQuery);
