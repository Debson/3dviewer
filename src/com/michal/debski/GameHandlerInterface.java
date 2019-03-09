package com.michal.debski;

public interface GameHandlerInterface
{
    class WindowProperties
    {
        static int width, height;

        WindowProperties(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        public static int getWidth()
        {
            return width;
        }

        public static int getHeight()
        {
            return height;
        }
    };

    public void OnWindowOpen();

    public void OnWindowClose();

    public void OnNewFrame();

    public void OnFinishFrame();

    public void OnRealtimeUpdate();

    public void OnRealTimeRender();
}
