// Generated code from Butter Knife. Do not modify!
package com.stc.chviewer.threadselect;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.stc.chviewer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ThreadSelectActivity_ViewBinding implements Unbinder {
  private ThreadSelectActivity target;

  @UiThread
  public ThreadSelectActivity_ViewBinding(ThreadSelectActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ThreadSelectActivity_ViewBinding(ThreadSelectActivity target, View source) {
    this.target = target;

    target.listThreads = Utils.findRequiredViewAsType(source, R.id.threads_list, "field 'listThreads'", RecyclerView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ThreadSelectActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listThreads = null;
    target.toolbar = null;
  }
}
