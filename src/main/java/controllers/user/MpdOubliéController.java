package controllers.user;
/*
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import java.util.Properties;

public class MpdOubliéController {

    @FXML
    private Button button_sendmail;

    @FXML
    private ImageView image;

    @FXML
    private TextField tf_email;

    @FXML
    void sendmail(ActionEvent event) throws MessagingException {
        String recipientEmail = tf_email.getText();
        sendEmail(recipientEmail);
    }

    private void sendEmail(String recipientEmail) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myEmail = "ferielkhamlia99@gmail.com";
        String password = "";

}

*/

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.binary.Base64;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static com.sun.javafx.application.PlatformImpl.setApplicationName;
import static javax.mail.Message.RecipientType.TO;

/*
public class MpdOubliéController {
    private static final String TEST_EMAIL = "ferielkhamlia99@gmail.com";

    private final Gmail service;
    @FXML
    private Button button_sendmail;

    @FXML
    private ImageView image;

    @FXML
    private TextField tf_email;
    public MpdOubliéController() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
         service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Test mailer").build();



    }
    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory) throws IOException {
// Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(MpdOubliéController.class.getResourceAsStream(" /C:/Users/MSI/Desktop/Avant integrationCRUD/src/main/resources/client_secret_340679100619-i2ggmfo8rjjbm18pcngq9ubntnjg5hvu.apps.googleusercontent.com.json")));

// Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }*/
    /*
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
// Load client secrets.
        InputStream in = GmailQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

// Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//returns an authorized Credential object.
        return credential;
    }
*/
  /*  private void sendEmail(String subject, String message) throws Exception{
        // send email to yourself :

// Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(TO, new InternetAddress(TEST_EMAIL));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
// Create send message
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: "+ msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {

            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }}



    }
    @FXML
    void sendmail(ActionEvent event) throws Exception {
        new MpdOubliéController().sendEmail("A new message","message");
    }

*/


/*
    // Identifiants EmailJS
    private static final String USER_ID = "BcFEU-wop9e0NEbxH";  // Remplace par ton vrai User ID
    private static final String SERVICE_ID = "service_24zzic8";  // Remplace par ton vrai Service ID
    private static final String TEMPLATE_ID = "template_bekougn";  // Remplace par ton vrai Template ID

    @FXML
    void sendmail(ActionEvent event) {
        String recipientEmail = tf_email.getText();

        if (recipientEmail.isEmpty()) {
            System.out.println("Veuillez entrer une adresse email.");
            return;
        }

        try {
            sendEmail(recipientEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendEmail(String recipientEmail) throws Exception {
        String url = "https://api.emailjs.com/api/v1.0/email/send";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            JSONObject emailData = new JSONObject();
            emailData.put("service_id", SERVICE_ID);
            emailData.put("template_id", TEMPLATE_ID);
            emailData.put("user_id", USER_ID);

            JSONObject templateParams = new JSONObject();
            templateParams.put("to_email", recipientEmail);
            templateParams.put("reply_to", "noreply@example.com");
            templateParams.put("user_email", recipientEmail);

            emailData.put("template_params", templateParams);

            request.setEntity(new StringEntity(emailData.toString()));

            try (CloseableHttpResponse response = client.execute(request)) {
                int responseCode = response.getCode();
                if (responseCode == 200) {
                    System.out.println("✅ Email envoyé avec succès !");
                } else {
                    System.out.println("❌ Erreur lors de l'envoi de l'email. Code: " + responseCode);
                }
            }
        }
    }
*/
