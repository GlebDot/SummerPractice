package graph;

/**class for storing information about vertex of the graph */
public class Vertex {
    Graph h;
    public int number;
    public int distance;
    public boolean isStart;
    public boolean isCheck;


    public Vertex(){
        number = -1;
        distance = 0;
        isStart = false;
        isCheck = false;

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

}