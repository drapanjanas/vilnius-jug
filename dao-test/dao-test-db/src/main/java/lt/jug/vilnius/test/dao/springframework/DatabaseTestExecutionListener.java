package lt.jug.vilnius.test.dao.springframework;

import lt.jug.vilnius.test.dao.DatabaseTesterImpl;
import javax.sql.DataSource;
import lt.jug.vilnius.test.dao.springframework.annotations.DataSet;
import lt.jug.vilnius.test.dao.springframework.annotations.DatabaseSchema;
import lt.jug.vilnius.test.dao.springframework.annotations.DatabaseSyntax;
import lt.jug.vilnius.test.dao.springframework.annotations.VerifyDataSet;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * TestExecutionListener which inserts data form DBUnit dataset file into
 * database before each test run. DataSet can be set via annotation @DataSet (if
 * not set, default is <classname>-dataset.xml). Also, data in database can be
 * verified after test against target dataset using @VerifyDataSet annotation
 * <p/>
 * NOTE1: This listener should be registered before
 * TransactionalTestExecutionLister NOTE2: if @VerifyDataSet is used, you need
 * also to use @Rollback(false) annotation on same test
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
public class DatabaseTestExecutionListener implements TestExecutionListener {

    private DatabaseTesterImpl helper;

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        helper = new DatabaseTesterImpl().withDataSource(testContext.getApplicationContext().getBean(DataSource.class));
        DatabaseSyntax syntax = testContext.getTestClass().getAnnotation(DatabaseSyntax.class);
        if (syntax != null) {
            helper = helper.withDatabaseSyntax(syntax.value());
        }
        DatabaseSchema schema = testContext.getTestClass().getAnnotation(DatabaseSchema.class);
        if (schema != null) {
            helper.createSchema(schema.value());
        }
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        String dataSetResourcePath = null;
        // first, the annotation on the test method
        DataSet dsLocation = testContext.getTestMethod().getAnnotation(DataSet.class);
        if (dsLocation != null) {
            dataSetResourcePath = dsLocation.value();
        } else {
            // second, the annotation on the test class
            dsLocation = testContext.getTestInstance().getClass().getAnnotation(DataSet.class);
            if (dsLocation != null) {
                dataSetResourcePath = dsLocation.value();
            } else {
                // no annotation, just fail
                throw new IllegalStateException("No dataset is configured, please add @DataSet annotation");
            }
        }
        if (dataSetResourcePath != null) {
            helper.loadDataset(dataSetResourcePath);
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        if (testContext.getTestException() == null) {
            VerifyDataSet verifyDataSet = testContext.getTestMethod().getAnnotation(VerifyDataSet.class);
            if (verifyDataSet != null) {
                helper.verifyWith(verifyDataSet.value(), verifyDataSet.excludedColumns());
            }
        }
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
    }
}
