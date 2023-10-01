import java.sql.*;

public class DataAdapter {
    private Connection connection;

    public DataAdapter(Connection connection) {
        this.connection = connection;
    }

    // =====================
    public boolean AddUser(User user) {
        // currently only check username
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE UserName = ?");
            statement.setString(1, user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // If the user already exist, fail it
                statement.close();
                resultSet.close();
                return false;
            } else {
                // Else register
                statement = connection.prepareStatement(
                        "INSERT INTO Users (UserName, Password, DisplayName, Email) VALUES (?,?,?,?)");
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getFullName());
                statement.setString(4, user.getEmail());
                statement.execute();
                resultSet.close();

                statement = connection.prepareStatement("SELECT last_insert_rowid()");
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    user.setUserID(set.getInt(1));
                    System.out.println("User Id " + user.getUserID());
                    statement.close();
                    return true;
                } else {
                    user.setUserID(-1);
                    statement.close();
                    System.out.println("something is wrong");
                    return false;
                }

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddListing(Apartment post) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO RentalApartments (Type, Price, Area, Address,Description,AvailableTime,PostUserID) VALUES (?,?,?,?,?,?,?)");
            statement.setString(1, post.getType());
            statement.setDouble(2, post.getPrice());
            statement.setDouble(3, post.getArea());
            statement.setString(4, post.getAddress());
            statement.setString(5, post.getDescr());
            statement.setString(6, post.getAvailableDate());
            statement.setInt(7, post.getPosterID());
            statement.execute();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ======================




    public User loadUser(String username, String password) {
        try {

            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM Users WHERE UserName = ? AND Password = ?");
            // System.out.println(password);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            // System.out.println(resultSet.getInt("UserID"));
            if (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("UserName"));
                user.setPassword(resultSet.getString("Password"));
                user.setFullName(resultSet.getString("DisplayName"));
                user.setEmail(resultSet.getString("Email"));
                resultSet.close();
                statement.close();
                return user;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }


}
