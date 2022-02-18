package day1;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
//        létrehozás, ez az osztály felelős azért, hogy megteremtse a kapcsolatot
//        az adatbázis és a program között
        MariaDbDataSource dataSource = new MariaDbDataSource();
//konfigurálni kell a datasourse-t set-ekkel(url, felhasznév és jelszó), sqlExc miatt kell a try catch blokk
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");

            dataSource.setUserName("root");

            dataSource.setPassword("1225");

        } catch (SQLException e) {
//            kivételt csomagoljuk
            throw new IllegalStateException("Can not reach DataBase");
        }

//    connection-t kérünk, (interface), le kell zárni try with resources-sal
//    elérhető a zárás

/*        try (Connection conn = dataSource.getConnection()){
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("insert into actors(actor_name) values ('John Doe') ");

        } catch (SQLException e) {
            throw new IllegalStateException("Can not connect");
        }*/

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        actorsRepository.saveActor("Jack Doe");
        System.out.println(actorsRepository.findActorWithPrefix("Jo"));
    }
}