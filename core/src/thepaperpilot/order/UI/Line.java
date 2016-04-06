package thepaperpilot.order.UI;

public class Line {
    public String name;
    public String message;
    public int face = 0;
    public String event;
    public Option[] options = new Option[]{};
    public int timer;
    public String next;

    public Line() {

    }

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
