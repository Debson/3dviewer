package com.michal.debski;

import org.joml.Matrix4f;

public class Shader extends ShaderManager
{
    Shader(String vertexCode, String fragmentCode)
    {
        super(vertexCode, fragmentCode);

        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();
        super.use();
        super.setMat4("projection", projectionMatrix);
        super.setMat4("view", viewMatrix);
    }

    public void updateShaders()
    {
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();
        super.use();
        super.setMat4("projection", projectionMatrix);
        super.setMat4("view", viewMatrix);
    }

}
