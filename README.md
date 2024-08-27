# Naman-Roy-21BCE11639

# Hero's Battle - A Turn-Based Chess-like Game

## Overview

Hero's Battle is a turn-based strategy game inspired by chess, built as a native Android application using Java. The game features a 5x5 grid where two players compete by strategically moving their characters (Pawns, Hero1, and Hero2) across the board. The game supports real-time gameplay through WebSocket communication.

## Features

- **Turn-Based Gameplay**: Players take turns to move their pieces across a 5x5 grid.
- **Three Types of Characters**:
  - **Pawn**: Moves one cell in any direction.
  - **Hero1**: Moves two cells in straight lines (horizontal/vertical) and eliminates any opponents in its path.
  - **Hero2**: Moves diagonally by 1 or 2 cells and eliminates any opponents in its path.
- **Real-Time Multiplayer**: Connect with other players over the internet using WebSocket.
- **Piece Customization**: Players can choose their own combination of 5 pieces at the start of the game.
- **Game Over Detection**: The game ends when one player eliminates all of their opponent’s pieces.

## Requirements

- Android Studio (Latest version recommended)
- An active internet connection
- Android device or emulator with Android 5.0 (API Level 21) or higher

## Setup

1. **Clone the repository:**
    ```sh
    git clone https://github.com/your-repository/heros-battle.git
    ```
   
2. **Open the project in Android Studio:**
    - Start Android Studio.
    - Select "Open an existing project".
    - Navigate to the cloned repository and open it.

3. **Add Internet Permission:**
    Ensure the following permission is declared in your `AndroidManifest.xml` file:
    ```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    ```

4. **Configure WebSocket URI:**
    - Open `MainActivity.java`.
    - Replace the WebSocket URI with your server's URI:
      ```java
      URI serverURI= new URI("wss://your-websocket-server-uri");
      ```

5. **Build and Run the Project:**
    - Connect your Android device or start an emulator.
    - Click on the "Run" button in Android Studio to build and install the app.

## Gameplay Instructions

1. **Game Setup:**
   - Upon launching the app, you'll be prompted to select your team composition.
   - Choose the number of Pawns, Hero1s, and Hero2s you wish to have, ensuring the total equals 5.
   - Click "Start Game" to begin.

2. **Game Flow:**
   - The game board will appear with your pieces on the top row and the opponent’s pieces on the bottom row.
   - Players take turns moving one piece at a time.
   - To move a piece, click on the desired piece, then click on the target cell.
   - **Hero1 and Hero2** will eliminate any opponent’s pieces in their path.

3. **Winning the Game:**
   - The game ends when one player eliminates all of the opponent's pieces.
   - The winner is displayed, and players can restart the game.
