package eti.domino;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ColorTriangles {
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private int vertexCount;
	private Color color;
	private int method;
	
	public ColorTriangles(float[] coords, Color color) {
		this(coords, color, GL10.GL_TRIANGLE_STRIP);
	}

	public ColorTriangles(float[] coords, Color color, int method) {
		this.color = color;
		this.method = method;
		this.vertexCount = coords.length / 3;

		vertexBuffer = GLHelpers.floatBuffer(vertexCount * 3);
		indexBuffer = GLHelpers.shortBuffer(vertexCount);

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < 3; j++) {
				vertexBuffer.put(coords[i * 3 + j] * 0.2f);
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			indexBuffer.put((short) i);
		}

		vertexBuffer.position(0);
		indexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glDrawElements(method, vertexCount, GL10.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
