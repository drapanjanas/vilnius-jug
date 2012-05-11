package lt.jug.vilnius.test.dao.examples.jdbc;

import java.util.Set;
import lt.jug.vilnius.test.dao.DatabaseTesterImpl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Test GeomentryDaoJdbcTemplateImpl.
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
public class GeometryDaoJdbcTemplateImplTest {
    private static DatabaseTesterImpl helper;
    private static GeometryDaoJdbcTemplateImpl geometryDaoJdbcTemplateImpl;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        helper = new DatabaseTesterImpl().withDatabaseSyntax("ORA");
        helper.createSchema("create-schema.sql");
        geometryDaoJdbcTemplateImpl = new GeometryDaoJdbcTemplateImpl();
        geometryDaoJdbcTemplateImpl.setJdbcTemplate(new JdbcTemplate(helper.getDataSource()));
    }

    @Before
    public void setUp() throws Exception {
        helper.loadDataset("/datasets/initial-dataset.xml");
    }

    @Test
    public void testGetVertextes() {
        Set<Point> points = geometryDaoJdbcTemplateImpl.getVertexes(1000L);
        assertEquals("Wrong count of vertexes", 2, points.size());
    }

    @Test
    public void testFindPoint() {
        Point point = geometryDaoJdbcTemplateImpl.findPoint(1, 2);
        assertNotNull("Point not found", point);
    }

    @Test
    public void testFindPointNotFound() {
        Point point = geometryDaoJdbcTemplateImpl.findPoint(0, 0);
        assertNull("Point found", point);
    }

    @Test
    public void testCreateFigure() throws Exception {
        Long figureId = geometryDaoJdbcTemplateImpl.createFigure("new");
        assertNotNull("Figure Id is null", figureId);
        helper.verifyWith("/datasets/figure-inserted-dataset.xml");
    }

    @Test
    public void testAddVertex() throws Exception {
        geometryDaoJdbcTemplateImpl.addVertex(2000L, new Point(1, 2));
        helper.verifyWith("/datasets/vertex-inerted-dataset.xml");
    }

    @Test
    public void testAddVertexPointNotExist() throws Exception {
        try {
            geometryDaoJdbcTemplateImpl.addVertex(2000L, new Point(0, 0));
            fail("Exception was not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Wrong exception message", "Provided point was not found", ex.getMessage());
        }
    }

    @Test
    public void testAddVertexFigureNotExist() throws Exception {
        try {
            geometryDaoJdbcTemplateImpl.addVertex(0L, new Point(1, 2));
            fail("Exception was not thrown");
        } catch (Exception ex) {
            //ok
        }
    }
}
