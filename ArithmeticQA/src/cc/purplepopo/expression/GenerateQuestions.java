package cc.purplepopo.expression;

import java.util.Random;


public class GenerateQuestions {
    private int chooseBoxNum;

    public GenerateQuestions() {
        this.chooseBoxNum = 4;
    }

    public void setChoseBoxNum(int chooseBoxNum) {
        this.chooseBoxNum = chooseBoxNum;
    }

    public String generate() {
        StringBuilder expression = new StringBuilder();
        Random random = new Random();
        int flag=0;
        for (int i = 0; i < chooseBoxNum; i++) {
            if (chooseBoxNum>2)
            if (random.nextInt(4)>i){
                expression.insert(expression.length(),"(");
                flag += 1;
            }
            if (i!=0&&expression.charAt(expression.length()-1)=='/') expression.append(String.valueOf(random.nextInt(99)+1));
            else expression.append(String.valueOf(random.nextInt(100)));
            if (chooseBoxNum>2)
            if (random.nextInt(4) < i && flag > 0) {
                expression.insert(expression.length(), ")");
                flag -= 1;
            }
            if (i==chooseBoxNum-1)break;
            switch (random.nextInt(4)){
                case 0:expression.append("+");break;
                case 1:expression.append("-");break;
                case 2:expression.append("*");break;
                case 3:expression.append("/");break;
            }
        }
        for (int i = 0; i < flag; i++) {
            expression.insert(expression.length(),")");
        }
        return expression.toString();
    }
}
