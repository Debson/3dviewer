/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Time class provides ticks since application started and delta time(timestamp between frames)
 *
 */

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
