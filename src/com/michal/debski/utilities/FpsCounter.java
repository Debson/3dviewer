/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Class responsible for updating FPS label.
 *
 */

package com.michal.debski.utilities;

import com.michal.debski.Time;

import javax.swing.*;

public class FpsCounter
{
    private static int frames = 0;
    private static double currentTime = 0;
    private static double lastTime = 0;
    private static JLabel fpsLabel = null;

    public static void SetFpsLabel(JLabel fpsl)
    {
        fpsLabel = fpsl;
    }

    public static void Update()
    {
        if (fpsLabel != null)
        {
            currentTime = Time.GetTicks();
            frames++;
            if (currentTime - lastTime >= 1.0)
            {
              fpsLabel.setText(String.format("ms/frame: %2.2f", 1000.0 / (double)frames));
              frames = 0;
              lastTime += 1;
            }
        }
    }
}

