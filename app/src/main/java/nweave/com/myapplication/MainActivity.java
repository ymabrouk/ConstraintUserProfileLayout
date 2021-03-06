package nweave.com.myapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private boolean isOpen = false ;
    private ConstraintSet layout1,layout2;
    private ConstraintLayout constraintLayout ;
    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ((TextView)findViewById(R.id.textView9)).setMovementMethod(new ScrollingMovementMethod());

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        imageViewPhoto = findViewById(R.id.photo);
        constraintLayout = findViewById(R.id.constraint_layout);
        layout2.clone(this,R.layout.profile_expanded);
        layout1.clone(constraintLayout);

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                collapseOrExpand();
            }
        });

        ImageView userImageProfile = findViewById(R.id.imageView);
        userImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseOrExpand();
            }
        });

    }

    private void collapseOrExpand() {
        if (!isOpen) {
            TransitionManager.beginDelayedTransition(constraintLayout);
            layout2.applyTo(constraintLayout);
            isOpen = !isOpen ;
        }

        else {

            TransitionManager.beginDelayedTransition(constraintLayout);
            layout1.applyTo(constraintLayout);
            isOpen = !isOpen ;

        }
    }
}
