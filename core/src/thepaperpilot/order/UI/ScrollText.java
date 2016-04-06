package thepaperpilot.order.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Constants;

public class ScrollText extends Label {
    private float time = 0;
    private int chars = 0;
    public String message = "";

    public ScrollText() {
        super("", Main.skin, "large");
        setAlignment(Align.topLeft);
        setWrap(true);
    }

    public void act(float delta) {
        super.act(delta);
        if (message != null && !message.equals("")) {
            time += delta;
            String scroll = "";
            int chars = 0;
            int currentChar = 0;
            while (chars < time * Constants.TEXT_SPEED && currentChar < message.length()) {
                switch (message.charAt(currentChar)) {
                    default:
                        scroll += message.charAt(currentChar);
                        chars++;
                        currentChar++;
                        break;
                    case '_':
                        chars++;
                        currentChar++;
                        break;
                    case '[':
                        String color = message.substring(currentChar, message.indexOf(']', currentChar));
                        scroll += color;
                        currentChar += color.length();
                        break;
                }
            }
            if (this.chars < Math.min(scroll.length(), (int) (time * Constants.TEXT_SPEED))) {
                this.chars += 3;
            }
            setText(scroll);
        }
    }

    public void setMessage(String message) {
        this.message = message;
        time = 1 / Constants.TEXT_SPEED;
        chars = 1;
    }

    public boolean isFinished() {
        return message.length() < (int) (time * Constants.TEXT_SPEED);
    }

    public void finish() {
        chars = message.length();
        time = message.length() / Constants.TEXT_SPEED;
    }
}
