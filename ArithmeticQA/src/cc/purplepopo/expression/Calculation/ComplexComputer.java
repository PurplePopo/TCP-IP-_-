package cc.purplepopo.expression.Calculation;

import java.util.ArrayList;

public class ComplexComputer{
    ArrayList<Operator> expressionStack = new ArrayList<>();
    ArrayList<Operator> resultStack = new ArrayList<>();
    ArrayList<Operator> operatorStack = new ArrayList<Operator>();
    RunningStatus runningStatus = RunningStatus.NORMAL;

    public ComplexComputer(String io){
        /*运算优先级
         * 1.括号开
         * 2.幂
         * 3.乘，除，取余
         * 4.加，减
         * 5.括号闭
         * 6.#
         * */
        //去掉无关的空格和制表符换行符等空白字符,并以#号为结束符
        io=io.replaceAll("\\s*", "").replaceAll("\\[","(").replaceAll("\\]",")")+"#";
        //重复符号处理
        io=io.replaceAll("\\+\\+","+0+").replaceAll("--","-0-").replaceAll("\\*\\*","*1*").replaceAll("//","/1/").replaceAll("\\^\\^","^1^");
        //开头负号处理
        if (io.charAt(0)=='-')io="0"+io;
        for (int i = 0,indexflag = 0; i < io.length(); i++)
            if (isnum(io.charAt(i))) continue;
            else if (io.charAt(i) == '.') {
                if (isnum(io.charAt(i + 1)) && i != 0) continue;
                else runningStatus = RunningStatus.POINT_ERR;
            } else {
                //是数字直接压堆，不是则判断是否为运算符错误
                if (i!=0&&isnum(io.charAt(i - 1)))
                    expressionStack.add(new Operator("number", Double.parseDouble(io.substring(indexflag, i))));//数字直接压堆
                else if (io.charAt(i) == '(' || io.charAt(i) == ')' ||io.charAt(i - 1) == '(' || io.charAt(i - 1) == ')') {
                } else {
                    runningStatus = RunningStatus.EXPRESSION_ERR;
                    return;
                }
                indexflag = i + 1;
                getOperator(io.charAt(i));//操作符压堆
            }


    }

    private boolean isnum(char c){
        if (c<='9'&&c>='0')return true;
        return false;
    }

    public String getComplexResult(){
        if (runningStatus==RunningStatus.NORMAL){
            for (int i=0;i<expressionStack.size();i++) {
                //数字直接入堆
                if (expressionStack.get(i).getName().contentEquals("number")){
                    resultStack.add(expressionStack.get(i));
                }else {
                    double num2 = resultStack.get(resultStack.size()-1).getValue();
                    double num1 = resultStack.get(resultStack.size()-2).getValue();
                    resultStack.remove(resultStack.size()-1);
                    resultStack.remove(resultStack.size()-1);
                    switch (expressionStack.get(i).getName()){
                        case "^":
                            resultStack.add(new Operator("number",Math.pow(num1,num2)));
                            break;
                        case "*":
                            resultStack.add(new Operator("number",(num1*num2)));
                            break;
                        case "/":
                            resultStack.add(new Operator("number",(num1/num2)));
                            break;
                        case "%":
                            resultStack.add(new Operator("number",(num1%num2)));
                            break;
                        case "+":
                            resultStack.add(new Operator("number",(num1+num2)));
                            break;
                        case "-":
                            resultStack.add(new Operator("number",(num1-num2)));
                            break;
                    }
                }
            }
            return String.valueOf(resultStack.get(0).getValue());
        }
        return String.valueOf(runningStatus);
    }

    public void getOperator(char c) {
        switch (c){
            case '(':
                operatorStack.add(new Operator(c+"",1));//优先级最高直接压堆
                break;
            case '^':
                for (;;){
                    if (operatorStack.isEmpty()){
                        operatorStack.add(new Operator(c+"",2));//空堆直接压堆
                        break;
                    }
                    //低优先级弹出堆,括号特殊对待
                    if (operatorStack.get(operatorStack.size()-1).getPriority()<=2&&!operatorStack.get(operatorStack.size()-1).getName().contentEquals("(")){
                        expressionStack.add(operatorStack.get(operatorStack.size()-1));//弹出到表达式堆
                        operatorStack.remove(operatorStack.size()-1);//弹出的移除
                    }else {
                        operatorStack.add(new Operator(c+"",2));//高优先级直接压堆
                        break;//退出循环
                    }
                }
                break;
            case '*':
            case '/':
            case '%':
                for (;;){
                    if (operatorStack.isEmpty()){
                        operatorStack.add(new Operator(c+"",3));//空堆直接压堆
                        break;
                    }
                    //低优先级弹出堆,括号特殊对待
                    if (operatorStack.get(operatorStack.size()-1).getPriority()<=3&&!operatorStack.get(operatorStack.size()-1).getName().contentEquals("(")){
                        expressionStack.add(operatorStack.get(operatorStack.size()-1));//弹出到表达式堆
                        operatorStack.remove(operatorStack.size()-1);//弹出的移除
                    }else {
                        operatorStack.add(new Operator(c+"",3));//高优先级直接压堆
                        break;//退出循环
                    }
                }
                break;
            case '+':
            case '-':
                for (;;){
                    if (operatorStack.isEmpty()){
                        operatorStack.add(new Operator(c+"",4));//空堆直接压堆
                        break;
                    }
                    //低优先级弹出堆,括号特殊对待
                    if (operatorStack.get(operatorStack.size()-1).getPriority()<=4&&!operatorStack.get(operatorStack.size()-1).getName().contentEquals("(")){
                        expressionStack.add(operatorStack.get(operatorStack.size()-1));//弹出到表达式堆
                        operatorStack.remove(operatorStack.size()-1);//弹出的移除
                    }else {
                        operatorStack.add(new Operator(c+"",4));//高优先级直接压堆
                        break;//退出循环
                    }
                }
                break;
            case ')':
                for (;;){
                    if (operatorStack.isEmpty()){runningStatus = RunningStatus.MATCH_ERR;break;}//空堆报括号匹配异常
                    //闭括号一直弹出堆直到开括号
                    if (!operatorStack.get(operatorStack.size()-1).getName().contentEquals("(")){
                        expressionStack.add(operatorStack.get(operatorStack.size()-1));//弹出到表达式堆
                        operatorStack.remove(operatorStack.size()-1);//弹出的移除
                    }else {
                        operatorStack.remove(operatorStack.size()-1);//弹出括号
                        break;//退出循环
                    }
                }
                break;
            case '#':
                for (;;) {
                    if (operatorStack.isEmpty())break;
                    if (operatorStack.get(operatorStack.size()-1).getName().contentEquals("("))runningStatus=RunningStatus.MATCH_ERR;
                    expressionStack.add(operatorStack.get(operatorStack.size()-1));//弹出到表达式堆
                    operatorStack.remove(operatorStack.size()-1);//弹出的移除
                }
                break;
            default:runningStatus = RunningStatus.SYMBOL_ERR;//未匹配到的字符
        }
    }
}