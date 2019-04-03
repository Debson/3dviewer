/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Shadows class manages creating framebuffer and filling the depth texture
 *                      with a current scene.
 *
 */

package com.michal.debski.environment;

import com.michal.debski.Core;
import com.michal.debski.SceneInterface;
import com.michal.debski.Shader;
import com.michal.debski.ShaderManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Shadows
{
    // High quality shadows
    private final int shadowWidth = 8192;
    private final int shadowHeight = 8192;

    private final String shadowShaderVertPath = "shaders" + File.separator + "shadowShader.vert";
    private final String shadowShaderFragPath = "shaders" + File.separator + "shadowShader.frag";
    public final int shaderTextureNum = GL_TEXTURE6;


    private Matrix4f lightSpaceMatrix;
    public final float nearPlane = 1.0f;
    public final float farPlane = 100.0f;
    public final float lightProjectionSize = 100.f;
    private Vector3f lightPos;

    private int depthMapFBO, depthMap;

    private Shader shader = null;


    public Shadows(Vector3f lightPos)
    {
        this.lightPos = lightPos;

        // Create shader that will render the scene to depth map texture
        shader = new Shader(shadowShaderVertPath, shadowShaderFragPath);

        // Configure depth map framebuffer and create depth texture
        depthMapFBO = glGenFramebuffers();

        depthMap = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthMap);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, shadowWidth, shadowHeight, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        float[] borderColor = { 1.f, 1.f, 1.f, 1.f };
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);

        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap, 0);
        int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(fboStatus != GL_FRAMEBUFFER_COMPLETE)
        {
            throw new AssertionError("Could not create Framebuffer: " + fboStatus);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void fillDepthMap(SceneInterface scene)
    {
        // Set global shader to simple shader used to render to depth map
        ShaderManager.SetShader(shader);

        Matrix4f lightProjection = new Matrix4f().ortho(-lightProjectionSize, lightProjectionSize, -lightProjectionSize, lightProjectionSize, nearPlane, farPlane);
        Matrix4f lightView = new Matrix4f().lookAt(lightPos, new Vector3f(0.f), new Vector3f(0.f, 1.f, 0.f));
        lightSpaceMatrix = new Matrix4f();
        lightProjection.mul(lightView, lightSpaceMatrix);

        shader.use();
        shader.setMat4("lightSpaceMatrix", lightSpaceMatrix);

        // Render scene to depth buffer
        glViewport(0, 0, shadowWidth, shadowHeight);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
            glClear(GL_DEPTH_BUFFER_BIT);
            scene.renderScene(shader);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glViewport(0, 0, Core.windowProperties.width, Core.windowProperties.height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Matrix4f getLightSpaceMatrix()
    {
        return lightSpaceMatrix;
    }

    public int getDepthMap()
    {
        return depthMap;
    }
}
