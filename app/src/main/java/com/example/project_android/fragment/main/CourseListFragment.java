package com.example.project_android.fragment.main;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.example.project_android.R;
import com.example.project_android.adapter.CourseListAdapter;
import com.example.project_android.dialog.CourseAddDialog;
import com.example.project_android.dialog.CourseCreateDialog;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;


@SuppressLint("NonConstantResourceId")
public class CourseListFragment extends Fragment{
    private static final int OPEN_ALBUM =151;
    private String data = "[{\"courseId\":1,\"teacherId\":1,\"courseName\":\"人工智能\",\"courseAvatar\":\"image/avatars/course-default.png\",\"courseIntroduce\":\"云计算云计算云计算云计算云计算云计算云计算云计算\",\"courseCode\":\"396973\",\"teacher\":{\"teacherId\":1,\"adminId\":1,\"teacherAccount\":\"000001\",\"teacherPassword\":\"000000\",\"teacherName\":\"张老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"avatars/user-default.png\",\"courses\":null}},{\"courseId\":3,\"teacherId\":2,\"courseName\":\"软件工程导论\",\"courseAvatar\":\"image/avatars/course-default.png\",\"courseIntroduce\":\"软件工程导论软件工程导论软件工程导论软件工程导论软件工程导论软件工程导论\",\"courseCode\":\"824695\",\"teacher\":{\"teacherId\":2,\"adminId\":2,\"teacherAccount\":\"000011\",\"teacherPassword\":\"000000\",\"teacherName\":\"李老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"avatars/user-default.png\",\"courses\":null}},{\"courseId\":4,\"teacherId\":2,\"courseName\":\"Linux操作系统\",\"courseAvatar\":\"image/avatars/course-default.png\",\"courseIntroduce\":\"Linux操作系统Linux操作系统Linux操作系统Linux操作系统Linux操作系统Linux操作系统Linux操作系统\",\"courseCode\":\"304971\",\"teacher\":{\"teacherId\":2,\"adminId\":2,\"teacherAccount\":\"000011\",\"teacherPassword\":\"000000\",\"teacherName\":\"李老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"avatars/user-default.png\",\"courses\":null}},{\"courseId\":5,\"teacherId\":3,\"courseName\":\"编译原理\",\"courseAvatar\":\"image/avatars/course-default.png\",\"courseIntroduce\":\"编译原理编译原理编译原理编译原理编译原理编译原理编译原理编译原理\",\"courseCode\":\"471439\",\"teacher\":{\"teacherId\":3,\"adminId\":2,\"teacherAccount\":\"000111\",\"teacherPassword\":\"000000\",\"teacherName\":\"王老师\",\"teacherSex\":false,\"teacherPhone\":\"13137749525\",\"teacherEmail\":\"2116161338@qq.com\",\"teacherAvatar\":\"avatars/user-default.png\",\"courses\":null}}]";

    @BindView(R.id.fragment_course_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.course_list_prompt)
    TextView promptText;

    private CourseListViewModel viewModel;
    private CourseCreateDialog createDialog;

    public String pictureDir;
    public String picturePath;
    private String userType;
    private String id;


    Unbinder unbinder;

    Handler courseListHandler = new Handler(msg -> {
        if (msg.what == 1){
            viewModel.updateCourses(msg.getData().getString("data"));
        }
        Toast.makeText(getActivity(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
        System.out.println(msg.getData().getString("message"));
        refreshLayout.setRefreshing(false);
        return false;
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        unbinder = ButterKnife.bind(this,view);
//        进行网络请求返回所有与用户相关的课程
        SharedPreferences preferences = getActivity().getSharedPreferences("localRecord", Context.MODE_PRIVATE);
        userType = preferences.getString("userType","2");
        id = preferences.getString("id","");
        Log.d("NET-->",String.valueOf(userType));

        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());

        Map<String, String> map = new HashMap<>();
        if (userType.equals("2")){
            promptText.setText("我的课程");
            map.put("student_id",id);
//            NetUtil.getNetData("course/findCourseByStudentId", map, courseListHandler);
            refreshLayout.setOnRefreshListener(() -> {
                refreshLayout.setRefreshing(false);
//                NetUtil.getNetData("course/findCourseByStudentId", map, courseListHandler);
            });
        } else {
            promptText.setText("我教的课");
//            map.put("teacher_id",preferences.getString("id",""));
            NetUtil.getNetData("course/findCourseByMap", map, courseListHandler);
            refreshLayout.setOnRefreshListener(() -> {
                refreshLayout.setRefreshing(false);
                NetUtil.getNetData("course/findCourseByMap", map, courseListHandler);
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        // TODO: Use the ViewModel
        viewModel.getCourseLists().observe(getViewLifecycleOwner(), courseLists -> ViewUtils.setRecycler(getActivity(), R.id.recycler_course_list, new CourseListAdapter(courseLists)));

        viewModel.updateCourses(data);
    }

    @OnClick(R.id.course_create_button)
    public void onClicked(View view){
        if (userType.equals("1")) {
            createDialog = new CourseCreateDialog(view.getContext());
            createDialog.setChooseClickListener(() -> {
                if (!PermissionUtils.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils permission = PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permission.request();
                } else {
                    openAlbum();
                }
            });
            pictureDir = PathUtils.getExternalAppPicturesPath();
            FileUtils.createOrExistsDir(pictureDir);
            picturePath = pictureDir + "/uploadTest.png";
            createDialog.show();
        } else {
            CourseAddDialog addDialog = new CourseAddDialog(view.getContext(),id);
            addDialog.show();
        }
    }

    public void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,OPEN_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case OPEN_ALBUM:
                if (resultCode == Activity.RESULT_OK){
                    File tempFile = new File(picturePath);
                    if (tempFile.exists()){
                        tempFile.delete();
                    }
                    Crop.of(data.getData(),Uri.fromFile(tempFile)).asSquare().withAspect(500,500).start(getContext(),this);
                }
                break;
            case Crop.REQUEST_CROP:
                File tempFile = new File(picturePath);
                try {
                    sendImage(tempFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public void sendImage(File file) throws FileNotFoundException {
        AlertDialog waitDialog = new AlertDialog.Builder(this.getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setMessage("文件正在上传，请等待")
                .setCancelable(false)
                .create();
        waitDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("photo",file);
        params.put("dir","avatars");
        client.post(ProjectStatic.SERVICE_PATH + "saveImage", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                waitDialog.dismiss();
                AlertDialog dialog = initResultDialog("文件上传成功");
                JSONObject object = JSON.parseObject(new String(responseBody));
                createDialog.setImgPath(object.getString("message"));
                createDialog.previewImage.setImageBitmap(ImageUtils.getBitmap(new File(picturePath)));
                createDialog.imageState.setTextColor(ColorUtils.getColor(R.color.green));
                createDialog.imageState.setText("选取成功");
                dialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                waitDialog.dismiss();
                AlertDialog dialog = initResultDialog("文件上传失败");
                createDialog.previewImage.setImageBitmap(ImageUtils.getBitmap(R.drawable.ic_net_error));
                dialog.show();
            }
        });
    }

    private AlertDialog initResultDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("创建课程")
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.setCancelable(true);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}