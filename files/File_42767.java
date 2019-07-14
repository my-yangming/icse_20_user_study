package com.roncoo.pay.permission.shiro.filter;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RcCaptchaValidateFilter extends AccessControlFilter {

	private boolean captchaEbabled = true;// 是�?�开�?�验�?�?支�?

	private String captchaParam = "captchaCode";// �?�?��??交的验�?�?�?�数�??

	private String failureKeyAttribute = "shiroLoginFailure"; // 验�?�?验�?失败�?�存储到的属性�??

	public void setCaptchaEbabled(boolean captchaEbabled) {
		this.captchaEbabled = captchaEbabled;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	public void setFailureKeyAttribute(String failureKeyAttribute) {
		this.failureKeyAttribute = failureKeyAttribute;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		// 1�?设置验�?�?是�?�开�?�属性，页�?��?�以根�?�该属性�?�决定是�?�显示验�?�?
		request.setAttribute("captchaEbabled", captchaEbabled);

		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		// 2�?判断验�?�?是�?��?用 或�?是表�?��??交（�?许访问）
		if (captchaEbabled == false || !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
			return true;
		}
		// 3�?此时是表�?��??交，验�?验�?�?是�?�正确
		// 获�?�页�?��??交的验�?�?
		String submitCaptcha = httpServletRequest.getParameter(captchaParam);
		// 获�?�session中的验�?�?
		String captcha = (String) httpServletRequest.getSession().getAttribute("rcCaptcha");
		if (submitCaptcha.equals(captcha)) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 如果验�?�?失败了，存储失败key属性
		request.setAttribute(failureKeyAttribute, "验�?�?错误!");
		return true;
	}

}
