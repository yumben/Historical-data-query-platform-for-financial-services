package cn.com.infohold.controller.sys;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.sys.SysMenu;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.commons.AppConstants;
import cn.com.infohold.service.ISysMenuService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author weiben
 * @since 2017-07-24
 */
@Controller
@RequestMapping("/sysMenu")
@Log4j2(topic = "SysMenuController")
public class SysMenuController  {
	@Autowired
	ISysMenuService sysMenuServiceImpl;

	/**
	 * 前端数据将数据都转成json字符串
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addMenu")
	@ResponseBody
	public JSONObject addMenu(HttpServletRequest request) {
		String json = StringUtil.getString(request.getParameter("json"));
		SysMenu entity = new SysMenu();
		JSONObject jsonObject = new JSONObject();
		try {
			entity = BasicJsonUtil.getInstance().toJavaBean(json, SysMenu.class);
			entity.setMenu_code(UUID.randomUUID().toString());
			entity.setIs_valid("1");
			boolean success = sysMenuServiceImpl.insert(entity);
			if (success) {
				jsonObject.put("code", 0);
				jsonObject.put("msg", "增加菜单成功");

			} else {
				jsonObject.put("code", -1);
				jsonObject.put("msg", "增加菜单失败");

			}
		} catch (Exception e) {
			log.error("保存菜单数据发生异常。", e);
			jsonObject.put("code", -2);
			jsonObject.put("msg", "增加菜单异常");
		}
		return jsonObject;
	}

	@RequestMapping(value = "/updateMenu")
	@ResponseBody
	public JSONObject updateMenu(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String json = StringUtil.getString(request.getParameter("json"));
		SysMenu entity = new SysMenu();
		try {
			entity = BasicJsonUtil.getInstance().toJavaBean(json, SysMenu.class);
			boolean success = sysMenuServiceImpl.updateById(entity);
			if (success) {
				jsonObject.put("code", 0);
				jsonObject.put("msg", "修改菜单成功");
			} else {
				jsonObject.put("code", -1);
				jsonObject.put("msg", "修改菜单失败");
			}
		} catch (Exception e) {
			log.error("修改菜单数据发生异常。", e);
			jsonObject.put("code", -2);
			jsonObject.put("msg", "修改菜单异常");
		}
		return jsonObject;
	}

	@RequestMapping(value = "/getMenu")
	@ResponseBody
	public JSONObject getMenu(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String menuCode = StringUtil.getString(request.getParameter("menuCode"));
		try {
			SysMenu entity = new SysMenu();
			entity.setMenu_code(menuCode);
			entity.setIs_valid("1");
			entity = sysMenuServiceImpl.selectById(entity);
			jsonObject = (JSONObject) JSON.toJSON(entity);
		} catch (Exception e) {
			log.error("修改菜单数据发生异常。", e);
			jsonObject.put("code", -2);
			jsonObject.put("msg", "修改菜单异常");
		}
		return jsonObject;
	}

	/**
	 * 删除菜单数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteMenu")
	@ResponseBody
	public JSONObject deleteMenu(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String menuCode = StringUtil.getString(request.getParameter("menu_code"));
		try {
			SysMenu entity = new SysMenu();
			entity.setMenu_code(menuCode);
			boolean success = sysMenuServiceImpl.deleteById(entity);
			if (success) {
				jsonObject.put("code", 0);
				jsonObject.put("msg", "删除菜单成功");
			} else {
				jsonObject.put("code", -1);
				jsonObject.put("msg", "删除菜单失败");
			}
		} catch (Exception e) {
			log.error("删除菜单数据发生异常。", e);
			jsonObject.put("code", -2);
			jsonObject.put("msg", "删除菜单异常");
		}
		return jsonObject;
	}

	/**
	 * 查询菜单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectMenu")
	@ResponseBody
	public JSONObject selectMenu(HttpServletRequest request) {
		String json = StringUtil.getString(request.getParameter("json"));// json格式的查询条件
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		String flag = StringUtil.getString(request.getParameter("flag"));
		SysMenu entity = new SysMenu();
		JSONObject jsonObject = new JSONObject();
		try {
			int pageNo = AppConstants.pageNo;
			if (!"".equals(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = AppConstants.pageSize;
			if (!"".equals(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			if (!"".equals(json)) {
				entity = BasicJsonUtil.getInstance().toJavaBean(json, SysMenu.class);
			}
			entity.setIs_valid("1");
			RetBean page = sysMenuServiceImpl.selectMenuList(entity, pageNo, pageSize, flag);

			jsonObject.put("list", page.getResults());
			jsonObject.put("pageNo", page.getTotalPage());
			jsonObject.put("total", page.getTotal());
			jsonObject.put("code", 0);
		} catch (Exception e) {
			jsonObject.put("code", -2);
			e.printStackTrace();
			log.error("查询菜单数据发生异常。", e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/getMenuTree")
	@ResponseBody
	public JSONObject getMenuTree(HttpServletRequest request) {
		String token = request.getParameter("token");
		JSONObject jsonObject = new JSONObject();
		List<Map<String, Object>> result = sysMenuServiceImpl.getMenuTree(token);
		jsonObject.put("list", result);
		return jsonObject;
	}

	@RequestMapping(value = "/selectMenuByParentId")
	@ResponseBody
	public JSONObject selectMenuByParentId(HttpServletRequest request) {
		String flag = StringUtil.getString(request.getParameter("flag"));
		String parentId = StringUtil.getString(request.getParameter("parentId"));// json格式的查询条件
		String pageNoStr = StringUtil.getString(request.getParameter("pageNo"));
		String pageSizeStr = StringUtil.getString(request.getParameter("pageSize"));
		SysMenu entity = new SysMenu();
		JSONObject jsonObject = new JSONObject();
		try {
			int pageNo = AppConstants.pageNo;
			if (!"".equals(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = AppConstants.pageSize;
			if (!"".equals(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			if (!"".equals(parentId)) {
				entity.setParent_id(parentId);
			}

			entity.setIs_valid("1");
			RetBean page = sysMenuServiceImpl.selectMenuList(entity, pageNo, pageSize, flag);

			jsonObject.put("list", page.getResults());
			jsonObject.put("pageNo", page.getTotalPage());
			jsonObject.put("total", page.getTotal());
			jsonObject.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", -2);
			log.error("查询菜单数据发生异常。", e);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/selectMenuAll")
	@ResponseBody
	public JSONObject selectMenuAll(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			List<Map<String, Object>> list = sysMenuServiceImpl.selectMenuAll();
			jsonObject.put("list", list);
		} catch (Exception e) {
			log.error("查询菜单数据发生异常。", e);
		}
		return jsonObject;
	}

}
