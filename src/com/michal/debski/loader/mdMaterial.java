package com.michal.debski.loader;

public class mdMaterial
{
    public mdMaterial(float initValue)
    {
        this.ambient = initValue;
        this.diffuse = initValue;
        this.specular = initValue;
    }

    public mdMaterial(float ambient, float diffuse, float specular)
    {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public float ambient;
    public float diffuse;
    public float specular;
}
