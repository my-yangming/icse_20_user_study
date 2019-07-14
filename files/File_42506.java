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

import com.roncoo.pay.permission.dao.PmsRolePermissionDao;
import com.roncoo.pay.permission.entity.PmsRolePermission;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �?��?-角色与�?��?点dao实现
 *
 * 龙果学院：www.roncoo.com
 * 
 * @author：shenjialong
 */
@Repository
public class PmsRolePermissionDaoImpl extends PermissionBaseDaoImpl<PmsRolePermission> implements PmsRolePermissionDao {

	/**
	 * 根�?�角色ID找到角色关�?�的�?��?点.
	 * 
	 * @param roleId
	 *            .
	 * @return rolePermissionList .
	 */
	public List<PmsRolePermission> listByRoleId(final long roleId) {
		return super.getSessionTemplate().selectList(getStatement("listByRoleId"), roleId);
	}

	/**
	 * 根�?�角色ID字符串获�?�相应角色-�?��?关�?�信�?�.
	 * 
	 * @param roleIds
	 * @return
	 */
	public List<PmsRolePermission> listByRoleIds(String roleIdsStr) {
		List<String> roldIds = Arrays.asList(roleIdsStr.split(","));
		return super.getSessionTemplate().selectList(getStatement("listByRoleIds"), roldIds);
	}
	
	public void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roleId", roleId);
		paramMap.put("permissionId", permissionId);
		super.getSessionTemplate().delete(getStatement("deleteByRoleIdAndPermissionId"), paramMap);
	}
	
	public void deleteByRoleId(Long roleId){
		super.getSessionTemplate().delete(getStatement("deleteByRoleId"), roleId);
	}
}
