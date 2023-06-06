public class Manga {
    protected String[] mangaTitles;  // Array to store manga titles
    protected double[] mangaPrices;  // Array to store manga prices

    public Manga() {
        mangaTitles = getMangaTitles();  // Initialize mangaTitles array
        mangaPrices = getMangaPrices();  // Initialize mangaPrices array
    }

    protected String[] getMangaTitles() {
        return new String[0];  // Default implementation returns an empty array
    }

    protected double[] getMangaPrices() {
        return new double[0];  // Default implementation returns an empty array

    }
}
