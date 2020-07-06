package graphEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import graph.*;
import javafx.beans.value.ChangeListener;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.*;


enum EdgeDrawingStates {DRAW_EDGE, NOT_DRAW_EDGE}

class EdgeVisual extends Group {
    Path line;
    Spinner<Integer> textWeigth;

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

    public EdgeVisual(NodeVisual start, NodeVisual end, boolean isDoubleEdge) {
        source = start;
        finish = end;

        edgeRef = new Edge(0, start.getVertexRef(), end.getVertexRef());

        line = new Path();
        line.setStrokeWidth(3);
        textWeigth = new Spinner<Integer>();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(-50, 50, 0);
        textWeigth.setValueFactory(valueFactory);


        line.setStroke(Color.BLACK);
        line.setFill(Color.TRANSPARENT);

        if (source.getLayoutX() == finish.getLayoutX() && finish.getLayoutY() == source.getLayoutY()) {
            drawLoop();
        } else {
            drawEdge(isDoubleEdge);
        }

        textWeigth.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        textWeigth.setEditable(true);

        textWeigth.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> value, Integer oldValue, Integer newValue) {
                edgeRef.changeWeight(newValue);
            }
        });

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

        double sin = Math.sin(-1.7);
        double cos = Math.cos(-1.7);

        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        line.getElements().add(new LineTo(x1, y1));
        line.getElements().add(new MoveTo(endX - line.getStrokeWidth(), endY));
        line.getElements().add(new LineTo(x2, y2));
    }

    private void drawEdge(boolean isDouble) {
        double startX = source.getLayoutX() + source.getBoundsInParent().getWidth() / 2.0;
        double startY = source.getLayoutY() + source.getBoundsInParent().getHeight() / 2.0;

        double endX = finish.getLayoutX() + finish.getBoundsInParent().getWidth() / 2.0;
        double endY = finish.getLayoutY() + finish.getBoundsInParent().getHeight() / 2.0;        

        double angle = Math.atan2((endY - startY), (endX - startX)); 
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        startX += (line.getStrokeWidth() + source.getBoundsInParent().getWidth()) / 2.0 * cos;
        startY += (line.getStrokeWidth() + source.getBoundsInParent().getWidth()) / 2.0 * sin;

        endX -= (line.getStrokeWidth() * 2 + source.getBoundsInParent().getWidth() / 2.0) * cos;
        endY -= (line.getStrokeWidth() * 2 + source.getBoundsInParent().getWidth()) / 2.0 * sin;

        angle -= Math.PI / 2;
        sin = Math.sin(angle);
        cos = Math.cos(angle);

        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 15.0 + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 15.0 + endY;

        line.getElements().add(new MoveTo(startX, startY));
        if (!isDouble) {
            line.getElements().add(new LineTo(endX, endY));
        } else {
            line.getElements().add(new MoveTo(endX, endY));
        }

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
                textOffset = 50;
            } else {
                textOffset = -50;
            }
            double textStartX = Math.min(source.getLayoutX(), finish.getLayoutX());
            double textStartY = Math.min(source.getLayoutY(), finish.getLayoutY());
    
            textX = Math.abs(source.getLayoutX() - finish.getLayoutX()) / 2.0 + textStartX + textOffset;
            textY = Math.abs(source.getLayoutY() - finish.getLayoutY()) / 2.0 + textStartY + textOffset;
        }


        textWeigth.setMaxWidth(50);
        textWeigth.setLayoutX(textX);
        textWeigth.setLayoutY(textY);
    }
}


class NodeVisual extends Button {
    private ArrayList<EdgeVisual> edgeRefs;

    private Vertex nodeRef;
    private Label labelRef;

    public Vertex getVertexRef() {
        return nodeRef;
    }

    public void setLabelRef(Label label) {
        labelRef = label;
    }

    public void setNewLabelValue(String info) {
        labelRef.setText(info);
        labelRef.setTextFill(Color.FIREBRICK);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        final KeyValue kv = new KeyValue(labelRef.textFillProperty(), Color.BLACK, Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(1500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public NodeVisual(String name) {
        super(name);
        edgeRefs = new ArrayList<EdgeVisual>();
        setShape(new Circle(15));
        setMaxSize(30, 30);
        setMinSize(30, 30);

        nodeRef = new Vertex(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Vertex) {
            Vertex obj = (Vertex)o;
            return nodeRef.equals(obj);
        }

        return false;
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

    private NodeVisual startNode;
    
    private Canvas canvas;
    private GraphicsContext context;
    private Pane parentBox;


    private ArrayList<NodeVisual> graphNodes;
    private ArrayList<Label> nodeLabels;
    private ArrayList<EdgeVisual> graphEdges;

    private EdgeDrawingStates edgeState;

    private NodeVisual edgeStart;
    private NodeVisual edgeEnd;

    private NodeVisual hightligthedNode;
    private EdgeVisual hightligthedEdge;

    private int graphNodesCount;

    public GraphEditor(Canvas canvas) {
        isEditing = true;
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
        parentBox = (Pane)canvas.getParent();
        graphNodesCount = 0;
        edgeState = EdgeDrawingStates.NOT_DRAW_EDGE;
        graph = new Graph();
        graphNodes = new ArrayList<NodeVisual>();

        nodeLabels = new ArrayList<Label>();
        graphNodes = new ArrayList<NodeVisual>();
        graphEdges = new ArrayList<EdgeVisual>();
        //add Vertex
        this.canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.PRIMARY) == 0 && isEditing) {
                    NodeVisual graphNode = createGraphNodeButton();
                    graphNode.setLayoutX(event.getX());
                    graphNode.setLayoutY(event.getY());
                    parentBox.getChildren().add(graphNode);
                    graphNodes.add(graphNode);
                    edgeState = EdgeDrawingStates.NOT_DRAW_EDGE;

                    if (graphNodes.size() == 1) {
                        setStartVertex(graphNode);
                    }
                }
            }
        });
    }

    private void setStartVertex(NodeVisual node) {
        if (startNode != null) {
            startNode.setTextFill(Color.BLACK);;
        }

        startNode = node;
        graph.setStartVertex(startNode.getVertexRef());
        startNode.setTextFill(Color.FIREBRICK);;
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
                        graphEdges.remove(edge);
                    }
                    graphNodes.remove(graphNode);
                    parentBox.getChildren().remove(graphNode);
                    if (graphNode.equals(startNode) && !graphNodes.isEmpty()) {
                        setStartVertex(graphNodes.get(0));
                    }
                }
            }
        });

        graphNode.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.MIDDLE) == 0 && isEditing) {
                    setStartVertex(graphNode);
                }
            }
        });

        return graphNode;
    }

    private EdgeVisual createEdge(NodeVisual start, NodeVisual end) {
        EdgeVisual tryEdge = new EdgeVisual(end, start, true);
        EdgeVisual edge;
        if (graphEdges.contains(tryEdge)) {
            edge = new EdgeVisual(start, end, true);
        } else {
            edge = new EdgeVisual(start, end, false);
        }
        

        graph.addEdge(edge.getEdgeRef());

        graphEdges.add(edge);
        //Delete edge event
        edge.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.SECONDARY) == 0 && isEditing) {
                    graph.deleteEdge(edge.getEdgeRef());
                    parentBox.getChildren().remove(edge);
                    graphEdges.remove(edge);
                }
            }
        });

        return edge;
    }

    private void setStartNode(NodeVisual node) {
        if (startNode != null) {
            startNode.setTextFill(Color.BLACK);
        }

        startNode = node;
        startNode.setTextFill(Color.FIREBRICK);
        graph.setStartVertex(startNode.getVertexRef());
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
        }
    }

    private void prepareGraphEditor() {
        for (NodeVisual node : graphNodes) {

            Label nodeLabel = new Label();
            nodeLabel.setLayoutY(node.getLayoutY() + node.getMinHeight());
            nodeLabel.setLayoutX(node.getLayoutX());

            nodeLabel.setText("inf");

            parentBox.getChildren().add(nodeLabel);
            nodeLabels.add(nodeLabel);
            node.setLabelRef(nodeLabel);
        }

        startNode.setNewLabelValue("0");

        for (EdgeVisual edge : graphEdges) {
            edge.textWeigth.setDisable(true);
        }
    }

    private void clearGraphEditorLabels() {
        for (Label label : nodeLabels) {
            parentBox.getChildren().remove(label);
        }

        for (EdgeVisual edge : graphEdges) {
            edge.line.setStroke(Color.BLACK);
        }

        nodeLabels.clear();

        for (EdgeVisual edge : graphEdges) {
            edge.textWeigth.setDisable(false);
        }
    }

    @Override
    public void setEditState(boolean isEditState) {
        isEditing = isEditState;
        if (!isEditing) {
            prepareGraphEditor();
        } else {
            clearGraphEditorLabels();
        }
    }

    @Override 
    public IGraph getGraph() {
        return graph;
    }


    @Override
    public void setCurrentEdge(Edge e) {
        System.out.println("It's me Pario");
        if (hightligthedEdge != null) {
            hightligthedEdge.line.setStroke(Color.BLACK);
        }
        for (EdgeVisual edgeVis : graphEdges) {
            if (e.start.name == edgeVis.edgeRef.start.name && e.end.name == edgeVis.edgeRef.end.name) {
                hightligthedEdge = edgeVis;
                edgeVis.line.setStroke(Color.RED);
                break;
            }
        }
    }

    @Override
    public void setCurrentVertex(Vertex v) {
        if (hightligthedNode != null) {
            hightligthedNode.setTextFill(Color.BLACK);
        }
        System.out.println("It's me Pario");
        for (NodeVisual node : graphNodes) {
            if (v.name == node.getVertexRef().name) {
                hightligthedNode = node;
                node.setTextFill(Color.GREEN);

                if (v.isCheck) {
                    hightligthedNode.setNewLabelValue(new Integer(v.distance).toString());
                }
                break;
            }
        }
    }

    @Override 
    public void clearEditor() {
        for (NodeVisual node : graphNodes) {
            graph.deleteVertex(node.getVertexRef());
            parentBox.getChildren().remove(node);
        }

        for (EdgeVisual edge : graphEdges) {
            parentBox.getChildren().remove(edge);
        }

        graphNodes.clear();
        graphEdges.clear();
    }

    @Override
    public void rerunEditor() {
        if (hightligthedEdge != null) {
            hightligthedEdge.line.setStroke(Color.BLACK);
        }

        if (hightligthedNode != null) {
            hightligthedNode.setTextFill(Color.BLACK);
        }
    }

    @Override
    public void loadGraph(Graph graph) {

    }
}