package com.example.liujian.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.liujian.retrofitdemo.proxy.DynamicProxy;
import com.example.liujian.retrofitdemo.proxy.RealSubject;
import com.example.liujian.retrofitdemo.proxy.Subject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * retrofit
     * @param view
     */
    public void getData1(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
        GitHubApi repo = retrofit.create(GitHubApi.class);
        Call<ResponseBody> call = repo.contributorsBySimpleGetCall("square", "retrofit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    ArrayList<Contributor> contributorArrayList = gson.fromJson(response.body().string(), new TypeToken<List<Contributor>>(){}.getType());
                    for(Contributor contributor : contributorArrayList){
                        Log.d("login",contributor.getLogin());
                        Log.d("contributions",contributor.getContributions());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * retrofit javaBean转换
     * @param view
     */
    public void getData2(View view){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        GitHubApi repo = retrofit.create(GitHubApi.class);
        Call<List<Contributor>> call = repo.contributorsBySimpleGetCall1("square", "retrofit");
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                List<Contributor> contributors = response.body();
                for(Contributor contributor : contributors){
                    Log.d("login",contributor.getLogin());
                    Log.d("contributions",contributor.getContributions());
                }
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });
    }

    /**
     * retrofit 日志
     * @param view
     */
    public void getData3(View view){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        GitHubApi repo = retrofit.create(GitHubApi.class);
        Call<List<Contributor>> call = repo.contributorsBySimpleGetCall1("square", "retrofit");
        call.cancel();
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                List<Contributor> contributors = response.body();
                for(Contributor contributor : contributors){
                    Log.d("login",contributor.getLogin());
                    Log.d("contributions",contributor.getContributions());
                }
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });
    }

    /**
     * retrofit + rxjava + okhttp
     * @param view
     */
    public void getData4(View view){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        GitHubApi repo = retrofit.create(GitHubApi.class);

        CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(repo.contributorsByRxJava("square", "retrofit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contributor>>() {
            @Override
            public void onCompleted() {
                Log.d("TAG", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Contributor> contributors) {
                for (Contributor c : contributors) {
                    Log.d("TAG", "login:" + c.getLogin() + "  contributions:" + c.getContributions());
                }
            }
        }));

    }

    public void upload(){
        File file = new File("filePath");
        String token ="dsdsddadad244";
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody tokenBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), token);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", tokenBody);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
        GitHubApi repo = retrofit.create(GitHubApi.class);
        Call<ResponseBody> call = repo.uploadFileWithPartMap("url/", map, filePart);
        // 执行
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void getData(View view){
        Retrofit retrofit = AppClient.newInstance().buildRetrofit();
        GitHubApi repo = retrofit.create(GitHubApi.class);

        CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(repo.contributorsByRxJava("square", "retrofit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contributor>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TAG", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Contributor> contributors) {
                        for (Contributor c : contributors) {
                            Log.d("TAG", "login:" + c.getLogin() + "  contributions:" + c.getContributions());
                        }
                    }
                }));

    }


    /**
     * 动态代理test
     * @param view
     */
    public void proxyTest(View view){
        Subject realSubject = new RealSubject();
        Subject subject = (Subject) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(),
                realSubject.getClass().getInterfaces(),
                new DynamicProxy(realSubject));
        subject.rent();
    }
}
