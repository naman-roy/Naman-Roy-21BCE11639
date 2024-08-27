package com.company.herosbattle;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GameWebSocketClient webSocketClient;
    private Character selectedCharacter = null;
    private Character[][] board = new Character[5][5];// 5x5 board
    private String currentPlayer = "A";// Turn tracking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupGameScreen();

        // Connect to the server
        try {
            URI serverURI= new URI("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self");
            webSocketClient = new GameWebSocketClient(serverURI);
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    // Setup Screen Logic
    private void setupGameScreen() {
        NumberPicker pawnPicker = findViewById(R.id.pawnPicker);
        NumberPicker hero1Picker = findViewById(R.id.hero1Picker);
        NumberPicker hero2Picker = findViewById(R.id.hero2Picker);
        Button startGameButton= findViewById(R.id.startGameButton);

        // Set min and max values
        pawnPicker.setMinValue(0);
        pawnPicker.setMaxValue(5);
        pawnPicker.setValue(2);

        hero1Picker.setMinValue(0);
        hero1Picker.setMaxValue(5);
        hero1Picker.setValue(2);

        hero2Picker.setMinValue(0);
        hero2Picker.setMaxValue(5);
        hero2Picker.setValue(1);


        startGameButton.setOnClickListener(v -> {
            // Get the selected values from the NumberPickers
            int pawnCount = pawnPicker.getValue();
            int hero1Count = hero1Picker.getValue();
            int hero2Count = hero2Picker.getValue();

            //Validate the input
            if(pawnCount+hero1Count+hero2Count !=5){
                Toast.makeText(this, "Ypu Must Select 5 Pieces", Toast.LENGTH_SHORT).show();
                return;
            }

            // Initializing the Board with selected pieces
            initializeBoard(pawnCount, hero1Count, hero2Count);

            // Hiding Setup and launching Game Screen
            findViewById(R.id.setupScreen).setVisibility(View.GONE);
            findViewById(R.id.board).setVisibility(View.VISIBLE);

            // Setup the layout
            setupLayout();

        });

    }

    private void initializeBoard(int pawnCount, int hero1Count, int hero2Count){

        // Player A initialization
        int index = 0;
        for(int i = 0; i < pawnCount; i++){
            board[0][index] = new GamePieces.Pawn("A", 0, index);
            index++;
        }
        for(int i = 0; i < hero1Count; i++){
            board[0][index] = new GamePieces.Hero1("A", 0, index);
            index++;
        }
        for(int i = 0; i < hero2Count; i++){
            board[0][index] = new GamePieces.Hero2("A", 0, index);
            index++;
        }

        // Player B initialization
        index=0;
        for(int i = 0; i < pawnCount; i++){
            board[4][index] = new GamePieces.Pawn("B", 4, index);
            index++;
        }
        for(int i = 0; i < hero1Count; i++){
            board[4][index] = new GamePieces.Hero1("B", 4, index);
            index++;
        }
        for(int i = 0; i < hero2Count; i++){
            board[4][index] = new GamePieces.Hero2("B", 4, index);
            index++;
        }

    }


    // Setting Up the Gridlayout
    private void setupLayout() {
        GridLayout board = findViewById(R.id.board);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int cellSize = screenWidth / board.getColumnCount()+5;
        int adjustedCellSize = (int) (cellSize * 0.95);

        int totalCells = board.getColumnCount() * board.getRowCount();

        for (int i = 0; i < totalCells; i++) {
            Button piece = new Button(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            params.width = adjustedCellSize;
            params.height = adjustedCellSize;
            params.setMargins(2, 2, 2, 2);

            piece.setLayoutParams(params);

            // Set the text on the button (for example purposes)
            piece.setText("Piece " + i);

            // Ensure the text is centered horizontally and vertically
            //piece.setGravity(Gravity.CENTER);

            // Optionally, adjust text size to fit well within the button
            piece.setTextSize(12); // Adjust as needed
            final int cellIndex = i; // Store the index as a final variable for use in the listener
            piece.setOnClickListener(v -> onCellClicked(cellIndex, piece));

            board.addView(piece);
        }
        updateBoard();
    }



    private void onCellClicked(int cellIndex, Button cell) {
        //int cellIndex = (int) cell.getTag();
        int row = cellIndex / 5;
        int col = cellIndex % 5;

        if (selectedCharacter != null) {
            // Handle movement
            if (selectedCharacter.isValidMove(row, col, board)) {

                // Handle the path logic
                List<int[]> pathCells = selectedCharacter.getPathToDestination(row, col);


                // Remove all enemy pieces in the path
                for (int[] pathCell : pathCells) {
                    int pathRow = pathCell[0];
                    int pathCol = pathCell[1];
                    if (board[pathRow][pathCol] != null && !board[pathRow][pathCol].playerName.equals(selectedCharacter.playerName)) {
                        board[pathRow][pathCol] = null; // Enemy piece removed
                    }
                }

                // Move the character
                board[selectedCharacter.getX()][selectedCharacter.getY()] = null;
                selectedCharacter.moveTo(row, col);
                board[row][col]=selectedCharacter;

                // Switching turns
                currentPlayer = currentPlayer.equals("A") ? "B" : "A";
                selectedCharacter = null;
                updateBoard();

                checkGameOver();
            } else {
                // Show error message: Invalid move
                cell.setBackgroundColor(Color.RED);
                Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Select character if available on the cell
            selectedCharacter = board[row][col];
            if (selectedCharacter != null && selectedCharacter.playerName.equals(currentPlayer)) {
                //resetCellColors();
                // Highlight the selected character
                cell.setBackgroundColor(Color.YELLOW);

            }else {
                selectedCharacter = null;
            }
        }
    }
    /*private void resetCellColors() {
        GridLayout gridLayout = findViewById(R.id.board);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button cell = (Button) gridLayout.getChildAt(i);
            cell.setBackgroundColor(Color.CYAN); // Set to default color
        }
    }*/


    private void updateBoard() {
        // Implement logic to update the board based on the characters' positions
        GridLayout gridLayout = findViewById(R.id.board);
        for(int i = 0; i < gridLayout.getChildCount(); i++){
            Button cell = (Button) gridLayout.getChildAt(i);
            cell.setText("");
            cell.setBackgroundColor(Color.BLACK);
        }

        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 5; col++){
                if(board[row][col]!=null){
                    int cellIndex = row * 5 + col;
                    Button cell = (Button) gridLayout.getChildAt(cellIndex);
                    cell.setText(board[row][col].getClass().getSimpleName() + " (" + board[row][col].playerName + ")");
                    cell.setBackgroundColor(Color.WHITE);
                }
            }
        }
    }

    // Logic for Game Over
    private void checkGameOver() {
        boolean playerAPiecesLeft = false;
        boolean playerBPiecesLeft = false;

        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 5; col++){
                if(board[row][col]!=null && board[row][col].playerName.equals("A")){
                    playerAPiecesLeft = true;
                }
                else if(board[row][col]!=null && board[row][col].playerName.equals("B")){
                    playerBPiecesLeft = true;
                }
            }
        }

        if(!playerAPiecesLeft || !playerBPiecesLeft){
            String winner = playerAPiecesLeft ? "Player A" : "Player B";
            Toast.makeText(this, winner + " wins!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }


}