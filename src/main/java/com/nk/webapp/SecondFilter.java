package com.nk.webapp;

import javax.servlet.*;
import java.io.IOException;

public class SecondFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Second filter works before servlet processing");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Second filter works after servlet processing");
    }

    public void destroy() {

    }
}
