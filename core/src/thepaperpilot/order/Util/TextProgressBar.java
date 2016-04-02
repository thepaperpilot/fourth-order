package thepaperpilot.order.Util;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TextProgressBar extends ProgressBar{
    private Label labelName;
    private Label labelValue;
    private Label.LabelStyle labelStyle;

    public TextProgressBar(String text, float min, float max, float stepSize,
                           boolean vertical, ProgressBarStyle style, Label.LabelStyle labelStyle) {
        super(min, max, stepSize, vertical, style);

        this.labelStyle = new Label.LabelStyle(labelStyle);
        this.labelStyle.fontColor.a = 0.7f;

        if (vertical) bottom();
        else {
            labelName = new Label(text, this.labelStyle);
            add(labelName).expand().left().top();
        }

        labelValue = new Label(vertical ? "" + (int) value : (int) value+"/"+ (int) max, labelStyle);
        add(labelValue).right().bottom();
    }

    @Override
    public boolean setValue(float value){
        if(!super.setValue(value))
            return false;

        refreshValue();
        return true;
    }

    @Override
    public void setRange(float min, float max){
        super.setRange(min, max);
        refreshValue();
    }

    public void refreshValue(){
        labelValue.setText(vertical ? "" + (int) value : (int) value+"/"+ (int) max);
    }

}
