package com.michal.debski.utilities;

import org.joml.Vector3f;

public class Transform
{
    private Vector3f position;
    private Vector3f scale;

    public Transform()
    {
        position = new Vector3f(0.f);
        scale = new Vector3f(1.f);
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }
}
