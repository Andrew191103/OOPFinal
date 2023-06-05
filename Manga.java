public class Manga {
    protected String[] mangaTitles;
    protected double[] mangaPrices;

    public Manga() {
        mangaTitles = getMangaTitles();
        mangaPrices = getMangaPrices();
    }

    protected String[] getMangaTitles() {
        return new String[0];
    }

    protected double[] getMangaPrices() {
        return new double[0];
    }
}
