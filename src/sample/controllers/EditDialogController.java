package sample.controllers;

import Parser.MyPair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by ivano on 15.07.2017.
 */
public class EditDialogController {

    @FXML
    Button btnOk;

    @FXML
    Button btnCancel;

    @FXML
    TextField txtVariable;

    @FXML
    TextField txtValue;

    MyPair tableCell;

    public void setTableCell(MyPair tableCell) {
        if (tableCell == null)
            return;
        this.tableCell = tableCell;
        txtVariable.setText(tableCell.getKey());
        txtValue.setText(Double.toString(tableCell.getValue()));
    }

    public MyPair getTableCell() { return  tableCell; }

    public void actionSave(ActionEvent actionEvent) {
        tableCell.setKey(txtVariable.getText());
        tableCell.setValue(Double.parseDouble(txtValue.getText()));
        actionClose(actionEvent);
    }

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }
}
