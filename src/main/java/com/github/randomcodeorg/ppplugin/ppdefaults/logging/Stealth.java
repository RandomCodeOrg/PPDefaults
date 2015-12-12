package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that will exclude annotated parameters, fields and methods from the log.
 * @author Marcel Singer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Stealth {

}
