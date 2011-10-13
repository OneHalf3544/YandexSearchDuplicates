package ru.yandex.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 12.10.11
 * Time: 22:33
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext beanFactory = new ClassPathXmlApplicationContext("classpath:context.xml");
        beanFactory.registerShutdownHook();
    }
}
