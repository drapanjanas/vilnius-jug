package lt.jug.vilnius.test.dao.springframework.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to specify dataset which is verified against actual database
 * data after annotated test. This annotation is used with
 * DBUnitTestExecutionListener
 *
 * NOTE: You will have to annotate your test with @Rollback(false)
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface VerifyDataSet {
    public String value();
    public String[] excludedColumns() default {"id"};
}