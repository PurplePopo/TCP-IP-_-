package cc.purplepopo.expression.Calculation;

public class Operator{

    private String name;//名字
    private int priority;//优先级
    private double value;//数值

    public Operator(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public Operator(String name, double value){
        this.name = name;
        this.value = value;
        this.priority = 0;//数值默认最高优先级
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}