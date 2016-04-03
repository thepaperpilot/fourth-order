package thepaperpilot.order;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Util.Constants;

public class Dialogue extends Table {
    final Line[] lines;
    int line = 0;
    Option selected;
    private final Image playerFace;
    private final Image[] enemyFaces;
    protected final Table message = new Table(Main.skin);
    ScrollText messageLabel;
    private float maxTimer;
    private float timer;
    public Runnable chain;

    public Dialogue(Line[] lines, String player, String[] enemies) {
        super(Main.skin);
        playerFace = new Image(Main.getTexture(player));
        enemyFaces = new Image[enemies.length];
        setFillParent(true);
        pad(8);

        // create each part of the dialogue
        this.lines = lines;

        // if the dialogue is empty, let's go ahead and not do anything
        if (this.lines.length == 0)
            return;

        // create the dialogue stage
        for (int i = 0; i < enemies.length; i++) {
            enemyFaces[i] = new Image(Main.getTexture(enemies[i]));
        }
        messageLabel = new ScrollText();
        messageLabel.setAlignment(Align.topLeft);
        messageLabel.setWrap(true);
        message.top().left();
        message.setBackground(Main.skin.getDrawable("default-round"));
        message.pad(20);
        Table faces = new Table(Main.skin);
        faces.bottom().left().add(playerFace).width(3 * Constants.FACE_SIZE).height(4 * Constants.FACE_SIZE).expand().bottom().left();
        for (Image image : enemyFaces) {
            faces.add(image).width(3 * Constants.FACE_SIZE).height(4 * Constants.FACE_SIZE).bottom().right();
        }
        bottom().left().add(faces).expand().fill().padBottom(4).row();
        add(message).colspan(2).expandX().fillX().height(Constants.DIALOGUE_SIZE);
        message.add(new Label("Click to continue...", Main.skin)).expand().center().bottom();

        this.timer = this.maxTimer = timer;
        if (maxTimer == 0) {
            // left click to advance the dialogue
            addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    advance(false);
                    return false;
                }

                public boolean keyDown(InputEvent event, int keycode) {
                    switch (keycode) {
                        case Input.Keys.E:
                        case Input.Keys.ENTER:
                            advance(true);
                            break;
                        case Input.Keys.UP:
                        case Input.Keys.W:
                        case Input.Keys.A:
                            if (selected == null || line == 0) return false;
                            Line currLine = Dialogue.this.lines[line - 1];
                            for (int i = 0; i < currLine.options.length; i++) {
                                if (selected == currLine.options[i]) {
                                    if (i == 0)
                                        selected = currLine.options[currLine.options.length - 1];
                                    else selected = currLine.options[i - 1];
                                    break;
                                }
                            }
                            updateSelected();
                            break;
                        case Input.Keys.DOWN:
                        case Input.Keys.S:
                        case Input.Keys.D:
                            if (selected == null || line == 0) return false;
                            currLine = Dialogue.this.lines[line - 1];
                            for (int i = 0; i < currLine.options.length; i++) {
                                if (selected == currLine.options[i]) {
                                    if (i == currLine.options.length - 1)
                                        selected = currLine.options[0];
                                    else selected = currLine.options[i + 1];
                                    break;
                                }
                            }
                            updateSelected();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
    }

    public void open(Stage stage) {
        stage.addActor(this);
        if (timer == 0) stage.setKeyboardFocus(this);

        // start the dialogue
        if(line == 0) next();
    }

    public void act(float delta) {
        super.act(delta);
        if (timer != 0) {
            timer -= delta;
            if (timer <= 0) {
                timer = maxTimer;
                next();
            }
        }
    }

    private void advance(boolean override) {
        if (line > 0 && (override || lines[line  - 1].options.length == 0)) {
            if (messageLabel.isFinished()) {
                if (lines[line - 1].options.length == 0) next();
                else if (selected != null) selected.select(this);
            } else {
                messageLabel.finish();
            }
        }
    }

    void end() {
        line = 0;
        timer = maxTimer;
        next();
        remove();
        if (chain != null) chain.run();
    }

    private void next() {
        if (line > 0) {
            // run last line's events
            if (lines[line - 1].events != null)
                lines[line - 1].events.run();
        }

        // check if we're done with the dialogue
        if (lines.length <= line) {
            end();
            return;
        }

        // update the dialogue stage for the next part of the dialogue
        Line nextLine = lines[line];
        line++;
        playerFace.setColor(1, 1, 1, nextLine.face == 0 ? 1 : .5f);
        for (int i = 0; i < enemyFaces.length; i++) {
            enemyFaces[i].setColor(1, 1, 1, i == nextLine.face - 1 ? 1 : .5f);
        }
        messageLabel.setMessage((nextLine.name != null ? "[GOLD]" + nextLine.name + "[]\n" : "") + nextLine.message);
        message.clearChildren();
        message.add(messageLabel).expandX().fillX().left().padBottom(20).row();
        if (nextLine.options.length == 0) {
            if (maxTimer == 0)
                message.add(new Label("Click to continue...", Main.skin)).expand().center().bottom();
        } else {
            for (int i = 0; i < nextLine.options.length; i++) {
                nextLine.options[i].reset(this);
                message.add(nextLine.options[i]).left().padLeft(10).row();
            }
            selected = nextLine.options[0];
            updateSelected();
        }
        if (maxTimer == 0) {
            setTouchable(nextLine.options.length == 0 ? Touchable.enabled : Touchable.childrenOnly);
        }

        Main.click();
    }

    public void updateSelected() {
        if (line == 0) return;
        for (int i = 0; i < lines[line - 1].options.length; i++) {
            Option option = lines[line - 1].options[i];
            if (selected == option) {
                option.setText(" > " + option.message);
                option.setColor(Color.ORANGE);
            } else {
                option.setText("> " + option.message);
                option.setColor(Color.WHITE);
            }
        }
    }

    public static class Option extends Label {
        public Runnable events;
        final String message;

        public Option(String message) {
            super("> " + message, Main.skin, "large");
            this.message = message;
        }

        public void reset(final Dialogue dialogue) {
            // do the actions when this button is clicked
            addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    select(dialogue);
                    return true;
                }

                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    dialogue.selected = Option.this;
                    dialogue.updateSelected();
                }
            });
        }

        public void select(Dialogue dialogue) {
            Main.click();
            if (events != null) events.run();
            dialogue.selected = null;
            dialogue.next();
        }
    }

    public static class ScrollText extends Label {
        private float time = 6 / Constants.TEXT_SPEED;
        private int chars = 3;
        public String message = "";

        public ScrollText() {
            super("", Main.skin, "large");
        }

        public void act(float delta) {
            super.act(delta);
            if (!message.equals("")) {
                time += delta;
                if (chars < Math.min(message.length(), (int) (time * Constants.TEXT_SPEED))) {
                    Main.click();
                    chars += 3;
                }
                setText(message.substring(0, Math.min(message.length(), (int) (time * Constants.TEXT_SPEED))));
            }
        }

        public void setMessage(String message) {
            this.message = message;
            time = 6 / Constants.TEXT_SPEED;
            chars = 3;
        }

        public boolean isFinished() {
            return message.length() < (int) (time * Constants.TEXT_SPEED);
        }

        public void finish() {
            chars = message.length();
            time = message.length();
            Main.click();
        }
    }

    public static class Line {
        public String name;
        final String message;
        public int face = 0;
        public Runnable events;
        public Option[] options = new Option[]{};

        public Line(String message) {
            this.message = message;
        }

        public Line(String message, String name) {
            this(message);
            this.name = name;
        }

        public Line(String message, String name, int face) {
            this(message, name);
            this.face = face;
        }
    }
}
