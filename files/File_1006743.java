package org.javacore.base.String; /*
 * Copyright [2015] [Jeff Lee]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author BYSocket
 * @since 2016-01-06 19:51:00
 *  常�?�?�试题：String作为方法�?�数传递,�?�外一个例�? ${@link StringT2}
 *  这就�?��?�“值传递�?，�?�方法�?作的是�?�数�?��?（也就是原型�?��?的一个值的拷�?）
 *  改�?�的也�?�是原型�?��?的一个拷�?而已，而�?��?��?本身
 */
public class StringT {
    public static void main(String[] args) {
        String str = "123";
        change(str);
        System.out.println(str);
    }

    public static void change(String str) {
        str = "456";
    }
}
