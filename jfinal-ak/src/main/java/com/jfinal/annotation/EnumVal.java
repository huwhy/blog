package com.jfinal.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface EnumVal {
    EnumValType value() default EnumValType.Ordinal;
}
