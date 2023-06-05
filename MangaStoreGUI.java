//MangeStoreGUI.java
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class MangaStoreGUI extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 250;

    private JPanel mangaPanel;   // Holds all the books
    private JPanel buttonsPanel; // Add/remove/checkout buttons
    private JPanel cartPanel;    // To hold books added by user
    private JPanel bannerPanel;  // Banner panel
    private JPanel searchPanel;  // Holds search/show all buttons

    private JList<String> mangaList;    // List of manga
    private JList<String> selectedList; // List of cart

    private JButton addSelected;    // Adds manga to cart
    private JButton removeSelected; // Removes manga from cart
    private JButton checkout;       // Adds all books prices + taxes
    private JButton searchButton;   // Search bar
    private JButton showAllButton;  // Show all manga available

    private MangaInfo mangaInfo = new MangaInfo();    // MangaInfo object
    private String[] mangaTitles = mangaInfo.getMangaTitles();    // Array holding manga names
    private double[] mangaPrices = mangaInfo.getMangaPrices();    // Array holding manga prices
    private DecimalFormat money;    // Monetary value

    private JScrollPane scrollPane1;    // Holds available manga list
    private JScrollPane scrollPane2;    // Holds selected manga list

    private JLabel panelTitle;    // Panel title
    private JLabel cartTitle;     // Panel title
    private JLabel banner;        // Panel title

    private JTextField searchField;    // Allows user to input search
    private int element = -1;      // Control variable
    private int selectedIndex;     // Index selected among available manga
    private int index;             // Int that holds selected index
    private int i, count;             // Control variables

    private double total;    // Total of prices
    private double mangaPrice;    // Hold manga prices
    private final double TAX = 0.05;    // Constant tax value

    private ListModel manga;    // List model for book name list
    private ListModel cart;        // List model for shopping cart list
    private DefaultListModel<String> CartDFM;

    private String selectedMangaTitle;    // Selected manga
    private String searchResults;    // Hold search result
    private String notFound = "Title not found";    // Result not found

    // MangaStoreGUI - builds a GUI with multiple panels
    // Title of GUI
    public MangaStoreGUI() throws IOException {
        setTitle("Manga Store Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Build panels
        buildMangaPanel();
        buildButtonsPanel();
        buildCartPanel();
        buildBannerPanel();
        buildSearchPanel();

        // Add panels to GUI frame
        add(bannerPanel, BorderLayout.NORTH);
        add(mangaPanel, BorderLayout.WEST);
        add(buttonsPanel, BorderLayout.CENTER);
        add(cartPanel, BorderLayout.EAST);
        add(searchPanel, BorderLayout.SOUTH);

        // Set visibility
        setVisible(true);
        pack();
    }

    public void buildMangaPanel() throws IOException {
        // Create panel to hold list of manga
        mangaPanel = new JPanel();
        // Set Panel layout
        mangaPanel.setLayout(new BorderLayout());

        // Create the list model for manga titles
        DefaultListModel<String> mangaListModel = new DefaultListModel<>();
        String[] mangaTitles = mangaInfo.getMangaTitles();
        for (String title : mangaTitles) {
            // Split the line into title and price (assuming they are separated by comma)
            String[] parts = title.split(",");
            mangaListModel.addElement(parts[0].trim()); // Add only the title to the list model
        }

        // Create the JList using the mangaListModel
        mangaList = new JList<>(mangaListModel);
        // Set selection preference
        mangaList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        // Visible book names
        mangaList.setVisibleRowCount(5);
        // Create scroll panel containing manga list
        scrollPane1 = new JScrollPane(mangaList);
        scrollPane1.setPreferredSize(new Dimension(175, 50));
        // Jlabel/panel title
        panelTitle = new JLabel("Available Manga");
        // Add Jlabel and scroll to panel
        mangaPanel.add(panelTitle, BorderLayout.NORTH);
        mangaPanel.add(scrollPane1);
    }

    // Build Buttons Panel (add, remove, checkout buttons)
    public void buildButtonsPanel() {
        // Create panel to hold list of books
        buttonsPanel = new JPanel();
        // Set Panel layout
        buttonsPanel.setLayout(new GridLayout(3, 1));
        // Create Buttons
        addSelected = new JButton("Add to Cart");
        removeSelected = new JButton("Remove from Cart");
        checkout = new JButton("Checkout");

        // Add Listeners
        addSelected.addActionListener(new AddButtonListener());
        removeSelected.addActionListener(new RemoveButtonListener());
        checkout.addActionListener(new CheckoutButtonListener());

        // Add button panel to GUI
        buttonsPanel.add(addSelected);
        buttonsPanel.add(removeSelected);
        buttonsPanel.add(checkout);
    }

    // Build Cart Panel (containing JList/Scroll panel)
    public void buildCartPanel() {
        // Create panel
        cartPanel = new JPanel();
        // Set Panel layout
        cartPanel.setLayout(new BorderLayout());
        // Create cart list
        selectedList = new JList<>();
        // Set row visibility
        selectedList.setVisibleRowCount(5);
        // Create scroll pane containing selected list items
        scrollPane2 = new JScrollPane(selectedList);
        scrollPane2.setPreferredSize(new Dimension(175, 50));
        // JLabel/Panel title
        cartTitle = new JLabel("Shopping Cart");

        // Add JLabel and scroll pane to panel
        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartPanel.add(scrollPane2);
    }

    // Build banner panel (builds panel containing banner for GUI)
    public void buildBannerPanel() {
        // Create panel
        bannerPanel = new JPanel();
        // Set Border Layout
        setLayout(new BorderLayout());
        // String containing JLabel text
        String labelText = "<html><b><font color='gray'>Andrew's</font></b>" +
                "<b><font color='#00CCFF'> Manga Paradise</font></b>";


        // Create JLabel
        JLabel banner = new JLabel(labelText);
        banner.setFont(new Font("Serif", Font.BOLD, 28));

        // Add banner to panel
        bannerPanel.add(banner);
    }

    // Build search button (builds panel containing search and show all buttons)
    public void buildSearchPanel() {
        // Create panel
        searchPanel = new JPanel();
        // Set Border Layout
        searchPanel.setLayout(new GridLayout(1, 3, 5, 5));
        // Create buttons
        searchButton = new JButton("Search");
        showAllButton = new JButton("Show All");
        // Create text file field
        searchField = new JTextField(15);
        // Add listeners
        searchButton.addActionListener(new SearchButtonListener());
        showAllButton.addActionListener(new ShowAllButtonListener());
        // Add buttons and text field to panel
        searchPanel.add(searchField, BorderLayout.WEST);
        searchPanel.add(searchButton, BorderLayout.CENTER);
        searchPanel.add(showAllButton, BorderLayout.EAST);
    }

    // Add Button Listener (adds selected item to cart upon selection)
    public class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedIndex = mangaList.getSelectedIndex();
            selectedMangaTitle = mangaList.getSelectedValue();

            manga = mangaList.getModel();
            cart = selectedList.getModel();

            DefaultListModel<String> shoppingCartDFM = new DefaultListModel<>();

            for (int count = 0; count < cart.getSize(); count++) {
                shoppingCartDFM.addElement((String) cart.getElementAt(count));
            }

            if (element == -1) {
                mangaPrice += mangaPrices[selectedIndex];
            } else {
                mangaPrice += mangaPrices[element];
            }

            shoppingCartDFM.addElement(selectedMangaTitle);
            selectedList.setModel(shoppingCartDFM);
        }
    }

    // Remove button listener (removes selected manga from cart)
    public class RemoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            index = selectedList.getSelectedIndex();
            ((DefaultListModel<String>) selectedList.getModel()).remove(index);

            if (element == -1) {
                if (mangaPrices[selectedIndex] <= mangaPrice)
                    mangaPrice -= (mangaPrices[selectedIndex]);
                else
                    mangaPrice = (mangaPrices[index]) - mangaPrice;
            } else
            if (mangaPrices[element] <= mangaPrice)
                mangaPrice -= (mangaPrices[element]);
            else
                mangaPrice = (mangaPrices[index]) - mangaPrice;
        }
    }

    // Checkout button listener
    private class CheckoutButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            money = new DecimalFormat("#,##0.00");
            total = (mangaPrice + (mangaPrice*TAX));

            JOptionPane.showMessageDialog(null, "Subtotal: $" + (money.format(mangaPrice)) + "\n" +
                    "Tax: $" + (money.format((mangaPrice*TAX))) + "\n" +
                    "Total: $" + (money.format(total)));
        }
    }

    // Search button listener (in order to search manga)
    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean found = false;
            index = 0;

            String searchInput = searchField.getText().trim();

            while (!found && index < mangaTitles.length) {
                String[] parts = mangaTitles[index].split(",");
                String title = parts[0].trim();

                if (title.equalsIgnoreCase(searchInput)) {
                    found = true;
                    element = index;
                }
                index++;
            }

            if (element == -1) {
                mangaList.setModel(new DefaultListModel<>());
                ((DefaultListModel<String>) mangaList.getModel()).addElement(notFound);
            } else {
                String[] parts = mangaTitles[element].split(",");
                String title = parts[0].trim();
                mangaList.setModel(new DefaultListModel<>());
                ((DefaultListModel<String>) mangaList.getModel()).addElement(title);
            }
        }
    }

    private class ShowAllButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean found = false;
            index = 0;
            element = -1;
            mangaList.setModel(new DefaultListModel<>());

            for (i = 0; i < mangaTitles.length; i++) {
                String[] parts = mangaTitles[i].split(",");
                String title = parts[0].trim();

                ((DefaultListModel<String>) mangaList.getModel()).addElement(title);
            }
        }
    }



    // Main Method (creates instance of GUI class)
    public static void main(String[] args) throws IOException {
        new MangaStoreGUI();
    }
}
