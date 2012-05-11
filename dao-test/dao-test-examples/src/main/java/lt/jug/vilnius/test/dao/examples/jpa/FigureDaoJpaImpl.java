package lt.jug.vilnius.test.dao.examples.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lt.jug.vilnius.test.dao.examples.jpa.model.Figure;
import org.springframework.stereotype.Repository;

/**
 * Example of simple JPA DAO
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
@Repository
public class FigureDaoJpaImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Figure findById(Long figureId) {
        return entityManager.find(Figure.class, figureId);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Figure save(Figure figure) {
        return entityManager.merge(figure);
    }

    public void delete(Figure figure) {
        entityManager.remove(figure);
    }
}
