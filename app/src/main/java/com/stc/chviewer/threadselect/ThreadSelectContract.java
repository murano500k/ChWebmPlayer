/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stc.chviewer.threadselect;

import com.stc.chviewer.BasePresenter;
import com.stc.chviewer.BaseView;
import com.stc.chviewer.retro.model.ChThread;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ThreadSelectContract {

    interface View extends BaseView<Presenter> {

        void updateProgress(int percent);

        void showThreads(final List<ChThread> threads, boolean sawError);

        void onThreadSelected(long threadNum);

        String getBoard();
        void showError(String text);

        void startPlayer(String boardName,String threadTitle, String[]uris, String[] extentions);
    }

    interface Presenter extends BasePresenter {
        void getUris(long id);
    }
}
