package com.stc.chviewer.activitythreads;

import com.stc.chviewer.BasePresenter;
import com.stc.chviewer.BaseView;
import com.stc.chviewer.activitythreads.model.PlayableItem;
import com.stc.chviewer.activitythreads.model.ThreadItemsPlaylist;

import java.util.List;

/**
 * Created by artem on 2/9/17.
 */

public interface ThreadsContract {
    interface View extends BaseView<ThreadsContract.Presenter> {
        String getBoard();
        void showBaseInfo(List<ThreadItemsPlaylist> threads, boolean sawError);
        void startPlayer(String board, ThreadItemsPlaylist threadItemsPlaylist, PlayableItem[] playableItems, int itemIndex );
        void showError(String errorText);
        void loadThreadContent(ThreadItemsPlaylist thread, PlayableItem[] playableItems);
    }

    interface Presenter extends BasePresenter {
        void requestStartPlayer(String threadId, int itemIndex);
        void requestGetThreadContent(String threadId);
        void requestSearchThreadContent(String query);
    }
}
