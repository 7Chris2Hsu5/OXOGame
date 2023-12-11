
import java.util.ArrayList;

class OXOModel {
    // private final OXOPlayer[][] cells;
    private final ArrayList<ArrayList<OXOPlayer>> cells;
    private final ArrayList<OXOPlayer> players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    private String errorMsg = "";

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        // cells = new OXOPlayer[numberOfRows][numberOfColumns];
        cells = new ArrayList<ArrayList<OXOPlayer>>();
        for (int i = 0; i < numberOfRows; i++) {
            cells.add(new ArrayList<OXOPlayer>());
            for (int j = 0; j < numberOfRows; j++) {
                cells.get(i).add(null); // 初始填入空值
            }
        }
        players = new ArrayList<OXOPlayer>();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void addPlayer(OXOPlayer player) {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players.get(number);
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        // return cells[rowNumber][colNumber];
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        // cells[rowNumber][colNumber] = player;
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }
    
    public boolean isError(){
        if(errorMsg.equals(""))
            return false;
        else
            return true;
    }
    
    public String getErrorMsg(){
        return errorMsg;
    }
    
    public void setErrorMsg(String msg){
        errorMsg = msg;
    }

    // below is for ArrayList
    public void addRow() {
        cells.add(new ArrayList<OXOPlayer>());
        for (int j = 0; j < getNumberOfColumns(); j++) {
            cells.get(getNumberOfRows() - 1).add(null); // 初始填入空值
        }
    }

    public void addColumn() {
        for (int i = 0; i < getNumberOfRows(); i++) {
            cells.get(i).add(null);
        }
    }

    public void removeRow() {
        if (getNumberOfRows() - 1 > 0) {
            cells.remove(getNumberOfRows() - 1);
        }
    }

    public void removeColumn() {
        if (getNumberOfColumns() - 1 > 0) {
            for (int i = 0; i < getNumberOfRows(); i++) {
                cells.get(i).remove(getNumberOfColumns() - 1);
            }
        }
    }
}
