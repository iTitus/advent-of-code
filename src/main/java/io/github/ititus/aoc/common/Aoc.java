package io.github.ititus.aoc.common;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aoc {

    int year();

    int day();

    boolean skip() default false;

}
