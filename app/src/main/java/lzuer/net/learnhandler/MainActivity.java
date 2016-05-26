package lzuer.net.learnhandler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private ProgressBar progressBar;
    private TextView text;

    private TextView sendCode;

    private int progress;
    private boolean clocking = false;
    private Timer timer;

    private Handler sendCodeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress == 0) {
                return;
            }
            progress--;
            if (progress == 0) {
                clocking = false;
                sendCode.setText("重新发送");
                if (timer != null) {
                    timer.cancel();
                }
            } else {
                sendCode.setText(progress + "s后重新发送");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        text = (TextView) findViewById(R.id.message);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);

        //code
        sendCode = (TextView) findViewById(R.id.sendCode);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clocking) {
                    Toast.makeText(MainActivity.this, "sending...", Toast.LENGTH_SHORT).show();
                    return;
                }
                calculateSendTime();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    final int value = i + 1;
                    progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(value + "");
                            progressBar.setProgress(value);
                        }
                    });
                }
            }
        };

        new Thread(runnable).start();
    }

    private void calculateSendTime() {
        clocking = true;
        progress = 20;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendCodeHandler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }
}
