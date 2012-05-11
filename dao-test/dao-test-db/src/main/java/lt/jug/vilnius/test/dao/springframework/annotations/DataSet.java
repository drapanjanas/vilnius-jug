package lt.jug.vilnius.test.dao.springframework.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to specify dataset to be loaded before each test execution. 
 * This annotation is used with DBUnitTestExecutionListener
 *
 * @author Arturas Girenko, a.girenko@gmail.com 
  */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface DataSet {
    public String value();
}