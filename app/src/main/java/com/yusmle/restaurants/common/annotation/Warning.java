package com.yusmle.restaurants.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target should be used carefully according to the warning message.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.SOURCE)
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD,
    ElementType.PACKAGE,
    ElementType.PARAMETER,
    ElementType.TYPE
})
public @interface Warning {

    /**
     * Text of warning message
     *
     * @return warning message value
     */
    String value();
}
