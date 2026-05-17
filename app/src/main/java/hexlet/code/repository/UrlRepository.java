package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    private static final String GET_ALL_URLS_SQL =
            "SELECT u.*, c.status_code, c.created_at as last_check "
                    + "FROM urls u "
                    + "LEFT JOIN url_checks c ON u.id = c.url_id "
                    + "WHERE c.created_at = (SELECT MAX(created_at) FROM url_checks WHERE url_id = u.id) "
                    + "OR c.created_at IS NULL "
                    + "ORDER BY u.created_at DESC";

    public static Long save(Url url) {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = getDataSource().getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            var createdAt = LocalDateTime.now();
            preparedStatement.setTimestamp(2, Timestamp.valueOf(createdAt));
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);

                url.setId(id);
                url.setCreatedAt(createdAt);

                return id;
            }
            throw new RuntimeException("Не удалось получить id");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static List<Url> getAll() {
        var sql = GET_ALL_URLS_SQL;
        try (var conn = getDataSource().getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            List<Url> res = new ArrayList();
            while (resultSet.next()) {
                Url url = new Url();
                url.setId(resultSet.getLong("id"));
                url.setName(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                if (resultSet.getTimestamp("last_check") != null) {
                    var check = new UrlCheck();
                    check.setStatusCode(resultSet.getInt("status_code"));
                    check.setCreatedAt(resultSet.getTimestamp("last_check").toLocalDateTime());
                    url.getChecks().add(check);
                }
                res.add(url);
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Url> findById(Long id) {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = getDataSource().getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Url url = new Url();
                url.setId(id);
                url.setName(resultSet.getString("name"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static Optional<Url> finbByName(String name) {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = getDataSource().getConnection();
             var stmnt = conn.prepareStatement(sql)) {
            stmnt.setString(1, name);
            var resultSet = stmnt.executeQuery();
            if (resultSet.next()) {
                Url url = new Url();
                url.setId(resultSet.getLong("id"));
                url.setName(resultSet.getString("name"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}

