package com.duyp.architecture.clean.redux.app.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.GravityCompat;

import com.duyp.architecture.clean.redux.app.R;

import java.util.ArrayList;

public class AutoLinearLayout extends FrameLayout {

    private final static int HORIZONTAL = 0;
    private final static int VERTICAL = 1;
    private final ArrayList<ViewPosition> mListPositions = new ArrayList<>();
    private int mOrientation;
    private int mGravity = Gravity.TOP | GravityCompat.START;

    public AutoLinearLayout(final Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public AutoLinearLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public AutoLinearLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public AutoLinearLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (mOrientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        mListPositions.clear();
        if (mOrientation == VERTICAL)
            layoutVertical(left, top, right, bottom);
        else
            layoutHorizontal(left, top, right, bottom);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLinearLayout, defStyleAttr, defStyleRes);
        try {
            mOrientation = a.getInt(R.styleable.AutoLinearLayout_auto_orientation, HORIZONTAL);
            final int gravity = a.getInt(R.styleable.AutoLinearLayout_auto_gravity, -1);
            if (gravity >= 0) {
                setGravity(gravity);
            }
        } finally {
            a.recycle();
        }
    }

    private void measureHorizontal(final int widthMeasureSpec, final int heightMeasureSpec) {
        int wSize = MeasureSpec.getSize(widthMeasureSpec) - (getPaddingLeft() + getPaddingRight());

        //Scrollview case
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
            wSize = Integer.MAX_VALUE;

        final int count = getChildCount();
        int rowWidth = 0;
        int totalHeight = 0;
        int rowMaxHeight = 0;
        int childWidth;
        int childHeight;
        int maxRowWidth = getPaddingLeft() + getPaddingRight();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                //keep max height value stored
                rowMaxHeight = Math.max(rowMaxHeight, childHeight);

                //exceed max width start new row and update total height
                if (childWidth + rowWidth > wSize) {
                    totalHeight += rowMaxHeight;
                    maxRowWidth = Math.max(maxRowWidth, rowWidth);
                    rowWidth = childWidth;
                    rowMaxHeight = childHeight;
                } else {
                    rowWidth += childWidth;
                }
            }
        }
        //plus last child height and width
        if (rowWidth != 0) {
            maxRowWidth = Math.max(maxRowWidth, rowWidth);
            totalHeight += rowMaxHeight;
        }

        //set width to max value
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
            wSize = maxRowWidth + (getPaddingLeft() + getPaddingRight());

        setMeasuredDimension(resolveSize(wSize, widthMeasureSpec),
                resolveSize(totalHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec));
    }

    private void measureVertical(final int widthMeasureSpec, final int heightMeasureSpec) {
        int hSize = MeasureSpec.getSize(heightMeasureSpec) - (getPaddingTop() + getPaddingBottom());

        final int count = getChildCount();
        int columnHeight = 0;
        int totalWidth = 0, maxColumnHeight = 0;
        int columnMaxWidth = 0;
        int childWidth;
        int childHeight;

        //Scrollview case
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
            hSize = Integer.MAX_VALUE;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                //keep max width value stored
                columnMaxWidth = Math.max(columnMaxWidth, childWidth);

                //exceed max height start new column and update total width
                if (childHeight + columnHeight > hSize) {
                    totalWidth += columnMaxWidth;
                    maxColumnHeight = Math.max(maxColumnHeight, columnHeight);
                    columnHeight = childHeight;
                    columnMaxWidth = childWidth;
                } else {
                    columnHeight += childHeight;
                }
            }
        }
        //plus last child width
        if (columnHeight != 0) {
            maxColumnHeight = Math.max(maxColumnHeight, columnHeight);
            totalWidth += columnMaxWidth;
        }

        //set height to max value
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
            hSize = maxColumnHeight + (getPaddingTop() + getPaddingBottom());

        setMeasuredDimension(resolveSize(totalWidth + getPaddingRight() + getPaddingLeft(),
                widthMeasureSpec), resolveSize(hSize, heightMeasureSpec));
    }

    /**
     * Arranges the children in columns. Takes care about child margin, padding, gravity and
     * child layout gravity.
     *
     * @param left   parent left
     * @param top    parent top
     * @param right  parent right
     * @param bottom parent bottom
     */
    private void layoutVertical(final int left, final int top, final int right, final int bottom) {
        final int count = getChildCount();
        if (count == 0)
            return;

        final int width = right - getPaddingLeft() - left - getPaddingRight();
        final int height = bottom - getPaddingTop() - top - getPaddingBottom();

        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

        int totalHorizontal = getPaddingLeft() + getPaddingRight();
        int totalVertical = 0;
        int column = 0;
        int maxChildWidth = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != View.GONE) {
                //if child is not updated yet call measure
                if (child.getMeasuredHeight() == 0 || child.getMeasuredWidth() == 0)
                    child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                //if there is not enough space jump to another column
                if (childTop + childHeight + lp.topMargin + lp.bottomMargin > height + getPaddingTop()) {
                    //before change column update positions if the gravity is present
                    updateChildPositionVertical(height, totalVertical, column, maxChildWidth);
                    childTop = getPaddingTop();
                    childLeft += maxChildWidth;
                    maxChildWidth = 0;
                    column++;
                    totalVertical = 0;
                }

                childTop += lp.topMargin;
                mListPositions.add(new ViewPosition(childLeft, childTop, column));
                //check max child width
                final int currentWidth = childWidth + lp.leftMargin + lp.rightMargin;
                if (maxChildWidth < currentWidth)
                    maxChildWidth = currentWidth;
                //get ready for next child
                childTop += childHeight + lp.bottomMargin;
                totalVertical += childHeight + lp.topMargin + lp.bottomMargin;
            }
        }

        //update positions for last column
        updateChildPositionVertical(height, totalVertical, column, maxChildWidth);
        totalHorizontal += childLeft + maxChildWidth;
        //final update for horizontal gravities and layout views
        updateChildPositionHorizontal(width, totalHorizontal, column, 0);
        //mListPositions.clear();
    }

    /**
     * Arranges the children in rows. Takes care about child margin, padding, gravity and
     * child layout gravity. Analog to vertical.
     *
     * @param left   parent left
     * @param top    parent top
     * @param right  parent right
     * @param bottom parent bottom
     */
    private void layoutHorizontal(final int left, final int top, final int right, final int bottom) {
        final int count = getChildCount();
        if (count == 0)
            return;

        final int width = right - getPaddingLeft() - left - getPaddingRight();
        final int height = bottom - getPaddingTop() - top - getPaddingBottom();

        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

        int totalHorizontal = 0;
        int totalVertical = getPaddingTop() + getPaddingBottom();
        int row = 0;
        int maxChildHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child != null && child.getVisibility() != View.GONE) {
                if (child.getMeasuredHeight() == 0 || child.getMeasuredWidth() == 0)
                    child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                if (childLeft + childWidth + lp.leftMargin + lp.rightMargin > width + getPaddingLeft()) {
                    updateChildPositionHorizontal(width, totalHorizontal, row, maxChildHeight);
                    childLeft = getPaddingLeft();
                    childTop += maxChildHeight;
                    maxChildHeight = 0;
                    row++;
                    totalHorizontal = 0;
                }

                childLeft += lp.leftMargin;
                mListPositions.add(new ViewPosition(childLeft, childTop, row));

                final int currentHeight = childHeight + lp.topMargin + lp.bottomMargin;
                if (maxChildHeight < currentHeight)
                    maxChildHeight = currentHeight;

                childLeft += childWidth + lp.rightMargin;
                totalHorizontal += childWidth + lp.rightMargin + lp.leftMargin;
            }
        }

        updateChildPositionHorizontal(width, totalHorizontal, row, maxChildHeight);
        totalVertical += childTop + maxChildHeight;
        updateChildPositionVertical(height, totalVertical, row, 0);
        //mListPositions.clear();
    }

    /**
     * Updates children positions. Takes cares about gravity and layout gravity.
     * Finally layout children to parent if needed.
     *
     * @param parentHeight  parent parentHeight
     * @param totalSize     total vertical size used by children in a column
     * @param column        column number
     * @param maxChildWidth the biggest child width
     */
    private void updateChildPositionVertical(final int parentHeight, final int totalSize, final int column, final int maxChildWidth) {
        for (int i = 0; i < mListPositions.size(); i++) {
            final ViewPosition pos = mListPositions.get(i);
            final View child = getChildAt(i);
            //(android:gravity)
            //update children position inside parent layout
            if (mOrientation == HORIZONTAL || pos.position == column) {
                updateTopPositionByGravity(pos, parentHeight - totalSize, mGravity);
            }
            //(android:layout_gravity)
            //update children position inside their space
            if (mOrientation == VERTICAL && pos.position == column) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int size = maxChildWidth - child.getMeasuredWidth() - lp.leftMargin - lp.rightMargin;
                updateLeftPositionByGravity(pos, size, lp.gravity);
            }
            //update children into layout parent
            if (mOrientation == HORIZONTAL)
                layout(child, pos);
        }
    }

    /**
     * Updates children positions. Takes cares about gravity and layout gravity.
     * Finally layout children to parent if needed. Analog to vertical.
     *
     * @param parentWidth    parent parentWidth
     * @param totalSize      total horizontal size used by children in a row
     * @param row            row number
     * @param maxChildHeight the biggest child height
     */
    private void updateChildPositionHorizontal(final int parentWidth, final int totalSize, final int row, final int maxChildHeight) {
        for (int i = 0; i < mListPositions.size(); i++) {
            final ViewPosition pos = mListPositions.get(i);
            final View child = getChildAt(i);

            if (mOrientation == VERTICAL || pos.position == row) {
                updateLeftPositionByGravity(pos, parentWidth - totalSize, mGravity);
            }

            if (mOrientation == HORIZONTAL && pos.position == row) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int size = maxChildHeight - child.getMeasuredHeight() - lp.topMargin - lp.bottomMargin;
                updateTopPositionByGravity(pos, size, lp.gravity);
            }

            if (mOrientation == VERTICAL)
                layout(child, pos);
        }
    }

    private void updateLeftPositionByGravity(final ViewPosition pos, final int size, final int gravity) {
        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case GravityCompat.END:
                pos.left += (size > 0) ? size : 0;
                break;

            case Gravity.CENTER_HORIZONTAL:
                pos.left += ((size > 0) ? size : 0) / 2;
                break;
        }
    }

    private void updateTopPositionByGravity(final ViewPosition pos, final int size, final int gravity) {
        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.BOTTOM:
                pos.top += (size > 0) ? size : 0;
                break;

            case Gravity.CENTER_VERTICAL:
                pos.top += ((size > 0) ? size : 0) / 2;
                break;
        }
    }

    private void layout(final View child, final ViewPosition pos) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();

        if (mOrientation == HORIZONTAL)
            child.layout(pos.left, pos.top + lp.topMargin, pos.left + child.getMeasuredWidth(), pos.top +
                    child.getMeasuredHeight() + lp.topMargin);
        else
            child.layout(pos.left + lp.leftMargin, pos.top, pos.left + child.getMeasuredWidth() +
                    lp.leftMargin, pos.top + child.getMeasuredHeight());
    }

    /**
     * Describes how the child views are positioned. Defaults to GRAVITY_TOP. If
     * this layout has a VERTICAL orientation, this controls where all the child
     * views are placed if there is extra vertical space. If this layout has a
     * HORIZONTAL orientation, this controls the alignment of the children.
     *
     * @param gravity See {@link Gravity}
     */
    private void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= GravityCompat.START;
            }

            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }

            mGravity = gravity;
            requestLayout();
        }
    }

    public void setHorizontalGravity(final int horizontalGravity) {
        final int gravity = horizontalGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & ~GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) | gravity;
            requestLayout();
        }
    }

    public void setVerticalGravity(final int verticalGravity) {
        final int gravity = verticalGravity & Gravity.VERTICAL_GRAVITY_MASK;
        if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.VERTICAL_GRAVITY_MASK) | gravity;
            requestLayout();
        }
    }

    /**
     * Returns the current orientation.
     *
     * @return either {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Should the layout be a column or a row.
     *
     * @param orientation Pass HORIZONTAL or VERTICAL. Default value is HORIZONTAL.
     */
    public void setOrientation(final int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    /**
     * Helper inner class that stores child position
     */
    private static class ViewPosition {
        int left;
        int top;
        int position; //row or column

        ViewPosition(final int l, final int t, final int p) {
            left = l;
            top = t;
            position = p;
        }

        @Override
        public String toString() {
            return "left-" + left + " top" + top + " pos" + position;
        }
    }

}
