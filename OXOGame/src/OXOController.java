
import java.util.Scanner;


class OXOController {

    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }
    
    public void handleIncomingCommand(String command) throws OXOMoveException {
        OXOPlayer currentPlayer = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
        int currentPlayerNum = gameModel.getCurrentPlayerNumber();
        String[] site = command.split(" ");

        if(command.equals("newplayer")){
            addPlayer();
            return;    
        }    
        try {
            // Input Row、Column Index
            int rowIndex = Integer.valueOf(site[0]);
            int columnIndex = Integer.valueOf(site[1]);

            // check input data
            if (gameModel.getWinner() != null) { // Game Over
                System.out.println("Game Over!");
            } 
            // Type Error
            else if (site.length != 2) { 
                gameModel.setErrorMsg("Input type Error!");
                throw new OXOMoveException.InvalidIdentifierLengthException(site.length);
            } 
            // Index Error
            else if (rowIndex >= gameModel.getNumberOfRows() || rowIndex < 0 || 
                    columnIndex < 0 || columnIndex >= gameModel.getNumberOfColumns()) { 
                gameModel.setErrorMsg("Row or Column's Index out of range!");
                throw new OXOMoveException.CellDoesNotExistException(rowIndex, columnIndex);
            } 
            // Index Already Input
            else if (gameModel.getCellOwner(rowIndex, columnIndex) != null) { 
                gameModel.setErrorMsg("Someone already input here!");
                throw new OXOMoveException.CellAlreadyTakenException(rowIndex, columnIndex);
            } 
            else {
                // set model
                gameModel.setCellOwner(rowIndex, columnIndex, currentPlayer);
                // reset errMsg
                gameModel.setErrorMsg("");
                
                // ----- game result ----- 
                // (Threshold)
                if (checkHorizontally_Threshold() || checkvertically_Threshold() || checkdiagonally_Threshold()) {
                    gameModel.setWinner(currentPlayer);
                }
                // (no Threshold)
                //if (checkHorizontally() || checkvertically() || checkdiagonally()) {
                //    gameModel.setWinner(currentPlayer);
                //}
                // (GameDrawn)
                if (IsFullFill()) {
                    gameModel.setGameDrawn();
                }
                
                // ----- change player -----
                if(currentPlayerNum == gameModel.getNumberOfPlayers() - 1){
                    gameModel.setCurrentPlayerNumber(0);
                }else{
                    gameModel.setCurrentPlayerNumber(currentPlayerNum + 1);
                }
            }
        } catch (NumberFormatException ex) {
            gameModel.setErrorMsg("Index must be Integer");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    public void addPlayer(){
        Scanner scn = new Scanner(System.in);
        System.out.println("Input new player's symbol : ");
        String symbol = scn.next();
        gameModel.addPlayer(new OXOPlayer(symbol.charAt(0)));
        System.out.println("Add new player successful!");
    }

    public boolean checkHorizontally() {
        OXOPlayer player = null;
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                // check first column
                if (j == 0) {
                    if (gameModel.getCellOwner(i, j) == null) // null break
                    {
                        break;
                    } else {
                        player = gameModel.getCellOwner(i, j);
                    }
                }
                // if first column has data, check all of them are the same
                if (player != null) {
                    if (player != gameModel.getCellOwner(i, j)) // different break
                    {
                        break;
                    } else if (j == gameModel.getNumberOfColumns() - 1) // all same player -> true
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkHorizontally_Threshold() {
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            OXOPlayer player = null;
            int count = 0;
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                // init data
                if (player == null && gameModel.getCellOwner(i, j) != null) {
                    player = gameModel.getCellOwner(i, j);
                    count += 1;
                } // same player
                else if (player != null && player == gameModel.getCellOwner(i, j)) {
                    count += 1;
                } // change player
                else if (player != null && gameModel.getCellOwner(i, j) != null && player != gameModel.getCellOwner(i, j)) {
                    player = gameModel.getCellOwner(i, j);
                    count = 1;
                } // next is null
                else if (player != null && gameModel.getCellOwner(i, j) == null) {
                    player = null;
                    count = 0;
                }
                if (count == gameModel.getWinThreshold()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkvertically() {
        OXOPlayer player = null;
        for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
            for (int j = 0; j < gameModel.getNumberOfRows(); j++) {
                // check first row
                if (j == 0) {
                    if (gameModel.getCellOwner(j, i) == null) // null break
                    {
                        break;
                    } else {
                        player = gameModel.getCellOwner(j, i);
                    }
                }
                // if first row has data, check all of them are the same
                if (player != null) {
                    if (player != gameModel.getCellOwner(j, i)) // different break
                    {
                        break;
                    } else if (j == gameModel.getNumberOfRows() - 1) // all same player -> true
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkvertically_Threshold() {
        for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
            OXOPlayer player = null;
            int count = 0;
            for (int j = 0; j < gameModel.getNumberOfRows(); j++) {
                // init data
                if (player == null && gameModel.getCellOwner(j, i) != null) {
                    player = gameModel.getCellOwner(j, i);
                    count += 1;
                } // same player
                else if (player != null && player == gameModel.getCellOwner(j, i)) {
                    count += 1;
                } // change player
                else if (player != null && gameModel.getCellOwner(j, i) != null && player != gameModel.getCellOwner(j, i)) {
                    player = gameModel.getCellOwner(j, i);
                    count = 1;
                } // next is null
                else if (player != null && gameModel.getCellOwner(j, i) == null) {
                    player = null;
                    count = 0;
                }
                if (count == gameModel.getWinThreshold()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkdiagonally() {
        // row = column
        if (gameModel.getNumberOfRows() != gameModel.getNumberOfColumns()) {
            return false;
        }
        // check upper left 
        boolean flag1 = false;
        if (gameModel.getCellOwner(0, 0) != null) {
            flag1 = true;
            OXOPlayer player1 = gameModel.getCellOwner(0, 0);
            for (int i = 1; i < gameModel.getNumberOfRows(); i++) {
                if (player1 != gameModel.getCellOwner(i, i)) {
                    flag1 = false;
                    break;
                }
            }
        }
        // check top right
        boolean flag2 = false;
        if (gameModel.getCellOwner(0, gameModel.getNumberOfColumns() - 1) != null) {
            flag2 = true;
            OXOPlayer player2 = gameModel.getCellOwner(0, gameModel.getNumberOfColumns() - 1);
            for (int i = 1; i < gameModel.getNumberOfRows(); i++) {
                if (player2 != gameModel.getCellOwner(i, gameModel.getNumberOfRows() - 1 - i)) {
                    flag2 = false;
                    break;
                }
            }
        }
        return flag1 || flag2;
    }

    public boolean checkdiagonally_Threshold() {
        for (int i = 0; i < gameModel.getNumberOfRows() - 1; i++) {
            for(int j = 0 ; j < gameModel.getNumberOfColumns() - 1 ; j++){
                OXOPlayer player = null;
                int count = 0;
                int row = i;
                int col = j;
                while(row < gameModel.getNumberOfRows() && col < gameModel.getNumberOfColumns()){
                    // init data
                    //System.out.println(row + ", " + col);
                    if (player == null && gameModel.getCellOwner(row, col) != null) {
                        player = gameModel.getCellOwner(row, col);
                        count += 1;
                    } // same player
                    else if (player != null && player == gameModel.getCellOwner(row, col)) {
                        count += 1;
                    } // change player
                    else if (player != null && gameModel.getCellOwner(row, col) != null && player != gameModel.getCellOwner(row, col)) {
                        player = gameModel.getCellOwner(row, col);
                        count = 1;
                    } // next is null
                    else if (player != null && gameModel.getCellOwner(row, col) == null) {
                        player = null;
                        count = 0;
                    }
                    row++; col++;
                    if (count == gameModel.getWinThreshold()) {
                        return true;
                    }
                }
            }
        }
        for (int i = 0 ; i < gameModel.getNumberOfRows() - 1; i++) {
            for(int j = gameModel.getNumberOfColumns() - 1 ; j > 0 ; j--){
                OXOPlayer player = null;
                int count = 0;
                int row = i;
                int col = j;
                while(row < gameModel.getNumberOfRows() && col > -1){
                    // init data
                    //System.out.println(row + ", " + col);
                    if (player == null && gameModel.getCellOwner(row, col) != null) {
                        player = gameModel.getCellOwner(row, col);
                        count += 1;
                    } // same player
                    else if (player != null && player == gameModel.getCellOwner(row, col)) {
                        count += 1;
                    } // change player
                    else if (player != null && gameModel.getCellOwner(row, col) != null && player != gameModel.getCellOwner(row, col)) {
                        player = gameModel.getCellOwner(row, col);
                        count = 1;
                    } // next is null
                    else if (player != null && gameModel.getCellOwner(row, col) == null) {
                        player = null;
                        count = 0;
                    }
                    row++; col--;
                    if (count == gameModel.getWinThreshold()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean IsFullFill() {
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                if (gameModel.getCellOwner(i, j) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addRow() {
        gameModel.addRow();
    }

    public void removeRow() {
        gameModel.removeRow();
    }

    public void addColumn() {
        gameModel.addColumn();
    }

    public void removeColumn() {
        gameModel.removeColumn();
    }

    public void increaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold() + 1);
        System.out.println("WinThreshold : " + gameModel.getWinThreshold());
    }

    public void decreaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
        System.out.println("WinThreshold : " + gameModel.getWinThreshold());
    }
}
