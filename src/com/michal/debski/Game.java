/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Game class creates another interface layer between user and the program
 *
 */

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
