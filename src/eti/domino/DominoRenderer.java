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
	
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		// TODO Auto-generated method stub
	}

	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}
}
