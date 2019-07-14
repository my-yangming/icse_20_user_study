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
package com.roncoo.pay.permission.service.impl;

import com.roncoo.pay.common.core.enums.PublicStatusEnum;
import com.roncoo.pay.common.core.page.PageBean;
import com.roncoo.pay.common.core.page.PageParam;
import com.roncoo.pay.permission.dao.PmsOperatorDao;
import com.roncoo.pay.permission.dao.PmsOperatorRoleDao;
import com.roncoo.pay.permission.entity.PmsOperator;
import com.roncoo.pay.permission.entity.PmsOperatorRole;
import com.roncoo.pay.permission.service.PmsOperatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * �?作员service接�?�实现
 *
 * 龙果学院：www.roncoo.com
 * 
 * @author：shenjialong
 */
@Service("pmsOperatorService")
public class PmsOperatorServiceImpl implements PmsOperatorService {
	@Autowired
	private PmsOperatorDao pmsOperatorDao;

	@Autowired
	private PmsOperatorRoleDao pmsOperatorRoleDao;

	/**
	 * 创建pmsOperator
	 */
	public void saveData(PmsOperator pmsOperator) {
		pmsOperatorDao.insert(pmsOperator);
	}

	/**
	 * 修改pmsOperator
	 */
	public void updateData(PmsOperator pmsOperator) {
		pmsOperatorDao.update(pmsOperator);
	}

	/**
	 * 根�?�id获�?�数�?�pmsOperator
	 * 
	 * @param id
	 * @return
	 */
	public PmsOperator getDataById(Long id) {
		return pmsOperatorDao.getById(id);

	}

	/**
	 * 分页查询pmsOperator
	 * 
	 * @param pageParam
	 * @param ActivityVo
	 *            PmsOperator
	 * @return
	 */
	public PageBean listPage(PageParam pageParam, PmsOperator pmsOperator) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginName", pmsOperator.getLoginName()); // �?作员登录�??（精确查询）
		paramMap.put("realName", pmsOperator.getRealName()); // �?作员姓�??（模糊查询）
		paramMap.put("status", pmsOperator.getStatus()); // 状�?

		return pmsOperatorDao.listPage(pageParam, paramMap);
	}

	/**
	 * 根�?�ID删除一个�?作员，�?�时删除与该�?作员关�?�的角色关�?�信�?�. type="1"的超级管�?�员�?能删除.
	 * 
	 * @param id
	 *            �?作员ID.
	 */
	public void deleteOperatorById(Long operatorId) {
		PmsOperator pmsOperator = pmsOperatorDao.getById(operatorId);
		if (pmsOperator != null) {
			if ("admin".equals(pmsOperator.getType())) {
				throw new RuntimeException("�?" + pmsOperator.getLoginName() + "】为超级管�?�员，�?能删除�?");
			}
			pmsOperatorDao.delete(operatorId);
			// 删除原�?�的角色与�?作员关�?�
			pmsOperatorRoleDao.deleteByOperatorId(operatorId);
		}
	}

	/**
	 * 更新�?作员信�?�.
	 * 
	 * @param operator
	 */
	public void update(PmsOperator operator) {
		pmsOperatorDao.update(operator);

	}

	/**
	 * 根�?��?作员ID更新�?作员密�?.
	 * 
	 * @param operatorId
	 * @param newPwd
	 *            (已进行SHA1加密)
	 */
	public void updateOperatorPwd(Long operatorId, String newPwd) {
		PmsOperator pmsOperator = pmsOperatorDao.getById(operatorId);
		pmsOperator.setLoginPwd(newPwd);
		pmsOperatorDao.update(pmsOperator);
	}

	/**
	 * 根�?�登录�??�?�得�?作员对象
	 */
	public PmsOperator findOperatorByLoginName(String loginName) {
		return pmsOperatorDao.findByLoginName(loginName);
	}

	/**
	 * �?存�?作員信�?��?�其关�?�的角色.
	 * 
	 * @param pmsOperator
	 *            .
	 * @param roleOperatorStr
	 *            .
	 */

	@Transactional
	public void saveOperator(PmsOperator pmsOperator, String roleOperatorStr) {
		// �?存�?作员信�?�
		pmsOperatorDao.insert(pmsOperator);
		// �?存角色关�?�信�?�
		if (StringUtils.isNotBlank(roleOperatorStr) && roleOperatorStr.length() > 0) {
			saveOrUpdateOperatorRole(pmsOperator, roleOperatorStr);
		}
	}

	/**
	 * �?存用户和角色之间的关�?�关系
	 */
	private void saveOrUpdateOperatorRole(PmsOperator pmsOperator, String roleIdsStr) {
		// 删除原�?�的角色与�?作员关�?�
		List<PmsOperatorRole> listPmsOperatorRoles = pmsOperatorRoleDao.listByOperatorId(pmsOperator.getId());
		Map<Long, PmsOperatorRole> delMap = new HashMap<Long, PmsOperatorRole>();
		for (PmsOperatorRole pmsOperatorRole : listPmsOperatorRoles) {
			delMap.put(pmsOperatorRole.getRoleId(), pmsOperatorRole);
		}
		if (StringUtils.isNotBlank(roleIdsStr)) {
			// 创建新的关�?�
			String[] roleIds = roleIdsStr.split(",");
			for (int i = 0; i < roleIds.length; i++) {
				long roleId = Long.parseLong(roleIds[i]);
				if (delMap.get(roleId) == null) {
					PmsOperatorRole pmsOperatorRole = new PmsOperatorRole();
					pmsOperatorRole.setOperatorId(pmsOperator.getId());
					pmsOperatorRole.setRoleId(roleId);
					pmsOperatorRole.setCreater(pmsOperator.getCreater());
					pmsOperatorRole.setCreateTime(new Date());
					pmsOperatorRole.setStatus(PublicStatusEnum.ACTIVE.name());
					pmsOperatorRoleDao.insert(pmsOperatorRole);
				} else {
					delMap.remove(roleId);
				}
			}
		}

		Iterator<Long> iterator = delMap.keySet().iterator();
		while (iterator.hasNext()) {
			long roleId = iterator.next();
			pmsOperatorRoleDao.deleteByRoleIdAndOperatorId(roleId, pmsOperator.getId());
		}
	}

	/**
	 * 修改�?作員信�?��?�其关�?�的角色.
	 * 
	 * @param pmsOperator
	 *            .
	 * @param roleOperatorStr
	 *            .
	 */
	public void updateOperator(PmsOperator pmsOperator, String roleOperatorStr) {
		pmsOperatorDao.update(pmsOperator);
		// 更新角色信�?�
		this.saveOrUpdateOperatorRole(pmsOperator, roleOperatorStr);
	}

}
