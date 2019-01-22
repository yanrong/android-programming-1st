package com.example.dyr.quizeactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.dyr.quiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOW =
            "com.example.dyr.quiz.answer_show";
    private boolean mAnswerIsTrue;

    private Button mShowAnswer;
    private Button mQuitAnswer;
    private TextView mWarnText;
    private TextView mShowAnswerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mQuitAnswer = (Button) findViewById(R.id.quit_answer_button);
        mWarnText = (TextView) findViewById(R.id.warning_textView);
        mShowAnswerText = (TextView) findViewById(R.id.cheat_textView);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mShowAnswerText.setText(R.string.correct_text);
                }else{
                    mShowAnswerText.setText(R.string.incorrect_text);
                }

                setAnswerShowResult(true);
            }
        });

    }

    private void setAnswerShowResult(boolean isAnswerIsShow){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerIsShow);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);

        return intent;
    }

    public static boolean wasAnswerShow(Intent intent){
        return intent.getBooleanExtra(EXTRA_ANSWER_SHOW, false);
    }
}
