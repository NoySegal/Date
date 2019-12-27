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

    /**
     * Searches the _stock array for similar FoodItem with the same name and catalogue number as newItem.
     *
     * @param newItem is a FoodItem object that is used to check for similar name and catalogue number.
     * @return index of the first similar item (by name and catalogue number), if not found returns -1.
     */
    private int indexOfFirstSimilarItem(FoodItem newItem) {
        int index = 0;

        for (int i = 0; i < _noOfItems; i++) {
            if (newItem.getName().equals(_stock[i].getName()) && newItem.getCatalogueNumber() == _stock[i].getCatalogueNumber()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches the _stock array for identical FoodItem newItem, starting the search from the first similar item.
     *
     * @param newItem    is a FoodItem object that is used to check for completely identical item.
     * @param startIndex is the index of the first item that has similar name and catalogue number in _stock array.
     * @return index of identical item (including production/expiry dates) if found in _stock array, otherwise returns -1.
     */
    private int indexOfIdenticalItem(FoodItem newItem, int startIndex) {
        int i = startIndex;
        while (i < _noOfItems && newItem.getName().equals(_stock[i].getName()) && newItem.getCatalogueNumber() == _stock[i].getCatalogueNumber()) {
            if (_stock[i].equals(newItem)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Tries to insert a new food item into _stock array, if identical item is existing, it adds to it's quantity.
     * If similar items are existing, it adds the new item before the similar items.
     * Otherwise, it inserts it based on the Catalogue number order in the _stock array.
     *
     * @param newItem is a FoodItem object to be inserted into _stock array.
     * @return true if newItem was successfully inserted into _stock, false otherwise.
     */
    public boolean addItem(FoodItem newItem) {
        // Holds the first index of same name and catalogue number item if existed, otherwise holds -1
        int firstSimilarIndex = indexOfFirstSimilarItem(newItem);

        // last index if array is populated
        int j = _noOfItems;

        if (firstSimilarIndex == -1) { // No similar items in stock

            if (_noOfItems == MAX_STOCK) { // Stock array is full
                return false;
            } else { // There is room for a new stock entry

                while (j > 0 && _stock[j - 1].getCatalogueNumber() > newItem.getCatalogueNumber()) {
                    _stock[j] = _stock[j - 1];
                    j--;
                }
                _stock[j] = new FoodItem(newItem);
                _noOfItems++;
                return true;
            }
        } else { //There is at least one similar item in stock

            int identicalItemsIndex = indexOfIdenticalItem(newItem, firstSimilarIndex);
            if (identicalItemsIndex == -1) { // No identical item found
                if (_noOfItems == MAX_STOCK) {
                    return false;
                } else {
                    //insert before first similar
                    while (j > firstSimilarIndex) {
                        _stock[j] = _stock[j - 1];
                        j--;
                    }
                    _stock[j] = new FoodItem(newItem);
                    _noOfItems++;
                    return true;
                }
            } else { // Identical item found -> increase item Quantity
                _stock[identicalItemsIndex].setQuantity(_stock[identicalItemsIndex].getQuantity() + newItem.getQuantity());
                return true;
            }
        }
    }

    /**
     * Creates a list of items (with different name/catalogue number) if each item's quantity is below the amount parameter.
     * Items with identical name and catalogue number will be compared to the amount with their mutual quantities.
     *
     * @param amount is the max number that if an item quantity passes, is not adjoined to the list.
     * @return a list of items to order
     */
    public String order(int amount) {

        String items = "";

        int currentItemAmount;

        int j;

        int lastIndexOfSimilarItem;

        for (int i = 0; i < _noOfItems; i++) {

            currentItemAmount = _stock[i].getQuantity();
            j = i + 1;
            lastIndexOfSimilarItem = i;
            while (j < _noOfItems && _stock[j].getName().equals(_stock[i].getName()) && _stock[j].getCatalogueNumber() == _stock[i].getCatalogueNumber()) {
                currentItemAmount += _stock[j].getQuantity();
                lastIndexOfSimilarItem = j;
                j++;
            }

            // Prevents recounting items that were previously counted
            i = lastIndexOfSimilarItem;

            if (currentItemAmount < amount) {
                if (i == _noOfItems - 1) {
                    items += _stock[i].getName();
                } else {
                    items += _stock[i].getName() + ", ";
                }
            }
        }

        return items;
    }

    /**
     * Counts items in the stock if the parameter temp (a refrigerator temperature) is between the respective item min/max temperatures.
     *
     * @param temp is the temperature in another refrigerator.
     * @return number of amount that can be moved to another refrigerator.
     */
    public int howMany(int temp) {

        int itemsToMove = 0;

        for (int i = 0; i < _noOfItems; i++) {
            if (_stock[i].getMinTemperature() <= temp && _stock[i].getMaxTemperature() >= temp) {
                itemsToMove += _stock[i].getQuantity();
            }
        }

        return itemsToMove;
    }

    /**
     * Removes Food Item object from the stock array at a given index.
     *
     * @param index is the index of item to be removed from _stock.
     */
    private void removeItemAtIndex(int index) {
        for (int j = index; j < _noOfItems - 1; j++) {
            _stock[j] = _stock[j + 1];
        }
        _stock[_noOfItems - 1] = null;
        _noOfItems--;
    }

    /**
     * Remove food items from stock that have expiry date before the Date parameter, while keeping the array organized.
     *
     * @param d is the date object to be compared with the expiry dates.
     */
    public void removeAfterDate(Date d) {

        for (int i = 0; i < _noOfItems; i++) {

            if (_stock[i].getExpiryDate().before(d)) {

                removeItemAtIndex(i);

                //Checking the item that took the removed item place in the same index
                i--;
            }
        }
    }

    /**
     * Finds the most expensive food item in stock.
     *
     * @return FoodItem object of the most expensive item in stock.
     */
    public FoodItem mostExpensive() {
        if (_noOfItems == 0) {
            return null;
        }

        FoodItem mostExpensiveItem = _stock[0];

        for (int i = 1; i < _noOfItems; i++) {

            if (_stock[i].getPrice() > mostExpensiveItem.getPrice()) {
                mostExpensiveItem = _stock[i];
            }
        }
        return new FoodItem(mostExpensiveItem);
    }

    /**
     * Counts the total quantity of the items in the stock.
     *
     * @return the total number of quantities in stock.
     */
    public int howManyPieces() {
        int counter = 0;

        for (int i = 0; i < _noOfItems; i++) {
            counter += _stock[i].getQuantity();
        }

        return counter;
    }

    /**
     * @return a string representation of the stock.
     */
    public String toString() {
        String stockHolder = "";

        for (int i = 0; i < _noOfItems; i++) {
            stockHolder += _stock[i].toString() + "\n";
        }

        return stockHolder;
    }

    /**
     * Updates the stock array and remove items that were sold given by the itemList.
     *
     * @param itemsList is a list of Strings representing items that were sold
     */
    public void updateStock(String[] itemsList) {

        boolean itemUpdatedFlag;

        for (int i = 0; i < itemsList.length; i++) {
            itemUpdatedFlag = false;
            for (int j = 0; j < _noOfItems && !itemUpdatedFlag; j++) {
                if (itemsList[i].equals(_stock[j].getName())) {
                    _stock[j].setQuantity(_stock[j].getQuantity() - 1);
                    if (_stock[j].getQuantity() == 0) {

                        removeItemAtIndex(j);
                    }
                    itemUpdatedFlag = true;
                }
            }
        }
    }

    /**
     * Finds the minimal common temperature between the range of (min to max temperatures) for each item so that the refrigerator should be at that temperature to hold all of the stock items.
     *
     * @return the minimal temperature the refrigerator should be to hold all the stock items or Integer.MAX_VALUE if minimum common temperature is not found or array is empty of stock.
     */
    public int getTempOfStock() {

        if (_noOfItems == 0) {
            return Integer.MAX_VALUE;
        }

        int minTemp = _stock[0].getMinTemperature();
        int maxTemp = _stock[0].getMaxTemperature();

        for (int i = 1; i < _noOfItems; i++) {

            if (_stock[i].getMaxTemperature() < minTemp || _stock[i].getMinTemperature() > maxTemp) {
                return Integer.MAX_VALUE;
            }

            if (_stock[i].getMinTemperature() > minTemp) {
                minTemp = _stock[i].getMinTemperature();
            }

            if (_stock[i].getMaxTemperature() < maxTemp) {
                maxTemp = _stock[i].getMaxTemperature();
            }
        }

        return minTemp;
    }
}
