package sample.controllers;

import Parser.MathParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.objects.ResizableCanvas;

/**
 * Created by ivano on 15.07.2017.
 */
public class GraphController {

    private Stage mainStage;
    private Scene mainScene;

    public static MathParser parser = new MathParser();
    public static String var;

    private Point2D delta;

    @FXML
    ResizableCanvas canva;

    @FXML
    AnchorPane canvasPane;

    @FXML
    TextField expression;

    @FXML
    Button btnStart;

    @FXML
    private void initialize() throws Exception {
        canva.widthProperty().bind(canvasPane.widthProperty());
        canva.heightProperty().bind(canvasPane.heightProperty());
    }



    public void setMainStage(Stage mainStage) { this.mainStage = mainStage; }
    public void setMainScene(Scene mainScene) { this.mainScene = mainScene; }



    public void onActionTypeMenu(ActionEvent actionEvent) {
        System.out.println("This is graphController");
        mainStage.setScene(mainScene);
        mainStage.setMinHeight(430);
        mainStage.setMinWidth(650);
    }

    public void onMouseClickedPlot(MouseEvent mouseEvent) {
        parser = new MathParser(expression.getText());
        var = parser.getVariable();

        canva.draw();
    }

    public void setOnActionTextField(ActionEvent actionEvent) {
        parser = new MathParser(expression.getText());
        var = parser.getVariable();

        canva.draw();
    }

    public void mousePressed(MouseEvent mouseEvent) {
        delta = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        canva.setLocalBorders(delta, new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        delta = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseScroll(ScrollEvent scrollEvent) {
        double zoomFactor = 0.6;
        double deltaY = scrollEvent.getDeltaY();
        if (deltaY < 0){
            zoomFactor = 10.0 / 6;
        }

        canva.setLocalBorders(canva.globalToLocal(new Point2D(scrollEvent.getX(), scrollEvent.getY())), zoomFactor);
    }

    public void onActionCloseMenu(ActionEvent actionEvent) {
        mainStage.close();
    }
}
