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
package com.roncoo.pay.permission.dao.impl;

import com.roncoo.pay.permission.dao.PmsMenuDao;
import com.roncoo.pay.permission.entity.PmsMenu;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * �?��?�?��?�
 *
 * 龙果学院：www.roncoo.com
 * 
 * @author：shenjialong
 */
@Repository("pmsMenuDao")
public class PmsMenuDaoImpl extends PermissionBaseDaoImpl<PmsMenu> implements PmsMenuDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List listByRoleIds(String roleIdsStr) {
		List<String> roldIds = Arrays.asList(roleIdsStr.split(","));
		return super.getSessionTemplate().selectList(getStatement("listByRoleIds"), roldIds);
	}

	/**
	 * 根�?�父�?��?�ID获�?�该�?��?�下的所有�?孙�?��?�.<br/>
	 * 
	 * @param parentId
	 *            (如果为空，则为获�?�所有的�?��?�).<br/>
	 * @return menuList.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List listByParent(Long parentId) {
		return super.getSessionTemplate().selectList(getStatement("listByParent"), parentId);
	}

	/**
	 * 根�?��?��?�ID查找�?��?�（�?�用于判断�?��?�下是�?�还有�?�?��?�）.
	 * 
	 * @param parentId
	 *            .
	 * @return menuList.
	 */
	@Override
	public List<PmsMenu> listByParentId(Long parentId) {
		return super.getSessionTemplate().selectList(getStatement("listByParentId"), parentId);
	}

	/***
	 * 根�?��??称和是�?��?��?节点查询数�?�
	 * 
	 * @param isLeaf
	 *            是�?�是�?��?节点
	 * @param name
	 *            节点�??称
	 * @return
	 */
	public List<PmsMenu> getMenuByNameAndIsLeaf(Map<String, Object> map) {
		return super.getSessionTemplate().selectList(getStatement("listBy"), map);
	}

}
