## Spring Boot


### Usage

- Hosted this application on Docker in AWS cloud. 
- List all super heroes: https://ec2-54-251-124-145.ap-southeast-1.compute.amazonaws.com:8443/get-all
- List one super hero: https://ec2-54-251-124-145.ap-southeast-1.compute.amazonaws.com:8443/get-by-name?name=Batman
- Create new super hero: https://ec2-54-251-124-145.ap-southeast-1.compute.amazonaws.com:8443/create?name=Spiderman&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=&dateOfAppearance=1980-09-01
- Username: test
- Password:  password
- Use the following urls to invoke controllers methods and see the interactions
  with the database:
    * `/get-all`: Return all super heroes
    * `/create?name=[name]&pseudonym=[pseudonym]&publisher=[publisher]&skill=[skill]&allies=[allies]&dateOfAppearance=[dateOfAppearance]`: create a new super hero with an auto-generated id and name as passed values.
    * `/delete?id=[id]`: delete the super hero with the passed id.
    * `/get-by-name?name=[name]`: retrieve the detail information for the super hero with the 
      passed name.
    * `/update?id=[id]&name=[name]`: update the name for the super hero indentified by the passed id.
    
### Frameworks & Source codes

#### Frameworks

- Spring Boot: Managing all restful api working well with Docker for Microservices architecture.
- Spring Security: For authentication.
- Spring Test: Managing unit & integration test.
- Spring Data & JPA & Hibernate: Managing data access layer.
- H2 Database: Embeded relational database of this app.
- AWS EC2 & Docker: Cloud infrastructure of this app.

#### Source codes

- src/main/java/com.jd.Application.java: Main application file.
- src/main/java/com.jd.controllers.SuperHeroController.java: Controller to manage super hero. 
- src/main/java/com.jd.controllers.UserController.java: Controller to manage users for authentication. 
- src/main/java/com.jd.models.SuperHero.java: super hero entity.
- src/main/java/com.jd.models.SuperHeroDao.java: Data access layer of super hero table.
- src/main/java/com.jd.models.Skill.java: skill of super hero entity, one super hero can have many skills. This is one-to-many relationship.
- src/main/java/com.jd.models.User.java: user entity.
- src/main/java/com.jd.models.UserDao.java: Data access layer of user table.
- src/main/docker/Dockerfile: Docker file.
- src/main/resources/application.properties: Spring Boot configuration file.

#### Testing

- src/test/java/com.jd.controllers.SuperHeroControllerIntegrationTest.java: Integration test.
- src/test/java/com.jd.controllers.SuperHeroControllerUnitTest.java: Unit test.

### Build and run

#### Configurations

Open the `application.properties` file and set your own configurations.

#### Prerequisites

- Java 8
- Maven 3

#### From Eclipse

Import as *Existing Maven Project* and run it as *Spring Boot App*.

#### From Docker
docker run -p 8443:8443 -t manhhoang/spring-boot