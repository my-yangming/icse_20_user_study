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

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import apijson.demo.server.model.Comment;
import apijson.demo.server.model.Login;
import apijson.demo.server.model.Moment;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import apijson.demo.server.model.Verify;
import zuo.biao.apijson.Log;
import zuo.biao.apijson.MethodAccess;
import zuo.biao.apijson.server.AbstractVerifier;
import zuo.biao.apijson.server.Visitor;


/**�?��?验�?器
 * @author Lemon
 */
public class DemoVerifier extends AbstractVerifier<Long> {
	private static final String TAG = "DemoVerifier";


	// <TableName, <METHOD, allowRoles>>
	// <User, <GET, [OWNER, ADMIN]>>
	static { //注册�?��?
		ACCESS_MAP.put(User.class.getSimpleName(), getAccessMap(User.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Privacy.class.getSimpleName(), getAccessMap(Privacy.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Moment.class.getSimpleName(), getAccessMap(Moment.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Comment.class.getSimpleName(), getAccessMap(Comment.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Verify.class.getSimpleName(), getAccessMap(Verify.class.getAnnotation(MethodAccess.class)));
		ACCESS_MAP.put(Login.class.getSimpleName(), getAccessMap(Login.class.getAnnotation(MethodAccess.class)));
	}


	@NotNull
	@Override
	public DemoParser createParser() {
		DemoParser parser = new DemoParser();
		parser.setVisitor(visitor);
		return parser;
	}

	/**登录校验
	 * @author 
	 * @modifier Lemon
	 * @param session
	 * @throws Exception
	 */
	public static void verifyLogin(HttpSession session) throws Exception {
		Log.d(TAG, "verifyLogin  session.getId() = " + (session == null ? null : session.getId()));
		new DemoVerifier().setVisitor(getVisitor(session)).verifyLogin();
	}


	/**获�?��?�访用户的id
	 * @author Lemon
	 * @param session
	 * @return 
	 */
	public static long getVisitorId(HttpSession session) {
		if (session == null) {
			return 0;
		}
		Long id = (Long) session.getAttribute(Controller.USER_ID);
		if (id == null) {
			Visitor<Long> v = getVisitor(session);
			id = v == null ? 0 : value(v.getId());
			session.setAttribute(Controller.USER_ID, id);
		}
		return value(id);
	}
	/**获�?��?�访用户
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Visitor<Long> getVisitor(HttpSession session) {
		return session == null ? null : (Visitor<Long>) session.getAttribute(Controller.USER_);
	}

	public static long value(Long v) {
		return v == null ? 0 : v;
	}


}
