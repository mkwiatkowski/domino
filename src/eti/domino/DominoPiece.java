package eti.domino;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.os.SystemClock;

public class DominoPiece {
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
