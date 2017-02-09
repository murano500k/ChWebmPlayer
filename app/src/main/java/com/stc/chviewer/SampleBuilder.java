/*
package com.stc.chviewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

*/
/**
 * Created by artem on 2/8/17.
 *//*


public class SampleBuilder {
    private static final String BASE_URL= "https://arhivach.org";
    private static final String KEY_URL= "KEY_URL";
    private static final String KEY_NAME= "KEY_NAME";
    private static final String BASE_LIST_TRHREADS_URL= "https://arhivach.org/index";
    private static final String CH_TAG = "1526";
    private static final int MAX_COUNT = 5;
    Subscriber<Integer> subscriber;



    private boolean sawError;

    private int count;
    Thread t;
    List<SampleGroup> list;
    Observable<Boolean> observable;
    Observer<Integer> progressObserver;
    private Observer<List<SampleGroup>> listObserver;


    public void start() {
        Observable.fromCallable(new Callable<List<SampleGroup>>() {

            @Override
            public List<SampleGroup> call() {
                return loadStatistics();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listObserver);

    }

    private List<SampleGroup> loadStatistics() {
        List<SampleGroup> result = new ArrayList<>();
        SampleGroup sGroup=new SampleGroup("threads");
        sGroup.samples=new ArrayList<>();
        ArrayList<HashMap<String ,String>>  threads = createThreadUrlList(CH_TAG);
        count=threads.size();

        if(count==0) return result;
        else if(count>MAX_COUNT) count=MAX_COUNT;
        Log.d(TAG, "doInBackground: count="+count);

        int i = 0;
        for(HashMap<String,String> thread: threads){
            if(i>count)break;
            ArrayList<String> urlList=createWebmUrlList(thread.get(KEY_URL));
            if(urlList!=null){
                sGroup.samples.add(i, createThreadPlaylist(thread.get(KEY_NAME), urlList));
                i++;
                int val=i * 100/count;
                Log.d(TAG, "onProgressUpdate: i="+i+" val="+val);
                Observable.just(val)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(progressObserver);
            }
        }
        result.add(0, sGroup);
        return result;
    }

    public PlaylistSample createThreadPlaylist(String title, ArrayList<String> uris){
        UriSample[] uriSamples=new UriSample[uris.size()];
        for(int i=0; i<uris.size(); i++){
            UriSample sample = new UriSample(uris.get(i),uris.get(i), "mpd");
            uriSamples[i]=sample;
        }
        return new PlaylistSample(title, uriSamples);
    }
    public ArrayList<HashMap<String ,String>> createThreadUrlList(String tag) {
        ArrayList<HashMap<String ,String>> uris=new ArrayList<HashMap<String ,String>>();
        HashMap<String, String> object =new HashMap<String, String>();
        object.put(KEY_NAME , "name");
        object.put(KEY_URL , "url");
        uris.add(object);
        return uris;
    }



    public ArrayList<String> createWebmUrlList(String testurl){
        ArrayList<String> uris=new ArrayList<>();
        boolean hasVideos=false;
        if(hasVideos) return uris;
        else return null;
    }

    public static class SampleGroup {

        public String title;
        public UriSample[] samples;


        public SampleGroup(String title, UriSample[] samples) {
            this.title = title;
            this.samples = samples;
        }

        public Intent buildIntent(Context context) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra(PlayerActivity.PREFER_EXTENSION_DECODERS, preferExtensionDecoders);
            if (drmSchemeUuid != null) {
                intent.putExtra(PlayerActivity.DRM_SCHEME_UUID_EXTRA, drmSchemeUuid.toString());
                intent.putExtra(PlayerActivity.DRM_LICENSE_URL, drmLicenseUrl);
                intent.putExtra(PlayerActivity.DRM_KEY_REQUEST_PROPERTIES, drmKeyRequestProperties);
            }
            return intent;
        }
    }

    public abstract static class Sample {

        public final String name;
        public final boolean preferExtensionDecoders;
        public final UUID drmSchemeUuid;
        public final String drmLicenseUrl;
        public final String[] drmKeyRequestProperties;

        public Sample(String name, UUID drmSchemeUuid, String drmLicenseUrl,
                      String[] drmKeyRequestProperties, boolean preferExtensionDecoders) {
            this.name = name;
            this.drmSchemeUuid = drmSchemeUuid;
            this.drmLicenseUrl = drmLicenseUrl;
            this.drmKeyRequestProperties = drmKeyRequestProperties;
            this.preferExtensionDecoders = preferExtensionDecoders;
        }
        public Intent buildIntent(Context context) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra(PlayerActivity.PREFER_EXTENSION_DECODERS, preferExtensionDecoders);
            if (drmSchemeUuid != null) {
                intent.putExtra(PlayerActivity.DRM_SCHEME_UUID_EXTRA, drmSchemeUuid.toString());
                intent.putExtra(PlayerActivity.DRM_LICENSE_URL, drmLicenseUrl);
                intent.putExtra(PlayerActivity.DRM_KEY_REQUEST_PROPERTIES, drmKeyRequestProperties);
            }
            return intent;
        }

    }

    public  static final class UriSample extends Sample {

        public final String uri;
        public final String extension;

        public UriSample(String name, UUID drmSchemeUuid, String drmLicenseUrl,
                         String[] drmKeyRequestProperties, boolean preferExtensionDecoders, String uri,
                         String extension) {
            super(name, drmSchemeUuid, drmLicenseUrl, drmKeyRequestProperties, preferExtensionDecoders);
            this.uri = uri;
            this.extension = extension;
        }
        public UriSample(String name, String uri,
                         String extension) {
            super(name, null, null, null, true);
            this.uri = uri;
            this.extension = extension;
        }

        @Override
        public Intent buildIntent(Context context) {
            return super.buildIntent(context)
                    .setData(Uri.parse(uri))
                    .putExtra(PlayerActivity.EXTENSION_EXTRA, extension)
                    .setAction(PlayerActivity.ACTION_VIEW);
        }

    }

    public  static final class PlaylistSampleGroup extends SampleGroup {

        public final UriSample[] children;
        public final String name;

        public PlaylistSampleGroup(String name,
                              UriSample[]  children) {
            this.name=name;
            this.children=children;
        }

        @Override
        public Intent buildIntent(Context context) {
            String[] uris = new String[children.length];
            String[] extensions = new String[children.length];
            for (int i = 0; i < children.length; i++) {
                uris[i] = children[i].uri;
                extensions[i] = children[i].extension;
            }
            return super.buildIntent(context)
                    .putExtra(PlayerActivity.URI_LIST_EXTRA, uris)
                    .putExtra(PlayerActivity.EXTENSION_LIST_EXTRA, extensions)
                    .setAction(PlayerActivity.ACTION_VIEW_LIST);
        }

    }
}
*/
