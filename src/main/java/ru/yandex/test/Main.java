package ru.yandex.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring context loader
 *
 * Date: 12.10.11
 * Time: 22:33
 *
 * @author OneHalf
 */
public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext beanFactory = new ClassPathXmlApplicationContext("classpath*:context.xml");
        beanFactory.registerShutdownHook();
    }
}
