package com.example.project_android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.project_android.R;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ShowImageDialog extends Dialog {
    @BindView(R.id.image)
    ImageView imageView;

    public ShowImageDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Msg);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_image, null);
        ButterKnife.bind(this,view);
        setContentView(view);

        setViewHeightByWidth();
    }

    @OnClick({R.id.image, R.id.image_layout})
    public void onClicked(){
        dismiss();
    }

    public void setImage(String path){
        Picasso.with(getContext())
                .load(path)
                .fit()
                .error(R.drawable.ic_net_error)
                .into(imageView);
    }

    public void setViewHeightByWidth() {
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

				int width = imageView.getMeasuredWidth();

                android.view.ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.height = width;
                imageView.setLayoutParams(lp);

                final ViewTreeObserver vto1 = imageView.getViewTreeObserver();
                vto1.removeOnPreDrawListener(this);

                return true;
            }
        };
        vto.addOnPreDrawListener(preDrawListener);
    }

    @Override
    public void show() {
        super.show();
        ViewUtils.show(getWindow());
    }
}
