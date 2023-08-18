<!-- https://github.com/othneildrew/Best-README-Template -->

[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://hexagonalchess-api.zucchi.dev/swagger-ui/index.html">
    <img src="images/logo.svg" alt="Logo" width="300px" style="max-width: 100%;">
  </a>

<h3>Hexagonal Chess - Backend</h3>

  <p>
    Immerse yourself in the strategic world of Gliński's variant chess with my project. The Backend is powered by Spring Boot, showcasing my expertise in web development.
    <br />
    <br />
    <a href="https://hexagonalchess-api.zucchi.dev/swagger-ui/index.html">RESTful API doc</a>
    ·
    <a href="https://github.com/sisimomo/hexagonalChess-backend/issues">Report Bug</a>
    ·
    <a href="https://github.com/sisimomo/hexagonalChess-backend/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

<div align="center">
  <a href="https://hexagonalchess-api.zucchi.dev/swagger-ui/index.html">
    <img src="images/screenshot.png" alt="Logo" width="500px" style="max-width: 100%;margin: auto;">
  </a>
</div>


As a passionate web developer, I constantly seek out intriguing challenges. Inspired by a
[YouTube video by C.G.P. Grey titled 'Can Chess, with Hexagons?'](https://youtu.be/bgR3yESAEVE), I
dove into the world of hexagonal chess and was immediately captivated by its unique complexity and
possibilities. This fascination drove me to create a digital version of the Gliński's variant,
blending my love for coding with my newfound interest for this chess variant.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [![IntelliJ-IDEA][IntelliJ-IDEA-shield]](https://www.jetbrains.com/idea/)
* [![MariaDB][MariaDB-shield]](https://mariadb.org/)
* [Keycloak](https://www.keycloak.org/)
* [![Spring][Spring-shield]](https://spring.io/)
* [![Java][Java-shield]](https://www.java.com/en/)
* [![Gradle][Gradle-shield]](https://gradle.org/)
* [![Swagger][Swagger-shield]](https://swagger.io/)
* [![Hibernate][Hibernate-shield]](https://hibernate.org/)
* [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph)
* [JPA](https://jakarta.ee/specifications/persistence/3.0/)
* [![Junit5][Junit5-shield]](https://junit.org/junit5/)
* [Lombok](https://projectlombok.org/)
* [MapStruct](https://mapstruct.org/)
* [JPA 2 Metamodel Generator](https://docs.jboss.org/hibernate/orm/5.4/topical/html_single/metamodelgen/MetamodelGenerator.html)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->

## Local development environment

To get a local copy up and running, follow these simple steps.

### Prerequisites

Before diving into the installation section, make sure your system meets the following
prerequisites:

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://www.java.com)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [npm](https://www.npmjs.com/)
- [Angular CLI](https://cli.angular.io/)

### Installation

#### 1. Get Local Docker Compose Running

1.1. **Clone the Docker Compose Repository:**

Start by cloning the Docker Compose repository using the following command:

```sh
git clone https://github.com/sisimomo/hexagonalChess-docker-compose.git
```

1.2. **Navigate to the Local Folder:**

Move into the local folder within the cloned repository:

```sh
cd ./hexagonalChess-docker-compose/local
```

1.3. **Configure Environment Variables:**

Duplicate the `.env.sample` file and rename it to `.env`:

```sh
cp ./.env.sample  ./.env
```

Customize the variables in the `.env` file to your preferences.

1.4. **Initiate Docker Compose:**

Start docker containers using Docker Compose:

```sh
docker-compose up -d
```

#### 2. Get the Backend Running

2.1. **Clone the Backend Repository:**

Clone the backend repository using the following command:

```sh
git clone https://github.com/sisimomo/hexagonalChess-backend.git
```

2.2. **Open in IntelliJ:**

Open the cloned repository folder using IntelliJ IDEA.

2.3. **Update Environment Variables:**

Adjust the environment variables according to your `.env` modifications in the "Run/Debug
configuration" named "HexagonalChessApplication" located in the `.run` folder within the cloned
repository.

2.4. **Start the Application:**

Launch the application using the "Run/Debug configuration" named "HexagonalChessApplication" in
IntelliJ.

#### 3. Get the Frontend Running

3.1. **Clone the Frontend Repository:**

Clone the frontend repository using the following command:

```sh
 git clone https://github.com/sisimomo/hexagonalChess-frontend.git
```

3.2. **Navigate to the Frontend Folder:**

Change your working directory to the frontend folder:

```sh
cd ./hexagonalChess-frontend
```

3.3. **Install Dependencies:**

Install project dependencies using npm:

```sh
npm install
```

3.4. **Run the Project:**

Start the project using Angular CLI:

```sh
ng serve
```

#### 4. Enjoy!

Feel free to explore the code and provide suggestions for enhancement by creating feature requests
through new [GitHub issues][issues-url].

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- ROADMAP -->

## Roadmap

- [ ] Send STOMP error message when an error occurred.
- [ ] Complete Chess Engine unit tests.
    - [x] Coordinate class.
    - [x] Piece abstract class.
    - [ ] Pawn class.
    - [ ] King class.
    - [ ] Game class.
- [ ] Create a RESTful endpoint returning all previously played games.
- [ ] Add a chat in the game page.

See the [open issues][issues-url] for a full list of
proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->

## Contact

[![LinkedIn][linkedin-shield]][linkedin-url]

Project Urls:

* [Backend](https://hexagonalchess-api.zucchi.dev/)
* [Frontend](https://hexagonalchess.zucchi.dev/)
* [Identity provider](https://hexagonalchess-auth.zucchi.dev/realms/HexagonalChess/account/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[forks-shield]: https://img.shields.io/github/forks/sisimomo/hexagonalChess-backend.svg?style=for-the-badge

[forks-url]: https://github.com/sisimomo/hexagonalChess-backend/network/members

[stars-shield]: https://img.shields.io/github/stars/sisimomo/hexagonalChess-backend.svg?style=for-the-badge

[stars-url]: https://github.com/sisimomo/hexagonalChess-backend/stargazers

[issues-shield]: https://img.shields.io/github/issues/sisimomo/hexagonalChess-backend.svg?style=for-the-badge

[issues-url]: https://github.com/sisimomo/hexagonalChess-backend/issues

[license-shield]: https://img.shields.io/github/license/sisimomo/hexagonalChess-backend.svg?style=for-the-badge

[license-url]: https://github.com/sisimomo/hexagonalChess-backend/blob/master/LICENSE.txt

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555

[linkedin-url]: https://linkedin.com/in/simon-vallieres-358555187

[Spring-shield]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white

[Java-shield]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white

[Gradle-shield]: https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white

[Swagger-shield]: https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white

[Junit5-shield]: https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white

[MariaDB-shield]: https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white

[Hibernate-shield]: https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white

[IntelliJ-IDEA-shield]: https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white