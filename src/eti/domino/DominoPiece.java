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
		Cuboid cuboid = new Cuboid(position);
		objects = new ArrayList<Object3D>();
        objects.add(cuboid);
        objects.add(new Bar(new RelativePosition(cuboid)));
        addDots(cuboid, top_dots, 0.12f);
        addDots(cuboid, bottom_dots, -0.12f);
	}
	
	public void draw(GL10 gl) {
		for (Object3D object : objects) {
			object.draw(gl);
		}
	}
	
	public boolean containsPoint(float x, float y) {
		return x > position.getX()-halfPieceWidth && x < position.getX()+halfPieceWidth
			&& y > position.getY()-halfPieceHeight && y < position.getY()+halfPieceHeight;
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
	
	private void addDots(Object3D object, int number, float offset) {
		switch (number) {
		case 1:
			addDots1(object, offset);
			break;
		case 3:
			addDots1(object, offset);
			addDots2(object, offset);
			break;
		case 2:
			addDots2(object, offset);
			break;
		case 5:
			addDots1(object, offset);
			addDots4(object, offset);		
			break;
		case 4:
			addDots4(object, offset);
			break;
		case 6:
			addDots4(object, offset);
			objects.add(new Circle(new RelativePosition(object, dotStep, offset, 0)));
			objects.add(new Circle(new RelativePosition(object, -dotStep, offset, 0)));
			break;
		}
	}
	
	private void addDots1(Object3D object, float offset) {
		objects.add(new Circle(new RelativePosition(object, 0, offset, 0)));
	}
	
	private void addDots2(Object3D object, float offset) {
		objects.add(new Circle(new RelativePosition(object, -dotStep, offset + dotStep, 0)));
		objects.add(new Circle(new RelativePosition(object, dotStep, offset - dotStep, 0)));
	}
	
	private void addDots4(Object3D object, float offset) {
		objects.add(new Circle(new RelativePosition(object, -dotStep, offset + dotStep, 0)));
		objects.add(new Circle(new RelativePosition(object, dotStep, offset + dotStep, 0)));
		objects.add(new Circle(new RelativePosition(object, -dotStep, offset - dotStep, 0)));
		objects.add(new Circle(new RelativePosition(object, dotStep, offset - dotStep, 0)));
	}
}
