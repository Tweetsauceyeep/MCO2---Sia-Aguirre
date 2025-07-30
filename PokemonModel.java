import java.util.*;
import java.io.*;

/**
 * Model class for the Enhanced Pokédex System.
 * This class contains all data and business logic for the application,
 * following the MVC (Model-View-Controller) design pattern.
 * 
 * The Model manages:
 * - Pokémon data (list, search, add operations)
 * - Move data (list, search, add operations)
 * - Item data (list, search, add operations)
 * - Trainer data (list, search, add operations)
 * - CSV file operations for data persistence
 * 
 * All data operations go through this class, ensuring
 * proper data management and business rule enforcement.
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class PokemonModel {
    /** List to store all Pokémon data */
    private List<Pokemon> pokemonList = new ArrayList<>();
    /** List to store all move data */
    private List<Move> moveList = new ArrayList<>();
    /** List to store all item data */
    private List<Item> itemList = new ArrayList<>();
    /** List to store all trainer data */
    private List<Trainer> trainerList = new ArrayList<>();
    
    /**
     * Constructor - initializes the system with default data.
     * Loads all default moves, items, Pokémon, and trainers
     * from CSV files or creates them if files don't exist.
     */
    public PokemonModel() {
        // Always initialize all default data
        initializeSystem();
    }
    
    /**
     * Initialize the system with default data.
     * Sets up all default moves and items, then loads
     * Pokémon and trainer data from CSV files.
     */
    private void initializeSystem() {
        // Always initialize moves and items with hardcoded defaults
        initializeDefaultMoves();
        initializeDefaultItems();
        
        // Load Pokémon from CSV (contains all default data)
        try {
            loadPokemonFromCSV("pokemon_data.csv");
            System.out.println("Loaded Pokémon from pokemon_data.csv");
        } catch (Exception e) {
            System.err.println("Error loading Pokémon from CSV: " + e.getMessage());
            throw new RuntimeException("Failed to load Pokémon data from pokemon_data.csv", e);
        }
        
        // Load trainers from CSV (contains all default data)
        try {
            loadTrainersFromCSV("trainers_data.csv");
            System.out.println("Loaded trainers from trainers_data.csv");
        } catch (Exception e) {
            System.err.println("Error loading trainers from CSV: " + e.getMessage());
            throw new RuntimeException("Failed to load trainer data from trainers_data.csv", e);
        }
    }
    
    // Core business logic methods
    
    /**
     * Add Pokémon to database.
     * Checks for duplicate Pokédex numbers and names before adding.
     * 
     * @param pokedexNumber The unique Pokédex number
     * @param name The name of the Pokémon
     * @param type1 The primary type
     * @param type2 The secondary type (can be null)
     * @param baseLevel The base level when encountered
     * @param evolvesFrom Pokédex number of pre-evolution (-1 if none)
     * @param evolvesTo Pokédex number of evolution (-1 if none)
     * @param evolutionLevel Level required for evolution
     * @param baseHP Base HP statistic
     * @param baseAttack Base Attack statistic
     * @param baseDefense Base Defense statistic
     * @param baseSpeed Base Speed statistic
     * @return true if the Pokémon was added successfully, false otherwise
     */
    public boolean addPokemon(int pokedexNumber, String name, String type1, String type2,
                            int baseLevel, int evolvesFrom, int evolvesTo, int evolutionLevel,
                            int baseHP, int baseAttack, int baseDefense, int baseSpeed) {

        // Check for duplicate Pokédex number
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getPokedexNumber() == pokedexNumber) {
                return false;
            }
        }

        // Check for duplicate name
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        Pokemon newPokemon = new Pokemon(pokedexNumber, name, type1, type2, baseLevel,
                                       evolvesFrom, evolvesTo, evolutionLevel,
                                       baseHP, baseAttack, baseDefense, baseSpeed);

        pokemonList.add(newPokemon);
        
        return true;
    }

    /**
     * Search Pokémon by name or type
     */
    public List<Pokemon> searchPokemon(String query) {
        List<Pokemon> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().toLowerCase().contains(lowerQuery) ||
                pokemon.getType1().toLowerCase().contains(lowerQuery) ||
                (pokemon.getType2() != null && pokemon.getType2().toLowerCase().contains(lowerQuery))) {
                results.add(pokemon);
            }
        }

        return results;
    }

    /**
     * Add move to database
     */
    public boolean addMove(String name, String description, String classification, String type1, String type2) {
        for (Move move : moveList) {
            if (move.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        Move newMove = new Move(name, description, classification, type1, type2);
        moveList.add(newMove);
        
        return true;
    }

    /**
     * Search moves by keyword
     */
    public List<Move> searchMoves(String query) {
        List<Move> results = new ArrayList<>();

        for (Move move : moveList) {
            if (move.matchesSearch(query)) {
                results.add(move);
            }
        }

        return results;
    }

    /**
     * Add item to database
     */
    public boolean addItem(String name, String category, String description, String effect,
                         int buyingPrice, int sellingPrice) {

        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        Item newItem = new Item(name, category, description, effect, buyingPrice, sellingPrice);
        itemList.add(newItem);
        
        return true;
    }

    /**
     * Search items by keyword
     */
    public List<Item> searchItems(String query) {
        List<Item> results = new ArrayList<>();

        for (Item item : itemList) {
            if (item.matchesSearch(query)) {
                results.add(item);
            }
        }

        return results;
    }

    /**
     * Add trainer to database
     */
    public boolean addTrainer(String name, String birthdate, String sex, String hometown, String description) {
        Trainer newTrainer = new Trainer(name, birthdate, sex, hometown, description);
        trainerList.add(newTrainer);
        
        return true;
    }

    /**
     * Search trainers by keyword
     */
    public List<Trainer> searchTrainers(String query) {
        List<Trainer> results = new ArrayList<>();

        for (Trainer trainer : trainerList) {
            if (trainer.matchesSearch(query)) {
                results.add(trainer);
            }
        }

        return results;
    }

    // CSV Save/Load methods
    public void savePokemonToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Number,Name,Type1,Type2,BaseLevel,EvolvesFrom,EvolvesTo,EvolutionLevel,HP,Attack,Defense,Speed,Moves,HeldItem");
            for (Pokemon p : pokemonList) {
                writer.println(p.formatToCSV());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving: " + e.getMessage());
        }
    }

    public void loadPokemonFromCSV(String filename) {
        pokemonList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);

                if (parts.length >= 12) {
                    try {
                        int pokedexNumber = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        String type1 = parts[2];
                        String type2 = parts[3].isEmpty() ? null : parts[3];
                        int baseLevel = Integer.parseInt(parts[4]);
                        int evolvesFrom = Integer.parseInt(parts[5]);
                        int evolvesTo = Integer.parseInt(parts[6]);
                        int evolutionLevel = Integer.parseInt(parts[7]);
                        int hp = Integer.parseInt(parts[8]);
                        int attack = Integer.parseInt(parts[9]);
                        int defense = Integer.parseInt(parts[10]);
                        int speed = Integer.parseInt(parts[11]);

                        Pokemon pokemon = new Pokemon(pokedexNumber, name, type1, type2,
                                                   baseLevel, evolvesFrom, evolvesTo,
                                                   evolutionLevel, hp, attack, defense, speed);

                        pokemonList.add(pokemon);

                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing numbers in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename);
        }
    }

    public void saveMovesToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Name,Description,Classification,Type1,Type2");
            for (Move move : moveList) {
                writer.printf("%s,%s,%s,%s,%s%n",
                    move.getName(),
                    move.getDescription(),
                    move.getClassification(),
                    move.getType1(),
                    move.getType2() != null ? move.getType2() : ""
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving: " + e.getMessage());
        }
    }

    public void loadMovesFromCSV(String filename) {
        moveList.clear();
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip header
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",", 5);
                if (parts.length >= 4) {
                    String name = parts[0];
                    String description = parts[1];
                    String classification = parts[2];
                    String type1 = parts[3];
                    String type2 = parts.length > 4 && !parts[4].isEmpty() ? parts[4] : null;
                    moveList.add(new Move(name, description, classification, type1, type2));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filename);
        }
    }

    public void saveTrainersToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Name,Birthdate,Sex,Hometown,Description,LineupPokemon,StoragePokemon,Items");
            for (Trainer trainer : trainerList) {
                // Format lineup Pokémon
                StringBuilder lineupStr = new StringBuilder();
                for (int i = 0; i < trainer.getLineupCount(); i++) {
                    if (i > 0) lineupStr.append(";");
                    lineupStr.append(trainer.getLineup()[i].getName());
                }
                
                // Format storage Pokémon
                StringBuilder storageStr = new StringBuilder();
                for (int i = 0; i < trainer.getStorageCount(); i++) {
                    if (i > 0) storageStr.append(";");
                    storageStr.append(trainer.getStorage()[i].getName());
                }
                
                // Format items
                StringBuilder itemsStr = new StringBuilder();
                for (int i = 0; i < trainer.getUniqueItemCount(); i++) {
                    if (i > 0) itemsStr.append(";");
                    itemsStr.append(trainer.getUniqueItems()[i].getName()).append(":").append(trainer.getItemQuantities()[i]);
                }
                
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    trainer.getName(),
                    trainer.getBirthdate(),
                    trainer.getSex(),
                    trainer.getHometown(),
                    trainer.getDescription(),
                    lineupStr.toString(),
                    storageStr.toString(),
                    itemsStr.toString()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving: " + e.getMessage());
        }
    }

    public void loadTrainersFromCSV(String filename) {
        trainerList.clear();
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip header
            }
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",", 8);
                if (parts.length >= 5) {
                    String name = parts[0];
                    String birthdate = parts[1];
                    String sex = parts[2];
                    String hometown = parts[3];
                    String description = parts[4];
                    
                    // Create trainer
                    Trainer trainer = new Trainer(name, birthdate, sex, hometown, description);
                    
                    // Add lineup Pokémon
                    if (parts.length > 5 && !parts[5].isEmpty()) {
                        String[] lineupPokemon = parts[5].split(";");
                        for (String pokemonName : lineupPokemon) {
                            Pokemon pokemon = findPokemonByName(pokemonName.trim());
                            if (pokemon != null) {
                                trainer.addPokemonToLineup(pokemon);
                            }
                        }
                    }
                    
                    // Add storage Pokémon
                    if (parts.length > 6 && !parts[6].isEmpty()) {
                        String[] storagePokemon = parts[6].split(";");
                        for (String pokemonName : storagePokemon) {
                            Pokemon pokemon = findPokemonByName(pokemonName.trim());
                            if (pokemon != null) {
                                trainer.addPokemonToStorage(pokemon);
                            }
                        }
                    }
                    
                    // Add items
                    if (parts.length > 7 && !parts[7].isEmpty()) {
                        String[] items = parts[7].split(";");
                        for (String itemStr : items) {
                            String[] itemParts = itemStr.split(":");
                            if (itemParts.length == 2) {
                                String itemName = itemParts[0].trim();
                                int quantity = Integer.parseInt(itemParts[1].trim());
                                Item item = findItemByName(itemName);
                                if (item != null) {
                                    trainer.addItem(item, quantity);
                                }
                            }
                        }
                    }
                    
                    trainerList.add(trainer);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filename);
        }
    }

    // Helper methods
    public Pokemon findPokemonByName(String name) {
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().equals(name)) {
                return new Pokemon(pokemon); // Return copy
            }
        }
        return null;
    }

    public Item findItemByName(String name) {
        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static Pokemon findPokemonByPokedexNumberStatic(int pokedexNumber, List<Pokemon> pokemonList) {
        for (Pokemon p : pokemonList) {
            if (p.getPokedexNumber() == pokedexNumber) {
                // Return a new instance to avoid mutating the original
                return new Pokemon(p.getPokedexNumber(), p.getName(), p.getType1(), p.getType2(),
                                  p.getBaseLevel(), p.getEvolvesFrom(), p.getEvolvesTo(), p.getEvolutionLevel(),
                                  p.getBaseHP(), p.getBaseAttack(), p.getBaseDefense(), p.getBaseSpeed());
            }
        }
        return null;
    }

    // Getters
    public List<Pokemon> getPokemonList() { return pokemonList; }
    public List<Move> getMoveList() { return moveList; }
    public List<Item> getItemList() { return itemList; }
    public List<Trainer> getTrainerList() { return trainerList; }

    // Initialization methods for default data
    private void initializeDefaultMoves() {
        addMove("Tackle", "A basic physical attack that damages the opponent", "TM", "Normal", null);
        addMove("Defend", "Increases the user's defense temporarily", "TM", "Normal", null);
        addMove("Thunderbolt", "A powerful electric attack that may paralyze", "TM", "Electric", null);
        addMove("Flamethrower", "A fire attack that may burn the opponent", "TM", "Fire", null);
        addMove("Water Gun", "A basic water attack", "TM", "Water", null);
        addMove("Vine Whip", "A grass attack using vines", "TM", "Grass", null);
        addMove("Psychic", "A powerful psychic attack", "TM", "Psychic", null);
        addMove("Earthquake", "A ground attack that hits all Pokémon", "TM", "Ground", null);
        addMove("Ice Beam", "An ice attack that may freeze", "TM", "Ice", null);
        addMove("Shadow Ball", "A ghost attack that may lower special defense", "TM", "Ghost", null);
        addMove("Thunder Wave", "A move that paralyzes the opponent", "TM", "Electric", null);
        addMove("Toxic", "A move that badly poisons the opponent", "TM", "Poison", null);

        // HM moves
        addMove("Cut", "Cuts down trees and grass", "HM", "Normal", null);
        addMove("Fly", "Flies to previously visited locations", "HM", "Flying", null);
        addMove("Surf", "Travels across water", "HM", "Water", null);
        addMove("Strength", "Moves heavy objects", "HM", "Normal", null);
        addMove("Rock Smash", "Smashes rocks to clear paths", "HM", "Fighting", null);
        addMove("Waterfall", "Climbs up waterfalls", "HM", "Water", null);
    }

    private void initializeDefaultItems() {
        // Vitamins
        addItem("HP Up", "Vitamin", "A nutritious drink for Pokémon.", "+10 HP EVs", 10000, 5000);
        addItem("Protein", "Vitamin", "A nutritious drink for Pokémon.", "+10 Attack EVs", 10000, 5000);
        addItem("Iron", "Vitamin", "A nutritious drink for Pokémon.", "+10 Defense EVs", 10000, 5000);
        addItem("Carbos", "Vitamin", "A nutritious drink for Pokémon.", "+10 Speed EVs", 10000, 5000);
        addItem("Zinc", "Vitamin", "A nutritious drink for Pokémon.", "+10 Special Defense EVs", 10000, 5000);

        // Special items
        addItem("Rare Candy", "Leveling Item", "A candy that is packed with energy.", "Increases level by 1", 0, 2400);

        // Feathers
        addItem("Health Feather", "Feather", "A feather that slightly increases HP.", "+1 HP EV", 300, 150);
        addItem("Muscle Feather", "Feather", "A feather that slightly increases Attack.", "+1 Attack EV", 300, 150);
        addItem("Resist Feather", "Feather", "A feather that slightly increases Defense.", "+1 Defense EV", 300, 150);
        addItem("Swift Feather", "Feather", "A feather that slightly increases Speed.", "+1 Speed EV", 300, 150);

        // Evolution Stones
        addItem("Fire Stone", "Evolution Stone", "A stone that radiates heat.", "Evolves Fire-type Pokémon", 3000, 1500);
        addItem("Water Stone", "Evolution Stone", "A stone with a blue, watery appearance.", "Evolves Water-type Pokémon", 3000, 1500);
        addItem("Thunder Stone", "Evolution Stone", "A stone that sparkles with electricity.", "Evolves Electric-type Pokémon", 3000, 1500);
        addItem("Leaf Stone", "Evolution Stone", "A stone with a leaf pattern.", "Evolves Grass-type Pokémon", 3000, 1500);
        addItem("Moon Stone", "Evolution Stone", "A stone that glows faintly in the moonlight.", "Evolves certain Pokémon", 0, 1500);
        addItem("Sun Stone", "Evolution Stone", "A stone that glows like the sun.", "Evolves certain Pokémon", 3000, 1500);
        addItem("Shiny Stone", "Evolution Stone", "A stone that sparkles brightly.", "Evolves certain Pokémon", 3000, 1500);
        addItem("Dusk Stone", "Evolution Stone", "A dark stone that is ominous in appearance.", "Evolves certain Pokémon", 3000, 1500);
        addItem("Dawn Stone", "Evolution Stone", "A stone that sparkles like the morning sky.", "Evolves certain Pokémon", 3000, 1500);
        addItem("Ice Stone", "Evolution Stone", "A stone that is cold to the touch.", "Evolves Ice-type Pokémon", 3000, 1500);

        // Special item
        addItem("Master Ball", "Pokeball", "The ultimate ball with a 100% catch rate.", "Catches any Pokémon", 99, 50);
    }

    private void initializeDefaultPokemon() {
        // Pokémon data is now loaded from pokemon_data.csv
        // This method is kept for backward compatibility but is no longer used
    }

    private void initializeDefaultTrainers() {
        // Trainer data is now loaded from trainers_data.csv
        // This method is kept for backward compatibility but is no longer used
    }
} 