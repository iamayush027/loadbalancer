package com.demo.loadbalancer.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long duration = end - start;

        logger.debug("Execution time of {} is {} ms", joinPoint.getSignature(), duration);

        return result;
    }
}
