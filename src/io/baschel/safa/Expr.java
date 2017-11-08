/********************************************************************************************
------------ AUTOGENERATED -------------
DO NOT EDIT THIS FILE BY HAND!
------------ AUTOGENERATED -------------
********************************************************************************************/
package io.baschel.safa;

import java.util.List;

abstract class Expr 
{
	abstract <R> R accept(Visitor<R> visitor);
	
	interface Visitor<R>
	{
		R visitGroupingExpr(Expr grouping);
		R visitLiteralExpr(Expr literal);
		R visitUnaryExpr(Expr unary);
		R visitBinaryExpr(Expr binary);
	} 
	
	static class Grouping extends Expr
	{
		Grouping(Expr expression)
		{
			this.expression = expression;
		}

		<R> R accept(Visitor<R> visitor)
		{
			return visitor.visitGroupingExpr(this);
		}
		
		final Expr expression;
	}

	static class Literal extends Expr
	{
		Literal(Object value)
		{
			this.value = value;
		}

		<R> R accept(Visitor<R> visitor)
		{
			return visitor.visitLiteralExpr(this);
		}
		
		final Object value;
	}

	static class Unary extends Expr
	{
		Unary(Token operator, Expr right)
		{
			this.operator = operator;
			this.right = right;
		}

		<R> R accept(Visitor<R> visitor)
		{
			return visitor.visitUnaryExpr(this);
		}
		
		final Token operator;
		final Expr right;
	}

	static class Binary extends Expr
	{
		Binary(Expr left, Token operator, Expr right)
		{
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		<R> R accept(Visitor<R> visitor)
		{
			return visitor.visitBinaryExpr(this);
		}
		
		final Expr left;
		final Token operator;
		final Expr right;
	}
}
