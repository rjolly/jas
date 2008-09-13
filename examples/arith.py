#
# jython examples for jas.
# $Id$
#

import sys;

from jas import Ring
from jas import QQ
from jas import CC
from jas import RF
from jas import startLog
from jas import terminate

# example for rational and complex numbers
#
#

rn = QQ(1,2);
print "rn:", rn;
print "rn^2:", rn*rn;

rn = QQ((3,2));
print "rn:", rn;
print "rn^2:", rn*rn;

c = CC();
print "c:", c;
c = c.one();
print "c:", c;
c = CC((2,),(3,));
print "c:", c;
print "c^5:", c**5 + c.one();

c = CC( (2,),rn );
print "c:", c;


r = Ring( "Q(x,y) L" );
print "Ring: " + str(r);
print;

# sage like: with generators for the polynomial ring
[x,y] = r.gens();
one = r.one();
zero = r.zero();

try:
    f = RF();
except:
    f = None;
print "f: " + str(f);
print;

d = x**2 + 5 * x - 6;
f = RF(d);
print "f: " + str(f);
print;

n = d*d + y + 1;
f = RF(d,n);
print "f: " + str(f);
print;

# beware not to mix expressions
f = f**2 - f;
print "f^3: " + str(f);
print;

f = f/f;
print "f/f: " + str(f);
print;

f = RF(d,one);
print "f: " + str(f);
print;

f = RF(zero);
print "f: " + str(f);
print;

f = RF( (d,y) );
print "f: " + str(f);
print;

print "one:  " + str(f.one());
print "zero: " + str(f.zero());
print;

terminate();
#sys.exit();
