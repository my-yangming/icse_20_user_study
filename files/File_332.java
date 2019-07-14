package com.crossoverjie.concurrent;

/**
 * Function:�?�例模�?-�?��?检查�?
 *
 * @author crossoverJie
 *         Date: 09/03/2018 01:14
 * @since JDK 1.8
 */
public class Singleton {

    private static volatile Singleton singleton;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    //防止指令�?排
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
