/*
package com.stc.chviewer.choosesamples;
import com.stc.chviewer.Constants;
import com.stc.chviewer.retro.ChRetroHelper;
import com.stc.chviewer.retro.model.Catalog;
import com.stc.chviewer.retro.model.ChThread;
import com.stc.chviewer.retro.model.ChThreadSimple;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.stc.chviewer.PlayerActivity;
import com.stc.chviewer.retro.model.File;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;
import static com.stc.chviewer.Constants.BASE_URL_2CH;


public class MySampleChooserPresenter implements MySampleChooserContract.Presenter {
	private static final String TAG = "MySampleChooserPresente";
	private static final String BASE_URL= "https://arhivach.org";
	private static final String KEY_URL= "KEY_URL";
	private static final String KEY_NAME= "KEY_NAME";
	private static final String BASE_LIST_TRHREADS_URL= "https://arhivach.org/index";
	private static final String CH_TAG = "1526";
	private static final int MAX_COUNT = 5;
    ChRetroHelper retroHelper;
    private String board;


	private final MySampleChooserContract.View mSampleChooserView;

	private boolean sawError;

	private int count;
	ChThreadSimple t;
	List<SampleGroup> list;
	Observable<Boolean> observable;
	Observer<Integer> progressObserver;
	private Observer<List<SampleGroup>> listObserver;

	public MySampleChooserPresenter(@NonNull MySampleChooserContract.View mSampleChooserView) {
        this.mSampleChooserView=mSampleChooserView;
        mSampleChooserView.setPresenter(this);
        retroHelper = new ChRetroHelper();
        board=mSampleChooserView.getBoard();


    }

    @Override
    public void start() {
	    Log.d(TAG, "start: ");

	    progressObserver=new Observer<Integer>() {

		    @Override
		    public void onError(Throwable e) {


		    }
		    @Override
		    public void onComplete() {

		    }
		    @Override
		    public void onSubscribe(Disposable d) {

		    }
		    @Override
		    public void onNext(Integer integer) {
			    mSampleChooserView.updateProgress(integer);
		    }
	    };
	    listObserver=new Observer<List<SampleGroup>>() {

		    @Override
		    public void onError(Throwable e) {
			    Log.e(TAG, "onError: ",e );
			    mSampleChooserView.showThreads(new ArrayList<>(), true);

		    }

		    @Override
		    public void onComplete() {
			    mSampleChooserView.updateProgress(100);

		    }

		    @Override
		    public void onSubscribe(Disposable d) {
			    mSampleChooserView.updateProgress(0);

		    }

		    @Override
		    public void onNext(List<SampleGroup> list) {
			    mSampleChooserView.showThreads(list, list == null);
		    }
	    };
	    Observable.fromCallable(new Callable<List<SampleGroup>>() {

		    @Override
		    public List<SampleGroup> call() {
			    return loadData();
		    }
	    })
			    .subscribeOn(Schedulers.io())
			    .observeOn(AndroidSchedulers.mainThread())
			    .subscribe(listObserver);
    }

    private List<SampleGroup> loadData() {
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
    public Observable<List<SampleGroup>> getAllSamplesForBoard(){
        List<SampleGroup> result = new ArrayList<>();

        retroHelper.getThreads(board).forEach(new Action1<List<ChThread>>() {
            @Override
            public void call(List<ChThread> chThreads) {
                for(ChThread thread: chThreads){
                    String groupName = (thread.getSubject()+"\n"
                            +thread.getPostsCount()+" posts"+"\n"
                            +thread.getDate());
                    SampleGroup sGroup=new SampleGroup(groupName);

                    sGroup.samples=new ArrayList<>();
                    List<String> urlStrings=new ArrayList<String>();
                    retroHelper.getFiles(board, thread.getNum()).subscribe(new rx.Observer<File>() {
                        @Override
                        public void onCompleted() {
                            sGroup.samples.add();

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(File file) {
                            urlStrings.add(BASE_URL_2CH+file.getPath());
                        }

                    });

                }



            }
        })
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
        String connect_url =getBaseUrl(tag);//+"?tag="+tag;
        Log.d(TAG, "createThreadUrlList: "+connect_url);
        String name = null;
        String url = null;

        HashMap<String, String> object =new HashMap<String, String>();
        object.put(KEY_NAME , name);
        object.put(KEY_URL , url);
        uris.add(object);


        return uris;
	}

	private String getBaseUrl(String tag) {
		return BASE_LIST_TRHREADS_URL;//+"/viewed/weekly";
	}
    public void getThreads2(String board){
        rx.Observable<Catalog> call = retroHelper.getCatalog(board);

        Subscription subscription = call
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new rx.Subscriber<Catalog>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.e(TAG, "onError: ",e );
                        }
                        mThreadSelectView.showThreads(null, false);

                    }

                    @Override
                    public void onNext(Catalog catalog) {
                        Log.d(TAG, "onCompleted: "+catalog);

                        mThreadSelectView.showThreads(catalog.getThreads(), false);
                    }

                });
    }
    public void getUris(long threadId){
        retroHelper.getFiles(board,threadId).filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
                if(file.getType()==6)return true;
                else return false;
            }
        }).flatMap(new Func1<File, rx.Observable<String>>() {
            @Override
            public rx.Observable<String> call(File file) {
                return rx.Observable.just(BASE_URL_2CH+ file.getPath());
            }
        }).toList().subscribe(new rx.Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mThreadSelectView.showError(e.getMessage());

            }

            @Override
            public void onNext(List<String>uris) {
                if(uris==null || uris.size()==0)                 mThreadSelectView.showError("null");
                else {

                    String[] extL = new String[uris.size()];
                    String[] uriL = new String[uris.size()];
                    int i = 0;
                    for(String uri: uris){
                        extL[i]="webm";
                        uriL[i]=uri;
                        i++;

                        mThreadSelectView.startPlayer(board,String.valueOf(threadId) ,extL,uriL );
                    }
                }
            }
        });
    }


	public ArrayList<String> createWebmUrlList(String testurl){
		ArrayList<String> uris=new ArrayList<>();
		boolean hasVideos=false;

		try {
			Document doc = Jsoup.connect(testurl)
					.referrer("http://www.google.com")
					.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.get();

			if (doc != null) {
				Elements elems = doc.getElementsByAttributeValue("class", "expand_image");
				if (elems != null && !elems.isEmpty()) {
					for(Element element : elems) {
						String src = element.attr("onclick");
						if (src != null) {
							int iWebm=src.indexOf(".webm")+5;
							if(iWebm<0) iWebm=src.indexOf(".ipg")+4;
							int iHttp=src.indexOf("https://");
							if(iHttp>0 && iWebm>0 && iHttp<iWebm ){
								String url = src.substring(iHttp, iWebm);
								hasVideos=true;
								//Log.w("URL", url);
								uris.add(url);
							}//else Log.e("ERROR", src);

						}
					}
				}
			}
		} catch (IOException e) {

		}
		if(hasVideos) return uris;
		else return null;
	}

	public static final class SampleGroup {

		public final String title;
		public List<Sample> samples;

		public SampleGroup(String title) {
			this.title = title;
			this.samples = new ArrayList<>();
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

	public  static final class PlaylistSample extends Sample {

		public final UriSample[] children;

		public PlaylistSample(String name, UUID drmSchemeUuid, String drmLicenseUrl,
		                      String[] drmKeyRequestProperties, boolean preferExtensionDecoders,
		                      UriSample... children) {
			super(name, drmSchemeUuid, drmLicenseUrl, drmKeyRequestProperties, preferExtensionDecoders);
			this.children = children;
		}
		public PlaylistSample(String name,
		                      UriSample[]  children) {
			super(name, null, null, null, true);
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
