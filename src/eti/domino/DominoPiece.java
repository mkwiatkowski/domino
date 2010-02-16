package eti.domino;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DominoPiece {
	private Position position;
	private ArrayList<Object3D> objects; 
	
	public DominoPiece(Position position) {
		this.position = position;		
		objects = new ArrayList<Object3D>();
        objects.add(new Cuboid(position));
        objects.add(new Circle(position));
	}
	
	public void draw(GL10 gl) {
		for (Object3D object : objects) {
			object.draw(gl);
		}
	}
	
	public boolean containsPoint(float x, float y) {
		final float halfPieceWidth = 0.12f;
		final float halfPieceHeight = 0.24f;
		return x > position.x-halfPieceWidth && x < position.x+halfPieceWidth
			&& y > position.y-halfPieceHeight && y < position.y+halfPieceHeight;
	}

	public void activate() {
		for (Object3D object : objects) {
			object.scaleTendency = "up";
		}
	}
	
	public void deactivate() {
		for (Object3D object : objects) {
			object.scaleTendency = "down";
			object.rotationFrequency = 0;
		}
	}
}
