/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Interface used mainly to pass scene rendering function to a Light class that manages
 *                      rendering shadows(Replacement for C-like function pointers)
 *
 */

package com.michal.debski;

public interface SceneInterface
{
    void renderScene(Shader shader);
}
