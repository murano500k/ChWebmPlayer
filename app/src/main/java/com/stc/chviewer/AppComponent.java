package com.stc.chviewer;

import com.stc.chviewer.activitythreads.ThreadsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by artem on 2/20/17.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(ThreadsActivity threadsActivity);
    void inject(ChApp app);
}
