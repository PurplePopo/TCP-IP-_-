package cc.purplepopo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ExpressionController{
    @FXML
    private Label listLable;
    @FXML
    private Label expressionLabel;
    @FXML
    private TextField textField;

    public void Init(long list, String expressionStr){
        this.listLable.setText(list+".");
        this.expressionLabel.setText(expressionStr+" = ");
    }

}
