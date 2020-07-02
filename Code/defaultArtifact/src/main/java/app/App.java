package app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        
        Border defaultBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
        new CornerRadii(1), new BorderWidths(2)));
        Button fileEditButton = new Button("File input"); //upload graph from file
        fileEditButton.setLayoutX(170.0);
        fileEditButton.setLayoutY(50.0);
        fileEditButton.setPrefWidth(100.0);
        fileEditButton.setPrefHeight(50.0);

        Button clearGraphButton = new Button("Delete graph");
        clearGraphButton.setLayoutX(290.0);
        clearGraphButton.setLayoutY(50.0);
        clearGraphButton.setPrefWidth(100.0);
        clearGraphButton.setPrefHeight(50.0);

        Button onWatchModeButton = new Button("Watch"); //change mode to watch (and back to edit)
        onWatchModeButton.setLayoutX(50.0);
        onWatchModeButton.setLayoutY(50.0);
        onWatchModeButton.setPrefWidth(100.0);
        onWatchModeButton.setPrefHeight(50.0);

        Button makeAlgStepButton = new Button("1 Step");
        makeAlgStepButton.setLayoutX(610.0);
        makeAlgStepButton.setLayoutY(65.0);
        makeAlgStepButton.setPrefWidth(70.0);
        makeAlgStepButton.setPrefHeight(35.0);
        makeAlgStepButton.setVisible(false);

        Button runFullAlgButton = new Button("Run full");
        runFullAlgButton.setLayoutX(700.0);
        runFullAlgButton.setLayoutY(65.0);
        runFullAlgButton.setPrefWidth(70.0);
        runFullAlgButton.setPrefHeight(35.0);
        runFullAlgButton.setVisible(false);

        Button reRunAlgButton = new Button("Re run"); //Firsov's button
        reRunAlgButton.setLayoutX(790.0);
        reRunAlgButton.setLayoutY(65.0);
        reRunAlgButton.setPrefWidth(70.0);
        reRunAlgButton.setPrefHeight(35.0);
        reRunAlgButton.setVisible(false);


        Label editLogger = new Label("Edit Logger:"); //in fact just loggerButton, label is temporary
        editLogger.setPrefWidth(200);
        editLogger.setLayoutX(1000.0);
        editLogger.setLayoutY(120.0);

        //handlers
        fileEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str = editLogger.getText() + "\nInput edit pressed";
                editLogger.setText(str);
            }
        });

        clearGraphButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str = "Edit Logger:\nGraph cleared";
                editLogger.setText(str);
            }
        });

        onWatchModeButton.setOnAction(new EventHandler<ActionEvent>() { //not ended yet
            @Override
            public void handle(ActionEvent event) {
                if(onWatchModeButton.getText() == "Watch"){
                    String str = "Algorithm Logger:";
                    editLogger.setText(str);
                    fileEditButton.setDisable(true);
                    clearGraphButton.setDisable(true);
                    onWatchModeButton.setText("Back to edit");
                    makeAlgStepButton.setVisible(true);
                    runFullAlgButton.setVisible(true);
                    reRunAlgButton.setVisible(true);
                }
                else{
                    editLogger.setText("Edit Logger:");
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
                String str = editLogger.getText() + "\nMaking step...";
                editLogger.setText(str);
            }
        });

        runFullAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str = editLogger.getText() + "\nRun full shit";
                editLogger.setText(str);
            }
        });

        reRunAlgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String str = editLogger.getText() + "\nRe run full shit";
                editLogger.setText(str);
            }
        });

        Group root = new Group(editLogger, fileEditButton, clearGraphButton, onWatchModeButton,
                makeAlgStepButton, runFullAlgButton, reRunAlgButton);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Everlasting summer practice");
        stage.setWidth(1250);
        stage.setHeight(800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}