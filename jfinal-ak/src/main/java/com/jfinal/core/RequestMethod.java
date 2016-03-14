package com.jfinal.core;

public enum RequestMethod {

    GET, POST, PUT, DELETE;
    
    public static final RequestMethod[] ALL = new RequestMethod[]{GET, POST, PUT, DELETE};

}