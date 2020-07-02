package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    //Var logger 
    //Var GraphEditor
    //Vars...

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

        ObservableList<String> strList = FXCollections.observableArrayList("Edit history:");
        ListView<String> loggerTable = new ListView<String>(strList);
        loggerTable.setPrefWidth(200);
        loggerTable.setPrefHeight(580);
        loggerTable.setLayoutX(1000.0);
        loggerTable.setLayoutY(140.0);
        loggerTable.setBorder(defaultBorder);

        HBox graphEditorBox = new HBox();
        graphEditorBox.setBorder(defaultBorder);
        graphEditorBox.setLayoutX(50);
        graphEditorBox.setLayoutY(120);
        graphEditorBox.setPrefHeight(600);
        graphEditorBox.setPrefWidth(900);

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
                strList.clear();
                strList.add("Graph cleared");
            }
        });

        onWatchModeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(onWatchModeButton.getText() == "Watch"){
                    String str = "Algorithm Logger:";
                    strList.clear();
                    strList.add("Algorithm steps:");
                    loggerLabel.setText(str);
                    fileEditButton.setDisable(true);
                    clearGraphButton.setDisable(true);
                    onWatchModeButton.setText("Back to edit");
                    makeAlgStepButton.setVisible(true);
                    runFullAlgButton.setVisible(true);
                    reRunAlgButton.setVisible(true);
                }
                else{
                    loggerLabel.setText("Edit Logger:");
                    strList.clear();
                    strList.add("Edit history:");
                    fileEditButton.setDisable(false);
                    clearGraphButton.setDisable(false);
                    onWatchModeButton.setText("Watch");
                    makeAlgStepButton.setVisible(false);
                    runFullAlgButton.setVisible(false);
                    reRunAlgButton.setVisible(false);
                }
            }
        });

        makeAlgStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                strList.add("Making step...");
            }
        });

        runFullAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                strList.add("Run full");
            }
        });

        reRunAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                strList.add("start from the beginning");
            }
        });

        Group root = new Group(loggerLabel, fileEditButton, clearGraphButton, onWatchModeButton,
                makeAlgStepButton, runFullAlgButton, reRunAlgButton, graphEditorBox, loggerTable);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Everlasting summer practice");
        //scene.setFill(Color.GRAY);
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