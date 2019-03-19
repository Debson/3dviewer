package com.michal.debski;

public class ShaderManager
{
    private static Shader shader = null;

    public static void SetShader(Shader s)
    {
        shader = s;
        shader.use();
    }

    public static Shader GetShader()
    {
        return shader;
    }
}
