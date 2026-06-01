package models;

public class Evenement {

    private int id;
    private String titre;
    private String categorie;
    private String date;
    private String lieu;
    private double prix;
    private String image;

    // Constructeur
    public Evenement(String titre, String categorie, String date, String lieu, double prix, String image) {
        this.titre = titre;
        this.categorie = categorie;
        this.date = date;
        this.lieu = lieu;
        this.prix = prix;
        this.image = image;
    }

    // Getters
    public int getId(){return id;}
    public String getTitre() { return titre; }
    public String getCategorie() { return categorie; }
    public String getDate() { return date; }
    public String getLieu() { return lieu; }
    public double getPrix() { return prix; }
    public String getImage() {
        return image;
    }
}