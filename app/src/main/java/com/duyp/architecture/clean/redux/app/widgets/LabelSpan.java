package com.duyp.architecture.clean.redux.app.widgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;

import java.util.Locale;


/**
 * adopted class from Android source code & modified to fit FastHub need.
 */

public class LabelSpan extends ReplacementSpan {
    private final TextPaint txtPaint = new TextPaint();
    private final FontMetricsInt fontMetrics = new FontMetricsInt();
    private final int color;
    private final SpanDimensions dims;

    private LabelSpan(final int color, @NonNull final SpanDimensions dims) {
        this.color = color;
        txtPaint.bgColor = color;
        this.dims = dims;
    }

    public LabelSpan(final int color) {
        this(color, new SpanDimensions() {
            @Override
            public int getPadding() {
                return 6;
            }

            @Override
            public int getPaddingExtraWidth() {
                return 0;
            }

            @Override
            public int getPaddingAfter() {
                return 0;
            }

            @Override
            public int getMaxWidth() {
                return 1000;//random number
            }

            @Override
            public float getRoundedCornerRadius() {
                return 5;
            }

            @Override
            public int getMarginTop() {
                return 8;
            }

            @Override
            public boolean isRtl() {
                return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;
            }
        });
    }

    @Override
    public int getSize(@NonNull final Paint paint, final CharSequence text, final int start, final int end, final FontMetricsInt fm) {
        setupFontMetrics(text, start, end, fm, paint);
        if (fm != null) {
            final int padding = dims.getPadding();
            final int margin = dims.getMarginTop();
            fm.ascent = Math.min(fm.top, fm.ascent - padding) - margin;
            fm.descent = Math.max(fm.bottom, padding);
            fm.top = fm.ascent;
            fm.bottom = fm.descent;
        }
        return measureWidth(txtPaint, text, start, end, dims.isRtl());
    }

    private int measureWidth(final Paint paint, final CharSequence text, final int start, final int end,
                             final boolean includePaddingAfter) {
        final int paddingW = dims.getPadding() + dims.getPaddingExtraWidth();
        final int maxWidth = dims.getMaxWidth();
        int w = (int) paint.measureText(text, start, end) + 2 * paddingW;
        if (includePaddingAfter) {
            w += dims.getPaddingAfter();
        }
        if (w > maxWidth) {
            w = maxWidth;
        }
        return w;
    }

    private void setupFontMetrics(final CharSequence text, final int start, final int end, final FontMetricsInt fm, final Paint p) {
        txtPaint.set(p);
        final CharacterStyle[] otherSpans = ((Spanned) text).getSpans(start, end, CharacterStyle.class);
        for (final CharacterStyle otherSpan : otherSpans) {
            otherSpan.updateDrawState(txtPaint);
        }
        txtPaint.setTextSize(p.getTextSize());
        if (fm != null) {
            txtPaint.getFontMetricsInt(fm);
        }
    }

    @Override
    public void draw(@NonNull final Canvas canvas, CharSequence text, int start, int end,
                     final float x, int top, int y, int bottom, @NonNull final Paint paint) {
        final int padding = dims.getPadding();
        final int paddingW = padding + dims.getPaddingExtraWidth();
        final int maxWidth = dims.getMaxWidth();
        setupFontMetrics(text, start, end, fontMetrics, paint);
        final int bgWidth = measureWidth(txtPaint, text, start, end, false);
        fontMetrics.top = Math.min(fontMetrics.top, fontMetrics.ascent - padding);
        fontMetrics.bottom = Math.max(fontMetrics.bottom, padding);
        top = y + fontMetrics.top - fontMetrics.bottom;
        bottom = y;
        y = bottom - fontMetrics.bottom;
        final boolean isRtl = dims.isRtl();
        final int paddingAfter = dims.getPaddingAfter();
        if (txtPaint.bgColor != 0) {
            final int prevColor = txtPaint.getColor();
            final Paint.Style prevStyle = txtPaint.getStyle();
            txtPaint.setColor(txtPaint.bgColor);
            txtPaint.setStyle(Paint.Style.FILL);
            final float left;
            if (isRtl) {
                left = x + paddingAfter;
            } else {
                left = x;
            }
            final float right = left + bgWidth;
            final RectF rect = new RectF(left, top, right, bottom);
            final float radius = dims.getRoundedCornerRadius();
            canvas.drawRoundRect(rect, radius, radius, txtPaint);
            txtPaint.setColor(prevColor);
            txtPaint.setStyle(prevStyle);
        }
        if (bgWidth == maxWidth) {
            text = TextUtils.ellipsize(text.subSequence(start, end).toString(), txtPaint, bgWidth - 2 * paddingW, TextUtils.TruncateAt.MIDDLE);
            start = 0;
            end = text.length();
        }
        float textX = x + paddingW;
        if (isRtl) {
            textX += paddingAfter;
        }
        if (color != Color.TRANSPARENT) txtPaint.setColor(ViewHelper.generateTextColor(color));
        canvas.drawText(text, start, end, textX, y, txtPaint);
    }

    public interface SpanDimensions {
        int getPadding();

        int getPaddingExtraWidth();

        int getPaddingAfter();

        int getMaxWidth();

        float getRoundedCornerRadius();

        int getMarginTop();

        boolean isRtl();
    }
}
