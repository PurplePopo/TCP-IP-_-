package cc.purplepopo;

import cc.purplepopo.expression.AnsweringQuestions;
import cc.purplepopo.expression.GenerateQuestions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class GenerateController {
    @FXML
    private Button generateButton;
    @FXML
    private Button startButton;
    @FXML
    private Button submitButton;
    @FXML
    private ChoiceBox choseBoxNum;
    @FXML
    private TextField testQuestionsNum,hour,minum,secound;
    @FXML
    private Label timer;
    @FXML
    private VBox textVBox;

    ArrayList<String> questionsList = new ArrayList<>();
    ArrayList<String> answersList = new ArrayList<>();

    GenerateQuestions generateQuestions = new GenerateQuestions();
    AnsweringQuestions answeringQuestions = new AnsweringQuestions();

    public void textOnlyNumber(){
        testQuestionsNum.setText(testQuestionsNum.getText().replaceAll("[a-z]",""));
    }

    public void textTime(){
        hour.setText(hour.getText().replaceAll("[a-z]",""));
        minum.setText(minum.getText().replaceAll("[a-z]",""));
        secound.setText(secound.getText().replaceAll("[a-z]",""));
        if (Integer.parseInt(hour.getText())>=60)hour.setText("59");
        if (Integer.parseInt(minum.getText())>=60)minum.setText("59");
        if (Integer.parseInt(secound.getText())>=60)secound.setText("59");
        hour.positionCaret(hour.getLength());
        minum.positionCaret(minum.getLength());
        secound.positionCaret(secound.getLength());
    }

    public void generateButtonClick(ActionEvent actionEvent) {
        startButton.setDisable(false);
        textVBox.setDisable(true);
        generateQuestions.setChoseBoxNum(Integer.parseInt(choseBoxNum.getValue().toString()));
        textVBox.getChildren().removeAll(textVBox.getChildren());
        questionsList.removeAll(questionsList);

        for (int i = 0; i < Integer.parseInt(testQuestionsNum.getText()); i++) {
            String expression = generateQuestions.generate();
            questionsList.add(expression);
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("expression.fxml"));
                loader.setBuilderFactory(new JavaFXBuilderFactory());
                textVBox.getChildren().add(loader.load());
                ExpressionController expressionController = loader.getController();
                expressionController.Init(i+1,expression);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void startButtonClick(ActionEvent actionEvent) {
        generateButton.setDisable(true);
        startButton.setDisable(true);
        choseBoxNum.setDisable(true);
        testQuestionsNum.setDisable(true);
        hour.setDisable(true);
        minum.setDisable(true);
        secound.setDisable(true);
        submitButton.setDisable(false);
        timer.setVisible(true);
        textVBox.setDisable(false);
        answeringQuestions.setTimer(Integer.parseInt(hour.getText()),Integer.parseInt(minum.getText()),Integer.parseInt(secound.getText()));
        answeringQuestions.setVBox(textVBox);
        answeringQuestions.startTimer(timer);
    }

    public void submitButtonClick(ActionEvent actionEvent){
        timer.setVisible(true);
        generateButton.setDisable(false);
        startButton.setDisable(false);
        choseBoxNum.setDisable(false);
        testQuestionsNum.setDisable(false);
        hour.setDisable(false);
        minum.setDisable(false);
        secound.setDisable(false);
        submitButton.setDisable(true);
        answeringQuestions.stopTimer();
        answersList.removeAll(answersList);
        for (int i = 0; i < textVBox.getChildren().size(); i++) {
            Group group = (Group) textVBox.getChildren().get(i);
            TextField textField = (TextField) group.getChildren().get(2);
            answersList.add(textField.getText());
        }
        try {
            Stage stage=new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("submit.fxml"));
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = loader.load();
            stage.setTitle("提交");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            SubmitController submitController = loader.getController();
            submitController.setData(questionsList,answersList);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
