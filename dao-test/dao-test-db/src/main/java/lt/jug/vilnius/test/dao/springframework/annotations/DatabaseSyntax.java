package lt.jug.vilnius.test.dao.springframework.annotations;

import java.lang.annotation.*;

/**
 * Used to specify HSQLDB supported syntaxes, like ORA, MYSQL. 
 * This annotation is used with DatabaseTestExecutionListener
 *
 * @author Arturas Girenko, a.girenko@gmail.com 
  */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface DatabaseSyntax {
    public String value();
}