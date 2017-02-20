package com.stc.chviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by artem on 2/20/17.
 */
@Module
final class AppModule {

    private final String mBaseUrl;

    private  final  ChApp mApplication;

    AppModule(ChApp application, String baseUrl) {
        mApplication = application;
        mBaseUrl=baseUrl;
    }

    @Provides
    @Singleton
    public Context providesAppContext() {
        return mApplication;
    }


    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }


    @Provides
    @Singleton
    public ChRetroHelper provideChRetroHelper() {
        return new ChRetroHelper(new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build());
    }
}
