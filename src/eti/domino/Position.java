package eti.domino;

public class Position {
	protected float x;
	protected float y;
	protected float z;

	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Position higher(float by) {
		return new Position(this.getX(), this.getY() + by, this.getZ());
	}
	
	public Position shifted(float by) {
		return new Position(this.getX() + by, this.getY(), this.getZ());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
}
