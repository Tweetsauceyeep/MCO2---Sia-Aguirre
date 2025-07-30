/**
 * Represents a Pokémon move with its properties and effects.
 * This class encapsulates all information about a move including
 * its name, description, classification (HM/TM), and type(s).
 * 
 * Each move has:
 * - A unique name and description
 * - A classification (HM for Hidden Machine, TM for Technical Machine)
 * - One or two types that determine compatibility with Pokémon
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class Move {
    /** The name of the move */
    private String name;
    /** A description of what the move does */
    private String description;
    /** The classification of the move (HM or TM) */
    private String classification;
    /** The primary type of the move */
    private String type1;
    /** The secondary type of the move (optional, can be null) */
    private String type2;
    
    /**
     * Constructor for creating a new move.
     * Initializes all move properties with the provided values.
     * 
     * @param name The name of the move
     * @param description A description of the move's effect
     * @param classification The classification (HM or TM)
     * @param type1 The primary type of the move
     * @param type2 The secondary type of the move (can be null)
     */
    public Move(String name, String description, String classification, String type1, String type2) {
        this.name = name;
        this.description = description;
        this.classification = classification;
        this.type1 = type1;
        this.type2 = type2;
    }
    
    // Getters
    
    /**
     * Gets the name of the move.
     * @return The move's name
     */
    public String getName() { return name; }
    
    /**
     * Gets the description of the move.
     * @return The move's description
     */
    public String getDescription() { return description; }
    
    /**
     * Gets the classification of the move.
     * @return The move's classification (HM or TM)
     */
    public String getClassification() { return classification; }
    
    /**
     * Gets the primary type of the move.
     * @return The primary type
     */
    public String getType1() { return type1; }
    
    /**
     * Gets the secondary type of the move.
     * @return The secondary type, or null if none
     */
    public String getType2() { return type2; }
    
    // Setters
    
    /**
     * Sets the name of the move.
     * @param name The new name for the move
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Sets the description of the move.
     * @param description The new description for the move
     */
    public void setDescription(String description) { this.description = description; }
    
    /**
     * Sets the classification of the move.
     * @param classification The new classification (HM or TM)
     */
    public void setClassification(String classification) { this.classification = classification; }
    
    /**
     * Sets the primary type of the move.
     * @param type1 The new primary type
     */
    public void setType1(String type1) { this.type1 = type1; }
    
    /**
     * Sets the secondary type of the move.
     * @param type2 The new secondary type (can be null)
     */
    public void setType2(String type2) { this.type2 = type2; }
    
    /**
     * Checks if this move matches a search query.
     * Performs a case-insensitive search across all move properties
     * including name, description, classification, and types.
     * 
     * @param query The search query to match against
     * @return true if the move matches the query, false otherwise
     */
    public boolean matchesSearch(String query) {
        String lowerQuery = query.toLowerCase();
        return name.toLowerCase().contains(lowerQuery) ||
               description.toLowerCase().contains(lowerQuery) ||
               classification.toLowerCase().contains(lowerQuery) ||
               type1.toLowerCase().contains(lowerQuery) ||
               (type2 != null && type2.toLowerCase().contains(lowerQuery));
    }
    
    /**
     * Returns a formatted string representation of the move.
     * Includes the name, classification, type(s), and description.
     * 
     * @return A formatted string representation of the move
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(classification).append(")\n");
        sb.append("Type: ").append(type1);
        if (type2 != null && !type2.isEmpty()) {
            sb.append("/").append(type2);
        }
        sb.append("\n");
        sb.append("Description: ").append(description).append("\n");
        return sb.toString();
    }
    
    /**
     * Checks if two moves are equal based on their names.
     * Two moves are considered equal if they have the same name.
     * 
     * @param obj The object to compare with
     * @return true if the moves have the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return name.equals(move.name);
    }
}
