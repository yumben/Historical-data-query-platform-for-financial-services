function list2Json(data, deep) {
	var isdeep = true;
	if (deep != undefined) {
		isdeep = deep;
	}
	var treedata = [];
	var obj1 = {};
	obj1.key = "1";
	obj1.text = "<a href='javascript:treeclick(\"common_sign\",\"公有\");'>公有标签</a>";
	obj1.nodes = gettreenodes(data, "公有", "", isdeep);
	treedata.push(obj1);
	var obj2 = {};
	obj2.key = "0";
	obj2.text = "<a href='javascript:treeclick(\"common_sign\",\"自定义\");'>自定义标签</a>";
	obj2.nodes = gettreenodes(data, "自定义", "", isdeep);
	treedata.push(obj2);
	return treedata;
}
function gettreenodes(data, comm, parent, isdeep) {
	var nodes = [];
	$.each(data, function(i, obj) {
		if (obj.common_sign == comm && obj.parent == parent) {
			var obj1 = {};
			obj1.key = obj.tags_typeid;
			if (parent == "") {
				obj1.text = "<a href='javascript:treeclick(\"tagsType\",\""
						+ obj.tags_type_name + "\");'>" + obj.tags_type_name
						+ "</a>";
			}else{
				obj1.text = "<a href='javascript:treeclick(\"tagsType1\",\""
					+ obj.tags_type_name + "\");'>" + obj.tags_type_name
					+ "</a>";
			}
			if (isdeep) {
				obj1.nodes = gettreenodes(data, comm, obj.tags_type_name,
						isdeep);
			}
			nodes.push(obj1);
		}
	});
	return nodes;
}

function list2Json1(data, deep) {
	var isdeep = true;
	if (deep != undefined) {
		isdeep = deep;
	}
	var treedata = [];
	var obj1 = {};
	obj1.key = "1";
	obj1.text = "产品类型";
	obj1.nodes = gettreenodes1(data, "1", "", isdeep);
	treedata.push(obj1);
	return treedata;
}
function gettreenodes1(data, comm, parent, isdeep) {
	var nodes = [];
	$.each(data, function(i, obj) {
		if (obj.parent_id === parent) {
			var obj1 = {};
			obj1.key = obj.product_type_id;
			obj1.text = "<a href='javascript:treeclick(\""
					+ obj.product_type_id + "\");'>" + obj.product_type_name
					+ "</a>";
			if (isdeep) {
				obj1.nodes = gettreenodes1(data, comm, obj.product_type_id,
						isdeep);
			}
			nodes.push(obj1);
		}
	});
	return nodes;
}


function list2Json3(data, deep) {
	var isdeep = true;
	if (deep != undefined) {
		isdeep = deep;
	}
	var treedata = [];
	var obj1 = {};
	obj1.key = "1";
	obj1.text = "<a href='javascript:treeclick(\"\",\"\");'>大数据查询平台</a>";
	obj1.nodes = gettreenodes3(data, "", "0", isdeep);
	treedata.push(obj1);
	return treedata;
}
function gettreenodes3(data, comm, parent, isdeep) {
	var nodes = [];
	$.each(data, function(i, obj) {
		if (obj.parent_id == parent) {
			var obj1 = {};
			obj1.key = obj.menu_code;
			if (parent == "0") {
				obj1.text = "<a href='javascript:treeclick(\"parent_id\",\""
						+ obj.menu_code + "\");'>" + obj.menu_name
						+ "</a>";
			}else{
				obj1.text = "<a href='javascript:treeclick(\"parent_id\",\""
					+ obj.menu_code + "\");'>" + obj.menu_name
					+ "</a>";
			}
			obj1.nodes = gettreenodes3(data, comm, obj.menu_code,true);
			nodes.push(obj1);
		}
	});
	return nodes;
}

function list2Json4(data, deep) {
	var isdeep = true;
	if (deep != undefined) {
		isdeep = deep;
	}
	var treedata = [];
	var obj1 = {};
	obj1.key = "1";
	obj1.text = "<a href='javascript:treeclick(\"\");'>数据资源配置</a>";
	obj1.nodes = gettreenodes4(data,isdeep);
	treedata.push(obj1);
	return treedata;
}
function gettreenodes4(data,isdeep) {
	var nodes = [];
	$.each(data, function(i, obj) {
		
			var obj1 = {};
			obj1.key = obj.dataTableId;
				obj1.text = "<a href='javascript:treeclick(\""
						+ obj.dataTableName + "\");'>" + obj.dataTableNameCN
						+ "</a>";
			
			nodes.push(obj1);
			
	});
	return nodes;
}

function list2Json5(data, deep) {
	var isdeep = true;
	if (deep != undefined) {
		isdeep = deep;
	}
	var treedata = [];
	var obj1 = {};
	obj1.key = "1";
	obj1.text = "<a href='javascript:treeclick(\"\",\"\");'>数据资源配置</a>";
	obj1.nodes = gettreenodes5(data,isdeep);
	treedata.push(obj1);
	return treedata;
}
function gettreenodes5(data,isdeep) {
	var nodes = [];
	$.each(data, function(i, obj) {
		
			var obj1 = {};
			obj1.key = obj.dataSourceName;
				obj1.text = "<a href='javascript:treeclick(\"\",\""
						+ obj.dataSourceName + "\");'>" + obj.dataSourceNameCN
						+ "</a>";
			
			nodes.push(obj1);
		
	});
	return nodes;
}

function list2Json6(data) {
	var treedata = [];
	$.each(data, function(i, obj) {
		if(obj.parentCatalog == "" || obj.parentCatalog == "null" || obj.parentCatalog == null){
			var obj1 = {};
			obj1.id = obj.catalogId;
			obj1.name = obj.catalogName;
			obj1.code = obj.catalogCode;
			obj1.parentId = obj.parentCatalog;
			obj1.childMenus = gettreenodes6(data, obj.catalogId);
			treedata.push(obj1);
		}
	});
	return treedata;
}
function gettreenodes6(data, parent) {
	
	var nodes = [];
	$.each(data, function(i, obj) {
		//alert("obj.parentId="+obj.parentId+"parent="+parent);
		if (obj.parentCatalog == parent) {
			var obj1 = {};
			obj1.id = obj.catalogId;
			obj1.name = obj.catalogName;
			obj1.parentId = obj.parentCatalog;
			obj1.code = obj.catalogCode;
			obj1.childMenus = gettreenodes6(data, obj.catalogId);
			
			nodes.push(obj1);
		}
	});
	return nodes;
}

// 机构管理
function list2Json7(data, deep) {
	var isdeep = true;
	if (deep != undefined) {
		isdeep = deep;
	}
	var treedata = [];
	var obj1 = {};
	obj1.key = "1";
	obj1.text = "<a href='javascript:treeclick(\"\",\"\");'>大数据查询平台</a>";
	obj1.nodes = gettreenodes7(data, "", null, isdeep);
	treedata.push(obj1);
	return treedata;
}
function gettreenodes7(data, comm, parent, isdeep) {
	
	var nodes = [];
	$.each(data, function(i, obj) {
		var obj1 = {};
		obj1.key = obj.branchCode;
		if (obj.branchParent == parent) {
			obj1.text = "<a href='javascript:treeclick(\"parentId\",\""
				+ obj.branchCode + "\");'>" + obj.branchName
				+ "</a>";
			obj1.nodes = gettreenodes7(data, comm, obj.branchCode,true);
			nodes.push(obj1);
		}else if(obj.branchParent == "underfind" || obj.branchParent == "0"){
			obj1.text = "<a href='javascript:treeclick(\"parentId\",\""
				+ obj.branchCode + "\");'>" + obj.branchName
				+ "</a>";
			obj1.nodes = gettreenodes7(data, comm, obj.branchCode,true);
			nodes.push(obj1);
		}
	});
	return nodes;
}

