package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    private final static int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score, maxTile;
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public Model() {
        resetGameTiles();
    }

    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
        score = 0;
        maxTile = 0;
    }
    private void saveState(Tile[][] tiles){
        if (isSaveNeeded) {
            Tile[][] save = new Tile[tiles.length][tiles[0].length];
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    save[i][j] = new Tile(tiles[i][j].value);
                }
            }
            previousStates.push(save);
            previousScores.push(score);
            isSaveNeeded = false;
        }
    }
    public void rollback(){
        if (!previousScores.empty() && !previousStates.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }
    public void randomMove(){
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n){
            case 0: left(); break;
            case 1: right(); break;
            case 2: up(); break;
            case 3: down(); break;
        }
    }

    public boolean hasBoardChanged(){
        int gameValues = 0;
        int saveValues = 0;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameValues += gameTiles[i][j].value;
                saveValues += previousStates.peek()[i][j].value;
            }
        }
        return gameValues != saveValues;
    }

    public MoveEfficiency getMoveEfficiency(Move move){
        move.move();
        MoveEfficiency efficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        if (hasBoardChanged()){
            rollback();
            return efficiency;
        }
        else return new MoveEfficiency(-1, 0, move);
    }
    public void autoMove(){
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.reverseOrder());
        queue.offer(getMoveEfficiency(this::left));
        queue.offer(getMoveEfficiency(this::up));
        queue.offer(getMoveEfficiency(this::down));
        queue.offer(getMoveEfficiency(this::right));
        queue.peek().getMove().move();
    }

    //метод, который будет смотреть какие плитки пустуют и менять вес одной из них, на 2 или 4 (9 : 1)
    private void addTile() {
        int value = (Math.random() < 0.9 ? 2 : 4);
        List<Tile> list = getEmptyTiles();
        if (list != null && list.size() > 0) {
            int random = (int) (list.size() * Math.random());
            list.get(random).value = value;
        }
    }
    private List<Tile> getEmptyTiles(){
        List<Tile> list = new ArrayList<>();
        for (Tile[] gameTile : gameTiles) {
            for (Tile aGameTile : gameTile) {
                if (aGameTile.value == 0) list.add(aGameTile);
            }
        }
        return list;
    }

    //Сжатие плиток, таким образом, чтобы все пустые плитки были справа, т.е. ряд {4, 2, 0, 4} становится рядом {4, 2, 4, 0}
    private boolean compressTiles(Tile[] tiles){
        boolean result = false;
        for (Tile tile : tiles) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[j].value == 0 && j < tiles.length - 1 && tiles[j + 1].value != 0) {
                    tiles[j].value = tiles[j + 1].value;
                    tiles[j + 1].value = 0;
                    result = true;
                }
            }
        }
        return result;
    }
    //Слияние плиток одного номинала, т.е. ряд {4, 4, 2, 0} становится рядом {8, 2, 0, 0}.
    private boolean mergeTiles(Tile[] tiles){
        boolean result = false;
        for (int i = 1; i < tiles.length; i++) {
            if (tiles[i - 1].value != 0 && tiles[i - 1].value == tiles[i].value){
                tiles[i - 1].value = tiles[i].value * 2;
                score += tiles[i - 1].value;
                maxTile = maxTile > tiles[i - 1].value? maxTile: tiles[i - 1].value;
                tiles[i].value = 0;
                Tile[] tiles1 = new Tile[tiles.length - i];
                System.arraycopy(tiles, i, tiles1, 0, tiles1.length);
                compressTiles(tiles1);
                result = true;
            }
        }
        return result;
    }

    public void left(){
        saveState(gameTiles);
        Tile[][] tile = new Tile[gameTiles[0].length][gameTiles.length];
        for (int i = 0; i < gameTiles[0].length; i++) {
            System.arraycopy(gameTiles[i], 0, tile[i], 0, gameTiles.length);
        }
        if (add(gameTiles)){
            for (int i = 0; i < gameTiles[0].length; i++) {
                System.arraycopy(tile[i], 0, gameTiles[i], 0, gameTiles.length);
            }
            isSaveNeeded = true;
            addTile();
        }
    }
    public void down(){
        saveState(gameTiles);
        Tile[][] tile = new Tile[gameTiles[0].length][gameTiles.length];
        for (int i = 0; i < gameTiles[0].length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                tile[i][j] = gameTiles[gameTiles.length-1-j][i];
            }
        }
        if (add(tile)){
            for (int i = 0; i < gameTiles[0].length; i++) {
                for (int j = 0; j < gameTiles.length; j++) {
                    gameTiles[i][j] = tile[j][tile.length-1-i];
                }
            }
            isSaveNeeded = true;
            addTile();
        }
    }
    public void up(){
        saveState(gameTiles);
        Tile[][] tile = new Tile[gameTiles[0].length][gameTiles.length];
        for (int i = 0; i < gameTiles[0].length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                tile[i][j] = gameTiles[j][gameTiles.length-1-i];
            }
        }
        if (add(tile)){
            for (int i = 0; i < gameTiles[0].length; i++) {
                for (int j = 0; j < gameTiles.length; j++) {
                    gameTiles[i][j] = tile[tile.length-1-j][i];
                }
            }
            isSaveNeeded = true;
            addTile();
        }
    }
    public void right(){
        saveState(gameTiles);
        Tile[][] tile = new Tile[gameTiles[0].length][gameTiles.length];
        for (int i = 0; i < gameTiles[0].length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                tile[i][j] = gameTiles[gameTiles.length-1-i][gameTiles.length-1-j];
            }
        }
        if (add(tile)){
            for (int i = 0; i < gameTiles[0].length; i++) {
                for (int j = 0; j < gameTiles.length; j++) {
                    gameTiles[i][j] = tile[tile.length-1-i][tile.length-1-j];
                }
            }
            isSaveNeeded = true;
            addTile();
        }
    }
    //можливість зробити хід без зміни gameTiles
    public boolean canMove(){
        if (!getEmptyTiles().isEmpty()) return true;
        for (Tile[] gameTile : gameTiles) {
            for (int j = 1; j < gameTile.length; j++) {
                if (gameTile[j].value == gameTile[j - 1].value) return true;
            }
        }
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 1; j < gameTiles[i].length; j++) {
                if (gameTiles[j][i].value == gameTiles[j-1][i].value) return true;
            }
        }
        return false;
    }
    //можливість зробити хід
    private boolean add(Tile[][] gameTiles) {
        boolean isChange = false;
        for (Tile[] tiles : gameTiles) {
            if (compressTiles(tiles) | mergeTiles(tiles)) isChange = true;
        }
        return isChange;
    }
}
