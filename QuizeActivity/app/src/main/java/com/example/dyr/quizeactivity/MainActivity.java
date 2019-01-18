package com.example.dyr.quizeactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index_key";
    private static final String TAG = "MainActivity";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionText;

    private Qusetion[] mQuestionBank = new Qusetion[]{
            new Qusetion(R.string.question_prc,true),
            new Qusetion(R.string.question_japan,false),
            new Qusetion(R.string.question_usa,false),
            new Qusetion(R.string.question_uk,true),
            new Qusetion(R.string.question_russia,true),
    };

    private int mCurrentIndext = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate called");
        if(savedInstanceState != null){
            mCurrentIndext = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionText = (TextView) findViewById(R.id.textQuiz);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (Button) findViewById(R.id.next_button);

        mTrueButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndext = (mCurrentIndext + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        saveInstanceState.putInt(KEY_INDEX, mCurrentIndext);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndext].getTextResId();
        mQuestionText.setText(question);
    }

    private void checkAnswer(boolean isCorrect){
        boolean answerIsTrue = mQuestionBank[mCurrentIndext].isAnswerTrue();
        int messageId = 0;
        if(answerIsTrue == isCorrect){
            messageId = R.string.correct_text;
            mCurrentIndext = (mCurrentIndext + 1)%mQuestionBank.length;
            updateQuestion();
        }else{
            messageId = R.string.incorrect_text;
        }

        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
    }
}
