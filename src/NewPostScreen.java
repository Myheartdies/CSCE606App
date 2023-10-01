
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

    private JTextField propertyNameField = new JTextField(18);
    private JTextField addressField = new JTextField(8);

    private JTextField areaField = new JTextField(4);

    private String[] types = { "1b1b", "2b2b" };
    private JComboBox<String> typeSelect = new JComboBox<>(types);

    private JTextField dateField = new JTextField(8);

    private JTextField priceField = new JTextField(4);

    private JTextArea descrField = new JTextArea(null, 20, 30);

    public JTextField getPropertyNameField() {
        return propertyNameField;
    }

    public JTextField getAddressField() {
        return addressField;
    }

    public JTextField getAreaField() {
        return areaField;
    }

    public JComboBox<String> getTypeSelect() {
        return typeSelect;
    }

    public JTextField getDateField() {
        return dateField;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public JTextArea getDescrField() {
        return descrField;
    }

    public NewPostScreen() {

        this.setTitle("Create New Listing for Apartment");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(400, 900);

        JPanel panelPropertyName = new JPanel();
        // panelInput1.setPreferredSize(new Dimension(400, 60));
        panelPropertyName.add(new JLabel("Apartment Name:"));
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
                5, 2, // rows, cols
                6, 6, // initX, initY
                6, 6); // xPad, yPad
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

        panelDescr.add(descrLabel);
        panelDescr.add(new JScrollPane(descrField));
        descrField.setText(
                "Other information you want to share with potential renters, like: \n Amenities, views around the property, is it close to a supermarket, etc.");
        // panelDescr.setLayout(new BoxLayout(panelDescr, BoxLayout.Y_AXIS));
        // descrField.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.getContentPane().add(panelDescr);

        JPanel panelButton = new JPanel();
        // panelButton.setBackground(Color.red);
        // panelButton.setAlignmentX(LEFT_ALIGNMENT);
        panelButton.setPreferredSize(new Dimension(400, 80));
        // panelButton.add(btnAdd);
        panelButton.add(btnPost);
        this.getContentPane().add(panelButton);
        // btnAdd.addActionListener(this::makeOrder);
        btnPost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Somehow, calling the function from outside of the class make the input field
                // unaccessable
                // So Currently I will call it in this class.
                // Application.getInstance().getPostingCtrl().onPost(e);
                onPost(e);
            }
        });
        this.pack();
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnPost() {
        return btnPost;
    }


    // ========================================================================================================
    // Divider for controller section
    // ========================================================================================================
    public void onPost(ActionEvent ev) {
        String type;
        String propertyName;
        String areaString;
        String address;
        String availableDateString;
        String priceString;
        String description = this.descrField.getText();
        areaString = this.areaField.getText();
        Double area;
        try {
            area =  Double.parseDouble(areaString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "invalid area input");
            return;
        }

        priceString = this.areaField.getText();
        Double price;
        try {
            price =  Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "invalid price input");
            return;
        }
        type = (String) this.typeSelect.getSelectedItem();
        propertyName = this.propertyNameField.getText();
        if (propertyName.length()<=0){
            JOptionPane.showMessageDialog(null, "property name cannot be empty");
            return;
        }

        availableDateString = this.dateField.getText();
        if (availableDateString.length()<=0){
            JOptionPane.showMessageDialog(null, "available date cannot be empty");
            return;
        }
        description =this.descrField.getText();

        address =this.addressField.getText();
        if (address.length()<=0){
            JOptionPane.showMessageDialog(null, "address date cannot be empty");
            return;
        }

        Apt post = new Apt();
        post.setType(type);
        post.setPrice(price);
        post.setArea(area);
        post.setAddress(address);
        post.setAvailableDate(availableDateString);
        post.setPosterID(Application.getInstance().getCurrentUser().getUserID());
        post.setDescr(description);
        if(
        Application.getInstance().getDataAdapter().AddListing(post)
        ){
            this.setVisible(false);
        }
        else{
            JOptionPane.showMessageDialog(null, "Unknown Error");
        }
    }

}