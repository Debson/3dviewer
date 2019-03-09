#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec2 TexCoords;

uniform sampler2D tex;

void main()
{
    vec4 tex = texture(tex, TexCoords);
    FragColor = tex * vec4(1.0);
}