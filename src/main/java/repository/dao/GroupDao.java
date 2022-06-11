package repository.dao;

import repository.domain.Group;
import repository.domain.Member;
import repository.domain.Role;
import repository.domain.Warning;
import util.jdbcconnector.JdbcConnection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class GroupDao {
    private static final Logger LOGGER = Logger.getLogger(GroupDao.class);

    protected static Role getRoleFromResultSet(ResultSet resultSet) {
        try {
            return Role.builder()
                    .id(resultSet.getInt(1))
                    .title(resultSet.getString(2))
                    .right_ping(resultSet.getBoolean(3))
                    .right_edit(resultSet.getBoolean(4))
                    .right_to_view(resultSet.getBoolean(5))
                    .right_admin(resultSet.getBoolean(6))
                    .build();
        } catch (SQLException e) {
            LOGGER.error("Role creation error");
        }
        return null;
    }

    protected static Warning getWarningFromResultSet(ResultSet resultSet) {
        try {
            return Warning.builder()
                    .id(resultSet.getInt(1))
                    .id_group(resultSet.getInt(2))
                    .id_member(resultSet.getInt(3))
                    .id_cautioning(resultSet.getInt(4))
                    .cause(resultSet.getString(5))
                    .date(resultSet.getTimestamp(6))
                    .deadline(resultSet.getInt(7))
                    .build();
        } catch (SQLException e) {
            LOGGER.error("Warning creation error");
        }
        return null;
    }

    protected static Group getGroupFromResultSet(ResultSet resultSet) {
        try {
            return Group.builder()
                    .id(resultSet.getInt(1))
                    .title(resultSet.getString(2))
                    .build();
        } catch (SQLException e) {
            LOGGER.error("Group creation error");
        }
        return null;
    }

    public static Boolean addMember(Integer id_member, Integer id_group, Integer id_role) throws IOException, SQLException {
        String SQL = """
                INSERT INTO members_in_group (id_group, id_member, id_role) VALUES (?, ?, ?)  ON CONFLICT (id_group, id_member) DO NOTHING;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_member);
            preparedStatement.setInt(3, id_role);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean editMember(Integer id_member, Integer id_group, Integer id_role) throws IOException, SQLException {
        String SQL = """
                UPDATE members_in_group SET id_role = ? WHERE id_member = ? and id_group = ?;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_role);
            preparedStatement.setInt(2, id_member);
            preparedStatement.setInt(3, id_group);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean deleteMember(Integer id_member, Integer id_group) throws IOException, SQLException {
        String SQL = """
                DELETE FROM members_in_group WHERE id_group = ? and id_member = ?;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_member);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean deleteAllMembers(Integer id_group) throws IOException, SQLException {
        String SQL = """
                DELETE members_in_groups WHERE id_group = ?;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static ArrayList<Role> getAllRole() throws IOException, SQLException {
        ArrayList<Role> result = new ArrayList<>();
        String SQL = """
                SELECT * FROM roles
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(getRoleFromResultSet(resultSet));
                }
            }
        }
        return result;
    }

    public static Integer addGroup(String title) throws IOException, SQLException {
        String SQL = """
                INSERT INTO groups (title) VALUES (?) RETURNING id;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, title);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                resultSet.next();
                return resultSet.getInt(1);
            } catch (SQLException e) {
                return -1;
            }
        }
    }

    public static Boolean editGroup(Integer id_group, String title) throws IOException, SQLException {
        String SQL = """
                UPDATE groups SET title = ? WHERE id = ?;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, id_group);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean deleteGroup(Integer id_group) throws IOException, SQLException {
        String SQL = """
                DELETE FROM groups WHERE id = ?;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean addWarning(Integer id_member, Integer id_group, Integer id_cautioning, String cause, Integer deadline) throws IOException, SQLException {
        ArrayList<Member> members = GroupDao.getAllMemberInGroup(id_group);
        if (members.stream().anyMatch(a -> Objects.equals(a.getId(), id_member))) {
            String SQL = """
                    INSERT INTO warnings (id_group, id_member, id_cautioning, cause, date, deadline) VALUES (?, ?, ?, ?, ?, ?);
                    """;
            try (Connection connection = new JdbcConnection().CreateConnect();
                 PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
                preparedStatement.setInt(1, id_group);
                preparedStatement.setInt(2, id_member);
                preparedStatement.setInt(3, id_cautioning);
                preparedStatement.setString(4, cause);
                preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setInt(6, deadline);
                try {
                    Integer numbersWarnings = GroupDao.getCountWarningsFromMemberInGroup(id_member, id_group);
                    if (numbersWarnings == 0)
                        return (preparedStatement.executeUpdate() != 0);
                    else if (numbersWarnings > 0)
                        return stopWarning(id_group, id_member) && (preparedStatement.executeUpdate() != 0);
                    else return false;
                } catch (SQLException e) {
                    startWarning(id_group, id_member);
                    return false;
                }
            }
        }
        else return false;
    }

    public static Boolean deleteWarning(Integer id_group, Integer id_member) throws IOException, SQLException {
        String SQL = """
                DELETE FROM warnings WHERE id_group = ? and id_member = ? and Date is not null;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_member);
            try {
                return preparedStatement.executeUpdate() != 0 && startWarning(id_group, id_member);
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean deleteAllWarnings(Integer id_group, Integer id_member) throws IOException, SQLException {
        String SQL = """
                DELETE FROM warnings WHERE id_group = ? and id_member = ?;
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_member);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean deleteAllWarningsInGroup(Integer id_group) throws IOException, SQLException {
        try {
            ArrayList<Warning> result = getAllWarnings();
            for (Warning object : result) {
                if (Objects.equals(object.getId_group(), id_group)) {
                    GroupDao.deleteAllWarnings(id_group, object.getId_member());
                }
            }
            return true;
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    public static Boolean startWarning(Integer id_group, Integer id_member) throws IOException, SQLException {
        String SQL = """
                UPDATE warnings SET date = ?
                WHERE id =
                    (select MAX(id)
                    from warnings
                    where id_group = ? and id_member = ?);
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(2, id_group);
            preparedStatement.setInt(3, id_member);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static Boolean stopWarning(Integer id_group, Integer id_member) throws IOException, SQLException {
        String SQL = """
                UPDATE warnings SET date = null
                WHERE id =
                    (select MAX(id)
                    from warnings
                    where id_group = ? and id_member = ?);
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_member);
            try {
                return preparedStatement.executeUpdate() != 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    public static ArrayList<Warning> getAllWarnings() throws IOException, SQLException {
        ArrayList<Warning> result = new ArrayList<>();
        String SQL = """
                SELECT * FROM warnings
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(getWarningFromResultSet(resultSet));
                }
            }
        }
        return result;
    }

    public static Integer getCountWarningsFromMemberInGroup(Integer id_member, Integer id_group) throws IOException, SQLException {
        String SQL = """
                SELECT COUNT(id) FROM warnings WHERE id_member = ? and id_group = ?
                """;
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, id_member);
            preparedStatement.setInt(2, id_group);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                resultSet.next();
                Integer count = resultSet.getInt(1);
                return count;
            } catch (SQLException e) {
                return -1;
            }
        }
    }

    public static boolean CheckWarning() {
        try {
            ArrayList<Warning> result = getAllWarnings();
            for (Warning object : result) {
                if (object.getDate() != null) {
                    Timestamp deadline = new Timestamp(System.currentTimeMillis() + object.getDeadline() * 86400000);
                    if (object.getDate().after(deadline)) {
                        deleteWarning(object.getId_group(), object.getId_member());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<Member> getAllMemberInGroup(Integer id_group) throws IOException, SQLException {
        ArrayList<Member> result = new ArrayList<>();
        String SQL = """
                SELECT members.id, first_name, last_name, user_name,
                id_role, title, right_to_view, right_ping, right_edit, right_admin,
                    (select count(id)
                    from warnings
                    where members.id = warnings.id_member
                    and id_group=?) as number_of_warning
                FROM members, members_in_group, roles
                WHERE members.id=members_in_group.id_member
                and roles.id=members_in_group.id_role and id_group=?
                """;

        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_group);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next())
                {
                    result.add(MemberDao.getMemberExtendedFromResultSet(resultSet));
                }
            }
        }
        return result;
    }

    public static ArrayList<Member> getMemberInGroupInChat(Integer id_group, Integer id_chat) throws IOException, SQLException {
        ArrayList<Member> result = new ArrayList<>();
        String SQL = """
                SELECT members.id, first_name, last_name, user_name,
                FROM members, members_in_group, members_in_chat,
                WHERE members.id=members_in_chat.id_member
                and members.id=members_in_group.id_member
                and roles.id=members_in_group.id_role and id_group=? and id_chat=?""";
        try (Connection connection = new JdbcConnection().CreateConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            preparedStatement.setInt(1, id_group);
            preparedStatement.setInt(2, id_chat);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next())
                {
                    result.add(MemberDao.getMemberFromResultSet(resultSet));
                }
            }
        }
        return result;
    }

}
