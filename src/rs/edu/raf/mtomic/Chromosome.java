package rs.edu.raf.mtomic;

public class Chromosome {

    private double coef1;
    private double coef2;
    private double coef3;
    private double coef4;
    private double closestPellet;

    private int score;

    public Chromosome(double coef1, double coef2, double coef3, double coef4, double closestPellet) {
        this.coef1 = clamp(coef1);
        this.coef2 = clamp(coef2);
        this.coef3 = clamp(coef3);
        this.coef4 = clamp(coef4);
        this.closestPellet = clamp(closestPellet);
    }

    public double getCoef1() {
        return coef1;
    }

    public void setCoef1(double coef1) {
        this.coef1 = clamp(coef1);
    }

    public double getCoef2() {
        return coef2;
    }

    public void setCoef2(double coef2) {
        this.coef2 = clamp(coef2);
    }

    public double getCoef3() {
        return coef3;
    }

    public void setCoef3(double coef3) {
        this.coef3 = clamp(coef3);
    }

    public double getCoef4() {
        return coef4;
    }

    public void setCoef4(double coef4) {
        this.coef4 = clamp(coef4);
    }

    public double getClosestPellet() {
        return closestPellet;
    }

    public void setClosestPellet(double closestPellet) {
        this.closestPellet = clamp(closestPellet);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double clamp(double value){
        if(value > 5000) return 5000;
        else if(value < -5000) return -5000;
        else return value;
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "coef1=" + coef1 +
                ", coef2=" + coef2 +
                ", coef3=" + coef3 +
                ", coef4=" + coef4 +
                ", closestPellet=" + closestPellet +
                ", score=" + score +
                '}';
    }
}
