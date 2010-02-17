package eti.domino;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DominoPiece {
	final private float halfPieceWidth = 0.12f;
	final private float halfPieceHeight = 0.24f;
	final private float dotStep = halfPieceWidth / 2.2f;
	final private float tablePieceDistance = 0.43f;

	public int topDots, bottomDots;
	public boolean topFree, bottomFree;
	private Position position;
	private ArrayList<Object3D> objects; 

	public DominoPiece(int topDots, int bottomDots) {
		this.topDots = topDots;
		this.bottomDots = bottomDots;
		this.topFree = true;
		this.bottomFree = true;
		this.position = new Position(0,0,0);

		Cuboid cuboid = new Cuboid(position);
		objects = new ArrayList<Object3D>();
        objects.add(cuboid);
        objects.add(new Bar(new RelativePosition(cuboid)));
        createDots();
	}

	public Position getPositionOnePieceHigher() {
		return position.higher(tablePieceDistance);
	}

	public Position getPositionOnePieceLower() {
		return position.higher(-tablePieceDistance);
	}

	public Position getPositionCopy() {
		return new Position(position);
	}

	public void setPosition(float x, float y, float z) {
		position.setX(x);
		position.setY(y);
		position.setZ(z);
	}
	
	public void setPosition(Position position) {
		setPosition(position.getX(), position.getY(), position.getZ());
	}

	public void flip() {
		int tmp = this.topDots;
		this.topDots = this.bottomDots;
		this.bottomDots = tmp;
		boolean tmp2 = this.topFree;
		this.topFree = this.bottomFree;
		this.bottomFree = tmp2;
		recreateDots();
	}
	
	public void draw(GL10 gl) {
		for (Object3D object : objects) {
			object.draw(gl);
		}
	}
	
	public void drawWithoutDots(GL10 gl) {
		for (int i=0; i < 2; i++) {
			objects.get(i).draw(gl);
		}
	}
	
	public boolean containsPointOnPlayerPieces(float x, float y) {
		return x > position.getX()-halfPieceWidth && x < position.getX()+halfPieceWidth
			&& y > position.getY()-halfPieceHeight && y < position.getY()+halfPieceHeight;
	}

	public boolean containsPointOnTable(float x, float y) {
		return x > position.getX()-halfPieceWidth*DominoRenderer.tableScale
			&& x < position.getX()+halfPieceWidth*DominoRenderer.tableScale
			&& y > position.getY()-halfPieceHeight*DominoRenderer.tableScale
			&& y < position.getY()+halfPieceHeight*DominoRenderer.tableScale;
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
	
	public void tilt(int tilt) {
		for (Object3D object : objects) {
			object.setTilt(tilt);
		}		
	}

	public boolean fitsWith(int dots) {
		return this.topDots == dots || this.bottomDots == dots;
	}

	private void recreateDots() {
		while (objects.size() > 2) {
			objects.remove(objects.size()-1);
		}
		createDots();
	}

	private void createDots() {
		Object3D cuboid = this.objects.get(0);
        addDots(cuboid, topDots, 0.11f);
        addDots(cuboid, bottomDots, -0.11f);		
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
	
	public String toString() {
		return "["+topDots+(topFree ? "*" : "")+" / "+bottomDots+(bottomFree ? "*" : "")+"]";
	}
}
