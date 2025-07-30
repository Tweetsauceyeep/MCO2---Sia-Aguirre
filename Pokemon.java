/**
 * Represents a Pokémon with all its attributes and behaviors.
 * This class implements proper encapsulation and OOP principles,
 * providing a complete representation of a Pokémon creature.
 * 
 * Each Pokémon instance contains:
 * - Basic information (name, types, Pokédex number)
 * - Evolution data (evolution chain and requirements)
 * - Base and current statistics
 * - Move set management (up to 4 moves)
 * - Item holding capabilities
 * - EV (Effort Value) tracking
 * 
 * The class supports Pokémon evolution, move learning,
 * item effects, and statistical modifications.
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class Pokemon {
    /** Static counter for tracking total Pokémon instances created */
    private static int pokemonCount = 0;
    
    // Core attributes
    /** The unique Pokédex number of this Pokémon */
    private int pokedexNumber;
    /** The name of this Pokémon */
    private String name;
    /** The primary type of this Pokémon */
    private String type1;
    /** The secondary type of this Pokémon (optional, can be null) */
    private String type2;
    /** The base level when this Pokémon is first encountered */
    private int baseLevel;
    /** Pokédex number of the Pokémon this evolves from (-1 if none) */
    private int evolvesFrom;
    /** Pokédex number of the Pokémon this evolves into (-1 if none) */
    private int evolvesTo;
    /** The level at which this Pokémon evolves */
    private int evolutionLevel;
    
    // Base stats
    /** Base HP (Hit Points) statistic */
    private int baseHP;
    /** Base Attack statistic */
    private int baseAttack;
    /** Base Defense statistic */
    private int baseDefense;
    /** Base Speed statistic */
    private int baseSpeed;
    
    // Current stats (can be modified by items)
    /** Current HP value (can be modified by items and effects) */
    private int currentHP;
    /** Current Attack value (can be modified by items and effects) */
    private int currentAttack;
    /** Current Defense value (can be modified by items and effects) */
    private int currentDefense;
    /** Current Speed value (can be modified by items and effects) */
    private int currentSpeed;
    /** Current level of this Pokémon */
    private int currentLevel;
    
    // Move set (maximum 4 moves)
    /** Array containing up to 4 moves this Pokémon knows */
    private Move[] moveSet;
    /** Number of moves currently known by this Pokémon */
    private int moveCount;
    
    // Held item
    /** Item currently held by this Pokémon (can be null) */
    private Item heldItem;
    
    // EV stats for item effects
    /** Effort Value points in HP */
    private int hpEV;
    /** Effort Value points in Attack */
    private int attackEV;
    /** Effort Value points in Defense */
    private int defenseEV;
    /** Effort Value points in Speed */
    private int speedEV;
    /** Effort Value points in Special Defense */
    private int specialDefenseEV;
    
    /**
     * Constructor for creating a new Pokémon instance.
     * Initializes all attributes and sets up default moves.
     * 
     * @param pokedexNumber The unique Pokédex number
     * @param name The name of the Pokémon
     * @param type1 The primary type
     * @param type2 The secondary type (can be null or empty)
     * @param baseLevel The base level when encountered
     * @param evolvesFrom Pokédex number of pre-evolution (-1 if none)
     * @param evolvesTo Pokédex number of evolution (-1 if none)
     * @param evolutionLevel Level required for evolution
     * @param baseHP Base HP statistic
     * @param baseAttack Base Attack statistic
     * @param baseDefense Base Defense statistic
     * @param baseSpeed Base Speed statistic
     */
    public Pokemon(int pokedexNumber, String name, String type1, String type2, 
                   int baseLevel, int evolvesFrom, int evolvesTo, int evolutionLevel,
                   int baseHP, int baseAttack, int baseDefense, int baseSpeed) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.baseLevel = baseLevel;
        this.currentLevel = baseLevel;
        this.evolvesFrom = evolvesFrom;
        this.evolvesTo = evolvesTo;
        this.evolutionLevel = evolutionLevel;
        
        // Set base stats
        this.baseHP = baseHP;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
        
        // Initialize current stats to base stats
        this.currentHP = baseHP;
        this.currentAttack = baseAttack;
        this.currentDefense = baseDefense;
        this.currentSpeed = baseSpeed;
        
        // Initialize move set with default moves
        this.moveSet = new Move[4];
        this.moveCount = 0;
        
        // Add default moves "Tackle" and "Defend"
        addDefaultMoves();
        
        // Initialize EVs
        this.hpEV = 0;
        this.attackEV = 0;
        this.defenseEV = 0;
        this.speedEV = 0;
        this.specialDefenseEV = 0;
        
        pokemonCount++;
    }
    
    /**
     * Copy constructor for creating a unique instance for trainers.
     * Creates a deep copy of the original Pokémon with all attributes.
     * 
     * @param original The Pokémon to copy from
     */
    public Pokemon(Pokemon original) {
        this.pokedexNumber = original.pokedexNumber;
        this.name = original.name;
        this.type1 = original.type1;
        this.type2 = original.type2;
        this.baseLevel = original.baseLevel;
        this.currentLevel = original.currentLevel;
        this.evolvesFrom = original.evolvesFrom;
        this.evolvesTo = original.evolvesTo;
        this.evolutionLevel = original.evolutionLevel;
        
        this.baseHP = original.baseHP;
        this.baseAttack = original.baseAttack;
        this.baseDefense = original.baseDefense;
        this.baseSpeed = original.baseSpeed;
        
        this.currentHP = original.currentHP;
        this.currentAttack = original.currentAttack;
        this.currentDefense = original.currentDefense;
        this.currentSpeed = original.currentSpeed;
        
        // Copy moves
        this.moveSet = new Move[4];
        this.moveCount = original.moveCount;
        for (int i = 0; i < moveCount; i++) {
            this.moveSet[i] = original.moveSet[i];
        }
        
        this.heldItem = original.heldItem;
        
        this.hpEV = original.hpEV;
        this.attackEV = original.attackEV;
        this.defenseEV = original.defenseEV;
        this.speedEV = original.speedEV;
        this.specialDefenseEV = original.specialDefenseEV;
    }
    
    /**
     * Adds default moves "Tackle" and "Defend" to new Pokémon.
     * This method is called during construction to ensure every Pokémon
     * starts with basic moves.
     */
    private void addDefaultMoves() {
        // Create default moves if they don't exist
        Move tackle = new Move("Tackle", "A basic physical attack", "TM", "Normal", null);
        Move defend = new Move("Defend", "Increases defense temporarily", "TM", "Normal", null);
        
        this.moveSet[0] = tackle;
        this.moveSet[1] = defend;
        this.moveCount = 2;
    }
    
    /**
     * Makes the Pokémon cry, displaying its characteristic sound.
     * In a real implementation, this would play an audio file.
     * Currently outputs the Pokémon's name in uppercase followed by "!"
     */
    public void cry() {
        System.out.println(name + " cries: \"" + name.toUpperCase() + "!\"");
        // In a real implementation, this would play an audio file
        System.out.println("*" + name + " makes its characteristic sound*");
    }
    
    /**
     * Teaches a new move to the Pokémon.
     * The move must be type-compatible with this Pokémon.
     * If the move set is full, a move must be replaced.
     * HM moves cannot be forgotten once learned.
     * 
     * @param move The move to learn
     * @param replaceIndex Index of move to replace if moveSet is full, -1 to add normally
     * @return true if move was learned successfully, false otherwise
     */
    public boolean learnMove(Move move, int replaceIndex) {
        // Check type compatibility
        if (!isCompatibleMove(move)) {
            return false;
        }
        
        // If there's space, add the move
        if (moveCount < 4) {
            moveSet[moveCount] = move;
            moveCount++;
            return true;
        }
        
        // If moveSet is full, replace the specified move (unless it's HM)
        if (replaceIndex >= 0 && replaceIndex < 4) {
            if (moveSet[replaceIndex].getClassification().equals("HM")) {
                return false; // Cannot forget HM moves
            }
            moveSet[replaceIndex] = move;
            return true;
        }
        
        return false;
    }
    
    /**
     * Checks if a move is compatible with this Pokémon.
     * Normal type moves can be learned by all Pokémon.
     * Other moves must share at least one type with the Pokémon.
     * 
     * @param move The move to check compatibility for
     * @return true if the move is compatible, false otherwise
     */
    private boolean isCompatibleMove(Move move) {
        String moveType1 = move.getType1();
        String moveType2 = move.getType2();
        
        // Normal type moves can be learned by all Pokémon
        if (moveType1.equals("Normal")) {
            return true;
        }
        
        // Move must share at least one type with the Pokémon
        return moveType1.equals(type1) || moveType1.equals(type2) ||
               (moveType2 != null && (moveType2.equals(type1) || moveType2.equals(type2)));
    }
    
    /**
     * Uses Rare Candy to level up the Pokémon.
     * Increases the current level by 1 and boosts all stats by 10%.
     * Checks if evolution should occur based on level requirements.
     * 
     * @return true if evolution should occur, false otherwise
     */
    public boolean useRareCandy() {
        currentLevel++;
        
        // Increase stats by 10%
        currentHP = (int)(currentHP * 1.1);
        currentAttack = (int)(currentAttack * 1.1);
        currentDefense = (int)(currentDefense * 1.1);
        currentSpeed = (int)(currentSpeed * 1.1);
        
        // Check for evolution
        if (evolvesTo != -1 && currentLevel >= evolutionLevel) {
            return true; // Indicates evolution should occur
        }
        
        return false;
    }
    
    /**
     * Evolves this Pokémon using evolution stone or level up.
     * Updates all base information to the evolved form while preserving
     * current stats if they are higher than the new base stats.
     * 
     * @param evolvedForm The evolved form of this Pokémon
     */
    public void evolve(Pokemon evolvedForm) {
        // Keep current stats if they're higher than base stats
        this.currentHP = Math.max(this.currentHP, evolvedForm.baseHP);
        this.currentAttack = Math.max(this.currentAttack, evolvedForm.baseAttack);
        this.currentDefense = Math.max(this.currentDefense, evolvedForm.baseDefense);
        this.currentSpeed = Math.max(this.currentSpeed, evolvedForm.baseSpeed);
        
        // Update base information
        this.pokedexNumber = evolvedForm.pokedexNumber;
        this.name = evolvedForm.name;
        this.type1 = evolvedForm.type1;
        this.type2 = evolvedForm.type2;
        this.baseHP = evolvedForm.baseHP;
        this.baseAttack = evolvedForm.baseAttack;
        this.baseDefense = evolvedForm.baseDefense;
        this.baseSpeed = evolvedForm.baseSpeed;
        this.evolvesFrom = evolvedForm.evolvesFrom;
        this.evolvesTo = evolvedForm.evolvesTo;
        this.evolutionLevel = evolvedForm.evolutionLevel;
        
        // Retain all moves and held items
    }
    
    /**
     * Applies item effects to the Pokémon.
     * Processes various item effects including HP, Attack, Defense, Speed,
     * and Special Defense EV boosts. Updates both EV values and current stats.
     * 
     * @param item The item whose effects should be applied
     */
    public void applyItemEffect(Item item) {
        String effect = item.getEffect();
        
        if (effect.contains("HP EV")) {
            if (effect.contains("+10")) {
                hpEV += 10;
                currentHP += 10;
            } else if (effect.contains("+1")) {
                hpEV += 1;
                currentHP += 1;
            }
        } else if (effect.contains("Attack EV")) {
            if (effect.contains("+10")) {
                attackEV += 10;
                currentAttack += 10;
            } else if (effect.contains("+1")) {
                attackEV += 1;
                currentAttack += 1;
            }
        } else if (effect.contains("Defense EV")) {
            if (effect.contains("+10")) {
                defenseEV += 10;
                currentDefense += 10;
            } else if (effect.contains("+1")) {
                defenseEV += 1;
                currentDefense += 1;
            }
        } else if (effect.contains("Speed EV")) {
            if (effect.contains("+10")) {
                speedEV += 10;
                currentSpeed += 10;
            } else if (effect.contains("+1")) {
                speedEV += 1;
                currentSpeed += 1;
            }
        } else if (effect.contains("Special Defense EV")) {
            if (effect.contains("+10")) {
                specialDefenseEV += 10;
            }
        }
    }
    
    // Getters and Setters
    
    /**
     * Gets the Pokédex number of this Pokémon.
     * @return The unique Pokédex number
     */
    public int getPokedexNumber() { return pokedexNumber; }
    
    /**
     * Gets the name of this Pokémon.
     * @return The Pokémon's name
     */
    public String getName() { return name; }
    
    /**
     * Gets the primary type of this Pokémon.
     * @return The primary type
     */
    public String getType1() { return type1; }
    
    /**
     * Gets the secondary type of this Pokémon.
     * @return The secondary type, or null if none
     */
    public String getType2() { return type2; }
    
    /**
     * Gets the base level of this Pokémon.
     * @return The base level when first encountered
     */
    public int getBaseLevel() { return baseLevel; }
    
    /**
     * Gets the current level of this Pokémon.
     * @return The current level
     */
    public int getCurrentLevel() { return currentLevel; }
    
    /**
     * Gets the Pokédex number of the Pokémon this evolves from.
     * @return The pre-evolution Pokédex number, or -1 if none
     */
    public int getEvolvesFrom() { return evolvesFrom; }
    
    /**
     * Gets the Pokédex number of the Pokémon this evolves into.
     * @return The evolution Pokédex number, or -1 if none
     */
    public int getEvolvesTo() { return evolvesTo; }
    
    /**
     * Gets the level required for evolution.
     * @return The evolution level requirement
     */
    public int getEvolutionLevel() { return evolutionLevel; }
    
    /**
     * Gets the base HP statistic.
     * @return The base HP value
     */
    public int getBaseHP() { return baseHP; }
    
    /**
     * Gets the base Attack statistic.
     * @return The base Attack value
     */
    public int getBaseAttack() { return baseAttack; }
    
    /**
     * Gets the base Defense statistic.
     * @return The base Defense value
     */
    public int getBaseDefense() { return baseDefense; }
    
    /**
     * Gets the base Speed statistic.
     * @return The base Speed value
     */
    public int getBaseSpeed() { return baseSpeed; }
    
    /**
     * Gets the current HP value.
     * @return The current HP value
     */
    public int getCurrentHP() { return currentHP; }
    
    /**
     * Gets the current Attack value.
     * @return The current Attack value
     */
    public int getCurrentAttack() { return currentAttack; }
    
    /**
     * Gets the current Defense value.
     * @return The current Defense value
     */
    public int getCurrentDefense() { return currentDefense; }
    
    /**
     * Gets the current Speed value.
     * @return The current Speed value
     */
    public int getCurrentSpeed() { return currentSpeed; }
    
    /**
     * Gets the move set of this Pokémon.
     * @return Array containing up to 4 moves
     */
    public Move[] getMoveSet() { return moveSet; }
    
    /**
     * Gets the number of moves currently known.
     * @return The number of moves known
     */
    public int getMoveCount() { return moveCount; }
    
    /**
     * Gets the item currently held by this Pokémon.
     * @return The held item, or null if none
     */
    public Item getHeldItem() { return heldItem; }
    
    /**
     * Sets the item held by this Pokémon.
     * @param item The item to hold
     */
    public void setHeldItem(Item item) { 
        this.heldItem = item; 
    }
    
    /**
     * Removes the item held by this Pokémon.
     */
    public void removeHeldItem() {
        this.heldItem = null;
    }
    
    /**
     * Gets the total number of Pokémon instances created.
     * @return The total count of Pokémon instances
     */
    public static int getPokemonCount() { return pokemonCount; }
    
    /**
     * Formats Pokémon data for CSV export.
     * Creates a comma-separated string containing all Pokémon attributes
     * including moves (separated by semicolons) and held item.
     * 
     * @return A CSV-formatted string representation of the Pokémon
     */
    public String formatToCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(pokedexNumber).append(",");
        sb.append(name).append(",");
        sb.append(type1).append(",");
        sb.append(type2 != null ? type2 : "").append(",");
        sb.append(baseLevel).append(",");
        sb.append(evolvesFrom).append(",");
        sb.append(evolvesTo).append(",");
        sb.append(evolutionLevel).append(",");
        sb.append(baseHP).append(",");
        sb.append(baseAttack).append(",");
        sb.append(baseDefense).append(",");
        sb.append(baseSpeed).append(",");
        
        // Format moves (separated by semicolons)
        StringBuilder movesStr = new StringBuilder();
        for (int i = 0; i < moveCount; i++) {
            movesStr.append(moveSet[i].getName());
            if (i < moveCount - 1) movesStr.append(";");
        }
        sb.append(movesStr.toString()).append(",");
        
        // Format held item
        sb.append(heldItem != null ? heldItem.getName() : "");
        
        return sb.toString();
    }

    /**
     * Returns a detailed string representation of the Pokémon.
     * Includes Pokédex number, name, types, level, stats, moves, and held item.
     * 
     * @return A formatted string representation of the Pokémon
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pokédex #").append(pokedexNumber).append(": ").append(name);
        sb.append(" (").append(type1);
        if (type2 != null && !type2.isEmpty()) {
            sb.append("/").append(type2);
        }
        sb.append(")\n");
        sb.append("Level: ").append(currentLevel).append("\n");
        sb.append("Stats - HP: ").append(currentHP).append(", Attack: ").append(currentAttack);
        sb.append(", Defense: ").append(currentDefense).append(", Speed: ").append(currentSpeed).append("\n");
        
        sb.append("Moves: ");
        for (int i = 0; i < moveCount; i++) {
            sb.append(moveSet[i].getName());
            if (i < moveCount - 1) sb.append(", ");
        }
        sb.append("\n");
        
        if (heldItem != null) {
            sb.append("Held Item: ").append(heldItem.getName()).append("\n");
        }
        
        return sb.toString();
    }
}
