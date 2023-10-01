import java.sql.*;

public class Application {

    private static Application instance;   // Singleton pattern

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }
    // Main components of this application

    private Connection connection;

    public Connection getDBConnection() {
        return connection;
    }

    private DataAdapter dataAdapter;

    private User currentUser = null;

    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private ProductView productView = new ProductView();

    private OrderView orderView = new OrderView();

    private MainScreen mainScreen = new MainScreen();
    private RegisterScreen regScreen = new RegisterScreen();
    private ListingScreen aptList = new ListingScreen();
    private NewPostScreen postingScreen = new NewPostScreen();

    public NewPostScreen getPostingScreen() {
        return postingScreen;
    }

    public ListingScreen getAptList() {
        return aptList;
    }

    public RegisterScreen getRegScreen() {
        return regScreen;
    }

    private NewPostScreen editScreen = new NewPostScreen();

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public ProductView getProductView() {
        return productView;
    }

    public OrderView getOrderView() {
        return orderView;
    }
    public NewPostScreen getNewPostScreen(){
        return editScreen;
    }

    public LoginScreen loginScreen = new LoginScreen();

    public LoginScreen getLoginScreen() {
        return loginScreen;
    }

    private LoginController loginScreenCtrl = new LoginController();
    private PostingController postingCtrl = new PostingController();
    

    // public LoginController loginController;

    // private ProductController productController;

    // public ProductController getProductController() {
    //     return productController;
    // }

    // private OrderController orderController;

    // public OrderController getOrderController() {
    //     return orderController;
    // }

    public PostingController getPostingCtrl() {
        return postingCtrl;
    }

    public LoginController getLoginScreenCtrl() {
        return loginScreenCtrl;
    }

    public DataAdapter getDataAdapter() {
        return dataAdapter;
    }


    private Application() {
        // create SQLite database connection here!
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:proto.db";

            connection = DriverManager.getConnection(url);
            dataAdapter = new DataAdapter(connection);

        }
        catch (ClassNotFoundException ex) {
            System.out.println("SQLite is not installed. System exits with error!");
            ex.printStackTrace();
            System.exit(1);
        }

        catch (SQLException ex) {
            System.out.println("SQLite database is not ready. System exits with error!" + ex.getMessage());

            System.exit(2);
        }

        // productController = new ProductController(productView);

        // orderController = new OrderController(orderView);

        // loginController = new LoginController(loginScreen);
    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }
}
