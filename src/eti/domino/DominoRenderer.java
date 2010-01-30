package eti.domino;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
	private Object3D piece;
	private int mTextureID;

	public DominoRenderer(Context context) {
		this.context = context;
		piece = new DominoPiece();
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

		mTextureID = loadTexture(gl, R.drawable.piece);
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
		piece.draw(gl, mTextureID);
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

class Object3D {
	private int drawMode = GL10.GL_TRIANGLE_STRIP;
	private int vertexCount;
	private float scaleFactor = 1.0f;
	public String scaleTendency = "down";
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ShortBuffer indexBuffer;

	public Object3D(float[] coords) {
		vertexCount = coords.length / 3;
		
		vertexBuffer = GLHelpers.floatBuffer(vertexCount * 3);
		textureBuffer = GLHelpers.floatBuffer(vertexCount * 2);
		indexBuffer = GLHelpers.shortBuffer(vertexCount);

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < 3; j++) {
				vertexBuffer.put(coords[i * 3 + j] * 0.2f);
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < 2; j++) {
				textureBuffer.put(coords[i * 3 + j] * 0.2f);
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			indexBuffer.put((short) i);
		}

		vertexBuffer.position(0);
		textureBuffer.position(0);
		indexBuffer.position(0);
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

	public void draw(GL10 gl, int textureId) {
		scaleCorrection(gl);
		useTexture(gl, textureId);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glDrawElements(drawMode, vertexCount, GL10.GL_UNSIGNED_SHORT, indexBuffer);
	}

	private void useTexture(GL10 gl, int textureId) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glActiveTexture(GL10.GL_TEXTURE0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		gl.glFrontFace(GL10.GL_CCW);
	}
}

class DominoPiece extends Object3D {
	private static float[] coords = {
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
			0.5f, -1, 0.1f,
			// right
			0.5f, 1, 0.1f,
			0.5f, -1, -0.1f,
			0.5f, 1, -0.1f,
			// left
			-0.5f, -1, -0.1f,
			-0.5f, 1, -0.1f,
			-0.5f, -1, 0.1f,
			-0.5f, 1, 0.1f
			};
	public DominoPiece() {
		super(coords);
	}
}
