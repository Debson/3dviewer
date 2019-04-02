package com.michal.debski;

public interface GameHandlerInterface
{
    class WindowProperties
    {
        public static int width, height;
        public static int winPosX, winPosY;
        public static int posX, posY;

        public static int[] resolutionWidth = {1280, 1024, 800, 640};
        public static int[] resolutionHeight = {720, 768, 600, 480};

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

        public static int getPosX()
        {
            return posX;
        }
        public static int getPosY()
        {
            return posY;
        }

    }

    public void OnWindowOpen();

    public void OnWindowClose();

    public void OnNewFrame();

    public void OnFinishFrame();

    public void OnRealtimeUpdate();

    public void OnRealTimeRender();

    public void OnFileDrop(String pathOfDroppedFile);

    public void OnWindowMove(int winX, int winY);

    public void OnWindowFocus(boolean hasFocus);
}
