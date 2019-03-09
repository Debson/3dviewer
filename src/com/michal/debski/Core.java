package com.michal.debski;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Core
{
    private long window;
    public static GameHandlerInterface.WindowProperties windowProperties;

    public void OpenGame(GameHandlerInterface gameHandler)
    {
        windowProperties = new GameHandlerInterface.WindowProperties(800, 600);

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Cannot initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(windowProperties.getWidth(), windowProperties.getHeight(), "model_loader", NULL, NULL);
        if(window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
                if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
                Input.UpdateKeyState(key, action);
        });

        try(MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2,
                                     (vidMode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

       /* glfwSetCursorPosCallback(window, (window, posX, posY) ->
        {
            Input.CursorPosCallback(posX, posY);
        });*/


        GL.createCapabilities();

        // Display information about system
        System.out.println("Vendor:         " + GL11.glGetString(GL11.GL_VENDOR));
        System.out.println("Renderer:       " + GL11.glGetString(GL11.GL_RENDERER));
        System.out.println("Version:        " + GL11.glGetString(GL11.GL_VERSION));
        System.out.println("LWJGL version:  " + Version.getVersion());

        gameHandler.OnWindowOpen();
    }

    public void RunGame(GameHandlerInterface gameHandler)
    {
        GL.createCapabilities();

        glClearColor(1.f, 0.6f, 0.f, 1.f);

        DoubleBuffer mousePosX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer mousePosY = BufferUtils.createDoubleBuffer(1);

        double currentTime = 0.0, prevTime = 0.0;
        while(!glfwWindowShouldClose(window))
        {
            prevTime = currentTime;
            currentTime = Time.GetTicks();
            Time.deltaTime = currentTime - prevTime;

            gameHandler.OnNewFrame();
            Input.OnStartFrame();

            glfwPollEvents();

            glfwGetCursorPos(window, mousePosX, mousePosY);
            Input.CursorPosCallback(mousePosX.get(0), mousePosY.get(0));


            gameHandler.OnRealtimeUpdate();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            gameHandler.OnRealTimeRender();
            glfwSwapBuffers(window);

            gameHandler.OnFinishFrame();
        }
    }

    public void StopGame(GameHandlerInterface gameHandler)
    {

    }

    public void CloseGame(GameHandlerInterface gameHandler)
    {

        gameHandler.OnWindowClose();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
