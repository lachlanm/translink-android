package com.lach.translink.activities.data;

import com.lach.translink.activities.location.ResolveLocationTestCase;
import com.lach.translink.data.DataComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MockPresenterModule.class,
        MockLocationDataModule.class,
        MockJourneyDataModule.class,
        MockPlaceDataModule.class,
        MockProviderHelperModule.class,
        MockCoreModule.class
})
public interface MockDataComponent extends DataComponent {

    ResolveLocationTestCase inject(ResolveLocationTestCase inject);

}
