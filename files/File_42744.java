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
package com.roncoo.pay.permission.biz;

import com.roncoo.pay.permission.service.PmsMenuService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author System
 * 
 * @since 2013-11-12
 */
@Component("pmsMenuBiz")
public class PmsMenuBiz {

	private static final Log log = LogFactory.getLog(PmsMenuBiz.class);

	@Autowired
	private PmsMenuService pmsMenuService;

	/**
	 * 获�?�用于编制�?��?�时的树.
	 */
	@SuppressWarnings("rawtypes")
	public String getTreeMenu(String actionUrl) {
		List treeData = pmsMenuService.getListByParent(null);
		StringBuffer strJson = new StringBuffer();
		recursionTreeMenu("0", strJson, treeData, actionUrl);
		return strJson.toString();
	}

	/**
	 * 递归输出树形�?��?�
	 * 
	 * @param pId
	 * @param buffer
	 */
	@SuppressWarnings("rawtypes")
	private void recursionTreeMenu(String pId, StringBuffer buffer, List list, String url) {
		if (pId.equals("0")) {
			buffer.append("<ul class=\"tree treeFolder collapse \" >");
		} else {
			buffer.append("<ul>");
		}
		List<Map> listMap = getSonMenuListByPid(pId, list);
		for (Map map : listMap) {
			String id = map.get("id").toString();// id
			String name = map.get("name").toString();// �??称
			String isLeaf = map.get("isLeaf").toString();// 是�?��?��?科目
			buffer.append("<li><a onclick=\"onClickMenuNode(" + id + ")\"  href=\"" + url + "?id=" + id + "\" target=\"ajax\" rel=\"jbsxBox\"  value=" + id + ">" + name + "</a>");
			if (!isLeaf.equals("1")) {
				recursionTreeMenu(id, buffer, list, url);
			}
			buffer.append("</li>");
		}
		buffer.append("</ul>");
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
