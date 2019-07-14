/*
 * Copyright 2019 http://www.hswebframework.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.hswebframework.web.authorization;

import java.io.Serializable;
import java.util.*;

/**
 * 用户授�?�信�?�,当�?登录用户的�?��?信�?�,包括用户的基本信�?�,角色,�?��?集�?�等常用信�?�<br>
 * 获�?�方�?:
 * <ul>
 * <li>springmvc 入�?�方�?: ResponseMessage myTest(Authorization auth){}</li>
 * <li>�?��?方法方�?:AuthorizationHolder.get();</li>
 * </ul>
 *
 * @author zhouhao
 * @see AuthenticationHolder
 * @see AuthenticationManager
 * @since 3.0
 */
public interface Authentication extends Serializable {

    /**
     * 获�?�当�?登录的用户�?��?信�?�
     * <pre>
     *
     *   Authentication auth= Authentication.current().get();
     *   //如果�?��?信�?��?存在将抛出{@link NoSuchElementException}建议使用下�?�的方�?获�?�
     *   Authentication auth=Authentication.current().orElse(null);
     *   //或者
     *   Authentication auth=Authentication.current().orElseThrow(UnAuthorizedException::new);
     * </pre>
     *
     * @return 返回Optional对象进行�?作
     * @see Optional
     * @see AuthenticationHolder
     */
    static Optional<Authentication> current() {
        return Optional.ofNullable(AuthenticationHolder.get());
    }

    /**
     * @return 用户信�?�
     */
    User getUser();

    /**
     * @return 用户�?有的角色集�?�
     */
    List<Role> getRoles();

    /**
     * @return 用户�?有的�?��?集�?�
     */
    List<Permission> getPermissions();

    /**
     * 根�?�id获�?�角色,角色�?存在则返回null
     *
     * @param id 角色id
     * @return 角色信�?�
     */
    default Optional<Role> getRole(String id) {
        if (null == id) {
            return Optional.empty();
        }
        return getRoles().stream()
                .filter(role -> role.getId().equals(id))
                .findAny();
    }

    /**
     * 根�?��?��?id获�?��?��?信�?�,�?��?�?存在则返回null
     *
     * @param id �?��?id
     * @return �?��?信�?�
     */
    default Optional<Permission> getPermission(String id) {
        if (null == id) {
            return Optional.empty();
        }
        return getPermissions().stream()
                .filter(permission -> permission.getId().equals(id))
                .findAny();
    }

    /**
     * 判断是�?��?有�?�?��?以�?�对�?��?的�?��?作事件
     *
     * @param permissionId �?��?id {@link Permission#getId()}
     * @param actions      �?��?作事件 {@link Permission#getActions()} 如果为空,则�?判断action,�?�判断permissionId
     * @return 是�?��?有�?��?
     */
    default boolean hasPermission(String permissionId, String... actions) {
        return getPermission(permissionId)
                .filter(permission -> actions.length == 0 || permission.getActions().containsAll(Arrays.asList(actions)))
                .isPresent();
    }

    /**
     * @param roleId 角色id {@link Role#getId()}
     * @return 是�?�拥有�?个角色
     */
    default boolean hasRole(String roleId) {
        return getRole(roleId).isPresent();
    }

    /**
     * 根�?�属性�??获�?�属性值,返回一个{@link Optional}对象。<br>
     * 此方法�?�用于获�?�自定义的属性信�?�
     *
     * @param name 属性�??
     * @param <T>  属性值类型
     * @return Optional属性值
     */
    <T extends Serializable> Optional<T> getAttribute(String name);

    /**
     * 设置一个属性值,如果属性�??称已�?存在,则将其覆盖。<br>
     * 注�?:由于�?��?信�?��?�能会被�?列化,属性值必须实现{@link Serializable}接�?�
     *
     * @param name   属性�??称
     * @param object 属性值
     * @see AuthenticationManager#sync(Authentication)
     */
    void setAttribute(String name, Serializable object);

    /**
     * 设置多个属性值,�?�数为map类型,key为属性�??称,value为属性值
     *
     * @param attributes 属性值map
     * @see AuthenticationManager#sync(Authentication)
     */
    void setAttributes(Map<String, Serializable> attributes);

    /**
     * 删除属性,并返回被删除的值
     *
     * @param name 属性�??
     * @param <T>  被删除的值类型
     * @return 被删除的值
     * @see AuthenticationManager#sync(Authentication)
     */
    <T extends Serializable> T removeAttributes(String name);

    /**
     * 获�?�全部属性,此属性为通过{@link this#setAttribute(String, Serializable)}或{@link this#setAttributes(Map)}设置的属性。
     *
     * @return 全部属性集�?�
     */
    Map<String, Serializable> getAttributes();

}
