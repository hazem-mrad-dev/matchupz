package models;

public class Transaction {
    private int id_transaction;
    private int id_contrat;
    private String type;
    private float sommeArgent;
    private String Date;
    private String description;

    public Transaction() {}

    public Transaction(int id_contrat, String type, float sommeArgent, String date, String description) {
        this.id_contrat = id_contrat;  // Ensure this is correctly set
        this.type = type;
        this.sommeArgent = sommeArgent;
        this.Date = date;
        this.description = description;
    }

    public Transaction(int id_transaction, int idContrat, String type, float sommeArgent, String Date, String description) {
        this.id_transaction = id_transaction;
        this.id_contrat = idContrat;
        this.type = type;
        this.sommeArgent = sommeArgent;
        this.Date = Date;
        this.description = description;
    }

    public int getId_transaction() {
        return id_transaction;
    }

    public String getType() {
        return type;
    }

    public float getSommeArgent() {
        return sommeArgent;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return description;
    }

    public void setId_transaction(int id_transaction) {
        this.id_transaction = id_transaction;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSommeArgent(float sommeArgent) {
        this.sommeArgent = sommeArgent;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                " type='" + type + '\'' +
                ", sommmeArgent=" + sommeArgent +
                ", Date='" + Date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId_contrat() {
        return id_contrat;
    }

    public void setId_contrat(int id_contrat) {
        this.id_contrat = id_contrat;
    }
}
