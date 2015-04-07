package org.andengine.entity;

import org.andengine.util.adt.list.IList;
import org.andengine.util.algorithm.sort.InsertionSorter;

import java.util.Comparator;
import java.util.List;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:08:56 - 06.08.2010
 */
public class ZIndexSorter extends InsertionSorter<IEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static ZIndexSorter INSTANCE;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Comparator<IEntity> mZIndexComparator = new Comparator<IEntity>() {
		@Override
		public int compare(final IEntity pEntityA, final IEntity pEntityB) {
			return pEntityA.getZIndex() - pEntityB.getZIndex();
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	private ZIndexSorter() {

	}

	public static ZIndexSorter getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ZIndexSorter();
		}
		return INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void sort(final IEntity[] pEntities) {
		this.sort(pEntities, this.mZIndexComparator);
	}

	public void sort(final IEntity[] pEntities, final int pStart, final int pEnd) {
		this.sort(pEntities, pStart, pEnd, this.mZIndexComparator);
	}

	public void sort(final List<IEntity> pEntities) {
		this.sort(pEntities, this.mZIndexComparator);
	}

	public void sort(final List<IEntity> pEntities, final int pStart, final int pEnd) {
		this.sort(pEntities, pStart, pEnd, this.mZIndexComparator);
	}

	public void sort(final IList<IEntity> pEntities) {
		this.sort(pEntities, this.mZIndexComparator);
	}

	public void sort(final IList<IEntity> pEntities, final int pStart, final int pEnd) {
		this.sort(pEntities, pStart, pEnd, this.mZIndexComparator);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}