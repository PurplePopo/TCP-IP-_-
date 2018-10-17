package cc.purplepopo;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SubmitController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField iptext,pwtext;
    @FXML
    private TextArea textArea,webArea;
    @FXML
    private Button textSubmit,webSubmit;

    ArrayList<String> questionsList=new ArrayList<>();
    ArrayList<String> answersList=new ArrayList<>();

    public void textSubmitButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("四则运算练习题.txt");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files","*.*")
        );
        File file = fileChooser.showSaveDialog(anchorPane.getScene().getWindow());
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(textArea.getText().toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WebSubmitButton(){
        String ip = iptext.getText();
        //在FTP服务器上生成一个文件，并将一个字符串写入到该文件中
        try {
            InputStream input = new ByteArrayInputStream(webArea.getText().getBytes("UTF-8"));
            boolean flag = FtpUtil.uploadFile(ip, "star", pwtext.getText(), 21, "ftp/", "index.html",input);
            System.out.println(flag);
            input.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Desktop.getDesktop().browse(new URI("http://purplepopo.cc/ftp"));
        }catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setData(ArrayList<String> questionsList, ArrayList<String> answersList) {
        this.answersList.removeAll(answersList);
        this.questionsList.removeAll(questionsList);
        this.questionsList.addAll(questionsList);
        this.answersList.addAll(answersList);
        webArea.insertText(webArea.getLength(),"<html>\n");
        webArea.insertText(webArea.getLength(),"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
        webArea.insertText(webArea.getLength(),"<title>测试报告文档</title></head>\n");
        webArea.insertText(webArea.getLength(),"<body><h1>四则运算报告</h1>\n<ol>\n");
        for (int i = 0; i < questionsList.size(); i++) {
            textArea.insertText(textArea.getLength(),(i+1)+".  \r\t"+questionsList.get(i)+" \r\t = \r\t"+answersList.get(i)+"\r\n"+System.lineSeparator());
            webArea.insertText(webArea.getLength(),"<li>"+questionsList.get(i)+" \r\t = \r\t"+answersList.get(i)+"</li>\n");
        }
        webArea.insertText(webArea.getLength(),"</ol>\n</body>\n</html>\n");
    }
}
