package com.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Route {
    /**
     * Name of package
     */
    String namespace();

    /**
     * Name of class
     */
    String activity();

    /**
     * Path of route
     */
    String path();

    /**
     * Name of group
     */
    String group();
}
