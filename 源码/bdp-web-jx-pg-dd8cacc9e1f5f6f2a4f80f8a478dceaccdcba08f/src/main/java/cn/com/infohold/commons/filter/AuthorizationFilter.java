package cn.com.infohold.commons.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.com.infohold.tools.util.StringUtil;

public class AuthorizationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getParameter("token");
		if (StringUtil.isEmpty(token)) {
			throw new ServletException();
		}
	}
}
