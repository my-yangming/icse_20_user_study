package com.github.vole.mps.constants;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信通�?�模�?�
 */
public class GrantType {

//    /**
//     * 客户端验�?
//     */
//    CLIENT("client", "客户端验�?"),
//    /**
//     * 用户�??密�?验�?
//     */
//    PASSWORD("password", "用户�??密�?验�?"),
//    /**
//     * 刷新验�?
//     */
//    REFRESH_TOKEN("refresh_token","刷新验�?"),
//    /**
//     * 授�?�验�?
//     */
//    AUTHORIZATION_CODE("authorization_code", "授�?�验�?");


    /**
     * 模�?��??称
     */
    @Getter
    @Setter
    private String key;
    /**
     * 模�?�签�??
     */
    @Getter
    @Setter
    private String name;

    GrantType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public static List<GrantType> list(){
        List<GrantType>  grantTypes = new ArrayList<GrantType>();
        GrantType client = new GrantType("client", "客户端验�?");
        grantTypes.add(client);
        GrantType password = new GrantType("password", "用户�??密�?验�?");
        grantTypes.add(password);
        GrantType refreshToken = new GrantType("refresh_token", "刷新验�?");
        grantTypes.add(refreshToken);
        GrantType authorizationCode = new GrantType("authorization_code", "授�?�验�?");
        grantTypes.add(authorizationCode);
        return grantTypes;
    }
}
