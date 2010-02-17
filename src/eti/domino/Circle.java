package eti.domino;

import javax.microedition.khronos.opengles.GL10;

public class Circle extends Object3D {
	private static float[] coords = {
		0, 0, -0.1f,
		0, 0.13f, -0.1f,
		0.13f, 0, -0.1f,
		0, -0.13f, -0.1f,
		-0.13f, 0, -0.1f,
		0, 0.13f, -0.1f
	};
	private ColorTriangles triangles;

	public Circle(Position position) {
		super(position);
		Color color = new Color(0.0f, 0.0f, 0.0f);
        triangles = new ColorTriangles(coords, color, GL10.GL_TRIANGLE_FAN);
	}
	
	public void draw(GL10 gl) {
		drawBefore(gl);
		triangles.draw(gl);
		drawAfter(gl);
	}
}
