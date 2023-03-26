package karolina;

public class Punkt {
    private String x;
    public Double y1;
    public Double y2;
    public Double y3;
    public Double y4;

    public Punkt() {}

    public Punkt(String x, Double y1, Double y2, Double y3, Double y4) {
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        this.y4 = y4;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }

    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }

    public Double getY3() {
        return y3;
    }

    public void setY3(Double y3) {
        this.y3 = y3;
    }

    public Double getY4() {
        return y4;
    }

    public void setY4(Double y4) {
        this.y4 = y4;
    }

    @Override
    public String toString() {
        return "Punkt{" +
                "x='" + x + '\'' +
                ", y1=" + y1 +
                ", y2=" + y2 +
                ", y3=" + y3 +
                ", y4=" + y4 +
                '}';
    }
}
