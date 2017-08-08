package com.solarexsoft.solarextableview.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/08/2017
 *    Desc:
 * </pre>
 */

public interface TableAdapter {
    public static final int IGNORE_ITEM_VIEW_TYPE = -1;
    void registerDataSetObserver(DataSetObserver observer);
    void unregisterDataSetObserver(DataSetObserver observer);
    int getRowCount();
    int getColumnCount();
    View getView(int row, int column, View convertView, ViewGroup parent);
    int getWidth(int column);
    int getHeight(int row);
    int getItemViewType(int row,int column);
    int getViewTypeCount();
}
