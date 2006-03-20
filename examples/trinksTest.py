#
# jython examples for jas.
# $Id$
#

import sys;

from jas import Ring
from jas import Ideal

# trinks 6/7 example

#r = Ring( "Mod 19 (B,S,T,Z,P,W) L" );
#r = Ring( "Mod 1152921504606846883 (B,S,T,Z,P,W) L" ); # 2^60-93
#r = Ring( "Quat(B,S,T,Z,P,W) L" );
#r = Ring( "Z(B,S,T,Z,P,W) L" );
#r = Ring( "C(B,S,T,Z,P,W) L" );
r = Ring( "Rat(B,S,T,Z,P,W) L" );
print "Ring: " + str(r);
print;


ps = """
( 
 ( 45 P + 35 S - 165 B - 36 ), 
 ( 35 P + 40 Z + 25 T - 27 S ), 
 ( 15 W + 25 S P + 30 Z - 18 T - 165 B**2 ), 
 ( - 9 W + 15 T P + 20 S Z ), 
 ( P W + 2 T Z - 11 B**3 ), 
 ( 99 W - 11 B S + 3 B**2 ),
 ( B**2 + 33/50 B + 2673/10000 )
) 
""";

# ( 10000 B**2 + 6600 B + 2673 )
# ( B**2 + 33/50 B + 2673/10000 )

f = Ideal( r, ps );
print "Ideal: " + str(f);
print;

rg = f.GB();
print "seq Output:", rg;
print;


rg = f.parGB(2);
print "par Output:", rg;
#print;

f.distClient(); # starts in background
rg = f.distGB(2);
print "dist Output:", rg;
#print;

#sys.exit();

