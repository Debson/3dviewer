package com.michal.debski;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;


public class ShaderManager
{
    private int programID, vertexID, fragmentID;

    ShaderManager(String vertexPath, String fragmentPath)
    {
        programID = glCreateProgram();
        if(programID == 0)
            //throw new Exception("Could not create a Shader Program");
            System.out.println("Could not create a Shader Program");

        try {
            vertexID = createShader(vertexPath, GL_VERTEX_SHADER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fragmentID = createShader(fragmentPath, GL_FRAGMENT_SHADER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        glLinkProgram(programID);
        if(glGetProgrami(programID, GL_LINK_STATUS) == 0)
            //throw new Exception("Error linking Shader code " + glGetProgramInfoLog(programID, 1024));
            System.out.println("Could not create a Shader Program");

        if(vertexID != 0)
            glDetachShader(programID, vertexID);

        if(fragmentID != 0)
            glDetachShader(programID, fragmentID);

        glValidateProgram(programID);
        if(glGetProgrami(programID, GL_VALIDATE_STATUS) == 0)
            //throw new Exception("Error at validating shader " + glGetProgramInfoLog(programID, 1024));
            System.out.println("Could not create a Shader Program");
    }

    protected void use()
    {
        glUseProgram(programID);
    }

    protected void setBool(String name, boolean value)
    {
        glUniform1i(glGetUniformLocation(programID, name), value == true ? 1 : 0);
    }
    protected  void setInt(String name, int value)
    {
        glUniform1i(glGetUniformLocation(programID, name), value);
    }
    protected void setFloat(String name, float value)
    {
        glUniform1f(glGetUniformLocation(programID, name), value);
    }
    protected void setVec2(String name, Vector2f value)
    {
        glUniform2f(glGetUniformLocation(programID, name), value.x, value.y);
    }
    protected void setVec2(String name, float x, float y)
    {
        glUniform2f(glGetUniformLocation(programID, name), x, y);
    }
    protected void setVec3(String name, Vector3f value)
    {
        glUniform3f(glGetUniformLocation(programID, name), value.x, value.y, value.z);
    }
    protected void setVec3(String name, float x, float y, float z)
    {
        glUniform3f(glGetUniformLocation(programID, name), x, y, z);
    }
    protected void setVec4(String name, Vector4f value)
    {
        glUniform4f(glGetUniformLocation(programID, name), value.x, value.y, value.z, value.w);
    }

    protected  void setVec4(String name, float x, float y, float z, float w)
    {
        glUniform4f(glGetUniformLocation(programID, name), x, y, z, w);
    }

    protected void setMat4(String name, Matrix4f mat)
    {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            mat.get(fb);
            glUniformMatrix4fv(glGetUniformLocation(programID, name), false, fb);
        }
    }

    private String loadFile(String path)
    {
        String str = "";
        try
        {
            str = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        }
        catch (IOException e)
        {
            System.out.println("Couldn't read file at path: " + path);
        }

        return str;
    }

    private int createShader(String path, int type) throws Exception
    {
        int shaderID = 0;
        String code = loadFile(path);

        shaderID = glCreateShader(type);
        if(shaderID == 0)
            throw new Exception("Could not create Shader");

        glShaderSource(shaderID, code);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiliung Shader code: " + glGetShaderInfoLog(shaderID, 1024));

        glAttachShader(programID, shaderID);

        return shaderID;
    }
}
