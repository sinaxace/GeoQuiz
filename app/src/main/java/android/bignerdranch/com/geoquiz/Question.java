package android.bignerdranch.com.geoquiz;

public class Question {
    //Note that the 'm' prefix is for Data Members of the class.
    private int mTextResId; //It's an int because it holds String of resource ID.
    private boolean mAnswerTrue;

    /**
     *
     * @param textResID Grabs a resource ID argument to store in mTextResId member.
     * @param answerTrue Grabs the boolean result of the user's choice for storing in mAnswerTrue member.
     */
    public Question(int textResID, boolean answerTrue) {
        mTextResId = textResID;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
