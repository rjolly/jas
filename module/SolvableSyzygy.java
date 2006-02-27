/*
 * $Id$
 */

package edu.jas.module;

import java.util.List;

import edu.jas.poly.GenSolvablePolynomial;
import edu.jas.poly.PolynomialList;
import edu.jas.structure.RingElem;

import edu.jas.vector.ModuleList;


/**
 * Syzygy interface for solvable polynomials.
 * Defines syzygy computations and tests.
 * @author Heinz Kredel
 */

public interface SolvableSyzygy<C extends RingElem<C>> {


    /**
     * Left syzygy for left Groebner base.
     * @param C coefficient type.
     * @param F a Groebner base.
     * @return leftSyz(F), a basis for the left module of syzygies for F.
     */
    public List<List<GenSolvablePolynomial<C>>> 
           leftZeroRelations(List<GenSolvablePolynomial<C>> F);


    /**
     * Left syzygy for left Groebner base.
     * @param C coefficient type.
     * @param modv number of module variables.
     * @param F a Groebner base.
     * @return leftSyz(F), a basis for the left module of syzygies for F.
     */
    public List<List<GenSolvablePolynomial<C>>>
           leftZeroRelations(int modv, 
                             List<GenSolvablePolynomial<C>> F);


    /**
     * Left syzygy for left module Groebner base.
     * @param C coefficient type.
     * @param M a Groebner base.
     * @return leftSyz(M), a basis for the left module of syzygies for M.
     */
    public ModuleList<C> 
           leftZeroRelations(ModuleList<C> M);


    /**
     * Test if left syzygy.
     * @param C coefficient type.
     * @param Z list of sysygies.
     * @param F a polynomial list.
     * @return true, if Z is a list of left syzygies for F, else false.
     */
    public boolean 
           isLeftZeroRelation(List<List<GenSolvablePolynomial<C>>> Z, 
                              List<GenSolvablePolynomial<C>> F);


    /**
     * Test if right syzygy.
     * @param C coefficient type.
     * @param Z list of sysygies.
     * @param F a polynomial list.
     * @return true, if Z is a list of right syzygies for F, else false.
     */
    public boolean 
           isRightZeroRelation(List<List<GenSolvablePolynomial<C>>> Z, 
                               List<GenSolvablePolynomial<C>> F);


    /**
     * Test if left sysygy of modules
     * @param C coefficient type.
     * @param Z list of sysygies.
     * @param F a module list.
     * @return true, if Z is a list of left syzygies for F, else false.
     */
    public boolean 
           isLeftZeroRelation(ModuleList<C> Z, 
                              ModuleList<C> F);


    /**
     * Test if right sysygy of modules
     * @param C coefficient type.
     * @param Z list of sysygies.
     * @param F a module list.
     * @return true, if Z is a list of right syzygies for F, else false.
     */
    public boolean 
           isRightZeroRelation(ModuleList<C> Z, 
                               ModuleList<C> F);


    /**
     * Resolution of a module.
     * Only with direct GBs.
     * @param C coefficient type.
     * @param M a module list of a Groebner basis.
     * @return a resolution of M.
     */
    public List<SolvResPart<C>> 
           resolution(ModuleList<C> M);


    /**
     * Resolution of a polynomial list.
     * Only with direct GBs.
     * @param C coefficient type.
     * @param F a polynomial list of a Groebner basis.
     * @return a resolution of F.
     */
    public List // <SolvResPart<C>|SolvResPolPart<C>> 
           resolution(PolynomialList<C> F);


    /**
     * Resolution of a module.
     * @param C coefficient type.
     * @param M a module list of an arbitrary basis.
     * @return a resolution of M.
     */
    public List<SolvResPart<C>> 
           resolutionArbitrary(ModuleList<C> M);


    /**
     * Resolution of a polynomial list.
     * @param C coefficient type.
     * @param F a polynomial list of an arbitrary basis.
     * @return a resolution of F.
     */
    public List // <SolvResPart<C>|SolvResPolPart<C>> 
           resolutionArbitrary(PolynomialList<C> F);


    /**
     * Left syzygy module from arbitrary base.
     * @param C coefficient type.
     * @param F a solvable polynomial list.
     * @return syz(F), a basis for the module of left syzygies for F.
     */
    public List<List<GenSolvablePolynomial<C>>> 
           leftZeroRelationsArbitrary(List<GenSolvablePolynomial<C>> F);


    /**
     * Left syzygy module from arbitrary base.
     * @param C coefficient type.
     * @param modv number of module variables.
     * @param F a solvable polynomial list.
     * @return syz(F), a basis for the module of left syzygies for F.
     */
    public List<List<GenSolvablePolynomial<C>>> 
           leftZeroRelationsArbitrary(int modv, List<GenSolvablePolynomial<C>> F);


    /**
     * Left syzygy for arbitrary left module base.
     * @param C coefficient type.
     * @param M an arbitrary base.
     * @return leftSyz(M), a basis for the left module of syzygies for M.
     */
    public ModuleList<C> 
           leftZeroRelationsArbitrary(ModuleList<C> M);


    /**
     * Right syzygy module from arbitrary base.
     * @param C coefficient type.
     * @param F a solvable polynomial list.
     * @return syz(F), a basis for the module of right syzygies for F.
     */
    public List<List<GenSolvablePolynomial<C>>> 
           rightZeroRelationsArbitrary(List<GenSolvablePolynomial<C>> F);


    /**
     * Right syzygy module from arbitrary base.
     * @param C coefficient type.
     * @param modv number of module variables.
     * @param F a solvable polynomial list.
     * @return syz(F), a basis for the module of right syzygies for F.
     */
    public List<List<GenSolvablePolynomial<C>>> 
           rightZeroRelationsArbitrary(int modv, List<GenSolvablePolynomial<C>> F);

}
