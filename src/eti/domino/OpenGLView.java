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

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {
                dominoRenderer.touch(event.getX(), event.getY());
            }});
        return true;
    }
}
