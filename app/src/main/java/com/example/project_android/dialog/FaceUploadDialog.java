package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.project_android.R;
import com.example.project_android.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class FaceUploadDialog extends Dialog {
    @BindView(R.id.face_preview)
    public ImageView preview;

    private String picturePath;

    private onNoClickedListener noClickedListener;
    private onYesClickedListener yesClickedListener;
    private onSelectedClickedListener selectedClickedListener;

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setNoClickedListener(onNoClickedListener noClickedListener) {
        this.noClickedListener = noClickedListener;
    }

    public void setYesClickedListener(onYesClickedListener yesClickedListener) {
        this.yesClickedListener = yesClickedListener;
    }

    public void setSelectedClickedListener(onSelectedClickedListener selectedClickedListener) {
        this.selectedClickedListener = selectedClickedListener;
    }

    public FaceUploadDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_face_upload, null);
        ButterKnife.bind(this,view);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }

    public void invisibleButton(){
        findViewById(R.id.face_collection).setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.yes,R.id.no,R.id.face_collection})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.yes:
                yesClickedListener.onYesClicked(view);
                break;
            case R.id.no:
                noClickedListener.onNoClicked(view);
                break;
            case R.id.face_collection:
                selectedClickedListener.onSelectedClicked(view);
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.show(getWindow());
    }

    public interface onNoClickedListener{
        void onNoClicked(View view);
    }

    public interface onYesClickedListener{
        void onYesClicked(View view);
    }

    public interface onSelectedClickedListener{
        void onSelectedClicked(View view);
    }

}
