package me.frep.vulcanlite.check.annotation;

import me.frep.vulcanlite.check.enums.CheckCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckInfo {

    String name() default "Check";
    String type() default "Type";
    String complexType() default "Complex Type";
    String description() default "Description";
    boolean experimental() default false;
    CheckCategory category() default CheckCategory.OTHER;
}
