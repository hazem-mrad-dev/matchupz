package services.EspaceSportif;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.EspaceSportif.EspaceSportif;
import utils.MyDataSource;
//import utils.MyDatabase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//import utils.MyDataSource
public class EspaceSportifService implements EspaceService<EspaceSportif> {

    private static final String API_KEY = "XCW8/1wIsfqTq2rNk1EKUQ==J1mWGCjrLv2PR8Kh"; // Votre clé API Ninja
    private static final String API_COORDONNEES = "https://api.api-ninjas.com/v1/geocoding?city=";
    private static final String API_WEATHER = "https://api.api-ninjas.com/v1/weather?lat={lat}&lon={lon}";
    //private final Connection connection = MyDatabase.getInstance().getConnection();
    Connection connection = MyDataSource.getInstance().getConn();
    @Override
    public void ajouter(EspaceSportif espaceSportif) {
        String req = "INSERT INTO `espacesportif` (`nom_espace`, `adresse`, `categorie`, `capacite`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, espaceSportif.getNomEspace());
            ps.setString(2, espaceSportif.getAdresse());
            ps.setString(3, espaceSportif.getCategorie());
            ps.setFloat(4, espaceSportif.getCapacite());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif ajouté !");

            // Appel des APIs pour affichage uniquement
            double[] coords = getCoordonnes(espaceSportif.getAdresse() + ", Tunisie"); // Append country
            if (coords != null) {
                String climat = getClimat(coords[0], coords[1]);
                System.out.println("Coordonnées : Latitude = " + coords[0] + ", Longitude = " + coords[1]);
                System.out.println("Climat : " + climat);
            } else {
                System.out.println("Impossible de récupérer les coordonnées pour cette adresse.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'appel API : " + e.getMessage());
        }
    }

    @Override
    public void modifier(EspaceSportif espaceSportif) {
        String req = "UPDATE `espacesportif` SET nom_espace=?, adresse=?, categorie=?, capacite=? WHERE id_lieu=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, espaceSportif.getNomEspace());
            ps.setString(2, espaceSportif.getAdresse());
            ps.setString(3, espaceSportif.getCategorie());
            ps.setFloat(4, espaceSportif.getCapacite());
            ps.setInt(5, espaceSportif.getIdLieu());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif modifié !");

            // Appel des APIs pour affichage uniquement
            double[] coords = getCoordonnes(espaceSportif.getAdresse() + ", Tunisie"); // Append country
            if (coords != null) {
                String climat = getClimat(coords[0], coords[1]);
                System.out.println("Coordonnées : Latitude = " + coords[0] + ", Longitude = " + coords[1]);
                System.out.println("Climat : " + climat);
            } else {
                System.out.println("Impossible de récupérer les coordonnées pour cette adresse.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'appel API : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(EspaceSportif espaceSportif) {
        String req = "DELETE FROM `espacesportif` WHERE id_lieu=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, espaceSportif.getIdLieu());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif supprimé !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<EspaceSportif> rechercher() {
        String req = "SELECT * FROM `espacesportif`";
        List<EspaceSportif> espacesSportifs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EspaceSportif espaceSportif = new EspaceSportif();
                espaceSportif.setIdLieu(rs.getInt("id_lieu"));
                espaceSportif.setNomEspace(rs.getString("nom_espace"));
                espaceSportif.setAdresse(rs.getString("adresse"));
                espaceSportif.setCategorie(rs.getString("categorie"));
                espaceSportif.setCapacite(rs.getFloat("capacite"));
                espacesSportifs.add(espaceSportif);

                // Appel des APIs pour affichage uniquement (non utilisé directement dans la liste)
                double[] coords = getCoordonnes(espaceSportif.getAdresse() + ", Tunisie"); // Append country
                if (coords != null) {
                    String climat = getClimat(coords[0], coords[1]);
                    System.out.println("Espace : " + espaceSportif.getNomEspace() + " - Coordonnées : " + coords[0] + ", " + coords[1] + " - Climat : " + climat);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'appel API : " + e.getMessage());
        }

        return espacesSportifs;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SHOW COLUMNS FROM `espacesportif` WHERE Field = 'categorie'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String enumValues = rs.getString("Type");
                enumValues = enumValues.substring(enumValues.indexOf("(") + 1, enumValues.lastIndexOf(")"));
                String[] values = enumValues.replace("'", "").split(",");
                categories.addAll(List.of(values));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }

    public List<EspaceSportif> rechercherParMotCle(String motCle) {
        String req = "SELECT id_lieu, nom_espace, adresse, categorie, capacite FROM espacesportif " +
                "WHERE nom_espace LIKE ? OR adresse LIKE ? OR categorie LIKE ?";
        List<EspaceSportif> espacesSportifs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");
            ps.setString(2, "%" + motCle + "%");
            ps.setString(3, "%" + motCle + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EspaceSportif espaceSportif = new EspaceSportif();
                    espaceSportif.setIdLieu(rs.getInt("id_lieu"));
                    espaceSportif.setNomEspace(rs.getString("nom_espace"));
                    espaceSportif.setAdresse(rs.getString("adresse"));
                    espaceSportif.setCategorie(rs.getString("categorie"));
                    espaceSportif.setCapacite(rs.getFloat("capacite"));
                    espacesSportifs.add(espaceSportif);

                    // Appel des APIs pour affichage uniquement (non utilisé directement dans la liste)
                    double[] coords = getCoordonnes(espaceSportif.getAdresse() + ", Tunisie"); // Append country
                    if (coords != null) {
                        String climat = getClimat(coords[0], coords[1]);
                        System.out.println("Espace : " + espaceSportif.getNomEspace() + " - Coordonnées : " + coords[0] + ", " + coords[1] + " - Climat : " + climat);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des espaces sportifs par mot-clé : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'appel API : " + e.getMessage());
        }

        return espacesSportifs;
    }

    public int getIdLieuByName(String nomLieu) {
        String req = "SELECT id_lieu FROM espacesportif WHERE nom_espace = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, nomLieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_lieu");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'ID du lieu : " + e.getMessage());
        }
        return -1;
    }

    public List<String> getLieux() {
        List<String> lieux = new ArrayList<>();
        String req = "SELECT nom_espace FROM espacesportif";

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lieux.add(rs.getString("nom_espace"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des lieux : " + e.getMessage());
        }

        return lieux;
    }

    public String getNomLieuById(int idLieu) {
        String query = "SELECT nom_espace FROM espacesportif WHERE id_lieu = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idLieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_espace");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération du nom du lieu : " + e.getMessage());
        }
        return null;
    }

    // Méthode pour récupérer les coordonnées (pas de stockage)
    public double[] getCoordonnes(String address) throws IOException {
        // Ensure the address includes a country (e.g., "City, Country")
        if (!address.contains(",")) {
            throw new IllegalArgumentException("L'adresse doit inclure la ville et le pays, sous forme 'Ville, Pays' (ex. 'Tunis, Tunisie')");
        }

        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String urlString = API_COORDONNEES + encodedAddress;
        System.out.println("Tentative de récupération des coordonnées pour l'adresse : " + address + " (URL : " + urlString + ")");

        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        connection.setConnectTimeout(5000); // Augmentation du timeout à 10 secondes
        connection.setReadTimeout(5000);    // Augmentation du timeout à 10 secondes

        int status = connection.getResponseCode();
        if (status != 200) {
            System.out.println("Error fetching coordinates: " + status + " pour l'URL : " + urlString);
            return null;
        }

        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());
            System.out.println("Réponse JSON brute : " + response.toString());

            JsonNode firstResult = jsonResponse.get(0);
            if (firstResult == null) {
                System.out.println("Aucun résultat trouvé pour l'adresse : " + address);
                return null;
            }

            double latitude = firstResult.get("latitude").asDouble();
            double longitude = firstResult.get("longitude").asDouble();

            System.out.println("Coordonnées trouvées : Latitude = " + latitude + ", Longitude = " + longitude);
            return new double[]{latitude, longitude};
        }
    }

    // Méthode pour récupérer le climat sous forme de String en utilisant l'API Weather Ninja
    public String getClimat(double latitude, double longitude) throws IOException {
        // Remplacez {lat} et {lon} par les valeurs réelles
        String urlString = API_WEATHER.replace("{lat}", String.valueOf(latitude)).replace("{lon}", String.valueOf(longitude));
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        if (status != 200) {
            System.out.println("Error fetching weather data: " + status);
            return null;
        }

        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());

            // Extrait les informations climatiques pertinentes de la réponse
            String temp = jsonResponse.get("temp").asText() + "°C"; // Température en Celsius
            String humidity = jsonResponse.get("humidity").asText() + "%"; // Humidité
            String condition = jsonResponse.get("cloud_pct").asText() + "% nuages"; // Pourcentage de nuages (exemple)

            // Retourne une description simplifiée du climat
            return String.format("Temp: %s, Humidité: %s, Condition: %s", temp, humidity, condition);
        }
    }

    public String getMapUrl(double latitude, double longitude) {
        // Utilise Mapbox pour générer une URL statique de carte avec un template
        String baseUrl = "https://api.mapbox.com/styles/v1/mapbox/streets-v11.html?access_token=pk.eyJ1IjoiaXNtYWlsMDIiLCJhIjoiY200cmJoajV4MDNibDJtc2VycmE2NnJ2MCJ9.6Nu1GLxUDkC9Odep6mKsmA#16/{lat}/{lon}";
        String accessToken = "pk.eyJ1IjoiaXNtYWlsMDIiLCJhIjoiY200cmJoajV4MDNibDJtc2VycmE2NnJ2MCJ9.6Nu1GLxUDkC9Odep6mKsmA"; // Note: Consider moving to config
        int zoomLevel = 15; // Default zoom level as specified

        // Replace placeholders with actual latitude and longitude
        String urlString = baseUrl
                .replace("{lat}", String.valueOf(latitude))
                .replace("{lon}", String.valueOf(longitude));

        return urlString;
    }
}