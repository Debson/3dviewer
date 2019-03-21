package com.michal.debski.environment;

import com.michal.debski.Model;
import com.michal.debski.Shader;
import com.michal.debski.ShaderManager;
import com.michal.debski.loader.Loader;
import com.michal.debski.utilities.Color;
import org.joml.Vector3f;

public class Light
{

    // This should be the parent class. Child classes: directional light, point light
    // Light method render should be called before any drawing, so it will set boolean variable
    // that manages lighting in shader to true, so the scene will be drawn with lighting.
    protected Vector3f position;
    protected float strength;
    protected Color color;
    protected boolean lightActive = true;
    private boolean renderLightCube = true;
    private Model cube = new Model(Loader.PrimitiveType.Cube);

    public Light(Vector3f position)
    {
        this.position = position;
        this.color = new Color(1.f);
    }

    public Light(Vector3f position, Color color)
    {
        this.lightActive = true;
        this.position = position;
        this.color = color;
    }

    public void on()
    {
        this.lightActive = true;
    }

    public void off()
    {
        this.lightActive = false;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public void renderLightCube(boolean val)
    {
        this.renderLightCube = val;
    }

    public void Render(Vector3f cameraPosition)
    {
        renderLight();
        if(lightActive)
        {
            ShaderManager.GetShader().setVec3("light.direction", position);
            //ShaderManager.GetShader().setVec3("lightPos", position);
            ShaderManager.GetShader().setVec3("light.color", color.r, color.g, color.b);
        }
    }

    protected void renderLight()
    {
        if(renderLightCube)
        {
            ShaderManager.GetShader().setBool("lightActive", false);
            cube.setColor(color);
            cube.setPosition(position);
            cube.Render();
        }

        ShaderManager.GetShader().setBool("lightActive", lightActive);
    }
}
