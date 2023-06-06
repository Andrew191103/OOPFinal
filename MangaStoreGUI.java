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
        setTitle("Your Ultimate Shopping Cart for Manga");
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
        //The code builds different panels by calling corresponding methods (buildMangaPanel(), buildButtonsPanel(), etc.).
        //Each panel is added to the GUI frame using the add method and specifying the desired region using BorderLayout.
        //The visibility of the GUI frame is set to true using setVisible(true).
        //The components within the frame are packed together using pack().
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
        panelTitle = new JLabel("Browse Manga Collection");
        // Add Jlabel and scroll to panel
        mangaPanel.add(panelTitle, BorderLayout.NORTH);
        mangaPanel.add(scrollPane1);
    }

    // Build Buttons Panel (add, remove, checkout buttons)
    public void buildButtonsPanel() {
        //It creates a JPanel named buttonsPanel that will hold the buttons.
        buttonsPanel = new JPanel();
        // Set Panel layout
        buttonsPanel.setLayout(new GridLayout(3, 1)); // The layout of buttonsPanel is set to GridLayout with 3 rows and 1 column, which arranges components in a grid.
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
        //This code sets up the buttons panel in the GUI and assigns action listeners to each button for performing respective actions when clicked
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
        selectedList.setVisibleRowCount(5); // it is set to 5, meaning that five rows will be visible without requiring scrolling.
        // Create scroll pane containing selected list items
        scrollPane2 = new JScrollPane(selectedList); //created to provide scrolling functionality to the selectedList JList.
        scrollPane2.setPreferredSize(new Dimension(175, 50)); // it is set to a width of 175 pixels and a height of 50 pixels.
        // JLabel/Panel title
        cartTitle = new JLabel("Shopping Cart");

        // Add JLabel and scroll pane to panel
        cartPanel.add(cartTitle, BorderLayout.NORTH); //The cartTitle label is added to the cartPanel in the NORTH region of the BorderLayout.
        cartPanel.add(scrollPane2); //The scrollPane2 is added to the cartPanel, which will be placed in the CENTER region of the BorderLayout.
        //By building the cart panel in this way, the GUI will display the shopping cart with a title at the top and a scrollable list of selected items below it.
    }

    // Build banner panel (builds panel containing banner for GUI)
    public void buildBannerPanel() {
        // Create panel
        bannerPanel = new JPanel(); //JPanel object named bannerPanel is created to hold the components of the banner panel.
        // Set Border Layout
        setLayout(new BorderLayout()); //The layout manager of the bannerPanel is set to BorderLayout. This layout manager arranges components in five regions: NORTH, SOUTH, EAST, WEST, and CENTER.
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
        searchPanel = new JPanel(); //JPanel object named searchPanel is created to hold the components of the search panel.
        // Set Border Layout
        searchPanel.setLayout(new GridLayout(1, 3, 5, 5)); //his layout manager arranges components in a grid with a specified number of rows and columns.
        // Create buttons
        searchButton = new JButton("Search");
        showAllButton = new JButton("Show All"); //Create buttons: Two JButton objects, searchButton and showAllButton, are created with the labels "Search" and "Show All" respectively.
        // Create text file field
        searchField = new JTextField(15); //A JTextField object named searchField is created with a width of 15 columns.
        // Add listeners
        searchButton.addActionListener(new SearchButtonListener());
        showAllButton.addActionListener(new ShowAllButtonListener());
        // Add buttons and text field to panel
        searchPanel.add(searchField, BorderLayout.WEST);
        searchPanel.add(searchButton, BorderLayout.CENTER);
        searchPanel.add(showAllButton, BorderLayout.EAST);
    }

    // Add Button Listener (adds selected item to cart upon selection) This listener is associated with the "Add to Cart" button and is triggered when the button is clicked.
    public class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedIndex = mangaList.getSelectedIndex();
            selectedMangaTitle = mangaList.getSelectedValue(); //It retrieves the index and value of the selected item from the mangaList component. The mangaList is assumed to be a JList that contains a list of manga titles.

            manga = mangaList.getModel();
            cart = selectedList.getModel(); //These models are assumed to be instances of DefaultListModel.

            DefaultListModel<String> shoppingCartDFM = new DefaultListModel<>(); //It creates a new DefaultListModel named shoppingCartDFM.

            for (int count = 0; count < cart.getSize(); count++) {
                shoppingCartDFM.addElement((String) cart.getElementAt(count)); //It iterates over each element in the cart model (selectedList) and adds them to the shoppingCartDFM model.
            }

            if (element == -1) {
                mangaPrice += mangaPrices[selectedIndex]; //It checks if the element variable is equal to -1. If it is, it means that no item is currently selected in the selectedList.
                // it adds the price of the manga corresponding to the selected index from mangaPrices array to the mangaPrice variable.
            } else {
                mangaPrice += mangaPrices[element]; //Otherwise, it adds the price of the manga corresponding to the element index in the mangaPrices array.
            }

            shoppingCartDFM.addElement(selectedMangaTitle); //It adds the selected manga title to the shoppingCartDFM model.
            selectedList.setModel(shoppingCartDFM); // it sets the shoppingCartDFM model as the model for the selectedList, effectively updating the contents of the shopping cart display.
        }
    }

    // Remove button listener (removes selected manga from cart)
    public class RemoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Retrieve the index of the selected item in the selectedList
            index = selectedList.getSelectedIndex();
            ((DefaultListModel<String>) selectedList.getModel()).remove(index);
            // Check if the element variable is -1 (no item selected)
            if (element == -1) {
                // If the mangaPrices[selectedIndex] is less than or equal to mangaPrice
                if (mangaPrices[selectedIndex] <= mangaPrice)
                    // Subtract the price of the manga from the mangaPrice
                    mangaPrice -= (mangaPrices[selectedIndex]);
                else
                    // Subtract mangaPrice from the price of the manga
                    mangaPrice = (mangaPrices[index]) - mangaPrice;
            } else
                // If the mangaPrices[element] is less than or equal to mangaPrice
            if (mangaPrices[element] <= mangaPrice)
                // Subtract the price of the manga from the mangaPrice
                mangaPrice -= (mangaPrices[element]);
            else
                // Subtract mangaPrice from the price of the manga
                mangaPrice = (mangaPrices[index]) - mangaPrice;
        }
    }

    // Checkout button listener
    private class CheckoutButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Create a DecimalFormat object to format the money values
            money = new DecimalFormat("#,##0.00");
            // Calculate the total price including tax
            total = (mangaPrice + (mangaPrice*TAX));
            // Display a message dialog with the subtotal, tax, and total
            JOptionPane.showMessageDialog(null, "Subtotal: $" + (money.format(mangaPrice)) + "\n" +
                    "Tax: $" + (money.format((mangaPrice*TAX))) + "\n" +
                    "Total: $" + (money.format(total)));
            //The code uses JOptionPane to display a message dialog with the subtotal, tax, and total. The money.format() method is used to format the values with a specific number format.
        }
    }

    // Search button listener (in order to search manga)
    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean found = false; // Flag to indicate if the manga is found
            index = 0; // Index counter for searching

            String searchInput = searchField.getText().trim(); // Get the search input from the text field

            while (!found && index < mangaTitles.length) {
                String[] parts = mangaTitles[index].split(",");
                String title = parts[0].trim();

                if (title.equalsIgnoreCase(searchInput)) {
                    found = true; // Found the manga
                    element = index; // Store the index of the found manga
                }
                index++;
            }

            if (element == -1) { // Manga not found
                mangaList.setModel(new DefaultListModel<>());
                ((DefaultListModel<String>) mangaList.getModel()).addElement(notFound);
            } else { // Manga found
                String[] parts = mangaTitles[element].split(",");
                String title = parts[0].trim();
                mangaList.setModel(new DefaultListModel<>());
                ((DefaultListModel<String>) mangaList.getModel()).addElement(title);
            }
        }
    }

    private class ShowAllButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            boolean found = false; // Flag to indicate if any manga is found
            index = 0;  // Index counter for iterating through the mangaTitles array
            element = -1; // Reset the stored element index
            mangaList.setModel(new DefaultListModel<>()); // Clear the current manga list


            for (i = 0; i < mangaTitles.length; i++) {
                String[] parts = mangaTitles[i].split(",");
                String title = parts[0].trim();

                ((DefaultListModel<String>) mangaList.getModel()).addElement(title); // Add each manga title to the list
                //this listener does not perform any filtering or searching. It simply adds all manga titles to the list.
                //It iterates through the mangaTitles array and adds each manga title to the manga list.
            }
        }
    }



    // Main Method (creates instance of GUI class)
    public static void main(String[] args) throws IOException {
        new MangaStoreGUI();  // Creates an instance of the MangaStoreGUI class, which in turn initializes and displays the Manga Store GUI.
        //The new MangaStoreGUI() statement creates an object of the MangaStoreGUI class, invoking its constructor and initiating the GUI.
        //throws IOException declaration in the method signature indicates that the constructor of MangaStoreGUI may throw an IOException, and it is handled or propagated accordingly.
    }
}
