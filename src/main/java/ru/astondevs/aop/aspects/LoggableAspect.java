package ru.astondevs.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Аспект для логирования методов и классов, аннотированных {@code @Loggable}.
 * При вызове таких методов в лог будут выводиться сообщения о начале выполнения метода,
 * его завершении и времени выполнения.
 */
@Aspect
public class LoggableAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggableAspect.class);

    /**
     * Точка среза для для классов, аннотированных {@code @Loggable}.
     */
    @Pointcut("within(@ru.astondevs.aop.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {

    }

    /**
     * Совет, который выполняется до и после вызова методов,
     * в соответсвии с определенной точкой среза.
     * @param proceedingJoinPoint предоставляет информацию о перехваченном методе
     * @return результат выполнения метода, тип {@link Object}
     * @throws Throwable возможное исключение {@link Throwable}
     */
    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}
