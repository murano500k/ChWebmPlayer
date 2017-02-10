// Generated code from Butter Knife. Do not modify!
package com.stc.chviewer.boardselect;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BoardSelectActivity_ViewBinding implements Unbinder {
  private BoardSelectActivity target;

  @UiThread
  public BoardSelectActivity_ViewBinding(BoardSelectActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BoardSelectActivity_ViewBinding(BoardSelectActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, 2131624072, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BoardSelectActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
  }
}
