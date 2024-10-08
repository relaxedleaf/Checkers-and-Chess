package com.lemon.check.Evacheck.Chess;

import java.io.Serializable;
import java.util.List;

public class NullChess extends Piece implements Serializable {
    public NullChess(){
        super("NullChess");
    }

    @Override
    public List<Point> getMoves(List<List<Piece>> board) {
        return null;
    }
}
