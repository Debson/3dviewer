package com.michal.debski.environment;

import com.michal.debski.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Shadows
{
    private int shadowWidth, shadowHeight;
    private float nearPlane = 1.f;
    private float farPlane = 7.5f;
    private Vector3f lightPos;

    private int depthMapFBO, depthMap;

    private Shader shader = null;

    public Shadows(int shadowWidth, int shadowHeight, Vector3f lightPos)
    {
        this.shadowWidth = shadowWidth;
        this.shadowHeight = shadowHeight;
        this.lightPos = lightPos;

        // Create shader that will render the scene to depth map texture
        shader = new Shader("shaders\\shadowShader.vert", "shaders\\shadowShader.frag");

        // Configure depth map framebuffer and create depth texture
        depthMapFBO =  glGenFramebuffers();

        depthMap = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthMap);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, shadowWidth, shadowHeight, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_COMPONENT, GL_TEXTURE_2D, depthMap, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        shader.use();
        shader.setInt("depthMap", 0);
    }

    public void onRenderStart()
    {
        Matrix4f lightProjection = new Matrix4f().ortho(-10.f, 10.f, -10.f, 10.f, nearPlane, farPlane);
        Matrix4f lightView = new Matrix4f().lookAt(lightPos, new Vector3f(0.f), new Vector3f(0.f, 1.f, 0.f));
        Matrix4f lightSpaceMatrix = new Matrix4f();
        lightProjection.mul(lightView, lightSpaceMatrix);

        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.use();
        shader.setMat4("lightSpaceMatrix", lightSpaceMatrix);

        glViewport(0, 0, shadowWidth, shadowHeight);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
            glClear(GL_DEPTH_BUFFER_BIT);
            glActiveTexture(GL_TEXTURE0);



    }

    public void onRenderFinish()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // need to set viewport as well
    }
}
