package com.michal.debski;

import com.michal.debski.loader.Loader;
import com.michal.debski.loader.mdMesh;
import com.michal.debski.utilities.Color;

public class Model extends Loader
{
    private Color color = new Color(1.f);

    public Model(String path)
    {
        super(path);
    }

    public Model(PrimitiveType type)
    {
        super(type);
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void Render(Shader shader)
    {
        for(mdMesh mesh : super.meshes)
        {
            shader.setVec4("color", color.r, color.g, color.b, color.a);
            mesh.Render(shader);
        }
    }
}
