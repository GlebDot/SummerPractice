package graph;

/**class for storing information about vertex of the graph */
public class Vertex {
    int number;

    void setNumber(int number){
        this.number = number;
    }

    @Override
    public Vertex clone() throws CloneNotSupportedException {
        Vertex v = new Vertex();
        v.setNumber(number);
        return v;
    }
}