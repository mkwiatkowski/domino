package eti.domino;

public class Position {
	public float x;
	public float y;
	public float z;

	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Position higher(float by) {
		return new Position(this.x, this.y + by, this.z);
	}
	
	public Position shifted(float by) {
		return new Position(this.x + by, this.y, this.z);
	}
}
