package com.nk.service;

import com.nk.dao.JdbcConnectionHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.sql.DataSource;
import java.sql.Connection;

@Aspect
public class TransactionalAspect {

    private DataSource dataSource;
    private JdbcConnectionHolder connectionHolder;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setConnectionHolder(JdbcConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Around("execution(* com.nk.service.DogService.*(..))")
    public Object executeInTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try (Connection connection = dataSource.getConnection()) {
            connectionHolder.set(connection);
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
