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

    get x = func()
    {
        return self._x;
    }

    set x = func(a)
    {
        // something else here
    }
}

f = Foo();
```

No, really, classes are variables
---
```
FooMaker = func(x, y)
{
    Foo = class()
    {
        // same as above...
    }

    f = Foo();
    f.x = x;
    f.y = y;
    return f;
}

c = FooMaker();
```

Or, alternatively you can return the actual class. It doesn't matter. But if you have a 'Maker' function that's the same as a constructor. You could probably even be more clever and use variable binding when you call the Maker function so you don't even need to do `f.x=blah;`. Hey, I just though about that now, it's a great idea!

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
