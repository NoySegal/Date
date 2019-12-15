/**
 * This class represents a stock object.
 *
 * @author Noy Segal
 * @version 2020a
 */
public class Stock {

    private FoodItem[] _stock;
    private int _noOfItems;

    // Max stock in the supermarket
    private final int MAX_STOCK = 100;

    /**
     * Default Constructor
     *
     * Creates a new Stock object
     */
    public Stock() {
        _stock = new FoodItem[MAX_STOCK];
    }

}
