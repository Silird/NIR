package ru.silirdco.nir.view.frames;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("unused")
public class MainJavaFX extends Application {

    private static MainFrameController mainFrameController;
    private static Node mainFrameNode;
    private static Stage stage;

    public static void show(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/MainFrame.fxml"));
        mainFrameController = new MainFrameController();
        loader.setController(mainFrameController);
        stage = primaryStage;

        try {
            mainFrameNode = loader.load();

            Scene scene = new Scene((Parent) mainFrameNode);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Главная форма");
            primaryStage.show();

            primaryStage.setOnCloseRequest(we -> {
                Platform.exit();
                System.exit(0);
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static Node getMainFrameNode() {
        return mainFrameNode;
    }

    public static Stage getStage() {
        return stage;
    }
}
