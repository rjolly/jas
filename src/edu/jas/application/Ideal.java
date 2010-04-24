/*
 * $Id$
 */

package edu.jas.application;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.jas.gb.ExtendedGB;
import edu.jas.gb.GroebnerBaseAbstract;
import edu.jas.gb.GroebnerBasePartial;
import edu.jas.gb.GroebnerBaseSeqPairSeq;
import edu.jas.gb.Reduction;
import edu.jas.gb.ReductionSeq;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenPolynomialRing;
import edu.jas.poly.OptimizedPolynomialList;
import edu.jas.poly.PolynomialList;
import edu.jas.poly.PolyUtil;
import edu.jas.poly.TermOrderOptimization;
import edu.jas.poly.TermOrder;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.RingFactory;
import edu.jas.structure.NotInvertibleException;
import edu.jas.ufd.SquarefreeAbstract;
import edu.jas.ufd.SquarefreeFactory;
import edu.jas.ufd.FactorAbstract;
import edu.jas.ufd.FactorFactory;


/**
 * Ideal implements some methods for ideal arithmetic, for example intersection
 * and quotient.
 * @author Heinz Kredel
 */
public class Ideal<C extends GcdRingElem<C>> implements Comparable<Ideal<C>>, Serializable, Cloneable {


    private static final Logger logger = Logger.getLogger(Ideal.class);


    private final boolean debug = true || logger.isDebugEnabled();


    /**
     * The data structure is a PolynomialList.
     */
    protected PolynomialList<C> list;


    /**
     * Indicator if list is a Groebner Base.
     */
    protected boolean isGB;


    /**
     * Indicator if test has been performed if this is a Groebner Base.
     */
    protected boolean testGB;


    /**
     * Indicator if list has optimized term order.
     */
    protected boolean isTopt;


    /**
     * Groebner base engine.
     */
    protected final GroebnerBaseAbstract<C> bb;


    /**
     * Reduction engine.
     */
    protected final Reduction<C> red;


    /**
     * Squarefree decomposition engine.
     */
    protected final SquarefreeAbstract<C> engine;


    /**
     * Constructor.
     * @param ring polynomial ring
     */
    public Ideal(GenPolynomialRing<C> ring) {
        this(ring, new ArrayList<GenPolynomial<C>>());
    }


    /**
     * Constructor.
     * @param ring polynomial ring
     * @param F list of polynomials
     */
    public Ideal(GenPolynomialRing<C> ring, List<GenPolynomial<C>> F) {
        this(new PolynomialList<C>(ring, F));
    }


    /**
     * Constructor.
     * @param ring polynomial ring
     * @param F list of polynomials
     * @param gb true if F is known to be a Groebner Base, else false
     */
    public Ideal(GenPolynomialRing<C> ring, List<GenPolynomial<C>> F, boolean gb) {
        this(new PolynomialList<C>(ring, F), gb);
    }


    /**
     * Constructor.
     * @param ring polynomial ring
     * @param F list of polynomials
     * @param gb true if F is known to be a Groebner Base, else false
     * @param topt true if term order is optimized, else false
     */
    public Ideal(GenPolynomialRing<C> ring, List<GenPolynomial<C>> F, boolean gb, boolean topt) {
        this(new PolynomialList<C>(ring, F), gb, topt);
    }


    /**
     * Constructor.
     * @param list polynomial list
     */
    public Ideal(PolynomialList<C> list) {
        this(list, false);
    }


    /**
     * Constructor.
     * @param list polynomial list
     * @param bb Groebner Base engine
     * @param red Reduction engine
     */
    public Ideal(PolynomialList<C> list, GroebnerBaseAbstract<C> bb, Reduction<C> red) {
        this(list, false, bb, red);
    }


    /**
     * Constructor.
     * @param list polynomial list
     * @param gb true if list is known to be a Groebner Base, else false
     */
    public Ideal(PolynomialList<C> list, boolean gb) {
        this(list, gb, new GroebnerBaseSeqPairSeq<C>(), new ReductionSeq<C>());
    }


    /**
     * Constructor.
     * @param list polynomial list
     * @param gb true if list is known to be a Groebner Base, else false
     * @param topt true if term order is optimized, else false
     */
    public Ideal(PolynomialList<C> list, boolean gb, boolean topt) {
        this(list, gb, topt, new GroebnerBaseSeqPairSeq<C>(), new ReductionSeq<C>());
    }


    /**
     * Constructor.
     * @param list polynomial list
     * @param gb true if list is known to be a Groebner Base, else false
     * @param bb Groebner Base engine
     * @param red Reduction engine
     */
    public Ideal(PolynomialList<C> list, boolean gb, GroebnerBaseAbstract<C> bb, Reduction<C> red) {
        this(list, gb, false, bb, red);
    }


    /**
     * Constructor.
     * @param list polynomial list
     * @param gb true if list is known to be a Groebner Base, else false
     * @param topt true if term order is optimized, else false
     * @param bb Groebner Base engine
     * @param red Reduction engine
     */
    public Ideal(PolynomialList<C> list, boolean gb, boolean topt, GroebnerBaseAbstract<C> bb,
            Reduction<C> red) {
        if (list == null || list.list == null) {
            throw new IllegalArgumentException("list and list.list may not be null");
        }
        this.list = list;
        this.isGB = gb;
        this.isTopt = topt;
        this.testGB = (gb ? true : false); // ??
        this.bb = bb;
        this.red = red;
        this.engine = SquarefreeFactory.<C> getImplementation(list.ring.coFac);
    }


    /**
     * Clone this.
     * @return a copy of this.
     */
    @Override
    public Ideal<C> clone() {
        return new Ideal<C>(list.clone(), isGB, isTopt, bb, red);
    }


    /**
     * Get the List of GenPolynomials.
     * @return list.list
     */
    public List<GenPolynomial<C>> getList() {
        return list.list;
    }


    /**
     * Get the GenPolynomialRing.
     * @return list.ring
     */
    public GenPolynomialRing<C> getRing() {
        return list.ring;
    }


    /**
     * String representation of the ideal.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return list.toString();
    }


    /**
     * Get a scripting compatible string representation.
     * @return script compatible representation for this Element.
     * @see edu.jas.structure.Element#toScript()
     */
    public String toScript() {
        // Python case
        return list.toScript();
    }


    /**
     * Comparison with any other object. Note: If both ideals are not Groebner
     * Bases, then false may be returned even the ideals are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object b) {
        if (!(b instanceof Ideal)) {
            logger.warn("equals no Ideal");
            return false;
        }
        Ideal<C> B = null;
        try {
            B = (Ideal<C>) b;
        } catch (ClassCastException ignored) {
            return false;
        }
        //if ( isGB && B.isGB ) {
        //   return list.equals( B.list ); requires also monic polys
        //} else { // compute GBs ?
        return this.contains(B) && B.contains(this);
        //}
    }


    /**
     * Ideal list comparison.
     * @param L other Ideal.
     * @return compareTo() of polynomial lists.
     */
    public int compareTo(Ideal<C> L) {
        return list.compareTo(L.list);
    }


    /**
     * Hash code for this ideal.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int h;
        h = list.hashCode();
        if (isGB) {
            h = h << 1;
        }
        if (testGB) {
            h += 1;
        }
        return h;
    }


    /**
     * Test if ZERO ideal.
     * @return true, if this is the 0 ideal, else false
     */
    public boolean isZERO() {
        return list.isZERO();
    }


    /**
     * Test if ONE ideal.
     * @return true, if this is the 1 ideal, else false
     */
    public boolean isONE() {
        return list.isONE();
    }


    /**
     * Optimize the term order.
     */
    public void doToptimize() {
        if (isTopt) {
            return;
        }
        list = TermOrderOptimization.<C> optimizeTermOrder(list);
        isTopt = true;
        if (isGB) {
            isGB = false;
            doGB();
        }
        return;
    }


    /**
     * Test if this is a Groebner base.
     * @return true, if this is a Groebner base, else false
     */
    public boolean isGB() {
        if (testGB) {
            return isGB;
        }
        logger.warn("isGB computing");
        isGB = bb.isGB(getList());
        testGB = true;
        return isGB;
    }


    /**
     * Do Groebner Base. compute the Groebner Base for this ideal.
     */
    public void doGB() {
        if (isGB && testGB) {
            return;
        }
        //logger.warn("GB computing");
        List<GenPolynomial<C>> G = getList();
        logger.info("GB computing = " + G);
        G = bb.GB(G);
        if (isTopt) {
            List<Integer> perm = ((OptimizedPolynomialList<C>) list).perm;
            list = new OptimizedPolynomialList<C>(perm, getRing(), G);
        } else {
            list = new PolynomialList<C>(getRing(), G);
        }
        isGB = true;
        testGB = true;
        return;
    }


    /**
     * Groebner Base. Get a Groebner Base for this ideal.
     * @return GB(this)
     */
    public Ideal<C> GB() {
        if (isGB) {
            return this;
        }
        doGB();
        return this;
    }


    /**
     * Ideal containment. Test if B is contained in this ideal. Note: this is
     * eventually modified to become a Groebner Base.
     * @param B ideal
     * @return true, if B is contained in this, else false
     */
    public boolean contains(Ideal<C> B) {
        if (B == null || B.isZERO()) {
            return true;
        }
        return contains(B.getList());
    }


    /**
     * Ideal containment. Test if b is contained in this ideal. Note: this is
     * eventually modified to become a Groebner Base.
     * @param b polynomial
     * @return true, if b is contained in this, else false
     */
    public boolean contains(GenPolynomial<C> b) {
        if (b == null || b.isZERO()) {
            return true;
        }
        if (this.isONE()) {
            return true;
        }
        if (this.isZERO()) {
            return false;
        }
        if (!isGB) {
            doGB();
        }
        GenPolynomial<C> z;
        z = red.normalform(getList(), b);
        if (z == null || z.isZERO()) {
            return true;
        }
        return false;
    }


    /**
     * Ideal containment. Test if each b in B is contained in this ideal. Note:
     * this is eventually modified to become a Groebner Base.
     * @param B list of polynomials
     * @return true, if each b in B is contained in this, else false
     */
    public boolean contains(List<GenPolynomial<C>> B) {
        if (B == null || B.size() == 0) {
            return true;
        }
        if (this.isONE()) {
            return true;
        }
        if (!isGB) {
            doGB();
        }
        for (GenPolynomial<C> b : B) {
            if (b == null) {
                continue;
            }
            GenPolynomial<C> z = red.normalform(getList(), b);
            if (!z.isZERO()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Summation. Generators for the sum of ideals. Note: if both ideals are
     * Groebner bases, a Groebner base is returned.
     * @param B ideal
     * @return ideal(this+B)
     */
    public Ideal<C> sum(Ideal<C> B) {
        if (B == null || B.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return B;
        }
        int s = getList().size() + B.getList().size();
        List<GenPolynomial<C>> c;
        c = new ArrayList<GenPolynomial<C>>(s);
        c.addAll(getList());
        c.addAll(B.getList());
        Ideal<C> I = new Ideal<C>(getRing(), c, false);
        if (isGB && B.isGB) {
            I.doGB();
        }
        return I;
    }


    /**
     * Summation. Generators for the sum of ideal and a polynomial. Note: if
     * this ideal is a Groebner base, a Groebner base is returned.
     * @param b polynomial
     * @return ideal(this+{b})
     */
    public Ideal<C> sum(GenPolynomial<C> b) {
        if (b == null || b.isZERO()) {
            return this;
        }
        int s = getList().size() + 1;
        List<GenPolynomial<C>> c;
        c = new ArrayList<GenPolynomial<C>>(s);
        c.addAll(getList());
        c.add(b);
        Ideal<C> I = new Ideal<C>(getRing(), c, false);
        if (isGB) {
            I.doGB();
        }
        return I;
    }


    /**
     * Product. Generators for the product of ideals. Note: if both ideals are
     * Groebner bases, a Groebner base is returned.
     * @param B ideal
     * @return ideal(this*B)
     */
    public Ideal<C> product(Ideal<C> B) {
        if (B == null || B.isZERO()) {
            return B;
        }
        if (this.isZERO()) {
            return this;
        }
        int s = getList().size() * B.getList().size();
        List<GenPolynomial<C>> c;
        c = new ArrayList<GenPolynomial<C>>(s);
        for (GenPolynomial<C> p : getList()) {
            for (GenPolynomial<C> q : B.getList()) {
                q = p.multiply(q);
                c.add(q);
            }
        }
        Ideal<C> I = new Ideal<C>(getRing(), c, false);
        if (isGB && B.isGB) {
            I.doGB();
        }
        return I;
    }


    /**
     * Intersection. Generators for the intersection of ideals.
     * @param B ideal
     * @return ideal(this \cap B), a Groebner base
     */
    public Ideal<C> intersect(Ideal<C> B) {
        if (B == null || B.isZERO()) { // (0)
            return B;
        }
        if (this.isZERO()) {
            return this;
        }
        int s = getList().size() + B.getList().size();
        List<GenPolynomial<C>> c;
        c = new ArrayList<GenPolynomial<C>>(s);
        List<GenPolynomial<C>> a = getList();
        List<GenPolynomial<C>> b = B.getList();

        GenPolynomialRing<C> tfac = getRing().extend(1);
        // term order is also adjusted
        for (GenPolynomial<C> p : a) {
            p = p.extend(tfac, 0, 1L); // t*p
            c.add(p);
        }
        for (GenPolynomial<C> p : b) {
            GenPolynomial<C> q = p.extend(tfac, 0, 1L);
            GenPolynomial<C> r = p.extend(tfac, 0, 0L);
            p = r.subtract(q); // (1-t)*p
            c.add(p);
        }
        logger.warn("intersect computing GB");
        List<GenPolynomial<C>> g = bb.GB(c);
        if (debug) {
            logger.debug("intersect GB = " + g);
        }
        Ideal<C> E = new Ideal<C>(tfac, g, true);
        Ideal<C> I = E.intersect(getRing());
        return I;
    }


    /**
     * Intersection. Generators for the intersection of a ideal with a
     * polynomial ring. The polynomial ring of this ideal must be a contraction
     * of R and the TermOrder must be an elimination order.
     * @param R polynomial ring
     * @return ideal(this \cap R)
     */
    public Ideal<C> intersect(GenPolynomialRing<C> R) {
        if (R == null) {
            throw new IllegalArgumentException("R may not be null");
        }
        int d = getRing().nvar - R.nvar;
        if (d <= 0) {
            return this;
        }
        //GenPolynomialRing<C> tfac = getRing().contract(d);
        //if ( ! tfac.equals( R ) ) { // check ?
        //   throw new RuntimeException("contract(this) != R");
        //}
        List<GenPolynomial<C>> h;
        h = new ArrayList<GenPolynomial<C>>(getList().size());
        for (GenPolynomial<C> p : getList()) {
            Map<ExpVector, GenPolynomial<C>> m = null;
            m = p.contract(R);
            if (debug) {
                logger.debug("intersect contract m = " + m);
            }
            if (m.size() == 1) { // contains one power of variables
                for (ExpVector e : m.keySet()) {
                    if (e.isZERO()) {
                        h.add(m.get(e));
                    }
                }
            }
        }
        return new Ideal<C>(R, h, isGB, isTopt);
    }


    /**
     * Eliminate. Generators for the intersection of a ideal with a polynomial
     * ring. The polynomial rings must have variable names.
     * @param R polynomial ring
     * @return ideal(this \cap R)
     */
    public Ideal<C> eliminate(GenPolynomialRing<C> R) {
        if (R == null) {
            throw new IllegalArgumentException("R may not be null");
        }
        String[] ename = R.getVars();
        Ideal<C> I = eliminate(ename);
        return I.intersect(R);
    }


    /**
     * Eliminate. Preparation of generators for the intersection of a ideal with
     * a polynomial ring.
     * @param ename variables for the elimination ring.
     * @return ideal(this) in K[ename,{vars \ ename}])
     */
    public Ideal<C> eliminate(String[] ename) {
        //System.out.println("ename = " + Arrays.toString(ename));
        if (ename == null) {
            throw new IllegalArgumentException("ename may not be null");
        }
        String[] aname = getRing().getVars();
        //System.out.println("aname = " + Arrays.toString(aname));
        if (aname == null) {
            throw new IllegalArgumentException("aname may not be null");
        }

        GroebnerBasePartial<C> bbp = new GroebnerBasePartial<C>(bb, null);
        String[] rname = bbp.remainingVars(aname, ename);
        //System.out.println("rname = " + Arrays.toString(rname));

        PolynomialList<C> Pl = bbp.elimPartialGB(getList(), rname, ename); // reversed!
        //System.out.println("Pl = " + Pl);
        if (debug) {
            logger.debug("elimnation GB = " + Pl);
        }
        //Ideal<C> I = new Ideal<C>( E, G, true );
        Ideal<C> I = new Ideal<C>(Pl, true);
        //System.out.println("elim, I = " + I);
        return I; //.intersect(R);
    }


    /**
     * Permutation of variables for elimination.
     * @param aname variables for the full polynomial ring.
     * @param ename variables for the elimination ring, subseteq aname.
     * @return perm({vars \ ename},ename)
     * @deprecated use the one in GroebnerBasePartial
     */
    @Deprecated
    public List<Integer> getPermutation(String[] aname, String[] ename) {
        if (aname == null || ename == null) {
            throw new IllegalArgumentException("aname or ename may not be null");
        }
        List<Integer> perm = new ArrayList<Integer>(aname.length);
        // add ename permutation
        for (int i = 0; i < ename.length; i++) {
            int j = indexOf(ename[i], aname);
            if (j < 0) {
                throw new IllegalArgumentException("ename not contained in aname");
            }
            perm.add(j);
        }
        //System.out.println("perm_low = " + perm);
        // add aname \ ename permutation
        for (int i = 0; i < aname.length; i++) {
            if (!perm.contains(i)) {
                perm.add(i);
            }
        }
        //System.out.println("perm_all = " + perm);
        // reverse permutation indices
        int n1 = aname.length - 1;
        List<Integer> perm1 = new ArrayList<Integer>(aname.length);
        for (Integer k : perm) {
            perm1.add(n1 - k);
        }
        perm = perm1;
        //System.out.println("perm_inv = " + perm1);
        Collections.reverse(perm);
        //System.out.println("perm_rev = " + perm);
        return perm;
    }


    /**
     * Index of s in A.
     * @param s search string
     * @param A string array
     * @return i if s == A[i] for some i, else -1.
     * @deprecated use the one in GroebnerBasePartial
     */
    @Deprecated
    public int indexOf(String s, String[] A) {
        for (int i = 0; i < A.length; i++) {
            if (s.equals(A[i])) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Quotient. Generators for the ideal quotient.
     * @param h polynomial
     * @return ideal(this : h), a Groebner base
     */
    public Ideal<C> quotient(GenPolynomial<C> h) {
        if (h == null) { // == (0)
            return this;
        }
        if (h.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        List<GenPolynomial<C>> H;
        H = new ArrayList<GenPolynomial<C>>(1);
        H.add(h);
        Ideal<C> Hi = new Ideal<C>(getRing(), H, true);

        Ideal<C> I = this.intersect(Hi);

        List<GenPolynomial<C>> Q;
        Q = new ArrayList<GenPolynomial<C>>(I.getList().size());
        for (GenPolynomial<C> q : I.getList()) {
            q = q.divide(h); // remainder == 0
            Q.add(q);
        }
        return new Ideal<C>(getRing(), Q, true /*false?*/);
    }


    /**
     * Quotient. Generators for the ideal quotient.
     * @param H ideal
     * @return ideal(this : H), a Groebner base
     */
    public Ideal<C> quotient(Ideal<C> H) {
        if (H == null) { // == (0)
            return this;
        }
        if (H.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        Ideal<C> Q = null;
        for (GenPolynomial<C> h : H.getList()) {
            Ideal<C> Hi = this.quotient(h);
            if (Q == null) {
                Q = Hi;
            } else {
                Q = Q.intersect(Hi);
            }
        }
        return Q;
    }


    /**
     * Infinite quotient. Generators for the infinite ideal quotient.
     * @param h polynomial
     * @return ideal(this : h<sup>s</sup>), a Groebner base
     */
    public Ideal<C> infiniteQuotientRab(GenPolynomial<C> h) {
        if (h == null) { // == (0)
            return this;
        }
        if (h.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        Ideal<C> I = this.GB(); // should be already
        List<GenPolynomial<C>> a = I.getList();
        List<GenPolynomial<C>> c;
        c = new ArrayList<GenPolynomial<C>>(a.size() + 1);

        GenPolynomialRing<C> tfac = getRing().extend(1);
        // term order is also adjusted
        for (GenPolynomial<C> p : a) {
            p = p.extend(tfac, 0, 0L); // p
            c.add(p);
        }
        GenPolynomial<C> q = h.extend(tfac, 0, 1L);
        GenPolynomial<C> r = tfac.getONE(); // h.extend( tfac, 0, 0L );
        GenPolynomial<C> hs = q.subtract(r); // 1 - t*h // (1-t)*h
        c.add(hs);
        logger.warn("infiniteQuotientRab computing GB");
        List<GenPolynomial<C>> g = bb.GB(c);
        if (debug) {
            logger.debug("infiniteQuotientRab GB = " + g);
        }
        Ideal<C> E = new Ideal<C>(tfac, g, true);
        Ideal<C> Is = E.intersect(getRing());
        return Is;
    }


    /**
     * Infinite quotient. Generators for the infinite ideal quotient.
     * @param h polynomial
     * @return ideal(this : h<sup>s</sup>), a Groebner base
     */
    public Ideal<C> infiniteQuotient(GenPolynomial<C> h) {
        if (h == null) { // == (0)
            return this;
        }
        if (h.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        int s = 0;
        Ideal<C> I = this.GB(); // should be already
        GenPolynomial<C> hs = h;
        Ideal<C> Is = I;

        boolean eq = false;
        while (!eq) {
            Is = I.quotient(hs);
            Is = Is.GB(); // should be already
            logger.info("infiniteQuotient s = " + s);
            eq = Is.contains(I); // I.contains(Is) always
            if (!eq) {
                I = Is;
                s++;
                // hs = hs.multiply( h );
            }
        }
        return Is;
    }


    /**
     * Radical membership test.
     * @param h polynomial
     * @return true if h is contained in the radical of ideal(this), else false.
     */
    public boolean isRadicalMember(GenPolynomial<C> h) {
        if (h == null) { // == (0)
            return true;
        }
        if (h.isZERO()) {
            return true;
        }
        if (this.isZERO()) {
            return true;
        }
        Ideal<C> x = infiniteQuotientRab(h);
        if (debug) {
            logger.debug("infiniteQuotientRab = " + x);
        }
        return x.isONE();
    }


    /**
     * Infinite quotient. Generators for the infinite ideal quotient.
     * @param h polynomial
     * @return ideal(this : h<sup>s</sup>), a Groebner base
     */
    public Ideal<C> infiniteQuotientOld(GenPolynomial<C> h) {
        if (h == null) { // == (0)
            return this;
        }
        if (h.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        int s = 0;
        Ideal<C> I = this.GB(); // should be already
        GenPolynomial<C> hs = h;

        boolean eq = false;
        while (!eq) {
            Ideal<C> Is = I.quotient(hs);
            Is = Is.GB(); // should be already
            logger.debug("infiniteQuotient s = " + s);
            eq = Is.contains(I); // I.contains(Is) always
            if (!eq) {
                I = Is;
                s++;
                hs = hs.multiply(h);
            }
        }
        return I;
    }


    /**
     * Infinite Quotient. Generators for the ideal infinite quotient.
     * @param H ideal
     * @return ideal(this : H<sup>s</sup>), a Groebner base
     */
    public Ideal<C> infiniteQuotient(Ideal<C> H) {
        if (H == null) { // == (0)
            return this;
        }
        if (H.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        Ideal<C> Q = null;
        for (GenPolynomial<C> h : H.getList()) {
            Ideal<C> Hi = this.infiniteQuotient(h);
            if (Q == null) {
                Q = Hi;
            } else {
                Q = Q.intersect(Hi);
            }
        }
        return Q;
    }


    /**
     * Infinite Quotient. Generators for the ideal infinite quotient.
     * @param H ideal
     * @return ideal(this : H<sup>s</sup>), a Groebner base
     */
    public Ideal<C> infiniteQuotientRab(Ideal<C> H) {
        if (H == null) { // == (0)
            return this;
        }
        if (H.isZERO()) {
            return this;
        }
        if (this.isZERO()) {
            return this;
        }
        Ideal<C> Q = null;
        for (GenPolynomial<C> h : H.getList()) {
            Ideal<C> Hi = this.infiniteQuotientRab(h);
            if (Q == null) {
                Q = Hi;
            } else {
                Q = Q.intersect(Hi);
            }
        }
        return Q;
    }


    /**
     * Normalform for element.
     * @param h polynomial
     * @return normalform of h with respect to this
     */
    public GenPolynomial<C> normalform(GenPolynomial<C> h) {
        if (h == null) {
            return h;
        }
        if (h.isZERO()) {
            return h;
        }
        if (this.isZERO()) {
            return h;
        }
        GenPolynomial<C> r;
        r = red.normalform(list.list, h);
        return r;
    }


    /**
     * Normalform for list of elements.
     * @param L polynomial list
     * @return list of normalforms of the elements of L with respect to this
     */
    public List<GenPolynomial<C>> normalform(List<GenPolynomial<C>> L) {
        if (L == null) {
            return L;
        }
        if (L.size() == 0) {
            return L;
        }
        if (this.isZERO()) {
            return L;
        }
        List<GenPolynomial<C>> M = new ArrayList<GenPolynomial<C>>(L.size());
        for (GenPolynomial<C> h : L) {
            GenPolynomial<C> r = normalform(h);
            if (r != null && !r.isZERO()) {
                M.add(r);
            }
        }
        return M;
    }


    /**
     * Inverse for element modulo this ideal.
     * @param h polynomial
     * @return inverse of h with respect to this, if defined
     */
    public GenPolynomial<C> inverse(GenPolynomial<C> h) {
        if (h == null || h.isZERO()) {
            throw new RuntimeException(" zero not invertible");
        }
        if (this.isZERO()) {
            throw new NotInvertibleException(" zero ideal");
        }
        List<GenPolynomial<C>> F = new ArrayList<GenPolynomial<C>>(1 + list.list.size());
        F.add(h);
        F.addAll(list.list);
        //System.out.println("F = " + F);
        ExtendedGB<C> x = bb.extGB(F);
        List<GenPolynomial<C>> G = x.G;
        //System.out.println("G = " + G);
        GenPolynomial<C> one = null;
        int i = -1;
        for (GenPolynomial<C> p : G) {
            i++;
            if (p == null) {
                continue;
            }
            if (p.isUnit()) {
                one = p;
                break;
            }
        }
        if (one == null) {
            throw new NotInvertibleException(" h = " + h);
        }
        List<GenPolynomial<C>> row = x.G2F.get(i); // != -1
        GenPolynomial<C> g = row.get(0);
        if (g == null || g.isZERO()) {
            throw new NotInvertibleException(" h = " + h);
        }
        // adjust g to get g*h == 1
        GenPolynomial<C> f = g.multiply(h);
        GenPolynomial<C> k = red.normalform(list.list, f);
        if (!k.isONE()) {
            C lbc = k.leadingBaseCoefficient();
            lbc = lbc.inverse();
            g = g.multiply(lbc);
        }
        if (debug) {
            //logger.info("inv G = " + G);
            //logger.info("inv G2F = " + x.G2F);
            //logger.info("inv row "+i+" = " + row);
            //logger.info("inv h = " + h);
            //logger.info("inv g = " + g);
            //logger.info("inv f = " + f);
            f = g.multiply(h);
            k = red.normalform(list.list, f);
            logger.info("inv k = " + k);
            if (!k.isUnit()) {
                throw new NotInvertibleException(" k = " + k);
            }
        }
        return g;
    }


    /**
     * Test if element is a unit modulo this ideal.
     * @param h polynomial
     * @return true if h is a unit with respect to this, else false
     */
    public boolean isUnit(GenPolynomial<C> h) {
        if (h == null || h.isZERO()) {
            return false;
        }
        if (this.isZERO()) {
            return false;
        }
        List<GenPolynomial<C>> F = new ArrayList<GenPolynomial<C>>(1 + list.list.size());
        F.add(h);
        F.addAll(list.list);
        List<GenPolynomial<C>> G = bb.GB(F);
        for (GenPolynomial<C> p : G) {
            if (p == null) {
                continue;
            }
            if (p.isUnit()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Radical approximation. Squarefree generators for the ideal.
     * @return squarefree(this), a Groebner base
     */
    public Ideal<C> squarefree() {
        if (this.isZERO()) {
            return this;
        }
        Ideal<C> R = this;
        Ideal<C> Rp = null;
        List<GenPolynomial<C>> li, ri;
        while (true) {
            li = R.getList();
            ri = new ArrayList<GenPolynomial<C>>(li); //.size() );
            for (GenPolynomial<C> h : li) {
                GenPolynomial<C> r = engine.squarefreePart(h);
                ri.add(r);
            }
            Rp = new Ideal<C>(R.getRing(), ri, false);
            Rp.doGB();
            if (R.equals(Rp)) {
                break;
            }
            R = Rp;
        }
        return R;
    }


    /**
     * Ideal common zero test.
     * @return -1, 0 or 1 if dimension(this) &eq; -1, 0 or &ge; 1.
     */
    public int commonZeroTest() {
        if (this.isZERO()) {
            return 1;
        }
        if (!isGB) {
            doGB();
        }
        if (this.isONE()) {
            return -1;
        }
        if (this.list.ring.nvar <= 0) {
            return -1;
        }
        //int uht = 0;
        Set<Integer> v = new HashSet<Integer>(); // for non reduced GBs
        // List<GenPolynomial<C>> Z = this.list.list;
        for (GenPolynomial<C> p : getList()) {
            ExpVector e = p.leadingExpVector();
            if (e == null) {
                continue;
            }
            int[] u = e.dependencyOnVariables();
            if (u == null) {
                continue;
            }
            if (u.length == 1) {
                //uht++;
                v.add(u[0]);
            }
        }
        if (this.list.ring.nvar == v.size()) {
            return 0;
        }
        return 1;
    }


    /**
     * Univariate head term degrees.
     * @return a list of the degrees of univariate head terms.
     */
    public List<Long> univariateDegrees() {
        List<Long> ud = new ArrayList<Long>();
        if (this.isZERO()) {
            return ud;
        }
        if (!isGB) {
            doGB();
        }
        if (this.isONE()) {
            return ud;
        }
        if (this.list.ring.nvar <= 0) {
            return ud;
        }
        //int uht = 0;
        Map<Integer,Long> v = new TreeMap<Integer,Long>(); // for non reduced GBs
        for (GenPolynomial<C> p : getList()) {
            ExpVector e = p.leadingExpVector();
            if (e == null) {
                continue;
            }
            int[] u = e.dependencyOnVariables();
            if (u == null) {
                continue;
            }
            if (u.length == 1) {
                //uht++;
                Long d = v.get(u[0]);
                if ( d == null ) {
                    v.put(u[0],e.getVal(u[0]));
                }
            }
        }
        for ( int i = 0; i < this.list.ring.nvar; i++ ) {
            Long d = v.get(i);
            ud.add(d);
        }
        //Collections.reverse(ud);
        return ud;
    }


    /**
     * Ideal dimension.
     * @return a dimension container (dim,maxIndep,list(maxIndep),vars).
     */
    public Dimension dimension() {
        int t = commonZeroTest();
        Set<Integer> S = new HashSet<Integer>();
        Set<Set<Integer>> M = new HashSet<Set<Integer>>();
        if (t <= 0) {
            return new Dimension(t, S, M, this.list.ring.getVars());
        }
        int d = 0;
        Set<Integer> U = new HashSet<Integer>();
        for (int i = 0; i < this.list.ring.nvar; i++) {
            U.add(i);
        }
        M = dimension(S, U, M);
        for (Set<Integer> m : M) {
            int dp = m.size();
            if (dp > d) {
                d = dp;
                S = m;
            }
        }
        return new Dimension(d, S, M, this.list.ring.getVars());
    }


    /**
     * Ideal dimension.
     * @param S is a set of independent variables.
     * @param U is a set of variables of unknown status.
     * @param M is a list of maximal sets of independent variables.
     * @return a list of maximal sets of independent variables, eventually
     *         containing S.
     */
    protected Set<Set<Integer>> dimension(Set<Integer> S, Set<Integer> U, Set<Set<Integer>> M) {
        Set<Set<Integer>> MP = M;
        Set<Integer> UP = new HashSet<Integer>(U);
        for (Integer j : U) {
            UP.remove(j);
            Set<Integer> SP = new HashSet<Integer>(S);
            SP.add(j);
            if (!containsHT(SP, getList())) {
                MP = dimension(SP, UP, MP);
            }
        }
        boolean contained = false;
        for (Set<Integer> m : MP) {
            if (m.containsAll(S)) {
                contained = true;
                break;
            }
        }
        if (!contained) {
            MP.add(S);
        }
        return MP;
    }


    /**
     * Ideal head term containment test.
     * @param G list of polynomials.
     * @param H index set.
     * @return true, if the vaiables of the head terms of each polynomial in G
     *         are contained in H, else false.
     */
    protected boolean containsHT(Set<Integer> H, List<GenPolynomial<C>> G) {
        Set<Integer> S = null;
        for (GenPolynomial<C> p : G) {
            if (p == null) {
                continue;
            }
            ExpVector e = p.leadingExpVector();
            if (e == null) {
                continue;
            }
            int[] v = e.dependencyOnVariables();
            if (v == null) {
                continue;
            }
            // System.out.println("v = " + Arrays.toString(v));
            if (S == null) { // revert indices
                S = new HashSet<Integer>(H.size());
                int r = e.length() - 1;
                for (Integer i : H) {
                    S.add(r - i);
                }
            }
            if (contains(v, S)) { // v \subset S
                return true;
            }
        }
        return false;
    }


    /**
     * Set containment. is v \subset H.
     * @param v index array.
     * @param H index set.
     * @return true, if each element of v is contained in H, else false .
     */
    protected boolean contains(int[] v, Set<Integer> H) {
        for (int i = 0; i < v.length; i++) {
            if (!H.contains(v[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * Construct univariate polynomials of minimal degree in all variables 
     * in zero dimensional ideal(G).
     * @return list of univariate polynomial of minimal degree in each variable in ideal(G)
     */
    public List<GenPolynomial<C>> constructUnivariate() {
        List<GenPolynomial<C>> univs = new ArrayList<GenPolynomial<C>>();
        for ( int i = list.ring.nvar-1; i >= 0; i-- ) {
            GenPolynomial<C> u = constructUnivariate(i);
            univs.add(u);
        }
        return univs;
    }


    /**
     * Construct univariate polynomial of minimal degree in variable i 
     * in zero dimensional ideal(G).
     * @param i variable index.
     * @return univariate polynomial of minimal degree in variable i in ideal(G)
     */
    public GenPolynomial<C> constructUnivariate(int i) {
        return constructUnivariate(i,getList());
    }


    /**
     * Construct univariate polynomial of minimal degree in variable i 
     * of a zero dimensional ideal(G).
     * @param i variable index.
     * @param G list of polynomials, a monic reduced Gr&ouml;bner base of a zero dimensional ideal.
     * @return univariate polynomial of minimal degree in variable i in ideal(G)
     */
    public GenPolynomial<C> constructUnivariate(int i, List<GenPolynomial<C>> G) {
        if ( G == null || G.size() == 0 ) {
            throw new IllegalArgumentException("G may not be null or empty");
        }
        List<Long> ud = univariateDegrees();
        int ll = 0;
        Long di = ud.get(i);
        if ( di != null ) {
            ll = (int) (long)di;
        } else {
            throw new IllegalArgumentException("ideal(G) not zero dimensional");
        }
        GenPolynomialRing<C> pfac = G.get(0).ring;
        RingFactory<C> cfac = pfac.coFac;
        String var = pfac.getVars()[pfac.nvar-1-i];
        GenPolynomialRing<C> ufac = new GenPolynomialRing<C>(cfac,1,new TermOrder(TermOrder.INVLEX),new String[]{ var });
        GenPolynomial<C> pol = ufac.getZERO();

        GenPolynomialRing<C> cpfac = new GenPolynomialRing<C>(cfac,ll,new TermOrder(TermOrder.INVLEX));
        GenPolynomialRing<GenPolynomial<C>> rfac = new GenPolynomialRing<GenPolynomial<C>>(cpfac,pfac);
        GenPolynomial<GenPolynomial<C>> P = rfac.getZERO();
        for ( int k = 0; k < ll; k++ ) {
            GenPolynomial<GenPolynomial<C>> Pp = rfac.univariate(i,k);
            GenPolynomial<C> cp = cpfac.univariate(cpfac.nvar-1-k);
            Pp = Pp.multiply(cp);
            P = P.sum(Pp);
        }
        GenPolynomial<C> X;
        GenPolynomial<C> XP;
        // solve system of linear equations for the coefficients of the univariate polynomial
        List<GenPolynomial<C>> ls;
        int z = -1;
        do {
            //System.out.println("ll  = " + ll);
            GenPolynomial<GenPolynomial<C>> Pp = rfac.univariate(i,ll);
            GenPolynomial<C> cp = cpfac.univariate(cpfac.nvar-1-ll);
            Pp = Pp.multiply(cp);
            P = P.sum(Pp);
            X = pfac.univariate(i,ll);
            XP = red.normalform(G,X);
            GenPolynomial<GenPolynomial<C>> XPp = PolyUtil.<C> toRecursive(rfac,XP);
            GenPolynomial<GenPolynomial<C>> XPs = XPp.sum(P);
            ls = new ArrayList<GenPolynomial<C>>(XPs.getMap().values());
            ls = red.irreducibleSet(ls);
            //System.out.println("ls = " + ls);
            Ideal<C> L = new Ideal<C>(cpfac,ls,true);
            z = L.commonZeroTest();
            if ( z != 0 ) {
                ll++;
                cpfac = cpfac.extend(1); 
                rfac = new GenPolynomialRing<GenPolynomial<C>>(cpfac,pfac);
                P = PolyUtil.<C> extendCoefficients(rfac,P,0,0L);
                XPp = PolyUtil.<C> extendCoefficients(rfac,XPp,0,1L);
                P = P.sum(XPp);
            }
        } while ( z != 0 ); // && ll <= 5 && !XP.isZERO()
        // construct result polynomial
        pol = ufac.univariate(0,ll);
        for ( GenPolynomial<C> pc : ls ) {
            ExpVector e = pc.leadingExpVector();
            if (e == null) {
                continue;
            }
            int[] v = e.dependencyOnVariables();
            if (v == null || v.length == 0) {
                continue;
            }
            int vi = v[0];
            C tc = pc.trailingBaseCoefficient();
            tc = tc.negate();
            GenPolynomial<C> pi = ufac.univariate(0,ll-1-vi);
            pi = pi.multiply(tc);
            pol = pol.sum(pi);
        }
        if ( logger.isInfoEnabled() ) {
            logger.info("univ pol = " + pol);
        }
        return pol;
    }


    /**
     * Zero dimensional ideal decompostition.
     * See algorithm DIRGZD of BGK 1984.
     * @param G list of polynomials, a monic reduced Gr&ouml;bner base of a zero dimensional ideal.
     * @return intersection of ideals G_i with cup( ideal(G_i) ) = ideal (G)
     */
    public List<IdealWithUniv<C>> zeroDimDecomposition() {
        List<IdealWithUniv<C>> dec = new ArrayList<IdealWithUniv<C>>();
        if ( this.isZERO() ) {
            return dec;
        }
        IdealWithUniv<C> iwu = new IdealWithUniv<C>(this,new ArrayList<GenPolynomial<C>>());
        dec.add(iwu);
        if ( this.isONE() ) {
            return dec;
        }
        FactorAbstract<C> ufd = FactorFactory.<C> getImplementation(list.ring.coFac);

        for ( int i = list.ring.nvar-1; i >= 0; i-- ) {
            List<IdealWithUniv<C>> part = new ArrayList<IdealWithUniv<C>>();
            for ( IdealWithUniv<C> id : dec ) {
                 GenPolynomial<C> u = id.ideal.constructUnivariate(i);
                 SortedMap<GenPolynomial<C>,java.lang.Long> facs = ufd.baseFactors(u);
                 if ( facs.size() == 1 && facs.get(facs.firstKey()) == 1L ) {
                     List<GenPolynomial<C>> iup = new ArrayList<GenPolynomial<C>>();
                     iup.addAll(id.upolys);
                     iup.add(u);
                     IdealWithUniv<C> Ipu = new IdealWithUniv<C>(id.ideal,iup);
                     part.add(Ipu);
                     continue; // irreducible
                 }
                 for ( GenPolynomial<C> p : facs.keySet() ) {
                     // make p multivariate
                     GenPolynomial<C> pm = id.ideal.list.ring.parse( p.toString() );
                     System.out.println("pm = " + pm);
                     Ideal<C> Ip = id.ideal.sum( pm );
                     List<GenPolynomial<C>> iup = new ArrayList<GenPolynomial<C>>();
                     iup.addAll(id.upolys);
                     iup.add(p);
                     IdealWithUniv<C> Ipu = new IdealWithUniv<C>(Ip,iup);
                     part.add(Ipu);
                 }
            }
            dec = part;
            part = new ArrayList<IdealWithUniv<C>>();
        }
        return dec;
    }

}


/**
 * Container for Ideals together wir univariate polynomials.
 * @author Heinz Kredel
 */
class IdealWithUniv<C extends GcdRingElem<C>> implements Serializable {


    /**
     * The ideal.
     */
    public final Ideal<C> ideal;


    /**
     * The list of univariate polynomials.
     */
    public final List<GenPolynomial<C>> upolys;


    /**
     * Constructor not for use.
     */
    protected IdealWithUniv() {
        throw new IllegalArgumentException("do not use this constructor");
    }


    /**
     * Constructor.
     * @param id the ideal
     * @param up the list of univaraite polynomials
     */
    protected IdealWithUniv(Ideal<C> id, List<GenPolynomial<C>> up) {
        ideal = id;
        upolys = up;
    }


    /**
     * String representation of the ideal.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ideal.toString() + "\nunivariate polynomials: " + upolys.toString();
    }


    /**
     * Get a scripting compatible string representation.
     * @return script compatible representation for this Element.
     * @see edu.jas.structure.Element#toScript()
     */
    public String toScript() {
        // Python case
        return ideal.toScript() +  ",  " + upolys.toString();
    }

}
