import java.util.*;
import java.io.*;

/**
 * Enhanced Pokédex System - All-in-One Implementation
 * Consolidates menu interface, system logic, and data management
 */
public class Main {
    // Data storage using ArrayList
    private static List<Pokemon> pokemonList = new ArrayList<>();
    private static List<Move> moveList = new ArrayList<>();
    private static List<Item> itemList = new ArrayList<>();
    private static List<Trainer> trainerList = new ArrayList<>();

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Main entry point
     */
    public static void main(String[] args) {
        System.out.println("=== Enhanced Pokédex System ===");
        System.out.println("Welcome to the Pokémon League Database!");
        System.out.println();

        // Initialize system with default data
        initializeSystem();

        // Start main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    pokemonMenu();
                    break;
                case 2:
                    movesMenu();
                    break;
                case 3:
                    itemsMenu();
                    break;
                case 4:
                    trainersMenu();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-4.");
            }
        }

        System.out.println("Thank you for using the Enhanced Pokédex System!");
        scanner.close();
    }

    /**
     * Initialize the system with default data
     */
    private static void initializeSystem() {
        initializeDefaultMoves();
        initializeDefaultItems();
        initializeDefaultPokemon();
        initializeDefaultTrainers();

        System.out.println("Enhanced Pokédex System initialized successfully!");
        System.out.println("Loaded " + pokemonList.size() + " Pokémon, " + moveList.size() + " moves, " + itemList.size() + " items, " + trainerList.size() + " trainers");

        // Demonstrate held items on newly caught Pokémon
        demonstrateHeldItems();

        // Start main menu
        
    }

    /**
     * Display main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       ENHANCED POKÉDEX SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Pokémon Management");
        System.out.println("2. Moves Management");
        System.out.println("3. Items Management");
        System.out.println("4. Trainer Management");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    /**
     * Pokémon management menu
     */
    private static void pokemonMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("       POKÉMON MANAGEMENT");
            System.out.println("=".repeat(40));
            System.out.println("1. Add New Pokémon");
            System.out.println("2. View All Pokémon");
            System.out.println("3. Search Pokémon");
            System.out.println("4. Pokémon Cry Demo");
            System.out.println("5. Save Pokémon to CSV");
            System.out.println("6. Load Pokémon from CSV");
            System.out.println("0. Back to Main Menu");
            System.out.println("=".repeat(40));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addPokemonMenu();
                    break;
                case 2:
                    viewAllPokemon();
                    break;
                case 3:
                    searchPokemonMenu();
                    break;
                case 4:
                    pokemonCryDemo();
                    break;
                case 5:
                    savePokemonToCSV();
                    break;
                case 6:
                    loadPokemonFromCSV();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-6.");
            }
        }
    }

    /**
     * Add new Pokémon
     */
    private static void addPokemonMenu() {
        System.out.println("\n--- Add New Pokémon ---");

        int pokedexNumber = getIntInput("Enter Pokédex Number: ");
        String name = getStringInput("Enter Pokémon Name: ");
        String type1 = getStringInput("Enter Type 1: ");
        String type2 = getStringInput("Enter Type 2 (or press Enter for none): ");
        if (type2.trim().isEmpty()) type2 = null;

        int baseLevel = getIntInput("Enter Base Level: ");
        int evolvesFrom = getIntInput("Enter Evolves From (Pokédex Number, -1 for none): ");
        int evolvesTo = getIntInput("Enter Evolves To (Pokédex Number, -1 for none): ");
        int evolutionLevel = getIntInput("Enter Evolution Level (-1 if doesn't evolve): ");

        System.out.println("\n--- Base Stats ---");
        int baseHP = getIntInput("Enter Base HP: ");
        int baseAttack = getIntInput("Enter Base Attack: ");
        int baseDefense = getIntInput("Enter Base Defense: ");
        int baseSpeed = getIntInput("Enter Base Speed: ");

        boolean success = addPokemon(pokedexNumber, name, type1, type2, baseLevel,
                                   evolvesFrom, evolvesTo, evolutionLevel,
                                   baseHP, baseAttack, baseDefense, baseSpeed);

        if (success) {
            System.out.println("\nPokémon added successfully!");
        }
    }

    /**
     * View all Pokémon
     */
    private static void viewAllPokemon() {
        if (pokemonList.isEmpty()) {
            System.out.println("No Pokémon in the database.");
            return;
        }

        System.out.println("\n=== All Pokémon in Database ===");
        for (int i = 0; i < pokemonList.size(); i++) {
            System.out.println((i + 1) + ". " + pokemonList.get(i));
            System.out.println("---");
        }
    }

    /**
     * Search Pokémon
     */
    private static void searchPokemonMenu() {
        String query = getStringInput("\nEnter search term (name or type): ");
        searchPokemon(query);
    }

    /**
     * Pokémon cry demonstration
     */
    private static void pokemonCryDemo() {
        if (pokemonList.isEmpty()) {
            System.out.println("No Pokémon available for cry demo.");
            return;
        }

        System.out.println("\n--- Available Pokémon ---");
        for (int i = 0; i < pokemonList.size(); i++) {
            System.out.println((i + 1) + ". " + pokemonList.get(i).getName());
        }

        int choice = getIntInput("Select a Pokémon (number): ");
        if (choice > 0 && choice <= pokemonList.size()) {
            Pokemon selectedPokemon = pokemonList.get(choice - 1);
            System.out.println("\n--- Pokémon Cry Demo ---");
            selectedPokemon.cry();
        } else {
            System.out.println("Invalid selection!");
        }
    }

    /**
     * Moves management menu
     */
    private static void movesMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("       MOVES MANAGEMENT");
            System.out.println("=".repeat(40));
            System.out.println("1. Add New Move");
            System.out.println("2. View All Moves");
            System.out.println("3. Search Moves");
            System.out.println("4. Save Moves to CSV");
            System.out.println("5. Load Moves from CSV");
            System.out.println("0. Back to Main Menu");
            System.out.println("=".repeat(40));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addMoveMenu();
                    break;
                case 2:
                    viewAllMoves();
                    break;
                case 3:
                    searchMovesMenu();
                    break;
                case 4:
                    saveMovesToCSV();
                    break;
                case 5:
                    loadMovesFromCSV();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-5.");
            }
        }
    }

    /**
     * Add new move
     */
    private static void addMoveMenu() {
        System.out.println("\n--- Add New Move ---");

        String name = getStringInput("Enter Move Name: ");
        String description = getStringInput("Enter Description: ");
        String classification = getStringInput("Enter Classification (TM/HM): ");
        String type1 = getStringInput("Enter Type 1: ");
        String type2 = getStringInput("Enter Type 2 (or press Enter for none): ");
        if (type2.trim().isEmpty()) type2 = null;

        boolean success = addMove(name, description, classification, type1, type2);

        if (success) {
            System.out.println("\nMove added successfully!");
        }
    }

    /**
     * View all moves
     */
    private static void viewAllMoves() {
        if (moveList.isEmpty()) {
            System.out.println("No moves in the database.");
            return;
        }

        System.out.println("\n=== All Moves in Database ===");
        for (int i = 0; i < moveList.size(); i++) {
            System.out.println((i + 1) + ". " + moveList.get(i));
            System.out.println("---");
        }
    }

    /**
     * Search moves
     */
    private static void searchMovesMenu() {
        String query = getStringInput("\nEnter search term: ");
        searchMoves(query);
    }

    /**
     * Items management menu
     */
    private static void itemsMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("       ITEMS MANAGEMENT");
            System.out.println("=".repeat(40));
            System.out.println("1. Add New Item");
            System.out.println("2. View All Items");
            System.out.println("3. Search Items");
            System.out.println("0. Back to Main Menu");
            System.out.println("=".repeat(40));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addItemMenu();
                    break;
                case 2:
                    viewAllItems();
                    break;
                case 3:
                    searchItemsMenu();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-3.");
            }
        }
    }

    /**
     * Add new item
     */
    private static void addItemMenu() {
        System.out.println("\n--- Add New Item ---");

        String name = getStringInput("Enter Item Name: ");
        String category = getStringInput("Enter Category: ");
        String description = getStringInput("Enter Description: ");
        String effect = getStringInput("Enter Effect: ");
        int buyingPrice = getIntInput("Enter Buying Price: ");
        int sellingPrice = getIntInput("Enter Selling Price: ");

        boolean success = addItem(name, category, description, effect, buyingPrice, sellingPrice);

        if (success) {
            System.out.println("\nItem added successfully!");
        }
    }

    /**
     * View all items
     */
    private static void viewAllItems() {
        if (itemList.isEmpty()) {
            System.out.println("No items in the database.");
            return;
        }

        System.out.println("\n=== All Items in Database ===");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println((i + 1) + ". " + itemList.get(i));
            System.out.println("---");
        }
    }

    /**
     * Search items
     */
    private static void searchItemsMenu() {
        String query = getStringInput("\nEnter search term: ");
        searchItems(query);
    }

    /**
     * Trainers management menu
     */
    private static void trainersMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("       TRAINERS MANAGEMENT");
            System.out.println("=".repeat(40));
            System.out.println("1. Add New Trainer");
            System.out.println("2. View All Trainers");
            System.out.println("3. Search Trainers");
            System.out.println("4. Trainer Operations");
            System.out.println("0. Back to Main Menu");
            System.out.println("=".repeat(40));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addTrainerMenu();
                    break;
                case 2:
                    viewAllTrainers();
                    break;
                case 3:
                    searchTrainersMenu();
                    break;
                case 4:
                    trainerOperationsMenu();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-4.");
            }
        }
    }

    /**
     * Add new trainer
     */
    private static void addTrainerMenu() {
        System.out.println("\n--- Add New Trainer ---");

        String name = getStringInput("Enter Trainer Name: ");
        String birthdate = getStringInput("Enter Birthdate (DD/MM/YYYY): ");
        String sex = getStringInput("Enter Sex (M/F): ");
        String hometown = getStringInput("Enter Hometown: ");
        String description = getStringInput("Enter Description: ");

        boolean success = addTrainer(name, birthdate, sex, hometown, description);

        if (success) {
            System.out.println("\nTrainer added successfully!");
        }
    }

    /**
     * View all trainers
     */
    private static void viewAllTrainers() {
        if (trainerList.isEmpty()) {
            System.out.println("No trainers in the database.");
            return;
        }

        System.out.println("\n=== All Trainers in Database ===");
        for (int i = 0; i < trainerList.size(); i++) {
            System.out.println((i + 1) + ". " + trainerList.get(i));

            Trainer trainer = trainerList.get(i);
            System.out.println("Active Pokémon Lineup:");
            for (int j = 0; j < trainer.getLineupCount(); j++) {
                System.out.println("  " + (j + 1) + ". " + trainer.getLineup()[j].getName() + 
                                 " (Level " + trainer.getLineup()[j].getCurrentLevel() + ")");
            }

            System.out.println("Pokémon in Storage: " + trainer.getStorageCount());
            System.out.println("---");
        }
    }

    /**
     * Search trainers
     */
    private static void searchTrainersMenu() {
        String query = getStringInput("\nEnter search term: ");
        searchTrainers(query);
    }

    /**
     * Trainer operations menu
     */
    private static void trainerOperationsMenu() {
        if (trainerList.isEmpty()) {
            System.out.println("No trainers available.");
            return;
        }

        System.out.println("\n--- Select Trainer ---");
        for (int i = 0; i < trainerList.size(); i++) {
            Trainer trainer = trainerList.get(i);
            System.out.println((i + 1) + ". " + trainer.getName() + " (ID: " + trainer.getTrainerID() + ")");
        }

        int trainerChoice = getIntInput("Select trainer (number): ");
        if (trainerChoice < 1 || trainerChoice > trainerList.size()) {
            System.out.println("Invalid trainer selection!");
            return;
        }

        Trainer selectedTrainer = trainerList.get(trainerChoice - 1);

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Trainer Operations: " + selectedTrainer.getName() + " ---");
            System.out.println("Money: ₽" + selectedTrainer.getMoney());
            System.out.println("1. View Pokémon in Lineup");
            System.out.println("2. View Pokémon in Storage");
            System.out.println("3. View Specific Pokémon Details");
            System.out.println("4. Buy Item");
            System.out.println("5. Use Item on Pokémon");
            System.out.println("6. Add Pokémon to Lineup");
            System.out.println("7. Teach Move to Pokémon");
            System.out.println("8. Swap Pokémon (Storage ↔ Lineup)");
            System.out.println("9. Release Pokémon");
            System.out.println("10. View Trainer Details");
            System.out.println("0. Back");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewPokemonInLineup(selectedTrainer);
                    break;
                case 2:
                    viewPokemonInStorage(selectedTrainer);
                    break;
                case 3:
                    viewSpecificPokemonMenu(selectedTrainer);
                    break;
                case 4:
                    buyItemMenu(selectedTrainer);
                    break;
                case 5:
                    useItemMenu(selectedTrainer);
                    break;
                case 6:
                    addPokemonToLineupMenu(selectedTrainer);
                    break;
                case 7:
                    teachMoveMenu(selectedTrainer);
                    break;
                case 8:
                    swapPokemonMenu(selectedTrainer);
                    break;
                case 9:
                    releasePokemonMenu(selectedTrainer);
                    break;
                case 10:
                    viewTrainerDetailsMenu(selectedTrainer);
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // Core business logic methods

    /**
     * Add Pokémon to database
     */
    private static boolean addPokemon(int pokedexNumber, String name, String type1, String type2,
                                    int baseLevel, int evolvesFrom, int evolvesTo, int evolutionLevel,
                                    int baseHP, int baseAttack, int baseDefense, int baseSpeed) {

        // Check for duplicate Pokédex number
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getPokedexNumber() == pokedexNumber) {
                System.out.println("Error: Pokédex number " + pokedexNumber + " already exists!");
                return false;
            }
        }

        // Check for duplicate name
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().equalsIgnoreCase(name)) {
                System.out.println("Error: Pokémon name '" + name + "' already exists!");
                return false;
            }
        }

        Pokemon newPokemon = new Pokemon(pokedexNumber, name, type1, type2, baseLevel,
                                       evolvesFrom, evolvesTo, evolutionLevel,
                                       baseHP, baseAttack, baseDefense, baseSpeed);

        pokemonList.add(newPokemon);
        System.out.println("Successfully added " + name + " to the Pokédex!");
        return true;
    }

    /**
     * Search Pokémon by name or type
     */
    private static void searchPokemon(String query) {
        boolean found = false;
        String lowerQuery = query.toLowerCase();

        System.out.println("\n=== Search Results for: '" + query + "' ===");

        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().toLowerCase().contains(lowerQuery) ||
                pokemon.getType1().toLowerCase().contains(lowerQuery) ||
                (pokemon.getType2() != null && pokemon.getType2().toLowerCase().contains(lowerQuery))) {

                System.out.println(pokemon);
                System.out.println("---");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No Pokémon found matching '" + query + "'");
        }
    }

    /**
     * Add move to database
     */
    private static boolean addMove(String name, String description, String classification, String type1, String type2) {
        for (Move move : moveList) {
            if (move.getName().equalsIgnoreCase(name)) {
                System.out.println("Error: Move '" + name + "' already exists!");
                return false;
            }
        }

        Move newMove = new Move(name, description, classification, type1, type2);
        moveList.add(newMove);
        System.out.println("Successfully added move '" + name + "'!");
        return true;
    }

    /**
     * Search moves by keyword
     */
    private static void searchMoves(String query) {
        boolean found = false;

        System.out.println("\n=== Search Results for: '" + query + "' ===");

        for (Move move : moveList) {
            if (move.matchesSearch(query)) {
                System.out.println(move);
                System.out.println("---");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No moves found matching '" + query + "'");
        }
    }

    /**
     * Add item to database
     */
    private static boolean addItem(String name, String category, String description, String effect,
                                 int buyingPrice, int sellingPrice) {

        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.println("Error: Item '" + name + "' already exists!");
                return false;
            }
        }

        Item newItem = new Item(name, category, description, effect, buyingPrice, sellingPrice);
        itemList.add(newItem);
        System.out.println("Successfully added item '" + name + "'!");
        return true;
    }

    /**
     * Search items by keyword
     */
    private static void searchItems(String query) {
        boolean found = false;

        System.out.println("\n=== Search Results for: '" + query + "' ===");

        for (Item item : itemList) {
            if (item.matchesSearch(query)) {
                System.out.println(item);
                System.out.println("---");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No items found matching '" + query + "'");
        }
    }

    /**
     * Add trainer to database
     */
    private static boolean addTrainer(String name, String birthdate, String sex, String hometown, String description) {
        Trainer newTrainer = new Trainer(name, birthdate, sex, hometown, description);
        trainerList.add(newTrainer);
        System.out.println("Successfully added trainer '" + name + "' with ID: " + newTrainer.getTrainerID());
        return true;
    }

    /**
     * Search trainers by keyword
     */
    private static void searchTrainers(String query) {
        boolean found = false;

        System.out.println("\n=== Search Results for: '" + query + "' ===");

        for (Trainer trainer : trainerList) {
            if (trainer.matchesSearch(query)) {
                System.out.println(trainer);
                System.out.println("---");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No trainers found matching '" + query + "'");
        }
    }

    // Trainer operation helper menus
    private static void buyItemMenu(Trainer trainer) {
        System.out.println("\n--- Available Items for Purchase ---");
        int purchasableCount = 0;

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if (item.getBuyingPrice() > 0) {
                purchasableCount++;
                System.out.println(purchasableCount + ". " + item.getName() + " - ₽" + item.getBuyingPrice());
            }
        }

        if (purchasableCount == 0) {
            System.out.println("No items available for purchase.");
            return;
        }

        int itemChoice = getIntInput("Select item to buy (number): ");
        if (itemChoice < 1 || itemChoice > purchasableCount) {
            System.out.println("Invalid item selection!");
            return;
        }

        int currentCount = 0;
        Item selectedItem = null;
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if (item.getBuyingPrice() > 0) {
                currentCount++;
                if (currentCount == itemChoice) {
                    selectedItem = item;
                    break;
                }
            }
        }

        if (selectedItem != null) {
            int quantity = getIntInput("Enter quantity to buy: ");
            trainer.buyItem(selectedItem, quantity);
        }
    }

    private static void useItemMenu(Trainer trainer) {
        if (trainer.getUniqueItemCount() == 0) {
            System.out.println("No items in inventory.");
            return;
        }

        if (trainer.getLineupCount() == 0) {
            System.out.println("No Pokémon in lineup to use items on.");
            return;
        }

        System.out.println("\n--- Trainer's Items ---");
        for (int i = 0; i < trainer.getUniqueItemCount(); i++) {
            Item item = trainer.getUniqueItems()[i];
            int quantity = trainer.getItemQuantities()[i];
            System.out.println((i + 1) + ". " + item.getName() + " x" + quantity);
        }

        int itemChoice = getIntInput("Select item to use (number): ");
        if (itemChoice < 1 || itemChoice > trainer.getUniqueItemCount()) {
            System.out.println("Invalid item selection!");
            return;
        }

        Item selectedItem = trainer.getUniqueItems()[itemChoice - 1];

        System.out.println("\n--- Select Pokémon ---");
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
        }

        int pokemonChoice = getIntInput("Select Pokémon (number): ");
        if (pokemonChoice < 1 || pokemonChoice > trainer.getLineupCount()) {
            System.out.println("Invalid Pokémon selection!");
            return;
        }

        Pokemon selectedPokemon = trainer.getLineup()[pokemonChoice - 1];

        // Provide information about the item and its effect
        System.out.println("\nSelected Item: " + selectedItem.getName());
        System.out.println("Item Description: " + getItemDescription(selectedItem));

        // Check for evolution information
        String evolutionInfo = getEvolutionInfo(selectedPokemon, selectedItem);
        if (!evolutionInfo.isEmpty()) {
            System.out.println("Evolution Info: " + evolutionInfo);
        }

        String confirm = getStringInput("Use " + selectedItem.getName() + " on " + selectedPokemon.getName() + "? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            trainer.useItem(selectedItem, selectedPokemon);
        } else {
            System.out.println("Item use cancelled.");
        }
    }

    private static void addPokemonToLineupMenu(Trainer trainer) {
        if (pokemonList.isEmpty()) {
            System.out.println("No Pokémon available in database.");
            return;
        }

        System.out.println("\n--- Available Pokémon ---");
        for (int i = 0; i < pokemonList.size(); i++) {
            Pokemon pokemon = pokemonList.get(i);
            System.out.println((i + 1) + ". " + pokemon.getName() + " (#" + pokemon.getPokedexNumber() + ")");
        }

        int pokemonChoice = getIntInput("Select Pokémon to add (number): ");
        if (pokemonChoice < 1 || pokemonChoice > pokemonList.size()) {
            System.out.println("Invalid Pokémon selection!");
            return;
        }

        Pokemon selectedPokemon = pokemonList.get(pokemonChoice - 1);
        trainer.addPokemonToLineup(selectedPokemon);
    }

    private static void viewTrainerDetailsMenu(Trainer trainer) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(trainer.toString());

        System.out.println("\n--- Pokémon Lineup ---");
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
            System.out.println("   Stats - HP: " + pokemon.getCurrentHP() + ", Attack: " + pokemon.getCurrentAttack() +
                             ", Defense: " + pokemon.getCurrentDefense() + ", Speed: " + pokemon.getCurrentSpeed());
        }

        System.out.println("\n--- Items ---");
        for (int i = 0; i < trainer.getUniqueItemCount(); i++) {
            Item item = trainer.getUniqueItems()[i];
            int quantity = trainer.getItemQuantities()[i];
            System.out.println("- " + item.getName() + " x" + quantity);
        }
    }

    private static void teachMoveMenu(Trainer trainer) {
        if (trainer.getLineupCount() == 0) {
            System.out.println("No Pokémon in lineup.");
            return;
        }

        System.out.println("\n=== TEACH MOVE TO POKÉMON ===");
        System.out.println("Select Pokémon:");
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
        }

        int pokemonChoice = getIntInput("\nSelect Pokémon (number): ");
        if (pokemonChoice < 1 || pokemonChoice > trainer.getLineupCount()) {
            System.out.println("Invalid Pokémon selection!");
            return;
        }

        Pokemon selectedPokemon = trainer.getLineup()[pokemonChoice - 1];

        System.out.println("\n=== AVAILABLE MOVES ===");
        System.out.println("HM = Hidden Machine (permanent), TM = Technical Machine (permanent)");
        for (int i = 0; i < moveList.size(); i++) {
            Move move = moveList.get(i);
            String moveType = move.getClassification().equals("HM") ? "🔹 HM" : "🔸 TM";
            System.out.println((i + 1) + ". " + move.getName() + " (" + moveType + ")");
        }

        int moveChoice = getIntInput("\nSelect move to teach (number): ");
        if (moveChoice < 1 || moveChoice > moveList.size()) {
            System.out.println("Invalid move selection!");
            return;
        }

        Move selectedMove = moveList.get(moveChoice - 1);

        // Check if Pokémon already knows 4 moves
        if (selectedPokemon.getMoveCount() >= 4) {
            System.out.println("\n" + selectedPokemon.getName() + " already knows 4 moves!");
            System.out.println("Current moves:");
            for (int i = 0; i < selectedPokemon.getMoveCount(); i++) {
                Move move = selectedPokemon.getMoveSet()[i];
                String moveType = move.getClassification().equals("HM") ? "🔹 HM" : "🔸 TM";
                System.out.println((i + 1) + ". " + move.getName() + " (" + moveType + ")");
            }

            int replaceChoice = getIntInput("\nSelect move to replace (1-4), or 0 to cancel: ");
            if (replaceChoice == 0) {
                System.out.println("Teaching cancelled.");
                return;
            }

            if (replaceChoice < 1 || replaceChoice > 4) {
                System.out.println("Invalid choice!");
                return;
            }

            trainer.teachMove(pokemonChoice - 1, selectedMove, replaceChoice - 1);
        } else {
            trainer.teachMove(pokemonChoice - 1, selectedMove, -1);
        }
    }

    // CSV Save/Load methods
    private static void savePokemonToCSV() {
        String filename = getStringInput("Enter filename (e.g., pokemon.csv): ");
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }
        savePokemonData(pokemonList, filename);
    }

    private static void loadPokemonFromCSV() {
        String filename = getStringInput("Enter filename to load: ");
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }
        loadPokemonData(filename);
    }

    private static void saveMovesToCSV() {
        String filename = getStringInput("Enter filename (e.g., moves.csv): ");
        if (!filename.endsWith(".csv")){
            filename += ".csv";
        }
        saveMovesToCSVFile(filename);
    }

    private static void loadMovesFromCSV() {
        String filename = getStringInput("Enter filename to load: ");
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }
        loadMovesFromCSVFile(filename);
    }

    /**
     * Save Pokémon data to CSV
     */
    private static void savePokemonData(List<Pokemon> list, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Number,Name,Type1,Type2,BaseLevel,EvolvesFrom,EvolvesTo,EvolutionLevel,HP,Attack,Defense,Speed,Moves,HeldItem");
            for (Pokemon p : list) {
                writer.println(p.formatToCSV());
            }
            System.out.println("Saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    /**
     * Load Pokémon data from CSV
     */
    private static void loadPokemonData(String filename) {
        pokemonList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();

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

                        Pokemon pokemon = new Pokemon(pokedexNumber, name, type1, type2,                                                    baseLevel, evolvesFrom, evolvesTo,
                                                    evolutionLevel, hp, attack, defense, speed);

                        pokemonList.add(pokemon);

                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing numbers in line: " + line);
                    }
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }

            System.out.println("Loaded " + pokemonList.size() + " Pokemon from " + filename);

        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
        }
    }

    /**
     * Save moves to CSV
     */
    private static void saveMovesToCSVFile(String filename) {
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
            System.out.println("Saved " + moveList.size() + " moves to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    /**
     * Load moves from CSV
     */
    private static void loadMovesFromCSVFile(String filename) {
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
            System.out.println("Loaded moves from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    // Initialization methods for default data
    private static void initializeDefaultMoves() {
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

    private static void initializeDefaultItems() {
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

    private static void initializeDefaultPokemon() {
        // Generation 1 starters and evolutions
        addPokemon(1, "Bulbasaur", "Grass", "Poison", 5, -1, 2, 16, 45, 49, 49, 45);
        addPokemon(2, "Ivysaur", "Grass", "Poison", 16, 1, 3, 32, 60, 62, 63, 60);
        addPokemon(3, "Venusaur", "Grass", "Poison", 32, 2, -1, -1, 80, 82, 83, 80);

        addPokemon(4, "Charmander", "Fire", null, 5, -1, 5, 16, 39, 52, 43, 65);
        addPokemon(5, "Charmeleon", "Fire", null, 16, 4, 6, 36, 58, 64, 58, 80);
        addPokemon(6, "Charizard", "Fire", "Flying", 36, 5, -1, -1, 78, 84, 78, 100);

        addPokemon(7, "Squirtle", "Water", null, 5, -1, 8, 16, 44, 48, 65, 43);
        addPokemon(8, "Wartortle", "Water", null, 16, 7, 9, 36, 59, 63, 80, 58);
        addPokemon(9, "Blastoise", "Water", null, 36, 8, -1, -1, 79, 83, 100, 78);

        // Additional Pokémon
        addPokemon(25, "Pikachu", "Electric", null, 5, 172, 26, -1, 35, 55, 40, 90);
        addPokemon(26, "Raichu", "Electric", null, 22, 25, -1, -1, 60, 90, 55, 110);
        addPokemon(172, "Pichu", "Electric", null, 1, -1, 25, 15, 20, 40, 15, 60);

        // Add more Pokémon as needed...
        addPokemon(133, "Eevee", "Normal", null, 8, -1, -1, -1, 55, 55, 50, 55);
        addPokemon(134, "Vaporeon", "Water", null, 8, 133, -1, -1, 130, 65, 60, 65);
        addPokemon(135, "Jolteon", "Electric", null, 8, 133, -1, -1, 65, 65, 60, 130);
        addPokemon(136, "Flareon", "Fire", null, 8, 133, -1, -1, 65, 130, 60, 65);

        // Evolution stone Pokémon
        addPokemon(58, "Growlithe", "Fire", null, 15, -1, 59, -1, 55, 70, 45, 60);
        addPokemon(59, "Arcanine", "Fire", null, 35, 58, -1, -1, 90, 110, 80, 95);

        // Legendary birds
        addPokemon(144, "Articuno", "Ice", "Flying", 50, -1, -1, -1, 90, 85, 100, 85);
        addPokemon(145, "Zapdos", "Electric", "Flying", 50, -1, -1, -1, 90, 90, 85, 100);
        addPokemon(146, "Moltres", "Fire", "Flying", 50, -1, -1, -1, 90, 100, 90, 90);

        // Dragon line
        addPokemon(147, "Dratini", "Dragon", null, 10, -1, 148, 30, 41, 64, 45, 50);
        addPokemon(148, "Dragonair", "Dragon", null, 30, 147, 149, 55, 61, 84, 65, 70);
        addPokemon(149, "Dragonite", "Dragon", "Flying", 55, 148, -1, -1, 91, 134, 95, 80);
    }

    private static void initializeDefaultTrainers() {
        // Trainer 1 - Ash
        addTrainer("Ash Ketchum", "01/01/2000", "Male", "Pallet Town", "A young trainer from Pallet Town");
        Trainer ash = trainerList.get(0);

        // Add Pokémon and setup as before...
        Pokemon pikachu = findPokemonByName("Pikachu");
        Pokemon charizard = findPokemonByName("Charizard");
        Pokemon blastoise = findPokemonByName("Blastoise");
        Pokemon venusaur = findPokemonByName("Venusaur");
        Pokemon dragonite = findPokemonByName("Dragonite");

        // Add Pokémon to lineup
        if (pikachu != null) ash.addPokemonToLineup(pikachu);
        if (charizard != null) ash.addPokemonToLineup(charizard);
        if (blastoise != null) ash.addPokemonToLineup(blastoise);
        if (venusaur != null) ash.addPokemonToLineup(venusaur);
        if (dragonite != null) ash.addPokemonToLineup(dragonite);

        // Add some Pokémon to storage for testing
        Pokemon eevee = findPokemonByName("Eevee");
        Pokemon articuno = findPokemonByName("Articuno");
        Pokemon dratini = findPokemonByName("Dratini");
        if (eevee != null) ash.addPokemonToStorage(eevee);
        if (articuno != null) ash.addPokemonToStorage(articuno);
        if (dratini != null) ash.addPokemonToStorage(dratini);

        // Add items
        Item masterBall = findItemByName("Master Ball");
        if (masterBall != null) ash.addItem(masterBall, 99);

        Item hpUp = findItemByName("HP Up");
        if (hpUp != null) ash.addItem(hpUp, 5);

        Item rareCandy = findItemByName("Rare Candy");
        if (rareCandy != null) ash.addItem(rareCandy, 10);

        Item fireStone = findItemByName("Fire Stone");
        if (fireStone != null) ash.addItem(fireStone, 3);
    }

    // New menu functions for trainer operations
    private static void viewPokemonInLineup(Trainer trainer) {
        if (trainer.getLineupCount() == 0) {
            System.out.println("No Pokémon in lineup.");
            return;
        }

        System.out.println("\n=== Pokémon in Lineup ===");
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (#" + pokemon.getPokedexNumber() + ")");
            System.out.println("   Type: " + pokemon.getType1() + (pokemon.getType2() != null ? "/" + pokemon.getType2() : ""));
            System.out.println("   Level: " + pokemon.getCurrentLevel());
            System.out.println("   Stats - HP: " + pokemon.getCurrentHP() + ", Attack: " + pokemon.getCurrentAttack() + 
                             ", Defense: " + pokemon.getCurrentDefense() + ", Speed: " + pokemon.getCurrentSpeed());
            if (pokemon.getHeldItem() != null) {
                System.out.println("   Holding: " + pokemon.getHeldItem().getName());
            }
            System.out.println("   Moves: ");
            for (int j = 0; j < pokemon.getMoveCount(); j++) {
                Move move = pokemon.getMoveSet()[j];
                System.out.println("     - " + move.getName() + " (" + move.getClassification() + ")");
            }
            System.out.println();
        }
    }

    private static void viewPokemonInStorage(Trainer trainer) {
        if (trainer.getStorageCount() == 0) {
            System.out.println("No Pokémon in storage.");
            return;
        }

        System.out.println("\n=== Pokémon in Storage ===");
        for (int i = 0; i < trainer.getStorageCount(); i++) {
            Pokemon pokemon = trainer.getStorage()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (#" + pokemon.getPokedexNumber() + ")");
            System.out.println("   Type: " + pokemon.getType1() + (pokemon.getType2() != null ? "/" + pokemon.getType2() : ""));
            System.out.println("   Level: " + pokemon.getCurrentLevel());
            System.out.println("   Stats - HP: " + pokemon.getCurrentHP() + ", Attack: " + pokemon.getCurrentAttack() + 
                             ", Defense: " + pokemon.getCurrentDefense() + ", Speed: " + pokemon.getCurrentSpeed());
            if (pokemon.getHeldItem() != null) {
                System.out.println("   Holding: " + pokemon.getHeldItem().getName());
            }
            System.out.println("   Moves: ");
            for (int j = 0; j < pokemon.getMoveCount(); j++) {
                Move move = pokemon.getMoveSet()[j];
                System.out.println("     - " + move.getName() + " (" + move.getClassification() + ")");
            }
            System.out.println();
        }
    }

    private static void viewSpecificPokemonMenu(Trainer trainer) {
        int totalPokemon = trainer.getLineupCount() + trainer.getStorageCount();
        if (totalPokemon == 0) {
            System.out.println("No Pokémon available.");
            return;
        }

        System.out.println("\n--- Select Pokémon to View ---");
        System.out.println("Lineup:");
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ") [Lineup]");
        }

        System.out.println("Storage:");
        for (int i = 0; i < trainer.getStorageCount(); i++) {
            Pokemon pokemon = trainer.getStorage()[i];
            System.out.println((trainer.getLineupCount() + i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ") [Storage]");
        }

        int choice = getIntInput("Select Pokémon (number): ");
        Pokemon selectedPokemon = null;
        String location = "";

        if (choice >= 1 && choice <= trainer.getLineupCount()) {
            selectedPokemon = trainer.getLineup()[choice - 1];
            location = "Lineup";
        } else if (choice > trainer.getLineupCount() && choice <= totalPokemon) {
            selectedPokemon = trainer.getStorage()[choice - trainer.getLineupCount() - 1];
            location = "Storage";
        } else {
            System.out.println("Invalid selection!");
            return;
        }

        // Display detailed information
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DETAILED POKÉMON INFORMATION");
        System.out.println("=".repeat(50));
        System.out.println("Name: " + selectedPokemon.getName());
        System.out.println("Pokédex Number: #" + selectedPokemon.getPokedexNumber());
        System.out.println("Type: " + selectedPokemon.getType1() + (selectedPokemon.getType2() != null ? "/" + selectedPokemon.getType2() : ""));
        System.out.println("Location: " + location);
        System.out.println("Current Level: " + selectedPokemon.getCurrentLevel());
        System.out.println();

        System.out.println("--- Current Stats ---");
        System.out.println("HP: " + selectedPokemon.getCurrentHP() + " (Base: " + selectedPokemon.getBaseHP() + ")");
        System.out.println("Attack: " + selectedPokemon.getCurrentAttack() + " (Base: " + selectedPokemon.getBaseAttack() + ")");
        System.out.println("Defense: " + selectedPokemon.getCurrentDefense() + " (Base: " + selectedPokemon.getBaseDefense() + ")");
        System.out.println("Speed: " + selectedPokemon.getCurrentSpeed() + " (Base: " + selectedPokemon.getBaseSpeed() + ")");
        System.out.println();

        System.out.println("--- Move Set ---");
        if (selectedPokemon.getMoveCount() == 0) {
            System.out.println("No moves learned.");
        } else {
            for (int i = 0; i < selectedPokemon.getMoveCount(); i++) {
                Move move = selectedPokemon.getMoveSet()[i];
                System.out.println((i + 1) + ". " + move.getName());
                System.out.println("   Classification: " + move.getClassification() + " (Technical/Hidden Machine)");
                System.out.println("   Type: " + move.getType1() + (move.getType2() != null ? "/" + move.getType2() : ""));
                System.out.println("   Description: " + move.getDescription());
                System.out.println();
            }
        }

        if (selectedPokemon.getHeldItem() != null) {
            System.out.println("--- Held Item ---");
            System.out.println("Item: " + selectedPokemon.getHeldItem().getName());
            System.out.println("Effect: " + selectedPokemon.getHeldItem().getEffect());
            System.out.println();
        }

        System.out.println("--- Evolution Information ---");
        if (selectedPokemon.getEvolvesFrom() != -1) {
            System.out.println("Evolves from: Pokédex #" + selectedPokemon.getEvolvesFrom());
        }
        if (selectedPokemon.getEvolvesTo() != -1) {
            System.out.println("Evolves to: Pokédex #" + selectedPokemon.getEvolvesTo());
            if (selectedPokemon.getEvolutionLevel() != -1) {
                System.out.println("Evolution Level: " + selectedPokemon.getEvolutionLevel());
            }
        }
        if (selectedPokemon.getEvolvesTo() == -1) {
            System.out.println("This Pokémon does not evolve further.");
        }
        System.out.println("=".repeat(50));
    }

    private static void swapPokemonMenu(Trainer trainer) {
        if (trainer.getStorageCount() == 0) {
            System.out.println("No Pokémon in storage to swap.");
            return;
        }

        if (trainer.getLineupCount() == 0) {
            System.out.println("No Pokémon in lineup to swap.");
            return;
        }

        System.out.println("\n--- Swap Pokémon Between Storage and Lineup ---");

        System.out.println("Storage Pokémon:");
        for (int i = 0; i < trainer.getStorageCount(); i++) {
            Pokemon pokemon = trainer.getStorage()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
        }

        int storageChoice = getIntInput("Select Pokémon from storage (number): ");
        if (storageChoice < 1 || storageChoice > trainer.getStorageCount()) {
            System.out.println("Invalid storage selection!");
            return;
        }

        System.out.println("\nLineup Pokémon:");
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
        }

        int lineupChoice = getIntInput("Select Pokémon from lineup to swap (number): ");
        if (lineupChoice < 1 || lineupChoice > trainer.getLineupCount()) {
            System.out.println("Invalid lineup selection!");
            return;
        }

        trainer.switchPokemonFromStorage(storageChoice - 1, lineupChoice - 1);
    }

    private static void releasePokemonMenu(Trainer trainer) {
        System.out.println("\n--- Release Pokémon ---");
        System.out.println("1. Release from Lineup");
        System.out.println("2. Release from Storage");
        System.out.println("0. Cancel");

        int choice = getIntInput("Select option: ");

        switch (choice) {
            case 1:
                if (trainer.getLineupCount() == 0) {
                    System.out.println("No Pokémon in lineup.");
                    return;
                }

                System.out.println("\nLineup Pokémon:");
                for (int i = 0; i < trainer.getLineupCount(); i++) {
                    Pokemon pokemon = trainer.getLineup()[i];
                    System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
                }

                int lineupChoice = getIntInput("Select Pokémon to release (number): ");
                if (lineupChoice >= 1 && lineupChoice <= trainer.getLineupCount()) {
                    String pokemonName = trainer.getLineup()[lineupChoice - 1].getName();
                    String confirm = getStringInput("Are you sure you want to release " + pokemonName + "? (yes/no): ");
                    if (confirm.equalsIgnoreCase("yes")) {
                        trainer.releasePokemon(lineupChoice - 1);
                    } else {
                        System.out.println("Release cancelled.");
                    }
                } else {
                    System.out.println("Invalid selection!");
                }
                break;

            case 2:
                if (trainer.getStorageCount() == 0) {
                    System.out.println("No Pokémon in storage.");
                    return;
                }

                System.out.println("\nStorage Pokémon:");
                for (int i = 0; i < trainer.getStorageCount(); i++) {
                    Pokemon pokemon = trainer.getStorage()[i];
                    System.out.println((i + 1) + ". " + pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")");
                }

                int storageChoice = getIntInput("Select Pokémon to release (number): ");
                if (storageChoice >= 1 && storageChoice <= trainer.getStorageCount()) {
                    String pokemonName = trainer.getStorage()[storageChoice - 1].getName();
                    String confirm = getStringInput("Are you sure you want to release " + pokemonName + "? (yes/no): ");
                    if (confirm.equalsIgnoreCase("yes")) {
                        trainer.releasePokemonFromStorage(storageChoice - 1);
                    } else {
                        System.out.println("Release cancelled.");
                    }
                } else {
                    System.out.println("Invalid selection!");
                }
                break;

            case 0:
                System.out.println("Release cancelled.");
                break;

            default:
                System.out.println("Invalid choice!");
        }
    }

    // Helper methods
    private static Pokemon findPokemonByName(String name) {
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().equals(name)) {
                return new Pokemon(pokemon); // Return copy
            }
        }
        return null;
    }

    // Helper method to find items by name
    private static Item findItemByName(String name) {
        for (Item item : itemList) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Get a user-friendly description of what an item does
     */
    private static String getItemDescription(Item item) {
        String name = item.getName();
        String category = item.getCategory();

        if (name.equals("Rare Candy")) {
            return "Levels up Pokémon by 1 (may trigger evolution!)";
        } else if (category.equals("Evolution Stone")) {
            return "Evolves compatible Pokémon (check type match)";
        } else if (category.equals("Vitamin")) {
            return "Permanently boosts stats";
        } else if (category.equals("Feather")) {
            return "Small stat boost";
        } else if (item.isHoldable()) {
            return "Can be held by Pokémon";
        } else {
            return "Usable item";
        }
    }

    /**
     * Get evolution information for a Pokémon when using specific items
     */
    private static String getEvolutionInfo(Pokemon pokemon, Item item) {
        StringBuilder info = new StringBuilder();

        if (item.getName().equals("Rare Candy")) {
            if (pokemon.getEvolvesTo() != -1 && pokemon.getCurrentLevel() >= pokemon.getEvolutionLevel() - 1) {
                info.append(" 🌟 READY TO EVOLVE!");
            } else if (pokemon.getEvolvesTo() != -1) {
                int levelsNeeded = pokemon.getEvolutionLevel() - pokemon.getCurrentLevel() - 1;
                info.append(" (Evolves at level ").append(pokemon.getEvolutionLevel()).append(", ").append(levelsNeeded).append(" more levels)");
            } else {
                info.append(" (Cannot evolve)");
            }
        } else if (item.getCategory().equals("Evolution Stone")) {
            String stoneName = item.getName();
            String pokemonType = pokemon.getType1();
            boolean compatible = false;

            if (stoneName.equals("Fire Stone") && pokemonType.equals("Fire")) compatible = true;
            if (stoneName.equals("Water Stone") && pokemonType.equals("Water")) compatible = true;
            if (stoneName.equals("Thunder Stone") && pokemonType.equals("Electric")) compatible = true;
            if (stoneName.equals("Leaf Stone") && pokemonType.equals("Grass")) compatible = true;

            if (compatible && pokemon.getEvolvesTo() != -1) {
                info.append(" ⚡ CAN EVOLVE with this stone!");
            } else if (compatible) {
                info.append(" (Stone compatible but no evolution data)");
            } else {
                info.append(" (Not compatible with ").append(stoneName).append(")");
            }
        }

        return info.toString();
    }

    // Input helper methods
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getTrainerCount() {
        return trainerList.size();
    }

    private static void showMainMenu() {
        // Start main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    pokemonMenu();
                    break;
                case 2:
                    movesMenu();
                    break;
                case 3:
                    itemsMenu();
                    break;
                case 4:
                    trainersMenu();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 0-4.");
            }
        }

        System.out.println("Thank you for using the Enhanced Pokédex System!");
    }

    private static void demonstrateHeldItems() {
        System.out.println("\n🎮 === HELD ITEMS DEMONSTRATION === 🎮");

        if (trainerList.isEmpty()) {
            System.out.println("No trainers available for demonstration.");
            return;
        }

        Trainer ash = trainerList.get(0);

        // Catch a new Growlithe and give it a Fire Stone
        Pokemon growlithe = findPokemonByName("Growlithe");
        if (growlithe != null) {
            System.out.println("🌟 Ash caught a wild Growlithe!");
            ash.addPokemonToStorage(growlithe);

            // Get the newly caught Growlithe from storage
            Pokemon newGrowlithe = ash.getStorage()[ash.getStorageCount() - 1];

            System.out.println("📦 Before giving held item:");
            if(newGrowlithe.getHeldItem() == null) {
                 System.out.println("   " + newGrowlithe.getName() + " is not holding anything.");
            }
            System.out.println("   Held Item: " + (newGrowlithe.getHeldItem() != null ? newGrowlithe.getHeldItem().getName() : "None"));

            // Give Fire Stone as held item
            Item fireStone = findItemByName("Fire Stone");
            if (fireStone != null) {
                System.out.println("\n💎 Ash gives " + newGrowlithe.getName() + " a " + fireStone.getName() + " to hold!");
                
                // First, let's show the Pokemon holding the item before using it
                newGrowlithe.setHeldItem(fireStone);
                System.out.println("✨ " + newGrowlithe.getName() + " is now holding: " + newGrowlithe.getHeldItem().getName());
                System.out.println("   Item Effect: " + newGrowlithe.getHeldItem().getEffect());
                
                // Now use the item (which will consume it and trigger evolution)
                ash.useItem(fireStone, newGrowlithe);
                
                System.out.println("🌟 After evolution:");
                System.out.println("   " + newGrowlithe.getName() + " evolved and the Fire Stone was consumed!");
                System.out.println("   Currently holding: " + (newGrowlithe.getHeldItem() != null ? newGrowlithe.getHeldItem().getName() : "Nothing"));
            }
        }

        // Also demonstrate with Master Ball (another holdable item)
        Pokemon eevee = null;
        for (int i = 0; i < ash.getStorageCount(); i++) {
            if (ash.getStorage()[i].getName().equals("Eevee")) {
                eevee = ash.getStorage()[i];
                break;
            }
        }

        if (eevee != null) {
            Item masterBall = findItemByName("Master Ball");
            if (masterBall != null) {
                System.out.println("\n🎾 Giving " + eevee.getName() + " a " + masterBall.getName() + " to hold!");
                eevee.setHeldItem(masterBall);
                System.out.println("   " + eevee.getName() + " is now holding: " + eevee.getHeldItem().getName());
                System.out.println("   " + eevee.getName() + " can use this for catching other Pokémon!");
            }
        }

        System.out.println("\n🎮 === END DEMONSTRATION === 🎮\n");
    }
}