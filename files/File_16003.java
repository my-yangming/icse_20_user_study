package org.hswebframework.web;

import org.hswebframework.expands.script.engine.DynamicScriptEngine;
import org.hswebframework.expands.script.engine.DynamicScriptEngineFactory;
import org.hswebframework.expands.script.engine.ExecuteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达�?工具,用户解�?表达�?为字符串
 *
 * @author zhouhao
 * @since 3.0
 */
public class ExpressionUtils {

    //表达�?�??�?�正则 ${.+?}
    private static final Pattern PATTERN = Pattern.compile("(?<=\\$\\{)(.+?)(?=})");

    /**
     * 获�?�默认的表达�?�?��?
     *
     * @return �?��?集�?�
     */
    public static Map<String, Object> getDefaultVar() {
        return new HashMap<>();
    }

    /**
     * 获�?�默认的表达�?�?��?并将制定的�?��?�?�并在一起
     *
     * @param var �?�?�并的�?��?集�?�
     * @return �?��?集�?�
     */
    public static Map<String, Object> getDefaultVar(Map<String, Object> var) {
        Map<String, Object> vars = getDefaultVar();
        vars.putAll(var);
        return vars;
    }

    /**
     * 使用默认的�?��?解�?表达�?
     *
     * @param expression 表达�?字符串
     * @param language   表达�?语言
     * @return 解�?结果
     * @throws Exception 解�?错误
     * @see ExpressionUtils#analytical(String, Map, String)
     */
    public static String analytical(String expression, String language) throws Exception {
        return analytical(expression, new HashMap<>(), language);
    }

    /**
     * 解�?表达�?,表达�?使用{@link ExpressionUtils#PATTERN}进行�??�?�<br>
     * 如调用 analytical("http://${3+2}/test",var,"spel")<br>
     * 支�?的表达�?语言:
     * <ul>
     * <li>freemarker</li>
     * <li>spel</li>
     * <li>ognl</li>
     * <li>groovy</li>
     * <li>js</li>
     * </ul>
     *
     * @param expression 表达�?字符串
     * @param vars       �?��?
     * @param language   表达�?语言
     * @return 解�?结果
     * @throws Exception 解�?错误
     */
    public static String analytical(String expression, Map<String, Object> vars, String language) throws Exception {
        Matcher matcher = PATTERN.matcher(expression);
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine(language);
        if (engine == null) {
            return expression;
        }
        vars = new HashMap<>(vars);
        vars.putAll(getDefaultVar());
        while (matcher.find()) {
            String real_expression = matcher.group();
            String e_id = String.valueOf(real_expression.hashCode());
            if (!engine.compiled(e_id)) {
                engine.compile(e_id, real_expression);
            }
            ExecuteResult result = engine.execute(e_id, vars);
            if (!result.isSuccess()) {
                throw new RuntimeException(result.getMessage(), result.getException());
            }
            String obj = String.valueOf(result.get());
            // expression = matcher.replaceFirst(obj);
            expression = expression.replace("${" + real_expression + "}", obj);
        }
        return expression;
    }

}
