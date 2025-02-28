package services.sponsor;

import models.Transaction;
import services.IService;
import utils.MyDataSource;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService implements IService<Transaction> {

    Connection connection = MyDataSource.getInstance().getConn();

    public TransactionService() {}

    @Override
    public void ajouter(Transaction transaction) {
        String insertQuery = "INSERT INTO `transactions`(`id_contrat`, `type`, `sommeArgent`, `Date`, `description`) VALUES (?,?,?,?,?)";

        try {
            // Validate type
            if (!transaction.getType().equalsIgnoreCase("credit") &&
                    !transaction.getType().equalsIgnoreCase("debit")) {
                System.out.println("Error: Type must be either 'credit' or 'debit'!");
                return;
            }

            // Validate amount
            if (transaction.getSommeArgent() <= 0) {
                System.out.println("Error: Amount must be positive!");
                return;
            }

            // Insert transaction
            PreparedStatement ps = this.connection.prepareStatement(insertQuery);
            ps.setInt(1, transaction.getId_contrat());
            ps.setString(2, transaction.getType());
            ps.setFloat(3, transaction.getSommeArgent());
            ps.setString(4, transaction.getDate());
            ps.setString(5, transaction.getDescription());
            ps.executeUpdate();
            System.out.println("Transaction added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Transaction transaction) {
        String updateQuery = "UPDATE `transactions` SET `id_contrat`=?, `type`=?, `sommeArgent`=?, `Date`=?, `description`=? WHERE `Id_Transaction`=?";

        try {
            // Validate type
            if (!transaction.getType().equalsIgnoreCase("credit") &&
                    !transaction.getType().equalsIgnoreCase("debit")) {
                System.out.println("Error: Type must be either 'credit' or 'debit'!");
                return;
            }

            // Validate amount
            if (transaction.getSommeArgent() <= 0) {
                System.out.println("Error: Amount must be positive!");
                return;
            }


            // Update transaction
            PreparedStatement ps = this.connection.prepareStatement(updateQuery);
            ps.setInt(1, transaction.getId_contrat());
            ps.setString(2, transaction.getType());
            ps.setFloat(3, transaction.getSommeArgent());
            ps.setString(4, transaction.getDate());
            ps.setString(5, transaction.getDescription());
            ps.setInt(6, transaction.getId_transaction());
            ps.executeUpdate();
            System.out.println("Transaction updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Transaction transaction) {
        // Log the ID to ensure it's being passed correctly
        int transactionId = transaction.getId_transaction();
        System.out.println("Attempting to delete transaction with ID: " + transactionId);

        // SQL delete query
        String deleteQuery = "DELETE FROM transactions WHERE Id_Transaction = ?";

        try (PreparedStatement ps = this.connection.prepareStatement(deleteQuery)) {
            // Set the ID parameter for the query
            ps.setInt(1, transactionId);

            // Execute the delete statement
            int rowsAffected = ps.executeUpdate();

            // Check how many rows were affected and print debug info
            if (rowsAffected > 0) {
                System.out.println("Transaction with ID " + transactionId + " deleted successfully.");
            } else {
                System.out.println("No transaction found with ID " + transactionId);
            }
        } catch (SQLException e) {
            // Log any SQL exceptions
            System.out.println("Error deleting transaction: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public List<Transaction> recherche() {
        String selectQuery = "SELECT * FROM `transactions`";
        List<Transaction> transactions = new ArrayList<>();

        try {
            PreparedStatement ps = this.connection.prepareStatement(selectQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                //transaction.setId_transaction(rs.getInt("id_transaction"));
                //transaction.setId_contrat(rs.getInt("id_contrat"));
                transaction.setType(rs.getString("type"));
                transaction.setSommeArgent(rs.getFloat("sommeArgent"));
                transaction.setDate(rs.getString("Date"));
                transaction.setDescription(rs.getString("description"));
                transactions.add(transaction);
            }
            System.out.println(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
    /*
    private String formatDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format: " + date);
            return null;
        }
    }*/
}
