package com.stc.chviewer;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    CountingIdlingResource idlingResource;
    Instrumentation instrumentation;
    private static final String TAG = "ExampleInstrumentedTest";

    @Before
    public void prepare(){
        instrumentation=InstrumentationRegistry.getInstrumentation();
        idlingResource = new CountingIdlingResource(TAG);
        Espresso.registerIdlingResources(idlingResource);

    }
    /*@Test
    public void testTest() throws Exception {
        int i=0;
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        idlingResource.increment();
        ChRetroHelper retroHelper = new ChRetroHelper();
        Observable<Catalog> catalog = retroHelper.getThreads("b");
        catalog.flatMapIterable(new Func1<Catalog, Iterable<File>>() {
            @Override
            public Iterable<File> call(Catalog catalog) {
                return catalog.getThreads().get(0).getFiles();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
            @Override
            public void onCompleted() {
                idlingResource.decrement();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ",e );
                idlingResource.decrement();
            }

            @Override
            public void onNext(File file) {
                Log.d(TAG, "onNext: "+file);
            }
        });
        i=0;
           while(!idlingResource.isIdleNow()){
            SystemClock.sleep(1000);
            Log.d(TAG, "sleep: "+i);
            i++;
        }

    }*/

    @Test
    public void testDataStructure() throws Exception {
        String webmUrl= "https://2ch.hk/b/src/146358725/14866409430490.jpg";
        String temp=webmUrl.substring(webmUrl.indexOf("src/")+4);
        Log.d(TAG, "testDataStructure: "+ temp.substring(0, temp.indexOf("/")));
    }

}
