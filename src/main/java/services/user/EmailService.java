package services.user;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    public static void sendEmail(String toEmail, String subject, String body) {
        // Set up the SMTP server properties
        Properties properties = new Properties();

        // For Gmail
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Change to the actual SMTP host
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


        // Set up authentication
        String username = "contact.reefinity@gmail.com";  // Replace with your email
        String password = "ajsf exhz zxxa tsrx"; // Replace with your app-specific password if using Gmail

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            // Better error logging
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
