package com.michal.debski.environment;

import com.michal.debski.utilities.Color;
import org.joml.Vector3f;

public class PointLight extends Light
{
    public PointLight(Vector3f position)
    {
        super(position);
        this.on();
    }

    public PointLight(Vector3f position, Color color)
    {
        super(position, color);
        this.on();
    }

}
