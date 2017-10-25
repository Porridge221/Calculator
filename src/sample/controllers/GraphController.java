package sample.controllers;

import Parser.MathParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

        if (canva.isZooming(zoomFactor))
            canva.setLocalBorders(canva.globalToLocal(new Point2D(scrollEvent.getX(), scrollEvent.getY())), zoomFactor);
        else
            showPopupMessage("Вы достишли границу приближения/отдаления", mainStage);
    }

    public void onActionCloseMenu(ActionEvent actionEvent) {
        mainStage.close();
    }

    public static Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
            }
        });
        label.getStylesheets().add("sample/style/styles.css");
        label.getStyleClass().add("popup");
        popup.getContent().add(label);
        return popup;
    }

    public static void showPopupMessage(final String message, final Stage stage) {
        final Popup popup = createPopup(message);
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                popup.setX(stage.getX() + stage.getWidth() - popup.getWidth() - 75);
                popup.setY(stage.getY() + stage.getHeight() - popup.getHeight() - 50);
            }
        });
        popup.show(stage);
    }
}
