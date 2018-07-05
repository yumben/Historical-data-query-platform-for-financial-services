package cn.com.infohold.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/sendPages")
public class SendPagesController {

	// 首先是登录页面
	@RequestMapping(value = "/{catalog}/{page}", method = { RequestMethod.GET })
	public String loginPage(HttpServletRequest request, @RequestParam String catalog, @RequestParam String page) {
		return "/" + catalog + "/" + page;
	}
}
