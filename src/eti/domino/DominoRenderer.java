package eti.domino;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class DominoRenderer implements GLSurfaceView.Renderer {
	private Context context;
	private ArrayList<DominoPiece> pieces;
	private DominoPiece currentPiece;

	public DominoRenderer(Context context) {
		this.context = context;
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
		for (DominoPiece piece : pieces) {
			if (piece.containsPoint(x, y)) {
				activatePiece(piece);
			}
		}
	}

	public void release(float x, float y) {
		deactivateCurrentPiece();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);

		gl.glClearColor(.5f, .5f, .5f, 1);

		int textureId = loadTexture(gl, R.drawable.piece);
		pieces = new ArrayList<DominoPiece>();
		for (float x=-1.04f; x < 1.2f; x += 0.3f) {
			pieces.add(new DominoPiece(textureId, new Position(x, -1.5f, 0)));
		}
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
		for (DominoPiece piece : pieces) {
			piece.draw(gl);
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

	private int loadTexture(GL10 gl, int resource) {
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);

		int textureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

		InputStream is = context.getResources().openRawResource(resource);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Ignore.
			}
		}
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();

		return textureId;
	}

	private void deactivateCurrentPiece() {
		if (currentPiece != null) {
			currentPiece.scaleTendency = "down";
			currentPiece.rotationFrequency = 0;
			currentPiece = null;
		}
	}
	
	private void activatePiece(DominoPiece piece) {
		deactivateCurrentPiece();
		currentPiece = piece;
		currentPiece.scaleTendency = "up";
		currentPiece.rotationFrequency = 0.5f;
	}
}
