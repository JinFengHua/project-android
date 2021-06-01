package com.example.project_android.fragment.main;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.project_android.activity.login.LoginActivity;
import com.example.project_android.adapter.CourseListAdapter;
import com.example.project_android.dialog.CourseAddDialog;
import com.example.project_android.dialog.CourseCreateDialog;
import com.example.project_android.dialog.FaceUploadDialog;
import com.example.project_android.dialog.LoadingDialog;
import com.example.project_android.util.CommenUtil;
import com.example.project_android.util.NetUtil;
import com.example.project_android.util.ProjectStatic;
import com.example.project_android.util.ViewUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;


@SuppressLint("NonConstantResourceId")
public class CourseListFragment extends Fragment{
    @BindView(R.id.fragment_course_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.course_list_prompt)
    TextView promptText;
    @BindView(R.id.search)
    EditText searchEdit;
    @BindView(R.id.content_not_found_layout)
    LinearLayout notFoundLayout;

    private CourseListViewModel viewModel;
    private CourseCreateDialog createDialog;
    private FaceUploadDialog faceUploadDialog;

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

//        创建存储图片的临时文件地址
        pictureDir = PathUtils.getExternalAppPicturesPath();
        FileUtils.createOrExistsDir(pictureDir);
        picturePath = pictureDir + "/temp.png";

        CommenUtil.initPhotoError();

//        进行网络请求返回所有与用户相关的课程
        SharedPreferences preferences = getActivity().getSharedPreferences("localRecord", Context.MODE_PRIVATE);
        userType = preferences.getString("userType","");
        id = preferences.getString("id","");
        if (userType.equals("2")) {
            String face = preferences.getString("face", "");
            uploadFace(face);
        }

        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshColor());

        Map<String, String> map = new HashMap<>();
        if (userType.equals("2")){
            promptText.setText("我的课程");
            map.put("studentId",id);
            NetUtil.getNetData("course/findCourseByStudentId", map, courseListHandler);

            refreshLayout.setOnRefreshListener(() -> {
                refreshLayout.setRefreshing(false);
                NetUtil.getNetData("course/findCourseByStudentId", map, courseListHandler);
            });

            searchEdit.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    String name = searchEdit.getText().toString();
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("studentId",id);
                    map1.put("name",name);
                    NetUtil.getNetData("course/findCourseByStudentIdWithName", map1, courseListHandler);
                }
                return false;
            });
        } else {
            promptText.setText("我教的课");
            map.put("teacherId",preferences.getString("id",""));
            NetUtil.getNetData("course/findCourseByTeacherId", map, courseListHandler);

            refreshLayout.setOnRefreshListener(() -> {
                refreshLayout.setRefreshing(false);
                NetUtil.getNetData("course/findCourseByTeacherId", map, courseListHandler);
            });

            searchEdit.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    String name = searchEdit.getText().toString();
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("teacherId",id);
                    map1.put("name",name);
                    NetUtil.getNetData("course/findCourseByTeacherIdWithName", map1, courseListHandler);
                }
                return false;
            });
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        // TODO: Use the ViewModel
        viewModel.getCourseLists().observe(getViewLifecycleOwner(), courseLists -> {
            if (courseLists.size() < 1){
                notFoundLayout.setVisibility(View.VISIBLE);
            } else {
                notFoundLayout.setVisibility(View.GONE);
                ViewUtils.setRecycler(getActivity(), R.id.recycler_course_list, new CourseListAdapter(courseLists));
            }
        });
    }

    public void uploadFace(String face){
        if (face.equals("")){
            faceUploadDialog = new FaceUploadDialog(getContext());
            faceUploadDialog.setNoClickedListener(view -> {
                faceUploadDialog.dismiss();
                LoadingDialog loadingDialog = new LoadingDialog(getContext());
                loadingDialog.setTitle("警告");
                loadingDialog.setMessage("未注册人脸信息，将返回登录");
                loadingDialog.showSingleButton();
                loadingDialog.setOnDismissListener(dialog1 -> {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                });
                loadingDialog.show();
            });

            faceUploadDialog.setSelectedClickedListener(view -> {
                /**
                 * 跳转到相机界面，在拍完照之后跳转到剪切界面剪切完成后返回到dialog并将照片显示在预览中
                 */
                openCamera();
            });

            faceUploadDialog.setYesClickedListener(view -> {
                //执行上传操作
                if (faceUploadDialog.getPicturePath() == null){
                    Toast.makeText(view.getContext(), "未选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    sendFace(new File(picturePath));
                } catch (FileNotFoundException e){
                    Toast.makeText(view.getContext(), "未找到文件", Toast.LENGTH_SHORT).show();
                }
            });
            faceUploadDialog.show();
        }
    }

    @OnClick(R.id.course_create_button)
    public void onClicked(View view){
        if (userType.equals("1")) {
            createDialog = new CourseCreateDialog(view.getContext());
            createDialog.setChooseClickListener(this::openAlbum);

            createDialog.show();
        } else {
            CourseAddDialog addDialog = new CourseAddDialog(view.getContext(),id);
            addDialog.show();
        }
    }

    public void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,ProjectStatic.OPEN_ALBUM);
    }

    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picturePath)));
        startActivityForResult(intent, ProjectStatic.OPEN_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File tempFile = new File(picturePath);
        switch (requestCode){
            case ProjectStatic.OPEN_ALBUM:
                if (resultCode == RESULT_OK){
                    /*if (tempFile.exists()){
                        tempFile.delete();
                    }*/
                    Crop.of(data.getData(),Uri.fromFile(tempFile)).asSquare().withAspect(500,500).start(getContext(),this);
                }
                break;
            case ProjectStatic.OPEN_CAMERA:
                if (resultCode == RESULT_OK) {
                    Crop.of(Uri.fromFile(tempFile),Uri.fromFile(tempFile)).asSquare().withAspect(500,500).start(getContext(),this);
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    try {
                        if (userType.equals("1")) {
                            sendImage(tempFile);
                        } else {
                            faceUploadDialog.preview.setImageURI(Uri.fromFile(tempFile));
                            faceUploadDialog.setPicturePath(picturePath);
                            faceUploadDialog.invisibleButton();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    public void sendFace(File file) throws FileNotFoundException {
        LoadingDialog waitDialog = new LoadingDialog(getContext());
        waitDialog.setTitle("上传人脸信息");
        waitDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("photo",file);
        params.put("type","4");
        params.put("id",String.valueOf(id));
        client.post(ProjectStatic.SERVICE_PATH + "document/saveImage", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject object = JSON.parseObject(new String(responseBody));
                String face = object.getString("message");
                Map<String, String> map = new HashMap<>();
                map.put("face",face);
                map.put("studentId",id);
                NetUtil.getNetData("account/modifyStudent",map,new Handler(msg -> {
                    waitDialog.showSingleButton();
                    if (msg.what == 1){
                        waitDialog.setMessage("人脸信息修改成功");
                        getActivity().getSharedPreferences("localRecord",Context.MODE_PRIVATE).edit().putString("face",face).apply();
                        faceUploadDialog.dismiss();
                    } else {
                        waitDialog.setMessage(msg.getData().getString("message"));
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

    public void sendImage(File file) throws FileNotFoundException {
        AlertDialog waitDialog = new AlertDialog.Builder(this.getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setMessage("文件正在上传，请等待")
                .setCancelable(false)
                .create();
        waitDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("photo",file);
        params.put("type","3");
        params.put("id","temp");
        client.post(ProjectStatic.SERVICE_PATH + "document/saveImage", params, new AsyncHttpResponseHandler() {
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
                createDialog.imageState.setTextColor(ColorUtils.getColor(R.color.cancel_red));
                createDialog.imageState.setText("上传失败");
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