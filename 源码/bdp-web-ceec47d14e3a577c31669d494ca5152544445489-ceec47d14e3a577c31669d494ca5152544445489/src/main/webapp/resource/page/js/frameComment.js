//判断不为空
function isNoEmpty(data) {
	if (data != null && data != undefined && data != 'null' && data != 'undefined' && data != '') {
		return data;
	}
}
//日期控件初始化
function intDate() {
	$(".datepicker").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		todayBtn : true,
		autoclose : true
	});
}
//获取表单元素数据
function getForm(obj, text, radio, checkbox, select, area, password, jsonVal) {
	text = obj.find('input[type="text"]');
	radio = obj.find('input[type="radio"]');
	checkbox = obj.find('input[type="checkbox"]');
	select = obj.find('select');
	area = obj.find('textarea');
	password = obj.find('input[type="password"]');
	if (text.length > 0) {
		textJson(text, 'text', jsonVal);
	}
	if (radio.length > 0) {
		textJson(radio, 'radio', jsonVal);
	}
	if (checkbox.length > 0) {
		textJson(checkbox, 'checkbox', jsonVal);
	}
	if (select.length > 0) {
		textJson(select, 'select', jsonVal);
	}
	if (area.length > 0) {
		textJson(area, 'textarea', jsonVal);
	}
	if (password.length > 0) {
		textJson(password, 'password', jsonVal);
	}
//alert(JSON.stringify(jsonVal)+'@@@@@@@@@@@@@')
}
//获取文本框输入的值
function textJson(data, type, json) {
	console.log(JSON.stringify(json)+"@@@@@@@@@@@")
	if (isNoEmpty(data)) {
		if (type == 'text') {
			$.each(data, function(index, el) {
				var _this = $(this);
				var nameId = '';
				var textVal = '';
				nameId = $(this).attr('name');
				textVal = $(this).val();
				if (isNoEmpty(textVal)) {
					if (nameId in json) {
						json[nameId] += ',' + textVal;
					} else {
						json[nameId] = textVal;
					}
				} else {
					json[nameId] = '';
				}
			});
		} else if (type == 'password') {
			$.each(data, function(index, el) {
				var _this = $(this);
				var nameId = '';
				var textVal = '';
				nameId = $(this).attr('name');
				textVal = $(this).val();
				if (isNoEmpty(textVal)) {
					if (nameId in json) {
						json[nameId] += ',' + hex_md5(textVal);
					} else {
						json[nameId] = hex_md5(textVal);
					}
				} else {
					json[nameId] = '';
				}
			});
		} else if (type == 'radio') {
			var checkedValue = '0';
			var nameId = '';
			var textVal = '';
			$.each(data, function(index, el) {
				var _this = $(this);
				nameId = $(this).attr('name');
				textVal = $(this).attr('value');
				if (_this.is(':checked')) {
					checkedValue = textVal;
				}
			});
			json[nameId] = checkedValue;
		} else if (type == 'checkbox') {
			$.each(data, function(index, el) {
				var _this = $(this);
				var nameId = '';
				var textVal = '';
				nameId = $(this).attr('name');
				//var checked=$(this).attr('checked');
				textVal = $(this).val();
				if (_this.is(':checked')) {
					if (isNoEmpty(textVal)) {
						if (nameId in json) {
							json[nameId] += ',' + textVal;
						} else {
							json[nameId] = textVal;
						}
					}
				}
			});
		} else if (type == 'select') {
			var checkedValue = '';
			var nameId = '';
			var textVal = '';
			$.each(data, function(index, el) {
				var _this = $(this);
				nameId = $(this).attr('name');
				textVal = $(this).val();
				if (isNoEmpty(textVal)) {
					if (nameId in json) {
						json[nameId] += ',' + textVal;
					} else {
						json[nameId] = textVal;
					}
				} else {
					json[nameId] = '';
				}
			});
		} else if (type == 'textarea') {
			$.each(data, function(index, el) {
				var _this = $(this);
				var nameId = '';
				var textVal = '';
				nameId = $(this).attr('name');
				textVal = $(this).val();
				if (isNoEmpty(textVal)) {
					if (nameId in json) {
						json[nameId] += ',' + textVal;
					} else {
						json[nameId] = textVal;
					}
				} else {
					json[nameId] = '';
				}
			});
		}

	}


}

//生成表单标签元素
function createHtml(type, obj, filed, codeName, val, pk, search, fkData) {
	//type文本类型； obj：对象；filed：字段中文名；codeName:字段的key；val是否有值；pk主键；search：是否是查询字段；fkData：字典、外键数据	
	//是否有值
	if (val) {
		val = val;
	} else {
		val = '';
	}
	//是主键的就隐藏
	var style = '';
	if (pk == '1') {
		style = 'display:none';
	} else {
		style = '';
	}
	if (type == 'text') {
		obj.append('<div class="item" style="' + style + '"><span>' + filed + '：</span><input class="form-control" type="text" name="' + codeName + '" value="' + val + '"></div>');
	}else if (type == 'password') {
		obj.append('<div class="item" style="' + style + '"><span>' + filed + '：</span><input class="form-control" type="password" name="' + codeName + '" value="' + val + '"></div>');
	}else if (type == 'textTime') {
		if (search == '1') {
			obj.append('<div class="item" style="' + style + '"><span>' + filed + '：</span>'
				+ '<input class="form-control datepicker" readonly type="text" id="' + codeName + 'Start" name="' + codeName + '" value="' + val + '">'
				+ ' 至 <input class="form-control datepicker" readonly type="text" id="' + codeName + 'End" name="' + codeName + '" value="' + val + '"></div>');
		} else {
			obj.append('<div class="item" style="' + style + '"><span>' + filed + '：</span><input class="form-control datepicker" readonly type="text" name="' + codeName + '" value="' + val + '"></div>');
		}
		$('#' + codeName + 'Start').on("click", function() {
			$('#' + codeName + 'Start').datetimepicker("setEndDate", $('#' + codeName + 'End').val())
		});
		$('#' + codeName + 'End').on("click", function() {
			$('#' + codeName + 'End').datetimepicker("setStartDate", $('#' + codeName + 'Start').val())
		});
		intDate();
	} else if (type == 'radio') {
		radioInput(obj, filed, codeName, val, style, fkData);
	} else if (type == 'checked') {
		checkeInput(obj, filed, codeName, val, style, fkData);
	} else if (type == 'select') {
		seletInput(obj, filed, codeName, val, style, fkData);
	} else if (type == 'textarea') {
		obj.append('<div class="item" style="' + style + '"><span>' + filed + '：</span><textarea class="form-control" type="textarea" name="' + codeName + '">' + val + '</textarea></div>');
	} else {
		obj.append('<div class="item" style="' + style + '"><span>' + filed + '：</span><input class="form-control" type="text" name="' + codeName + '" value="' + val + '"></div>');
	}
}

//单选框生成的表单元素
function radioInput(obj, filed, codeName, val, style, fkData) {
	var radioHtml = '';
	if (isNoEmpty(fkData)) {
		for (var i = 0; i < fkData.length; i++) {
			for (var key in fkData[i]) {
				var valList = fkData[i][key];
				if (key == codeName) {
					radioHtml += '<div class="item" style="' + style + '"><span>' + filed + '：</span>';
					$.each(valList, function(index, el) {
						var chioce = el.choice;
						var isChecked = '';
						if (chioce == '1' || val == el.value) {
							isChecked = 'checked';
						}
						radioHtml += '<label><input type="radio" value="' + el.value + '" name="' + codeName + '" ' + isChecked + '>' + el.label + '</label>';
					});
					radioHtml += '</div>';
					obj.append(radioHtml);
				}
			}
		}
	}
}

//多选框生成的表单元素
function checkeInput(obj, filed, codeName, val, style, fkData) {
	var checkeHtml = '';
	if (isNoEmpty(fkData)) {
		for (var i = 0; i < fkData.length; i++) {
			for (var key in fkData[i]) {
				var valList = fkData[i][key];
				if (key == codeName) {
					checkeHtml += '<div class="item" style="' + style + '"><span>' + filed + '：</span>';
					$.each(valList, function(index, el) {
						var chioce = el.choice;
						var isChecked = '';
						for (var i = 0; i < val.length; i++) {
							if (el.value == val[i]) {
								isChecked = 'checked';

							}
						}
						console.log(val + '###' + el.value)
						checkeHtml += '<label><input type="checkbox" value="' + el.value + '" name="' + codeName + '" ' + isChecked + '>' + el.label + '</label>';
					});
					checkeHtml += '</div>';
					obj.append(checkeHtml);
				}
			}
		}
	}
}

//下拉选框生成的表单元素
function seletInput(obj, filed, codeName, val, style, fkData) {
	var selectdHtml = '';
	if (isNoEmpty(fkData)) {
		for (var i = 0; i < fkData.length; i++) {
			for (var key in fkData[i]) {
				var valList = fkData[i][key];
				if (key == codeName) {
					selectdHtml += '<div class="item" style="' + style + '"><span>' + filed + '：</span><select class="form-control" name="' + codeName + '">';
					$.each(valList, function(index, el) {
						var isChecked = '';
						if (el.value == val) {
							isChecked = 'selected';
						}
						selectdHtml += '<option ' + isChecked + ' value="' + el.value + '">' + el.label + '</option>';
					})
					selectdHtml += '</select></div>';
					obj.append(selectdHtml);
				}
			}
		}
	}
}



//遍历数组判断div是否为空
function divEmpty(obj) {
	$.each(obj, function(index, el) {
		if ($(this).is(':empty')) {
			$(this).css('display', 'none');
			$(this).prev('h5').css('display', 'none');
		}
	})
}

//配置界面元数据选择
$(document).on('click', '.tableNav li > h5', function() {
	var sibling = $(this).siblings('ul');
	var paren = $(this).parent('li').siblings('li').children('ul');
	paren.hide();
	$(this).parent('li').siblings('li').children('h5').find('i').addClass('fa-plus-square').removeClass('fa-minus-square');
	if (sibling.is(':hidden')) {
		sibling.show();
		$(this).children('i').addClass('fa-minus-square').removeClass('fa-plus-square');
	} else {
		sibling.hide();
		$(this).children('i').addClass('fa-plus-square').removeClass('fa-minus-square');
	}
})

//表单界面收缩
$(document).on('click', '.subTitle > i', function() {
	var sibling = $(this).parent('.subTitle').next('.subCon');
	if (sibling.is(':hidden')) {
		sibling.slideDown();
		$(this).addClass('fa-angle-double-down').removeClass('fa-angle-double-up');
		$(this).parent('.subTitle').css({
			'marginBottom' : '0',
			'borderBottom' : '0'
		})
	} else {
		sibling.slideUp();
		$(this).addClass('fa-angle-double-up').removeClass('fa-angle-double-down');
		$(this).parent('.subTitle').css({
			'marginBottom' : '15px',
			'borderBottom' : '1px solid #e7e7e7'
		})
	}
})