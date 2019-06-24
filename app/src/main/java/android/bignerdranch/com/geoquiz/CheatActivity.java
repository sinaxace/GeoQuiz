package android.bignerdranch.com.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "android.bignerdranch.com.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "android.bignerdranch.com.geoquiz.answer_shown";

    private boolean mAnswerIsTrue; //The boolean for containing final cheat answer

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    /**
     * The newIntent method is for connecting the activity's data over explicitly, so that
     * CheatActivity is aware of the actual answer that needs to be revealed.
     * @param packageContext contains the main activity data (QuizActivity)
     * @param answerIsTrue passes over whether the cheating answer is true/false
     */
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue); //Passing value from QuizActivity using Intent Extras
        return intent;
    }

    /**
     *
     * @param result contains data from the Intent object in QuizActivity.
     * @return sends back the Extra value.
     */
    public static boolean wasAnswerShown(Intent result) {
        //getBooleanExtra is a method for retrieving value from the Intent Extra constant.
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    /**
     * This onCreate method is instantiated once the new Intent method has been called.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false); //Assigning cheat answer.

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);

                //Example of Android Lint warning due to minSDK version not supporting methods below.
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) { //Condition for checking SDK version
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        }); //Note: This is just a small animation, more is taught about animation programming in later chapters.
    }

    /**
     * This method is for letting the parent activity know whether the user cheated so that an alternate Toast will appear.
     * @param isAnswerShown collects a true value from mShowAnswerButton once clicked.
     */
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
