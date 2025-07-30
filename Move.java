/**
 * Represents a Pokémon move with its properties and effects
 */
public class Move {
    private String name;
    private String description;
    private String classification; // "HM" or "TM"
    private String type1;
    private String type2; // Optional
    
    /**
     * Constructor for creating a new move
     */
    public Move(String name, String description, String classification, String type1, String type2) {
        this.name = name;
        this.description = description;
        this.classification = classification;
        this.type1 = type1;
        this.type2 = type2;
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getClassification() { return classification; }
    public String getType1() { return type1; }
    public String getType2() { return type2; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setClassification(String classification) { this.classification = classification; }
    public void setType1(String type1) { this.type1 = type1; }
    public void setType2(String type2) { this.type2 = type2; }
    
    /**
     * Checks if this move matches a search query
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
     * Returns a string representation of the move
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
     * Checks if two moves are equal based on name
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return name.equals(move.name);
    }
}
