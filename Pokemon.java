/**
 * Represents a Pokémon with all its attributes and behaviors
 * Implements proper encapsulation and OOP principles
 */
public class Pokemon {
    // Static counter for unique instances
    private static int pokemonCount = 0;
    
    // Core attributes
    private int pokedexNumber;
    private String name;
    private String type1;
    private String type2; // Optional
    private int baseLevel;
    private int evolvesFrom; // Pokédex number, -1 if none
    private int evolvesTo; // Pokédex number, -1 if none
    private int evolutionLevel;
    
    // Base stats
    private int baseHP;
    private int baseAttack;
    private int baseDefense;
    private int baseSpeed;
    
    // Current stats (can be modified by items)
    private int currentHP;
    private int currentAttack;
    private int currentDefense;
    private int currentSpeed;
    private int currentLevel;
    
    // Move set (maximum 4 moves)
    private Move[] moveSet;
    private int moveCount;
    
    // Held item
    private Item heldItem;
    
    // EV stats for item effects
    private int hpEV;
    private int attackEV;
    private int defenseEV;
    private int speedEV;
    private int specialDefenseEV;
    
    /**
     * Constructor for creating a new Pokémon
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
     * Copy constructor for trainer's unique instance
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
     * Adds default moves "Tackle" and "Defend" to new Pokémon
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
     * The Pokémon's cry method - plays the sound it makes
     */
    public void cry() {
        System.out.println(name + " cries: \"" + name.toUpperCase() + "!\"");
        // In a real implementation, this would play an audio file
        System.out.println("*" + name + " makes its characteristic sound*");
    }
    
    /**
     * Teaches a new move to the Pokémon
     * @param move The move to learn
     * @param replaceIndex Index of move to replace if moveSet is full, -1 to add normally
     * @return true if move was learned successfully
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
     * Checks if a move is compatible with this Pokémon
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
     * Uses Rare Candy to level up the Pokémon
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
     * Evolves this Pokémon using evolution stone or level up
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
     * Applies item effects to the Pokémon
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
    public int getPokedexNumber() { return pokedexNumber; }
    public String getName() { return name; }
    public String getType1() { return type1; }
    public String getType2() { return type2; }
    public int getBaseLevel() { return baseLevel; }
    public int getCurrentLevel() { return currentLevel; }
    public int getEvolvesFrom() { return evolvesFrom; }
    public int getEvolvesTo() { return evolvesTo; }
    public int getEvolutionLevel() { return evolutionLevel; }
    
    public int getBaseHP() { return baseHP; }
    public int getBaseAttack() { return baseAttack; }
    public int getBaseDefense() { return baseDefense; }
    public int getBaseSpeed() { return baseSpeed; }
    
    public int getCurrentHP() { return currentHP; }
    public int getCurrentAttack() { return currentAttack; }
    public int getCurrentDefense() { return currentDefense; }
    public int getCurrentSpeed() { return currentSpeed; }
    
    public Move[] getMoveSet() { return moveSet; }
    public int getMoveCount() { return moveCount; }
    public Item getHeldItem() { return heldItem; }
    
    public void setHeldItem(Item item) { 
        this.heldItem = item; 
    }
    
    public void removeHeldItem() {
        this.heldItem = null;
    }
    
    public static int getPokemonCount() { return pokemonCount; }
    
    /**
     * Formats Pokémon data for CSV export
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
     * Returns a string representation of the Pokémon
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
