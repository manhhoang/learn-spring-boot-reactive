## Spring Boot

curl -u test:password http://localhost:8080/get-all

### Usage

- Run the application and go on http://localhost:8080/
- Use the following urls to invoke controllers methods and see the interactions
  with the database:
    * `/get-all`: Return all super heroes
    * `/create?name=[name]&pseudonym=[pseudonym]&publisher=[publisher]&skill=[skill]&allies=[allies]&dateOfAppearance=[dateOfAppearance]`: create a new super hero with an auto-generated id and name as passed values.
    * `/delete?id=[id]`: delete the super hero with the passed id.
    * `/get-by-name?name=[name]`: retrieve the detail information for the super hero with the 
      passed name.
    * `/update?id=[id]&name=[name]`: update the name for the super hero indentified by the passed id.

### Build and run

#### Configurations

Open the `application.properties` file and set your own configurations.

#### Prerequisites

- Java 8
- Maven 3

#### From terminal

Go on the project's root folder, then type:

    $ mvn spring-boot:run

#### From Eclipse

Import as *Existing Maven Project* and run it as *Spring Boot App*.

#### From Docker Toolbox
docker run -p 8080:8080 -t manhhoang/spring-boot