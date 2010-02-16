package eti.domino;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DominoPiece {
	final private float halfPieceWidth = 0.12f;
	final private float halfPieceHeight = 0.24f;
	final private float dotStep = halfPieceWidth / 2.2f;

	private Position position;
	private ArrayList<Object3D> objects; 
	
	public DominoPiece(Position position, int top_dots, int bottom_dots) {
		this.position = position;		
		objects = new ArrayList<Object3D>();
        objects.add(new Cuboid(position));
        objects.add(new Bar(position));
        addDots(top_dots, 0.12f);
        addDots(bottom_dots, -0.12f);
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
	
	private void addDots(int number, float offset) {
		switch (number) {
		case 1:
			addDots1(offset);
			break;
		case 3:
			addDots1(offset);
			addDots2(offset);
			break;
		case 2:
			addDots2(offset);
			break;
		case 5:
			addDots1(offset);
			addDots4(offset);		
			break;
		case 4:
			addDots4(offset);
			break;
		case 6:
			addDots4(offset);
			objects.add(new Circle(position.higher(offset).shifted(dotStep)));
			objects.add(new Circle(position.higher(offset).shifted(-dotStep)));
			break;
		}
	}
	
	private void addDots1(float offset) {
		objects.add(new Circle(position.higher(offset)));
	}
	
	private void addDots2(float offset) {
		objects.add(new Circle(position.higher(offset + dotStep).shifted(-dotStep)));
		objects.add(new Circle(position.higher(offset - dotStep).shifted(dotStep)));
	}
	
	private void addDots4(float offset) {
		objects.add(new Circle(position.higher(offset + dotStep).shifted(-dotStep)));
		objects.add(new Circle(position.higher(offset + dotStep).shifted(dotStep)));
		objects.add(new Circle(position.higher(offset - dotStep).shifted(-dotStep)));
		objects.add(new Circle(position.higher(offset - dotStep).shifted(dotStep)));	
	}
}
