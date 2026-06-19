package models;

public class Administrateur {

    private int idAdmin;
    private String nom;
    private String email;

    public Administrateur() {
    }

    public Administrateur(int idAdmin, String nom, String email) {
        this.idAdmin = idAdmin;
        this.nom = nom;
        this.email = email;
    }

    public Administrateur(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nom + " - " + email;
    }
}