package com.solarexsoft.solarextableview;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.solarexsoft.solarextableview.adapter.TableAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 07/08/2017
 *    Desc:
 * </pre>
 */

public class TableView extends ViewGroup {
    private int currentX;
    private int currentY;

    private TableAdapter adapter;
    private int scrollX;
    private int scrollY;
    private int firstRow;
    private int firstColumn;
    private int[] widths;
    private int[] heights;

    private List<View> rowViewList;
    private List<View> columnViewList;
    private List<List<View>> bodyViewTable;

    private int rowCount;
    private int columnCount;
    private int width;
    private int height;

    private Recycler recycler;

    private TableAdapterDataSetObserver tableAdapterDataSetObserver;
    private boolean needRelayout;

    private int minVelocity;
    private int maxVelocity;

    private Flinger flinger;

    private VelocityTracker velocityTracker;

    private int touchSlop;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.rowViewList = new ArrayList<View>();
        this.columnViewList = new ArrayList<View>();
        this.bodyViewTable = new ArrayList<List<View>>();

        this.needRelayout = true;

        this.flinger = new Flinger(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        this.touchSlop = configuration.getScaledTouchSlop();
        this.minVelocity = configuration.getScaledMinimumFlingVelocity();
        this.maxVelocity = configuration.getScaledMaximumFlingVelocity();

        this.setWillNotDraw(false);
    }

    public TableAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TableAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(tableAdapterDataSetObserver);
        }
        this.adapter = adapter;
        tableAdapterDataSetObserver = new TableAdapterDataSetObserver();
        this.adapter.registerDataSetObserver(tableAdapterDataSetObserver);
        this.recycler = new Recycler(adapter.getViewTypeCount());
        scrollX = 0;
        scrollY = 0;
        firstColumn = 0;
        firstRow = 0;

        needRelayout = true;
        requestLayout();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = (int) ev.getRawX();
                currentY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(currentX - (int) ev.getRawX());
                int dy = Math.abs(currentY - (int) ev.getRawY());
                if (dx > touchSlop || dy > touchSlop) {
                    intercept = true;
                }
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!flinger.isFinished()) {
                    flinger.forceFinished();
                }
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                final int dx = currentX - x;
                final int dy = currentY - y;
                currentX = x;
                currentY = y;
                scrollBy(dx, dy);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                int velocityX = (int) velocityTracker.getXVelocity();
                int velocityY = (int) velocityTracker.getYVelocity();
                if (Math.abs(velocityX) > minVelocity || Math.abs(velocityY) > minVelocity) {
                    flinger.start(getActualScrollX(), getActualScrollY(), velocityX, velocityY,
                            getMaxScrollX(), getMaxScrollY());
                } else {
                    if (this.velocityTracker != null) {
                        this.velocityTracker.recycle();
                        this.velocityTracker = null;
                    }
                }
                break;

        }
        return true;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (needRelayout) {
            scrollX = x;
            firstColumn = 0;
            scrollY = y;
            firstRow = 0;
        } else {
            scrollBy(x - sumArray(widths, 1, firstColumn) - scrollX, y - sumArray(heights, 1,
                    firstRow) - scrollY);
        }
    }

    @Override
    public void scrollBy(@Px int x, @Px int y) {
        scrollX += x;
        scrollY += y;
        if (needRelayout) {
            return;
        }
        scrollBounds();
        if (scrollX == 0) {
            // no op
        } else if (scrollX > 0) {
            while (widths[firstColumn + 1] < scrollX) {
                if (!rowViewList.isEmpty()) {
                    removeLeft();
                }
                scrollX -= widths[firstColumn + 1];
                firstColumn++;
            }
            while (getFilledWidth() < width) {
                addRight();
            }
        } else {
            while (!rowViewList.isEmpty() && getFilledWidth() - widths[firstColumn + rowViewList
                    .size()] >= width) {
                removeRight();
            }
            if (rowViewList.isEmpty()) {
                while (scrollX < 0) {
                    firstColumn--;
                    scrollX += widths[firstColumn + 1];
                }
                while (getFilledWidth() < width) {
                    addRight();
                }
            } else {
                while (0 > scrollX) {
                    addLeft();
                    firstColumn--;
                    scrollX += widths[firstColumn + 1];
                }
            }
        }

        if (scrollY == 0) {
            // no op
        } else if (scrollY > 0) {
            while (heights[firstRow + 1] < scrollY) {
                if (!columnViewList.isEmpty()) {
                    removeTop();
                }
                scrollY -= heights[firstRow + 1];
                firstRow++;
            }
            while (getFilledHeight() < height) {
                addBottom();
            }
        } else {
            while (!columnViewList.isEmpty() && getFilledHeight() - heights[firstRow +
                    columnViewList.size()] >= height) {
                removeBottom();
            }
            if (columnViewList.isEmpty()) {
                while (scrollY < 0) {
                    firstRow--;
                    scrollY += heights[firstRow + 1];
                }
                while (getFilledHeight() < height) {
                    addBottom();
                }
            } else {
                while (0 > scrollY) {
                    addTop();
                    firstRow--;
                    scrollY += heights[firstRow + 1];
                }
            }
        }
        repositionViews();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private class TableAdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            needRelayout = true;
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            // nothing todo
        }
    }

    private class Flinger implements Runnable {
        private final Scroller scroller;

        private int lastX = 0;
        private int lastY = 0;

        Flinger(Context context) {
            scroller = new Scroller(context);
        }

        void start(int startX, int startY, int velocityX, int velocityY, int maxX, int maxY) {
            scroller.fling(startX, startY, velocityX, velocityY, 0, maxX, 0, maxY);
            lastX = startX;
            lastY = startY;
            post(this);
        }

        @Override
        public void run() {
            if (scroller.isFinished()) {
                return;
            }
            boolean more = scroller.computeScrollOffset();
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            int diffX = lastX - x;
            int diffY = lastY - y;
            if (diffX != 0 || diffY != 0) {
                scrollBy(diffX, diffY);
                lastX = x;
                lastY = y;
            }
            if (more) {
                post(this);
            }

        }

        boolean isFinished() {
            return scroller.isFinished();
        }

        void forceFinished() {
            if (!scroller.isFinished()) {
                scroller.forceFinished(true);
            }
        }
    }
}
