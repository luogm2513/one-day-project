/**
 * 
 */
package net.caiban.pc.event.service.events.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.caiban.pc.event.service.events.EventsService;
import net.caiban.pc.event.service.sys.SysUserService;
import net.caiban.utils.lang.StringUtils;

import org.springframework.stereotype.Component;

/**
 * @author mays
 *
 */
@Component("eventsService")
public class EventsServiceImpl implements EventsService {
	
	@Resource
	private SysUserService sysUserService;

	@Override
	public Integer[] changeToIntArray(String origin) {
		Integer ids[] = {};
		do {
			if (origin == null || origin.length() <= 0) {
				break;
			}
			String[] idstrArray = origin.split(",");
			if (idstrArray.length == 0) {
				break;
			}
			ids = new Integer[idstrArray.length];
			for (int i = 0; i < idstrArray.length; i++) {
				ids[i] = Integer.valueOf(idstrArray[i]);
			}
		} while (false);
		return ids;
	}

	@Override
	public Map<String, Integer> filterAppendJoiner(String origin,
			String originId, String append) {
		
		origin = filterEmpty(origin);
		originId = filterEmpty(originId);
		append = filterEmpty(append);
		
		Integer[] idArr = changeToIntArray(originId);
		String[] accountArr = {};
		origin = replaceCN(origin);
		if(StringUtils.isNotEmpty(origin)){
			accountArr = origin.split(",");
		}
		
		if(idArr.length != accountArr.length){
			return null;
		}
		
		append = replaceCN(append);
		String[] appendArr = {};
		if(StringUtils.isNotEmpty(append)){
			appendArr = append.split(",");
		}
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i=0; i<accountArr.length; i++ ){
			map.put(accountArr[i], idArr[i]);
		}
		
		for(String account: appendArr){
			if(map.get(account)==null){
				Integer id = sysUserService.queryIdByAccount(account);
				if(id!=null && id.intValue()>0){
					map.put(account, id);
				}
			}
		}
		
		return map;
	}
	
	private String filterEmpty(String arg){
		if("".equals(arg)){
			return null;
		}
		return arg;
	}
	
	private String replaceCN(String arg){
		if(arg == null){
			return null;
		}
		
		return arg.replace("，",",");
	}

}
