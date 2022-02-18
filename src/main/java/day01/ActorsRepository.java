package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {

    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into actors(actor_name) values(?)")) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not update" + name, sqle);
        }
    }

    public List<String> findActorWithPrefix(String prefix) {
        List<String> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select actor_name from actors where actor_name like ?")) {
            stmt.setString(1, prefix + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addNameToList(rs, result);
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not query" + sqle);
        }
        return result;
    }

    private void addNameToList(ResultSet rs, List<String> result) {
        String actorName;
        try {
            actorName = rs.getString("actor_name");
            result.add(actorName);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not query" + sqle);
        }
    }
}