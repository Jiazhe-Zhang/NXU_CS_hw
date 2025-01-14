package cms.web.action.staff;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cms.bean.PageForm;
import cms.bean.PageView;
import cms.bean.QueryResult;
import cms.bean.RequestResult;
import cms.bean.ResultCode;
import cms.bean.staff.SysResources;
import cms.bean.staff.SysRoles;
import cms.service.setting.SettingService;
import cms.service.staff.ACLService;
import cms.utils.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ACL列表
 * @author Administrator
 *
 */
@Controller
public class ACLAction {
	@Resource ACLService aclService;//通过接口引用代理返回的对象
	
	@Resource SettingService settingService;
	/**
	 * 资源列表
	 * @param pageForm
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/control/resources/list") 
	public String resources(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer jpql = new StringBuffer("");
		//存放参数值
		List<Object> params = new ArrayList<Object>();

		jpql.append(" o.urlParentId is null");
		//调用分页算法代码
		PageView<SysResources> pageView = new PageView<SysResources>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		orderby.put("priority", "asc");//根据priority字段降序排序
		QueryResult<SysResources> qr = aclService.getScrollData(SysResources.class,firstindex, pageView.getMaxresult(),jpql.toString(),params.toArray(),orderby);
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		return JsonUtils.toJSONString(new RequestResult(ResultCode.SUCCESS,pageView));
	}
	/**
	 * 角色列表
	 * @param pageForm
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/control/roles/list") 
	public String roles(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//调用分页算法代码
		PageView<SysRoles> pageView = new PageView<SysRoles>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();
		//排序
		QueryResult<SysRoles> qr = aclService.getScrollData(SysRoles.class,firstindex, pageView.getMaxresult());
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		return JsonUtils.toJSONString(new RequestResult(ResultCode.SUCCESS,pageView));
	}
}
