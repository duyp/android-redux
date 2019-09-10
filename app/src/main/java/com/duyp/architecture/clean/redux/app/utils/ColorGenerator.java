package com.duyp.architecture.clean.redux.app.utils;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ColorGenerator {

    private static final ColorGenerator MATERIAL;
    private static final ColorGenerator MATERIAL_DARK;

    static {
        MATERIAL = create(Arrays.asList(
                0xff1976d2,
                0xff00838f,
                0xff512da8,
                0xff2e7d32,
                0xff283593,
                0xff01579b,
                0xffc51162,
                0xff6a1b9a,
                0xffd50000,
                0xff00695c
        ));
        MATERIAL_DARK = create(Arrays.asList(
                0xffffc107,
                0xffffc400,
                0xff2196f3,
                0xff2979ff,
                0xffa1887f,
                0xff4dd0e1,
                0xff00acc1,
                0xffe64a19,
                0xff9575cd,
                0xff66bb6a
        ));
    }

    private final List<Integer> colors;

    private ColorGenerator(final List<Integer> colorList) {
        colors = colorList;
    }

    private static ColorGenerator create(final List<Integer> colorList) {
        return new ColorGenerator(colorList);
    }

    @ColorInt
    public static int getColor(@NonNull final Context context, @Nullable final Object object) {
        //if (AppHelper.isNightMode(context.getResources())) {
        //    return MATERIAL_DARK.getColor(object);
        //} else {
        return MATERIAL.getColor(object);
        //}
    }

    private int getColor(@Nullable Object key) {
        key = Objects.toString(key, "default");
        return colors.get(Math.abs(key.hashCode()) % colors.size());
    }
}
