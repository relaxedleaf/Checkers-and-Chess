package com.lemon.check.Evacheck.Checkers;

import java.io.Serializable;

public class NullChecker extends Checker implements Serializable{
    private static String type = "NullChecker";
    public NullChecker(){
        super(type);
    }
}
