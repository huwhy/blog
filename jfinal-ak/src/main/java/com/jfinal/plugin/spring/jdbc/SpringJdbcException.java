package com.jfinal.plugin.spring.jdbc;

public class SpringJdbcException extends RuntimeException {

    private static final long serialVersionUID = 342820722361408621L;

    public SpringJdbcException(String message) {
        super(message);
    }

    public SpringJdbcException(Throwable cause) {
        super(cause);
    }

    public SpringJdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}










