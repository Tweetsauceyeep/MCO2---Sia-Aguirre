/**
 * Represents an item in the Pokémon world with its properties and effects.
 * This class encapsulates all information about items including their
 * name, category, description, effects, and pricing information.
 * 
 * Each item has:
 * - A unique name and category
 * - A description of its appearance or purpose
 * - An effect description that explains what it does
 * - Buying and selling prices for shop transactions
 * 
 * Items can be categorized as Evolution Stones, Accessories, Poké Balls,
 * or other categories, and some can be held by Pokémon.
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class Item {
    /** The name of the item */
    private String name;
    /** The category of the item (e.g., Evolution Stone, Accessory, Poké Ball) */
    private String category;
    /** A description of the item's appearance or purpose */
    private String description;
    /** A description of what the item does when used */
    private String effect;
    /** The price to buy this item from a shop */
    private int buyingPrice;
    /** The price received when selling this item to a shop */
    private int sellingPrice;
    
    /**
     * Constructor for creating a new item.
     * Initializes all item properties with the provided values.
     * 
     * @param name The name of the item
     * @param category The category of the item
     * @param description A description of the item
     * @param effect A description of the item's effect
     * @param buyingPrice The price to buy the item
     * @param sellingPrice The price received when selling the item
     */
    public Item(String name, String category, String description, String effect, 
                int buyingPrice, int sellingPrice) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.effect = effect;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }
    
    // Getters
    
    /**
     * Gets the name of the item.
     * @return The item's name
     */
    public String getName() { return name; }
    
    /**
     * Gets the category of the item.
     * @return The item's category
     */
    public String getCategory() { return category; }
    
    /**
     * Gets the description of the item.
     * @return The item's description
     */
    public String getDescription() { return description; }
    
    /**
     * Gets the effect description of the item.
     * @return The item's effect description
     */
    public String getEffect() { return effect; }
    
    /**
     * Gets the buying price of the item.
     * @return The price to buy the item
     */
    public int getBuyingPrice() { return buyingPrice; }
    
    /**
     * Gets the selling price of the item.
     * @return The price received when selling the item
     */
    public int getSellingPrice() { return sellingPrice; }
    
    // Setters
    
    /**
     * Sets the name of the item.
     * @param name The new name for the item
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Sets the category of the item.
     * @param category The new category for the item
     */
    public void setCategory(String category) { this.category = category; }
    
    /**
     * Sets the description of the item.
     * @param description The new description for the item
     */
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Sets the effect description of the item.
     * @param effect The new effect description for the item
     */
    public void setEffect(String effect) { this.effect = effect; }
    
    /**
     * Sets the buying price of the item.
     * @param buyingPrice The new buying price for the item
     */
    public void setBuyingPrice(int buyingPrice) { this.buyingPrice = buyingPrice; }
    
    /**
     * Sets the selling price of the item.
     * @param sellingPrice The new selling price for the item
     */
    public void setSellingPrice(int sellingPrice) { this.sellingPrice = sellingPrice; }
    
    /**
     * Checks if this item can be held by a Pokémon.
     * Only certain categories of items can be held by Pokémon.
     * 
     * @return true if the item can be held, false otherwise
     */
    public boolean isHoldable() {
        return category.equals("Evolution Stone") || 
               category.equals("Accessory") || 
               category.equals("Poké Ball");
    }
    
    /**
     * Checks if this item matches a search query.
     * Performs a case-insensitive search across all item properties
     * including name, category, description, and effect.
     * 
     * @param query The search query to match against
     * @return true if the item matches the query, false otherwise
     */
    public boolean matchesSearch(String query) {
        String lowerQuery = query.toLowerCase();
        return name.toLowerCase().contains(lowerQuery) ||
               category.toLowerCase().contains(lowerQuery) ||
               description.toLowerCase().contains(lowerQuery) ||
               effect.toLowerCase().contains(lowerQuery);
    }
    
    /**
     * Returns a formatted string representation of the item.
     * Includes the name, category, description, effect, and pricing information.
     * 
     * @return A formatted string representation of the item
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(category).append(")\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Effect: ").append(effect).append("\n");
        sb.append("Price: ₽").append(buyingPrice);
        if (buyingPrice == 0) {
            sb.append(" (Not sold)");
        }
        sb.append(" | Sell: ₽").append(sellingPrice).append("\n");
        return sb.toString();
    }
    
    /**
     * Checks if two items are equal based on their names.
     * Two items are considered equal if they have the same name.
     * 
     * @param obj The object to compare with
     * @return true if the items have the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return name.equals(item.name);
    }
}
