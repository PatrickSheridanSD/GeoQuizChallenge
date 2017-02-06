package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY = "cheat";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] myQuestionBank = new Question[]{
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,true),
            new Question(R.string.question_ireland,true)
           // new Question(R.string.noQ,true)
    };

    private int mEndQuestions = myQuestionBank.length;

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private void updateQuestion(){
        int question = myQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    public void noBack(){
        int check = 0;
        if (mCurrentIndex <= 0 ){
            check = R.string.no_back;
        }
        Toast.makeText(this,check,Toast.LENGTH_LONG).show();
    }

    public void tooFar(){
            int check = 0;
            check = R.string.too_far;
        Toast.makeText(this,check,Toast.LENGTH_LONG).show();

    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = myQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % myQuestionBank.length;
                updateQuestion();
            }
        });


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                  checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mCurrentIndex >= (myQuestionBank.length - 1)){
                    mCurrentIndex = (myQuestionBank.length -1);
                    tooFar();
                    updateQuestion();
                }else {
                    mCurrentIndex = (mCurrentIndex + 1) % myQuestionBank.length;
                    mIsCheater = false;
                    updateQuestion();
                }

            }
        });
        //updateQuestion();

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mCurrentIndex <=0){
                    mCurrentIndex = 0;
                    noBack();
                    mIsCheater = false;
                }else{
                mCurrentIndex = (mCurrentIndex - 1) % myQuestionBank.length;
                mIsCheater = false;
                    updateQuestion();
            }}

        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ///startCheating
                boolean answerIsTrue = myQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(KEY);
        }
        updateQuestion();


    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putBoolean(KEY,mIsCheater);
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() Called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() Called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() Called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() Called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() Called");
    }
}
