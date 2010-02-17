package eti.domino;

import java.util.ArrayList;
import java.util.Random;

public class Table {
	private ArrayList<DominoPiece> humanPlayerPieces;
	private ArrayList<DominoPiece> computerPlayerPieces;
	private ArrayList<DominoPiece> bankPieces;
	private ArrayList<DominoPiece> tablePieces;

	public Table() {
		humanPlayerPieces = new ArrayList<DominoPiece>();
		computerPlayerPieces = new ArrayList<DominoPiece>();
		bankPieces = new ArrayList<DominoPiece>();
		tablePieces = new ArrayList<DominoPiece>();

		for (int top=0; top <= 6; top++) {
			for (int bottom=top; bottom <= 6; bottom++) {
				bankPieces.add(new DominoPiece(top, bottom));
			}
		}
	}

	public ArrayList<DominoPiece> getHumanPlayerPieces() {
		return humanPlayerPieces;
	}

	public ArrayList<DominoPiece> getTablePieces() {
		return tablePieces;
	}

	public void startGame() {
		for (int i=1; i <= 7; i++) {
			getRandomPieceForHuman();
		}
		for (int i=1; i <= 7; i++) {
			getRandomPieceForComputer();
		}
		putRandomPieceOnTable();
	}

	public void putRandomPieceOnTable() {
		int idx = getRandomPieceIndex();
		DominoPiece piece = bankPieces.remove(idx);
		tablePieces.add(piece);
	}

	// Return true when the piece has been successfully placed on table.
	public boolean putPieceOnTable(DominoPiece piece, DominoPiece adjacentPiece) {
		if (piece.fitsWith(adjacentPiece.topDots)) {
			movePieceToTable(piece);
			piece.setPosition(adjacentPiece.getPositionOnePieceHigher());
			if (piece.topDots == adjacentPiece.topDots) {
				piece.flip();
			}
			adjacentPiece.topFree = false;
			piece.bottomFree = false;
			return true;
		} else if (piece.fitsWith(adjacentPiece.bottomDots)) {
			movePieceToTable(piece);
			piece.setPosition(adjacentPiece.getPositionOnePieceLower());
			if (piece.bottomDots == adjacentPiece.bottomDots) {
				piece.flip();
			}
			adjacentPiece.bottomFree = false;
			piece.topFree = false;
			return true;				
		}
		return false;
	}

	public DominoPiece getRandomPieceForHuman() {
		int idx = getRandomPieceIndex();
		DominoPiece piece = bankPieces.remove(idx);
		humanPlayerPieces.add(piece);
		return piece;
	}

	public DominoPiece getRandomPieceForComputer() {
		int idx = getRandomPieceIndex();
		DominoPiece piece = bankPieces.remove(idx);
		computerPlayerPieces.add(piece);
		return piece;
	}

	private int getRandomPieceIndex() {
		// TODO: check if there are any pieces left first!
		Random generator = new Random();
		return generator.nextInt(bankPieces.size());
	}

	private void movePieceToTable(DominoPiece piece) {
		if (humanPlayerPieces.contains(piece)) {
			humanPlayerPieces.remove(piece);
		} else if (computerPlayerPieces.contains(piece)) {
			computerPlayerPieces.remove(piece);
		}
		tablePieces.add(piece);
	}
}
