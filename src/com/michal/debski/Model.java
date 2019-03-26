package com.michal.debski;

import com.michal.debski.loader.Loader;
import com.michal.debski.loader.mdMesh;
import com.michal.debski.utilities.Color;
import com.michal.debski.utilities.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Vector;

public class Model extends Loader
{
    private Color color = new Color(1.f);
    private Matrix4f matrixModel;

    private Transform transform = new Transform();

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

    public Transform getTransform()
    {
        return transform;
    }

    public void Render()
    {
        ShaderManager.GetShader().use();
        matrixModel = new Matrix4f().translate(transform.getPosition()).scale(transform.getScale());
        ShaderManager.GetShader().setVec4("color", color.r, color.g, color.b, color.a);
        ShaderManager.GetShader().setMat4("model", matrixModel);
        for(mdMesh mesh : super.meshes)
        {
            mesh.Render();
        }
    }
}
