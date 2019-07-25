/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.system.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.annotation.PreAuth;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.constant.RoleConstant;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.Func;
import org.springblade.system.entity.Menu;
import org.springblade.system.service.IMenuService;
import org.springblade.system.vo.MenuVO;
import org.springblade.system.wrapper.MenuWrapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Api(value = "�?��?�", tags = "�?��?�")
public class MenuController extends BladeController {

	private IMenuService menuService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入menu")
	public R<MenuVO> detail(Menu menu) {
		Menu detail = menuService.getOne(Condition.getQueryWrapper(menu));
		return R.data(MenuWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "�?��?�编�?�", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "name", value = "�?��?��??称", paramType = "query", dataType = "string")
	})
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "列表", notes = "传入menu")
	public R<List<MenuVO>> list(@ApiIgnore @RequestParam Map<String, Object> menu) {
		@SuppressWarnings("unchecked")
		List<Menu> list = menuService.list(Condition.getQueryWrapper(menu, Menu.class).lambda().orderByAsc(Menu::getSort));
		return R.data(MenuWrapper.build().listNodeVO(list));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增或修改", notes = "传入menu")
	public R submit(@Valid @RequestBody Menu menu) {
		return R.status(menuService.saveOrUpdate(menu));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集�?�", required = true) @RequestParam String ids) {
		return R.status(menuService.removeByIds(Func.toIntList(ids)));
	}

	/**
	 * �?端�?��?�数�?�
	 */
	@GetMapping("/routes")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "�?端�?��?�数�?�", notes = "�?端�?��?�数�?�")
	public R<List<MenuVO>> routes(BladeUser user) {
		List<MenuVO> list = menuService.routes(user.getRoleId());
		return R.data(list);
	}

	/**
	 * �?端按钮数�?�
	 */
	@GetMapping("/buttons")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "�?端按钮数�?�", notes = "�?端按钮数�?�")
	public R<List<MenuVO>> buttons(BladeUser user) {
		List<MenuVO> list = menuService.buttons(user.getRoleId());
		return R.data(list);
	}

	/**
	 * 获�?��?��?�树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<MenuVO>> tree() {
		List<MenuVO> tree = menuService.tree();
		return R.data(tree);
	}

	/**
	 * 获�?��?��?分�?树形结构
	 */
	@GetMapping("/grant-tree")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "�?��?分�?树形结构", notes = "�?��?分�?树形结构")
	public R<List<MenuVO>> grantTree(BladeUser user) {
		return R.data(menuService.grantTree(user));
	}

	/**
	 * 获�?��?��?分�?树形结构
	 */
	@GetMapping("/role-tree-keys")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "角色所分�?的树", notes = "角色所分�?的树")
	public R<List<String>> roleTreeKeys(String roleIds) {
		return R.data(menuService.roleTreeKeys(roleIds));
	}

	/**
	 * 获�?��?置的角色�?��?
	 */
	@GetMapping("auth-routes")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "�?��?�的角色�?��?")
	public R<List<Kv>> authRoutes(BladeUser user) {
		return R.data(menuService.authRoutes(user));
	}

}
