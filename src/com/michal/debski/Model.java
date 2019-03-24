package com.michal.debski;

import com.michal.debski.loader.Loader;
import com.michal.debski.loader.mdMesh;
import com.michal.debski.utilities.Color;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Vector;

public class Model extends Loader
{
    private Color color = new Color(1.f);
    private Matrix4f matrixModel;
    private Vector3f position = new Vector3f(0.f);
    private Vector3f scale = new Vector3f(1.f);

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

    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }

    public void setPosition(Vector3f position)
    {
       this.position = position;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void Render()
    {
        for(mdMesh mesh : super.meshes)
        {
            ShaderManager.GetShader().use();
            matrixModel = new Matrix4f().translate(position).scale(scale);
            ShaderManager.GetShader().setVec4("color", color.r, color.g, color.b, color.a);
            ShaderManager.GetShader().setMat4("model", matrixModel);
            mesh.Render();
        }
    }
}
