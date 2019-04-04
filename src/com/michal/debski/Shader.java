/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Wrapper class for a mdShader
 *
 */

package com.michal.debski;

import org.joml.Matrix4f;

public class Shader extends mdShader
{
    public Shader(String vertexCode, String fragmentCode)
    {
        super(vertexCode, fragmentCode);

        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();
        super.use();
        super.setMat4("projection", projectionMatrix);
        super.setMat4("view", viewMatrix);
    }

    public void updateShaders()
    {
        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();
        super.use();
        super.setMat4("projection", projectionMatrix);
        super.setMat4("view", viewMatrix);
    }

}
