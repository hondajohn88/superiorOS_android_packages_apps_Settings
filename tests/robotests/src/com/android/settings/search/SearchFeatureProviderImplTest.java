/*
 * Copyright (C) 2016 The Android Open Source Project
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
 *
 */

package com.android.settings.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Menu;

import com.android.settings.SettingsRobolectricTestRunner;
import com.android.settings.TestConfig;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search2.DatabaseIndexingManager;
import com.android.settings.search2.SearchFeatureProviderImpl;

import com.android.settings.testutils.FakeFeatureFactory;
import com.android.settingslib.drawer.DashboardCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SettingsRobolectricTestRunner.class)
@Config(manifest = TestConfig.MANIFEST_PATH, sdk = TestConfig.SDK_VERSION)
public class SearchFeatureProviderImplTest {
    private SearchFeatureProviderImpl mProvider;
    private Activity mActivity;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Menu menu;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mActivity = Robolectric.buildActivity(Activity.class).create().visible().get();
        mProvider = new SearchFeatureProviderImpl();
    }

    @Test
    public void testPassNull_NoError() {
        mProvider.setUpSearchMenu(null, null);
    }

    @Test
    public void testSetUpMenu_HasItemAdded() {
        mProvider.setUpSearchMenu(menu, mActivity);

        verify(menu).add(anyInt(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testUpdateIndexNewSearch_UsesDatabaseIndexingManager() {
        mProvider = spy(new SearchFeatureProviderImpl());
        when(mProvider.isEnabled(mActivity)).thenReturn(true);

        mProvider.updateIndex(mActivity);
        verify(mProvider).getIndexingManager(any(Context.class));
    }

    @Test
    public void testUpdateIndexNewSearch_UsesIndex() {
        mProvider = spy(new SearchFeatureProviderImpl());
        when(mProvider.isEnabled(mActivity)).thenReturn(false);

        mProvider.updateIndex(mActivity);
        verify(mProvider, never()).getIndexingManager(any(Context.class));
    }
}
