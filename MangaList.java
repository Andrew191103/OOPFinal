public class MangaList extends MangaInfo {
    public MangaList() {
        super();  // Call the constructor of the superclass (MangaInfo)
    }

    public void printMangaInfo() {
        System.out.println("Manga Titles:");  // Print the heading for manga titles
        for (String title : mangaTitles) {
            System.out.println(title);  // Print each manga title
        }

        System.out.println("Manga Prices:");   // Print the heading for manga prices
        for (double price : mangaPrices) {
            System.out.println(price);  // Print each manga price
        }
    }
}
//The MangaList class extends the MangaInfo class, indicating that it inherits from MangaInfo.
//The constructor MangaList() is defined, which calls the superclass constructor using the super() keyword. This ensures that the mangaTitles and mangaPrices arrays are initialized.
//The printMangaInfo() method is defined to display the manga titles and prices.
//Within the printMangaInfo() method, a loop is used to iterate over the mangaTitles array and print each title.
//Another loop is used to iterate over the mangaPrices array and print each price.
//This code snippet demonstrates how the MangaList class utilizes the manga titles and prices inherited from the MangaInfo class to print the information.
