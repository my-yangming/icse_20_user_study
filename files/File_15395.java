package apijson.demo.server;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import apijson.demo.server.config.ConfigYml;
import com.alibaba.fastjson.JSONObject;
import apijson.demo.server.model.Privacy;
import apijson.demo.server.model.User;
import org.springframework.stereotype.Component;
import zuo.biao.apijson.RequestMethod;
import zuo.biao.apijson.StringUtil;
import zuo.biao.apijson.server.AbstractSQLConfig;
import zuo.biao.apijson.server.Join;
import zuo.biao.apijson.server.SQLConfig;


/**SQL�?置
 * @author Lemon
 */
@Component
public class DemoSQLConfig extends AbstractSQLConfig {
	//表�??映射，�?�?真实表�??，对安全�?求很高的表�?�以这么�?�
	static {
		TABLE_KEY_MAP.put(User.class.getSimpleName(), "apijson_user");
		TABLE_KEY_MAP.put(Privacy.class.getSimpleName(), "apijson_privacy");
	}

	ConfigYml dataSourceConfig = new ConfigYml();
	Map dataSource = dataSourceConfig.read();

	@Override
	public String getDBUri() {
		//TODO 改�?你自己的
		return dataSource.get("url")+"";
	}
	@Override
	public String getDBAccount() {
		return dataSource.get("username")+"";
	}
	@Override
	public String getDBPassword() {
		return dataSource.get("password")+"";
	}
	@Override
	public String getSchema() {
		String s = super.getSchema();
		return StringUtil.isEmpty(s, true) ? dataSource.get("schema")+"" : s; //TODO 改�?你自己的
	}
	
	@Override
	public String getAlias() { //getTable �?能�?写，因为Verifier用大�?写�?感的�??称判断�?��?
		return super.getAlias();
	}
	

	public DemoSQLConfig() throws FileNotFoundException {
		this(RequestMethod.GET);
	}
	public DemoSQLConfig(RequestMethod method) throws FileNotFoundException {
		super(method);
	}
	public DemoSQLConfig(RequestMethod method, String table) throws FileNotFoundException {
		super(method, table);
	}
	public DemoSQLConfig(RequestMethod method, int count, int page) throws FileNotFoundException {
		super(method, count, page);
	}


	/**获�?�SQL�?置
	 * @param table
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static SQLConfig newSQLConfig(RequestMethod method, String table, JSONObject request, List<Join> joinList) throws Exception {
		return newSQLConfig(method, table, request, joinList, new Callback() {

			@Override
			public DemoSQLConfig getSQLConfig(RequestMethod method, String table) {
				try {
					return new DemoSQLConfig(method, table);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}


}
