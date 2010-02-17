package eti.domino;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;

public class Object3D {
	protected Position position;
	protected float rotationFrequency = 0;
	private long rotationStart;
	private int tilt = 0;
	private float maxScaleUp = 1.5f;
	private float scaleFactor = 1.0f;
	protected String scaleTendency = "down";
	
	public Object3D(Position position) {
		this.position = position;
	}
	
	public void draw(GL10 gl) {}
	
	public void drawBefore(GL10 gl) {
		gl.glPushMatrix();
		positionCorrection(gl);
		scaleCorrection(gl);
		rotationCorrection(gl);
		tiltCorrection(gl);
	}
	
	public void drawAfter(GL10 gl) {
		gl.glPopMatrix();
	}

	public float getScaleFactor() {
		return scaleFactor;
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
		gl.glTranslatef(position.getX(), position.getY(), position.getZ());
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
