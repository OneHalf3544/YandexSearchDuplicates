package ru.yandex.test;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 14.08.11
 * Time: 15:22
 */
@Aspect
public class LoggerAspect {

    Logger log = Logger.getLogger(LoggerAspect.class);

    @Pointcut("execution(public * ru.yandex.test.impl.*.*(..))")
    public void logableMethod() {}

    @Before("logableMethod()")
    public void logBefore(JoinPoint joinPoint) {
        StringBuilder message = new StringBuilder("execute method ").append(joinPoint.getSignature().getName());

        if (joinPoint.getArgs().length != 0) {
            message.append(", arguments: ").append(Arrays.toString(joinPoint.getArgs()));
        }

        log.info(message.toString());
    }
}
