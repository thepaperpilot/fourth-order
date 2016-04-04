package thepaperpilot.order.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import thepaperpilot.order.Main;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(1280, 720);
                config.preferFlash = false;
                return config;
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new Main();
        }
}