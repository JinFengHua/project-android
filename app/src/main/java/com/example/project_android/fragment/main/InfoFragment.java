package com.example.project_android.fragment.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project_android.R;
import com.example.project_android.adapter.CourseListAdapter;
import com.example.project_android.entity.CourseList;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.ui.companent.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class InfoFragment extends Fragment {
    @BindView(R.id.info_detail_account_text)
    TextView accountName;
    @BindView(R.id.info_detail_account)
    TextView account;
    @BindView(R.id.info_detail_class)
    TextView classText;
    @BindView(R.id.info_detail_name)
    TextView name;
    @BindView(R.id.info_detail_sex)
    TextView sex;
    @BindView(R.id.info_detail_phone)
    TextView phone;
    @BindView(R.id.info_detail_email)
    TextView email;
    @BindView(R.id.info_detail_face_layout)
    LinearLayout faceLayout;
    @BindView(R.id.info_detail_class_layout)
    LinearLayout classLayout;
    @BindView(R.id.info_detail_face_prompt)
    LinearLayout promptLayout;
    @BindView(R.id.info_detail_face)
    ImageView face;
    @BindView(R.id.info_detail_avatar)
    CircleImageView avatar;

    private SharedPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = getActivity().getSharedPreferences("localRecord", Context.MODE_PRIVATE);
        Picasso.with(getContext())
                .load(ProjectStatic.SERVICE_PATH + preferences.getString("avatar",""))
                .fit()
                .error(R.drawable.ic_net_error)
                .into(avatar);
        phone.setText(preferences.getString("phone",""));
        email.setText(preferences.getString("email",""));
        sex.setText(preferences.getBoolean("sex",false) ? "男" : "女");
        account.setText(preferences.getString("account",""));
        name.setText(preferences.getString("name",""));
        String type = preferences.getString("userType","");
        accountName.setText(type.equals("1") ? "工号" : "学号");
        if (type.equals("1")){
            accountName.setText("工号");
            faceLayout.setVisibility(View.GONE);
            classLayout.setVisibility(View.GONE);
        } else {
            accountName.setText("学号");
            classLayout.setVisibility(View.VISIBLE);
            classText.setText(preferences.getString("class",""));
            if (preferences.getString("face",null) != null) {
                promptLayout.setVisibility(View.GONE);
                Picasso.with(getContext())
                        .load(ProjectStatic.SERVICE_PATH + preferences.getString("face", ""))
                        .fit()
                        .error(R.drawable.ic_net_error)
                        .into(avatar);
            } else {
                promptLayout.setVisibility(View.VISIBLE);
            }
        }

    }

}