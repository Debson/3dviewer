package com.michal.debski.environment;

import com.michal.debski.utilities.Color;
import org.joml.Vector3f;

public class DirectionalLight extends Light
{
    public DirectionalLight(Vector3f position)
    {
        super(position);
        super.on();
    }

    public DirectionalLight(Vector3f position, Color color)
    {
        super(position, color);
        super.on();
    }
}
