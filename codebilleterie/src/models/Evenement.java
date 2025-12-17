package models;

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private String date;
    private String lieu;
    private double prix;
    private String image;

    public Evenement(int id, String titre, String description, String date, String lieu, double prix, String image) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.prix = prix;
        this.image = image;
    }

    public Evenement(String titre, String description, String date, String lieu, double prix, String image) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.prix = prix;
        this.image = image;
    }

    // --- Getters & Setters ---

    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getLieu() { return lieu; }
    public double getPrix() { return prix; }
    public String getImage() { return image; }

    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setImage(String image) { this.image = image; }

    
}
