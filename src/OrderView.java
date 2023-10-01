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

public class OrderView extends JFrame {

    private JButton btnAdd = new JButton("Add a new item");
    private JButton btnPay = new JButton("Finish and pay");

    private DefaultTableModel items = new DefaultTableModel(); // store information for the table!

    private JTable tblItems = new JTable(items);
    private JLabel labTotal = new JLabel("Total: ");

    // extra field for address and credit card number
    private JTextField creditField = new JTextField(18);
    private JTextField expField = new JTextField(4);
    private JTextField nameField = new JTextField(8);
    private JTextField cvvField = new JTextField(4);
    private JTextField addressField = new JTextField(25);

    private double taxRate = 0.03;

    private Order order = null;

    public OrderView() {

        this.setTitle("Order View");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setSize(400, 600);

        items.addColumn("Product ID");
        items.addColumn("Name");
        items.addColumn("Price");
        items.addColumn("Quantity");
        items.addColumn("Cost");

        JPanel panelOrder = new JPanel();
        panelOrder.setPreferredSize(new Dimension(500, 450));
        panelOrder.setLayout(new BoxLayout(panelOrder, BoxLayout.PAGE_AXIS));
        tblItems.setBounds(0, 0, 400, 350);
        panelOrder.add(tblItems.getTableHeader());
        panelOrder.add(tblItems);
        panelOrder.add(labTotal);
        // panelOrder.setBackground(Color.blue);
        tblItems.setFillsViewportHeight(true);
        this.getContentPane().add(panelOrder);

        JPanel panelInput1 = new JPanel();
        // panelInput1.setPreferredSize(new Dimension(400, 60));
        panelInput1.add(new JLabel("Credit card number:"));
        panelInput1.add(creditField);
        creditField.setHorizontalAlignment(JTextField.LEFT);
        this.getContentPane().add(panelInput1);

        JPanel panelInput2 = new JPanel();
        // panelInput1.setPreferredSize(new Dimension(400, 60));
        panelInput2.add(new JLabel("Name on card:"));
        panelInput2.add(nameField);
        panelInput2.add(new JLabel("valid thru (m/y):"));
        panelInput2.add(expField);
        panelInput2.add(new JLabel("CVV:"));
        panelInput2.add(cvvField);
        creditField.setHorizontalAlignment(JTextField.LEFT);
        this.getContentPane().add(panelInput2);

        JPanel panelInput3 = new JPanel();
        panelInput3.add(new JLabel("Address:"));
        panelInput3.add(addressField);
        // panelInput1.setPreferredSize(new Dimension(400, 60));
        addressField.setHorizontalAlignment(JTextField.LEFT);
        this.getContentPane().add(panelInput3);

        JPanel panelButton = new JPanel();
        // panelButton.setBackground(Color.red);
        panelButton.setPreferredSize(new Dimension(400, 80));
        panelButton.add(btnAdd);
        panelButton.add(btnPay);
        this.getContentPane().add(panelButton);
        btnAdd.addActionListener(this::addProduct);
        btnPay.addActionListener(this::makeOrder);
        order = new Order();
        this.pack();
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnPay() {
        return btnPay;
    }

    public JLabel getLabTotal() {
        return labTotal;
    }

    public void addRow(Object[] row) {
        items.addRow(row);
    }
    // ========================================================================================================
    // Divider for controller section
    // ========================================================================================================

    private void makeOrder(ActionEvent ev) {
        // JOptionPane.showMessageDialog(null, "This function is being implemented!");

        /*
         * Remember to update new quantity of products!
         * product.setQuantity(product.getQuantity() - quantity); // update new
         * quantity!!
         * dataAdapter.saveProduct(product); // and save this product back
         */
        // order = null;

        // ========================================================================================================
        // ATTENTION: I have not implemented the quantity update yet, because the current checkpoint only ask me to 
        // "create" an order, not to actually "make" one; also, it's because OrderView is client side code, so update
        //  should not happen here.
        // ========================================================================================================
        String cardNum;
        String validThru;
        String cvv;
        String address;
        String nameOnCard;

        // Assign value to order

        cardNum = this.creditField.getText();
        try {
            Long.parseLong(cardNum);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Credit card number must be a number");
            return;
        }
        validThru = this.expField.getText();
        if (validThru.length() == 0) {
            JOptionPane.showMessageDialog(null, "Expiration date cannot be empty!");
            return;
        }
        cvv = this.cvvField.getText();
        if (cvv.length() == 0) {
            JOptionPane.showMessageDialog(null, "cvv cannot be empty!");
            return;
        }
        address = this.addressField.getText();
        if (address.length() == 0) {
            JOptionPane.showMessageDialog(null, "Address cannot be empty!");
            return;
        }
        nameOnCard = this.nameField.getText();

        if (nameOnCard.length() == 0) {
            JOptionPane.showMessageDialog(null, "Name on card cannot be empty!");
            return;
        }

        order.setCardNum(cardNum);
        order.setValidThru(validThru);
        order.setCvv(cvv);
        order.setNameOnCard(nameOnCard);
        order.setAddress(address);
        order.setTotalTax(order.getTotalCost() * taxRate);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        order.setDate(strDate);

        // Save order to database, generate and save receipt to database.
        if (Application.getInstance().getDataAdapter().saveOrder(order)) {
            // Generate receipt
            String receiptString = generateReceipt(order);
            // Save receipt to database
            int receiptId = Application.getInstance().getDataAdapter().saveReceipt(order.getOrderID(), receiptString);
            // Show receipt, with receipt id
            showReceipt(receiptId, receiptString);
        } else {
            JOptionPane.showMessageDialog(null, "error saving order");
            return;
        }
        // Not implemented:
        // Update Quantity of products selected

        // Clear the current order
        order = new Order();
        // Clear the table
        items.setRowCount(0);
        getLabTotal().setText("Total: ");


    }

    // TODO: Finish whole receipt
    private String generateReceipt(Order order) {
        String output = "";
        // output += "Order of Username: " + Application.getInstance().getCurrentUser().getUsername() + "\n";
        // output += "Full name: " + Application.getInstance().getCurrentUser().getFullName() + "\n";
        // output += "Shipping address: " + order.getAddress() + "\n";
        // output += "------------------------------\n";
        // output += "Payment:" + "\n";
        // if (order.getCardNum().length() >= 4)
        //     output += "Credit card number: ***** " + order.getCardNum().substring(order.getCardNum().length() - 4)
        //             + "\n";
        // else
        //     output += "Credit card number: ***** " + order.getCardNum() + "\n";
        // output += "=================================\n";
        // output += "Purchased Item(s) and prices:\n";
        // Product product;
        // for (OrderLine line : order.getLines()) {
        //     int productID = line.getProductID();
        //     // product = Application.getInstance().getDataAdapter().loadProduct(productID);
        //     output += "--------------------------------\n";
        //     // output += product.getName() + " * " + line.getQuantity() + "\n";
        //     // output += "Price: $" + product.getPrice() + " * " + line.getQuantity() + " = " + line.getCost() + "\n";
        // }
        // output += "-----------------------------------------\n";
        // output += "Total Cost: " + order.getTotalCost() + "\n";
        // output += "Total Tax:  " + order.getTotalTax() + "\n";

        // System.out.println(output);
        return output;
    }

    private void showReceipt(int receiptId, String receiptString) {
        String outpString = "Order saved and generated receipt with ID #"+receiptId+"\n";
        outpString += "Below is the generated receipt:\n";
        outpString += "====================================================================\n";
        outpString +=  receiptString;
        JOptionPane.showMessageDialog(null, outpString);
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
        // Product product = Application.getInstance().getDataAdapter().loadProduct(productID);
        // if (product == null) {
        //     JOptionPane.showMessageDialog(null, "This product does not exist!");
        //     return;
        // }

        String quantityInput = JOptionPane.showInputDialog(null, "Enter quantity: ");
        double quantity = 0;
        try {
            quantity = Double.parseDouble(quantityInput);
        } catch (NumberFormatException er) {
            JOptionPane.showMessageDialog(null, "Invalid quanity! Please provide a double!");
            return;
        }
        // if (quantity < 0 || quantity > product.getQuantity()) {
        //     JOptionPane.showMessageDialog(null, "This quantity is not valid!");
        //     return;
        // }

        // OrderLine line = new OrderLine();
        // line.setOrderID(this.order.getOrderID());
        // line.setProductID(product.getProductID());
        // line.setQuantity(quantity);
        // line.setCost(quantity * product.getPrice());
        // order.getLines().add(line);
        // order.setTotalCost(order.getTotalCost() + line.getCost());

        // Object[] row = new Object[5];
        // row[0] = line.getProductID();
        // row[1] = product.getName();
        // row[2] = product.getPrice();
        // row[3] = line.getQuantity();
        // row[4] = line.getCost();

        // addRow(row);
        // getLabTotal().setText("Total: $" + order.getTotalCost());
        // invalidate();
    }
}
