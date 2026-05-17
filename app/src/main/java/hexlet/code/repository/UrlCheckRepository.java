package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static final String ADD_NEW_CHECK_SQL = "INSERT INTO url_checks "
            + "(url_id, status_code, created_at, title, h1, description) VALUES (?, ?, ?, ?, ?, ?)";
    public static final int MAX_LENGTH = 200;
    private static final int CREATED_AT = 3;
    private static final int TITLE = 4;
    private static final int H1 = 5;
    private static final int DESCRIPTION = 6;

    public static List<UrlCheck> findByUrlId(Long id) {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = getDataSource().getConnection();
             var stmnt = conn.prepareStatement(sql)) {
            stmnt.setLong(1, id);
            var result = stmnt.executeQuery();
            List<UrlCheck> checks = new ArrayList<>();
            while (result.next()) {
                UrlCheck check = new UrlCheck();
                check.setId(result.getLong("id"));
                check.setUrlId(result.getLong("url_id"));
                check.setStatusCode(result.getInt("status_code"));
                check.setTitle(checkLength(result.getString("title")));
                check.setH1(checkLength(result.getString("h1")));
                check.setDescription(checkLength(result.getString("description")));
                check.setCreatedAt(result.getTimestamp("created_at").toLocalDateTime());
                checks.add(check);
            }
            return checks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(UrlCheck check) {
        var sql = ADD_NEW_CHECK_SQL;
        try (var conn = getDataSource().getConnection();
             var stmnt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmnt.setLong(1, check.getUrlId());
            stmnt.setInt(2, check.getStatusCode());
            var createdAt = LocalDateTime.now();
            stmnt.setTimestamp(CREATED_AT, Timestamp.valueOf(createdAt));
            stmnt.setString(TITLE, check.getTitle());
            stmnt.setString(H1, check.getH1());
            stmnt.setString(DESCRIPTION, check.getDescription());
            stmnt.executeUpdate();
            try (ResultSet generatedKeys = stmnt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    check.setId(generatedKeys.getLong(1));
                    check.setCreatedAt(createdAt);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String checkLength(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return s.length() > MAX_LENGTH ? s.substring(0, MAX_LENGTH) + "..." : s;
    }
}
