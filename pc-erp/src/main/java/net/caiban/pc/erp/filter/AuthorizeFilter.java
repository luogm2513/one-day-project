/**
 * Copyright 2011 ASTO.
 * All right reserved.
 * Created on 2011-5-5
 */
package net.caiban.pc.erp.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.caiban.pc.erp.config.AppConst;
import net.caiban.pc.erp.domain.SessionUser;
import net.caiban.utils.lang.StringUtils;
import net.sf.json.JSONObject;


/**
 * @author mays (mays@zz91.com)
 *
 * created on 2011-5-5
 */
public class AuthorizeFilter implements Filter {
	
//	private String deniedURL = "";
	private String loginURL = "";
	
	private Set<String> noLoginPage;
//	private Set<String> noAuthPage;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest rq, ServletResponse rp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request= (HttpServletRequest) rq;
		HttpServletResponse response = (HttpServletResponse) rp;

		String uri = request.getRequestURI();
		String path = request.getContextPath();
		path= path==null?"":path;

		StringBuffer refURL=request.getRequestURL();
		
		String url=loginURL+"?refurl="+URLEncoder.encode(refURL.toString(), "utf-8")+"&refparam="+URLEncoder.encode(JSONObject.fromObject(request.getParameterMap()).toString(), "utf-8");
		
		do {

			SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(AppConst.SESSION_KEY);
			request.setAttribute("sessionUser", sessionUser);
			request.setAttribute("juser", JSONObject.fromObject(sessionUser));

//			RequestContext requestContext = new RequestContext(request);
//			Locale locale = requestContext.getLocale();
//			request.setAttribute("locale", locale);
			
			if(filterByConfig(noLoginPage, path, uri)){
				chain.doFilter(request, response);
				return ;
			}
			
			if(sessionUser==null){
				break;
			}
			
//			if(filterByConfig(noAuthPage, path, uri)){
//				chain.doFilter(request, response);
//				return ;
//			}
			
			
			chain.doFilter(request, response);
			return ;
		} while (false);
//		
//		//AJAX请求权限过滤
		if(request.getHeader("x-requested-with")!=null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
			response.setHeader("sessionstatus","timeout");
			response.setHeader("redirectUrl",path+url);
			return ;
		}

		response.sendRedirect(path+url);
		return ;
	}
//
	@Override
	public void init(FilterConfig config) throws ServletException {
//		deniedURL = config.getInitParameter("deniedURL");
		loginURL = config.getInitParameter("loginURL");
		
//		projectCode = config.getInitParameter("projectCode");
//		projectPassword = config.getInitParameter("projectPassword");
		
		String tmp[]= null;
		noLoginPage = new HashSet<String>();
		String e1=config.getInitParameter("noLoginPage");
		if(StringUtils.isNotEmpty(e1)){
			tmp=e1.split("\\|");
			for(String ex:tmp){
				noLoginPage.add(ex.trim().replace("\\n", ""));
			}
		}

//		noAuthPage = new HashSet<String>();
//		String e2=config.getInitParameter("noAuthPage");
//		if(StringUtils.isNotEmpty(e2)){
//			tmp=e2.split("\\|");
//			for(String ex:tmp){
//				noAuthPage.add(ex.trim().replace("\\n", ""));
//			}
//		}
	}

	public boolean filterByConfig(Set<String> exclude, String path,String uri){
		for(String url:exclude){
			url=path+url;
			if(url.startsWith("*")){
				if(uri.endsWith(url.substring(1))){
					return true;
				}
			}else if(url.endsWith("*")){
				if(uri.startsWith(url.substring(0, url.length()-1))){
					return true;
				}
			}else{
				if(url.equals(uri)){
					return true;
				}
			}
		}
		return false;
	}
//	
//	public boolean filterByAuth(String[] rightArr, HttpServletRequest request){
//		if(rightArr==null || rightArr.length<=0){
//			return false;
//		}
//		
//		String query = request.getQueryString();
//		String contextPath = request.getContextPath();
//		String uri = request.getRequestURI();
//		if(uri.startsWith(contextPath)){
//			uri=uri.substring(contextPath.length(),uri.length());
//		}
//		String url = uri+(query == null ? "" : "?"+query);
//
//		boolean ispass=false;
//		for(String s:rightArr){
//			if(url.matches(s.trim())){
//				ispass=true;
//				break;
//			}
//		}
//		return ispass;
//	}

}
