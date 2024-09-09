package com.fast.lhr.nu.edu.pk.juba.quizApp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView questionView, scoreView, timeView, finalScoreView;
    RadioButton radioOption1, radioOption2, radioOption3, radioOption4;
    RadioGroup radioOptions;
    Button btnNext, btnPrev, btnConfirm, btnPeek, btnEnd;
    int currentQuestion = 0, score = 0, time = 180, totalScore = 100;
    boolean examEnded = false;
    Question [] allQuestions = new Question[20];
    CountDownTimer cTimer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(examEnded) {return;}
                showNextQuestion();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(examEnded) {return;}
                currentQuestion = (currentQuestion - 1);
                if (currentQuestion == -1) {
                    currentQuestion = 19;
                }
                resetText();
                radioOptions.clearCheck();
            }
        });
        btnPeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(examEnded) {return;}

                int currentAnswer = allQuestions[currentQuestion].getCorrectOption();
                int id = getResources().getIdentifier("radio_option" + Integer.toString(currentAnswer), "id", getPackageName());

                RadioButton choice = (RadioButton) findViewById(id);

                choice.setChecked(true);

                if (allQuestions[currentQuestion].isAnswered()) {
                    return;
                }

                allQuestions[currentQuestion].setAnswered(true);

                if (score <= 0) {
                    return;
                }

                score--;
                scoreView.setText("Score: " + Integer.toString(score));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allQuestions[currentQuestion].isAnswered() || examEnded) {
                    return;
                }
                if (radioOptions.getCheckedRadioButtonId() == -1) {
                    return;
                }
                allQuestions[currentQuestion].setAnswered(true);
                validateAnswer();
                scoreView.setText("Score: " + Integer.toString(score));

                showNextQuestion();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalScoreView.setText("Final Score: " + Integer.toString(score) + "\nPercentage: "  + Integer.toString(score) + "%");
                finalScoreView.setVisibility(finalScoreView.VISIBLE);
                examEnded = true;

                if (cTimer != null) {
                    cTimer.cancel();
                }
            }
        });

        //to make this into minute timer, just change the countdown interval to 60000, time variable and move the time decrement to the bottom of the onTick function
        cTimer = new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                time--;
                timeView.setText("Remaining time: " + String.valueOf(time) + "s");
            }

            public void onFinish() {
                finalScoreView.setText("Final Score: " + Integer.toString(score) + "\nPercentage: "  + Integer.toString(score) + "%");
                finalScoreView.setVisibility(finalScoreView.VISIBLE);
                examEnded = true;
            }
        }.start();
    }

    private void init() {
        String[] questions = getResources().getStringArray(R.array.questions);
        int[] correctOptions = {3,2,1,3,3,2,3,3,3,2,2,3,1,2,1,1,2,2,2,3};

        for (int i = 0; i < 20; i++) {
            allQuestions[i] = new Question();
            allQuestions[i].setQuestion(questions[i * 5]);
            allQuestions[i].setOption1(questions[(i * 5) + 1]);
            allQuestions[i].setOption2(questions[(i * 5) + 2]);
            allQuestions[i].setOption3(questions[(i * 5) + 3]);
            allQuestions[i].setOption4(questions[(i * 5) + 4]);

            allQuestions[i].setCorrectOption(correctOptions[i]);
        }

        questionView = (TextView)findViewById(R.id.questionView);
        scoreView = (TextView)findViewById(R.id.scoreView);
        timeView = (TextView)findViewById(R.id.timeView);
        finalScoreView = (TextView)findViewById(R.id.finalScoreView);

        btnNext = (Button)findViewById(R.id.btnNext);
        btnPrev = (Button)findViewById(R.id.btnPrev);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnPeek = (Button)findViewById(R.id.btnPeek);
        btnEnd = (Button)findViewById(R.id.btnEnd);

        radioOptions = (RadioGroup)findViewById(R.id.radioOptions);
        radioOption1 = (RadioButton)findViewById(R.id.radio_option1);
        radioOption2 = (RadioButton)findViewById(R.id.radio_option2);
        radioOption3 = (RadioButton)findViewById(R.id.radio_option3);
        radioOption4 = (RadioButton)findViewById(R.id.radio_option4);

        resetText();
        scoreView.setText("Score: " + Integer.toString(score));
        timeView.setText("Remaining time: " + String.valueOf(time) + "s");
    }

    private void showNextQuestion() {
        currentQuestion = (currentQuestion + 1) % 20;
        resetText();
        radioOptions.clearCheck();
    }

    private void resetText() {
        questionView.setText(allQuestions[currentQuestion].getQuestion());
        radioOption1.setText(allQuestions[currentQuestion].getOption1());
        radioOption2.setText(allQuestions[currentQuestion].getOption2());
        radioOption3.setText(allQuestions[currentQuestion].getOption3());
        radioOption4.setText(allQuestions[currentQuestion].getOption4());
    }

    private void validateAnswer () {
        int correctAnswer = allQuestions[currentQuestion].getCorrectOption();
        if( correctAnswer == 1) {
            if(radioOption1.isChecked()) {
                score += 5;
            }
            else {
                score -= 1;
            }
        } else if (correctAnswer == 2) {
            if(radioOption2.isChecked()) {
                score += 5;
            }
            else {
                score -= 1;
            }
        } else if (correctAnswer == 3) {
            if(radioOption3.isChecked()) {
                score += 5;
            }
            else {
                score -= 1;
            }
        } else if (correctAnswer == 4) {
            if(radioOption4.isChecked()) {
                score += 5;
            }
            else {
                score -= 1;
            }
        }
        if (score < 0 ) {
            score = 0;
        }
    }
}
