package com.nk.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiDataSourceFactory {
    public static DataSource dataSource(String jndiPath) throws NamingException {
        Context initialContext = new InitialContext();
        return (DataSource)initialContext.lookup(jndiPath);
    }
}
