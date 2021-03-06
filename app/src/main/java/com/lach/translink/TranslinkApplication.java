package com.lach.translink;

import android.app.Activity;

import com.lach.common.BaseApplication;
import com.lach.common.data.CoreModule;
import com.lach.common.data.preference.PreferencesProvider;
import com.lach.common.log.Instrumentation;
import com.lach.common.util.ThemeHelper;
import com.lach.translink.data.CoreComponent;
import com.lach.translink.data.DaggerCoreComponentImpl;
import com.lach.translink.data.DaggerDataComponentImpl;
import com.lach.translink.data.DataComponent;
import com.lach.translink.app.AppInit;
import com.lach.translink.network.DaggerGoCardNetworkComponentImpl;
import com.lach.translink.network.GoCardNetworkComponent;
import com.lach.translink.webview.DaggerWebViewComponent;
import com.lach.translink.webview.WebViewComponent;
import com.lachlanm.xwalkfallback.WebViewFacade;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Inject;
import javax.inject.Provider;

public class TranslinkApplication extends BaseApplication {

    private CoreModule coreModule;
    private CoreComponent coreComponent;
    private DataComponent dataComponent;
    private Provider<GoCardNetworkComponent> goCardNetworkComponentProvider;

    private boolean webViewBeenInitialised;

    @Inject
    PreferencesProvider preferencesProvider;

    @Inject
    WebViewFacade webViewFacade;

    @Override
    public void onCreate() {
        super.onCreate();

        // Load any application specific logic. This can be overridden within a build type.
        AppInit.init(this);

        // Loads the instrumentation which sends bug reports and crashes.
        Instrumentation.init(this, "364904c2");

        // Loads the DbFlow manager used for database handling.
        FlowManager.init(this);

        goCardNetworkComponentProvider = new Provider<GoCardNetworkComponent>() {
            @Override
            public GoCardNetworkComponent get() {
                return DaggerGoCardNetworkComponentImpl.builder()
                        .coreModule(getCoreModule())
                        .build();
            }
        };

        // This must be injected now, as we rely on it to obtain the preferencesProvider
        WebViewComponent webViewComponent = DaggerWebViewComponent.builder()
                .coreModule(getCoreModule())
                .build();

        webViewComponent.inject(this);
    }

    @Override
    public void applyTheme(Activity activity, boolean useActionBar) {
        ThemeHelper.applyTheme(activity, preferencesProvider.getPreferences(), useActionBar);
    }

    @Override
    public boolean isLightTheme() {
        return ThemeHelper.isLightTheme(preferencesProvider.getPreferences());
    }

    public synchronized CoreModule getCoreModule() {
        if (coreModule == null) {
            coreModule = new CoreModule(this);
        }
        return coreModule;
    }

    public synchronized DataComponent getDataComponent() {
        if (dataComponent == null) {
            dataComponent = DaggerDataComponentImpl.builder()
                    .coreModule(getCoreModule())
                    .build();
        }
        return dataComponent;
    }

    public void setDataComponent(DataComponent locationDataComponent) {
        this.dataComponent = locationDataComponent;
    }

    public synchronized CoreComponent getCoreComponent() {
        if (coreComponent == null) {
            coreComponent = DaggerCoreComponentImpl.builder()
                    .coreModule(getCoreModule())
                    .build();
        }
        return coreComponent;
    }

    public void setCoreComponent(CoreComponent coreComponent) {
        this.coreComponent = coreComponent;
    }

    public synchronized void initWebViewIfRequired(Activity activity) {
        if (webViewBeenInitialised) {
            return;
        }
        webViewBeenInitialised = true;

        webViewFacade.getView(activity);
        webViewFacade.destroy();
        webViewFacade = null;
    }

    public void setGoCardNetworkComponentProvider(Provider<GoCardNetworkComponent> goCardNetworkComponentProvider) {
        this.goCardNetworkComponentProvider = goCardNetworkComponentProvider;
    }

    public GoCardNetworkComponent createGoCardNetworkComponent() {
        return goCardNetworkComponentProvider.get();
    }
}
