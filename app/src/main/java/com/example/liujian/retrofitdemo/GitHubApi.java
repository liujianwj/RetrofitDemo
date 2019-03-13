package com.example.liujian.retrofitdemo;

import android.content.res.TypedArray;
import android.util.TypedValue;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by liujian on 2017/7/24.
 */

public interface GitHubApi {
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: RetrofitBean-Sample-App",
            "name:ljd"
    })
    @GET("repos/{owner}/{repo}/contributors")
    Call<ResponseBody> contributorsBySimpleGetCall(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributorsBySimpleGetCall1(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributorsByRxJava(@Path("owner") String owner, @Path("repo") String repo);

    @FormUrlEncoded
    @POST("contributor/edit")
    Call<Contributor> updateContributor(@Field("name") String name);

    @FormUrlEncoded
    @POST("contributor/edit")
    Call<Contributor> updateContributor1(@FieldMap Map<String, String> fieldMap);

    /**
     * 上传文件
     * @param description
     * @param file
     * @return
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadContributor(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    /**
     * 上传一张图片
     * @param file
     * @return
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(
            @Part() RequestBody file);

    /**
     * 上传多张图片
     * @param url
     * @param maps
     * @return
     */
    @Multipart
    @POST()
    Call<ResponseBody> uploadFiles(
            @Url String url,
            @PartMap() Map<String, RequestBody> maps);

    /**
     * 图文上传
     * @param url
     * @param partMap
     * @param file
     * @return
     */
    @Multipart
    @POST
    Call<ResponseBody> uploadFileWithPartMap(
            @Url() String url,
            @PartMap() Map<String, RequestBody> partMap,
            @Part  MultipartBody.Part file);

}
