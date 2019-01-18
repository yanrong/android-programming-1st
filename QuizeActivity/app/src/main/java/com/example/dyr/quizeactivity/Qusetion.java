package com.example.dyr.quizeactivity;

public class Qusetion {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Qusetion(int TextResId, boolean AnswerTrue) {
        this.mTextResId = TextResId;
        this.mAnswerTrue = AnswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int TextResId) {
        mTextResId = TextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean AnswerTrue) {
        mAnswerTrue = AnswerTrue;
    }
}
