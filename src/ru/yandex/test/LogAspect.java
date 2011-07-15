package ru.yandex.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 14.07.11
 * Time: 21:39
 */
@Aspect
public class LogAspect {

    @Pointcut("execution(* ru.yandex.test.VacancySource.*(..))")
    public void vacancyBuildPointcut() {}

    
    @Before("vacancyBuildPointcut()")
    public void logBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(joinPoint.getSignature().toString());
    }

    @After("vacancyBuildPointcut()")
    public void logAfter(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(joinPoint.getSignature().toString());
    }
}
