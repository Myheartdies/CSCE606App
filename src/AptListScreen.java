
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

public class AptListScreen extends JFrame {

    private DefaultTableModel listings = new DefaultTableModel(); // store information for the table!

    private JTable ListingTbl = new JTable(listings);

    public JTable getListingTbl() {
        return ListingTbl;
    }

    public AptListScreen() {

        this.setTitle("Apartment listings");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setSize(500, 600);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        listings.addColumn("Apartment Name");
        listings.addColumn("Type");
        listings.addColumn("sq.ft.");
        listings.addColumn("Rent");
        listings.addColumn("Address");
        listings.addColumn("Available");
        listings.addColumn("");

        JPanel panelListings = new JPanel();
        panelListings.setPreferredSize(new Dimension(500, 450));
        panelListings.setLayout(new BoxLayout(panelListings, BoxLayout.PAGE_AXIS));
        // ListingTbl.setBounds(0, 0, 400, 350);
        panelListings.add(ListingTbl.getTableHeader());
        panelListings.add(ListingTbl);

        JScrollPane scrollable = new JScrollPane(panelListings);
        this.getContentPane().add(scrollable);
        for (int i = 0; i < 100; i++) {
            Object[] row = new Object[7];
            row[0] = "sdfsdfs";
            row[1] = "sdfsdf";
            row[2] = "sfsdf";
            row[3] = "dfgdg";
            row[4] = "sfsf";
            row[5] = "sdfsdf";
            row[6] = "sfsf";
            listings.addRow(row);
        }
        // row[6] = "sfsdfsdf";
        // panelListings.add(labTotal);
    }
}
