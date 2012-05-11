package lt.jug.vilnius.test.dao;

/**
 * Commonly used operation needed in DB tests.
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
public interface DatabaseTester {

    /**
     * Drops and creates database schema.
     *
     * @param schemaScriptLocation - Schema DDL script
     */
    void createSchema(String schemaScriptLocation);

    /**
     * Cleans all data and insert from dataset file.
     *
     * @param datasetLocation - dataset location
     */
    void loadDataset(String datasetLocation);

    /**
     * Compares data in database with provided dataset.
     *
     * @param datasetLocation - dataset to compare with
     */
    void verifyWith(String datasetLocation);

    /**
     * Compares data in database with provided dataset.
     *
     * @param datasetLocation - dataset to compare with
     * @param excludeColumns - exclude colums during comparison
     */
    void verifyWith(String datasetLocation, String... excludedColumns);
}
