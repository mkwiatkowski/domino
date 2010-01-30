package eti.domino;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class DominoActivity extends Activity {
    private GLSurfaceView openGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openGLView = new OpenGLView(this);
        setContentView(openGLView);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        openGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openGLView.onResume();
    }
}
