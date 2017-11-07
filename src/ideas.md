Syntax thoughts:
<pre>
A = class()
{
    _x = 1;
    _y = 2;

    __new__ = func(x,y,z)
    {
        self.x = x;
        self.y = y;
        self.z = z; // yeah you can do this too...
    }

    f = func(self, a, b)
    {
          return [self.x + a, self.y+b];
    }

    get x = func(self)
    {
        return self._x;
    }

    set x = func(self, val)
    {
        self._x = val;
    }

    doAThing = func(self, a, b)
    {
        innerFunc = func(blort)
        {
            return a+b+blort;
        }

        return innerFunc;
    }
}

a = A(1,2,3);
a.x;
a._x; // should throw
a.f(1,2);
A.f(a,1,2);

</pre>