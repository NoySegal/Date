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
     * Creates a new Stock object
     */
    public Stock() {

        _stock = new FoodItem[MAX_STOCK];
        _noOfItems = 0;
    }

    /**
     * @return the number of products currently in stock
     */
    public int getNumOfItems() {

        return _noOfItems;
    }

    public boolean addItem(FoodItem newItem) {
        //TODO this method
        //true if item already in array or if not and arr not full..
        //false if item not in arr and arr is full
        return false;
    }
}
