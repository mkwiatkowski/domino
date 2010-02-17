package eti.domino;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class DominoRenderer implements GLSurfaceView.Renderer {
	public static final String TAG = "Domino";
	public static final float tableScale = 0.65f;

	@SuppressWarnings("unused")
	private Context context;

	private Table table;
	private ComputerPlayer computerPlayer;
	private DominoPiece currentPiece;
	private Position currentPieceOldPosition;

	public DominoRenderer(Context context) {
		this.context = context;
		table = new Table();
		table.startGame();
		computerPlayer = new ComputerPlayer();
	}

	private float xOnScreenToCoord(float x) {
		final float leftEdgeCoord = -1.35f;
		final float rightEdgeCoord = 1.35f;
		final float screenWidth = 320;
		return (x / screenWidth) * (rightEdgeCoord - leftEdgeCoord) + leftEdgeCoord;
	}

	private float yOnScreenToCoord(float y) {
		final float topEdgeCoord = 1.85f;
		final float bottomEdgeCoord = -1.85f;
		final float screenHeight = 430;
		return -1 * ((y / screenHeight) - 1) * (topEdgeCoord - bottomEdgeCoord) + bottomEdgeCoord;
	}

	public void touch(float xOnScreen, float yOnScreen) {
		float x = xOnScreenToCoord(xOnScreen);
		float y = yOnScreenToCoord(yOnScreen);
		if (currentPiece == null) {
			for (DominoPiece piece : table.getHumanPlayerPieces()) {
				if (piece.containsPointOnPlayerPieces(x, y)) {
					activatePiece(piece);
				}
			}
		} else {
			currentPiece.setPosition(x, y, 0);
		}
	}

	public void release(float xOnScreen, float yOnScreen) {
		float x = xOnScreenToCoord(xOnScreen);
		float y = yOnScreenToCoord(yOnScreen);
		if (currentPiece != null) {
			for (DominoPiece piece : table.getTablePieces()) {
				if (piece.containsPointOnTable(x, y)) {
					tableStatus();
					if (table.putPieceOnTable(currentPiece, piece)) {
						deactivateCurrentPiece(false);
						gameStep();
						tableStatus();
						return;
					}
				}
			}
			deactivateCurrentPiece(true);
		}
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glClearColor(.5f, .5f, .5f, 1);
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		gl.glViewport(0, 0, w, h);
		
        float ratio = (float) w / h;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
	}

	public void onDrawFrame(GL10 gl) {
		clearScreen(gl);
		setupCamera(gl);

		layOutHumanPlayerPieces();
		for (DominoPiece piece : table.getHumanPlayerPieces()) {
			piece.draw(gl);
		}

		layOutComputerPieces();
		for (DominoPiece piece : table.getComputerPlayerPieces()) {
			piece.drawWithoutDots(gl);
		}

		for (DominoPiece piece : table.getTablePieces()) {
			gl.glPushMatrix();
			gl.glScalef(DominoRenderer.tableScale, DominoRenderer.tableScale, DominoRenderer.tableScale);
			piece.draw(gl);
			gl.glPopMatrix();
		}
	}

	private void clearScreen(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}

	private void setupCamera(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}
	
	private void layOutHumanPlayerPieces() {
		float x=-1.04f;
		for (DominoPiece piece : table.getHumanPlayerPieces()) {
			if (piece != currentPiece) {
				piece.setPosition(x, -1.4f, 0);
			}
			x += 0.3f;
			// TODO: make sure x < 1.2f
		}
	}
	
	private void layOutComputerPieces() {
		float x=-1.04f;
		for (DominoPiece piece : table.getComputerPlayerPieces()) {
			piece.setPosition(x, 1.8f, 0);
			x += 0.3f;
			// TODO: make sure x < 1.2f
		}
	}

	private void deactivateCurrentPiece(boolean resetPosition) {
		if (currentPiece != null) {
			if (resetPosition) {
				currentPiece.setPosition(currentPieceOldPosition);
			}
			currentPiece.deactivate();
			currentPiece = null;
			currentPieceOldPosition = null;
		}
	}

	private void activatePiece(DominoPiece piece) {
		if (currentPiece == null) {
			currentPiece = piece;
			currentPieceOldPosition = currentPiece.getPositionCopy();
			currentPiece.activate();
		}
	}
	
	private void tableStatus() {
		Log.d(DominoRenderer.TAG, "PIECES ON TABLE");
		for (DominoPiece piece : table.getTablePieces()) {
			Log.d(DominoRenderer.TAG, piece.toString());		
		}
	}
	
	private void gameStep() {
		if (table.computerHasWon()) {
			// TODO show "lost" screen
			Log.d(DominoRenderer.TAG, "You have lost!");
		} else if (table.humanHasWon()) {
			// TODO show "won" screen
			Log.d(DominoRenderer.TAG, "You have won!");
		}

		// The piece has been placed by the user, now
		// it's the computer turn.
		boolean computerMoved = computerPlayer.move(table);
		
		if (!computerMoved && table.isTieWithNoComputerMoves()) {
			Log.d(DominoRenderer.TAG, "Tie!");
		}

		// Check if the player has a move, otherwise draw
		// pieces for him.
		try {
			table.drawPiecesForPlayerIfNeeded();
		} catch (NoPiecesLeftException e) {
			// No moves for the player, let the computer play.
			gameStep();
		}
	}
}
