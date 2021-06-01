package com.example.project_android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_android.R;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ShowFaceDialog extends Dialog {
    @BindView(R.id.title) TextView titleText;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.no)
    Button no;

    private String name;
    private File file;
    private String location;

    private onRecordSuccess recordSuccess;

    public void setRecordSuccess(onRecordSuccess recordSuccess) {
        this.recordSuccess = recordSuccess;
    }

    public ShowFaceDialog(@NonNull Context context, String path, String name, String location) {
        super(context,R.style.Dialog_Msg);
        this.name = name;
        this.location = location;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_show_face, null);
        ButterKnife.bind(this,inflate);
        setContentView(inflate);

        file = new File(path);
        titleText.setText("开始签到");
        if (!file.exists()){
            no.performClick();
        }
        imageView.setImageURI(Uri.fromFile(file));
    }

    @OnClick({R.id.yes,R.id.no})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.yes:
                if (!file.exists()){
                    Toast.makeText(view.getContext(), "未选择图片", Toast.LENGTH_SHORT).show();
                    break;
                }
                try {
                    sendFace(file);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                break;
            case R.id.no:
                if (file.exists()) {
                    file.delete();
                }
                dismiss();
                break;
        }
    }

    public void sendFace(File file) throws FileNotFoundException {
        LoadingDialog waitDialog = new LoadingDialog(getContext());
        waitDialog.setTitle("正在签到");
        waitDialog.setOnDismissListener(dialog -> dismiss());
        waitDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("photo",file);
        params.put("type","5");
        params.put("id",name);
        client.post(ProjectStatic.SERVICE_PATH + "document/saveImage", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String studentId = name.substring(0,name.indexOf("_"));
                String attendId = name.substring(name.indexOf("_") + 1);
                Map<String, String> map = new HashMap<>();
                map.put("studentId",studentId);
                map.put("attendId",attendId);
                map.put("time",new Timestamp(System.currentTimeMillis()).toString());
                map.put("location",location);
                NetUtil.getNetData("record/doRecord",map, 120000,new Handler(msg -> {
                    waitDialog.showSingleButton();
                    waitDialog.setMessage(msg.getData().getString("message"));
                    if(msg.what == 1){
                        waitDialog.setOnDismissListener(dialog -> {
                            dismiss();
                            recordSuccess.closeActivity();
                        });
                    }
                    return false;
                }));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                waitDialog.setMessage("图片上传失败，请重试");
                waitDialog.showSingleButton();
            }
        });
    }

    public interface onRecordSuccess{
        void closeActivity();
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.show(getWindow());
    }
}
