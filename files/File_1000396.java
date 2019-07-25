package org.nutz.dao.impl.link;

import java.util.Map;

import org.nutz.dao.FieldFilter;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.entity.LinkField;
import org.nutz.dao.impl.AbstractLinkVisitor;
import org.nutz.lang.Lang;

public class DoUpdateLinkVisitor extends AbstractLinkVisitor {

    public void visit(Object obj, final LinkField lnk) {
        Object value = lnk.getValue(obj);
        if (Lang.eleSize(value) == 0)
            return;
        if (value instanceof Map<?, ?>)
            value = ((Map<?, ?>) value).values();

        FieldMatcher fm = FieldFilter.get(lnk.getLinkedEntity().getType());

        // 如果需�?忽略 Null 字段，则为�?个 POJO 都生�?一�?�语�?�
        if (null != fm && fm.isIgnoreNull()) {
            opt.addUpdateForIgnoreNull(lnk.getLinkedEntity(), value, fm);
        }
        // �?�则生�?一�?�批处�?�语�?�
        else {
            opt.addUpdate(lnk.getLinkedEntity(), value);
        }

    }

}
