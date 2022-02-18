package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");

            dataSource.setUserName("root");

            dataSource.setPassword("1225");

        } catch (SQLException e) {

            throw new IllegalStateException("Can not reach DataBase");
        }

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        actorsRepository.saveActor("Jack Doe");
        System.out.println(actorsRepository.findActorWithPrefix("J"));
    }
}