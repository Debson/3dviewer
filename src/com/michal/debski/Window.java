/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Static class used to access some of the GLFW functions on a window
 *
 */

package com.michal.debski;

import static org.lwjgl.glfw.GLFW.*;

public class Window
{
    private static long window;

    static void SetWindow(long win)
    {
       window = win;
    }

    static void FocusWindow()
    {
        glfwFocusWindow(window);
    }

    static void ShowWindow()
    {
        glfwShowWindow(window);
    }

    static void RestoreWindow()
    {
        glfwRestoreWindow(window);
    }

    static void CursorDisabled(boolean cursorDisabled)
    {
        if(cursorDisabled)
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        else
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }
}
