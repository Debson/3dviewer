#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec2 TexCoords;

uniform sampler2D tex;

void main()
{
    vec4 tex = texture(tex, TexCoords);
    FragColor = tex * vec4(1.0);
    if((FragPos.x < -0.8 || FragPos.x > 0.8 || FragPos.z < -0.8 || FragPos.z > 0.8) && (FragPos.y > 0.8 || FragPos.y < -0.8))
        FragColor = vec4(1.0, 0.5, 1.0, 1.0);
     else
        FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}