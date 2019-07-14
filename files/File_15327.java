/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.server;

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;

import java.net.URLDecoder;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import apijson.demo.server.model.BaseModel;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.JSONRequest;
import zuo.biao.apijson.server.exception.ConditionErrorException;
import zuo.biao.apijson.server.exception.ConflictException;
import zuo.biao.apijson.server.exception.NotExistException;
import zuo.biao.apijson.server.exception.OutOfRangeException;


/**request controller
 * <br > 建议全通过HTTP POST�?�请求:
 * <br > 1.�?少代�? - 客户端无需写HTTP GET,PUT等�?��?方�?的请求代�?
 * <br > 2.�??高性能 - 无需URL encode和decode
 * <br > 3.调试方便 - 建议使用 APIJSON在线测试工具 或 Postman
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {
	private static final String TAG = "Controller";

	//通用接�?�，�?�事务型�?作 和 简�?�事务型�?作 都�?�通过这些接�?�自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**获�?�
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@PostMapping(value = "get")
	public String get(@RequestBody String request, HttpSession session) {
		return new DemoParser(GET).setSession(session).parse(request);
	}

	/**计数
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@PostMapping("head")
	public String head(@RequestBody String request, HttpSession session) {
		return new DemoParser(HEAD).setSession(session).parse(request);
	}

	/**�?制性GET，request和response都�?�明文，�?览器看�?到，用于对安全性�?求高的GET请求
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	@PostMapping("gets")
	public String gets(@RequestBody String request, HttpSession session) {
		return new DemoParser(GETS).setSession(session).parse(request);
	}

	/**�?制性HEAD，request和response都�?�明文，�?览器看�?到，用于对安全性�?求高的HEAD请求
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	@PostMapping("heads")
	public String heads(@RequestBody String request, HttpSession session) {
		return new DemoParser(HEADS).setSession(session).parse(request);
	}

	/**新增
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	@PostMapping("post")
	public String post(@RequestBody String request, HttpSession session) {
		return new DemoParser(POST).setSession(session).parse(request);
	}

	/**修改
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	@PostMapping("put")
	public String put(@RequestBody String request, HttpSession session) {
		return new DemoParser(PUT).setSession(session).parse(request);
	}

	/**删除
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	@PostMapping("delete")
	public String delete(@RequestBody String request, HttpSession session) {
		return new DemoParser(DELETE).setSession(session).parse(request);
	}





	/**获�?�
	 * �?�为兼容HTTP GET请求，推�??用HTTP POST，�?�删除
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@RequestMapping("get/{request}")
	public String openGet(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser会报错
		}
		return get(request, session);
	}

	/**计数
	 * �?�为兼容HTTP GET请求，推�??用HTTP POST，�?�删除
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	@RequestMapping("head/{request}")
	public String openHead(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser会报错
		}
		return head(request, session);
	}


	//通用接�?�，�?�事务型�?作 和 简�?�事务型�?作 都�?�通过这些接�?�自动化实现>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>













	public static final String USER_;
	public static final String PRIVACY_;
	public static final String VERIFY_; //加下划线�?�缀是为了�?��? Verify 和 verify 都�?�VERIFY，分�?清
	static {
		USER_ = User.class.getSimpleName();
		PRIVACY_ = Privacy.class.getSimpleName();
		VERIFY_ = Verify.class.getSimpleName();
	}

	public static final String VERSION = JSONRequest.KEY_VERSION;
	public static final String FORMAT = JSONRequest.KEY_FORMAT;
	public static final String COUNT = JSONResponse.KEY_COUNT;
	public static final String TOTAL = JSONResponse.KEY_TOTAL;

	public static final String ID = "id";
	public static final String USER_ID = "userId";
	public static final String CURRENT_USER_ID = "currentUserId";

	public static final String NAME = "name";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String _PASSWORD = "_password";
	public static final String _PAY_PASSWORD = "_payPassword";
	public static final String OLD_PASSWORD = "oldPassword";
	public static final String VERIFY = "verify";

	public static final String TYPE = "type";



	/**生�?验�?�?,修改为post请求
	 * @param request
	 * @return
	 */
	@PostMapping("post/verify")
	public JSONObject postVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		int type;
		String phone;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}

		new DemoParser(DELETE, true).parse(newVerifyRequest(type, phone, null));

		JSONObject response = new DemoParser(POST, true).parseResponse(
				newVerifyRequest(type, phone, "" + (new Random().nextInt(9999) + 1000))
				);

		JSONObject verify = null;
		try {
			verify = response.getJSONObject(StringUtil.firstCase(VERIFY_));
		} catch (Exception e) {}

		if (verify == null || JSONResponse.isSuccess(verify.getIntValue(JSONResponse.KEY_CODE)) == false) {
			new DemoParser(DELETE, true).parseResponse(new JSONRequest(new Verify(type, phone)));
			return response;
		}

		//TODO 这里直接返回验�?�?，方便测试。实际上应该�?�返回�?功信�?�，验�?�?通过短信�?��?
		JSONObject object = new JSONObject();
		object.put(TYPE, type);
		object.put(PHONE, phone);
		return getVerify(JSON.toJSONString(object));
	}

	/**获�?�验�?�?
	 * @param request
	 * @return
	 */
	@PostMapping("gets/verify")
	public JSONObject getVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		int type;
		String phone;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}
		return new DemoParser(GETS, true).parseResponse(newVerifyRequest(type, phone, null));
	}

	/**校验验�?�?
	 * @param request
	 * @return
	 */
	@PostMapping("heads/verify")
	public JSONObject headVerify(@RequestBody String request) {
		JSONObject requestObject = null;
		int type;
		String phone;
		String verify;
		try {
			requestObject = DemoParser.parseRequest(request);
			type = requestObject.getIntValue(TYPE);
			phone = requestObject.getString(PHONE);
			verify = requestObject.getString(VERIFY);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}
		return headVerify(type, phone, verify);
	}

	/**校验验�?�?
	 * @author Lemon
	 * @param type
	 * @param phone
	 * @param code
	 * @return
	 */
	public JSONObject headVerify(int type, String phone, String code) {
		JSONResponse response = new JSONResponse(
				new DemoParser(GETS, true).parseResponse(
						new JSONRequest(
								new Verify(type, phone)
								.setVerify(code)
								).setTag(VERIFY_)
						)
				);
		Verify verify = response.getObject(Verify.class);
		if (verify == null) {
			return DemoParser.newErrorResult(StringUtil.isEmpty(code, true)
					? new NotExistException("验�?�?�?存在�?") : new ConditionErrorException("手机�?�或验�?�?错误�?"));
		}

		//验�?�?过期
		long time = BaseModel.getTimeMillis(verify.getDate());
		long now = System.currentTimeMillis();
		if (now > 60*1000 + time) {
			new DemoParser(DELETE, true).parseResponse(
					new JSONRequest(new Verify(type, phone)).setTag(VERIFY_)
					);
			return DemoParser.newErrorResult(new TimeoutException("验�?�?已过期�?"));
		}

		return new JSONResponse(
				new DemoParser(HEADS, true).parseResponse(
						new JSONRequest(new Verify(type, phone).setVerify(code)).setFormat(true)
						)
				);
	}



	/**新建一个验�?�?请求
	 * @param phone
	 * @param verify
	 * @return
	 */
	private zuo.biao.apijson.JSONRequest newVerifyRequest(int type, String phone, String verify) {
		return new JSONRequest(new Verify(type, phone).setVerify(verify)).setTag(VERIFY_).setFormat(true);
	}


	public static final String LOGIN = "login";
	public static final String REMEMBER = "remember";
	public static final String DEFAULTS = "defaults";

	public static final int LOGIN_TYPE_PASSWORD = 0;//密�?登录
	public static final int LOGIN_TYPE_VERIFY = 1;//验�?�?登录
	/**用户登录
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @return
	 * @see
	 * <pre>
		{
			"type": 0,  //登录方�?，�?�必须  0-密�? 1-验�?�?
			"phone": "13000082001",
			"password": "1234567",
			"version": 1 //全局版本�?�，�?�必须
		}
	 * </pre>
	 */
	@PostMapping(LOGIN)
	public JSONObject login(@RequestBody String request, HttpSession session) {
		JSONObject requestObject = null;
		boolean isPassword;
		String phone;
		String password;
		int version;
		Boolean format;
		boolean remember;
		JSONObject defaults;
		try {
			requestObject = DemoParser.parseRequest(request);

			isPassword = requestObject.getIntValue(TYPE) == LOGIN_TYPE_PASSWORD;//登录方�?
			phone = requestObject.getString(PHONE);//手机
			password = requestObject.getString(PASSWORD);//密�?

			if (StringUtil.isPhone(phone) == false) {
				throw new IllegalArgumentException("手机�?��?�?�法�?");
			}

			if (isPassword) {
				if (StringUtil.isPassword(password) == false) {
					throw new IllegalArgumentException("密�?�?�?�法�?");
				}
			} else {
				if (StringUtil.isVerify(password) == false) {
					throw new IllegalArgumentException("验�?�?�?�?�法�?");
				}
			}

			version = requestObject.getIntValue(VERSION);
			format = requestObject.getBoolean(FORMAT);
			remember = requestObject.getBooleanValue(REMEMBER);
			defaults = requestObject.getJSONObject(DEFAULTS); //默认加到�?个请求最外层的字段
			requestObject.remove(VERSION);
			requestObject.remove(FORMAT);
			requestObject.remove(REMEMBER);
			requestObject.remove(DEFAULTS);
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}



		//手机�?�是�?�已注册
		JSONObject phoneResponse = new DemoParser(HEADS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						)
				);
		if (JSONResponse.isSuccess(phoneResponse) == false) {
			return DemoParser.newResult(phoneResponse.getIntValue(JSONResponse.KEY_CODE), phoneResponse.getString(JSONResponse.KEY_MSG));
		}
		JSONResponse response = new JSONResponse(phoneResponse).getJSONResponse(PRIVACY_);
		if(JSONResponse.isExist(response) == false) {
			return DemoParser.newErrorResult(new NotExistException("手机�?�未注册"));
		}

		//根�?�phone获�?�User
		JSONObject privacyResponse = new DemoParser(GETS, true).parseResponse(
				new JSONRequest(
						new Privacy().setPhone(phone)
						).setFormat(true)
				);
		response = new JSONResponse(privacyResponse);

		Privacy privacy = response == null ? null : response.getObject(Privacy.class);
		long userId = privacy == null ? 0 : BaseModel.value(privacy.getId());
		if (userId <= 0) {
			return privacyResponse;
		}

		//校验凭�? 
		if (isPassword) {//password密�?登录
			response = new JSONResponse(
					new DemoParser(HEADS, true).parseResponse(
							new JSONRequest(new Privacy(userId).setPassword(password))
							)
					);
		} else {//verify手机验�?�?登录
			response = new JSONResponse(headVerify(Verify.TYPE_LOGIN, phone, password));
		}
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		response = response.getJSONResponse(isPassword ? PRIVACY_ : VERIFY_);
		if (JSONResponse.isExist(response) == false) {
			return DemoParser.newErrorResult(new ConditionErrorException("账�?�或密�?错误"));
		}

		response = new JSONResponse(
				new DemoParser(GETS, true).parseResponse(
						new JSONRequest(new User(userId)).setFormat(true)
						)
				);
		User user = response.getObject(User.class);
		if (user == null || BaseModel.value(user.getId()) != userId) {
			return DemoParser.newErrorResult(new NullPointerException("�?务器内部错误"));
		}

		//登录状�?�?存至session
		session.setAttribute(USER_ID, userId); //用户id
		session.setAttribute(TYPE, isPassword ? LOGIN_TYPE_PASSWORD : LOGIN_TYPE_VERIFY); //登录方�?
		session.setAttribute(USER_, user); //用户
		session.setAttribute(PRIVACY_, privacy); //用户�?�?信�?�
		session.setAttribute(VERSION, version); //全局默认版本�?�
		session.setAttribute(FORMAT, format); //全局默认格�?化�?置
		session.setAttribute(REMEMBER, remember); //是�?�记�?登录
		session.setAttribute(DEFAULTS, defaults); //给�?个请求JSON最外层加的字段
		session.setMaxInactiveInterval(60*60*24*(remember ? 7 : 1)); //设置session过期时间

		response.put(REMEMBER, remember);
		response.put(DEFAULTS, defaults);
		return response;
	}

	/**退出登录，清空session
	 * @param session
	 * @return
	 */
	@PostMapping("logout")
	public JSONObject logout(HttpSession session) {
		long userId;
		try {
			userId = DemoVerifier.getVisitorId(session);//必须在session.invalidate();�?�?
			Log.d(TAG, "logout  userId = " + userId + "; session.getId() = " + (session == null ? null : session.getId()));
			session.invalidate();
		} catch (Exception e) {
			return DemoParser.newErrorResult(e);
		}

		JSONObject result = DemoParser.newSuccessResult();
		JSONObject user = DemoParser.newSuccessResult();
		user.put(ID, userId);
		user.put(COUNT, 1);
		result.put(StringUtil.firstCase(USER_), user);

		return result;
	}


	private static final String REGISTER = "register";
	/**注册
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @return
	 * @see
	 * <pre>
		{
			"Privacy": {
				"phone": "13000082222",
				"_password": "123456"
			},
			"User": {
				"name": "APIJSONUser"
			},
			"verify": "1234"
		}
	 * </pre>
	 */
	@PostMapping(REGISTER)
	public JSONObject register(@RequestBody String request) {
		JSONObject requestObject = null;

		JSONObject privacyObj;

		String phone;
		String verify;
		String password;
		try {
			requestObject = DemoParser.parseRequest(request);
			privacyObj = requestObject.getJSONObject(PRIVACY_);

			phone = StringUtil.getString(privacyObj.getString(PHONE));
			verify = requestObject.getString(VERIFY);
			password = privacyObj.getString(_PASSWORD);

			if (StringUtil.isPhone(phone) == false) {
				return newIllegalArgumentResult(requestObject, PRIVACY_ + "/" + PHONE);
			}
			if (StringUtil.isPassword(password) == false) {
				return newIllegalArgumentResult(requestObject, PRIVACY_ + "/" + _PASSWORD);
			}
			if (StringUtil.isVerify(verify) == false) {
				return newIllegalArgumentResult(requestObject, VERIFY);
			}
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}


		JSONResponse response = new JSONResponse(headVerify(Verify.TYPE_REGISTER, phone, verify));
		if (JSONResponse.isSuccess(response) == false) {
			return response;
		}
		//手机�?�或验�?�?错误
		if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
			return DemoParser.extendErrorResult(response, new ConditionErrorException("手机�?�或验�?�?错误�?"));
		}



		//生�?User和Privacy
		if (StringUtil.isEmpty(requestObject.getString(JSONRequest.KEY_TAG), true)) {
			requestObject.put(JSONRequest.KEY_TAG, REGISTER);
		}
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		response = new JSONResponse( 
				new DemoParser(POST).setNoVerifyLogin(true).parseResponse(requestObject)
				);

		//验�?User和Privacy
		User user = response.getObject(User.class);
		long userId = user == null ? 0 : BaseModel.value(user.getId());
		Privacy privacy = response.getObject(Privacy.class);
		long userId2 = privacy == null ? 0 : BaseModel.value(privacy.getId());
		Exception e = null;
		if (userId <= 0 || userId != userId2) { //id�?�?�
			e = new Exception("�?务器内部错误�?写入User或Privacy失败�?");
		}

		if (e != null) { //出现错误，回退
			new DemoParser(DELETE, true).parseResponse(
					new JSONRequest(new User(userId))
					);
			new DemoParser(DELETE, true).parseResponse(
					new JSONRequest(new Privacy(userId2))
					);
		}

		return response;
	}


	/**
	 * @param requestObject
	 * @param key
	 * @return
	 */
	public static JSONObject newIllegalArgumentResult(JSONObject requestObject, String key) {
		return newIllegalArgumentResult(requestObject, key, null);
	}
	/**
	 * @param requestObject
	 * @param key
	 * @param msg 详细说明
	 * @return
	 */
	public static JSONObject newIllegalArgumentResult(JSONObject requestObject, String key, String msg) {
		return DemoParser.extendErrorResult(requestObject
				, new IllegalArgumentException(key + ":value 中value�?�?�法�?" + StringUtil.getString(msg)));
	}


	/**设置密�?
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @return
	 * @see
	 * <pre>
	    使用旧密�?修改
		{
			"oldPassword": 123456,
			"Privacy":{
			  "id": 13000082001,
			  "_password": "1234567"
			}
		}
		或使用手机�?�+验�?�?修改
		{
			"verify": "1234",
			"Privacy":{
			  "phone": "13000082001",
			  "_password": "1234567"
			}
		}
	 * </pre>
	 */
	@PostMapping("put/password")
	public JSONObject putPassword(@RequestBody String request){
		JSONObject requestObject = null;
		String oldPassword;
		String verify;

		int type = Verify.TYPE_PASSWORD;

		JSONObject privacyObj;
		long userId;
		String phone;
		String password;
		try {
			requestObject = DemoParser.parseRequest(request);
			oldPassword = StringUtil.getString(requestObject.getString(OLD_PASSWORD));
			verify = StringUtil.getString(requestObject.getString(VERIFY));

			requestObject.remove(OLD_PASSWORD);
			requestObject.remove(VERIFY);

			privacyObj = requestObject.getJSONObject(PRIVACY_);
			if (privacyObj == null) {
				throw new IllegalArgumentException(PRIVACY_ + " �?能为空�?");
			}
			userId = privacyObj.getLongValue(ID);
			phone = privacyObj.getString(PHONE);
			password = privacyObj.getString(_PASSWORD);

			if (StringUtil.isEmpty(password, true)) { //支付密�?
				type = Verify.TYPE_PAY_PASSWORD;
				password = privacyObj.getString(_PAY_PASSWORD);
				if (StringUtil.isNumberPassword(password) == false) {
					throw new IllegalArgumentException(PRIVACY_ + "/" + _PAY_PASSWORD + ":value 中value�?�?�法�?");
				}
			} else { //登录密�?
				if (StringUtil.isPassword(password) == false) {
					throw new IllegalArgumentException(PRIVACY_ + "/" + _PASSWORD + ":value 中value�?�?�法�?");
				}
			}
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}


		if (StringUtil.isPassword(oldPassword)) {
			if (userId <= 0) { //手机�?�+验�?�?�?需�?userId
				return DemoParser.extendErrorResult(requestObject, new IllegalArgumentException(ID + ":value 中value�?�?�法�?"));
			}
			if (oldPassword.equals(password)) {
				return DemoParser.extendErrorResult(requestObject, new ConflictException("新旧密�?�?能一样�?"));
			}

			//验�?旧密�?
			Privacy privacy = new Privacy(userId);
			if (type == Verify.TYPE_PASSWORD) {
				privacy.setPassword(oldPassword);
			} else {
				privacy.setPayPassword(oldPassword);
			}
			JSONResponse response = new JSONResponse( 
					new DemoParser(HEAD, true).parseResponse(
							new JSONRequest(privacy).setFormat(true)
							)
					);
			if (JSONResponse.isExist(response.getJSONResponse(PRIVACY_)) == false) {
				return DemoParser.extendErrorResult(requestObject, new ConditionErrorException("账�?�或原密�?错误，请�?新输入�?"));
			}
		}
		else if (StringUtil.isPhone(phone) && StringUtil.isVerify(verify)) {
			JSONResponse response = new JSONResponse(headVerify(type, phone, verify));
			if (JSONResponse.isSuccess(response) == false) {
				return response;
			}
			if (JSONResponse.isExist(response.getJSONResponse(VERIFY_)) == false) {
				return DemoParser.extendErrorResult(response, new ConditionErrorException("手机�?�或验�?�?错误�?"));
			}
			response = new JSONResponse(
					new DemoParser(GET, true).parseResponse(
							new JSONRequest(
									new Privacy().setPhone(phone)
									)
							)
					);
			Privacy privacy = response.getObject(Privacy.class);
			long id = privacy == null ? 0 : BaseModel.value(privacy.getId());
			privacyObj.remove(PHONE);
			privacyObj.put(ID, id);

			requestObject.put(PRIVACY_, privacyObj);
		} else {
			return DemoParser.extendErrorResult(requestObject, new IllegalArgumentException("请输入�?�法的 旧密�? 或 手机�?�+验�?�? �?"));
		}
		//TODO 上线版加上   password = MD5Util.MD5(password);


		//		requestObject.put(JSONRequest.KEY_TAG, "Password");
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		//修改密�?
		return new DemoParser(PUT, true).parseResponse(requestObject);
	}



	/**充值/�??现
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see
	 * <pre>
		{
			"Privacy": {
				"id": 82001,
				"balance+": 100,
				"_payPassword": "123456"
			}
		}
	 * </pre>
	 */
	@PostMapping("put/balance")
	public JSONObject putBalance(@RequestBody String request, HttpSession session) {
		JSONObject requestObject = null;
		JSONObject privacyObj;
		long userId;
		String payPassword;
		double change;
		try {
			DemoVerifier.verifyLogin(session);
			requestObject = new DemoParser(PUT).setRequest(DemoParser.parseRequest(request)).parseCorrectRequest();

			privacyObj = requestObject.getJSONObject(PRIVACY_);
			if (privacyObj == null) {
				throw new NullPointerException("请设置 " + PRIVACY_ + "�?");
			}
			userId = privacyObj.getLongValue(ID);
			payPassword = privacyObj.getString(_PAY_PASSWORD);
			change = privacyObj.getDoubleValue("balance+");

			if (userId <= 0) {
				throw new IllegalArgumentException(PRIVACY_ + "." + ID + ":value 中value�?�?�法�?");
			}
			if (StringUtil.isPassword(payPassword) == false) {
				throw new IllegalArgumentException(PRIVACY_ + "." + _PAY_PASSWORD + ":value 中value�?�?�法�?");
			}
		} catch (Exception e) {
			return DemoParser.extendErrorResult(requestObject, e);
		}

		//验�?密�?<<<<<<<<<<<<<<<<<<<<<<<

		privacyObj.remove("balance+");
		JSONResponse response = new JSONResponse(
				new DemoParser(HEADS, true).setSession(session).parseResponse(
						new JSONRequest(PRIVACY_, privacyObj)
						)
				);
		response = response.getJSONResponse(PRIVACY_);
		if (JSONResponse.isExist(response) == false) {
			return DemoParser.extendErrorResult(requestObject, new ConditionErrorException("支付密�?错误�?"));
		}

		//验�?密�?>>>>>>>>>>>>>>>>>>>>>>>>


		//验�?金�?范围<<<<<<<<<<<<<<<<<<<<<<<

		if (change == 0) {
			return DemoParser.extendErrorResult(requestObject, new OutOfRangeException("balance+的值�?能为0�?"));
		}
		if (Math.abs(change) > 10000) {
			return DemoParser.extendErrorResult(requestObject, new OutOfRangeException("�?�次 充值/�??现 的金�?�?能超过10000元�?"));
		}

		//验�?金�?范围>>>>>>>>>>>>>>>>>>>>>>>>

		if (change < 0) {//�??现
			response = new JSONResponse(
					new DemoParser(GETS, true).parseResponse(
							new JSONRequest(
									new Privacy(userId)
									)
							)
					);
			Privacy privacy = response == null ? null : response.getObject(Privacy.class);
			long id = privacy == null ? 0 : BaseModel.value(privacy.getId());
			if (id != userId) {
				return DemoParser.extendErrorResult(requestObject, new Exception("�?务器内部错误�?"));
			}

			if (BaseModel.value(privacy.getBalance()) < -change) {
				return DemoParser.extendErrorResult(requestObject, new OutOfRangeException("余�?�?足�?"));
			}
		}


		privacyObj.remove(_PAY_PASSWORD);
		privacyObj.put("balance+", change);
		requestObject.put(PRIVACY_, privacyObj);
		requestObject.put(JSONRequest.KEY_TAG, PRIVACY_);
		requestObject.put(JSONRequest.KEY_FORMAT, true);
		//�?�?验�?，里�?�会验�?身份
		return new DemoParser(PUT).setSession(session).parseResponse(requestObject);
	}


}
