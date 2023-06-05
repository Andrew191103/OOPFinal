public class MangaList extends MangaInfo {
    public MangaList() {
        super();
    }

    public void printMangaInfo() {
        System.out.println("Manga Titles:");
        for (String title : mangaTitles) {
            System.out.println(title);
        }

        System.out.println("Manga Prices:");
        for (double price : mangaPrices) {
            System.out.println(price);
        }
    }
}
