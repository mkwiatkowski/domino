package eti.domino;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class TexturedTriangleStrip {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private FloatBuffer normalBuffer;
	private ShortBuffer indexBuffer;
	private int textureId;
	private int vertexCount;

	public TexturedTriangleStrip(float[] coords, float[] normal, int textureId) {
		this.textureId = textureId;
		this.vertexCount = coords.length / 3;

		vertexBuffer = GLHelpers.floatBuffer(vertexCount * 3);
		textureBuffer = GLHelpers.floatBuffer(vertexCount * 2);
		normalBuffer = GLHelpers.floatBuffer(vertexCount * 3);
		indexBuffer = GLHelpers.shortBuffer(vertexCount);

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < 3; j++) {
				vertexBuffer.put(coords[i * 3 + j] * 0.2f);
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < 2; j++) {
				textureBuffer.put(coords[i * 3 + j] * 0.2f);
			}
		}
		
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < 3; j++) {
				normalBuffer.put(normal[j]);
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			indexBuffer.put((short) i);
		}

		vertexBuffer.position(0);
		textureBuffer.position(0);
		normalBuffer.position(0);
		indexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		useTexture(gl, textureId);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, vertexCount, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	private void useTexture(GL10 gl, int textureId) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	}
}

