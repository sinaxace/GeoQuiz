package android.bignerdranch.com.geoquiz;

/* Note: This beginner project was tested on API Levels 24 & 25 on the Emulator
         Also this was built in Linux so refactoring is necessary for Windows 10 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * QuizActivity is the Controller class for the GeoQuiz app.
 * Chapters 1 to 5 of the 3rd Edition of Android Programming: The Big Nerd Ranch Guide contain
 * details of this project that were designed by the following authors:
 * @author Bill Phillips --> Instructor at Big Nerd Ranch.
 * @author Chris Stewart --> Director of Big Nerd Ranch's Android team.
 * @author Kristin Marsicano --> Android Developer at Big Nerd Ranch.
 *
 * This is a beginner project for learning the Android SDK basics after being taught Core Java &
 * JavaFX Application Development at Sheridan College. Modified code is specified when the comment
 * starts with the challenge name.
 *
 * @modifier Sina Maleki --> Student of FAST programs at Trafalgar campus in Oakville ON.
 */
public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    //For making log messages to see how Logcat works in Android Studio.
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    //An Object array for referencing Question Objects from string res file.
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    }; //Note: there's a better way to do this but for now, we're beginning this way.

    private int mCurrentIndex = 0;
    private byte score = 0;
    private boolean mIsCheater;

    /**
     * onCreate(Bundle) is called when a new instance of activity subclass is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Since this is a protected method, we'll need to call Activity's onCreate(Bundle)
        Log.d(TAG, "onCreate(Bundle) called"); //Part of stacktrace debugging
        setContentView(R.layout.activity_quiz); //Setting up the UI...

        //If the activity instance exists...
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        //Challenge: Add Listener to TextView (similar to button because they all inherit from View superclass)
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //We usually create Anonymous inner classes for events, but Lambdas are an option too.
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        }); //End of modification by Sina.

        //For connecting the Controller's data member to the widget (View Object)
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                //Click buttons only once per question.
                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
            }
        });

        /*Challenge: Add a Previous Button and replace Next & Previous buttons with images.
            ImageButton inherits from ImageView while Button inherits from TextView.*/
        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is for fixing the bug that crashes the program when pressing the previous button at index 0 (question 1)
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = 6;
                } //To prevent program from exiting when pressing previous.
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        }); //End of Modification by Sina.

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Starts CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                /*Intent is an Object that a component(Activities, Services, Broadcast Recievers, etc...)
                communicate with the OS*/
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue); //Explicit Intent
                //We're telling ActivityManager which activity to start.
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    /**
     * onActivityResult(int, int, Intent) makes sure that the requestCode and ResultCode are as expected
     * to make future maintenance easier.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    /*The following methods below are for demonstrating how the activity lifecycle works and
       how I can use it in the Logcat*/
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


    /**
     * @param userPressedTrue accepts a boolean argument from user.
     * checkAnswer() compares the user's answer with the Question object's boolean.
     */
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                score++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        //Challenge: Reposition the Toast at the top of screen
        Toast myToast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.TOP, 0, 200);
        myToast.show(); //End of modification by Sina
    }

    /**
     * updateQuestion() implements the DRY principal (Don't Repeat Yourself) to ensure best practice.
     */
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        //Challenge: Make buttons only clickable once per question and display score once finished.
        //Resetting the buttons as clickable once next question is generated.
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        //Shows the final score at first question.
        if (mCurrentIndex == 0) {
            String scoreToast = getResources().getString(R.string.total_score) + " " + score;
            Toast.makeText(this, scoreToast, Toast.LENGTH_SHORT).show();
            score = 0;
        } //End of Modification by Sina
    }
}