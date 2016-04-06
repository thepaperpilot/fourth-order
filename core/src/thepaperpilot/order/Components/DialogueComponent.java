package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import thepaperpilot.order.UI.Line;
import thepaperpilot.order.UI.Option;
import thepaperpilot.order.UI.ScrollText;

import java.util.HashMap;
import java.util.Map;

public class DialogueComponent implements Component {
    public HashMap<String, Line> lines = new HashMap<String, Line>();
    public String start = "";
    public String player = "PortraitPlayer";
    public String[] actors = new String[]{};
    transient public Map<String, Runnable> events = new HashMap<String, Runnable>();

    transient public String line;
    transient public Option selected;
    transient public float timer;

    transient public Image playerFace;
    transient public Image actorFace;
    transient public Table message;
    transient public ScrollText messageLabel;

    public static DialogueComponent read(String file) {
        return new Json().fromJson(DialogueComponent.class, Gdx.files.internal(file).readString());
    }
}
