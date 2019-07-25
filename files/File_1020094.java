package com.myimooc.spring.simple.ioc.interfaces;

/**
 * <br>
 * 标题: �?��?�接�?�编程<br>
 * �??述: �?�外一个实现<br>
 * 时间: 2017/01/18<br>
 *
 * @author zc
 */
public class OneInterfacesImpl implements OneInterface {

    public String hello(String word) {
        return "Word form interfaces\"OneInterface\":" + word;
    }

    @Override
    public void say(String arg) {
        System.out.println("OneInterfacesImpl say：" + arg);
    }

}
