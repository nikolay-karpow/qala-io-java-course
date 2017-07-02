package com.nk.webapp;

import org.springframework.beans.factory.annotation.Autowired;

public class MyBean {

    private MySecondBean mySecondBean;

    public MyBean() {

    }

    @Autowired
    public void setMySecondBean(MySecondBean mySecondBean) {
        this.mySecondBean = mySecondBean;
        System.out.println("MyBean is created with property " + mySecondBean.getMsg());
    }

}
