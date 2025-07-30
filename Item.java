/**
 * Represents an item in the Pokémon world with its properties and effects
 */
public class Item {
    private String name;
    private String category;
    private String description;
    private String effect;
    private int buyingPrice;
    private int sellingPrice;
    
    /**
     * Constructor for creating a new item
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
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getEffect() { return effect; }
    public int getBuyingPrice() { return buyingPrice; }
    public int getSellingPrice() { return sellingPrice; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setEffect(String effect) { this.effect = effect; }
    public void setBuyingPrice(int buyingPrice) { this.buyingPrice = buyingPrice; }
    public void setSellingPrice(int sellingPrice) { this.sellingPrice = sellingPrice; }
    
    /**
     * Checks if this item can be held by a Pokémon
     */
    public boolean isHoldable() {
        return category.equals("Evolution Stone") || 
               category.equals("Accessory") || 
               category.equals("Poké Ball");
    }
    
    /**
     * Checks if this item matches a search query
     */
    public boolean matchesSearch(String query) {
        String lowerQuery = query.toLowerCase();
        return name.toLowerCase().contains(lowerQuery) ||
               category.toLowerCase().contains(lowerQuery) ||
               description.toLowerCase().contains(lowerQuery) ||
               effect.toLowerCase().contains(lowerQuery);
    }
    
    /**
     * Returns a string representation of the item
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
     * Checks if two items are equal based on name
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return name.equals(item.name);
    }
}
