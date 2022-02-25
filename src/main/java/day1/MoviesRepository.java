package day1;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {
    // dependency injection (konstruktor) pl.: interfacenek a példányosítását konstruktoron szeretném megkapni, a konstruktoron keresztül kapja meg az implementáló osztályt
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

    public List<Movie> findAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select id, title, release_date from movies")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Long id = rs.getLong("id");
                    String title = rs.getString("title");
                    LocalDate date = rs.getDate("release_date").toLocalDate();
                    Movie movie = new Movie(id, title, date);
                    movies.add(movie);
                }
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not find", sqle);
        }

        return movies;
    }

    public Optional<Movie> findMovieByTitle(String title){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from movies where title=?")){
            stmt.setString(1,title);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return Optional.of(new Movie(rs.getLong("id"),rs.getString("title"),rs.getDate("release_date").toLocalDate()));
                }
                return Optional.empty();
            }

        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot connect to movies!",sqlException);
        }
    }



    private List<Movie> processResultSet(ResultSet rs) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while(rs.next()){
            long id = rs.getLong("id");
            String title = rs.getString("title");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            movies.add(new Movie(id,title,releaseDate));
        }

        return movies;
    }


}