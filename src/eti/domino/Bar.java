package eti.domino;

import javax.microedition.khronos.opengles.GL10;

public class Bar extends Object3D {
	private static float[] coords = {
		-0.45f, 0.05f, 0,
		0.45f, 0.05f, 0,
		-0.45f, -0.05f, 0,
		0.45f, -0.05f, 0
	};
	private ColorTriangles triangles;

	public Bar(Position position) {
		super(position);
		Color color = new Color(0.0f, 0.0f, 0.0f);
        triangles = new ColorTriangles(coords, color);
	}
	
	public void draw(GL10 gl) {
		drawBefore(gl);
		triangles.draw(gl);
		drawAfter(gl);
	}
}
