# Person-Organizer


### WHO? 
Hi, I'm Robin and a backend software developer from germany.

---
### WHAT?
This is a simple project to demonstrate concepts and technologies in Spring Boot with Java.
You can store and retrieve information about people.
It's not intended to be used in production!

---
### HOW?

Now you see the general architecture and technologies that are involved:

Maven: Multi module project
  - backend: main service to store and retrieve personal data
  - client: simple service to trigger the backend
  - lib: shared library between backend and client
  - [frontend](_docu/readme/Frontend.md): 3 different basic implementations

Backend:
- [API](_docu/readme/Api.md): REST/OpenAPI, GraphQL, Kafka 
- Service: Business Logic, Mapping
- [Database](_docu/readme/Database.md): Postgres, Liquibase, QueryDSL, MongoDB 
- Cross Cutting:
  - AOP: Aspect, Advice, Pointcut, JoinPoint, Custom Annotation
  - Configuration: Profiles, Properties, CommandLineRunner
  - Error Handling: Custom Exceptions, Exception Handler
  - Mapping: MapStruct Interfaces
  - Validation: Requests in API or Mapper
  - [Security](_docu/readme/Security.md): Http Basic, User in Database
- Testing: 
  - Unit: JUnit, Mockito, AssertJ, Hamcrest, DBRider
  - Integration: RestAssured, Cucumber, TestContainer
  - Client: Wiremock
  - and nativ Spring Boot concepts
- Monitoring: Sleuth, Zipkin, Jaeger, Log4j2, Actuator
- Infrastructure: Docker Compose to boot up Databases, Kafka and Zipkin/Jaeger
- Other resources: 
  - Insomnia collection to trigger the backend api with requests
  - Python scripts to retrieve data from the backend api or database

Flowchart (via yEd):
![flow_overall](/_docu/flow/flow_overall.png)

---
### Check!

Settings / Plugins in Intellij:
- OpenAPI
- Graphql
- Markdown
- Docker
- Kafkalytic
- GitHub Copilot

Project Structure in Intellij:
- check used modules
- select correct jdk (11)

Before you run the project:
- load dependencies from maven
- compile files and generate classes
- check active spring profiles
- use default profile with h2/flapdoodle (no kafka)
- or use databases & kafka profile
- start docker-compose files to boot up infrastructure

Start backend on port 8080:
- via Run-Config in Intellij
- via Maven in terminal: `mvn spring-boot:run --projects backend`

Options to trigger the backend:
- old school via browser
- with the provided insomnia collection
- run client scheduler (start server on port 8081)
- future: use frontend (start server and access homepage)

Check results:
- API:
  - Insomnia (external tools)
  - Swagger-UI (auto generated)
  - Graphiql (auto generated)
- Database (external tools):
  - DBeaver (Postgres)
  - Studio 3T (MongoDB)
- Monitoring (via Docker):
  - Zipkin/Jaeger

---
### THANKS!

The following resources were used to create this project and learn continuously:
- [Baeldung](https://github.com/eugenp)
- [Dan Vega](https://github.com/danvega/danvega)
- [Mike Møller Nielsen](https://github.com/ekim197711)
- [in28minutes](https://www.youtube.com/user/rithustutorials)
- [Amigoscode](https://www.youtube.com/c/amigoscode)
- [Laur Spilca](https://www.youtube.com/c/LaurentiuSpilca)
- [Java Brains](https://www.youtube.com/c/JavaBrainsChannel)
- [Java Guides](https://www.youtube.com/c/JavaGuides)
- [Spring Academy](https://www.youtube.com/c/SpringAcademy)
- [Spring Framework Guru](https://www.youtube.com/c/SpringframeworkGuru)
- [SpringDeveloper](https://www.youtube.com/user/SpringSourceDev)
- [Spring I/O](https://www.youtube.com/c/SpringIOConference)
- [GOTO](https://www.youtube.com/c/GotoConferences)
- [Devoxx](https://www.youtube.com/c/Devoxx2015)
- [Jfokus](https://www.youtube.com/c/JfokusTheConference)
- [IntelliJ](https://www.youtube.com/c/intellijidea)
- and many more...

Some helpful channels in general on YouTube for software devs:

in german:
- [the native web GmbH](https://www.youtube.com/c/thenativewebGmbH)
- [David Tielke](https://www.youtube.com/c/DavidTielke)
- [CodeBrot](https://www.youtube.com/c/CodeBrot)
- [Florian Dalwigk](https://www.youtube.com/c/Algorithmenverstehen)
- [Kevin Chromik](https://www.youtube.com/c/KevinChromik)
- [Niklas Steenfatt](https://www.youtube.com/c/NiklasSteenfatt)
- [The Morpheus Tutorials](https://www.youtube.com/c/TheMorpheus407)
- [Fabian Hiller](https://www.youtube.com/c/FabianHiller)
- [Informatik mit Prof. Sebastian](https://www.youtube.com/channel/UCr_MPKZ3dVSHwtdhxZ2Eb6Q)

in english:
- [freeCodeCamp](https://www.youtube.com/c/Freecodecamp)
- [CS50](https://www.youtube.com/c/cs50)
- [Fireship](https://www.youtube.com/c/Fireship)
- [Academind](https://www.youtube.com/c/Academind)
- [Tech With Tim](https://www.youtube.com/c/TechWithTim)
- [Clément Mihailescu](https://www.youtube.com/channel/UCaO6VoaYJv4kS-TQO_M-N_g)
- [Keep On Coding](https://www.youtube.com/c/KeepOnCoding)
- [Hitesh Choudhary](https://www.youtube.com/c/HiteshChoudharydotcom)
- [Coding with John](https://www.youtube.com/c/CodingwithJohn)
- [Marco Codes](https://www.youtube.com/c/MarcoCodes)
- [Traversy Media](https://www.youtube.com/c/TraversyMedia)
- [Ben Awad](https://www.youtube.com/c/BenAwad97)
- [Web Dev Simplified](https://www.youtube.com/c/WebDevSimplified)
- [CodeOpinion](https://www.youtube.com/channel/UC3RKA4vunFAfrfxiJhPEplw)
- [TechWorld with Nana](https://www.youtube.com/c/TechWorldwithNana)
- [The Net Ninja](https://www.youtube.com/c/TheNetNinja)
- [Engineer Man](https://www.youtube.com/c/EngineerMan)
- [thenewboston](https://www.youtube.com/user/thenewboston)
- [London App Brewery](https://www.youtube.com/c/Londonappbrewery)
- and many more...
