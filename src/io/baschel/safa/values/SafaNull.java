package io.baschel.safa.values;

public class SafaNull implements SafaValue<Void> {

    public static final SafaNull NUL = new SafaNull();

    private SafaNull()
    {
    }

    @Override
    public Void value() {
        return null;
    }
}
