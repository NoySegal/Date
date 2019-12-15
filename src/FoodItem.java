/**
 * This class represents a food item object
 *
 * @author Noy Segal
 * @version 2020a
 */
public class FoodItem {

    private String _name;
    private long _catalogueNumber;
    private int _quantity;
    private Date _productionDate;
    private Date _expiryDate;
    private int _minTemperature;
    private int _maxTemperature;
    private int _price;


    private final int EMPTY_NAME_SIZE = 0;

    //Catalogue constraints
    private final int MIN_CATALOGUE_NUM = 1000;
    private final int MAX_CATALOGUE_NUM = 9999;

    //Minimums of quantity and price
    private final int MIN_QUANTITY_NUM = 0;
    private final int MIN_PRICE = 1;

    //Default values
    private final int DEFAULT_CATALOGUE_NUM = 9999;
    private final int DEFAULT_QUANTITY = 0;
    private final int DEFAULT_PRICE = 1;


    /**
     * creates a new FoodItem object
     * Parameters:
     *
     * @param name            - name of food item (immutable)
     * @param catalogueNumber - catalogue number of food item (immutable)
     * @param quantity        - quantity of food item
     * @param productionDate  - production date
     * @param expiryDate      - expiry date
     * @param minTemperature  - minimum storage temperature (immutable)
     * @param maxTemperature  - maximum storage temperature (immutable)
     * @param price           - unit price
     */
    public FoodItem(String name,
                    long catalogueNumber,
                    int quantity,
                    Date productionDate,
                    Date expiryDate,
                    int minTemperature,
                    int maxTemperature,
                    int price) {

        if (name.length() == EMPTY_NAME_SIZE) {
            _name = "item";
        } else {
            _name = name;
        }


        if (catalogueNumber < MIN_CATALOGUE_NUM || catalogueNumber > MAX_CATALOGUE_NUM) {
            _catalogueNumber = DEFAULT_CATALOGUE_NUM;
        } else {
            _catalogueNumber = catalogueNumber;
        }


        if (quantity < MIN_QUANTITY_NUM) {
            _quantity = DEFAULT_QUANTITY;
        } else {
            _quantity = quantity;
        }


        _productionDate = new Date(productionDate);

        if (expiryDate.before(productionDate)) {
            _expiryDate = _productionDate.tomorrow();
        } else {
            _expiryDate = new Date(expiryDate);
        }


        if (minTemperature > maxTemperature) {
            _maxTemperature = minTemperature;
            _minTemperature = maxTemperature;
        } else {
            _maxTemperature = maxTemperature;
            _minTemperature = minTemperature;
        }


        if (price < MIN_PRICE) {
            _price = DEFAULT_PRICE;
        } else {
            _price = price;
        }
    }

    /**
     * Copy constructor for FoodItem.
     * Construct a FoodItem with the same variables as another FoodItem.
     *
     * @param other - the FoodItem object from which to construct the new FoodItem
     */
    public FoodItem(FoodItem other) {

        _name = other._name;

        _catalogueNumber = other._catalogueNumber;

        _quantity = other._quantity;

        _productionDate = new Date(other._productionDate);
        _expiryDate = new Date(other._expiryDate);

        _minTemperature = other._minTemperature;
        _maxTemperature = other._maxTemperature;

        _price = other._price;
    }

    /**
     * check if 2 food items are the same (excluding the quantity values)
     *
     * @param other - the food item to compare this food item to
     * @return true if the food items are the same
     */
    public boolean equals(FoodItem other) {
        return _name.equals(other._name) &&
                _catalogueNumber == other._catalogueNumber &&
                _productionDate.equals(other._productionDate) && _expiryDate.equals(other._expiryDate) &&
                _minTemperature == other._minTemperature && _maxTemperature == other._maxTemperature &&
                _price == other._price;
    }

    /**
     * @return the catalogue number of the food item
     */
    public long getCatalogueNumber() {
        return _catalogueNumber;
    }

    /**
     * @return the expiry date of the food item
     */
    public Date getExpiryDate() {
        return new Date(_expiryDate);
    }

    /**
     * @return the max temperature of the food item
     */
    public int getMaxTemperature() {
        return _maxTemperature;
    }

    /**
     * @return the min temperature of the food item
     */
    public int getMinTemperature() {
        return _minTemperature;
    }

    /**
     * @return the name of the food item
     */
    public String getName() {
        return _name;
    }

    /**
     * @return the price of the food item
     */
    public int getPrice() {
        return _price;
    }

    /**
     * @return the production date of the food item
     */
    public Date getProductionDate() {
        return new Date(_productionDate);
    }

    /**
     * @return the quantity of the food item
     */
    public int getQuantity() {
        return _quantity;
    }

    /**
     * Calculates how many items can be purchased with amount of money, depends on quantity available.
     *
     * @param amount is amount of money used to evalute amount of items to be bought
     * @return amount of items that are available which can be purchased with that amount of money.
     */
    public int howManyItems(int amount) {

        if (amount < MIN_PRICE) {
            return 0;

        } else {

            int amountToPrice = amount / _price;

            return Math.min(amountToPrice, _quantity);
        }
    }

    /**
     * Checks if this food item price is cheaper than other food item.
     *
     * @param other is another food item to be compared with
     * @return true if this food item price is cheaper than other price
     */
    public boolean isCheaper(FoodItem other) {
        return _price < other._price;
    }

    /**
     * Checks if a given date is between the production ad expiry dates (including these dates).
     *
     * @param d is a given date
     * @return true if d is between (and including) production date and expiry date
     */
    public boolean isFresh(Date d) {
        return (d.after(_productionDate) && d.before(_expiryDate)) ||
                d.equals(_productionDate) ||
                d.equals(_expiryDate);
    }

    /**
     * Checks if this item is older (productionDate) than another product pass as argument.
     *
     * @param other is another food item
     * @return true if this item was produced before other item
     */
    public boolean olderFoodItem(FoodItem other) {
        return _productionDate.before(other._productionDate);
    }

    /**
     * Sets the expiry date if the parameter is valid (expiry is not before production), otherwise no change.
     *
     * @param d the new date of expiry if valid
     */
    public void setExpiryDate(Date d) {

        if (!d.before(_productionDate)) {
            _expiryDate = new Date(d);
        }
    }

    /**
     * Sets the food item price if the parameter is valid (positive number)
     *
     * @param n the new price to set if valid
     */
    public void setPrice(int n) {

        if (n >= MIN_PRICE) {
            _price = n;
        }
    }

    /**
     * Sets the production date if the parameter is valid (production is not after expiry), otherwise no change.
     *
     * @param d the new date of production if valid
     */
    public void setProductionDate(Date d) {

        if (!d.after(_expiryDate)) {
            _productionDate = new Date(d);
        }
    }

    /**
     * Sets the quantity if the parameter is not a negative number, otherwise no change.
     *
     * @param n the quantity if valid
     */
    public void setQuantity(int n) {

        if (n >= MIN_QUANTITY_NUM) {
            _quantity = n;
        }
    }

    /**
     * @return a string representation of this food item
     */
    public String toString() {
        String foodItemHolder = "FoodItem: ";
        foodItemHolder += _name + "\tCatalogueNumber: ";
        foodItemHolder += _catalogueNumber + "\tProductionDate: ";
        foodItemHolder += _productionDate + "\tExpiryDate: ";
        foodItemHolder += _expiryDate + "\tQuantity: ";
        foodItemHolder += _quantity;

        return foodItemHolder;
    }
}
