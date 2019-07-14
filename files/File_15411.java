package apijson.demo.server;

import static zuo.biao.apijson.RequestMethod.DELETE;
import static zuo.biao.apijson.RequestMethod.GET;
import static zuo.biao.apijson.RequestMethod.GETS;
import static zuo.biao.apijson.RequestMethod.HEAD;
import static zuo.biao.apijson.RequestMethod.HEADS;
import static zuo.biao.apijson.RequestMethod.POST;
import static zuo.biao.apijson.RequestMethod.PUT;

import com.jfinal.kit.HttpKit;

import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import zuo.biao.apijson.JSONResponse;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.server.JSONRequest;

public class Controller extends com.jfinal.core.Controller {

	//通用接�?�，�?�事务型�?作 和 简�?�事务型�?作 都�?�通过这些接�?�自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	/**获�?�
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	public void get() {
		renderJson(new DemoParser(GET).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**计数
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEAD}
	 */
	public void head() {
		renderJson(new DemoParser(HEAD).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**�?制性GET，request和response都�?�明文，�?览器看�?到，用于对安全性�?求高的GET请求
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GETS}
	 */
	public void gets() {
		renderJson(new DemoParser(GETS).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**�?制性HEAD，request和response都�?�明文，�?览器看�?到，用于对安全性�?求高的HEAD请求
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#HEADS}
	 */
	public void heads() {
		renderJson(new DemoParser(HEADS).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**新增
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#POST}
	 */
	public void post() {
		renderJson(new DemoParser(POST).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**修改
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#PUT}
	 */
	public void put() {
		renderJson(new DemoParser(PUT).setSession(getSession()).parse(HttpKit.readData(getRequest())));
	}

	/**删除
	 * @param request �?�用String，�?��?encode�?�未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#DELETE}
	 */
	public void delete() {
		renderJson(new DemoParser(DELETE).setSession(getSession()).parse(HttpKit.readData(getRequest())));
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

}
