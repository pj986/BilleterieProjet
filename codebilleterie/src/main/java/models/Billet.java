package models;

public class Billet {
    private int id;
    private int evenementId;
    private String emailClient;
    private String statut;

    public Billet(int evenementId, String emailClient, String statut) {
        this.evenementId = evenementId;
        this.emailClient = emailClient;
        this.statut = statut;
    }

    public int getEvenementId() { return evenementId; }
    public String getEmailClient() { return emailClient; }
    public String getStatut() { return statut; }
}
