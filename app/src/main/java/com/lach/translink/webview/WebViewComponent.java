package com.lach.translink.webview;

import com.lach.common.data.CoreModule;
import com.lach.translink.TranslinkApplication;
import com.lach.translink.data.journey.JourneyDataModule;
import com.lach.translink.ui.result.JourneyResultFragment;
import com.lach.translink.tasks.result.TaskJourneySearch;

import dagger.Component;

@Component(modules = {WebViewModule.class, JourneyDataModule.class, CoreModule.class})
public interface WebViewComponent {

    TranslinkApplication inject(TranslinkApplication inject);

    JourneyResultFragment inject(JourneyResultFragment inject);

    TaskJourneySearch inject(TaskJourneySearch inject);

}
