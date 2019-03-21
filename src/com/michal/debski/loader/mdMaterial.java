package com.michal.debski.loader;

import org.joml.Vector3f;

public class mdMaterial
{
    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;
    public float shininess;

    public mdMaterial(float initValue)
    {
        this.ambient = new Vector3f(initValue);
        this.diffuse = new Vector3f(initValue);
        this.specular = new Vector3f(initValue);
        this.shininess = 16.f;
    }

    public mdMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess)
    {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
