/*
 * $Id$
 */

package edu.jas.gb;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;

import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.GenPolynomialTokenizer;
import edu.jas.poly.PolynomialList;


/**
 * Groebner base sequential iterative GB tests with JUnit.
 * @author Heinz Kredel.
 */

public class GroebnerBaseSigSeqIterTest extends TestCase {


    /**
     * main
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        junit.textui.TestRunner.run(suite());
    }


    /**
     * Constructs a <CODE>GroebnerBaseSigSeqIterTest</CODE> object.
     * @param name String.
     */
    public GroebnerBaseSigSeqIterTest(String name) {
        super(name);
    }


    /**
     * suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(GroebnerBaseSigSeqIterTest.class);
        return suite;
    }


    GenPolynomialRing<BigRational> fac;


    List<GenPolynomial<BigRational>> L, G;


    PolynomialList<BigRational> F;


    GroebnerBaseAbstract<BigRational> bb;


    GenPolynomial<BigRational> a, b, c, d, e;


    int rl = 4; //4; //3; 


    int kl = 7; // 10


    int ll = 7;


    int el = 3; // 4


    float q = 0.2f; //0.4f


    @Override
    protected void setUp() {
        BigRational coeff = new BigRational(9);
        fac = new GenPolynomialRing<BigRational>(coeff, rl);
        a = b = c = d = e = null;
        //bb = new GroebnerBaseSigSeqIter<BigRational>();
        //bb = new GroebnerBaseGGVSigSeqIter<BigRational>();
        //bb = new GroebnerBaseArrisSigSeqIter<BigRational>();
        bb = new GroebnerBaseF5zSigSeqIter<BigRational>();
        //bb = new GroebnerBaseSeqIter<BigRational>();
    }


    @Override
    protected void tearDown() {
        a = b = c = d = e = null;
        fac = null;
        bb = null;
    }


    /**
     * Test sequential GBase.
     */
    public void xtestSequentialGBase() {
        L = new ArrayList<GenPolynomial<BigRational>>();

        a = fac.random(kl, ll, el, q);
        b = fac.random(kl, ll, el, q);
        c = fac.random(kl, ll, el, q);
        d = fac.random(kl, ll, el, q);
        e = d; //fac.random(kl, ll, el, q );

        if (a.isZERO() || b.isZERO() || c.isZERO() || d.isZERO()) {
            return;
        }

        assertTrue("not isZERO( a )", !a.isZERO());
        L.add(a);

        L = bb.GB(L);
        assertTrue("isGB( { a } )", bb.isGB(L));

        assertTrue("not isZERO( b )", !b.isZERO());
        L.add(b);
        //System.out.println("L = " + L.size() );

        L = bb.GB(L);
        assertTrue("isGB( { a, b } )", bb.isGB(L));

        assertTrue("not isZERO( c )", !c.isZERO());
        L.add(c);

        L = bb.GB(L);
        assertTrue("isGB( { a, b, c } )", bb.isGB(L));

        assertTrue("not isZERO( d )", !d.isZERO());
        L.add(d);

        L = bb.GB(L);
        assertTrue("isGB( { a, b, c, d } )", bb.isGB(L));

        assertTrue("not isZERO( e )", !e.isZERO());
        L.add(e);

        L = bb.GB(L);
        assertTrue("isGB( { a, b, c, d, e } )", bb.isGB(L));
    }


    /**
     * Test Trinks7 GBase.
     */
    @SuppressWarnings("unchecked")
    public void testTrinks7GBase() {
        String exam = "(B,S,T,Z,P,W) L " + "( " 
                        + "( P W + 2 T Z - 11 B**3 ), "
	    //+ "( B**2 + 33/50 B + 2673/10000 ) " 
                        + "( 45 P + 35 S - 165 B - 36 ), "
                        + "( 35 P + 40 Z + 25 T - 27 S ), " 
                        + "( 15 W + 25 S P + 30 Z - 18 T - 165 B**2 ), "
                        + "( - 9 W + 15 T P + 20 S Z ), " 
                        + "( 99 W - 11 B S + 3 B**2 ), " 
                        + ") ";
        @SuppressWarnings("unused")
        String exam2 = "(x,y,z) L " + "( " + "( z y**2 + 2 x + 1/2 )" + "( z x**2 - y**2 - 1/2 x )"
                        + "( -z + y**2 x + 4 x**2 + 1/4 )" + " )";

        Reader source = new StringReader(exam);
        GenPolynomialTokenizer parser = new GenPolynomialTokenizer(source);
        try {
            F = (PolynomialList<BigRational>) parser.nextPolynomialSet();
        } catch (ClassCastException e) {
            fail("" + e);
        } catch (IOException e) {
            fail("" + e);
        }
        System.out.println("F = " + F);

        long t = System.currentTimeMillis();
        G = bb.GB(F.list);
        t = System.currentTimeMillis() - t;
        System.out.println("G = " + G);
        System.out.println("executed in " + t + " milliseconds");
        assertTrue("isGB( GB(Trinks7) )", bb.isGB(G));
        assertEquals("#GB(Trinks7) == 6", 6, G.size());
        //PolynomialList<BigRational> trinks = new PolynomialList<BigRational>(F.ring,G);
        //System.out.println("G = " + trinks);
    }

}
