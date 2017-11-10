package io.baschel.safa.values;

public interface SafaValue<V>
{
    V value();

    default String _string()
    {
        return value().toString();
    }
}
