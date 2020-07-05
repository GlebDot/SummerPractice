package app;

import graphEditor.GraphEditor;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import algorithm.*;
import graphEditor.*;
import graph.*;
import logger.AlgorithmMessage;
import logger.Logger;



/**
 * JavaFX App
 */
public class App extends Application {

    private Logger logger;
    private IAlgorithm algorithmSolver;

    //Var GraphEditor
    //Vars...


    private IGraphEditor graphEditor;
    public App(){
        logger = Logger.getInstance(FXCollections.observableArrayList("Edit history:"));
        algorithmSolver = new Algorithm();
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
        reRunAlgButton.setVisible(false);
        makeAlgStepButton.setVisible(false);
        runFullAlgButton.setVisible(false);

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
        
        graphEditor = new GraphEditor(canvas);
        //handlers
        fileEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.showOpenDialog(stage);
            }
        });

        clearGraphButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.clear();
                logger.logEvent("Graph cleared");
            }
        });

        onWatchModeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(onWatchModeButton.getText() == "Watch"){
                    logger.clear();
                    logger.logEvent("Algorithm steps:");
                    loggerLabel.setText("Algorithm Logger:");
                    fileEditButton.setDisable(true);
                    clearGraphButton.setDisable(true);
                    onWatchModeButton.setText("Back to edit");
                    makeAlgStepButton.setVisible(true);
                    runFullAlgButton.setVisible(true);
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
                    reRunAlgButton.setVisible(false);

                    graphEditor.setEditState(true);
                }
            }
        });

        makeAlgStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AlgorithmMessage mes = algorithmSolver.stepForward();
                logger.logEvent(mes.getMessage());
            }
        });

        runFullAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.logEvent("Run full");
            }
        });

        reRunAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logger.logEvent("start from the beginning");
            }
        });

        Group root = new Group(loggerLabel, fileEditButton, clearGraphButton, onWatchModeButton,
                makeAlgStepButton, runFullAlgButton, reRunAlgButton, graphEditorBox, loggerTable);
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