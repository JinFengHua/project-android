package com.example.project_android.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.project_android.R;
import com.example.project_android.dialog.ConfirmDialog;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.util.CommenUtil;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.smssdk.ui.companent.CircleImageView;
import cz.msebera.android.httpclient.Header;

@SuppressLint("NonConstantResourceId")
public class ModifyInfo extends AppCompatActivity {
    @BindView(R.id.info_detail_class)
    EditText classText;
    @BindView(R.id.info_detail_name)
    EditText name;
    @BindView(R.id.info_detail_phone)
    EditText phone;
    @BindView(R.id.info_detail_email)
    EditText email;
    
    @BindView(R.id.face_layout)
    LinearLayout faceLayout;
    @BindView(R.id.info_detail_class_layout)
    LinearLayout classLayout;
    @BindView(R.id.info_detail_face)
    ImageView face;
    @BindView(R.id.info_detail_avatar)
    CircleImageView avatar;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public String pictureDir;
    public String albumPath;
    public String cameraPath;
    private String type;
    private String id;
    private String oldPhone;

    private String newName;
    private boolean sex;
    private String newPhone;
    private String newEmail;
    private String newClass;
    private Map<String, String> map = new HashMap<>();
    private LoadingDialog dialog;

    private boolean isCamera;
//    判断哪个图像更改了
    private boolean avatarChanged = false;
    private boolean faceChanged = false;

//    记录当前图像上传状态
    private boolean avatarFinish = false;
    private boolean faceFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        ButterKnife.bind(this);
        ViewUtils.initActionBar(this,"修改信息");

        preferences = getSharedPreferences("localRecord",MODE_PRIVATE);
        type = preferences.getString("userType", "");
        id = preferences.getString("id","");
        oldPhone = preferences.getString("phone","");

        pictureDir = PathUtils.getExternalAppPicturesPath();
        FileUtils.createOrExistsDir(pictureDir);
        albumPath = pictureDir + "/album.png";
        cameraPath = pictureDir + "/camera.png";

        CommenUtil.initPhotoError();

        dialog = new LoadingDialog(this);

        initView();
    }

    private void initView() {
        Picasso.with(this)
                .load(ProjectStatic.SERVICE_PATH + preferences.getString("avatar",""))
                .fit()
                .error(R.drawable.ic_net_error)
                .into(avatar);

        sex = preferences.getBoolean("sex",true);
        int id = sex ? R.id.male : R.id.female;
        name.setText(preferences.getString("name",""));
        findViewById(id).performClick();
        phone.setText(preferences.getString("phone",""));
        email.setText(preferences.getString("email",""));

        if (type.equals("2")){
            classLayout.setVisibility(View.VISIBLE);
            classText.setText(preferences.getString("class",""));

            faceLayout.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(ProjectStatic.SERVICE_PATH + preferences.getString("face",""))
                    .fit()
                    .error(R.drawable.ic_net_error)
                    .into(face);
        }
    }

    @OnClick({R.id.info_detail_avatar, R.id.info_detail_face, R.id.info_detail_modify})
    public void onClicked(View view){
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle("提示");
        switch (view.getId()){
            case R.id.info_detail_avatar:
                loadingDialog.setMessage("是否更改头像");
                loadingDialog.setOnYesClickedListener(view1 -> {
                    openAlbum();
                    loadingDialog.dismiss();
                });
                loadingDialog.show();
                break;
            case R.id.info_detail_face:
                loadingDialog.setMessage("是否更改人脸信息");
                loadingDialog.setOnYesClickedListener(view1 -> {
                    openCamera();
                    loadingDialog.dismiss();
                });
                loadingDialog.show();
                break;
            case R.id.info_detail_modify:
                dialog.setTitle("开始修改");
                dialog.setMessage(StringUtils.getString(R.string.wait_message));
                commitModify();
                break;
        }
    }

    public void openAlbum(){
        isCamera = false;
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,ProjectStatic.OPEN_ALBUM);
    }

    public void openCamera(){
        isCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraPath)));
        startActivityForResult(intent, ProjectStatic.OPEN_CAMERA);
    }

    private void commitModify() {
        newName = name.getText().toString();
        newPhone = phone.getText().toString();
        newEmail = email.getText().toString();

        if ( newName.length() < 1 ){
            Toast.makeText(this, "姓名不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( !CommenUtil.isPhone(newPhone) ){
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( !CommenUtil.IsEmail(newEmail) ){
            Toast.makeText(this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equals("2")){
            newClass = classText.getText().toString();
            if (newClass.length() < 7){
                Toast.makeText(this, "班级格式错误", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (faceChanged){
            long time = System.currentTimeMillis() - preferences.getLong("modifyTime", 0);
            if (time < 1000*3600*24){
                Toast.makeText(this, "修改人脸信息过于频繁", Toast.LENGTH_SHORT).show();
            }
        }

        map.put("name",newName);
        map.put("sex",sex ? "true" : "false");
        map.put("phone",newPhone);
        map.put("email",newEmail);

        if (!newPhone.equals(oldPhone)){
//            手机号不同，需要验证
            ConfirmDialog confirmDialog = new ConfirmDialog(this,newPhone);
            confirmDialog.setConfirmSuccessListener(() -> {
                confirmDialog.dismiss();
                dialog.show();
                if (avatarChanged) {
                    sendImage(new File(albumPath),Integer.parseInt(type),id);
                }
                if (faceChanged) {
                    sendImage(new File(cameraPath), 4, id);
                }
            });
            confirmDialog.show();
        } else {
            dialog.show();
            if (avatarChanged) {
                sendImage(new File(albumPath),Integer.parseInt(type),id);
            }
            if (faceChanged) {
                sendImage(new File(cameraPath), 4, id);
            }
            if ( !(avatarChanged || faceChanged)){
                if (type.equals("1")) {
                    map.put("teacherId", id);
                    NetUtil.getNetData("account/modifyTeacher",map,handler);
                } else {
                    map.put("studentId", id);
                    NetUtil.getNetData("account/modifyStudent",map,handler);
                }

            }
        }

    }

//    监听真正的修改
    Handler handler = new Handler(msg -> {
        dialog.showSingleButton();
        dialog.setMessage(msg.getData().getString("message"));
        if (msg.what == 1){
            dialog.setOnDismissListener(dialog1 -> {
                finish();
            });
        }
        return false;
    });

    Handler uploadHandler = new Handler(msg -> {
        String img = (String) msg.obj;
        switch (msg.what){
//            上传教师头像
            case 1:
                map.put("teacherId",id);
                map.put("avatar",img);
                NetUtil.getNetData("account/modifyTeacher",map,handler);
                break;
//            上传学生头像
            case 2:
                avatarFinish = true;
                map.put("major",newClass);
                map.put("studentId",id);
                map.put("avatar",img);
                if (faceChanged){
                    if (faceFinish){
//                        执行逻辑
                        NetUtil.getNetData("account/modifyStudent",map,handler);
                    }
                } else {
//                    开始上传
                    NetUtil.getNetData("account/modifyStudent",map,handler);
                }
                break;
//            上传人脸信息
            case 4:
                faceFinish = true;
                map.put("face",img);
                editor.putLong("modifyTime",System.currentTimeMillis());
                editor.apply();
                if (avatarChanged){
                    if (avatarFinish){
                        NetUtil.getNetData("account/modifyStudent",map,handler);
                    }
                } else {
//                    开始上传
                    NetUtil.getNetData("account/modifyStudent",map,handler);
                }
                break;
        }
        return false;
    });

    public void sendImage(File file, Integer type, String id){
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("photo", file);
            params.put("type", type);
            params.put("id",id);
            client.post(ProjectStatic.SERVICE_PATH + "document/saveImage", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    JSONObject object = JSON.parseObject(new String(responseBody));
                    String imgPath = object.getString("message");
                    Message message = new Message();
                    message.what = type;
                    message.obj = imgPath;
                    uploadHandler.sendMessage(message);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    dialog.setMessage("文件上传失败，请重试");
                    dialog.showSingleButton();
                }

            });
        } catch (FileNotFoundException e){
            Toast.makeText(this, "未找到文件", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ProjectStatic.OPEN_ALBUM:
                if (resultCode == RESULT_OK){
                    File tempFile = new File(albumPath);
                    Crop.of(data.getData(),Uri.fromFile(tempFile)).asSquare().withAspect(500,500).start(this);
                }
                break;
            case ProjectStatic.OPEN_CAMERA:
                if (resultCode == RESULT_OK) {
                    File tempFile = new File(cameraPath);
                    Crop.of(Uri.fromFile(tempFile),Uri.fromFile(tempFile)).asSquare().withAspect(500,500).start(this);
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    if (isCamera) {
                        faceChanged = true;
                        face.setImageBitmap(ImageUtils.getBitmap(cameraPath));
                    } else {
                        avatarChanged = true;
                        avatar.setImageBitmap(ImageUtils.getBitmap(albumPath));
                    }
                }
                break;

        }
    }

    @OnCheckedChanged({R.id.male, R.id.female})
    public void onCheckChanged(CompoundButton view, boolean isChanged){
        switch (view.getId()){
            case R.id.male:
                if (isChanged){
                    sex = true;
                }
                break;
            case R.id.female:
                if (isChanged){
                    sex = false;
                }
                break;
        }
    }
}