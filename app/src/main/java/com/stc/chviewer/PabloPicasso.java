package com.stc.chviewer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by artem on 11/12/16.
 */

public final class PabloPicasso {
	private static Picasso instance;
	private static OkHttpDownloader downloader;

	public static Picasso with(Context context) {
		if (instance == null) {
			//if (downloader == null) downloader =  new OkHttpDownloader(context.getApplicationContext(), 2400000);
			instance = new Picasso.Builder(context.getApplicationContext())
					.memoryCache(new LruCache(context)).listener(new Picasso.Listener() {
						@Override
						public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
							Log.e("PICSSO ERROR", uri.toString());
						}

					})
					/*.downloader(downloader)*/
					.build();
		}
		return instance;
	}

	private PabloPicasso() {
		throw new AssertionError("No instances.");
	}/*
	public static void saveBitmap(Bitmap bitmap, File file){
		Observable.just(bitmap).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).subscribe(bitmap1 -> {
			try {

				if (!file.exists()) {
					assertNotNull(file.createNewFile());
				}
				FileOutputStream ostream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
				ostream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}*/
}
