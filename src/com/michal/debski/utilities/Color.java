package com.michal.debski.utilities;

public class Color
{
    public float r, g, b, a;

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.f;
    }

    public Color(float initValue)
    {
        this.r = initValue;
        this.g = initValue;
        this.b = initValue;
        this.a = initValue;
    }
}
