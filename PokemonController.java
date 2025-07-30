import java.util.List;

/**
 * Controller class for the Enhanced Pokédex System
 * Handles communication between Model and View
 */
public class PokemonController {
    private PokemonModel model;
    
    public PokemonController(PokemonModel model) {
        this.model = model;
    }
    
    // Pokémon operations
    public boolean addPokemon(int pokedexNumber, String name, String type1, String type2,
                            int baseLevel, int evolvesFrom, int evolvesTo, int evolutionLevel,
                            int baseHP, int baseAttack, int baseDefense, int baseSpeed) {
        return model.addPokemon(pokedexNumber, name, type1, type2, baseLevel, evolvesFrom, 
                               evolvesTo, evolutionLevel, baseHP, baseAttack, baseDefense, baseSpeed);
    }
    
    public List<Pokemon> searchPokemon(String query) {
        return model.searchPokemon(query);
    }
    
    public List<Pokemon> getAllPokemon() {
        return model.getPokemonList();
    }
    
    public void savePokemonToCSV(String filename) {
        model.savePokemonToCSV(filename);
    }
    
    public void loadPokemonFromCSV(String filename) {
        model.loadPokemonFromCSV(filename);
    }
    
    // Move operations
    public boolean addMove(String name, String description, String classification, String type1, String type2) {
        return model.addMove(name, description, classification, type1, type2);
    }
    
    public List<Move> searchMoves(String query) {
        return model.searchMoves(query);
    }
    
    public List<Move> getAllMoves() {
        return model.getMoveList();
    }
    
    public void saveMovesToCSV(String filename) {
        model.saveMovesToCSV(filename);
    }
    
    public void loadMovesFromCSV(String filename) {
        model.loadMovesFromCSV(filename);
    }
    
    // Item operations
    public boolean addItem(String name, String category, String description, String effect,
                         int buyingPrice, int sellingPrice) {
        return model.addItem(name, category, description, effect, buyingPrice, sellingPrice);
    }
    
    public List<Item> searchItems(String query) {
        return model.searchItems(query);
    }
    
    public List<Item> getAllItems() {
        return model.getItemList();
    }
    
    // Trainer operations
    public boolean addTrainer(String name, String birthdate, String sex, String hometown, String description) {
        return model.addTrainer(name, birthdate, sex, hometown, description);
    }
    
    public List<Trainer> searchTrainers(String query) {
        return model.searchTrainers(query);
    }
    
    public List<Trainer> getAllTrainers() {
        return model.getTrainerList();
    }
    
    public void saveTrainersToCSV(String filename) {
        model.saveTrainersToCSV(filename);
    }
    
    public void loadTrainersFromCSV(String filename) {
        model.loadTrainersFromCSV(filename);
    }
    
    // Helper methods
    public Pokemon findPokemonByName(String name) {
        return model.findPokemonByName(name);
    }
    
    public Item findItemByName(String name) {
        return model.findItemByName(name);
    }
} 