package com.duyp.architecture.clean.redux.app.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duyp.architecture.clean.redux.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;


/**
 * Created by kosh20111 on 10/7/2015 10:42 PM
 */
public class ViewHelper {

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getPrimaryColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryTextColor(@NonNull final Context context) {
        return getColorAttr(context, android.R.attr.textColorPrimary);
    }

    @ColorInt
    public static int getSecondaryTextColor(@NonNull final Context context) {
        return getColorAttr(context, android.R.attr.textColorSecondary);
    }

    @ColorInt
    public static int getTertiaryTextColor(@NonNull final Context context) {
        return getColorAttr(context, android.R.attr.textColorTertiary);
    }

    @ColorInt
    public static int getAccentColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getIconColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.icon_color);
    }

    @ColorInt
    public static int getWindowBackground(@NonNull final Context context) {
        return getColorAttr(context, android.R.attr.windowBackground);
    }

    @ColorInt
    public static int getListDivider(@NonNull final Context context) {
        return getColorAttr(context, R.attr.dividerColor);
    }

    @ColorInt
    public static int getCardBackground(@NonNull final Context context) {
        return getColorAttr(context, R.attr.card_background);
    }

    @ColorInt
    public static int getPatchAdditionColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.patch_addition);
    }

    @ColorInt
    public static int getPatchDeletionColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.patch_deletion);
    }

    @ColorInt
    public static int getPatchRefColor(@NonNull final Context context) {
        return getColorAttr(context, R.attr.patch_ref);
    }

    @ColorInt
    private static int getColorAttr(@NonNull final Context context, final int attr) {
        final Resources.Theme theme = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }

    public static int toPx(@NonNull final Context context, final int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void tintDrawable(@NonNull final Drawable drawable, @ColorInt final int color) {
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static Drawable getDrawableSelector(final int normalColor, final int pressedColor) {
        return new RippleDrawable(ColorStateList.valueOf(pressedColor), getRippleMask(normalColor), getRippleMask(normalColor));
    }

    @NonNull
    private static Drawable getRippleMask(final int color) {
        final float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, 3);
        final RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        final ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    @NonNull
    private static StateListDrawable getStateListDrawable(final int normalColor, final int pressedColor) {
        final StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(pressedColor));
        states.addState(new int[]{}, new ColorDrawable(normalColor));
        return states;
    }

    public static ColorStateList textSelector(final int normalColor, final int pressedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }

    private static boolean isTablet(@NonNull final Resources resources) {
        return (resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isTablet(@NonNull final Context context) {
        return context != null && isTablet(context.getResources());
    }

    public static boolean isLandscape(@NonNull final Resources resources) {
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public static Rect getLayoutPosition(@NonNull final View view) {
        final Rect myViewRect = new Rect();
        view.getGlobalVisibleRect(myViewRect);
        return myViewRect;
    }

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static String getTransitionName(@NonNull final View view) {
        return !TextUtils.isEmpty(view.getTransitionName()) ? view.getTransitionName() : null;
    }

    @SuppressWarnings("WeakerAccess")
    public static void showKeyboard(@NonNull final View v, @NonNull final Context activity) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    public static void showKeyboard(@NonNull final View v) {
        showKeyboard(v, v.getContext());
    }

    public static void hideKeyboard(@NonNull final View view) {
        final InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @ColorInt
    public static int generateTextColor(final int background) {
        return getContrastColor(background);
    }

    @ColorInt
    private static int getContrastColor(@ColorInt final int color) {
        final double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }

    //public static boolean isEllipsed(@NonNull TextView textView) {
    //    Layout layout = textView.getLayout();
    //    if (layout != null) {
    //        int lines = layout.getLineCount();
    //        if (lines > 0) {
    //            return IntStream.range(0, lines).anyMatch(line -> layout.getEllipsisCount(line) > 0);
    //        }
    //    }
    //    return false;
    //}

    @NonNull
    public static TextView getTabTextView(@NonNull final TabLayout tabs, final int tabIndex) {
        return (TextView) (((LinearLayout) ((LinearLayout) tabs.getChildAt(0)).getChildAt(tabIndex)).getChildAt(1));
    }
}
