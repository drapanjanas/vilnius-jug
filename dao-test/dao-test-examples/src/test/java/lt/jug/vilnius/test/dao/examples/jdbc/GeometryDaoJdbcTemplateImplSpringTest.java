package lt.jug.vilnius.test.dao.examples.jdbc;

import java.util.Set;

import lt.jug.vilnius.test.dao.springframework.DatabaseTestExecutionListener;
import lt.jug.vilnius.test.dao.springframework.annotations.DataSet;
import lt.jug.vilnius.test.dao.springframework.annotations.DatabaseSchema;
import lt.jug.vilnius.test.dao.springframework.annotations.DatabaseSyntax;
import lt.jug.vilnius.test.dao.springframework.annotations.VerifyDataSet;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Test GeomentryDaoJdbcTemplateImpl.
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/spring/jdbc-hsqldb.xml",
    "classpath:META-INF/spring/dao-layer-jdbc.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DatabaseTestExecutionListener.class})
@DatabaseSyntax("ORA")
@DatabaseSchema("create-schema.sql")
@DataSet("/datasets/initial-dataset.xml")
public class GeometryDaoJdbcTemplateImplSpringTest {

    @Autowired
    private GeometryDaoJdbcTemplateImpl geometryDaoJdbcTemplateImpl;
    
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
    @VerifyDataSet("/datasets/figure-inserted-dataset.xml")
    public void testCreateFigure() throws Exception {
        Long figureId = geometryDaoJdbcTemplateImpl.createFigure("new");
        assertNotNull("Figure Id is null", figureId);
    }
    
    @Test
    @VerifyDataSet("/datasets/vertex-inerted-dataset.xml")
    public void testAddVertex() throws Exception {
        geometryDaoJdbcTemplateImpl.addVertex(2000L, new Point(1, 2));
    }
    
    @Test
    public void testAddVertexPointNotExist() throws Exception {
        try {
            geometryDaoJdbcTemplateImpl.addVertex(2000L, new Point(0, 0));
            fail("Exception was not thrown");
        }
        catch (IllegalArgumentException ex) {
            assertEquals("Wrong exception message", "Provided point was not found", ex.getMessage()); 
        }
    }
    
     @Test
     public void testAddVertexFigureNotExist() throws Exception {
        try {
            geometryDaoJdbcTemplateImpl.addVertex(0L, new Point(1, 2));
            fail("Exception was not thrown");
        }
        catch (Exception ex) {
            //ok
        }
    }
}
