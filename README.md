# One Night Ultimate Werewolf (Server)

Server Side implementation of the social deduction game "One Night Ultimate Werewolf".

[Rules](https://www.fgbradleys.com/rules/rules2/OneNightUltimateWerewolf-rules.pdf)

## Techstack:

1. Java 8
2. Spring Boot (Rest and CRUDs with API endpoints)
3. Hibernate
4. SQL
5. Flutter (frontend - [Repo is Here](https://github.com/samliew94/onenightultimatewerewolf-client))

## Compiling and making JAR file with Maven
The jar file is excluded to keep the repo's size within GitHub's constraint.

1. In root dir, run "mvn clean package spring-boot:repackage".
2. A JAR file will be generated in \target folder.

## How to Run (Maven):
1. JAVA_HOME must be pointed to a JDK/JRE 8.
2. open CMD and enter \target directory
3. execute "java -jar onuw-0.0.1.jar"
4. Navigate to localhost (alternatively use ipv4 addr from ipconfig)
