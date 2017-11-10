package io.baschel.safa.values;

import java.math.BigDecimal;

public class SafaImmutableLiteral<V> implements SafaValue<V>
{
    private final V value;
    public SafaImmutableLiteral(V v)
    {
        this.value = v;
    }

    @Override
    public V value() {
        return this.value;
    }
}
