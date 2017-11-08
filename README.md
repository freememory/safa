Safa: A Language I'd Code In
=============================
This repository is based on the excellent work by @munificent in his book
*Crafting Interpreters*. I can only hope I do his work justice. Sadly, I'm
not all that smart and so I doubt I'll have the skills or discipline to code
all the things I want.

What am I looking for? I'd like the following:

Functions are just variables
---
```
foo = func(a,b,c)
{
    // Basic syntax
    a = 1, b = 2;
    if(a >= 2)
        b++;
}
```

Classes are just variables
---
```
Foo = class(FooBase1, FooBase2, AnotherMixin3)
{
    _x = 1;
    _y = 2;

    __new__ = func(self, arg1, arg2)
    {
        self.x = arg1;
    }

    get x = func()
    {
        return self._x;
    }

    set x = func(a)
    {
        // something else here
    }
}

f = Foo(1,2);
```

No, really, classes are variables
---
```
FooMaker = func()
{
    Foo = class()
    {
    }

    return Foo;
}

c = FooMaker();
f = c();
```

Lists
---
Everything you know and love about lists, I guess?
```
A = [1,2,3];
for(x in A)
{
    f(x);
}

b = 1 in A;
b == true;
```

Structures
---
Honestly not sure how I feel about the below
Maybe Structures should be String-only, and we define a different
structure type -- like GStructure -- for non-strings? I dunno, TBD!

```
S = {||}
S.foo = "hi";
S.bar = 42;
S.baz = {|
    "floogle": "bazzle",
    400: [1,2,3] |};

for([k,v] in S)
{
    print(k + ": " + v + "\n");
}
```

Numbers
---
Infinite precision, infinite length. This is 2017, wubba lubba dub dub.
All numbers are doubles, represented by BigDecimal. Probably it's going to be insanely slow...