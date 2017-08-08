package com.solarexsoft.solarextableview;

import android.view.View;

import java.util.Stack;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/08/2017
 *    Desc:
 * </pre>
 */

public class Recycler {
    private Stack<View>[] views;

    public Recycler(int typeCount) {
        views = new Stack[typeCount];
        for (int i = 0; i < typeCount; i++) {
            views[i] = new Stack<View>();
        }
    }

    public void addRecycledView(View view, int type) {
        views[type].push(view);
    }

    public View getRecycledView(int type) {
        try {
            return views[type].pop();
        } catch (Exception e) {
            return null;
        }

    }
}
