package lt.jug.vilnius.test.dao.springframework.annotations;

import java.lang.annotation.*;

/**
 * Used to schema DDL script. 
 * This annotation is used with DatabaseTestExecutionListener
 *
 * @author Arturas Girenko, a.girenko@gmail.com 
  */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface DatabaseSchema {
    public String value();
}