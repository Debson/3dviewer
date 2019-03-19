package com.michal.debski;

import com.michal.debski.loader.Loader;
import com.michal.debski.loader.mdMesh;
import com.michal.debski.utilities.Color;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model extends Loader
{
    private Color color = new Color(1.f);
    private Matrix4f matrixModel;
    private Vector3f position = new Vector3f(0.f);

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

    public void setPosition(Vector3f position)
    {
       this.position = position;
    }

    public void Render()
    {
        for(mdMesh mesh : super.meshes)
        {
            matrixModel = new Matrix4f();
            matrixModel.translate(position);
            ShaderManager.GetShader().setVec4("color", color.r, color.g, color.b, color.a);
            ShaderManager.GetShader().setMat4("model", matrixModel);
            mesh.Render();
        }
    }
}
