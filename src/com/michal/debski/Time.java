package com.michal.debski;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time
{
    public static double deltaTime;


    public static double GetTicks()
    {
       return glfwGetTime();
    }
}
