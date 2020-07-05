package graphEditor;

import java.util.ArrayList;

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
import javafx.scene.shape.*;

enum EdgeDrawingStates {DRAW_EDGE, NOT_DRAW_EDGE}

class EdgeVisual extends Group {
    Path line;
    TextField textWeigth;

    protected Button source;
    protected Button finish;
    protected Edge edgeRef;

    public Edge getEdgeRef() {
        return edgeRef;
    }

    @Override 
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (o instanceof EdgeVisual) {
            EdgeVisual edgeObj = (EdgeVisual)o;
            if (source.getLayoutX() == edgeObj.source.getLayoutX() && source.getLayoutY() 
            == edgeObj.source.getLayoutY() && finish.getLayoutX() == edgeObj.finish.getLayoutX() && 
            finish.getLayoutY() == edgeObj.finish.getLayoutY()) {
                return true;
            }
        }

        return false;
    }

    public EdgeVisual(NodeVisual start, NodeVisual end) {
        source = start;
        finish = end;

        edgeRef = new Edge(0, start.getVertexRef(), end.getVertexRef());

        line = new Path();
        textWeigth = new TextField("0");

        line.setStroke(Color.BLACK);
        line.setFill(Color.TRANSPARENT);

        if (source.getLayoutX() == finish.getLayoutX() && finish.getLayoutY() == source.getLayoutY()) {
            drawLoop();
        } else {
            drawEdge();
        }

        setWeigthTextPosition();

        getChildren().add(line);
        getChildren().add(textWeigth);
    }

    private void drawLoop() {
        double startX = source.getLayoutX() +  source.getBoundsInParent().getWidth() / 2.0;;
        double startY = source.getLayoutY();

        double endX = source.getLayoutX();
        double endY = source.getLayoutY() + source.getBoundsInParent().getHeight() / 2.0;;

        line.getElements().add(new MoveTo(startX, startY));

        line.getElements().add(new ArcTo(25, 25, 0, endX, endY, true, false));

        double sin = Math.sin(-1.5);
        double cos = Math.cos(-1.5);

        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        line.getElements().add(new LineTo(x1, y1));
        line.getElements().add(new MoveTo(endX, endY));
        line.getElements().add(new LineTo(x2, y2));
    }

    private void drawEdge() {
        double startX = source.getLayoutX() + source.getBoundsInParent().getWidth() / 2.0;
        double startY = source.getLayoutY() + source.getBoundsInParent().getHeight() / 2.0;

        double endX = finish.getLayoutX() + finish.getBoundsInParent().getWidth() / 2.0;
        double endY = finish.getLayoutY() + finish.getBoundsInParent().getHeight() / 2.0;        

        double angle = Math.atan2((endY - startY), (endX - startX)); 
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        startX += source.getBoundsInParent().getWidth() / 2.0 * cos;
        startY += source.getBoundsInParent().getWidth() / 2.0 * sin;

        endX -= source.getBoundsInParent().getWidth() / 2.0 * cos;
        endY -= source.getBoundsInParent().getWidth() / 2.0 * sin;

        angle -= Math.PI / 2;
        sin = Math.sin(angle);
        cos = Math.cos(angle);

        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        line.getElements().add(new MoveTo(startX, startY));
        line.getElements().add(new LineTo(endX, endY));

        line.getElements().add(new LineTo(x1, y1));
        line.getElements().add(new MoveTo(endX, endY));
        line.getElements().add(new LineTo(x2, y2));
    }

    private void setWeigthTextPosition() {
        double textX = 0.0;
        double textY = 0.0;
        if (source.getLayoutX() == finish.getLayoutX() && source.getLayoutY() == finish.getLayoutY()) {

            textX = source.getLayoutX() - 60;
            textY = source.getLayoutY() - 60;

        } else {
            double textOffset = 0.0;
            if (source.getLayoutY() > finish.getLayoutY()) {
                textOffset = 20;
            } else {
                textOffset = -20;
            }
            double textStartX = Math.min(source.getLayoutX(), finish.getLayoutX());
            double textStartY = Math.min(source.getLayoutY(), finish.getLayoutY());
    
            textX = Math.abs(source.getLayoutX() - finish.getLayoutX()) / 2.0 + textStartX + textOffset;
            textY = Math.abs(source.getLayoutY() - finish.getLayoutY()) / 2.0 + textStartY + textOffset;
        }

        textWeigth.setMaxWidth(35);
        textWeigth.setLayoutX(textX);
        textWeigth.setLayoutY(textY);
    }
}


class NodeVisual extends Button {
    private ArrayList<EdgeVisual> edgeRefs;

    private Vertex nodeRef;

    public Vertex getVertexRef() {
        return nodeRef;
    }

    public NodeVisual(String name) {
        super(name);
        edgeRefs = new ArrayList<EdgeVisual>();
        setShape(new Circle(15));
        setMaxSize(30, 30);
        setMinSize(30, 30);

        nodeRef = new Vertex(name);
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
    private boolean isEditing;
    
    private Canvas canvas;
    private GraphicsContext context;
    private Pane parentBox;

    private EdgeDrawingStates edgeState;

    private NodeVisual edgeStart;
    private NodeVisual edgeEnd;

    private int graphNodesCount;

    public GraphEditor(Canvas canvas) {
        isEditing = true;
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
        parentBox = (Pane)canvas.getParent();
        graphNodesCount = 0;
        edgeState = EdgeDrawingStates.NOT_DRAW_EDGE;
        graph = new Graph();

        this.canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.PRIMARY) == 0 && isEditing) {
                    NodeVisual graphNode = createGraphNodeButton();
                    graphNode.setLayoutX(event.getX());
                    graphNode.setLayoutY(event.getY());
                    parentBox.getChildren().add(graphNode);
                    edgeState = EdgeDrawingStates.NOT_DRAW_EDGE;
                }
            }
        });
    }

    private NodeVisual createGraphNodeButton() {
        String buttonName = "" + (char)('A' + graphNodesCount);
        NodeVisual graphNode = new NodeVisual(buttonName);
        graphNodesCount++;

        graph.addVertex(graphNode.getVertexRef());
        

        graphNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEditing) {
                    if (edgeState == EdgeDrawingStates.NOT_DRAW_EDGE) {
                        edgeState = EdgeDrawingStates.DRAW_EDGE;
                        edgeDrawBegin(graphNode);
                    } else {
                        edgeState = EdgeDrawingStates.NOT_DRAW_EDGE;
                        edgeDrawEnd(graphNode);
                    }
                }
            }
        });

        //delete vertex event
        graphNode.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.SECONDARY) == 0 && isEditing) {
                    System.out.println("Delete vertex: " + graphNode.getText());
                    graph.deleteVertex(graphNode.getVertexRef());

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

    private EdgeVisual createEdge(NodeVisual start, NodeVisual end) {
        EdgeVisual edge = new EdgeVisual(start, end);

        graph.addEdge(edge.getEdgeRef());

        //Delete edge event
        edge.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.SECONDARY) == 0 && isEditing) {
                    graph.deleteEdge(edge.getEdgeRef());
                    parentBox.getChildren().remove(edge);
                }
            }
        });

        return edge;
    }

    private void edgeDrawBegin(NodeVisual clickedButton) {
        edgeStart = clickedButton;
    }

    private void edgeDrawEnd(NodeVisual clickedButton) {
        edgeEnd = clickedButton;

        
        EdgeVisual edgeVis = createEdge(edgeStart, edgeEnd);

        if (!parentBox.getChildren().contains(edgeVis)) {
            edgeEnd.setEdge(edgeVis);
            edgeStart.setEdge(edgeVis);

            parentBox.getChildren().add(edgeVis);
            System.out.println("Add edge");
        }
    }

    @Override
    public void setState(boolean isEditState) {
        isEditing = isEditState;
    }

    @Override 
    public IGraph getGraph() {
        return graph;
    }
}