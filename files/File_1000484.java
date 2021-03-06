package org.nutz.el.opt.arithmetic;

import java.util.Queue;

import org.nutz.el.ElException;
import org.nutz.el.opt.AbstractOpt;

/**
 * "("
 * @author juqkai(juqkai@gmail.com)
 *
 */
public class LBracketOpt extends AbstractOpt{
    public String fetchSelf() {
        return "(";
    }
    public int fetchPriority() {
        return 100;
    }
    
    public void wrap(Queue<Object> obj) {
        throw new ElException("'('符�?��?能进行wrap�?作!");
    }
    public Object calculate() {
        throw new ElException("'('符�?��?能进行计算�?作!");
    }
}
