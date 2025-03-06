package model;

public class Loan {
    private int id;
    private int aplicant_id;
    private double amount;
    private String status;

    public Loan() {

    }

    public Loan(int id, int aplicant_id, double amount, String status) {
        this.id = id;
        this.aplicant_id = aplicant_id;
        this.amount = amount;
        this.status = status;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public int getAplicantId() { return this.aplicant_id; }

    public void setAplicantId(int id) { this.aplicant_id = id; }

    public double getAmount() { return this.amount; }

    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    public String toString() {
        return "Loan{" + "id=" + id + ", amount=" + amount + ", status=" + status + '}';
    }
}
