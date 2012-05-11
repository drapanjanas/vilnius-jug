package lt.jug.vilnius.test.dao.examples.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lt.jug.vilnius.test.dao.examples.jpa.model.Point;
import org.springframework.stereotype.Repository;

/**
 * Example of simple JPA DAO
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
@Repository
public class PointDaoJpaImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Point findById(Long pointId) {
        return entityManager.find(Point.class, pointId);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Point save(Point point) {
        return entityManager.merge(point);
    }

    public void delete(Point point) {
        entityManager.remove(point);
    }
}
