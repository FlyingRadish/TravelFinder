package org.liangxw.travelfinder.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.component.zxing.encode.QRCodeEncoder;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.logger.Log;


public class GroupQRCodeActivity extends BaseActivity implements ActivityStack.ActivityRule {

    private final static String TAG = GroupQRCodeActivity.class.getSimpleName();

    String QRCode;
    ImageView imgQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_qr_code);
        super.onCreate(savedInstanceState);

        imgQRCode = (ImageView) findViewById(R.id.img_qr_code);



        QRCode = getIntent().getAction();
        Log.i(TAG, "QRCode:" + QRCode);
        if (QRCode == null) {
            toast("错误的传递参数");
            Log.i(TAG, "wrong params");
            return;
        }
        QRCode = Globe.QR_PREFIX + QRCode;
        imgQRCode.postDelayed(new Runnable() {
            @Override
            public void run() {
                genarateQRCode(QRCode);
            }
        },2000);

    }

    Bitmap bitmap;

    private void genarateQRCode(String qrCode) {
        try {
            bitmap = QRCodeEncoder.encode(qrCode, imgQRCode.getWidth(), imgQRCode.getHeight());
            imgQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            toast("生成二维码失败");
            Log.i(TAG, "failed to create QRCode");
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected ActivityStack.ActivityRule getActivityRule() {
        return this;
    }

    @Override
    public boolean onPop(Activity prev, Activity now) {
        Log.i(TAG, "onPop, prev:" + prev.getClass().getSimpleName());
        if (!(prev instanceof GroupMapActivity)) {
            Log.i(TAG, "create");
            startActivity(GroupMapActivity.class);
        }
        return true;
    }
}
