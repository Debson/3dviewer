package com.michal.debski;

public class Game extends Core
{
    private GameHandlerInterface gameHandler;

    Game(GameHandlerInterface gameHandler)
    {
        this.gameHandler = gameHandler;
    }

    public void Open()
    {
        super.OpenGame(gameHandler);
    }

    public void Run()
    {
        super.RunGame(gameHandler);
    }

    public void Stop()
    {
        super.StopGame(gameHandler);
    }

    public void Close()
    {
        super.CloseGame(gameHandler);
    }
}
