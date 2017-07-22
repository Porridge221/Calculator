package sample.controllers;

import Parser.MathParser;
import Parser.MyPair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.javafx.JavaFXRenderer3d;
import org.jzy3d.javafx.controllers.mouse.JavaFXCameraMouseController;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;


import java.io.IOException;

public class Controller {

    MathParser parser = new MathParser();

    @FXML
    Button startBtn;

    @FXML
    TextField expression;

    @FXML
    TextArea outputField;

    @FXML
    TableView<MyPair> varTable;

    @FXML
    TableColumn<MyPair, String> columnVar;

    @FXML
    TableColumn<MyPair, Double> columnVal;

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private Parent fxmlEdit;
    private EditDialogController editDialogController;
    private Stage editDialogStage;

    private Parent fxmlGraph;
    private GraphController graphController;
    private Stage graphStage;

    private Parent fxmlGraph3d;
    private Graph3dController graph3dController;
    private Stage graph3dStage;

    private Stage mainStage;
    private Scene mainScene;

    @FXML
    private void initialize() throws Exception {
        columnVar.setCellValueFactory(new PropertyValueFactory<MyPair, String>("key"));
        columnVal.setCellValueFactory(new PropertyValueFactory<MyPair, Double>("value"));

        initLoader();
    }

    private void Parse() {
        parser = new MathParser(expression.getText());
    }

    private void Execute() {
        outputField.setStyle("-fx-text-inner-color: black;");
        outputField.setText("Введенное выражение: " + expression.getText() + "\n" +
                "Выражение в обратной польской записи: " + parser.getConvertedString() + "\n" +
                "Результат: " + Double.toString(parser.Execute()) + "\n" +
                "----------------------------------" + "\n" + outputField.getText());

        varTable.setItems(parser.getVariableList());
    }


    public void setMainStage(Stage mainStage) { this.mainStage = mainStage; }
    public void setMainScene(Scene mainScene) { this.mainScene = mainScene; }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../fxml/edit.fxml"));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();

            /*fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/graph.fxml"));
            fxmlGraph = fxmlLoader.load();
            graphController = fxmlLoader.getController();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditDialog() {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle("Редактирование записи");
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }

        editDialogStage.showAndWait();
    }

    private void showGraphDialog() {
        /*graphStage = new Stage();
        graphStage.setTitle("Graph");
        graphStage.setMinHeight(640);
        graphStage.setMinWidth(800);
        graphStage.setScene(new Scene(fxmlGraph));
        //graphStage.initModality(Modality.WINDOW_MODAL);
        graphStage.initOwner(mainStage);*/

        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/graph.fxml"));
            fxmlGraph = fxmlLoader.load();
            graphController = fxmlLoader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        graphController.setMainStage(mainStage);
        graphController.setMainScene(mainScene);
        mainStage.setScene(new Scene(fxmlGraph));
        mainStage.setMinHeight(640);
        mainStage.setMinWidth(800);
        //mainStage.hide();
        //graphStage.show();
    }

    private void showGraph3dDialog() {
        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../fxml/graph3d.fxml"));
            fxmlGraph3d = fxmlLoader.load();
            graph3dController = fxmlLoader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        graph3dController.setMainStage(mainStage);
        graph3dController.setMainScene(mainScene);
        Scene scene = new Scene(fxmlGraph3d);
        graph3dController.setScene(scene);
        mainStage.setScene(scene);
        mainStage.setMinHeight(640);
        mainStage.setMinWidth(800);
    }


    public void OnExecute(MouseEvent mouseEvent) {
        if (parser.getSource().equals(expression.getText())) {
            Execute();
        } else {
            Parse();
            Execute();
        }
    }

    public void setOnActionTextField(ActionEvent actionEvent) {
        if (parser.getSource().equals(expression.getText())) {
            Execute();
        } else {
            Parse();
            Execute();
        }
    }

    public void setOnTableClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            editDialogController.setTableCell(varTable.getSelectionModel().getSelectedItem());
            showEditDialog();
            MyPair newCell = editDialogController.getTableCell();
            parser.replaceVariable(newCell.getKey(), newCell.getValue());
            varTable.setItems(parser.getVariableList());
            //Execute();
        }
    }

    public void onActionTypeMenu(ActionEvent actionEvent) {
        showGraphDialog();
    }

    public void onActionCloseMenu(ActionEvent actionEvent) {
        mainStage.close();
    }

    public void onActionMenuGraph3d(ActionEvent actionEvent) {
        showGraph3dDialog();
    }
}
