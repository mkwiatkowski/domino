package eti.domino;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class DominoRenderer implements GLSurfaceView.Renderer {
	private Context context;
	
	public DominoRenderer(Context context) {
		this.context = context;
	}

	public void touch(float x, float y) {
	   // TODO
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);

		gl.glClearColor(.5f, .5f, .5f, 1);
	}
	
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		gl.glViewport(0, 0, w, h);
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
	}
}
