package com.michal.debski.environment;

import com.michal.debski.*;
import com.michal.debski.loader.Loader;
import com.michal.debski.utilities.Color;
import com.michal.debski.utilities.Transform;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;

public class Light extends Model
{
    // This should be the parent class. Child classes: directional light, point light
    // Light method render should be called before any drawing, so it will set boolean variable
    // that manages lighting in shader to true, so the scene will be drawn with lighting.
    protected float strength;
    protected Color color;
    protected boolean lightActive = true;
    private boolean orbiting = false;
    private Vector3f orbitingPosition = null;
    private float orbitingRadius = 10.f;
    private float orbitingSpeed = 2.f;

    private boolean renderLightCube = true;
    private boolean castShadows = true;
    private Shadows shadows = null;


    public Light(Vector3f position)
    {
        super(Loader.PrimitiveType.Cube);
        this.getTransform().setPosition(position);
        this.color = new Color(1.f);
        shadows = new Shadows(this.getTransform().getPosition());
    }

    public Light(Vector3f position, Color color)
    {
        super(Loader.PrimitiveType.Cube);
        this.getTransform().setPosition(position);
        this.color = color;
        shadows = new Shadows(this.getTransform().getPosition());
    }

    public void on()
    {
        this.lightActive = true;
    }

    public void off()
    {
        this.lightActive = false;
    }

    public void setOribitng(boolean orbiting)
    {
        this.orbiting = orbiting;
    }

    public void setOrbitingSpeed(float orbitingSpeed)
    {
        this.orbitingSpeed = orbitingSpeed;
    }

    public void setOrbitingRadius(float orbitingRadius)
    {
        this.orbitingRadius = orbitingRadius;
    }

    public void setOrbitingAroundPosition(Vector3f orbitingPosition, float orbitingRadius, float orbitingSpeed, boolean orbiting)
    {
        this.orbiting = orbiting;
        this.orbitingRadius = orbitingRadius;
        this.orbitingSpeed = orbitingSpeed;
        this.orbitingPosition = orbitingPosition;
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

            ShaderManager.GetShader().setVec3("light.direction", getTransform().getPosition());
            //ShaderManager.GetShader().setVec3("lightPos", position);
            ShaderManager.GetShader().setVec3("light.color", color.r, color.g, color.b);
        }
    }

    protected void renderLight()
    {
        if(renderLightCube)
        {
            ShaderManager.GetShader().setBool("lightActive", false);
            if(orbiting && orbitingPosition != null)
            {
                getTransform().getPosition().x = orbitingPosition.x + (float)Math.sin(Time.GetTicks() * orbitingSpeed) * orbitingRadius;
                getTransform().getPosition().z = orbitingPosition.z + (float)Math.cos(Time.GetTicks() * orbitingSpeed) * orbitingRadius;
            }
            else if(orbiting)
            {
                getTransform().getPosition().x += (float)Math.sin(Time.GetTicks() * orbitingSpeed) * orbitingRadius;
                getTransform().getPosition().z += (float)Math.cos(Time.GetTicks() * orbitingSpeed) * orbitingRadius;
            }

            setColor(color);
            getTransform().setPosition(getTransform().getPosition());
            Render();
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
        //ShaderManager.GetShader().setBool("shadows.switchedOn", true);
        glActiveTexture(shadows.shaderTextureNum);
        glBindTexture(GL_TEXTURE_2D, shadows.getDepthMap());
    }
}
