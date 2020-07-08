package app;

import graphEditor.GraphEditor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import algorithm.*;
import fileReader.fileReader;
import graphEditor.*;
import graph.*;
import javafx.util.Duration;
import logger.AlgorithmMessage;
import logger.Logger;



/**
 * JavaFX App
 */
public class App extends Application {

    private Logger logger;
    private IAlgorithm algorithmSolver;
    private fileReader reader;
    private Timeline timeline;
    private IGraphEditor graphEditor;

    public App(){
        logger = Logger.getInstance();
        algorithmSolver = new Algorithm();
        timeline = null;
    }


    @Override
    public void start(Stage stage) {
        Border defaultBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
        new CornerRadii(1), new BorderWidths(2)));

        Button fileEditButton = setButtonConfiguration(170.0, 50.0, 100.0, 50.0, "File input");
        Button clearGraphButton = setButtonConfiguration(290.0, 50.0, 100.0, 50.0, "Delete graph");
        Button onWatchModeButton = setButtonConfiguration(50.0, 50.0, 100.0, 50.0, "Watch");
        Button makeAlgStepButton = setButtonConfiguration(610.0, 65.0, 70.0, 35.0, "1 Step");
        Button runFullAlgButton = setButtonConfiguration(700.0, 65.0, 70.0, 35.0, "Run full");
        Button reRunAlgButton = setButtonConfiguration(790.0, 65.0, 70.0, 35.0, "Re run");
        Button killRunButton = setButtonConfiguration(880.0, 65.0, 70.0, 35.0, "STOP");
        Button runOneCycleButton = setButtonConfiguration(500.0, 65.0, 90.0, 35.0, "Run 1 cycle");
        Button noAnimationsButton = setButtonConfiguration(410.0, 65.0, 70.0, 35.0, "1 cycle");

        noAnimationsButton.setVisible(false);
        runOneCycleButton.setVisible(false);
        killRunButton.setStyle("-fx-background-color: #ff0000; ");
        killRunButton.setVisible(false);
        reRunAlgButton.setVisible(false);
        makeAlgStepButton.setVisible(false);
        runFullAlgButton.setVisible(false);
        onWatchModeButton.setDisable(true);

        Label loggerLabel = new Label("Edit Logger:");
        loggerLabel.setPrefWidth(200);
        loggerLabel.setLayoutX(1000.0);
        loggerLabel.setLayoutY(120.0);
        loggerLabel.setBorder(defaultBorder);

        ListView<String> loggerTable = new ListView<String>(logger.strList);
        loggerTable.setPrefWidth(200);
        loggerTable.setPrefHeight(580);
        loggerTable.setLayoutX(1000.0);
        loggerTable.setLayoutY(140.0);
        loggerTable.setBorder(defaultBorder);
            loggerTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<String> stList = loggerTable.getItems();
                StringBuilder outStr = new StringBuilder(stList.toString());
                outStr.deleteCharAt(0);
                outStr.deleteCharAt(outStr.length() - 1);
                for(int i = 0; i < outStr.length(); i++){
                    if(outStr.charAt(i) == ','){
                        outStr.setCharAt(i, '\n');
                        outStr.setCharAt(i+1, '\n');
                    }
                }
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(outStr.toString());
                clipboard.setContent(content);
            }
        });

        Pane graphEditorBox = new Pane();
        graphEditorBox.setBorder(defaultBorder);
        graphEditorBox.setLayoutX(50);
        graphEditorBox.setLayoutY(120);
        graphEditorBox.setPrefHeight(600);
        graphEditorBox.setPrefWidth(900);

        Canvas canvas = new Canvas();
        canvas.setHeight(600);
        canvas.setWidth(900);
        
        graphEditorBox.getChildren().add(canvas);
        
        graphEditor = new GraphEditor(canvas, onWatchModeButton);
        //handlers
        fileEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    reader = new fileReader(file);
                    Graph g = reader.readFromFile();
                    if (g != null) {
                        graphEditor.clearEditor();
                        graphEditor.loadGraph(g);
                    }
                }
                
            }
        });

        clearGraphButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.clear();
                graphEditor.clearEditor();
                logger.logEvent("Graph cleared");
            }
        });

        onWatchModeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(onWatchModeButton.getText().equals("Watch")){
                    logger.clear();
                    logger.logEvent("Algorithm steps:");
                    loggerLabel.setText("Algorithm Logger:");
                    fileEditButton.setDisable(true);
                    noAnimationsButton.setVisible(true);
                    clearGraphButton.setDisable(true);
                    onWatchModeButton.setText("Back to edit");
                    makeAlgStepButton.setVisible(true);
                    runFullAlgButton.setVisible(true);
                    runOneCycleButton.setVisible(true);
                    reRunAlgButton.setVisible(true);

                    graphEditor.setEditState(false);
                    algorithmSolver.initAlgorithm((Graph)graphEditor.getGraph());
                }
                else{
                    loggerLabel.setText("Edit Logger:");
                    logger.clear();
                    logger.logEvent("Edit history:");
                    fileEditButton.setDisable(false);
                    clearGraphButton.setDisable(false);
                    onWatchModeButton.setText("Watch");
                    makeAlgStepButton.setVisible(false);
                    runFullAlgButton.setVisible(false);
                    runOneCycleButton.setVisible(false);
                    reRunAlgButton.setVisible(false);
                    noAnimationsButton.setVisible(false);
                    graphEditor.setEditState(true);
                }
            }
        });

        makeAlgStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AlgorithmMessage mes = algorithmSolver.stepForward();
                logger.logEvent(logger.prepare(mes.getMessage()));
                graphEditor.setCurrentEdge(mes.getViewingEdge());
                if (mes.getViewingEdge() != null) {
                    graphEditor.setCurrentVertex(mes.getViewingEdge().end);
                } else {
                    graphEditor.setCurrentVertex(null);
                }
            }
        });

        runFullAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                algorithmSolver.initAlgorithm((Graph)graphEditor.getGraph());
                graphEditor.rerunEditor();
                timeline = new Timeline();
                makeAlgStepButton.setDisable(true);
                runFullAlgButton.setDisable(true);
                runOneCycleButton.setDisable(true);
                reRunAlgButton.setDisable(true);
                onWatchModeButton.setDisable(true);
                killRunButton.setVisible(true);
                noAnimationsButton.setDisable(true);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        AlgorithmMessage mes = algorithmSolver.stepForward();
                        logger.logEvent(logger.prepare(mes.getMessage()));
                        graphEditor.setCurrentEdge(mes.getViewingEdge());
                        if (mes.getViewingEdge() != null) {
                            graphEditor.setCurrentVertex(mes.getViewingEdge().end);
                        } else {
                            graphEditor.setCurrentVertex(null);
                        }
                        if(mes.isFinish()){
                            makeAlgStepButton.setDisable(false);
                            runFullAlgButton.setDisable(false);
                            reRunAlgButton.setDisable(false);
                            runOneCycleButton.setDisable(false);
                            onWatchModeButton.setDisable(false);
                            killRunButton.setVisible(false);
                            noAnimationsButton.setDisable(false);
                            timeline.stop();
                        }
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });

        reRunAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.clear();
                algorithmSolver.initAlgorithm((Graph)graphEditor.getGraph());
                graphEditor.rerunEditor();
            }
        });

        killRunButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.stop();
                logger.logEvent("Algorithm execution paused\nContinue via \"1 step\"");
                makeAlgStepButton.setDisable(false);
                runFullAlgButton.setDisable(false);
                reRunAlgButton.setDisable(false);
                onWatchModeButton.setDisable(false);
                killRunButton.setVisible(false);
                runOneCycleButton.setDisable(false);
                noAnimationsButton.setDisable(false);
            }
        });

        runOneCycleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline = new Timeline();
                makeAlgStepButton.setDisable(true);
                runFullAlgButton.setDisable(true);
                runOneCycleButton.setDisable(true);
                reRunAlgButton.setDisable(true);
                onWatchModeButton.setDisable(true);
                killRunButton.setVisible(true);
                noAnimationsButton.setDisable(true);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        AlgorithmMessage mes = algorithmSolver.stepForward();
                        logger.logEvent(logger.prepare(mes.getMessage()));
                        graphEditor.setCurrentEdge(mes.getViewingEdge());
                        if (mes.getViewingEdge() != null) {
                            graphEditor.setCurrentVertex(mes.getViewingEdge().end);
                        } else {
                            graphEditor.setCurrentVertex(null);
                        }

                        if(mes.isEndOfCycle() || mes.getMessage().indexOf("The algorithm has already com") != -1){
                            makeAlgStepButton.setDisable(false);
                            runFullAlgButton.setDisable(false);
                            reRunAlgButton.setDisable(false);
                            runOneCycleButton.setDisable(false);
                            onWatchModeButton.setDisable(false);
                            killRunButton.setVisible(false);
                            noAnimationsButton.setDisable(false);
                            timeline.stop();
                        }
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });

        noAnimationsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                while(true){
                    AlgorithmMessage mes = algorithmSolver.stepForward();
                    logger.logEvent(logger.prepare(mes.getMessage()));
                    graphEditor.setCurrentEdge(mes.getViewingEdge());
                    if(mes.isEndOfCycle() || mes.getMessage().indexOf("The algorithm has already com") != -1){
                        return;
                    }
                }
            }
        });

        Group root = new Group(loggerLabel, fileEditButton, clearGraphButton, onWatchModeButton,
                makeAlgStepButton, runFullAlgButton, reRunAlgButton, graphEditorBox,
                loggerTable, killRunButton, runOneCycleButton, noAnimationsButton);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Everlasting summer practice");
        stage.setWidth(1250);
        stage.setHeight(800);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button setButtonConfiguration(double offsetX, double offsetY, double width, double height, String text){
        Border defaultBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(1), new BorderWidths(2)));
        Button button = new Button(text);
        button.setLayoutX(offsetX);
        button.setLayoutY(offsetY);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setBorder(defaultBorder);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}