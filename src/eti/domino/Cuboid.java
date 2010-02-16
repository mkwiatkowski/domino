package eti.domino;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class Cuboid extends Object3D {
	private static float[] front_coords = {
		-0.5f, -1, 0.1f,
		0.5f, -1, 0.1f,
		-0.5f, 1, 0.1f,
		0.5f, 1, 0.1f	
	};
	private static float[] top_coords = {
		-0.5f, 1, 0.1f,
		0.5f, 1, 0.1f,
		-0.5f, 1, -0.1f,
		0.5f, 1, -0.1f
	};
	private static float[] back_coords = {
		-0.5f, 1, -0.1f,
		0.5f, 1, -0.1f,
		-0.5f, -1, -0.1f,
		0.5f, -1, -0.1f
	};
	private static float[] bottom_coords = {
		-0.5f, -1, -0.1f,
		0.5f, -1, -0.1f,
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
	private ArrayList<ColorTriangles> strips; 

	public Cuboid(Position position) {
		super(position);
		Color color = new Color(1.0f, 0.8f, 0.5f);
        strips = new ArrayList<ColorTriangles>();
        strips.add(new ColorTriangles(front_coords, color));
        strips.add(new ColorTriangles(top_coords, color));
        strips.add(new ColorTriangles(back_coords, color));
        strips.add(new ColorTriangles(bottom_coords, color));
        strips.add(new ColorTriangles(right_coords, color));
        strips.add(new ColorTriangles(left_coords, color));
	}

	public void draw(GL10 gl) {
		drawBefore(gl);
		for (ColorTriangles strip : strips) {
			strip.draw(gl);
		}
		drawAfter(gl);
	}
}
