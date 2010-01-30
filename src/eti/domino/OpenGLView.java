package eti.domino;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class OpenGLView extends GLSurfaceView {
	DominoRenderer dominoRenderer;

	public OpenGLView(Context context) {
		super(context);
		dominoRenderer = new DominoRenderer(context);
		setEGLConfigChooser(false);
		setRenderer(dominoRenderer);
	}

	// http://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html
	//
	// The renderer will be called on a separate thread, so that rendering
	// performance is decoupled from the UI thread. Clients typically need to
	// communicate with the renderer from the UI thread, because that's where
	// input events are received.
	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					dominoRenderer.touch(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					dominoRenderer.release(event.getX(), event.getY());
					break;
				}

			}
		});
		return true;
	}
}
