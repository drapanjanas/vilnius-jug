package lt.jug.vilnius.test.dao.examples.jdbc;

import java.util.Set;

/**
 * Operations with database.
 *
 * @author arturas
 */
public interface GeometryDao {

    /**
     * Creates new Figure.
     *
     * @return Figure Id
     */
    public Long createFigure(String name);

    /**
     * Adds point to figure.
     *
     * @param figureId - identifies figure
     * @param vertex - Point to add as vertex
     */
    public void addVertex(Long figureId, Point vertex);

    /**
     * Removes vertex from figure.
     *
     * @param figureId - identifies figure
     * @param vertex - Point to add as vertex
     */
    public void removeVertex(Long figureId, Point vertex);

    /**
     * Returns vertexes of a figure.
     *
     * @param figureId - identifies the figure
     */
    Set<Point> getVertexes(Long figureId);

    /**
     * Saves new point in db if it does not exist.
     *
     * @param point
     */
    Point findPoint(double x, double y);
    
    /**
     * Saves new point in db if it does not exist.
     *
     * @param point
     */
    void savePoint(Point point);
}
