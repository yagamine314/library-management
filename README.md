# 📚 Système de Gestion de Bibliothèque

Application Java complète avec interface JavaFX pour gérer une bibliothèque (livres, membres, emprunts).

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9-red.svg)](https://maven.apache.org/)

---

## 👥 Équipe de Développement

| Rôle | Nom | Responsabilité |
|------|-----|----------------|
| **Étudiant A** | Amine Benatti | Module Livres (CRUD + Interface) |
| **Étudiant B** | Idriss Jabri | Module Membres (CRUD + Interface) |
| **Étudiant C** | Hamza Ahaddad | Module Emprunts (Logique métier) |
| **Étudiant D** | Ilyas Jaafar | Infrastructure (Singleton, DAO, Exceptions) |

---

## 🛠 Technologies Utilisées

- **Java 17** - Langage de programmation
- **JavaFX 21** - Interface graphique moderne
- **MySQL 8.0** - Base de données relationnelle
- **Maven 3.9** - Gestionnaire de dépendances et build
- **JUnit 5** - Framework de tests unitaires
- **JDBC** - Connecteur base de données

---

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé:

- ☑️ **JDK 17 ou supérieur** ([Télécharger](https://www.oracle.com/java/technologies/downloads/#java17))
- ☑️ **MySQL 8.0 ou supérieur** ([Télécharger](https://dev.mysql.com/downloads/mysql/))
- ☑️ **Maven 3.9 ou supérieur** ([Télécharger](https://maven.apache.org/download.cgi))
- ☑️ **Un IDE Java** (IntelliJ IDEA, Eclipse, ou VS Code)

### Vérification des installations:

```bash
# Vérifier Java
java -version
# Doit afficher: java version "17.x.x"

# Vérifier Maven
mvn -version
# Doit afficher: Apache Maven 3.9.x

# Vérifier MySQL
mysql --version
# Doit afficher: mysql Ver 8.0.x
```

---

## 🚀 Installation et Configuration

### Étape 1: Cloner le projet

```bash
git clone https://github.com/yagamine314/library-management
cd library-management
```

### Étape 2: Créer la base de données

**Option A: Via ligne de commande**
```bash
mysql -u root -p < src/main/resources/sql/create_database.sql
```

**Option B: Via MySQL Workbench**
1. Ouvrir MySQL Workbench
2. Se connecter au serveur local
3. Fichier → Run SQL Script
4. Sélectionner `src/main/resources/sql/create_database.sql`
5. Exécuter

**Option C: Manuellement**
```bash
# Se connecter à MySQL
mysql -u root -p

# Dans le prompt MySQL
source src/main/resources/sql/create_database.sql
```

### Étape 3: Configurer la connexion à la base de données

Ouvrir le fichier `src/main/java/com/library/util/DatabaseConnection.java` et modifier la ligne 13:

```java
private static final String PASSWORD = "votre_mot_de_passe_mysql";
```

**Exemple:**
```java
// Si votre mot de passe MySQL est "admin123"
private static final String PASSWORD = "admin123";
```

### Étape 4: Compiler le projet

```bash
mvn clean compile
```

**Résultat attendu:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 5.234 s
```

### Étape 5: Lancer l'application

```bash
mvn javafx:run
```

**Résultat attendu:**
- Une fenêtre s'ouvre avec le titre "Système de Gestion de Bibliothèque"
- Trois onglets visibles: 📖 Livres, 👥 Membres, 🔄 Emprunts
- Message dans la console: "✓ Application démarrée avec succès!"

### Étape 6: Lancer les tests (optionnel)

```bash
mvn test
```

**Résultat attendu:**
```
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
```

---

## 🏗 Architecture du Projet

### Structure des dossiers

```
library-management/
│
├── src/
│   ├── main/
│   │   ├── java/com/library/
│   │   │   │
│   │   │   ├── controller/              # 🎮 Contrôleurs JavaFX (Couche Présentation)
│   │   │   │   ├── MainController.java           # Navigation principale
│   │   │   │   ├── LivreController.java          # Gestion des livres
│   │   │   │   ├── MembreController.java         # Gestion des membres
│   │   │   │   └── EmpruntController.java        # Gestion des emprunts
│   │   │   │
│   │   │   ├── dao/                     # 💾 Data Access Objects (Couche DAO)
│   │   │   │   ├── DAO.java                      # Interface générique
│   │   │   │   ├── LivreDAO.java                 # Interface Livre
│   │   │   │   ├── MembreDAO.java                # Interface Membre
│   │   │   │   ├── EmpruntDAO.java               # Interface Emprunt
│   │   │   │   └── impl/                         # Implémentations
│   │   │   │       ├── LivreDAOImpl.java
│   │   │   │       ├── MembreDAOImpl.java
│   │   │   │       └── EmpruntDAOImpl.java
│   │   │   │
│   │   │   ├── exception/               # ⚠️ Exceptions personnalisées
│   │   │   │   ├── ValidationException.java
│   │   │   │   ├── LivreIndisponibleException.java
│   │   │   │   ├── MembreInactifException.java
│   │   │   │   ├── LimiteEmpruntDepasseeException.java
│   │   │   │   └── EmpruntNotFoundException.java
│   │   │   │
│   │   │   ├── model/                   # 📦 Entités métier (Couche Modèle)
│   │   │   │   ├── Document.java                 # Classe abstraite
│   │   │   │   ├── Livre.java                    # extends Document
│   │   │   │   ├── Magazine.java                 # extends Document
│   │   │   │   ├── Empruntable.java              # Interface
│   │   │   │   ├── Personne.java                 # Classe abstraite
│   │   │   │   ├── Membre.java                   # extends Personne
│   │   │   │   └── Emprunt.java
│   │   │   │
│   │   │   ├── service/                 # 🔧 Logique métier (Couche Service)
│   │   │   │   ├── BibliothequeService.java      # Livres + Membres
│   │   │   │   └── EmpruntService.java           # Emprunts
│   │   │   │
│   │   │   ├── util/                    # 🛠 Utilitaires
│   │   │   │   ├── DatabaseConnection.java       # Singleton DB
│   │   │   │   ├── StringValidator.java          # Validation
│   │   │   │   └── DateUtils.java                # Gestion dates
│   │   │   │
│   │   │   └── Main.java                # ▶️ Point d'entrée
│   │   │
│   │   └── resources/
│   │       ├── fxml/                    # 🎨 Interfaces FXML
│   │       │   ├── MainView.fxml
│   │       │   ├── LivreView.fxml
│   │       │   ├── MembreView.fxml
│   │       │   └── EmpruntView.fxml
│   │       │
│   │       └── sql/
│   │           └── create_database.sql  # Script BD
│   │
│   └── test/                            # 🧪 Tests unitaires
│       └── java/com/library/
│
├── pom.xml                              # Configuration Maven
├── README.md                            # Ce fichier
└── .gitignore
```

### Architecture en Couches (Layered Architecture)

```
┌─────────────────────────────────────────────────────────┐
│   COUCHE PRÉSENTATION (JavaFX Controllers)              │
│   ├─ MainController                                     │
│   ├─ LivreController                                    │
│   ├─ MembreController                                   │
│   └─ EmpruntController                                  │
│                                                          │
│   Responsabilité: Interface utilisateur, événements     │
└─────────────────────────────────────────────────────────┘
                            ↓ ↑
┌─────────────────────────────────────────────────────────┐
│   COUCHE SERVICE (Logique Métier)                       │
│   ├─ BibliothequeService (Livres + Membres)            │
│   └─ EmpruntService (Emprunts + Pénalités)             │
│                                                          │
│   Responsabilité: Règles métier, validations, calculs   │
└─────────────────────────────────────────────────────────┘
                            ↓ ↑
┌─────────────────────────────────────────────────────────┐
│   COUCHE DAO (Accès aux Données)                        │
│   ├─ LivreDAOImpl                                       │
│   ├─ MembreDAOImpl                                      │
│   └─ EmpruntDAOImpl                                     │
│                                                          │
│   Responsabilité: CRUD, requêtes SQL, PreparedStatement │
└─────────────────────────────────────────────────────────┘
                            ↓ ↑
┌─────────────────────────────────────────────────────────┐
│   COUCHE MODÈLE (Entités)                               │
│   ├─ Document (abstraite)                               │
│   ├─ Livre, Magazine                                    │
│   ├─ Personne (abstraite)                               │
│   ├─ Membre, Emprunt                                    │
│   └─ Empruntable (interface)                            │
│                                                          │
│   Responsabilité: Structure des données                 │
└─────────────────────────────────────────────────────────┘
                            ↓ ↑
┌─────────────────────────────────────────────────────────┐
│   BASE DE DONNÉES MySQL                                 │
│   ├─ Table: livres                                      │
│   ├─ Table: membres                                     │
│   └─ Table: emprunts                                    │
└─────────────────────────────────────────────────────────┘
```

### Règles d'Architecture Importantes

✅ **Le Contrôleur NE PARLE JAMAIS directement au DAO**
```
Controller → Service → DAO → Database
```

✅ **Le Service coordonne les opérations complexes**
```java
// Exemple: Emprunt nécessite validation de 3 choses
empruntService.emprunterLivre(isbn, membreId, date);
// → Vérifie livre disponible
// → Vérifie membre actif
// → Vérifie limite 3 emprunts
// → Crée emprunt
// → Marque livre indisponible
```

✅ **Le DAO fait UNIQUEMENT du CRUD**
```java
// DAO = Simple et direct
livreDAO.findByIsbn(isbn);
livreDAO.save(livre);
livreDAO.update(livre);
```

---

## 📊 Modèle de Données (Base de données)

### Diagramme Entité-Association

```
┌─────────────────┐          ┌──────────────────┐          ┌─────────────────┐
│     LIVRES      │          │    EMPRUNTS      │          │    MEMBRES      │
├─────────────────┤          ├──────────────────┤          ├─────────────────┤
│ id (PK)         │◄─────────│ id (PK)          │─────────►│ id (PK)         │
│ isbn (UNIQUE)   │          │ id_livre (FK)    │          │ nom             │
│ titre           │          │ id_membre (FK)   │          │ prenom          │
│ auteur          │          │ date_emprunt     │          │ email (UNIQUE)  │
│ annee_pub       │          │ date_retour_prev │          │ actif           │
│ disponible      │          │ date_retour_eff  │          │ created_at      │
│ created_at      │          │ penalite         │          │ updated_at      │
│ updated_at      │          │ created_at       │          └─────────────────┘
└─────────────────┘          │ updated_at       │
                             └──────────────────┘
```

### Tables détaillées

#### 📖 Table `livres`
```sql
CREATE TABLE livres (
    id VARCHAR(50) PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    annee_publication INT NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_titre (titre),
    INDEX idx_auteur (auteur),
    INDEX idx_disponible (disponible),
    INDEX idx_isbn (isbn)
);
```

#### 👥 Table `membres`
```sql
CREATE TABLE membres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    actif BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_actif (actif),
    INDEX idx_nom_prenom (nom, prenom)
);
```

#### 🔄 Table `emprunts`
```sql
CREATE TABLE emprunts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_livre VARCHAR(50) NOT NULL,
    id_membre INT NOT NULL,
    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE NULL,
    penalite DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_emprunt_livre FOREIGN KEY (id_livre) 
        REFERENCES livres(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_emprunt_membre FOREIGN KEY (id_membre) 
        REFERENCES membres(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    
    INDEX idx_id_livre (id_livre),
    INDEX idx_id_membre (id_membre),
    INDEX idx_date_emprunt (date_emprunt),
    INDEX idx_retour_effective (date_retour_effective)
);
```

---

## ✨ Fonctionnalités de l'Application

### 📖 Module Livres (Étudiant A)

| Fonctionnalité | Description | Statut |
|----------------|-------------|--------|
| **Ajouter** | Créer un nouveau livre avec ISBN, titre, auteur, année | ✅ |
| **Modifier** | Mettre à jour les informations d'un livre existant | ✅ |
| **Supprimer** | Supprimer un livre (si non emprunté) | ✅ |
| **Rechercher** | Recherche par titre, auteur ou ISBN | ✅ |
| **Lister** | Afficher tous les livres dans un tableau | ✅ |
| **Filtrer** | Voir uniquement les livres disponibles/indisponibles | ✅ |
| **Emprunter** | Marquer un livre comme emprunté | ✅ |
| **Retourner** | Marquer un livre comme disponible | ✅ |

**Validations:**
- ISBN unique (format: 978-X-XXX-XXXXX-X)
- Titre et auteur obligatoires (1-255 caractères)
- Année valide (positive, ≤ année actuelle)

---

### 👥 Module Membres (Étudiant B)

| Fonctionnalité | Description | Statut |
|----------------|-------------|--------|
| **Enregistrer** | Créer un nouveau membre avec nom, prénom, email | ✅ |
| **Modifier** | Mettre à jour les informations d'un membre | ✅ |
| **Supprimer** | Supprimer un membre (si aucun emprunt en cours) | ✅ |
| **Activer/Désactiver** | Changer le statut d'un membre | ✅ |
| **Rechercher** | Recherche par nom, prénom ou email | ✅ |
| **Lister** | Afficher tous les membres | ✅ |
| **Filtrer** | Voir uniquement les membres actifs | ✅ |
| **Historique** | Voir tous les emprunts d'un membre | ✅ |

**Validations:**
- Email unique et valide (format: xxx@xxx.xxx)
- Nom et prénom obligatoires (2-100 caractères)
- Impossible de désactiver un membre avec emprunts en cours

---

### 🔄 Module Emprunts (Étudiant C)

| Fonctionnalité | Description | Statut |
|----------------|-------------|--------|
| **Emprunter** | Créer un nouvel emprunt | ✅ |
| **Retourner** | Enregistrer le retour d'un livre | ✅ |
| **Calculer pénalité** | Calcul automatique des frais de retard | ✅ |
| **Lister emprunts** | Afficher tous les emprunts | ✅ |
| **Emprunts en cours** | Voir les emprunts non retournés | ✅ |
| **Emprunts en retard** | Identifier les retards | ✅ |
| **Statistiques** | Générer des stats d'emprunts | ✅ |

**Règles métier:**
- Maximum 3 emprunts simultanés par membre
- Durée d'emprunt: 14 jours
- Pénalité: 5€ par jour de retard
- Seuls les membres actifs peuvent emprunter
- Livre doit être disponible

---

## 🔒 Règles Métier Implémentées

### 1. Limite d'emprunts
```java
// Un membre ne peut avoir que 3 emprunts en cours maximum
if (nombreEmpruntsEnCours >= 3) {
    throw new LimiteEmpruntDepasseeException(membreId, nombreEmpruntsEnCours);
}
```

### 2. Durée d'emprunt
```java
// Par défaut: 14 jours
LocalDate dateRetourPrevue = LocalDate.now().plusDays(14);
```

### 3. Calcul des pénalités
```java
// 5€ par jour de retard
long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
BigDecimal penalite = BigDecimal.valueOf(joursRetard * 5.0);
```

### 4. Validation membre actif
```java
// Membre inactif ne peut pas emprunter
if (!membre.isActif()) {
    throw new MembreInactifException(membreId);
}
```

### 5. Validation disponibilité livre
```java
// Livre doit être disponible pour emprunt
if (!livre.isDisponible()) {
    throw new LivreIndisponibleException(isbn);
}
```

### 6. Intégrité référentielle
```sql
-- Impossible de supprimer un livre/membre avec emprunts
CONSTRAINT fk_emprunt_livre FOREIGN KEY (id_livre) 
    REFERENCES livres(id) ON DELETE RESTRICT
```

---

## 🎨 Design Patterns Utilisés

### 1. **Singleton Pattern** (DatabaseConnection)
```java
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    
    // Double-Checked Locking (thread-safe)
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}
```
**Avantage:** Une seule connexion DB partagée = meilleure performance

---

### 2. **DAO Pattern** (Data Access Object)
```java
public interface DAO<T> {
    void save(T entity) throws SQLException;
    T findById(Object id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(Object id) throws SQLException;
}
```
**Avantage:** Séparation claire entre logique métier et accès données

---

### 3. **MVC Pattern** (Model-View-Controller)
```
Model: Livre.java, Membre.java, Emprunt.java
View: LivreView.fxml, MembreView.fxml
Controller: LivreController.java, MembreController.java
```
**Avantage:** Séparation des responsabilités

---

### 4. **Factory Method** (Exceptions personnalisées)
```java
public static ValidationException champManquant(String fieldName) {
    return new ValidationException("Le champ '" + fieldName + "' est obligatoire.");
}
```
**Avantage:** Création d'objets exception standardisée

---

### 5. **Strategy Pattern** (Interface Empruntable)
```java
public interface Empruntable {
    boolean isDisponible();
    void emprunter();
    void retourner();
}
```
**Avantage:** Polymorphisme pour différents types de documents

---

## 🧪 Tests et Qualité du Code

### Exécuter les tests
```bash
# Tous les tests
mvn test

# Tests d'une classe spécifique
mvn test -Dtest=LivreDAOTest

# Avec rapport de couverture
mvn clean test jacoco:report
```

### Objectifs de couverture
- ✅ Tests unitaires: > 80% de couverture
- ✅ Tests d'intégration: Tous les services
- ✅ Tests JavaFX: Controllers principaux

### Structure des tests
```
src/test/java/com/library/
├── dao/
│   ├── LivreDAOTest.java
│   ├── MembreDAOTest.java
│   └── EmpruntDAOTest.java
├── service/
│   ├── BibliothequeServiceTest.java
│   └── EmpruntServiceTest.java
└── util/
    ├── StringValidatorTest.java
    └── DateUtilsTest.java
```

---

## 📝 Données de Test

La base de données est pré-remplie avec des données de test:

### 📖 10 Livres classiques
1. Le Petit Prince - Antoine de Saint-Exupéry (1943)
2. L'Étranger - Albert Camus (1942)
3. 1984 - George Orwell (1949)
4. Les Misérables (Tome 1) - Victor Hugo (1862) **[EMPRUNTÉ]**
5. Le Seigneur des Anneaux - J.R.R. Tolkien (1954)
6. Harry Potter à l'école des sorciers - J.K. Rowling (1997) **[EMPRUNTÉ]**
7. Le Comte de Monte-Cristo - Alexandre Dumas (1844)
8. Germinal - Émile Zola (1885)
9. Madame Bovary - Gustave Flaubert (1857)
10. Les Fleurs du mal - Charles Baudelaire (1857)

### 👥 4 Membres (l'équipe du projet)
1. Amine Benatti - aminebenatti314@gmail.com
2. Idriss Jabri - Idriss.jabri@esi.ac.ma
3. Hamza Ahaddad - Hamza.ahaddad@esi.ac.ma
4. Ilyas Jaafar - ilyas.jaafar@esi.ac.ma

### 🔄 5 Emprunts
- 2 emprunts en cours
- 3 emprunts terminés (dont 1 avec pénalité)

---

## 🐛 Dépannage (Troubleshooting)

### Problème: Application ne démarre pas

**Erreur:** `Error: JavaFX runtime components are missing`

**Solution:**
```bash
# Vérifier que JavaFX est bien dans pom.xml
mvn dependency:tree | grep javafx

# Si manquant, relancer
mvn clean install
```

---

### Problème: Erreur de connexion MySQL

**Erreur:** `SQLException: Access denied for user 'root'@'localhost'`

**Solutions:**
1. Vérifier le mot de passe dans `DatabaseConnection.java`
2. Vérifier que MySQL est démarré:
   ```bash
   # Windows
   net start MySQL80
   
   # Linux/Mac
   sudo systemctl start mysql
   ```
3. Tester la connexion:
   ```bash
   mysql -u root -p
   ```

---

### Problème: FXML LoadException

**Erreur:** `javafx.fxml.LoadException: ... location is required`

**Solutions:**
1. Vérifier que les fichiers FXML sont dans `src/main/resources/fxml/`
2. Vérifier les chemins dans le code:
   ```java
   getClass().getResource("/fxml/MainView.fxml")  // ← Le / est important!
   ```
3. Nettoyer et recompiler:
   ```bash
   mvn clean compile
   ```

---

### Problème: Controllers non trouvés

**Erreur:** `javafx.fxml.LoadException: Controller class cannot be found`

**Solutions:**
1. Vérifier le package dans FXML:
   ```xml
   fx:controller="com.library.controller.LivreController"
   ```
2. Vérifier que la classe existe dans `src/main/java/com/library/controller/`
3. Recompiler:
   ```bash
   mvn clean compile
   ```

---

### Problème: Base de données vide

**Symptôme:** Aucun livre/membre n'apparaît dans l'application

**Solution:**
```bash
# Recréer la base de données
mysql -u root -p < src/main/resources/sql/create_database.sql
```

---

### Problème: Erreur de compilation Maven

**Erreur:** `[ERROR] Failed to execute goal ... compiler:compile`

**Solutions:**
```bash
# 1. Nettoyer complètement
mvn clean

# 2. Mettre à jour les dépendances
mvn clean install -U

# 3. Si erreur persiste, supprimer le dossier target
rm -rf target/
mvn clean compile
```

---



## 🤝 Contribution et Workflow Git

### Structure des branches
```
main          ← Code stable (production)
  ↑
dev           ← Développement (intégration)
  ↑
  ├─ feature/livres      ← Idriss
  ├─ feature/membres     ← Ilyas
  ├─ feature/emprunts    ← Hamza
  └─ feature/infra       ← Amine
```

