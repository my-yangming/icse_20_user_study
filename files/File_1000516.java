package org.nutz.el.opt.object;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import org.nutz.el.opt.TwoTernary;

/**
 * 数组读�?�
 * 将'['�?�为读�?��?作符使用,它读�?�两个�?�数,一个是数组本身,一个是下标
 * 多维数组,则是先读出一维,然�?��?读�?�下一维度的数�?�
 * @author juqkai(juqkai@gmail.com)
 *
 */
public class ArrayOpt extends TwoTernary {
    public int fetchPriority() {
        return 1;
    }
    @SuppressWarnings("rawtypes")
    public Object calculate() {
        Object lval = calculateItem(left);
        Object rval = calculateItem(right);
        
        //@ JKTODO 这里�?�?�?与, AccessOpt 里�?�相�?�的代�?�?�并呢?
        if(lval instanceof Map){
            Map<?,?> om = (Map<?, ?>) lval;
            if(om.containsKey(right.toString())){
                return om.get(right.toString());
            }
        } else if (lval instanceof List) {
            return ((List)lval).get((Integer)rval);
        }
        
        return Array.get(lval, (Integer)rval);
    }
    public String fetchSelf() {
        return "[";
    }
}
