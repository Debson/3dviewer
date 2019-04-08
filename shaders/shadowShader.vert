/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Description:
 *
 */

#version 330 core
layout (location = 0) in vec3 aPos;

uniform mat4 lightSpaceMatrix;
uniform mat4 model;

void main()
{
    gl_Position = lightSpaceMatrix * model * vec4(aPos, 1.0);
}