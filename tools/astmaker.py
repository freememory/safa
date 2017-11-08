#!/usr/bin/python3
import sys

def define_ast(outdir, base_class, **classmap):
	with open('%s/%s.java' % (outdir, base_class), "wt") as file:
		code = '''/********************************************************************************************
------------ AUTOGENERATED -------------
DO NOT EDIT THIS FILE BY HAND!
------------ AUTOGENERATED -------------
********************************************************************************************/
package io.baschel.safa;

import java.util.List;

abstract class {base} {{
	{derived}
}}
'''
		code = code.format(base=base_class, derived='\n'.join([define_type(base_class, k, v) for k,v in classmap.items()]))
		file.write(code)

def define_type(base, derv, fields):
	subclass = '''
	static class {classname} extends {superclass}
	{{
		{classname}({fields})
		{{
{initializer}
		}}
		
{classfields}
	}}'''
	
	parameters = {
		'classname': derv,
		'superclass': base,
		'fields': ', '.join(['%s %s' % t for t in fields]),
		'initializer': '\n'.join(['			this.%s = %s;' % (t[1], t[1]) for t in fields]),
		'classfields': '\n'.join(['		final %s %s;' % t for t in fields]),
	}
	
	return subclass.format(**parameters)		

def main(args):
	define_ast(args[0], 'Expr', **{
		'Unary': [('Token', 'operator'), ('Expr', 'right')],
		'Grouping':[('Expr', 'expression')],
		'Literal':[('Object', 'value')],
		'Binary': [('Expr', 'left'), ('Token', 'operator'), ('Expr', 'right')]
	})

if __name__=='__main__':
	main(sys.argv[1:])
