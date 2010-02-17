package eti.domino;

public class RelativePosition extends Position {
	private Object3D object;

	public RelativePosition(Object3D object, float x, float y, float z) {
		super(x, y, z);
		this.object = object;
	}

	public RelativePosition(Object3D object) {
		this(object, 0, 0, 0);
	}

	public float getX() {
		return object.position.getX() + x * object.getScaleFactor();
	}

	public float getY() {
		return object.position.getY() + y * object.getScaleFactor();
	}

	public float getZ() {
		return object.position.getZ() + z * object.getScaleFactor();
	}
}
