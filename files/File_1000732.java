package org.nutz.mapl.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 递归MapList结构, 将路径与相应的值传递给�?类进行处�?�.
 * @author juqkai(juqkai@gmail.com)
 */
public abstract class MaplEach {
    //路径
    protected LinkedList<String> paths = new LinkedList<String>();
    protected LinkedList<Integer> arrayIndex = new LinkedList<Integer>();
    /**
     * 转�?�对象
     * @param obj
     */
    protected void each(Object obj) {
        if(obj instanceof Map){
            convertMap((Map<?, ?>) obj);
        } else if(obj instanceof List){
            convertList((List<?>) obj);
        }
    }
    /**
     * 转�?�map
     * @param obj
     */
    private void convertMap(Map<?, ?> obj) {
        for(Object key : obj.keySet()){
            paths.addLast(key.toString());
            DLR(fetchPath(), obj.get(key));
            each(obj.get(key));
            LRD(fetchPath(), obj.get(key));
            paths.removeLast();
        }
    }
    /**
     * �??�?�路径
     * @return
     */
    private String fetchPath(){
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for(String path : paths){
            if(!first){
                sb.append(".");
            }
            sb.append(path);
            first = false;
        }
        return sb.toString();
    }
    /**
     * 转�?�LIST
     * @param val
     */
    private void convertList(List<?> val){
        if(paths.size() <= 0){
            paths.add("[]");
        }else{
            paths.addLast(paths.removeLast() + "[]");
        }
        for(int i = 0; i < val.size(); i++){
            arrayIndex.addLast(i);
            each(val.get(i));
            arrayIndex.removeLast();
        }
    }
    
    /**
     * �?�?
     * @param path
     * @param item
     */
    protected abstract void DLR(String path, Object item);
    /**
     * �?��?
     * @param path
     * @param item
     */
    protected abstract void LRD(String path, Object item);
}
