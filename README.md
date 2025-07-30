# Enhanced Pokédex System - GUI Version

This is a Java Swing GUI implementation of the Enhanced Pokédex System following the MVC (Model-View-Controller) design pattern.

## Features

- **Pokémon Management**: Add, view, search, and manage Pokémon data
- **Moves Management**: Add, view, search, and manage Pokémon moves
- **Items Management**: Add, view, search, and manage items
- **Trainer Management**: Add trainers and perform various operations with their Pokémon and items
- **CSV Import/Export**: Save and load data in CSV format
- **Auto-Save Functionality**: Automatically saves data to CSV files when changes are made
- **Modern GUI**: Clean, intuitive interface with tabbed navigation

## How to Run

### Prerequisites
- Java 8 or higher
- All original class files (Pokemon.java, Move.java, Item.java, Trainer.java)

### Running the GUI
1. Compile all Java files:
   ```bash
   javac *.java
   ```

2. Run the GUI version:
   ```bash
   java PokemonGUI
   ```

### Running the Original Console Version
```bash
java Main
```

## GUI Features

### Auto-Save System
- **Automatic Data Persistence**: Data is automatically saved to CSV files when changes are made
- **Auto-Save Settings**: Configure auto-save through File → Auto-Save Settings menu
- **Auto-Save Directory**: Files are saved to the 'autosave' directory by default
- **Data Recovery**: Auto-saved data is automatically loaded when the application starts
- **Status Indicator**: Auto-save status is displayed in the status bar

### Pokémon Tab
- View all Pokémon in a table format
- Search Pokémon by name or type
- Add new Pokémon with detailed form
- Save/load Pokémon data to/from CSV
- Play Pokémon cries
- View detailed information in the bottom panel

### Moves Tab
- View all moves in a table format
- Search moves by name, type, or description
- Add new moves (TM/HM)
- Save/load moves data to/from CSV
- View detailed move information

### Items Tab
- View all items in a table format
- Search items by name, category, or effect
- Add new items with pricing information
- View detailed item information

### Trainers Tab
- View all trainers in a table format
- Search trainers by name, hometown, or description
- Add new trainers
- Access trainer operations through a detailed dialog:
  - View Pokémon in lineup and storage
  - Buy and sell items
  - Use items on Pokémon
  - Teach moves to Pokémon
  - Swap Pokémon between lineup and storage
  - Release Pokémon
  - View detailed trainer information

## Architecture

The GUI follows the MVC pattern:

- **Model** (`PokemonModel.java`): Contains all data and business logic
- **View** (`MainFrame.java`, `*Panel.java`): Handles the user interface
- **Controller** (`PokemonController.java`): Manages communication between Model and View

## File Structure

```
├── PokemonGUI.java          # Main GUI entry point
├── PokemonModel.java        # Model (data and business logic)
├── PokemonController.java   # Controller
├── MainFrame.java          # Main window frame
├── PokemonPanel.java       # Pokémon management panel
├── MovesPanel.java         # Moves management panel
├── ItemsPanel.java         # Items management panel
├── TrainersPanel.java      # Trainers management panel
├── Main.java              # Original console version
├── Pokemon.java           # Pokémon class
├── Move.java              # Move class
├── Item.java              # Item class
├── Trainer.java           # Trainer class
└── README.md              # This file
```

## Default Data

The system comes pre-loaded with:
- 20+ Pokémon including starters and evolutions
- 18 moves (TMs and HMs)
- 20+ items including vitamins, evolution stones, and special items
- 1 default trainer (Ash Ketchum) with Pokémon and items

## Tips

- Use the search functionality to quickly find specific Pokémon, moves, or items
- Right-click on table headers to sort data
- All forms include validation to prevent invalid data entry
- The trainer operations dialog provides comprehensive management tools
- CSV export/import allows for data backup and sharing
- Auto-save ensures your data is never lost - changes are saved automatically
- Check the status bar to see if auto-save is enabled
- Use File → Auto-Save Settings to configure auto-save behavior 