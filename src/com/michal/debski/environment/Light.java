package com.michal.debski.environment;

import com.michal.debski.*;
import com.michal.debski.loader.Loader;
import com.michal.debski.utilities.Color;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;

public class Light
{
    // This should be the parent class. Child classes: directional light, point light
    // Light method render should be called before any drawing, so it will set boolean variable
    // that manages lighting in shader to true, so the scene will be drawn with lighting.
    protected Vector3f position;
    protected float strength;
    protected Color color;
    protected boolean lightActive = true;
    private boolean orbiting = false;
    private boolean renderLightCube = true;
    private boolean castShadows = true;
    private Model cube = new Model(Loader.PrimitiveType.Cube);
    private Shadows shadows = null;

    public Light(Vector3f position)
    {
        this.position = position;
        this.color = new Color(1.f);
        shadows = new Shadows(this.position);
    }

    public Light(Vector3f position, Color color)
    {
        this.position = position;
        this.color = color;
        shadows = new Shadows(this.position);
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

    public void setOribitng(boolean orbiting)
    {
        this.orbiting = orbiting;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void renderLightCube(boolean val)
    {
        this.renderLightCube = val;
    }

    public void Render(Vector3f cameraPosition)
    {
        ShaderManager.GetShader().use();
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
            if(orbiting)
            {
                position.x = (float)Math.sin(Time.GetTicks() * 1.f) * 70.f;
                position.z = (float)Math.cos(Time.GetTicks() * 1.f) * 70.f;
            }
            cube.setColor(color);
            cube.setPosition(position);
            cube.Render();
        }

        ShaderManager.GetShader().setBool("lightActive", lightActive);
    }

    public void renderSceneWithShadows(SceneInterface scene)
    {
        Shader tempShader = ShaderManager.GetShader();
        shadows.fillDepthMap(scene);

        ShaderManager.SetShader(tempShader);
        // Render scene as normal
        ShaderManager.GetShader().use();
        ShaderManager.GetShader().setInt("shadows.depthMap", 6);
        ShaderManager.GetShader().setMat4("lightSpaceMatrix", shadows.getLightSpaceMatrix());
        ShaderManager.GetShader().setBool("shadows.switchedOn", true);
        glActiveTexture(shadows.shaderTextureNum);
        glBindTexture(GL_TEXTURE_2D, shadows.getDepthMap());
        scene.renderScene();
    }
}
