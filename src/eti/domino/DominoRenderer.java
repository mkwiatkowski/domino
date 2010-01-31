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
import android.os.SystemClock;

public class DominoRenderer implements GLSurfaceView.Renderer {
	private Context context;
	private DominoPiece piece;

	public DominoRenderer(Context context) {
		this.context = context;
	}

	public void touch(float x, float y) {
		piece.scaleTendency = "up";
	}

	public void release(float x, float y) {
		piece.scaleTendency = "down";
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);

		gl.glClearColor(.5f, .5f, .5f, 1);

		int textureId = loadTexture(gl, R.drawable.piece);
		piece = new DominoPiece(textureId);
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
		piece.rotate(gl, 0.5f);
		piece.draw(gl);
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
}

class DominoPiece {
	private static float[] front_to_bottom_coords = {
		// front
		-0.5f, -1, 0.1f,
		0.5f, -1, 0.1f,
		-0.5f, 1, 0.1f,
		0.5f, 1, 0.1f,
		// top
		-0.5f, 1, -0.1f,
		0.5f, 1, -0.1f,
		// back
		-0.5f, -1, -0.1f,
		0.5f, -1, -0.1f,
		// bottom
		-0.5f, -1, 0.1f,
		0.5f, -1, 0.1f
	};
	private static float[] right_coords = {
		0.5f, -1, 0.1f,
		0.5f, -1, -0.1f,
		0.5f, 1, 0.1f,
		0.5f, 1, -0.1f
	};
	private static float[] left_coords = {
		-0.5f, -1, 0.1f,
		-0.5f, -1, -0.1f,
		-0.5f, 1, 0.1f,
		-0.5f, 1, -0.1f
	};
	private float scaleFactor = 1.0f;
	private ArrayList<TexturedTriangleStrip> strips; 
	public String scaleTendency = "down";

	public DominoPiece(int textureId) {
		strips = new ArrayList<TexturedTriangleStrip>();
		strips.add(new TexturedTriangleStrip(front_to_bottom_coords, textureId));
		strips.add(new TexturedTriangleStrip(right_coords, textureId));
		strips.add(new TexturedTriangleStrip(left_coords, textureId));
	}

	public void rotate(GL10 gl, float frequency) {
		long steps = (long)(1/frequency * 1000);
        long stepno = SystemClock.uptimeMillis() % steps;
        float angle = 360f/steps * ((int) stepno);
        gl.glRotatef(30, 1.0f, 0, 0);
        gl.glRotatef(angle, 0, 1.0f, 0);
	}
	
	private void scaleCorrection(GL10 gl) {
		if (scaleTendency == "up" && scaleFactor < 2.0) {
			scaleFactor += 0.1;
		} else if (scaleTendency == "down" && scaleFactor > 1.0) {
			scaleFactor -= 0.1;
		}
		gl.glScalef(scaleFactor, scaleFactor, scaleFactor);
	}

	public void draw(GL10 gl) {
		scaleCorrection(gl);
		for (TexturedTriangleStrip strip : strips) {
			strip.draw(gl);			
		}
	}
}
