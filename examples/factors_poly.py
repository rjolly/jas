#
# jython examples for jas.
# $Id$
#

from java.lang import System
from java.lang import Integer

from jas import PolyRing, QQ
from jas import terminate
from jas import startLog

# polynomial examples: factorization

cr = PolyRing(QQ(),"x",PolyRing.lex );
print "Ring cr: " + str(cr);

r = PolyRing(cr,"y,z",PolyRing.lex );
print "Ring: " + str(r);
print;

[one,x,y,z] = r.gens();

#f = z * ( y + 1 )**2 * ( x**2 + x + 1 )**3;
#f = z * ( y + 1 ) * ( x**2 + x + 1 );
#f = ( y + 1 ) * ( x**2 + x + 1 );
#f = ( y + z**2 ) * ( x**2 + x + 1 );

#f = x**4 * y + x**3  + z + x   + z**2 + y * z**2;
## f = x**3 + ( ( y + 2 ) * z + 2 * y + 1 ) * x**2 \
##     + ( ( y + 2 ) * z**2 + ( y**2 + 2 * y + 1 ) * z + 2 * y**2 + y ) * x \
##     + ( y + 1 ) * z**3 + ( y + 1 ) * z**2 + ( y**3 + y**2 ) * z + y**3 + y**2;

f = ( x + y * z + y + z + 1 ) * ( x**2 + ( y + z ) * x + y**2 + z**2 );
#f = ( x + y * z + y + z + 1 ) * ( x**2 + ( y + z ) * x + y**2 + 1 );

#f = ( x + y ) * ( x - y);


print "f = ", f;
print;

startLog();

t = System.currentTimeMillis();
G = r.factors(f);
t = System.currentTimeMillis() - t;
print "#G = ", len(G);
#print "factor time =", t, "milliseconds";

g = one;
for h, i in G.iteritems():
    print "h**i = (", h, ")**" + str(i);
    h = h**i;
    g = g*h;
#print "g = ", g;

if cmp(f,g) == 0:
    print "factor time =", t, "milliseconds,", "isFactors(f,g): true" ;
else:
    print "factor time =", t, "milliseconds,", "isFactors(f,g): ",  cmp(f,g);
print;

#startLog();
terminate();
