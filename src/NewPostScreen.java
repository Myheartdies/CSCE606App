
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewPostScreen extends JFrame {

    private JButton btnAdd = new JButton("Add a new item");
    private JButton btnPost = new JButton("Post this listing");

    // private DefaultTableModel items = new DefaultTableModel(); // store
    // information for the table!

    // private JTable tblItems = new JTable(items);
    private JLabel labTotal = new JLabel("Total: ");

    // extra field for address and credit card number
    private JTextField propertyNameField = new JTextField(18);
    private JTextField addressField = new JTextField(8);
    private JTextField areaField = new JTextField(4);
    private JTextField typeField = new JTextField(4);
    private String[] types = {"1b1b","2b2b"};
    private JComboBox<String> typeSelect = new JComboBox<>(types);
    private JTextField dateField = new JTextField(8);
    private JTextField priceField = new JTextField(4);
    private JTextArea descrField = new JTextArea(null, 20, 30);

    private double taxRate = 0.03;

    private Order order = null;

    public NewPostScreen() {

        this.setTitle("Create New Post");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(400, 900);

        JPanel panelPropertyName = new JPanel();
        // panelInput1.setPreferredSize(new Dimension(400, 60));
        panelPropertyName.add(new JLabel("Property Name:"));
        panelPropertyName.add(propertyNameField);
        // propertyNameField.setHorizontalAlignment(JTextField.LEFT);
        // panelPropertyName.setBackground(Color.blue);
        // panelInput1.setPreferredSize(new Dimension(400, 60));
        this.getContentPane().add(panelPropertyName);

        JPanel panelInputAddress = new JPanel(new SpringLayout());
        panelInputAddress.add(new JLabel("Address: "));
        panelInputAddress.add(addressField);
        panelInputAddress.add(new JLabel("Area (in sq. ft.): "));
        panelInputAddress.add(areaField);
        panelInputAddress.add(new JLabel("Type: "));
        panelInputAddress.add(typeSelect);
        panelInputAddress.add(new JLabel("Available from: "));
        panelInputAddress.add(dateField);
        panelInputAddress.add(new JLabel("Price (per month): "));
        panelInputAddress.add(priceField);
        SpringUtilities.makeCompactGrid(panelInputAddress,
                5, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        this.getContentPane().add(panelInputAddress);




        JPanel panelDescr = new JPanel();
        // panelDescr.setBackground(Color.red);
        // panelDescr.setAlignmentX(LEFT_ALIGNMENT);
        panelDescr.setLayout(new BoxLayout(panelDescr, BoxLayout.PAGE_AXIS));
        JLabel descrLabel = new JLabel("Description :");
        descrLabel.setHorizontalAlignment(JLabel.LEFT);
        descrLabel.setAlignmentX(LEFT_ALIGNMENT);
        // descrLabel.setBackground(Color.red);
        descrField.setAlignmentX(LEFT_ALIGNMENT);
        // Dimension preferredSize = descrField.getPreferredSize();
        // descrField.setMaximumSize(new Dimension(preferredSize.width, preferredSize.height));

        panelDescr.add(descrLabel);
        panelDescr.add(new JScrollPane(descrField));
        descrField.setText("Other information you want to share with potential renters, like: \n Amenities, views around the property, is it close to a supermarket, etc.");
        // panelDescr.setLayout(new BoxLayout(panelDescr, BoxLayout.Y_AXIS));
        // descrField.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.getContentPane().add(panelDescr);

        JPanel panelButton = new JPanel();
        // panelButton.setBackground(Color.red);
        // panelButton.setAlignmentX(LEFT_ALIGNMENT);
        panelButton.setPreferredSize(new Dimension(400, 80));
        panelButton.add(btnAdd);
        panelButton.add(btnPost);
        this.getContentPane().add(panelButton);
        btnAdd.addActionListener(this::addProduct);
        btnPost.addActionListener(this::makeOrder);
        order = new Order();
        this.pack();
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnPost() {
        return btnPost;
    }

    public JLabel getLabTotal() {
        return labTotal;
    }

    // public void addRow(Object[] row) {
    // items.addRow(row);
    // }
    // ========================================================================================================
    // Divider for controller section
    // ========================================================================================================

    private void makeOrder(ActionEvent ev) {

    }

    private void addProduct(ActionEvent e) {
        // if (order == null) {
        // order = new Order();
        // }

        String id = JOptionPane.showInputDialog("Enter ProductID: ");
        int productID = 0;
        try {
            productID = Integer.parseInt(id);
        } catch (NumberFormatException er) {
            JOptionPane.showMessageDialog(null, "Invalid product ID! Please provide an integer!");
            return;
        }
        Product product = Application.getInstance().getDataAdapter().loadProduct(productID);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "This product does not exist!");
            return;
        }

        String quantityInput = JOptionPane.showInputDialog(null, "Enter quantity: ");
        double quantity = 0;
        try {
            quantity = Double.parseDouble(quantityInput);
        } catch (NumberFormatException er) {
            JOptionPane.showMessageDialog(null, "Invalid quanity! Please provide a double!");
            return;
        }
        if (quantity < 0 || quantity > product.getQuantity()) {
            JOptionPane.showMessageDialog(null, "This quantity is not valid!");
            return;
        }

        OrderLine line = new OrderLine();
        line.setOrderID(this.order.getOrderID());
        line.setProductID(product.getProductID());
        line.setQuantity(quantity);
        line.setCost(quantity * product.getPrice());
        order.getLines().add(line);
        order.setTotalCost(order.getTotalCost() + line.getCost());

        Object[] row = new Object[5];
        row[0] = line.getProductID();
        row[1] = product.getName();
        row[2] = product.getPrice();
        row[3] = line.getQuantity();
        row[4] = line.getCost();

        // addRow(row);
        getLabTotal().setText("Total: $" + order.getTotalCost());
        invalidate();
    }
}