/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Static class used to GET and SET a currently used Shader in the application.
 *                      It allows to access a shader instance globally.
 *
 */

package com.michal.debski;

public class ShaderManager
{
    private static Shader shader = null;

    public static void SetShader(Shader s)
    {
        shader = s;
        shader.use();
    }

    public static Shader GetShader()
    {
        return shader;
    }
}
