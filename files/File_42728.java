/*
 * Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.roncoo.pay.controller.login;

import com.roncoo.pay.common.core.dwz.DWZ;
import com.roncoo.pay.common.core.dwz.DwzAjax;
import com.roncoo.pay.common.core.utils.StringUtil;
import com.roncoo.pay.controller.common.BaseController;
import com.roncoo.pay.permission.entity.PmsOperator;
import com.roncoo.pay.permission.exception.PermissionException;
import com.roncoo.pay.permission.service.PmsMenuService;
import com.roncoo.pay.permission.service.PmsOperatorRoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 龙果学院：www.roncoo.com
 * 
 * @author：Along
 */
@Controller
public class LoginController extends BaseController {

	private static final Log LOG = LogFactory.getLog(LoginController.class);

	@Autowired
	private PmsOperatorRoleService pmsOperatorRoleService;
	@Autowired
	private PmsMenuService pmsMenuService;

	/**
	 * 函数功能说明 ： 进入�?��?�登陆页�?�.
	 * 
	 * @�?�数： @return
	 * @return String
	 * @throws
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest req, Model model) {

		String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
		String error = null;
		if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
			error = "用户�??/密�?错误";
		} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
			error = "用户�??/密�?错误";
		} else if (PermissionException.class.getName().equals(exceptionClassName)) {
			error = "网络异常,请�?�系龙果管�?�员";
		} else if (exceptionClassName != null) {
			error = "错误�??示：" + exceptionClassName;
		}
		model.addAttribute("message", error);
		return "system/login";
	}

	/**
	 * 函数功能说明 ： 登陆�?��?�管�?�系统. 修改者�??字： 修改日期： 修改内容：
	 * 
	 * @�?�数： @param request
	 * @�?�数： @param model
	 * @�?�数： @return
	 * @return String
	 * @throws PermissionException
	 */
	@RequestMapping("/")
	public String index(HttpServletRequest req, Model model) {
		PmsOperator pmsOperator = (PmsOperator) this.getSession().getAttribute("PmsOperator");
		try {
			String tree = this.buildOperatorPermissionMenu(pmsOperator);
			model.addAttribute("tree", tree);
		} catch (PermissionException e) {
			LOG.error("登录异常:" + e.getMessage());
			model.addAttribute("message", e.getMessage());
			return "system/login";
		}
		return "system/index";

	}

	/**
	 * 函数功能说明 ：进入退出系统确认页�?�. 修改者�??字： 修改日期： 修改内容：
	 * 
	 * @�?�数： @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/admin/confirm", method = RequestMethod.GET)
	public String confirm() {
		return "system/confirm";
	}

	/**
	 * 函数功能说明 ： 退出系统. 修改者�??字： 修改日期： 修改内容：
	 * 
	 * @�?�数： @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/admin/logout", method = RequestMethod.POST)
	public String logout(HttpServletRequest request, Model model) {
		// �?是以form的形�?�??交的数�?�,�?new一个DwzAjax对象
		DwzAjax dwz = new DwzAjax();
		try {
			HttpSession session = request.getSession();
			session.removeAttribute("employee");
			LOG.info("***clean session success!***");
		} catch (Exception e) {
			LOG.error(e);
			dwz.setStatusCode(DWZ.ERROR);
			dwz.setMessage("退出系统时系统出现异常，请通知系统管�?�员�?");
			model.addAttribute("dwz", dwz);
			return "admin.common.ajaxDone";
		}
		return "admin.login";
	}

	/**
	 * 获�?�用户的�?��?��?��?
	 * 
	 * @param pmsOperator
	 * @return
	 * @throws PermissionException
	 * @throws Exception
	 */
	private String buildOperatorPermissionMenu(PmsOperator pmsOperator) throws PermissionException {
		// 根�?�用户ID得到该用户的所有角色拼�?的字符串
		String roleIds = pmsOperatorRoleService.getRoleIdsByOperatorId(pmsOperator.getId());
		if (StringUtils.isBlank(roleIds)) {
			LOG.error("==>用户[" + pmsOperator.getLoginName() + "]没有�?置对应的�?��?角色");
			throw new RuntimeException("该�?�?�已被�?�消所有系统�?��?");
		}
		// 根�?��?作员拥有的角色ID,构建管�?��?��?�的树形�?��?功能�?��?�
		return this.buildPermissionTree(roleIds);
	}

	/**
	 * 根�?��?作员拥有的角色ID,构建管�?��?��?�的树形�?��?功能�?��?�
	 * 
	 * @param roleIds
	 * @return
	 * @throws PermissionException
	 */
	@SuppressWarnings("rawtypes")
	public String buildPermissionTree(String roleIds) throws PermissionException {
		List treeData = null;
		try {
			treeData = pmsMenuService.listByRoleIds(roleIds);
			if (StringUtil.isEmpty(treeData)) {
				LOG.error("用户没有分�?�?��?��?��?");
				throw new PermissionException(PermissionException.PERMISSION_USER_NOT_MENU, "该用户没有分�?�?��?��?��?"); // 该用户没有分�?�?��?��?��?
			}
		} catch (Exception e) {
			LOG.error("根�?�角色查询�?��?�出现错误", e);
			throw new PermissionException(PermissionException.PERMISSION_QUERY_MENU_BY_ROLE_ERROR, "根�?�角色查询�?��?�出现错误"); // 查询当�?角色的
		}
		StringBuffer strJson = new StringBuffer();
		buildAdminPermissionTree("0", strJson, treeData);
		return strJson.toString();
	}

	/**
	 * 构建管�?��?��?�的树形�?��?功能�?��?�
	 * 
	 * @param pId
	 * @param treeBuf
	 * @param menuList
	 */
	@SuppressWarnings("rawtypes")
	private void buildAdminPermissionTree(String pId, StringBuffer treeBuf, List menuList) {

		List<Map> listMap = getSonMenuListByPid(pId.toString(), menuList);
		for (Map map : listMap) {
			String id = map.get("id").toString();// id
			String name = map.get("name").toString();// �??称
			String isLeaf = map.get("isLeaf").toString();// 是�?��?��?
			String level = map.get("level").toString();// �?��?�层级（1�?2�?3�?4）
			String url = map.get("url").toString(); // ACTION访问地�?�
			String navTabId = "";
			if (!StringUtil.isEmpty(map.get("targetName"))) {
				navTabId = map.get("targetName").toString(); // 用于刷新查询页�?�
			}

			if ("1".equals(level)) {
				treeBuf.append("<div class='accordionHeader'>");
				treeBuf.append("<h2> <span>Folder</span> " + name + "</h2>");
				treeBuf.append("</div>");
				treeBuf.append("<div class='accordionContent'>");
			}

			if ("YES".equals(isLeaf)) {
				treeBuf.append("<li><a href='" + url + "' target='navTab' rel='" + navTabId + "'>" + name + "</a></li>");
			} else {

				if ("1".equals(level)) {
					treeBuf.append("<ul class='tree treeFolder'>");
				} else {
					treeBuf.append("<li><a>" + name + "</a>");
					treeBuf.append("<ul>");
				}

				buildAdminPermissionTree(id, treeBuf, menuList);

				if ("1".equals(level)) {
					treeBuf.append("</ul>");
				} else {
					treeBuf.append("</ul></li>");
				}

			}

			if ("1".equals(level)) {
				treeBuf.append("</div>");
			}
		}

	}

	/**
	 * 根�?�(pId)获�?�(menuList)中的所有�?�?��?�集�?�.
	 * 
	 * @param pId
	 *            父�?��?�ID.
	 * @param menuList
	 *            �?��?�集�?�.
	 * @return sonMenuList.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> getSonMenuListByPid(String pId, List menuList) {
		List sonMenuList = new ArrayList<Object>();
		for (Object menu : menuList) {
			Map map = (Map) menu;
			if (map != null) {
				String parentId = map.get("pId").toString();// 父id
				if (parentId.equals(pId)) {
					sonMenuList.add(map);
				}
			}
		}
		return sonMenuList;
	}

}
