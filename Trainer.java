/**
 * Represents a Pokémon trainer with their profile and inventory
 */
public class Trainer {
    // Constants
    private static final int MAX_LINEUP = 6;
    private static final int MAX_MOVES = 4;
    private static final int MAX_UNIQUE_ITEMS = 10;
    private static final int MAX_TOTAL_ITEMS = 50;
    private static final int INITIAL_MONEY = 1000000; // ₱1,000,000
    
    // Static counter for unique trainer IDs
    private static int trainerCount = 0;
    
    // Trainer attributes
    private int trainerID;
    private String name;
    private String birthdate;
    private String sex;
    private String hometown;
    private String description;
    private int money;
    
    // Pokémon management
    private Pokemon[] lineup; // Active Pokémon (max 6)
    private int lineupCount;
    private Pokemon[] storage; // Storage for extra Pokémon
    private int storageCount;
    private int maxStorage;
    
    // Inventory management
    private Item[] uniqueItems; // Up to 10 unique items
    private int[] itemQuantities; // Quantities for each unique item
    private int uniqueItemCount;
    private int totalItemCount;
    
    /**
     * Constructor for creating a new trainer
     */
    public Trainer(String name, String birthdate, String sex, String hometown, String description) {
        this.trainerID = ++trainerCount;
        this.name = name;
        this.birthdate = birthdate;
        this.sex = sex;
        this.hometown = hometown;
        this.description = description;
        this.money = INITIAL_MONEY;
        
        // Initialize Pokémon arrays
        this.lineup = new Pokemon[MAX_LINEUP];
        this.lineupCount = 0;
        this.storage = new Pokemon[100]; // Large storage capacity
        this.storageCount = 0;
        this.maxStorage = 100;
        
        // Initialize inventory arrays
        this.uniqueItems = new Item[MAX_UNIQUE_ITEMS];
        this.itemQuantities = new int[MAX_UNIQUE_ITEMS];
        this.uniqueItemCount = 0;
        this.totalItemCount = 0;
    }
    
    /**
     * Buys an item from the shop
     */
    public boolean buyItem(Item item, int quantity) {
        int totalCost = item.getBuyingPrice() * quantity;
        
        // Check if item is purchasable
        if (item.getBuyingPrice() == 0) {
            System.out.println("This item is not sold in shops!");
            return false;
        }
        
        // Check if trainer has enough money
        if (money < totalCost) {
            System.out.println("Insufficient funds! Need ₽" + totalCost + ", have ₽" + money);
            return false;
        }
        
        // Check if adding this quantity would exceed total item limit
        if (totalItemCount + quantity > MAX_TOTAL_ITEMS) {
            System.out.println("Cannot carry more than " + MAX_TOTAL_ITEMS + " items total!");
            return false;
        }
        
        // Find if item already exists in inventory
        int itemIndex = findItemIndex(item);
        
        if (itemIndex != -1) {
            // Item exists, increase quantity
            itemQuantities[itemIndex] += quantity;
        } else {
            // New item, check unique item limit
            if (uniqueItemCount >= MAX_UNIQUE_ITEMS) {
                System.out.println("Cannot carry more than " + MAX_UNIQUE_ITEMS + " unique items!");
                return false;
            }
            
            // Add new item
            uniqueItems[uniqueItemCount] = item;
            itemQuantities[uniqueItemCount] = quantity;
            uniqueItemCount++;
        }
        
        // Deduct money and update total count
        money -= totalCost;
        totalItemCount += quantity;
        
        System.out.println("Successfully bought " + quantity + " " + item.getName() + "(s) for ₽" + totalCost);
        return true;
    }
    
    /**
     * Sells an item for its selling price
     */
    public boolean sellItem(Item item, int quantity) {
        int itemIndex = findItemIndex(item);
        
        if (itemIndex == -1) {
            System.out.println("You don't have this item!");
            return false;
        }
        
        if (itemQuantities[itemIndex] < quantity) {
            System.out.println("You don't have enough of this item! Have: " + itemQuantities[itemIndex]);
            return false;
        }
        
        // Calculate selling price
        int totalEarned = item.getSellingPrice() * quantity;
        
        // Remove items from inventory
        itemQuantities[itemIndex] -= quantity;
        totalItemCount -= quantity;
        
        // If quantity becomes 0, remove the item from unique items
        if (itemQuantities[itemIndex] == 0) {
            removeItemFromInventory(itemIndex);
        }
        
        // Add money
        money += totalEarned;
        
        System.out.println("Successfully sold " + quantity + " " + item.getName() + "(s) for ₽" + totalEarned);
        return true;
    }
    
    /**
     * Uses an item on a Pokémon
     */
    public boolean useItem(Item item, Pokemon target) {
        int itemIndex = findItemIndex(item);
        
        if (itemIndex == -1) {
            System.out.println("You don't have this item!");
            return false;
        }
        
        if (itemQuantities[itemIndex] <= 0) {
            System.out.println("You don't have any of this item left!");
            return false;
        }
        
        // Apply item effect based on category
        String category = item.getCategory();
        
        if (category.equals("Vitamin") || category.equals("Feather")) {
            // Apply stat boost
            target.applyItemEffect(item);
            System.out.println("Used " + item.getName() + " on " + target.getName() + "!");
            
            // Consume the item
            itemQuantities[itemIndex]--;
            totalItemCount--;
            
            if (itemQuantities[itemIndex] == 0) {
                removeItemFromInventory(itemIndex);
            }
            
        } else if (category.equals("Leveling Item")) {
            // Rare Candy logic
            boolean shouldEvolve = target.useRareCandy();
            System.out.println(target.getName() + " leveled up to level " + target.getCurrentLevel() + "!");
            
            if (shouldEvolve) {
                System.out.println(target.getName() + " is ready to evolve!");
                // Try to evolve the Pokémon
                evolveByLevel(target);
            }
            
            // Consume the item
            itemQuantities[itemIndex]--;
            totalItemCount--;
            
            if (itemQuantities[itemIndex] == 0) {
                removeItemFromInventory(itemIndex);
            }
            
        } else if (category.equals("Evolution Stone")) {
            // Evolution stone logic
            System.out.println("Used " + item.getName() + " on " + target.getName() + "!");
            evolveByStone(target, item);
            
            // Consume the item
            itemQuantities[itemIndex]--;
            totalItemCount--;
            
            if (itemQuantities[itemIndex] == 0) {
                removeItemFromInventory(itemIndex);
            }
            
        } else if (item.isHoldable()) {
            // Held item logic
            if (target.getHeldItem() != null) {
                System.out.println(target.getName() + " is already holding " + target.getHeldItem().getName() + "!");
                System.out.println("Replaced " + target.getHeldItem().getName() + " with " + item.getName());
            }
            target.setHeldItem(item);
            System.out.println(target.getName() + " is now holding " + item.getName() + "!");
            
            // Consume the item
            itemQuantities[itemIndex]--;
            totalItemCount--;
            
            if (itemQuantities[itemIndex] == 0) {
                removeItemFromInventory(itemIndex);
            }
            
        } else {
            System.out.println("This item cannot be used on Pokémon!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Adds a Pokémon to the trainer's lineup
     */
    public boolean addPokemonToLineup(Pokemon pokemon) {
        if (lineupCount >= MAX_LINEUP) {
            System.out.println("Lineup is full! Maximum " + MAX_LINEUP + " Pokémon allowed.");
            return false;
        }
        
        // Create a unique instance for this trainer
        Pokemon trainerPokemon = new Pokemon(pokemon);
        lineup[lineupCount] = trainerPokemon;
        lineupCount++;
        
        System.out.println(pokemon.getName() + " has been added to your lineup!");
        return true;
    }
    
    /**
     * Adds a Pokémon directly to storage
     */
    public boolean addPokemonToStorage(Pokemon pokemon) {
        if (storageCount >= maxStorage) {
            System.out.println("Storage is full!");
            return false;
        }
        
        // Create a unique instance for this trainer
        Pokemon trainerPokemon = new Pokemon(pokemon);
        storage[storageCount] = trainerPokemon;
        storageCount++;
        
        System.out.println(pokemon.getName() + " has been added to storage!");
        return true;
    }
    
    /**
     * Adds an item to inventory (simpler version for initialization)
     */
    public boolean addItem(Item item, int quantity) {
        // Check if adding this quantity would exceed total item limit
        if (totalItemCount + quantity > MAX_TOTAL_ITEMS) {
            System.out.println("Cannot carry more than " + MAX_TOTAL_ITEMS + " items total!");
            return false;
        }
        
        // Find if item already exists in inventory
        int itemIndex = findItemIndex(item);
        
        if (itemIndex != -1) {
            // Item exists, increase quantity
            itemQuantities[itemIndex] += quantity;
        } else {
            // New item, check unique item limit
            if (uniqueItemCount >= MAX_UNIQUE_ITEMS) {
                System.out.println("Cannot carry more than " + MAX_UNIQUE_ITEMS + " unique items!");
                return false;
            }
            
            // Add new item
            uniqueItems[uniqueItemCount] = item;
            itemQuantities[uniqueItemCount] = quantity;
            uniqueItemCount++;
        }
        
        // Update total count
        totalItemCount += quantity;
        
        System.out.println("Added " + quantity + " " + item.getName() + "(s) to inventory");
        return true;
    }
    
    /**
     * Switches a Pokémon from storage to lineup
     */
    public boolean switchPokemonFromStorage(int storageIndex, int lineupIndex) {
        if (storageIndex < 0 || storageIndex >= storageCount) {
            System.out.println("Invalid storage index!");
            return false;
        }
        
        if (lineupIndex < 0 || lineupIndex >= lineupCount) {
            System.out.println("Invalid lineup index!");
            return false;
        }
        
        // Swap Pokémon
        Pokemon temp = lineup[lineupIndex];
        lineup[lineupIndex] = storage[storageIndex];
        storage[storageIndex] = temp;
        
        System.out.println("Successfully switched Pokémon!");
        return true;
    }
    
    /**
     * Moves a Pokémon from lineup to storage
     */
    public boolean movePokemonToStorage(int lineupIndex) {
        if (lineupIndex < 0 || lineupIndex >= lineupCount) {
            System.out.println("Invalid lineup index!");
            return false;
        }
        
        if (storageCount >= maxStorage) {
            System.out.println("Storage is full!");
            return false;
        }
        
        // Move Pokémon to storage
        storage[storageCount] = lineup[lineupIndex];
        storageCount++;
        
        // Shift lineup array
        for (int i = lineupIndex; i < lineupCount - 1; i++) {
            lineup[i] = lineup[i + 1];
        }
        lineup[lineupCount - 1] = null;
        lineupCount--;
        
        System.out.println("Pokémon moved to storage!");
        return true;
    }
    
    /**
     * Releases a Pokémon from the trainer's collection
     */
    public boolean releasePokemon(int lineupIndex) {
        if (lineupIndex < 0 || lineupIndex >= lineupCount) {
            System.out.println("Invalid lineup index!");
            return false;
        }
        
        String pokemonName = lineup[lineupIndex].getName();
        
        // Shift lineup array
        for (int i = lineupIndex; i < lineupCount - 1; i++) {
            lineup[i] = lineup[i + 1];
        }
        lineup[lineupCount - 1] = null;
        lineupCount--;
        
        System.out.println(pokemonName + " has been released!");
        return true;
    }
    
    /**
     * Teaches a move to a Pokémon
     */
    public boolean teachMove(int pokemonIndex, Move move, int replaceIndex) {
        if (pokemonIndex < 0 || pokemonIndex >= lineupCount) {
            System.out.println("Invalid Pokémon index!");
            return false;
        }
        
        Pokemon pokemon = lineup[pokemonIndex];
        boolean learned = pokemon.learnMove(move, replaceIndex);
        
        if (learned) {
            System.out.println(pokemon.getName() + " learned " + move.getName() + "!");
        } else {
            System.out.println(pokemon.getName() + " cannot learn " + move.getName() + "!");
        }
        
        return learned;
    }
    
    /**
     * Finds the index of an item in the inventory
     */
    private int findItemIndex(Item item) {
        for (int i = 0; i < uniqueItemCount; i++) {
            if (uniqueItems[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Removes an item from the inventory when quantity reaches 0
     */
    private void removeItemFromInventory(int index) {
        for (int i = index; i < uniqueItemCount - 1; i++) {
            uniqueItems[i] = uniqueItems[i + 1];
            itemQuantities[i] = itemQuantities[i + 1];
        }
        uniqueItems[uniqueItemCount - 1] = null;
        itemQuantities[uniqueItemCount - 1] = 0;
        uniqueItemCount--;
    }
    
    // Getters
    public int getTrainerID() { return trainerID; }
    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public String getSex() { return sex; }
    public String getHometown() { return hometown; }
    public String getDescription() { return description; }
    public int getMoney() { return money; }
    public Pokemon[] getLineup() { return lineup; }
    public int getLineupCount() { return lineupCount; }
    public Pokemon[] getStorage() { return storage; }
    public int getStorageCount() { return storageCount; }
    public Item[] getUniqueItems() { return uniqueItems; }
    public int[] getItemQuantities() { return itemQuantities; }
    public int getUniqueItemCount() { return uniqueItemCount; }
    public int getTotalItemCount() { return totalItemCount; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public void setSex(String sex) { this.sex = sex; }
    public void setHometown(String hometown) { this.hometown = hometown; }
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Checks if this trainer matches a search query
     */
    public boolean matchesSearch(String query) {
        String lowerQuery = query.toLowerCase();
        return name.toLowerCase().contains(lowerQuery) ||
               hometown.toLowerCase().contains(lowerQuery) ||
               description.toLowerCase().contains(lowerQuery) ||
               String.valueOf(trainerID).contains(query);
    }
    
    /**
     * Returns a string representation of the trainer
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trainer ID: ").append(trainerID).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Birthdate: ").append(birthdate).append("\n");
        sb.append("Sex: ").append(sex).append("\n");
        sb.append("Hometown: ").append(hometown).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Money: ₽").append(money).append("\n");
        sb.append("Pokémon in Lineup: ").append(lineupCount).append("/").append(MAX_LINEUP).append("\n");
        sb.append("Pokémon in Storage: ").append(storageCount).append("\n");
        sb.append("Items: ").append(totalItemCount).append("/").append(MAX_TOTAL_ITEMS);
        sb.append(" (").append(uniqueItemCount).append("/").append(MAX_UNIQUE_ITEMS).append(" unique)\n");
        return sb.toString();
    }
    
    /**
     * Evolves a Pokémon by level up
     */
    private void evolveByLevel(Pokemon pokemon) {
        if (pokemon.getEvolvesTo() == -1 || pokemon.getCurrentLevel() < pokemon.getEvolutionLevel()) {
            return;
        }
        
        // Find the evolution Pokemon by Pokedex number
        Pokemon evolution = findPokemonByPokedexNumber(pokemon.getEvolvesTo());
        if (evolution != null) {
            String oldName = pokemon.getName();
            pokemon.evolve(evolution);
            System.out.println(oldName + " evolved into " + pokemon.getName() + "!");
        } else {
            System.out.println(pokemon.getName() + " is ready to evolve, but evolution data not found.");
        }
    }
    
    /**
     * Evolves a Pokémon using an evolution stone
     */
    private void evolveByStone(Pokemon pokemon, Item stone) {
        String stoneName = stone.getName();
        String pokemonType = pokemon.getType1();
        
        // Check if stone matches Pokémon type and Pokemon can evolve
        boolean canEvolve = false;
        if (stoneName.equals("Fire Stone") && pokemonType.equals("Fire")) canEvolve = true;
        if (stoneName.equals("Water Stone") && pokemonType.equals("Water")) canEvolve = true;
        if (stoneName.equals("Thunder Stone") && pokemonType.equals("Electric")) canEvolve = true;
        if (stoneName.equals("Leaf Stone") && pokemonType.equals("Grass")) canEvolve = true;
        
        if (canEvolve && pokemon.getEvolvesTo() != -1) {
            // Find the evolution Pokemon by Pokedex number
            Pokemon evolution = findPokemonByPokedexNumber(pokemon.getEvolvesTo());
            if (evolution != null) {
                String oldName = pokemon.getName();
                pokemon.evolve(evolution);
                System.out.println(oldName + " evolved into " + pokemon.getName() + " using " + stoneName + "!");
            } else {
                System.out.println(pokemon.getName() + " can evolve, but evolution data not found.");
            }
        } else {
            System.out.println("The " + stoneName + " has no effect on " + pokemon.getName() + ".");
        }
    }
    
    /**
     * Releases a Pokémon from storage
     */
    public boolean releasePokemonFromStorage(int storageIndex) {
        if (storageIndex < 0 || storageIndex >= storageCount) {
            System.out.println("Invalid storage index!");
            return false;
        }
        
        String pokemonName = storage[storageIndex].getName();
        
        // Shift storage array
        for (int i = storageIndex; i < storageCount - 1; i++) {
            storage[i] = storage[i + 1];
        }
        storage[storageCount - 1] = null;
        storageCount--;
        
        System.out.println(pokemonName + " has been released from storage!");
        return true;
    }
    
    public static int getTrainerCount() { return trainerCount; }
    
    /**
     * Helper method to find Pokemon by Pokedex number (now uses the main Pokemon list)
     */
    private Pokemon findPokemonByPokedexNumber(int pokedexNumber) {
        // Use the static method from PokemonModel to search the main list
        return PokemonModel.findPokemonByPokedexNumberStatic(pokedexNumber, new PokemonModel().getPokemonList());
    }
}
