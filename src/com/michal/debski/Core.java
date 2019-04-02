package com.michal.debski;


import com.michal.debski.utilities.FpsCounter;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Core
{
    private long window;
    public static GameHandlerInterface.WindowProperties windowProperties;
    GLFWFramebufferSizeCallback fbCallback;



    public void OpenGame(GameHandlerInterface gameHandler)
    {
        //windowProperties = new GameHandlerInterface.WindowProperties(1280, 720);

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Cannot initialize GLFW");

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);


        // Set window dimension basing on primary screen resolution
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        int resCouter = 0;
        while (vidMode.width() <= windowProperties.resolutionWidth[resCouter] + Gui.GetWidth() ||
                vidMode.height() <= windowProperties.resolutionHeight[resCouter])
        {
            resCouter++;
        }

        windowProperties = new GameHandlerInterface.WindowProperties(windowProperties.resolutionWidth[resCouter],
                                                                     windowProperties.resolutionHeight[resCouter]);

        System.out.println("Window Width:   " + windowProperties.resolutionWidth[resCouter]);
        System.out.println("Window Height:  " + windowProperties.resolutionHeight[resCouter]);

        window = glfwCreateWindow(windowProperties.getWidth(), windowProperties.getHeight(), "model_loader", NULL, NULL);
        if(window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        Window.SetWindow(window);

        setupGLFW(gameHandler);

        setupOpenGL();


        // Display information about the system
        System.out.println("\nVendor:         " + glGetString(GL_VENDOR));
        System.out.println("Renderer:       " + glGetString(GL_RENDERER));
        System.out.println("Version:        " + glGetString(GL_VERSION));
        System.out.println("LWJGL version:  " + Version.getVersion());

        gameHandler.OnWindowOpen();
    }

    public void RunGame(GameHandlerInterface gameHandler)
    {
        GL.createCapabilities();

        //glClearColor(1.f, 0.6f, 0.f, 1.f);
        glClearColor(0.1f, 0.1f, 0.1f, 1.f);

        DoubleBuffer mousePosX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer mousePosY = BufferUtils.createDoubleBuffer(1);

        double currentTime = 0.0, prevTime = 0.0;
        while(!glfwWindowShouldClose(window))
        {
            prevTime = currentTime;
            currentTime = Time.GetTicks();
            Time.deltaTime = currentTime - prevTime;

            FpsCounter.Update();

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

        memFree(mousePosX);
        memFree(mousePosY);
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

    private void setupGLFW(GameHandlerInterface gameHandler)
    {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0 && (windowProperties.width!= width || windowProperties.height != height)) {
                    windowProperties.width = width;
                    windowProperties.height = height;
                }
            }
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            Input.UpdateKeyState(key, action);
        });


        glfwSetDropCallback(window, (window1, count, names) ->
        {
            PointerBuffer nameBuffer = memPointerBuffer(names, count);
            String pathOfDroppedFile = memUTF8(memByteBufferNT1(nameBuffer.get(0)));
            gameHandler.OnFileDrop(pathOfDroppedFile);
        });

        glfwSetMouseButtonCallback(window, (window1, button, action, mods) ->
        {
            Input.UpdateKeyState(button, action);
        });

        glfwSetWindowPosCallback(window, (window1, posX, posY) ->
        {
            windowProperties.posX = posX;
            windowProperties.posY = posY;
            gameHandler.OnWindowMove(posX, posY);
        });

        glfwSetWindowFocusCallback(window, (window1, focused) ->
        {
            gameHandler.OnWindowFocus(focused);
        });

        try(MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0) + Gui.GetWidth()) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);

        // V-Sync turned on
        glfwSwapInterval(1);

        // Disable cursor, so it won't block at screen bounds
        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwShowWindow(window);

        GL.createCapabilities();
    }

    private void setupOpenGL()
    {
        glEnable(GL_DEPTH_TEST);
    }
}
