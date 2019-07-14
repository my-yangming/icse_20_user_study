package com.geekq.admin.entity;

import com.geekq.common.utils.numcal.BitStatesUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.util.StringUtils;

/**
 * @author 邱润泽
 */
@Getter
@Setter
@Alias("UserInfo")
public class Userinfo extends BaseDomain {

    private static final long serialVersionUID = -2194938919842714855L;
    /**
     * 版本�?�
     */
    private int version;
    /**
     * �?状�?
     */
    private Long bitState = 0L;
    /**
     * 对应实�??认�?中的真实姓�??
     */
    private String realName;
    /**
     * 对应实�??认�?中的身份�?�?�
     */
    private String idNumber;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     *  手机�?�
     */
    private String phoneNumber = "";
    /**
     * 用户当�?认�?分数
     */
    private int authScore = 0;
    /**
     * 用户有效的实�??认�?对象
     */
    private Long realauthId;

    /**
     * 会员的基本资料
     * 月收入
     */
    private SystemDictionaryItem incomeGrade;
    /**
     *  婚姻情况
     */
    private SystemDictionaryItem marriage;
    /**
     * �?女情况
     */
    private SystemDictionaryItem kidCount;
    /**
     * 学历
     */
    private SystemDictionaryItem educationBackground;
    /**
     * �?房�?�件
     */
    private SystemDictionaryItem houseCondition;

    public static Userinfo empty(Long id) {
        Userinfo userinfo = new Userinfo();
		userinfo.setId(id);
		userinfo.setBitState(BitStatesUtils.OP_BASIC_INFO);
        return userinfo;
    }

    public void addState(Long state) {
        this.bitState = BitStatesUtils.addState(this.bitState, state);
    }

    public void removeState(Long state) {
        this.bitState = BitStatesUtils.removeState(this.bitState, state);
    }

    public boolean getIsBindPhone() {

        return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_BIND_PHONE);
    }

    public boolean getIsBindEmail() {
        return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_BIND_EMAIL);
    }

    public boolean getBaseInfo() {
        return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_BASE_INFO);
    }

    public boolean getRealAuth() {
        return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_REAL_AUTH);
    }

    public boolean getVedioAuth() {
        return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_VEDIO_AUTH);
    }

    public boolean getHasBidRequest(){
        return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_HAS_BIDRQUEST);
    }

    /**
     * 获�?�用户真实�??字的�?�?字符串，�?�显示姓�?
     *
     * @param 真实�??字
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获�?�用户真实�??字的�?�?字符串，�?�显示姓�?
     *
     * @param realName
     *            真实�??字
     * @return
     */
    public static String getAnonymousRealName(String realName) {
        if (StringUtils.hasLength(realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获�?�用户身份�?��?的�?�?字符串
     *
     * @param idNumber
     * @return
     */
    public static String getAnonymousIdNumber(String idNumber) {
        if (StringUtils.hasLength(idNumber)) {
            int len = idNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += idNumber.charAt(i);
                }
            }
            return replace;
        }
        return idNumber;
    }


    /**
     * 获�?�用户手机�?��?的�?�?字符串
     *
     * @param phoneNumber
     *            用户手机�?��?
     * @return
     */
    public static String getAnonymousPhoneNumber(String phoneNumber) {
        if (StringUtils.hasLength(phoneNumber)) {
            int len = phoneNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if (i > 2 && i < 7) {
                    replace += "*";
                } else {
                    replace += phoneNumber.charAt(i);
                }
            }
            return replace;
        }
        return phoneNumber;
    }

    /**
     * 获�?�用户�?�?�的�?�?字符串
     *
     * @param currentAddress
     *            用户�?�?�
     * @return
     */
    public static String getAnonymousCurrentAddress(String currentAddress) {
        if (StringUtils.hasLength(currentAddress)
                && currentAddress.length() > 4) {
            String last = currentAddress.substring(currentAddress.length() - 4,
                    currentAddress.length());
            String stars = "";
            for (int i = 0; i < currentAddress.length() - 4; i++) {
                stars += "*";
            }
            return stars + last;
        }
        return currentAddress;
    }
}
