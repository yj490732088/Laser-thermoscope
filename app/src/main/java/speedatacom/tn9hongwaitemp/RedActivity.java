package speedatacom.tn9hongwaitemp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.speedata.temperture.TN90Model;
import com.speedata.temperture.Temperture;
import com.speedata.temperture.TempertureCallBack;

import util.PlaySoundPool;

public class RedActivity extends Activity implements View.OnClickListener {
    private final String TAG = "RedActivity";

    private TextView tvmubian, btnStart;


    private PlaySoundPool playSoundPool;
    private TN90Model tn90Model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_read);
        playSoundPool = PlaySoundPool.getPlaySoundPool(this);
        initUI();
        tn90Model = new TN90Model();
        tn90Model.openDev();
        tn90Model.setCallBack(new TempertureCallBack() {
            @Override
            public void receTempData(Temperture temperture) {
                String msg = "";
                if (temperture.getBodyTemp() < 29) {
                    playSoundPool.playError();
                    msg = "体温低"+temperture.getBodyTemp();
                } else if (temperture.getBodyTemp() > 39) {
                    playSoundPool.playError();
                    msg = "高温警告";
                } else {
                    playSoundPool.playLaser();
                    msg = temperture.getBodyTemp() + "℃";
                }
                final String finalMsg = msg;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvmubian.setText(finalMsg);
                        btnStart.setEnabled(true);
                    }
                });
            }
        });
    }

    private void initUI() {
        tvmubian = (TextView) findViewById(R.id.tv_mubiao);
        btnStart = (TextView) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        btnStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.v("LogTemp", "Start Thread");
                    btnStart.setEnabled(false);
                    tn90Model.startMeasurement(true);
                }
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart) {
            tn90Model.startMeasurement(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        tn90Model.stopMeasurement();
        tn90Model.closeDev();
        super.onDestroy();
    }


}
