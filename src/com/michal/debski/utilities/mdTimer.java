/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Simple implementation of a timer. Have only the functionality which was needed for this application.
 *
 */

package com.michal.debski.utilities;

import com.michal.debski.Time;

public class mdTimer
{
    private double currentTime = 0.0;
    private double lastFrameLength = 0.0;
    private boolean stopped = true;
    private boolean initialized = false;

    public void start()
    {
        if(stopped)
        {
            stopped = false;
        }
    }

    public void stop()
    {
        if(!stopped)
        {
            stopped = true;
        }

    }

    public void restart()
    {
        currentTime = 0.0;
    }

    public boolean isRunning()
    {
        return !stopped;
    }

    public double getCurrentTime()
    {
        /*  "lastFrameLength != Time.deltaTime" - increment @currentTime only once every frame
            this function is called
         */
        if(!stopped && lastFrameLength != Time.deltaTime)
        {
            currentTime += Time.deltaTime;
            lastFrameLength = Time.deltaTime;
        }

        return currentTime;
    }
}
