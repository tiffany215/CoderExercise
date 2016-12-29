package com.example.tonitagen.okhttp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private final static String mBaseUrl = "http://192.168.1.104:8080/OkHttpDemo/";
    private TextView ResponseResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();

        ResponseResult = (TextView) findViewById(R.id.id_tv_result);

    }


    /**
     * get请求
     *
     * @param view
     */
    public void doGetRequest(View view) {

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "login?userName=wangxu&passWord=123").build();

        executeRequest(request);

    }


    /**
     * post请求
     *
     * @param view
     */
    public void doPostRequest(View view) {
        FormBody.Builder body = new FormBody.Builder();
        body.add("userName", "tiffany");
        body.add("passWord", "123446");
        FormBody formBody = body.build();

        Request.Builder builder1 = new Request.Builder();
        Request request = builder1.url(mBaseUrl + "doPostRequest").post(formBody).build();
        executeRequest(request);
    }


    /**
     * 向服务器发送字符串
     *
     * @param view
     */
    public void doPostString(View view) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), "{'userName':'tiffany','passWold':'123456'}");

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "requestString").post(requestBody).build();

        executeRequest(request);


    }


    /**
     * 向服务器发送文件。
     * @param view
     */
    public void doPostFile(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "temp_head_image.jpg");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "requestString").post(requestBody).build();

        executeRequest(request);

    }



    public void uploadInfo(View view){

        File file = new File(Environment.getExternalStorageDirectory(), "temp_head_image.jpg");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Builder multBuild = new MultipartBody.Builder();
        multBuild.addFormDataPart("userName","tiffany");
        multBuild.addFormDataPart("password","123456");
        RequestBody requestBody = multBuild.addFormDataPart("mPhoto","banner.jpg",RequestBody.create(MediaType.parse("application/octet-stream"),file)).build();


        Request.Builder builder = new Request.Builder();
        Request request =  builder.url(mBaseUrl + "/uploadInfo").post(requestBody).build();

        executeRequest(request);

    }


    /**
     * 执行请求
     *
     * @param request
     */
    private void executeRequest(Request request) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ResponseResult.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


}
