package com.zheng.upms.rpc.service.impl;

import com.zheng.upms.dao.mapper.*;
import com.zheng.upms.dao.model.*;
import com.zheng.upms.rpc.api.UpmsApiService;
import com.zheng.upms.rpc.mapper.UpmsApiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UpmsApiService实现
 * Created by shuzheng on 2016/01/19.
 */
@Service
@Transactional
public class UpmsApiServiceImpl implements UpmsApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsApiServiceImpl.class);

    @Autowired
    UpmsUserMapper upmsUserMapper;

    @Autowired
    UpmsApiMapper upmsApiMapper;

    @Autowired
    UpmsRolePermissionMapper upmsRolePermissionMapper;

    @Autowired
    UpmsUserPermissionMapper upmsUserPermissionMapper;

    @Autowired
    UpmsSystemMapper upmsSystemMapper;

    @Autowired
    UpmsOrganizationMapper upmsOrganizationMapper;

    @Autowired
    UpmsLogMapper upmsLogMapper;

    /**
     * 根�?�用户id获�?�所拥有的�?��?
     * @param upmsUserId
     * @return
     */
    @Override
    public List<UpmsPermission> selectUpmsPermissionByUpmsUserId(Integer upmsUserId) {
        // 用户�?存在或�?定状�?
        UpmsUser upmsUser = upmsUserMapper.selectByPrimaryKey(upmsUserId);
        if (null == upmsUser || 1 == upmsUser.getLocked()) {
            LOGGER.info("selectUpmsPermissionByUpmsUserId : upmsUserId={}", upmsUserId);
            return null;
        }
        List<UpmsPermission> upmsPermissions = upmsApiMapper.selectUpmsPermissionByUpmsUserId(upmsUserId);
        return upmsPermissions;
    }

    /**
     * 根�?�用户id获�?�所拥有的�?��?
     * @param upmsUserId
     * @return
     */
    @Override
    @Cacheable(value = "zheng-upms-rpc-service-ehcache", key = "'selectUpmsPermissionByUpmsUserId_' + #upmsUserId")
    public List<UpmsPermission> selectUpmsPermissionByUpmsUserIdByCache(Integer upmsUserId) {
        return selectUpmsPermissionByUpmsUserId(upmsUserId);
    }

    /**
     * 根�?�用户id获�?�所属的角色
     * @param upmsUserId
     * @return
     */
    @Override
    public List<UpmsRole> selectUpmsRoleByUpmsUserId(Integer upmsUserId) {
        // 用户�?存在或�?定状�?
        UpmsUser upmsUser = upmsUserMapper.selectByPrimaryKey(upmsUserId);
        if (null == upmsUser || 1 == upmsUser.getLocked()) {
            LOGGER.info("selectUpmsRoleByUpmsUserId : upmsUserId={}", upmsUserId);
            return null;
        }
        List<UpmsRole> upmsRoles = upmsApiMapper.selectUpmsRoleByUpmsUserId(upmsUserId);
        return upmsRoles;
    }

    /**
     * 根�?�用户id获�?�所属的角色
     * @param upmsUserId
     * @return
     */
    @Override
    @Cacheable(value = "zheng-upms-rpc-service-ehcache", key = "'selectUpmsRoleByUpmsUserId_' + #upmsUserId")
    public List<UpmsRole> selectUpmsRoleByUpmsUserIdByCache(Integer upmsUserId) {
        return selectUpmsRoleByUpmsUserId(upmsUserId);
    }

    /**
     * 根�?�角色id获�?�所拥有的�?��?
     * @param upmsRoleId
     * @return
     */
    @Override
    public List<UpmsRolePermission> selectUpmsRolePermisstionByUpmsRoleId(Integer upmsRoleId) {
        UpmsRolePermissionExample upmsRolePermissionExample = new UpmsRolePermissionExample();
        upmsRolePermissionExample.createCriteria()
                .andRoleIdEqualTo(upmsRoleId);
        List<UpmsRolePermission> upmsRolePermissions = upmsRolePermissionMapper.selectByExample(upmsRolePermissionExample);
        return upmsRolePermissions;
    }

    /**
     * 根�?�用户id获�?�所拥有的�?��?
     * @param upmsUserId
     * @return
     */
    @Override
    public List<UpmsUserPermission> selectUpmsUserPermissionByUpmsUserId(Integer upmsUserId) {
        UpmsUserPermissionExample upmsUserPermissionExample = new UpmsUserPermissionExample();
        upmsUserPermissionExample.createCriteria()
                .andUserIdEqualTo(upmsUserId);
        List<UpmsUserPermission> upmsUserPermissions = upmsUserPermissionMapper.selectByExample(upmsUserPermissionExample);
        return upmsUserPermissions;
    }

    /**
     * 根�?��?�件获�?�系统数�?�
     * @param upmsSystemExample
     * @return
     */
    @Override
    public List<UpmsSystem> selectUpmsSystemByExample(UpmsSystemExample upmsSystemExample) {
        return upmsSystemMapper.selectByExample(upmsSystemExample);
    }

    /**
     * 根�?��?�件获�?�组织数�?�
     * @param upmsOrganizationExample
     * @return
     */
    @Override
    public List<UpmsOrganization> selectUpmsOrganizationByExample(UpmsOrganizationExample upmsOrganizationExample) {
        return upmsOrganizationMapper.selectByExample(upmsOrganizationExample);
    }

    /**
     * 根�?�username获�?�UpmsUser
     * @param username
     * @return
     */
    @Override
    public UpmsUser selectUpmsUserByUsername(String username) {
        UpmsUserExample upmsUserExample = new UpmsUserExample();
        upmsUserExample.createCriteria()
                .andUsernameEqualTo(username);
        List<UpmsUser> upmsUsers = upmsUserMapper.selectByExample(upmsUserExample);
        if (null != upmsUsers && upmsUsers.size() > 0) {
            return upmsUsers.get(0);
        }
        return null;
    }

    /**
     * 写入�?作日志
     * @param record
     * @return
     */
    @Override
    public int insertUpmsLogSelective(UpmsLog record) {
        return upmsLogMapper.insertSelective(record);
    }

}
