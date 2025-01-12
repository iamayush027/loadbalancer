package com.demo.loadbalancer.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // This means it will be used on methods
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}
