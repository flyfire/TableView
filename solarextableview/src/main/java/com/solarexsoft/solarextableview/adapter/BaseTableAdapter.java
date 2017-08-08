package com.solarexsoft.solarextableview.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 08/08/2017
 *    Desc:
 * </pre>
 */

public abstract class BaseTableAdapter implements TableAdapter {
    private DataSetObservable mObservable = new DataSetObservable();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mObservable.unregisterAll();
    }

    public void notifyDataSetChanged() {
        mObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mObservable.notifyInvalidated();
    }

}
