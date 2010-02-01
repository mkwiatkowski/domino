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
	private static float[] front_normal = {0,0,1};
	private static float[] top_coords = {
		-0.5f, 1, 0.1f,
		0.5f, 1, 0.1f,
		-0.5f, 1, -0.1f,
		0.5f, 1, -0.1f
	};
	private static float[] top_normal = {0,1,0};
	private static float[] back_coords = {
		-0.5f, 1, -0.1f,
		0.5f, 1, -0.1f,
		-0.5f, -1, -0.1f,
		0.5f, -1, -0.1f
	};
	private static float[] back_normal = {0,0,-1};
	private static float[] bottom_coords = {
		-0.5f, -1, -0.1f,
		0.5f, -1, -0.1f,
		-0.5f, -1, 0.1f,
		0.5f, -1, 0.1f
	};
	private static float[] bottom_normal = {0,-1,0};
	private static float[] right_coords = {
		0.5f, -1, 0.1f,
		0.5f, -1, -0.1f,
		0.5f, 1, 0.1f,
		0.5f, 1, -0.1f
	};
	private static float[] right_normal = {1,0,0};
	private static float[] left_coords = {
		-0.5f, -1, 0.1f,
		-0.5f, -1, -0.1f,
		-0.5f, 1, 0.1f,
		-0.5f, 1, -0.1f
	};
	private static float[] left_normal = {-1,0,0};
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
        strips.add(new TexturedTriangleStrip(front_coords, front_normal, frontTextureId));
        strips.add(new TexturedTriangleStrip(top_coords, top_normal, backTextureId));
        strips.add(new TexturedTriangleStrip(back_coords, back_normal, backTextureId));
        strips.add(new TexturedTriangleStrip(bottom_coords, bottom_normal, backTextureId));
        strips.add(new TexturedTriangleStrip(right_coords, right_normal, backTextureId));
        strips.add(new TexturedTriangleStrip(left_coords, left_normal, backTextureId));
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
