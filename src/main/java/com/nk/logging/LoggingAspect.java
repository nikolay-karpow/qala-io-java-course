package com.nk.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    private final static Logger logger = Logger.getLogger("LoggingAspect");

    @Before("execution(@com.nk.logging.Logged * *(..))")
    public void logMethodInvocation(JoinPoint joinPoint) {
        logger.info(joinPoint.getStaticPart().getSignature().toLongString());
    }
}
