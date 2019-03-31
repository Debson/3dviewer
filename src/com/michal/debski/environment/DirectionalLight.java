package com.michal.debski.environment;

import com.michal.debski.ShaderManager;
import com.michal.debski.utilities.Colour;
import org.joml.Vector3f;

public class DirectionalLight extends Light
{
    public DirectionalLight(Vector3f position)
    {
        super(position);
        super.on();
    }

    public DirectionalLight(Vector3f position, Colour color)
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
            Colour color = getColor();
            ShaderManager.GetShader().setVec3("dirLight.color", color.r * getTransform().getScale().x,
                    color.g * getTransform().getScale().y,
                    color.b * getTransform().getScale().z);
        }
    }
}
