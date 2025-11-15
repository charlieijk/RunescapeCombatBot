package rs.kreme.ksbot.api.scripts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to describe scripts within the bot framework.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScriptManifest {
    String name();
    String author();
    Category category();
    String description() default "";
    String[] servers() default {};
    double version() default 1.0;
}
