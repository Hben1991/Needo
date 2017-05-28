package com.hben.needo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import java.util.Timer;
import java.util.TimerTask;

import static com.hben.needo.AppConstants.NEW_POST_TITLE_MAX_LENGTH;

public class AddNewPostActivity extends AppCompatActivity {

    private final long DELAY = 800;
    private Timer timer = new Timer();

    //Ui
    private TextView title;
    private TextView content;
    private CircularProgressButton submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post_layout);

        initViews();
        setFilters();
        submitNewPost();
    }

    private void initViews() {

        title = (TextView) findViewById(R.id.inputTitleEditText);
        content = (TextView) findViewById(R.id.inputContentEditText);
        submitButton = (CircularProgressButton) findViewById(R.id.submitButton);
    }

    private void setFilters() {

        title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NEW_POST_TITLE_MAX_LENGTH)});
        submitButton.setProgress(0);
    }

    private void submitNewPost() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                if (title.getText().toString().trim().length() == 0 || content.getText().toString().trim().length() == 0) {
                    submitButton.setIndeterminateProgressMode(true);
                    submitButton.setProgress(-1);

                    title.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            submitButton.setProgress(0);
                        }
                    });
                    content.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            submitButton.setProgress(0);
                        }
                    });

                } else {
                    returnIntent.putExtra("title", title.getText().toString());
                    returnIntent.putExtra("content", content.getText().toString());
                    submitButton.setIndeterminateProgressMode(true);
                    for (int i = 0; i < 100; i++) {
                        submitButton.setProgress(i);
                    }
                    submitButton.setProgress(100);
                    submitButton.setCompleteText(getString(R.string.done));
                    setResult(AddNewPostActivity.RESULT_OK, returnIntent);

                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },
                            DELAY
                    );
                }
            }
        });
    }

}
