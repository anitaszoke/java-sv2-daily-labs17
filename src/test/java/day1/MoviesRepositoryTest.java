package day1;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MoviesRepositoryTest {


    MoviesRepository moviesRepository;
    Flyway flyway;

    @BeforeEach
    void init() {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors-test?useUnicode=true");

            dataSource.setUserName("root");

            dataSource.setPassword("1225");

        } catch (SQLException e) {

            throw new IllegalStateException("Can not reach DataBase");
        }
        flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        moviesRepository = new MoviesRepository(dataSource);
    }

    @Test
    void testInsert() {
        moviesRepository.saveMovie("Az", LocalDate.of(2017, 9, 8));
        System.out.println(moviesRepository.findAllMovies());
        flyway.clean();

    }
}