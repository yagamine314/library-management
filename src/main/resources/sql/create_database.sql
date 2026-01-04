-- ============================================
-- Système de Gestion de Bibliothèque
-- ============================================

DROP DATABASE IF EXISTS library_db;
CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_db;

-- ============================================
-- TABLE: livres
-- ============================================

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
    INDEX idx_isbn (isbn),
    INDEX idx_id (id)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: membres
-- ============================================
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
) ENGINE=InnoDB;

-- ============================================
-- TABLE: emprunts
-- ============================================
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

    CONSTRAINT fk_emprunt_livre
        FOREIGN KEY (id_livre) REFERENCES livres(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_emprunt_membre
        FOREIGN KEY (id_membre) REFERENCES membres(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    INDEX idx_id_livre (id_livre),
    INDEX idx_id_membre (id_membre),
    INDEX idx_date_emprunt (date_emprunt),
    INDEX idx_retour_effective (date_retour_effective)
) ENGINE=InnoDB;

-- ============================================
-- DONNÉES DE TEST - LIVRES
-- ============================================
INSERT INTO livres (id, isbn, titre, auteur, annee_publication, disponible) VALUES
('1', '978-2-07-061275-8', 'Le Petit Prince', 'Antoine de Saint-Exupéry', 1943, TRUE),
('2', '978-2-07-036002-4', 'L''Étranger', 'Albert Camus', 1942, TRUE),
('3', '978-2-07-036822-8', '1984', 'George Orwell', 1949, TRUE),
('4', '978-2-253-09633-7', 'Les Misérables (Tome 1)', 'Victor Hugo', 1862, FALSE),
('5', '978-2-07-513404-0', 'Le Seigneur des Anneaux (Intégrale)', 'J.R.R. Tolkien', 1954, TRUE),
('6', '978-2-07-058462-8', 'Harry Potter à l''école des sorciers', 'J.K. Rowling', 1997, FALSE),
('7', '978-2-253-00427-1', 'Le Comte de Monte-Cristo (Tome 1)', 'Alexandre Dumas', 1844, TRUE),
('8', '978-2-253-00542-1', 'Germinal', 'Émile Zola', 1885, TRUE),
('9', '978-2-253-00286-4', 'Madame Bovary', 'Gustave Flaubert', 1857, TRUE),
('10', '978-2-253-01410-2', 'Les Fleurs du mal', 'Charles Baudelaire', 1857, TRUE);

-- ============================================
-- DONNÉES DE TEST - MEMBRES
-- ============================================
INSERT INTO membres (nom, prenom, email, actif) VALUES
('Benatti', 'Amine', 'aminebenatti314@gmail.com', TRUE),
('Jabri', 'Idriss', 'Idriss.jabri@esi.ac.ma', TRUE),
('Ahaddad', 'Hamza', 'Hamza.ahaddad@esi.ac.ma', TRUE),
('Jaafar', 'Ilyas', 'ilyas.jaafar@esi.ac.ma', TRUE);

-- ============================================
-- DONNÉES DE TEST - EMPRUNTS
-- ============================================
INSERT INTO emprunts (id_livre, id_membre, date_emprunt, date_retour_prevue, date_retour_effective, penalite) VALUES
('4', 1, '2024-12-01', '2024-12-15', NULL, 0.00),
('6', 2, '2024-12-10', '2024-12-24', NULL, 0.00),
('1', 1, '2024-11-01', '2024-11-15', '2024-11-14', 0.00),
('2', 2, '2024-11-05', '2024-11-19', '2024-11-25', 30.00),
('3', 3, '2024-11-10', '2024-11-24', '2024-11-23', 0.00);

-- ============================================
-- VÉRIFICATION
-- ============================================
SELECT 'Database created' AS message;
SELECT COUNT(*) AS total_livres FROM livres;
SELECT COUNT(*) AS total_membres FROM membres;
SELECT COUNT(*) AS total_emprunts FROM emprunts;