package eti.domino;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DominoPiece {
	final private float halfPieceWidth = 0.12f;
	final private float halfPieceHeight = 0.24f;

	private Position position;
	private ArrayList<Object3D> objects; 
	
	public DominoPiece(Position position) {
		this.position = position;		
		objects = new ArrayList<Object3D>();
        objects.add(new Cuboid(position));
        objects.add(new Bar(position));
        objects.add(new Circle(position.higher(0.125f)));
	}
	
	public void draw(GL10 gl) {
		for (Object3D object : objects) {
			object.draw(gl);
		}
	}
	
	public boolean containsPoint(float x, float y) {
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
