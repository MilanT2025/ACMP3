package Controlador;

import java.util.Objects;

public class Options {

    private String value;
    private String text;

    public Options() {
    }

    public Options(String val, String txt) {
        value = val;
        text = txt;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String val) {
        value = val;
    }

    public void setText(String txt) {
        text = txt;
    }

    public String getText() {
        return text;
    }

    @Override

    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Options)) {
            return false;
        }
        Options other = (Options) o;
        if (!this.getText().trim().equals("")) {
            return (this.getText().equals(other.getText()));
        } else {
            return (this.getValue().equals(other.getValue()));
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
        hash = 79 * hash + Objects.hashCode(this.text);
        return hash;
    }

}
