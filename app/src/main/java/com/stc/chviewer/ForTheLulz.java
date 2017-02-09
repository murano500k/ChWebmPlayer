/*
package com.stc.chviewer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

class ForTheLulz extends AsyncTask<Object, Object, ArrayList<String>> {
    private static final String testurl= "https://arhivach.org/thread/228705/";
    public static final String LIST = "list";
    Context context;

    public ForTheLulz(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... args) {
        ArrayList<String> strings=new ArrayList<>();
        try {
            Document doc = Jsoup.connect(testurl)
                    .referrer("http://www.google.com")
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .get();

            if (doc != null) {
                //Log.w("doc", doc.toString());

                Elements elems = doc.getElementsByAttributeValue("class", "expand_image");
                if (elems != null && !elems.isEmpty()) {
                    for(Element element : elems) {


                        //Log.w("elem", elem.toString());
                        String src = element.attr("onclick");
                        if (src != null) {
                            int iWebm=src.indexOf(".webm")+5;
                            if(iWebm<0) iWebm=src.indexOf(".ipg")+4;
                            int iHttp=src.indexOf("https://");
                            if(iHttp>0 && iWebm>0 && iHttp<iWebm ){
                                String url = src.substring(iHttp, iWebm);
                                Log.w("URL", url);
                                strings.add(url);
                            }else Log.e("ERROR", src);

                        }
                    }
                }
            }
        } catch (IOException e) {
            // Error handling goes here
        }
        return strings;
    }
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        Intent i=new Intent(context, PlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(LIST, result);
        i.putExtras(bundle);
        context.startActivity(i);
        //ImageView lulz = (ImageView) findViewById(R.id.lulpix);
        // if (result != null) {
        //     lulz.setImageBitmap(result);
        // } else {
        //Your fallback drawable resource goes here
        //lulz.setImageResource(R.drawable.nolulzwherehad);
        // }
    }
}*/
