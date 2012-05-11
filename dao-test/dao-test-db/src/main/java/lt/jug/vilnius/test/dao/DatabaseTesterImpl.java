package lt.jug.vilnius.test.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.Assertion;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;

/**
 * Methods to simplify database tests.
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
public class DatabaseTesterImpl implements DatabaseTester {

    private DataSource dataSource;

    /**
     * Returns DataSource or lazily creates one.
     *
     * @return DataSource instance
     */
    public DataSource getDataSource() {
        return dataSource != null ? dataSource : createDataSource();
    }

    /**
     * Sets SQL syntax to ORACLE. This is HSQLDB specific feature, read more in
     * HSQLDB documentation.
     *
     * @return DatabaseTestHelper instance
     */
    public DatabaseTesterImpl withDatabaseSyntax(String syntaxName) {
        try {
            executeSql("SET DATABASE SQL SYNTAX " + syntaxName + " TRUE");
            return this;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Sets custom DataSource.
     *
     * @return DatabaseTestHelper instance
     */
    public DatabaseTesterImpl withDataSource(DataSource ds) {
        this.dataSource = ds;
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void createSchema(String schemaScriptLocation) {
        try {
            executeSql("DROP SCHEMA PUBLIC CASCADE");
            executeSql(loadSqlScript(schemaScriptLocation));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void loadDataset(String location) {
        try {
            DataFileLoader loader = new FlatXmlDataFileLoader();
            IDataSet dataset = loader.load(location);
            IDatabaseConnection conn = createDatabaseConnection();
            executeSql("SET DATABASE REFERENTIAL INTEGRITY FALSE");
            DatabaseOperation.CLEAN_INSERT.execute(conn, dataset);
            executeSql("SET DATABASE REFERENTIAL INTEGRITY TRUE");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void verifyWith(String datasetLocation) {
        verifyWith(datasetLocation, "id");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void verifyWith(String datasetLocation, String... excludedColumns) {
        IDatabaseConnection dbConn = null;
        try {
            dbConn = createDatabaseConnection();
            DataFileLoader loader = new FlatXmlDataFileLoader();
            IDataSet actualDataSet = dbConn.createDataSet();
            IDataSet expectedDataSet = loader.load(datasetLocation);
            // compare each table which presents in expected dataset with actual
            for (String name : expectedDataSet.getTableNames()) {
                ITable expecetdTable = expectedDataSet.getTable(name);
                // get only those colums wich exist in expected dataset
                ITable actualTable = DefaultColumnFilter.includedColumnsTable(actualDataSet.getTable(name),
                        expecetdTable.getTableMetaData().getColumns());

                //Exclude id fields - they can be unpredictable in case of inserts
                expecetdTable = DefaultColumnFilter.excludedColumnsTable(expecetdTable, excludedColumns);
                actualTable = DefaultColumnFilter.excludedColumnsTable(actualTable, excludedColumns);

                Assertion.assertEquals(expecetdTable, actualTable);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException ex) {
                    //meh.. whatever
                }
            }
        }
    }

    private DataSource createDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.hsqldb.jdbcDriver");
        ds.setUrl("jdbc:hsqldb:mem:sample");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    private IDatabaseConnection createDatabaseConnection() throws SQLException {
        IDatabaseConnection conn = new DatabaseDataSourceConnection(getDataSource());
        conn.getConfig().setProperty("http://www.dbunit.org/properties/datatypeFactory",
                new HsqldbDataTypeFactory());
        conn.getConnection().setReadOnly(false);
        return conn;
    }
    
    public void executeSql(String sql) throws SQLException, IOException {
        Connection conn = null;
        Statement st;
        try {
            conn = getDataSource().getConnection();
            st = conn.createStatement();
            st.execute(sql);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                // ignore  
            }
        }
    }

    private static String loadSqlScript(String filename) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        if (in == null) {
            throw new IOException("File not found: " + filename);
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder buffer = new StringBuilder();
        String line;
        String EOL = System.getProperty("line.separator");
        while ((line = r.readLine()) != null) {
            if (!isBlank(line) && !line.startsWith("#")) {
                buffer.append(line).append(EOL);
            }
        }
        in.close();
        return buffer.toString();
    }

    private static boolean isBlank(String line) {
        return line == null || line.trim().length() == 0;
    }
}
