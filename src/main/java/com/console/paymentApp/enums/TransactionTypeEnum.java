package com.console.paymentApp.enums;


import org.springframework.util.StringUtils;

public enum TransactionTypeEnum {
    CREDIT, DEBIT;

    public boolean toEquals(String name){
        return !StringUtils.isEmpty(name)? this.name().equalsIgnoreCase(name) : Boolean.FALSE;
    }
}
