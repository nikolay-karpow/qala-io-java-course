package com.nk.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.sql.Connection;

import static com.nk.service.DependenciesHolderForTransactionalAspect.getConnectionHolder;
import static com.nk.service.DependenciesHolderForTransactionalAspect.getDataSource;

@Aspect
public class TransactionalAspect {

    @Around("execution(* com.nk.service.DogService.*(..))")
    public Object executeInTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try (Connection connection = getDataSource().getConnection()) {
            getConnectionHolder().set(connection);
            boolean savedAutoCommit = connection.getAutoCommit();
            try {
                connection.setAutoCommit(false);
                Object result = joinPoint.proceed();
                connection.commit();
                return result;
            } catch (Throwable e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(savedAutoCommit);
            }
        }
    }
}
