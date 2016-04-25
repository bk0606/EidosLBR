package com.eidos.simulatableAPI.utils;

public class Mathf {
    public static final float PI = (float) Math.PI;
    public static final float RAD_TO_DEG = 180f / PI;

    public static float[] DblToFlt(double[] doubles) {
        float[] floats = new float[doubles.length];
        for (int i = 0, len = doubles.length; i < len; i++) {
            floats[i] = (float) doubles[i];
        }
        return floats;
    }
}