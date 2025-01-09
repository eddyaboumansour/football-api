# API de Gestion d'une Équipe de Football

## Énoncé

On aimerait pouvoir créer une API pour gérer l’équipe de football de Nice en Ligue 1. Le directeur sportif du club
souhaite répertorier en base de données la liste de ses joueurs et le budget de l’équipe afin de mieux gérer le marché
des transferts à venir.

### Consignes

Développer une API REST avec les fonctionnalités suivantes (l’ajout de fonctionnalités supplémentaires sera considéré
comme un bonus) :

- Retourner une liste paginée d’équipes contenant chacune une liste de joueurs. Cette liste pourra être triée côté
  serveur (tri par nom d’équipe, acronyme et budget).

- Ajouter une équipe avec ou sans joueurs associés (tous les autres champs sont obligatoires).

### Environnement technique

- **Framework** : Spring Boot

- **ORM** : Hibernate

- **Base de données** : Au choix (embarqué, PostgreSQL, Oracle, etc.)

### Modèle de données

- **Équipe** : `[id, name, acronym, joueurs, budget]`

- **Joueur** : `[id, name, position]`

### Évaluation

Les éléments suivants seront pris en compte lors de l’évaluation du projet :

- Architecture

- Choix techniques

- Documentation (Commentaires / Javadoc / Logs)

- Tests unitaires et d’intégration

----------

## Description de l'API

Cette API Spring Boot permet de gérer des équipes de football et leurs joueurs. Elle propose des endpoints pour lister
les équipes, ajouter une nouvelle équipe, supprimer une équipe, et récupérer les joueurs non assignés.

### Endpoints

#### 1. `/team/list` (GET)

- **Description** : Récupère une liste paginée et triée d’équipes.

- **Paramètres** :

    - `sort` : Tri (nom, acronyme, budget)

    - `pageRequest` : Pagination (numéro de page, taille)

- **Réponse** : Une page d’équipes représentées sous forme de `TeamDTO`.

- **Exemple** :

  ```
  GET /team/list?sort=name&pageRequest.page=0&pageRequest.size=10
  ```

#### 2. `/team` (POST)

- **Description** : Ajoute une nouvelle équipe.

- **Corps de la requête** : Un objet `TeamCreateDTO` contenant les détails de l’équipe.

- **Réponse** : L’équipe créée sous forme de `TeamDTO`.

- **Exemple** :

  ```
  POST /team
  Content-Type: application/json
  {
    "name": "OGC Nice",
    "acronym": "OGCN",
    "budget": 50000000,
    "players": [
      { "name": "Joueur1", "position": "Attaquant" }
    ]
  }
  ```

#### 3. `/team/{id}` (DELETE) (Bonus)

- **Description** : Supprime une équipe par son ID et désaffecter les joueurs de l'équipe

- **Exemple** :

  ```
  DELETE /team/1
  ```

#### 4. `/player/available-players` (GET) (Bonus)

- **Description** : Liste les joueurs non assignés à une équipe. Sert à afficher une liste des joueurs non assignés à
  une équipe dans le frontend

- **Réponse** : Une liste de `PlayerDTO`.

- **Exemple** :

  ```
  GET /player/available-players
  ```

----------

## Configuration de la Base de Données

- **PostgreSQL** :

  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
  spring.datasource.username=postgres
  spring.datasource.password=postgres
  ```

- **Liquibase** : Active par défaut pour gérer les migrations de schéma. Désactivable via
  `spring.liquibase.enabled=false` pour optimiser le démarrage de l'application.

## Choix Techniques

----------


### **1. Spring Boot 3.1.5**

J’ai choisi **Spring Boot** pour simplifier le développement d’applications Java, notamment des API REST. La version
3.1.5 apporte des améliorations de performance et une meilleure compatibilité avec Jakarta EE 10.

#### 🔹 **Cas d’usage :**

- **Développement d’API REST** : Création de microservices exposant des endpoints REST avec des contrôleurs Spring (
  `@RestController`).

----------

### **2. JPA (Java Persistence API)**

J’ai choisi **JPA** pour gérer la persistance des objets métier dans une base de données relationnelle. Il s’agit d’une
spécification standard généralement implémentée par Hibernate.

#### 🔹 **Cas d’usage :**

- **Gestion des entités** : Mapping des objets Java vers des tables SQL grâce aux annotations comme `@Entity`, `@Table`,
  `@Column`.
- **Requêtes simplifiées** : Utilisation de `@Query` avec Spring Data JPA pour exécuter des requêtes spécifiques sans
  écrire de SQL complexe.
- **Gestion des relations** : Définition des relations entre entités (`@OneToMany`, `@ManyToOne`, `@ManyToMany`) pour
  structurer la base de données efficacement.
- **Pagination et tri** : Utilisation de `Pageable` pour récupérer des résultats paginés et triés sans surcharge de la
  base de données.
- **Gestion des transactions** : Utilisation des annotations @Transactional pour garantir l’intégrité des données lors
  des opérations critiques.
- **Utilisation de `EntityGraph` pour éviter le problème "N + 1"** : Cela permet de charger les joueurs associés en une
  seule requête lors de la récupération des équipes. La propriété `spring.jpa.show-sql` est désactivée par défaut.
- **Création d’un index sur le champ `team_id`** : Cela permet d’effectuer des requêtes rapides pour obtenir les joueurs
  par équipe et permettre de facilement set le team_id à null lors de la suppression d’une équipe.

----------

### **3. Hibernate**

J’ai choisi **Hibernate** comme implémentation de JPA pour faciliter la gestion des données en évitant d’écrire des
requêtes SQL complexes.

#### 🔹 **Cas d’usage :**

- **Persistance des entités** : Transformation automatique des objets Java en tables SQL et inversement.
- **Optimisation des performances** : Utilisation des caches de premier et second niveau pour minimiser les accès à la
  base de données.
- **Gestion des transactions** : Utilisation des annotations `@Transactional` pour garantir l’intégrité des données lors
  des opérations critiques.
- **Validation des paramètres avec Hibernate Validation** : Hibernate Validation est utilisé pour vérifier la validité
  des paramètres des requêtes.

----------

### **4. Base de Données PostgreSQL**

J’ai choisi **PostgreSQL** comme base de données relationnelle pour sa fiabilité, sa scalabilité et son intégration
native avec Spring Data JPA.

#### 🔹 **Cas d’usage :**

- **Stockage des données applicatives** : Utilisation avec Hibernate pour gérer les relations entre entités et les
  transactions.

----------

### **5. Liquibase**

J’ai choisi **Liquibase** pour versionner et gérer les évolutions du schéma de base de données de manière automatisée,
évitant les interventions manuelles.

#### 🔹 **Cas d’usage :**

- **Automatisation des migrations** : Définition des changements de structure via des fichiers YAML, XML ou SQL
  appliqués automatiquement (Les fichiers sont présent dans db.changelog).
- **Rollback des versions** : Restauration d’une version précédente en cas d’erreur de déploiement sur un environnement
  à travers le changelog.
- **Suivi des changements** : Chaque modification est enregistrée dans une table de suivi, garantissant une traçabilité
  complète des évolutions de la base de données.
- **Synchronisation des bases sur plusieurs environnements** : Assurer que la base de données en production, en test et
  en développement reste cohérente.

----------

### **6. MapStruct**

J’ai choisi **MapStruct** pour automatiser le mapping entre entités et DTOs avec PlayerMapper et TeamMapper, réduisant
ainsi le code répétitif et accélérant le développement.

#### 🔹 **Cas d’usage :**

- **Conversion des entités vers DTOs** : Transformation des objets métier en objets adaptés aux échanges via API sans
  code répétitif.
- **Mapping conditionnel** : Filtrage de certains champs en fonction des besoins métier.
- **Optimisation des performances** : Génération de code natif au lieu d’utiliser la réflexion, améliorant ainsi la
  rapidité des mappings.

----------

### **7. Lombok**

J’ai choisi **Lombok** pour réduire le code standard (boilerplate) en générant automatiquement des méthodes comme les
getters, setters et constructeurs grâce à des annotations.

#### 🔹 **Cas d’usage :**

- **Réduction du code répétitif** : Suppression des déclarations manuelles de `toString()`, `equals()` et `hashCode()`.
- **Utilisation des `@Builder` et `@AllArgsConstructor`** : Construction d’objets complexes avec une syntaxe fluide.
- **Amélioration de la lisibilité du code** : Moins de code technique, rendant les classes plus compréhensibles.

----------

### **8. SpringDoc OpenAPI**

J’ai choisi **SpringDoc OpenAPI** pour documenter les API REST et permettre de tester facilement les endpoints via une
interface Swagger.

#### 🔹 **Cas d’usage :**

- **Documentation dynamique** : Exposition des endpoints avec leurs paramètres et réponses sous une interface
  interactive.
- **Tests rapides des API** : Exécution directe des requêtes depuis Swagger UI sans passer par un client externe (
  Postman, cURL).
- **Génération de spécifications OpenAPI** : Production automatique de fichiers JSON ou YAML conformes aux standards
  d’API documentation.

----------

### **9. JUnit et Mockito**

J’ai choisi **JUnit et Mockito** pour tester mon application en isolant les composants et en simulant les dépendances.

#### 🔹 **Cas d’usage :**

- **Tests unitaires des services métier** : Vérification du bon fonctionnement des méthodes de services en simulant
  leurs dépendances.
- **Mocking des dépendances** : Utilisation de Mockito pour tester une classe sans interagir avec des bases de données
  réelles ou d’autres services.
- **Tests d’intégration** : Validation du comportement global des API en s’assurant que les différentes couches
  interagissent correctement.
- **Scénario couvert** : Création d’une équipe avec des joueurs, puis suppression de l’équipe.

## Installation et Exécution

### 1. Prérequis

- Java 17

- Maven

- PostgreSQL (ou base embarquée)

### 2. Cloner le projet

```
git clone https://url.du.dépôt.git
cd football-api
```

### 3. Construire et Exécuter l’application

```
mvn clean install
mvn spring-boot:run
```

### 4. Accès à l’API

- API : `http://localhost:8080`

- Documentation Swagger : `http://localhost:8080/swagger-ui.html`
- Une simple interface frontend (Bonus) : `http://localhost:8080/index.html`

