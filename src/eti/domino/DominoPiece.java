package eti.domino;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.os.SystemClock;

public class DominoPiece {
	private static float[] front_coords = {
		-0.5f, -1, 0.1f,
		0.5f, -1, 0.1f,
		-0.5f, 1, 0.1f,
		0.5f, 1, 0.1f	
	};
	private static float[] top_to_bottom_coords = {
		// top
		-0.5f, 1, 0.1f,
		0.5f, 1, 0.1f,
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
	private float maxScaleUp = 1.5f;
	private float scaleFactor = 1.0f;
	private String scaleTendency = "down";
	private ArrayList<TexturedTriangleStrip> strips; 
	private Position position;
	private float rotationFrequency = 0;
	private long rotationStart;
	private int tilt = 0;

	public DominoPiece(int frontTextureId, int backTextureId, Position position) {
		this.position = position;
        strips = new ArrayList<TexturedTriangleStrip>();
        strips.add(new TexturedTriangleStrip(front_coords, frontTextureId));
        strips.add(new TexturedTriangleStrip(top_to_bottom_coords, backTextureId));
        strips.add(new TexturedTriangleStrip(right_coords, backTextureId));
        strips.add(new TexturedTriangleStrip(left_coords, backTextureId));
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		positionCorrection(gl);
		scaleCorrection(gl);
		rotationCorrection(gl);
		tiltCorrection(gl);
		for (TexturedTriangleStrip strip : strips) {
			strip.draw(gl);
		}
		gl.glPopMatrix();
	}
	
	public boolean containsPoint(float x, float y) {
		final float halfPieceWidth = 0.12f;
		final float halfPieceHeight = 0.24f;
		return x > position.x-halfPieceWidth && x < position.x+halfPieceWidth
			&& y > position.y-halfPieceHeight && y < position.y+halfPieceHeight;
	}
	
	public void activate() {
		scaleTendency = "up";
	}
	
	public void deactivate() {
		scaleTendency = "down";
		rotationFrequency = 0;
	}

	private void scaleCorrection(GL10 gl) {
		if (scaleTendency == "up") {
			if (scaleFactor < maxScaleUp) {
				scaleFactor += 0.1;
			} else if (rotationFrequency == 0) {
				// Start rotating once it has been made bigger.
				rotationFrequency = 0.5f;
				rotationStart = SystemClock.uptimeMillis();
			}
		} else if (scaleTendency == "down" && scaleFactor > 1.0) {
			scaleFactor -= 0.1;
		}
		gl.glScalef(scaleFactor, scaleFactor, scaleFactor);
	}

	private void positionCorrection(GL10 gl) {
		gl.glTranslatef(position.x, position.y, position.z);
	}

	private void tiltCorrection(GL10 gl) {
		gl.glRotatef(tilt, 1.0f, 0, 0);
	}

	private void rotationCorrection(GL10 gl) {
		if (rotationFrequency > 0) {
			long steps = (long)(1/rotationFrequency * 1000);
			long stepno = (SystemClock.uptimeMillis() - rotationStart) % steps;
			float angle = 360f/steps * ((int) stepno);
			gl.glRotatef(angle, 0, 1.0f, 0);
		}
	}
}
