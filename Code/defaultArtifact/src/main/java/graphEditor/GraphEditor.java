package graphEditor;

import java.util.ArrayList;
import java.util.List;

import graph.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;

enum EdgeDrawingSTates {DRAW_EDGE, NOT_DRAW_EDGE}

class EdgeVisual extends Group {
    QuadCurve line;
    TextField textWeigth;

    protected Button source;
    protected Button finish;

    public EdgeVisual(Button start, Button end) {
        source = start;
        finish = end;

        line = new QuadCurve();
        textWeigth = new TextField("-1");

        line.setControlX(0.0f);
        line.setControlY(0.0f);

        line.setStartX(source.getLayoutX() + source.getBoundsInParent().getWidth() / 2.0);
        line.setStartY(source.getLayoutY() + source.getBoundsInParent().getHeight() / 2.0);
    
        line.setEndX(finish.getLayoutX() + finish.getBoundsInParent().getWidth() / 2.0);
        line.setEndY(finish.getLayoutY() + finish.getBoundsInParent().getHeight() / 2.0);

        line.setViewOrder(1);

        setWeigthTextPosition();

        getChildren().add(line);
        getChildren().add(textWeigth);
    }

    private void setWeigthTextPosition() {
        double textStartX = Math.min(line.getStartX(), line.getEndX());
        double textStartY = Math.min(line.getStartY(), line.getEndY());
        double textX = Math.abs(line.getStartX() - line.getEndX()) / 2.0 + textStartX;
        double textY = Math.abs(line.getStartY() - line.getEndY()) / 2.0 + textStartY;

        textWeigth.setMaxWidth(35);
        textWeigth.setLayoutX(textX);
        textWeigth.setLayoutY(textY);
    }
}


class NodeVisual extends Button {
    private ArrayList<EdgeVisual> edgeRefs;

    public NodeVisual(String name) {
        super(name);
        edgeRefs = new ArrayList<EdgeVisual>();
        setShape(new Circle(15));
        setMaxSize(30, 30);
        setMinSize(30, 30);
    }

    public void setEdge(EdgeVisual edge) {
        edgeRefs.add(edge);
    }

    public ArrayList<EdgeVisual> getEdges() {
        return edgeRefs;
    }
}
/**Class for editing and bulding graph */
public class GraphEditor implements IGraphEditor {
    private IGraph graph;
    
    private Canvas canvas;
    private GraphicsContext context;
    private Pane parentBox;

    private EdgeDrawingSTates edgeState;

    private NodeVisual edgeStart;
    private NodeVisual edgeEnd;

    private int graphNodesCount;

    public GraphEditor(Canvas canvas) {
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
        parentBox = (Pane)canvas.getParent();
        graphNodesCount = 0;
        edgeState = EdgeDrawingSTates.NOT_DRAW_EDGE;

        this.canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                context.setFill(Color.RED);
                NodeVisual graphNode = createGraphNodeButton();
                graphNode.setLayoutX(event.getX());
                graphNode.setLayoutY(event.getY());
                parentBox.getChildren().add(graphNode);
            }
        });
    }

    private NodeVisual createGraphNodeButton() {
        String buttonName = "" + (char)('A' + graphNodesCount);
        NodeVisual graphNode = new NodeVisual(buttonName);
        graphNodesCount++;
        

        graphNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (edgeState == EdgeDrawingSTates.NOT_DRAW_EDGE) {
                    edgeState = EdgeDrawingSTates.DRAW_EDGE;
                    edgeDrawBegin(graphNode);
                } else {
                    edgeState = EdgeDrawingSTates.NOT_DRAW_EDGE;
                    edgeDrawEnd(graphNode);
                }
            }
        });

        graphNode.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.SECONDARY) == 0) {
                    System.out.println("Delete vertex: " + graphNode.getText());
                    ArrayList<EdgeVisual> edgesToRemove = graphNode.getEdges();
                    for (EdgeVisual edge : edgesToRemove) {
                        parentBox.getChildren().remove(edge);
                    }
                    parentBox.getChildren().remove(graphNode);
                }
            }
        });

        return graphNode;
    }

    private void edgeDrawBegin(NodeVisual clickedButton) {
        edgeStart = clickedButton;
    }

    private void edgeDrawEnd(NodeVisual clickedButton) {
        edgeEnd = clickedButton;

        
        EdgeVisual edgeVis = new EdgeVisual(edgeStart, edgeEnd);
        
        edgeEnd.setEdge(edgeVis);
        edgeStart.setEdge(edgeVis);

        parentBox.getChildren().add(edgeVis);
    }

    @Override
    public void addVertex(Vertex v) {

    }

    @Override
    public void addEdge(Edge e) {

    }

    @Override 
    public IGraph getGraph() {
        return graph;
    }
}