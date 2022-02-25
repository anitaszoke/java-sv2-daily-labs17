package day1;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//nem függ attól, hogy milyen adatbázison dolgozom, ugyanúgy néz ki ez az osztály
public class ActorsRepository {

    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveActor(String name) {
        try (Connection conn = dataSource.getConnection();
//            előkészített állítás, paraméterezett
             PreparedStatement stmt = conn.prepareStatement("insert into actors(actor_name) values(?)")) {
//      nem nullától, hanem 1-től index
            stmt.setString(1, name);
//            utasítás végrehajtása
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Cannot insert and get id!");
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not update" + name, sqle);
        }
    }

    public List<String> findActorWithPrefix(String prefix) {
        List<String> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select actor_name from actors where actor_name like ?")) {
// LIKE után a részletet a set-be kell tenni, a like után csak a kérdőjel kell
            stmt.setString(1, prefix + "%");

//            resultset - eredményhalmaz, le kell zárni, trywithres., nem kell catch, az utolsó catch ág elkapja, várok vissza értéket
//            a resultset a lekérdezésnek megfelelő sorokat tartalmazza, így végig lehet rajta iterálni és hozzáadni a listához
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
//                    csak az oszlop nevét kell átadni
                    String actorName = rs.getString("actor_name");
                    result.add(actorName);
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not query" + sqle);
        }
        return result;
    }

    public Optional<Actor> findActorByName(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select * from actors where actor_name=?")) {
            stmt.setString(1, name);

            return processSelectStatement(stmt);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect fo select by name!");
        }
    }

    private Optional<Actor> processSelectStatement(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Actor(rs.getLong("id"), rs.getString("actor_name")));
            }
            return Optional.empty();
        }
    }
}