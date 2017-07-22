package sample.controllers;

import Parser.MathParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * Created by ivano on 22.07.2017.
 */
public class Graph3dController {

    private Stage mainStage;
    private Scene mainScene;

    private Scene scene;

    private MathParser parser = new MathParser();
    private ImageView imageView = new ImageView();

    @FXML
    Pane canvasPane;

    @FXML
    TextField expression;

    @FXML
    TextField leftBorderRange;

    @FXML
    TextField rightBorderRange;

    @FXML
    Button btnStart;

    @FXML
    private void initialize() {
        imageView.fitHeightProperty().bind(canvasPane.heightProperty());
        imageView.fitWidthProperty().bind(canvasPane.widthProperty());
    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit) {

        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                parser.replaceVariable(parser.getVar1(), x);
                parser.replaceVariable(parser.getVar2(), y);
                return parser.Execute();
            }
        };

        Range range = new Range(Float.parseFloat(leftBorderRange.getText()), Float.parseFloat(rightBorderRange.getText()));
        int steps = 100;

        final Shape surface = Builder.buildOrthonormal(mapper, range, steps);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        Quality quality = Quality.Advanced;
        quality.setSmoothPolygon(true);
        //quality.setAnimated(true);

        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        chart.getScene().getGraph().add(surface);
        return chart;
    }

    private void plot() {
        canvasPane.getChildren().clear();

        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart  = getDemoChart(factory, "offscreen");
        imageView = factory.bindImageView(chart);

        imageView.fitHeightProperty().bind(canvasPane.heightProperty());
        imageView.fitWidthProperty().bind(canvasPane.widthProperty());

        canvasPane.getChildren().add(imageView);

        factory.addSceneSizeChangedListener(chart, scene);
    }



    public void setMainStage(Stage mainStage) { this.mainStage = mainStage; }
    public void setMainScene(Scene mainScene) { this.mainScene = mainScene; }

    public void setScene(Scene scene) { this.scene = scene; }

    public void onActionTypeMenu(ActionEvent actionEvent) {
        System.out.println("This is graph3dController");
        mainStage.setScene(mainScene);
        mainStage.setMinHeight(430);
        mainStage.setMinWidth(650);
    }

    public void setOnActionTextField(ActionEvent actionEvent) {
        parser = new MathParser(expression.getText());

        plot();
    }

    public void onMouseClickedPlot(MouseEvent mouseEvent) {
        parser = new MathParser(expression.getText());

        plot();
    }

    public void onActionCloseMenu(ActionEvent actionEvent) {
        mainStage.close();
    }
}
