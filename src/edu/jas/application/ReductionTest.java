/*
 * $Id$
 */

package edu.jas.application;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;

import edu.jas.structure.Product;
import edu.jas.structure.ProductRing;
import edu.jas.structure.RingFactory;

import edu.jas.arith.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.arith.BigComplex;

import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.ColorPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.PolynomialList;

import edu.jas.application.Ideal;


/**
 * Reduction tests with JUnit.
 * @author Heinz Kredel.
 */

public class ReductionTest extends TestCase {

/**
 * main
 */
   public static void main (String[] args) {
          BasicConfigurator.configure();
          junit.textui.TestRunner.run( suite() );
   }

/**
 * Constructs a <CODE>ReductionTest</CODE> object.
 * @param name String
 */
   public ReductionTest(String name) {
          super(name);
   }

/**
 * suite.
 * @return a test suite.
 */
public static Test suite() {
     TestSuite suite= new TestSuite(ReductionTest.class);
     return suite;
   }

   //private final static int bitlen = 100;

   GenPolynomialRing<BigRational> fac;

   GenPolynomial<BigRational> a;
   GenPolynomial<BigRational> b;
   GenPolynomial<BigRational> c;
   GenPolynomial<BigRational> d;
   GenPolynomial<BigRational> e;

   List<GenPolynomial<BigRational>> L;
   PolynomialList<BigRational> F;
   PolynomialList<BigRational> G;

    //ReductionSeq<BigRational> red;
    //Reduction<BigRational> redpar;

   int rl = 4; 
   int kl = 10;
   int ll = 11;
   int el = 5;
   float q = 0.6f;

   protected void setUp() {
       a = b = c = d = e = null;
       fac = new GenPolynomialRing<BigRational>( new BigRational(0), rl );
       //red = new ReductionSeq<BigRational>();
       //redpar = new ReductionPar<BigRational>();
   }

   protected void tearDown() {
       a = b = c = d = e = null;
       fac = null;
       //red = null;
       //redpar = null;
   }


/**
 * Test rational coefficient polynomial parametric reduction.
 * 
 */
 public void testRatPolReduction() {

     RingFactory<BigRational> bi = new BigRational(0);
     GenPolynomialRing<BigRational> pr 
         = new GenPolynomialRing<BigRational>(bi,2, new String[] { "a", "b" } );
     GenPolynomialRing<GenPolynomial<BigRational>> fac 
        = new GenPolynomialRing<GenPolynomial<BigRational>>(pr,rl);

     CReductionSeq<BigRational> cred 
         = new CReductionSeq<BigRational>();

     GenPolynomial<GenPolynomial<BigRational>> a = fac.random(5, ll, el, q );
     while ( a.isZERO() ) {
         a = fac.random(kl, ll, el, q );
     }
     GenPolynomial<GenPolynomial<BigRational>> b = fac.random(5, ll, el, q );
     while ( b.isZERO() ) {
         b = fac.random(kl, ll, el, q );
     }
     GenPolynomial<GenPolynomial<BigRational>> g = fac.getZERO();

     Map.Entry<ExpVector,GenPolynomial<BigRational>> m = a.leadingMonomial();
     ExpVector e = m.getKey();
     GenPolynomial<BigRational> c = m.getValue();

     GenPolynomial<GenPolynomial<BigRational>> r = fac.getZERO();
     r = r.sum(c,e);

     GenPolynomial<GenPolynomial<BigRational>> w = a.reductum();

     ColorPolynomial<BigRational> p 
         = new ColorPolynomial<BigRational>(g,r,w); 
     //System.out.println("p = " + p);
     assertTrue("check(p) ", p.checkInvariant());
     assertTrue("deter(p) ", p.isDetermined());
     //System.out.println("cond != 0: " + p.getConditionNonZero());
     //System.out.println("cond == 0: " + p.getConditionZero());

     p = new ColorPolynomial<BigRational>(r,g,w); 
     //System.out.println("p = " + p);
     assertTrue("check(p) ", p.checkInvariant());
     assertFalse("deter(p) ", p.isDetermined());
     //System.out.println("cond != 0: " + p.getConditionNonZero());
     //System.out.println("cond == 0: " + p.getConditionZero());

     p = new ColorPolynomial<BigRational>(r,w,g); 
     //System.out.println("p = " + p);
     assertTrue("check(p) ", p.checkInvariant());
     assertTrue("deter(p) ", p.isDetermined());
     //System.out.println("cond != 0: " + p.getConditionNonZero());
     //System.out.println("cond == 0: " + p.getConditionZero());

     p = new ColorPolynomial<BigRational>(w,g,g); 
     //System.out.println("p = " + p);
     assertTrue("check(p) ", p.checkInvariant());
     assertFalse("deter(p) ", p.isDetermined());
     assertTrue("p == 0 ", p.isZERO());
     //System.out.println("cond != 0: " + p.getConditionNonZero());
     //System.out.println("cond == 0: " + p.getConditionZero());

     List<GenPolynomial<BigRational>> i 
        = new ArrayList<GenPolynomial<BigRational>>();
     Ideal<BigRational> id = new Ideal<BigRational>(pr,i); 
     List<ColorPolynomial<BigRational>> cp 
        = new ArrayList<ColorPolynomial<BigRational>>();
     ColoredSystem<BigRational> s 
        = new ColoredSystem<BigRational>(id,cp);
     //System.out.println("s = " + s);

     assertTrue("isDetermined ", s.isDetermined()); 
     assertTrue("checkInvariant ", s.checkInvariant()); 

     List<ColoredSystem<BigRational>> CS 
         = new ArrayList<ColoredSystem<BigRational>>();
     CS.add(s);
     //System.out.println("CS = " + CS);
     List<ColoredSystem<BigRational>> CSp = CS; 

     //System.out.println("\na = " + a);
     //System.out.println("b = " + b + "\n");

     CS = cred.determine(CS,a);
     //System.out.println("CS = " + CS);
     for ( ColoredSystem<BigRational> x : CS ) {
         assertTrue("isDetermined ", x.isDetermined()); 
         assertTrue("checkInvariant ", x.checkInvariant()); 
     }

     CS = cred.determine(CS,a);
     //System.out.println("CS = " + CS);
     for ( ColoredSystem<BigRational> x : CS ) {
         assertTrue("isDetermined ", x.isDetermined()); 
         assertTrue("checkInvariant ", x.checkInvariant()); 
     }

     List<GenPolynomial<GenPolynomial<BigRational>>> L;
     L = new ArrayList<GenPolynomial<GenPolynomial<BigRational>>>();
     L.add(a);
     L.add(b);

     //System.out.println("determine ---------------------------------------------");
     CSp = cred.determine(CSp,L);
     //System.out.println("CS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" + CSp);
     //System.out.println("++++++++++++++++++++++++++++++++++++++");
     for ( ColoredSystem<BigRational> x : CSp ) {
         assertTrue("isDetermined ", x.isDetermined()); 
         assertTrue("checkInvariant ", x.checkInvariant()); 
         //System.out.println("condition == 0: " + x.getConditionZero());
         //System.out.println("condition != 0: " + x.getConditionNonZero());
         //System.out.println("polynomial list: " + x.getPolynomialList());
         //System.out.println("++++++++++++++++++++++++++++++++++++++");
     }

     ColorPolynomial<BigRational> q, h;
     for ( ColoredSystem<BigRational> x : CSp ) {
         int k = x.S.size();
         for ( int j = 0; j < k; j++ ) {
             p = x.S.get(j);
             for ( int l = j+1; l < k; l++ ) {
                 q = x.S.get(l);
                 h = cred.SPolynomial(p,q);
                 //System.out.println("spol(a,b) = " + h);
                 boolean t = cred.isNormalform( x.S, h );
                 //System.out.println("isNF(spol(a,b)) = " + t);
                 h = cred.normalform( x.S, h );
                 //System.out.println("NF(spol(a,b)) = " + h);
                 t = cred.isNormalform( x.S, h );
                 //System.out.println("isNF(NF(spol(a,b))) = " + t);
                 assertTrue("isNF(NF(spol(a,b))) ", t); 
             }
         }
     }

    ComprehensiveGroebnerBaseSeq<BigRational> cgb = 
        new ComprehensiveGroebnerBaseSeq<BigRational>(cred,bi);

    System.out.println("isGB(L) = " + cgb.isGB(L));


 }

}
