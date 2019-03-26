package com.michal.debski.environment;

import com.michal.debski.ShaderManager;
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

    public void Render(Vector3f cameraPosition)
    {
        super.renderLight();

        if(lightActive)
        {
            ShaderManager.GetShader().setVec3("viewPos", cameraPosition);
            ShaderManager.GetShader().setVec3("dirLight.direction", getTransform().getPosition());
            //ShaderManager.GetShader().setVec3("lightPos", position);
            ShaderManager.GetShader().setVec3("dirLight.color", color.r, color.g, color.b);
        }
    }
}
