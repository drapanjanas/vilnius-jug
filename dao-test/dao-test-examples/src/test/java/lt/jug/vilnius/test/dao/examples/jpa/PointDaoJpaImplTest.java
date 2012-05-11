package lt.jug.vilnius.test.dao.examples.jpa;

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
public class PointDaoJpaImplTest {

    @Autowired 
    private PointDaoJpaImpl pointDaoJpaImpl;
    
    @Test
    @Transactional
    public void testFindById() {
        Point point = pointDaoJpaImpl.findById(300L);
        assertNotNull("Not found", point);
    }
    
    @Test
    @Transactional
    public void testFindByIdNotFound() {
        Point point = pointDaoJpaImpl.findById(0L);
        assertNull("Wtf?? Found..", point);
    }
    
    @Test
    @Transactional
    @Rollback(false)
    @VerifyDataSet("/datasets/insert-point-dataset.xml")
    public void testSaveInsert() {
        Point point = new Point();
        point.setX(10);
        point.setY(8);
        Point saved = pointDaoJpaImpl.save(point);
        assertNotNull("Id is null", saved.getId());
    }
    
    @Test
    @Transactional
    @Rollback(false)
    @VerifyDataSet("/datasets/update-point-dataset.xml")
    public void testSaveUpdate() {
        Point point = pointDaoJpaImpl.findById(300L);
        point.setX(10);
        point.setY(8);
        pointDaoJpaImpl.save(point);
    }
    
    @Test
    @Transactional
    @Rollback(false)
    @VerifyDataSet("/datasets/point-deleted-dataset.xml")
    public void testDelete() {
        Point point = pointDaoJpaImpl.findById(300L);
        pointDaoJpaImpl.delete(point);
    }
}
