# Cahier des Charges - Billeterie Tic’n Go

---

## 1. Contexte et Objectifs
**Objectif** :
Développer un back-office en Java pour gérer l'édition des billets (création, modification, tarification, validation, annulation) pour une application type BilletRéduc : https://www.billetreduc.com/.

**Public cible** :
- Administrateurs de la plateforme
- Clients qui n'ont pas accès directement au système

---

## 2. Fonctionnalités Clés

### A. Gestion des Billets

| Fonctionnalité          | Description                                                                 | Répercussions concrètes                          |
|-------------------------|-----------------------------------------------------------------------------|--------------------------------------------------|
| Création de billets     | Génération de billets (numéro unique, événement, place, tarif)             | Permet de vendre des places pour chaque événement.|
| Modification            | Mise à jour des informations (prix, place, statut)                          | Flexibilité en cas de changement de dernière minute.|
| Annulation/Validation   | Changement de statut (valide, annulé, remboursé)                            | Gestion des désistements et remboursements.      |
| Export des billets      | Export en PDF ou format imprimable (avec QR code)                           | Facilite l'entrée en salle et le contrôle.       |
| Recherche avancée       | Filtre par événement, date, statut, utilisateur                             | Gain de temps pour le service client.            |

#### Détail
1 spectacle possède :
- 1 titre
- 1 affiche
- 1 lieu qui peut accueillir d’autres spectacles !
- Des tags/labels: spectacle, comédie musical, onemanshow, etc.
- 1 durée
- Description courte
- Description longue
- Une langue
- Parfois un âge minimum : 3 ans, 18 ans
- Parfois des photos
- Plusieurs séances dans la même journée

1 client possède :
- Nom/pseudo
- Adresse email
- Parfois un numéro de téléphone
- Historique des spectacles
- Adresse : 1 simple champ texte par simplicité
 
1 billet possède un numéro unique qui est affiché sur le billet, il est forcément rattaché à un client et à un évènement.

Les administrateurs possèdent un email utilisé comme login et un mot de passe.


### B. Gestion des Tarifs (selon le temps)

| Fonctionnalité          | Description                                                                 | Répercussions concrètes                          |
|-------------------------|-----------------------------------------------------------------------------|--------------------------------------------------|
| Grilles tarifaires      | Définition des tarifs par catégorie (plein, réduit, groupe)                 | Adaptation aux politiques commerciales.           |
| Promotions              | Application de réductions temporaires ou ciblées                           | Stimule les ventes et fidélise les clients.      |


---


## 3. Sécurité et Conformité

- Authentification : Identifiants uniques (hashé avec bcrypt) + 2FA (selon le temps)
- Respect RGPD

---

## 4. Ergonomie

- Tableau de bord avec vue globale des billets (filtres).
- Formulaires clairs pour la création/modification.

---

## 5. Workflow Exemple

1. **Création** :
   Admin sélectionne un événement → définit places/tarifs → génère les billets
2. **Modification** :
   Recherche du billet →  mise à jour du billet
3. **Annulation** :
   Changement de statut + envoi d’email au client

---

## 6. Livrables
- Code source Java.
- Archive Java: jar fonctionnelle
- Scripts SQL.
- Documentation pour l'instalation (selon le temps).
- Documentation pour le déploiement (selon le temps).
- Environnement de test avec jeux de données exemples.

---

## 7. Budget
- Développement : 25 000 €
- Maintenance : 10-20% du coût initial/an

---
