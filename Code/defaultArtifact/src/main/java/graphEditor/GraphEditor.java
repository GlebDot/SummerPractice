package graphEditor;

import java.util.ArrayList;

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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.*;
import logger.AlgorithmMessage;
import logger.ILogger;
import logger.Logger;


enum EdgeDrawingStates {DRAW_EDGE, NOT_DRAW_EDGE}

class EdgeVisual extends Group {
    Path line;
    Spinner<Integer> textWeigth;
    Text edgeWeigthLabel;

    protected Button source;
    protected Button finish;
    protected Edge edgeRef;

    private Border selectBorder;

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

        selectBorder = new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID,
        new CornerRadii(1), new BorderWidths(2)));

        line = new Path();
        edgeWeigthLabel = new Text("0");
        edgeWeigthLabel.setFont(Font.font("Colibri", FontWeight.SEMI_BOLD, 14));
        //edgeWeigthLabel.setPrefSize(50, 50);
        edgeWeigthLabel.setVisible(false);
        line.setStrokeWidth(3);
        textWeigth = new Spinner<Integer>();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(-50, 50, 0);
        textWeigth.setValueFactory(valueFactory);


        line.setStroke(Color.BLACK);
        line.setFill(Color.TRANSPARENT);

        line.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                toFront();
                if (!textWeigth.isDisable()){
                    line.setStroke(Color.YELLOW);
                }
                textWeigth.setBorder(selectBorder);
                edgeWeigthLabel.setLayoutX(event.getX());
                edgeWeigthLabel.setLayoutY(event.getY() - 14);

                edgeWeigthLabel.setVisible(true);
            }
        });

        line.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (!textWeigth.isDisable()){
                    line.setStroke(Color.BLACK);
                }
                textWeigth.setBorder(Border.EMPTY);
                edgeWeigthLabel.setVisible(false);
            }
        });

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
                edgeWeigthLabel.setText(newValue.toString());
            }
        });

        setWeigthTextPosition();

        textWeigth.toFront();
        line.toBack();


        getChildren().add(edgeWeigthLabel);
        getChildren().add(line);
        getChildren().add(textWeigth);
    }

    private void drawLoop() {
        double startX = source.getLayoutX() +  source.getMaxWidth() / 2.0;;
        double startY = source.getLayoutY();

        double endX = source.getLayoutX();
        double endY = source.getLayoutY() + source.getMaxHeight() / 2.0;;

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
        double startX = source.getLayoutX() + source.getMaxWidth() / 2.0;
        double startY = source.getLayoutY() + source.getMaxHeight() / 2.0;

        double endX = finish.getLayoutX() + finish.getMaxWidth() / 2.0;
        double endY = finish.getLayoutY() + finish.getMaxHeight() / 2.0;        

        double angle = Math.atan2((endY - startY), (endX - startX)); 
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        startX += (line.getStrokeWidth() + source.getMaxWidth()) / 2.0 * cos;
        startY += (line.getStrokeWidth() + source.getMaxWidth()) / 2.0 * sin;

        endX -= (line.getStrokeWidth() * 2 + source.getMaxWidth() / 2.0) * cos;
        endY -= (line.getStrokeWidth() * 2 + source.getMaxWidth()) / 2.0 * sin;

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

            double angle = Math.atan2((finish.getLayoutY() - source.getLayoutY()), 
            (finish.getLayoutX() - source.getLayoutX()));

            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double textStartX = source.getLayoutX();
            double textStartY = source.getLayoutY();
    
            textX = Math.abs(source.getLayoutX() - finish.getLayoutX()) * 2.0 / 5.0 * cos + textStartX;
            textY = Math.abs(source.getLayoutY() - finish.getLayoutY()) * 2.0 / 5.0 * sin + textStartY;
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

    public void setNewName(String name) {
        setText(name);
        nodeRef.name = name;
    }

    public Label getLabelRef() {
        return labelRef;
    }

    public void setNewLabelValue(String info) {
        labelRef.setTextFill(Color.FIREBRICK);
        
        if (info.equals(labelRef.getText())) {
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            final KeyValue kv = new KeyValue(labelRef.textFillProperty(), Color.BLACK, Interpolator.EASE_BOTH);
            final KeyFrame kf = new KeyFrame(Duration.millis(1500), kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
        } else {
            labelRef.setText(info);
        }
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
    private Border hightlightBorder;
    private Pane parentBox;

    private ILogger logger;
    private Button controlButtonRef;


    private ArrayList<NodeVisual> graphNodes;
    private ArrayList<Label> nodeLabels;
    private ArrayList<EdgeVisual> graphEdges;

    private EdgeDrawingStates edgeState;

    private NodeVisual edgeStart;
    private NodeVisual edgeEnd;

    private NodeVisual hightligthedNode;
    private EdgeVisual hightligthedEdge;

    private int graphNodesCount;

    public GraphEditor(Canvas canvas, Button b) {
        hightlightBorder = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID,
        new CornerRadii(1), new BorderWidths(3)));
        isEditing = true;
        this.canvas = canvas;
        context = canvas.getGraphicsContext2D();
        parentBox = (Pane)canvas.getParent();
        graphNodesCount = 0;
        edgeState = EdgeDrawingStates.NOT_DRAW_EDGE;
        graph = new Graph();

        nodeLabels = new ArrayList<Label>();
        graphNodes = new ArrayList<NodeVisual>();
        graphEdges = new ArrayList<EdgeVisual>();

        logger = Logger.getInstance();

        controlButtonRef = b;

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
                
                    logger.logEvent(new AlgorithmMessage("Created vertex: " + graphNode.getText()));
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
        startNode.setTextFill(Color.FIREBRICK);

        logger.logEvent(new AlgorithmMessage("New start vertex: " + startNode.getText()));
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
                    graph.deleteVertex(graphNode.getVertexRef());

                    logger.logEvent(new AlgorithmMessage("Deleted vertex: " + graphNode.getText()));
                    ArrayList<EdgeVisual> edgesToRemove = graphNode.getEdges();
                    for (EdgeVisual edge : edgesToRemove) {
                        parentBox.getChildren().remove(edge);
                        graphEdges.remove(edge);
                    }
                    if (graphEdges.isEmpty()) {
                        controlButtonRef.setDisable(true);
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
        logger.logEvent(new AlgorithmMessage("Created edge: " + start.getText() + " - " + 
        end.getText()));
    

        graph.addEdge(edge.getEdgeRef());

        graphEdges.add(edge);
        if (controlButtonRef.isDisable()) {
            controlButtonRef.setDisable(false);
        }
        //Delete edge event
        edge.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().compareTo(MouseButton.SECONDARY) == 0 && isEditing) {
                    logger.logEvent(new AlgorithmMessage("Deleted edge: " + edge.source.getText() + " - " + 
                    edge.finish.getText()));

                    graph.deleteEdge(edge.getEdgeRef());
                    parentBox.getChildren().remove(edge);
                    graphEdges.remove(edge);
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

        if (startNode != null) {
            startNode.setNewLabelValue("0");
        }


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

        if (hightligthedEdge != null) {
            hightligthedEdge.line.setStroke(Color.BLACK);
        }

        if (hightligthedNode != null) {
            hightligthedNode.setBorder(Border.EMPTY);
        }
    }

    private void setEndStartNodesForEdge(Edge e) {
        for (NodeVisual node : graphNodes) {
            if (e.start.name.equals(node.getVertexRef().name)) {
                edgeStart = node;
            }

            if (e.end.name.equals(node.getVertexRef().name)) {
                edgeEnd = node;
            }
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
        if (e == null) {
            if (hightligthedEdge != null) {
                hightligthedEdge.line.setStroke(Color.BLACK);
                hightligthedEdge = null;
            }

            return;
        }

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
        if (v == null) {
            if (hightligthedNode != null) {
                hightligthedNode.setBorder(Border.EMPTY);
                hightligthedNode.getLabelRef().setTextFill(Color.BLACK);
                hightligthedNode = null;
            }
            
            return;
        }


        if (hightligthedNode != null) {
            hightligthedNode.getLabelRef().setTextFill(Color.BLACK);
            hightligthedNode.setBorder(Border.EMPTY);
        }
        for (NodeVisual node : graphNodes) {
            if (v.name == node.getVertexRef().name) {
                hightligthedNode = node;
                node.setBorder(hightlightBorder);

                if (v.isCheck) {
                    hightligthedNode.setNewLabelValue(new Integer(v.distance).toString());
                }
                break;
            }
        }
    }

    @Override 
    public void clearEditor() {
        graph = new Graph();
        graphNodesCount = 0;
        edgeStart = null;
        edgeEnd = null;
        for (NodeVisual node : graphNodes) {
            parentBox.getChildren().remove(node);
        }

        for (EdgeVisual edge : graphEdges) {
            parentBox.getChildren().remove(edge);
        }

        graphNodes.clear();
        graphEdges.clear();
        controlButtonRef.setDisable(true);
    }

    @Override
    public void rerunEditor() {
        if (hightligthedEdge != null) {
            hightligthedEdge.line.setStroke(Color.BLACK);
        }

        if (hightligthedNode != null) {
            hightligthedNode.setBorder(Border.EMPTY);
        }

        for (NodeVisual node : graphNodes) {
            node.setNewLabelValue("inf");
        }

        if (startNode != null) {
            startNode.setNewLabelValue("0");
        }
    }

    @Override
    public void loadGraph(Graph graph) {
        double stepX = (parentBox.getWidth() - 150) / Math.ceil(graph.graph.size() * 1.0 / 2);
        double stepY = (parentBox.getHeight() - 150) / Math.ceil(graph.graph.size() * 1.0 / 2);

        double coordX = 50;
        double coordY = 50;

        int xOrder = 0;
        int yOrder = 0;

        for(Vertex vertex : graph.graph.keySet()) {
            NodeVisual graphNode = createGraphNodeButton();
            graphNode.setLayoutX(coordX);

            if (xOrder % 2 == 0) {
                graphNode.setLayoutY(coordY);
            } else {
                graphNode.setLayoutY(coordY + 50);
            }
            graphNode.setNewName(vertex.name);
            parentBox.getChildren().add(graphNode);
            graphNodes.add(graphNode);

            if (vertex.isStart) {
                setStartVertex(graphNode);
            }

            coordX += stepX;
            xOrder++;
            if (coordX > parentBox.getWidth() - stepX) {
                coordY += stepY;
                yOrder++;
                xOrder = 0;
                if (yOrder % 2 == 0) {
                    coordX = 75;
                } else {
                    coordX = 50;
                }
            }
        } 

        for (ArrayList<Edge> edges : graph.graph.values()) {
            for (Edge e : edges) {
                setEndStartNodesForEdge(e);
                EdgeVisual edgeVis = createEdge(edgeStart, edgeEnd);
                edgeEnd.setEdge(edgeVis);
                edgeStart.setEdge(edgeVis);
                parentBox.getChildren().add(edgeVis);
                
                edgeVis.textWeigth.getValueFactory().setValue(e.weight);
            }
        }
    }
}