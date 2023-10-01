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
                statement.close();
                resultSet.close();
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddListing(Post post) {
        return false;
    }

    // ======================

    public Product loadProduct(int id) {
        try {
            String query = "SELECT * FROM Products WHERE ProductID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return product;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
            statement.setInt(1, product.getProductID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection
                        .prepareStatement("UPDATE Products SET Name = ?, Price = ?, Quantity = ? WHERE ProductID = ?");
                statement.setString(1, product.getName());
                statement.setDouble(2, product.getPrice());
                statement.setDouble(3, product.getQuantity());
                statement.setInt(4, product.getProductID());
            } else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Products VALUES (?, ?, ?, ?, ?)");
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setDouble(4, product.getQuantity());
                statement.setInt(1, product.getProductID());
                statement.setInt(5, product.getSellerID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true; // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public Order loadOrder(int id) {
        try {
            Order order = null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Orders WHERE OrderID = " + id);

            if (resultSet.next()) {
                order = new Order();
                order.setOrderID(resultSet.getInt("OrderID"));
                order.setBuyerID(resultSet.getInt("CustomerID"));
                order.setTotalCost(resultSet.getDouble("TotalCost"));
                order.setDate(resultSet.getString("OrderDate"));
                resultSet.close();
                statement.close();
            }

            // loading the order lines for this order
            resultSet = statement.executeQuery("SELECT * FROM OrderLine WHERE OrderID = " + id);

            while (resultSet.next()) {
                OrderLine line = new OrderLine();
                line.setOrderID(resultSet.getInt(1));
                line.setProductID(resultSet.getInt(2));
                line.setQuantity(resultSet.getDouble(3));
                line.setCost(resultSet.getDouble(4));
                order.addLine(line);
            }

            return order;

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return null;
        }
    }

    // Save order and receipt, return receipt ID
    public boolean saveOrder(Order order) {
        try {
            int orderID;
            PreparedStatement statement = connection
                    .prepareStatement(
                            "INSERT INTO Orders (CustomerID, OrderDate,Address,CardNum,ValidThru,NameOnCard,TotalCost,TotalTax) VALUES (?, ?, ?, ?, ?,?,?,?)");
            // statement.setInt(1, order.getOrderID());
            statement.setInt(1, order.getBuyerID());
            statement.setString(2, order.getDate());
            statement.setString(3, order.getAddress());
            statement.setString(4, order.getCardNum());
            statement.setString(5, order.getValidThru());
            statement.setString(6, order.getNameOnCard());

            statement.setDouble(7, order.getTotalCost());
            statement.setDouble(8, order.getTotalTax());

            statement.execute(); // commit to the database;
            statement.close();

            statement = connection.prepareStatement("SELECT last_insert_rowid()");
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                order.setOrderID(set.getInt(1));
                System.out.println("Order Id " + order.getOrderID());
            } else {
                order.setOrderID(-1);
                System.out.println("something is wrong");
            }
            // Get the orderID of the generated order

            statement = connection.prepareStatement("INSERT INTO OrderLine VALUES (?, ?, ?, ?)");

            for (OrderLine line : order.getLines()) { // store for each order line!
                statement.setInt(1, order.getOrderID());
                statement.setInt(2, line.getProductID());
                statement.setDouble(3, line.getQuantity());
                statement.setDouble(4, line.getCost());

                statement.execute(); // commit to the database;
            }
            statement.close();
            return true; // save successfully!
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }

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

    public int saveReceipt(int orderId, String receipt) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO Receipts (OrderID, Receipt) VALUES (?,?)");
            statement.setInt(1, orderId);
            statement.setString(2, receipt);
            statement.execute();
            statement.close();
            statement = connection.prepareStatement("SELECT last_insert_rowid()");
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getInt(1);
            } else {
                System.out.println("something is wrong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }

}
