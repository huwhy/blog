package com.jfinal.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Sql {
    String insertSQL();

    String updateSQL();
}
