package com.solarexsoft.tableview;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solarexsoft.solarextableview.adapter.BaseTableAdapter;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 08/08/2017
 *    Desc:
 * </pre>
 */

public class MatrixTableAdapter<T> extends BaseTableAdapter {

    private static final int WIDTH = 110;
    private static final int HEIGHT = 32;
    private final Context mContext;
    private T[][] table;

    private final int width;
    private final int height;

    public MatrixTableAdapter(Context context) {
        this(context, null);
    }

    public MatrixTableAdapter(Context context, T[][] table) {
        mContext = context;
        Resources r = context.getResources();

        width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH, r
                .getDisplayMetrics()));
        height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT, r
                .getDisplayMetrics()));
        this.table = table;
    }

    @Override
    public int getRowCount() {
        return table.length - 1;
    }

    @Override
    public int getColumnCount() {
        return table[0].length - 1;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = new TextView(mContext);
            ((TextView)convertView).setGravity(Gravity.CENTER);
        }
        ((TextView)convertView).setText(table[row+1][column+1].toString());
        return convertView;
    }

    @Override
    public int getWidth(int column) {
        return width;
    }

    @Override
    public int getHeight(int row) {
        return height;
    }

    @Override
    public int getItemViewType(int row, int column) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
