package models.match;



import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TheImages {

    private ImageView images;

    // Constructeur qui prend l'URL de l'image
    public TheImages(String imageURL) {
        setImages(imageURL);
    }

    // Getter pour l'ImageView
    public ImageView getImages() {
        return this.images;
    }

    // Setter pour l'ImageView avec redimensionnement
    public void setImages(String URL) {
        System.out.println("image exists");
        Image anImage = new Image(URL);

        // Créer un ImageView à partir de l'image
        this.images = new ImageView(anImage);

        // Redimensionner l'image
        this.images.setFitWidth(50);  // Largeur de l'image
        this.images.setFitHeight(50); // Hauteur de l'image
        this.images.setPreserveRatio(true); // Maintenir le ratio de l'image
    }
}

/*
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TheImages {

    private ImageView images;

    // Constructeur qui prend l'URL de l'image
    public TheImages(String imageURL) {
        setImages(imageURL);
    }

    // Getter pour l'ImageView
    public ImageView getImages() {
        return this.images;
    }

    // Setter pour l'ImageView à partir de l'URL
    public void setImages(String URL) {
        System.out.println("image exists");
        Image anImage = new Image(URL);
        this.images = new ImageView(anImage);
    }
}
*/