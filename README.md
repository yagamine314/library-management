# 📚 Système de Gestion de Bibliothèque

Application Java avec JavaFX pour gérer une bibliothèque.

## 🛠️ Technologies

- **Java 17**
- **JavaFX 21** 
- **MySQL 8** 
- **Maven 3.9**
- **JUnit 5**

## 📦 Installation

### 1. Cloner le projet
```bash
git clone [URL_REPO]
cd library-management
```

### 2. Créer la base de données
```bash
mysql -u root -p < src/main/resources/sql/create_database.sql
```

### 3. Configurer la connexion
Modifier `src/main/java/com/library/util/DatabaseConnection.java` ligne 13:
```java
private static final String PASSWORD = "votre_mot_de_passe";
```

### 4. Compiler
```bash
mvn clean compile
```

### 5. Lancer les tests
```bash
mvn test
```

## 🏗️ Architecture
```
┌─────────────────────────────┐
│   PRÉSENTATION (JavaFX)     │
├─────────────────────────────┤
│   SERVICE (Logique Métier)  │
├─────────────────────────────┤
│   DAO (Accès aux Données)   │
├─────────────────────────────┤
│   MODÈLE (Entités)          │
└─────────────────────────────┘
```




### Tests:
```bash
Tests run: 3, Failures: 0, Errors: 0
```

**Status:** ✅ Phase 1 terminée | **Version:** 1.0-SNAPSHOT