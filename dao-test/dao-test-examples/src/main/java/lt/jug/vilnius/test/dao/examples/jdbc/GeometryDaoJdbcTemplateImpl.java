package lt.jug.vilnius.test.dao.examples.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Spring JdbcTemplate implementation of GeometryDao
 *
 * @author arturas
 */
@Repository
public class GeometryDaoJdbcTemplateImpl implements GeometryDao {

    @Autowired
    private JdbcOperations jdbcTemplate;

    public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Long createFigure(String name) {
        Long figureId = nextFigureId();
        jdbcTemplate.update("insert into figures (id, name) values (?, ?)", new Object[]{figureId, name});
        return figureId;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addVertex(Long figureId, Point vertex) {
        Long pointId = getPointId(vertex);
        if (pointId == null) {
            throw new IllegalArgumentException("Provided point was not found");
        }
        jdbcTemplate.update("insert into vertexes (figure_id, point_id) values (?, ?)",
                new Object[]{figureId, pointId});
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeVertex(Long figureId, Point vertex) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Set<Point> getVertexes(Long figureId) {
        List<Point> pointList = jdbcTemplate.query("select p.x, p.y from vertexes v "
                + "inner join points p on p.id = v.point_id "
                + "where v.figure_id = ?", new Object[]{figureId}, new PointRowMapper());
        return new HashSet<Point>(pointList);
    }

    @Override
    public Point findPoint(double x, double y) {
        List<Point> pointList = jdbcTemplate.query("select x, y from points p "
                + "where x = ? and y = ?", new Object[]{x, y}, new PointRowMapper());
        return pointList.size() > 0 ? pointList.get(0) : null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void savePoint(Point point) {
    }

    private Long nextFigureId() throws DataAccessException {
        Long figureId = jdbcTemplate.query("select FIGURE_ID_SEQ.NEXTVAL from dual",
                new ResultSetExtractor<Long>() {

                    @Override
                    public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                        rs.next();
                        return rs.getLong(1);
                    }
                });
        return figureId;
    }
    
    private Long getPointId(Point vertex) {
        Long pointId = jdbcTemplate.query("select id from points where x = ? and y = ?",
                new Object[]{vertex.getX(), vertex.getY()},
                new ResultSetExtractor<Long>() {

                    @Override
                    public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return rs.getLong(1);
                        } else {
                            return null;
                        }
                    }
                });
        return pointId;
    }

    /**
     * Creates Point from ResultSet
     */
    private static final class PointRowMapper implements RowMapper<Point> {

        @Override
        public Point mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Point(rs.getDouble("x"), rs.getDouble("y"));
        }
    }
}
