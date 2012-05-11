package lt.jug.vilnius.test.dao.examples.jpa.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 * Geometric figure
 *
 * @author Arturas Girenko a.girenko@gmail.com
 */
@Entity
@Table(name = "figures")
@SequenceGenerator(name = "figureIdGenerator", sequenceName = "FIGURE_ID_SEQ")
public class Figure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "figureIdGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany
    @JoinTable(name = "vertexes", joinColumns = {
        @JoinColumn(name = "figure_id")}, inverseJoinColumns = {
        @JoinColumn(name = "point_id")})
    private Set<Point> vertexes;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Point> getVertexes() {
        return vertexes;
    }

    public void setVertexes(Set<Point> vertexes) {
        this.vertexes = vertexes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Figure other = (Figure) obj;
        if (this.vertexes != other.vertexes && (this.vertexes == null || !this.vertexes.equals(other.vertexes))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.vertexes != null ? this.vertexes.hashCode() : 0);
        return hash;
    }
}
