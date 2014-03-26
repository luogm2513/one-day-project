/**
 * 
 */
package net.caiban.pc.event.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.caiban.pc.event.config.AppConst;
import net.caiban.pc.event.domain.SessionUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;


/**
 * @author mays
 *
 */
public class BaseController {

	public ModelAndView printJson(Object obj, Map<String, Object> out) {
		String jsonString = "";
		if (obj instanceof List) {
			jsonString = (JSONArray.fromObject(obj).toString());
		} else {
			jsonString = (JSONObject.fromObject(obj).toString());
		}
		out.put("json", jsonString);
		return new ModelAndView("json");
	}
	
	public SessionUser getSessionBank(HttpServletRequest request) {
		return (SessionUser) request.getSession().getAttribute(AppConst.SESSION_KEY);
	}
	
	public void setSessionUser(HttpServletRequest request, SessionUser bank){
		request.getSession().setAttribute(AppConst.SESSION_KEY, bank);
	}
	
	public void removeSession(HttpServletRequest request, String sessionKey) {
		request.getSession().removeAttribute(sessionKey);
	}
}