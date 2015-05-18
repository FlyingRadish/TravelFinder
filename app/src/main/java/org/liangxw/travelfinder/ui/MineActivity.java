package org.liangxw.travelfinder.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.Avatar;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.ImageSelectManager;
import org.liangxw.travelfinder.util.ToastTool;
import org.liangxw.travelfinder.util.logger.Log;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class MineActivity extends BaseActivity {

    private final static String TAG = MineActivity.class.getSimpleName();

    UserWrapper userWrapper;
    @InjectView(R.id.text_nick_name)
    TextView textNickName;
    @InjectView(R.id.text_user_name)
    TextView textUserName;
    @InjectView(R.id.text_phone)
    TextView textPhone;
    @InjectView(R.id.img_avatar)
    ImageView imgAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userWrapper = UserWrapper.getCurrentUser();
        if (userWrapper == null) {
            //to-do
            Log.i(TAG, "no current user");
            ActivityStack.getInstance(this).restart();
            return;
        }

        ButterKnife.inject(this);

        textNickName.setText(userWrapper.getNickName());
        textUserName.setText("登录名:" + userWrapper.getUsername());
        textPhone.setText(userWrapper.getMobilePhoneNumber());

        imgAvatar.setImageResource(R.drawable.duang);
        refreshAvatar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }


    @OnClick({R.id.btn_logout, R.id.panel_modify_password})
    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                userWrapper.logOut();
                ActivityStack.getInstance(this).restart();
                break;
            case R.id.panel_modify_password:
                startActivity(ResetPasswordActivity.class);
                break;
        }
    }

    ImageSelectManager imageSelectManager;


    @OnLongClick(R.id.img_avatar)
    boolean onAvatarChanged(View v) {
        if (imageSelectManager == null) {
            imageSelectManager = new ImageSelectManager(Environment.getExternalStorageDirectory().getAbsolutePath(), "temp.jpg");
        }
        Intent intent = imageSelectManager.getSelectIntent(1, 1, 300, 300);
        if (intent == null) {
            ToastTool.show(this, "无法建立缓存");
            return true;
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                startActivityForResult(intent, REQ_CODE_UNDER_KITKAT);
            } else {
                startActivityForResult(intent, REQ_CODE_KITKAT);
            }
        } catch (Exception ex) {
            ToastTool.show(this, "无法获取图片");
        }
        return true;
    }

    private final static int REQ_CODE_UNDER_KITKAT = 101;
    private final static int REQ_CODE_KITKAT = 102;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode != RESULT_OK) {
            ToastTool.show(this, "无法获取图片");
            return;
        }
        if (requestCode == REQ_CODE_UNDER_KITKAT) {
            if (data != null) {
                try {
                    Bitmap image = imageSelectManager.getBitmapFromUri(getContentResolver());
                    Log.i(TAG, "image x:" + image.getWidth() + ", y:" + image.getHeight());
                    imgAvatar.setImageBitmap(image);

                    final File tempFile = imageSelectManager.getOutputFile();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveAvatar(tempFile);
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastTool.show(this, "无法获取图片");
                }
            }
        } else if (requestCode == REQ_CODE_KITKAT) {
            String path = imageSelectManager.getImagePathFromUriForKitkat(this, data.getData());
            Intent intent = imageSelectManager.getCropImageIntent(Uri.fromFile(new File(path)), 1, 1, 300, 300);
            try {
                startActivityForResult(intent, REQ_CODE_UNDER_KITKAT);
            } catch (Exception ex) {
                ToastTool.show(this, "无法获取图片");
            }
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "refresh");
                    refreshAvatar();
                    break;
                case 2:
                    Log.i(TAG, "back");
                    imgAvatar.setImageResource(R.drawable.duang);
                    refreshAvatar();
                    break;
            }
        }
    };

    private void refreshAvatar() {
        UserWrapper userWrapper = UserWrapper.getCurrentUser();
        AVFile file = userWrapper.getAvUser().getAVFile("avatar");
        if (file != null) {
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, AVException e) {
                    Log.i(TAG, "file get!");
                    Bitmap bitmap = Avatar.getBitmap(bytes);
                    imgAvatar.setImageBitmap(bitmap);
                }
            });
        }
    }

    private void saveAvatar(File file) {
        try {
            AVFile avFile = AVFile.withFile(file.getName(), file);
            avFile.save();
            Log.i(TAG, "avFile saved");
            UserWrapper userWrapper = UserWrapper.getCurrentUser();
            userWrapper.getAvUser().put("avatar", avFile);
            userWrapper.save();
        } catch (IOException | AVException e) {
            e.printStackTrace();
            handler.obtainMessage(2).sendToTarget();
            return;
        }
        handler.obtainMessage(1).sendToTarget();
    }


}
