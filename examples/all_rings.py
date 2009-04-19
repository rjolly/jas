#
# jython examples for jas.
# $Id$
#

import sys;

from jas import Ring
from jas import PolyRing
from jas import Ideal
from jas import startLog
from jas import terminate
from jas import ZZ, QQ, ZM, DD, CC, Quat, Oct, AN, RealN, RF
from edu.jas.arith import BigDecimal


print "------- ZZ = BigInteger ------------";
z1 = ZZ(12345678901234567890);
print "z1 = " + str(z1);
z2 = z1**2 + 12345678901234567890;
print "z2 = " + str(z2);
print;

print "------- QQ = BigRational ------------";
r1 = QQ(1,12345678901234567890);
print "r1 = " + str(r1**3);
r2 = r1**2 + (1,12345678901234567890);
print "r2 = " + str(r2);
print;

print "------- ZM = ModInteger ------------";
m1 = ZM(19,12345678901234567890);
print "m1 = " + str(m1);
m2 = m1**2 + 12345678901234567890;
print "m2 = " + str(m2);
print;

print "------- DD = BigDecimal ------------";
d1 = DD(12345678901234567890);
print "d1 = " + str(d1);
d2 = (1/d1)**2;
print "d2 = " + str(d2);
print;

print "------- CC = BigComplex ------------";
c1 = CC((1,2),(5,));
print "c1 = " + str(c1);
c2 = (1/c1)**2;
print "c2 = " + str(c2);
c3 = CC(0,(1,));
c3 = 1/c3;
print "c3 = " + str(c3);
[one,I] = CC().gens();
print "one = " + str(one);
print "I   = " + str(I);
c4 = c3 + 5 * I;
print "c4 = " + str(c4);
print;

print "------- Quat = BigQuaternion ------------";
[oneQ,IQ,JQ,KQ] = Quat().gens();
print "oneQ = " + str(oneQ);
print "IQ   = " + str(IQ);
print "JQ   = " + str(JQ);
print "KQ   = " + str(KQ);
q1 = 2 + 3 * IQ + 4 * JQ + 5 * KQ;
print "q1 = " + str(q1);
q2 = (1/q1)**2;
print "q2 = " + str(q2);
q3 = q2 * q1 * q1;
print "q3 = " + str(q3);
q4 = (-23,1458) + (-1,243)*IQ + (-4,729)*JQ + (-5,729)*KQ
print "q4 = " + str(q4);
q5 = q2 - q4;
print "q5  = " + str(q5);
print;

print "------- Oct = BigOctonion ------------";
#print [ str(g) for g in Oct().gens() ];
[oneOR,IOR,JOR,KOR,oneOI,IOI,JOI,KOI] = Oct().gens();
print "oneOR = " + str(oneOR);
print "IOR   = " + str(IOR);
print "JOR   = " + str(JOR);
print "KOR   = " + str(KOR);
print "oneOI = " + str(oneOI);
print "IOI   = " + str(IOI);
print "JOI   = " + str(JOI);
print "KOI   = " + str(KOI);
o1 = 2 * oneOR + 3 * IOR + 4 * JOR + 5 * KOR + 6 * oneOI + 7 * IOI + 8 * JOI + 9 * KOI;
print "o1 = " + str(o1);
o2 = (1/o1)**2;
print "o2 = " + str(o2);
o3 = o2 * o1 * o1;
print "o3 = " + str(o3);
o4 = (-69,20164)*oneOR + (-3,20164)*IOR + (-1,5041)*JOR + (-5,20164)*KOR  + (-3,10082)*oneOI + (-7,20164)*IOI + (-2,5041)*JOI + (-9,20164)*KOI;
print "o4 = " + str(o4);
o5 = o2 - o4;
print "o5  = " + str(o5);
print;

print "------- PolyRing(ZZ(),\"x,y,z\") ---------";
r = PolyRing(ZZ(),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[one,x,y,z] = r.gens();
print "one = " + str(one);
print "x   = " + str(x);
print "y   = " + str(y);
print "z   = " + str(z);
p1 = 2 + 3 * x + 4 * y + 5 * z + ( x + y + z )**2;
print "p1  = " + str(p1);
p2  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + 5 * z + 4 * y + 3 * x + 2;
print "p2  = " + str(p2);
p3 = p1 - p2;
print "p3  = " + str(p3);
print "p3.factory() = " + str(p3.factory());
print;

print "------- PolyRing(QQ(),\"x,y,z\") ---------";
r = PolyRing(QQ(),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[one,x,y,z] = r.gens();
print "one = " + str(one);
print "x   = " + str(x);
print "y   = " + str(y);
print "z   = " + str(z);
s1 = QQ(1,2) + QQ(2,3) * x + QQ(2,5) * y + ( x + y + z )**2;
print "s1  = " + str(s1);
s2 = (1,2) + (2,3) * x + (2,5) * y + ( x + y + z )**2;
print "s2  = " + str(s2);
s3  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + (2,5) * y + (2,3) * x + (1,2);
print "s3  = " + str(s3);
s4 = s1 - s3;
print "s4  = " + str(s4);
print "s4.factory() = " + str(s4.factory());
print;

print "------- PolyRing(ZM(11),\"x,y,z\") ---------";
r = PolyRing(ZM(11),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[one,x,y,z] = r.gens();
print "one = " + str(one);
print "x   = " + str(x);
print "y   = " + str(y);
print "z   = " + str(z);
p1 = 12 + 13 * x + 14 * y + 15 * z + ( x + y + z )**2;
print "p1  = " + str(p1);
p2  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + 4 * z + 3 * y + 2 * x + 1;
print "p2  = " + str(p2);
p3 = p1 - p2;
print "p3  = " + str(p3);
print "p3.factory() = " + str(p3.factory());
print;

print "------- PolyRing(DD(),\"x,y,z\") ---------";
r = PolyRing(DD(),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[one,x,y,z] = r.gens();
print "one = " + str(one);
print "x   = " + str(x);
print "y   = " + str(y);
print "z   = " + str(z);
p1 = 0.2 + 0.3 * x + 0.4 * y + 0.5 * z + ( x + y + z )**2;
print "p1  = " + str(p1);
p2  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + 0.5 * z + 0.40000000000000002220446049250313080847263336181641 * y + 0.29999999999999998889776975374843459576368331909180 * x + 0.200000000000000011102230246251565404236316680908203125;
print "p2  = " + str(p2);
p3 = p1 - p2;
print "p3  = " + str(p3);
print "p3.factory() = " + str(p3.factory());
print;

print "------- PolyRing(CC(),\"x,y,z\") ---------";
r = PolyRing(CC(),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[one,I,x,y,z] = r.gens();
print "one = " + str(one);
print "x   = " + str(x);
print "y   = " + str(y);
print "z   = " + str(z);
s1 = CC((1,2)) + CC((2,3)) * x + CC((2,5)) * y + ( x + y + z )**2;
print "s1  = " + str(s1);
#print "s1.factory() = " + str(s1.factory());
#print "s1.coefficients() = " + str(s1.coefficients());
s2 = ((1,2),) + ((2,3),) * x + ((2,5),) * y + ( x + y + z )**2;
print "s2  = " + str(s2);
#print "s2.factory() = " + str(s2.factory());
#print "s2.coefficients() = " + str(s2.coefficients());
s3  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + (2,5) * y + (2,3) * x + (1,2);
print "s3  = " + str(s3);
#print "s3.factory() = " + str(s3.factory());
#print "s3.coefficients() = " + str(s3.coefficients());
s4 = s3 - s1;
print "s4  = " + str(s4);
print "s4.factory() = " + str(s4.factory());
print;

print "------- PolyRing(Quat(),\"x,y,z\") ---------";
r = PolyRing(Quat(),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[oneQ,IQ,JQ,KQ,x,y,z] = r.gens();
print "oneQ = " + str(oneQ);
print "IQ   = " + str(IQ);
print "JQ   = " + str(JQ);
print "KQ   = " + str(KQ);
print "x    = " + str(x);
print "y    = " + str(y);
print "z    = " + str(z);
s1 = Quat((1,2)) + Quat((2,3)) * x + Quat((2,5)) * y + ( x + y + z )**2;
print "s1  = " + str(s1);
s2 = ((1,2),) + ((2,3),) * x + ((2,5),) * y + ( x + y + z )**2;
print "s2  = " + str(s2);
s3  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + (2,5) * y + (2,3) * x + (1,2);
print "s3  = " + str(s3);
s4 = s3 - s1;
print "s4  = " + str(s4);
print "s4.factory() = " + str(s4.factory());
print;

print "------- PolyRing(Oct(),\"x,y,z\") ---------";
r = PolyRing(Oct(),"x,y,z",PolyRing.grad);
print "r = " + str(r);
[oneOR,IOR,JOR,KOR,oneOI,IOI,JOI,KOI,x,y,z] = r.gens();
print "oneOR = " + str(oneOR);
print "IOR   = " + str(IOR);
print "JOR   = " + str(JOR);
print "KOR   = " + str(KOR);
print "oneOI = " + str(oneOI);
print "IOI   = " + str(IOI);
print "JOI   = " + str(JOI);
print "KOI   = " + str(KOI);
print "x     = " + str(x);
print "y     = " + str(y);
print "z     = " + str(z);
s1 = Oct(Quat((1,2))) + Oct(Quat((2,3))) * x + Oct(Quat((2,5))) * y + ( x + y + z )**2;
print "s1  = " + str(s1);
s2 = QQ(1,2) + QQ(2,3) * x + QQ(2,5) * y + ( x + y + z )**2;
print "s2  = " + str(s2);
s3  = z**2 + 2 * y * z + 2 * x * z + y**2 + 2 * x * y + x**2 + (2,5) * y + (2,3) * x + (1,2);
print "s3  = " + str(s3);
s4 = s3 - s1;
print "s4  = " + str(s4);
print "s4.factory() = " + str(s4.factory());
print;

print "------- AN(alpha**2 - 2,\"alpha\") ---------";
r = PolyRing(QQ(),"alpha",PolyRing.lex);
print "r = " + str(r);
[one,alpha] = r.gens();
print "one   = " + str(one);
print "alpha = " + str(alpha);
sqrt2 = alpha**2 - 2;
print "sqrt2 = " + str(sqrt2);
a = AN(sqrt2,alpha);
print "a     = " + str(a);
b = a**2 -2;
print "b     = " + str(b);
print;

print "------- GF(alpha**2 - 2,\"alpha\") ---------";
r = PolyRing(ZM(17),"alpha",PolyRing.lex);
print "r = " + str(r);
[one,alpha] = r.gens();
print "one   = " + str(one);
print "alpha = " + str(alpha);
sqrt2 = alpha**2 - 2;
print "sqrt2 = " + str(sqrt2);
a = AN(sqrt2,alpha);
print "a     = " + str(a);
b = a**2 -2;
print "b     = " + str(b);
print;

print "------- RAN(alpha**2 - 2,\"alpha\",((1),(2)) ---------";
r = PolyRing(QQ(),"alpha",PolyRing.lex);
print "r = " + str(r);
[one,alpha] = r.gens();
print "one   = " + str(one);
print "alpha = " + str(alpha);
sqrt2 = alpha**2 - 2;
print "sqrt2 = " + str(sqrt2);
a = RealN(sqrt2,(1,2),alpha);
print "a     = " + str(a);
b = 7 * a - 10;
print "b     = " + str(b);
print "b.factory()  = " + str(b.factory());
print "sign(b)      = " + str(b.signum());
print "magnitude(b) = " + str(BigDecimal(b.elem.magnitude()));
print;



print "------------------------------------";

#r = Ring( "IntFunc(e,f)(B,S,T,Z,P,W) L" );
#print "Ring: " + str(r);
#print;

terminate();
#sys.exit();

