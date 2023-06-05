//MangeStoreCLI.java
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MangaStoreCLI {
    private List<String> mangaTitles;
    private List<Double> mangaPrices;

    private DecimalFormat money;
    private double total;
    private double bookPrice;
    private final double TAX = 0.06;

    public MangaStoreCLI() {
        mangaTitles = new ArrayList<>();
        mangaPrices = new ArrayList<>();
        money = new DecimalFormat("#,##0.00");
        total = 0.0;
        bookPrice = 0.0;
        initializeMangaData();
    }

    public void displayAvailableManga() {
        System.out.println("Available Manga:");
        for (int i = 0; i < mangaTitles.size(); i++) {
            System.out.println((i + 1) + ". " + mangaTitles.get(i) + " - $" + money.format(mangaPrices.get(i)));
        }
    }

    public void addToCart(int mangaIndex) {
        if (mangaIndex >= 0 && mangaIndex < mangaTitles.size()) {
            bookPrice += mangaPrices.get(mangaIndex);
            System.out.println(mangaTitles.get(mangaIndex) + " added to the cart.");
        } else {
            System.out.println("Invalid manga index.");
        }
    }

    public void removeFromCart(int cartIndex) {
        if (cartIndex >= 0 && cartIndex < mangaTitles.size()) {
            bookPrice -= mangaPrices.get(cartIndex);
            System.out.println(mangaTitles.get(cartIndex) + " removed from the cart.");
        } else {
            System.out.println("Invalid cart index.");
        }
    }

    public void calculateTotal() {
        total = bookPrice + (bookPrice * TAX);
        System.out.println("Subtotal: $" + money.format(bookPrice));
        System.out.println("Tax: $" + money.format(bookPrice * TAX));
        System.out.println("Total: $" + money.format(total));
    }

    public void initializeMangaData() {
        String[] titles = MangaInfo.getMangaTitles();
        double[] prices = MangaInfo.getMangaPrices();

        for (int i = 0; i < titles.length; i++) {
            mangaTitles.add(titles[i]);
            mangaPrices.add(prices[i]);
        }
    }

    public static void main(String[] args) {
        MangaStoreCLI mangaStore = new MangaStoreCLI();
        mangaStore.displayAvailableManga();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Select an option:");
            System.out.println("1. Add manga to cart");
            System.out.println("2. Remove manga from cart");
            System.out.println("3. Calculate total");
            System.out.println("4. Exit");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter the manga index: ");
                int mangaIndex = Integer.parseInt(scanner.nextLine()) - 1;
                mangaStore.addToCart(mangaIndex);
            } else if (choice.equals("2")) {
                System.out.print("Enter the cart index: ");
                int cartIndex = Integer.parseInt(scanner.nextLine()) - 1;
                mangaStore.removeFromCart(cartIndex);
            } else if (choice.equals("3")) {
                mangaStore.calculateTotal();
            } else if (choice.equals("4")) {
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        System.out.println("Thank you for using the MangaStoreCLI!");
        scanner.close();
    }
}
