package graph;

/**class for storing information about vertex of the graph */
public class Vertex implements Comparable<Vertex> {
    Graph h;
    public int number;
    public int distance;
    public boolean isStart;
    public boolean isCheck;
    public String name;
    public Vertex(){
        number = -1;
        distance = 0;
        isStart = false;
        isCheck = false;

    }

    public Vertex(String name){
        this();
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    @Override
    public Vertex clone() throws CloneNotSupportedException {
        Vertex v = new Vertex();
        v.setNumber(number);
        return v;
    }

    @Override
    public int compareTo(Vertex o) {
        return this.number - o.number;
    }
}