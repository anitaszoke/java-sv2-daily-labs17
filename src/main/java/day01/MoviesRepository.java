package day01;

import day1.Movie;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate release_date) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into movies(title, release_date) values(?,?)")) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(release_date));
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect", sqle);
        }
    }

    public List<day1.Movie> findAllMovies() {
        List<day1.Movie> movies = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select id, title, release_date from movies")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String title = rs.getString("title");
                    LocalDate date = rs.getDate("release_date").toLocalDate();
                    day1.Movie movie = new Movie(id, title, date);
                    movies.add(movie);
                }
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not find", sqle);
        }

        return movies;
    }
}