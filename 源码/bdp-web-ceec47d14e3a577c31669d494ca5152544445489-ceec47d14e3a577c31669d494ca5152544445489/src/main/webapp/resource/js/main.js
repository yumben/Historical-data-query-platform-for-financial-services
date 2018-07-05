$(function() {
	/*菜单二级下拉*/
	$(document).on('click', '#ulmenuTop > li', function(event) {
		//$('#ulmenuTop li').hover(function(){
		event.stopPropagation();
		var sib = $('#ulmenuTop li').find('ul').hide();
		var child = $(this).find('ul');
		if (child.is(':hidden')) {
			child.show();
		} else {
			child.hide();
		}
	})
	//点击空白选择框消失
	$(document).click(function(event) {
		// event.preventDefault(); 
		$('#ulmenuTop > li > ul').hide();
	})
});
//头部菜单回调函数
function menuIntTop(data) {
	var menuList = data.list;
	var ulmenuTop = $('#ulmenuTop');
	topMenu(menuList, ulmenuTop)
}
//头部菜单
function topMenu(data, obj) {
	//console.log(JSON.stringify(data)+'+++')
	var selectHtml = '';
	for (var i = 0; i < data.length; i++) {
		var item = data[i];
		//判断childrens是否存在
		if (item.childrens) {
			if (item.childrens == '') {
				selectHtml += '<li><a href="' + item.menu_url + '">' + item.menu_name + '</a></li>';
			} else {
				selectHtml += '<li><a>' + item.menu_name + '</a>';
				var childMenu = item.childrens;
				if (childMenu != '') {
					intMenuAside(childMenu);
				}
			}
		} else {
			selectHtml += '<li><a href="' + item.menu_url + '">' + item.menu_name + '</a></li>';
		}
	}
	obj.append(selectHtml);
	function intMenuAside(childMenu) {
		var menu = childMenu;
		selectHtml += '<ul>';
		//判断childMenu是否存在
		if (childMenu) {
			for (var j = 0; j < childMenu.length; j++) {
				var menuchild = childMenu[j];
				//判断childMenu是否存在
				if (menuchild.childrens) {
					if (menuchild.childrens != '') {
						selectHtml += '<li><a href="' + childMenu[j].menu_url + '">' + childMenu[j].menu_name + '</a>';
						intMenuAside(menuchild.childrens)
						selectHtml += '</li>';
					} else {
						selectHtml += '<li><a>' + childMenu[j].menu_name + '</a></li>';
					}
				} else {
					selectHtml += '<li><a href="' + childMenu[j].menu_url + '">' + childMenu[j].menu_name + '</a></li>';
				}
			}
		}
		selectHtml += '</ul></li>';
	}
}
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return (r[2]);
	return null;
}
function getLength(str) {
	var realLength = 0,
		len = str.length,
		charCode = -1;
	for (var i = 0; i < len; i++) {
		charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128)
			realLength += 1;
		else
			realLength += 2;
	}
	return realLength;
}


function cutstr(str, len) {
	var str_length = 0;
	var str_len = 0;
	str_cut = new String();
	str_len = getLength(str); // str.length;
	for (var i = 0; i < str_len; i++) {
		a = str.charAt(i);
		str_length++;
		if (escape(a).length > 4) {
			//中文字符的长度经编码之后大于4  
			str_length++;
		}
		str_cut = str_cut.concat(a);
		if (str_length >= len) {
			str_cut = str_cut.concat("...");
			return str_cut;
		}
	}
	//如果给定字符串小于指定长度，则返回源字符串；  
	if (str_length < len) {
		return str;
	}
}

//创建vue对象的公共方法
function newVue(elementName, param) {
	var vueObj = new Vue({
		el : '#' + elementName,
		data : param,
		methods : {}
	});
	return vueObj;
}

function string4jsonObject(data) {
	var jsonObject = JSON.stringify(data);
	jsonObject = jQuery.parseJSON(jsonObject);
	return jsonObject;
}

function emptyFrom(jsonObject) {
	for (key in jsonObject) {
		jsonObject[key] = '';
	}
	return jsonObject;
}

//新界面的js
$(document).on("click", "#tabmenu > li", function() {
	$(this).addClass("current").siblings("li").removeClass("current");
	var index = $(this).index();
	$("#tabcontent > div").eq(index).show().siblings().hide();
});

//增加字段的js
$(document).on("click", ".field-input", function(event) {
	event.stopPropagation();
	var ul = $(this).siblings("ul");
	if (ul.is(":hidden")) {
		$(".filed-list").hide();
		ul.show();
	} else {
		ul.hide();
	}
});
$(document).on("click", ".filed-list li", function() {
	var Text = $(this).html();
	var protype = $(this).attr("protype");
	var code = $(this).attr("code");
	var id = $(this).attr("id");
	var val = $(this).val();

	$(this).closest(".filed-down").find("em").html(Text);
	$(this).closest(".filed-down").find("em").attr("protype", protype);
	$(this).closest(".filed-down").find("em").attr("code", code);
	$(this).closest(".filed-down").find("em").attr("id", id);
	$(this).closest(".filed-down").find("em").attr("value", val);
});
$(document).click(function() {
	$(".filed-list").hide();
});


//添加计算字段
$(document).on("click", ".add-downlist", function(event) {
	event.stopPropagation();
	var ullist = $(this).parent("p").siblings("ul");
	if (ullist.is(":hidden")) {
		$(".filed-list01").hide();
		ullist.show();
	} else {
		ullist.hide();
	}
})
$(document).on("click", ".filed-list01 li", function() {
	var Text = $(this).html();
	$(this).closest(".search-add-down").find("input").val(Text);
});
$(document).click(function() {
	$(".filed-list01").hide();
});


//光标的位置增加
function insertText(obj, str) {
	if (document.selection) {
		var sel = document.selection.createRange();
		sel.text = str;
	} else if (typeof obj.selectionStart === 'number' && typeof obj.selectionEnd === 'number') {
		var startPos = obj.selectionStart,
			endPos = obj.selectionEnd,
			cursorPos = startPos,
			tmpStr = obj.value;
		obj.value = tmpStr.substring(0, startPos) + str + tmpStr.substring(endPos, tmpStr.length);
		cursorPos += str.length;
		obj.selectionStart = obj.selectionEnd = cursorPos;
	} else {
		obj.value += str;
	}
}

//增加显示按钮
$(document).on("click", ".addFilebtn", function() {
	var sibl = $(this).siblings("span");
	if (sibl.is(":hidden")) {
		sibl.show();
	} else {
		sibl.hide();
	}
})

$(document).on("click", ".filesShow a", function() {
	$(".filesShow").hide();
})

/**
获取当前的系统时间
*/
function getNowFormatDate() {
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	+ " " + date.getHours() + seperator2 + date.getMinutes()
	+ seperator2 + date.getSeconds();
	return currentdate;
}

//主题切换
$(document).on("click", ".skin > em", function() {
	var sibling = $(this).siblings("span");
	if (sibling.is(":hidden")) {
		//alert("000");
		sibling.show();
	} else {
		sibling.hide();
	}
})
var cookieHref = "hrefval";
if ($.cookie(cookieHref)) {
	$("#skinlink").attr("href", $.cookie(cookieHref));
}
$(document).on("click", ".skin > span > a", function(event) {
	event.stopPropagation();
	var val = $(this).attr("value");
	var elem = $("#skinlink");
	if (val == "1") {
		$(elem).attr("href", "");
	} else if (val == "2") {
		$(elem).attr("href", "../resource/style/skin.css");
	}
	$(this).parent("span").hide();
	$.cookie(cookieHref, $(elem).attr("href"), {
		path : '/',
		expires : 10
	});
})

//存cookie
function setCookie(keyVal, cookieVal) {
	$.cookie(keyVal, cookieVal, {
		expires : 7,
		path : '/'
	});
}
//取cookie
function getCookie(cookieVal) {
	var str = $.cookie(cookieVal);
	return str;
}
//清除cookie
function clearCookie(keyVal) {
	$.cookie(keyVal, '', {
		expires : -1,
		path : '/'
	});
}

function selectTree(tree, val) {
	//console.log(val+'#valvalvalval##')
	//console.log(JSON.stringify(tree)+'##treetree#')
	var newtree = [];
	if (val == '') {
		newtree = tree;
	} else {
		$.each(tree, function(i, obj) {
			var newobj = {};
			newobj['value'] = obj['value'];
			newobj['label'] = obj['label'];
			if (obj['childrens']) {
				if (obj['childrens'].length > 0) {
					newobj['childrens'] = selectTree(obj['childrens'], val);
				}
			}
			if (newobj.label.indexOf(val) >= 0 || (newobj['childrens'] && newobj['childrens'].length > 0)) {
				newtree.push(newobj);
			}
		});
	}
	return newtree;
}


function isHavSame(arr) {
	var temp={};
	var res={};
	res['code']='0';
	res['msg']='';
	$.each(arr, function(i, o) {
		if(o['alias']&&o['alias']!=''){
			if(temp[o['alias']]){
				res['code']='1';
				res['msg']=res['msg']+o['title_name']+'，';
				//console.log(o['alias']+'///'+o['title_name'])
				//res['msg']=res['msg']+'\n'+o['alias']+'--'+o['title_name'];
			}else{
				temp[o['alias']]='1';
			}
		}else{
			if(temp[o['field']]){
				res['code']='1';
				//console.log(o['alias']+'//----/'+o['title_name'])
				res['msg']=res['msg']+o['title_name']+'，';
				//res['msg']=res['msg']+'\n'+o['field']+'--'+o['title_name'];
			}else{
				temp[o['field']]='1';
			}
		}
	});
	return res;
}

function getOnjAttr(obj, attrs) {
	var attrJSON = {}
	$.each(attrs, function(key, val) {
		var temp = obj.attr(key);
		attrJSON[key] = temp == undefined || temp == 'undefined' ? '' : temp;
	});
	return attrJSON;
}

function setOnjAttr(obj, attrs) {
	if(JSON.stringify(attrs) != "{}"){
		$.each(attrs, function(key, val) {
			var temp = obj.attr(key, val);
		});
	}
}

//判断值是undefined的函数
function undeFun(val){
	if( val == undefined || val == 'undefined'){
		return val ='';
	}else{
		return val;
	}
}

//判断值
function isNoemty(data){	
	if(data !='' && data !='null' && data != null && data !=undefined && data !='undefined'){
		return data
	}else{
		return ''
	}
}
//日期控件初始化
function intDate(){
	$(".datepicker").datetimepicker({
		format: 'yyyy-mm-dd',
		minView :2,
		todayBtn: true,
		autoclose:true
	});
}
