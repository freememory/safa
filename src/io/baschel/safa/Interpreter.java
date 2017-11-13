package io.baschel.safa;


import io.baschel.safa.values.SafaImmutableLiteral;
import io.baschel.safa.values.SafaValue;

import java.math.BigDecimal;

import static io.baschel.safa.values.SafaNull.NUL;

public class Interpreter implements Expr.Visitor<SafaValue> {
    @Override
    public SafaValue visitGroupingExpr(Expr.Grouping grouping) {
        return eval(grouping.expression);
    }

    private SafaValue eval(Expr expr)
    {
        return expr.accept(this);
    }

    private boolean isTruthy(Object o)
    {
        if(o == null || o == NUL)
            return false;
        if(o instanceof Number)
            return ((Double)o) != 0;
        if(o instanceof Boolean)
            return (Boolean)o;
        return true;
    }

    private SafaValue makeValue(Object v)
    {
        if(v == null)
            return NUL;
        if(v instanceof Number)
            return new SafaImmutableLiteral<>((Double) v);
        else if(v instanceof Boolean)
            return new SafaImmutableLiteral<>((Boolean)v);
        else if(v instanceof String)
            return new SafaImmutableLiteral<>((String)v);
        return NUL;
    }

    @Override
    public SafaValue visitLiteralExpr(Expr.Literal literal) {
        Object v = literal.value;
        return makeValue(v);
    }

    @Override
    public SafaValue visitPostfixExpr(Expr.Postfix postfix)
    {
        return null;
    }

    @Override
    public SafaValue visitUnaryExpr(Expr.Unary unary) {
        SafaValue right = eval(unary.right);

        switch(unary.operator.type) {
            case MINUS:
                return makeValue(-(Double)right.value());
            case BANG:
                return makeValue(!isTruthy(right.value()));
            case TILDE:
                return makeValue((double) ~(((Double) right.value()).intValue()));
        }
        return null;
    }

    @Override
    public SafaValue visitBinaryExpr(Expr.Binary binary) {
        SafaValue left = eval(binary.left);
        SafaValue right = eval(binary.right);

        switch (binary.operator.type) {
            case MINUS:
                return makeValue((double)left.value() - (double)right.value());
            case SLASH:
                return makeValue((double)left.value() / (double)right.value());
            case STAR:
                return makeValue((double)left.value() * (double)right.value());
        }

        // Unreachable.
        return null;
    }
}
