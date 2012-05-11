package lt.jug.vilnius.test.dao.examples.jpa;

import java.util.HashSet;
import lt.jug.vilnius.test.dao.examples.jpa.model.Figure;
import lt.jug.vilnius.test.dao.examples.jpa.model.Point;
import lt.jug.vilnius.test.dao.springframework.DatabaseTestExecutionListener;
import lt.jug.vilnius.test.dao.springframework.annotations.DataSet;
import lt.jug.vilnius.test.dao.springframework.annotations.VerifyDataSet;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test PointDaoJpaImpl.
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/spring/jpa-hibernate-hsqldb.xml",
    "classpath:META-INF/spring/dao-layer-jpa.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DatabaseTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@DataSet("/datasets/initial-dataset.xml")
public class FigureDaoJpaImplTest {

    @Autowired
    private FigureDaoJpaImpl figureDaoJpaImpl;

    @Test
    @Transactional
    public void testFindById() {
        Figure figure = figureDaoJpaImpl.findById(1000L);
        assertNotNull("Not found", figure);
        assertEquals("Wrong count of vertextes", 2, figure.getVertexes().size());
    }

    @Test
    @Transactional
    public void testFindByIdNotFound() {
        Figure figure = figureDaoJpaImpl.findById(0L);
        assertNull("Wtf?? Found..", figure);
    }

    @Test
    @Transactional
    @Rollback(false)
    @VerifyDataSet(value="/datasets/figure-with-vertex-inserted-dataset.xml", 
            excludedColumns={"id", "figure_id"})
    public void testSaveInsertWithVertex() {
        Figure figure = new Figure();
        figure.setName("new");
        figure.setVertexes(new HashSet());
        Point p = new Point();
        p.setId(300L);
        figure.getVertexes().add(p);
        Figure saved = figureDaoJpaImpl.save(figure);
        assertNotNull("Id is null", saved.getId());
    }

    @Test
    @Transactional
    @Rollback(false)
    @VerifyDataSet("/datasets/figure-deleted-dataset.xml")
    public void testDelete() {
        Figure figure = figureDaoJpaImpl.findById(1000L);
        figureDaoJpaImpl.delete(figure);
    }
}
