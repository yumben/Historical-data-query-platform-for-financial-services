function initmenu() {
	var index = 1;
	var da = [];
	var pathname = window.location.pathname;
	var json = {
		"parentid" : "0"
	};
	var param = {
		"pageSize" : 999,
		"json" : JSON.stringify(json),
		"flag" : "menuOrder"
	};

	ajaxSend("/bdp-web/sysMenu/selectMenu", param, function(data) {
		$.each(data.list, function(j, obj) {
			if (obj.menuUrl.indexOf(pathname) >= 0) {

				if (obj.parentId == "0") {
					index = obj.menuCode;
				} else {
					index = obj.parentId;
				}
			}
			if (GetQueryString("index")) {
				index = GetQueryString("index");
			}
			da.push(obj);
		});
		initmenu1(da, index);
	});
}


function initmenu1(data, index) {
	var li = '';
	$.each(data, function(i, obj) {
		if (obj.parentId == "0") {
			if ((obj.identify) == index || obj.menuCode == index) {
				li += '<li class="current"><a href="' + obj.menuUrl + '">'
					+ obj.menuName + '</a></li>';
				$("title").html(obj.menuName);
			} else {
				li += '<li><a href="' + obj.menuUrl + '">' + obj.menuName
					+ '</a></li>';
			}
		}
	});
	$("#ulmenu").html(li);
	initmenu2();
}

function initmenu2(data) {
	var menuCode = GetQueryString("menuCode");
	var param = {
		"parentId" : menuCode,
		"flag" : "menuOrder"
	};
	var menu2 = '';
	ajaxSend("/bdp-web/sysMenu/selectMenuByParentId", param, function(data) {
		$.each(data.list, function(j, obj) {
			var pathname = window.location.pathname;
			var search = window.location.search;
			var temp = "?";
			if (obj.menuUrl.indexOf('?') >= 0) {
				temp = "&";
			}
			if (obj.menuUrl.indexOf(pathname) >= 0) {
				menu2 += '<div class="current"><a href="' + obj.menuUrl + temp + 'menuCode=' + obj.parentId + '">'
					+ obj.menuName + '</a></div>';
			} else {
				menu2 += '<div><a href="' + obj.menuUrl + temp+ 'menuCode=' + obj.parentId + '">' + obj.menuName
					+ '</a></div>';
			}
		});
		$("#menu2").html(menu2);
	});
}

function _initmenulist(index) {
	var c_box = '';
	$.getJSON("/pms-web/resource/data/menu.json", function(data) {
		$.each(data, function(j, child) {
			if (child.parent_id == index) {
				c_box += '<a href="' + child.url + '"><img src="' + child.image
					+ '"></a>';
			}
		});
		$("#menulist").html(c_box);
	});
}