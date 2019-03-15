#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec2 TexCoords;


bool isTextureBinded(sampler2D tex);

struct Material
{
    sampler2D ambientMap;
    sampler2D diffuseMap;
    sampler2D normalMap;
    sampler2D heightMap;

};

uniform Material material;

uniform vec4 color = vec4(1.0);

void main()
{
    vec4 tex;
    if(isTextureBinded(material.diffuseMap))
    {
        tex = texture(material.diffuseMap, TexCoords);
        FragColor = tex * color;
    }
    else
    {
        FragColor = color;
    }


   /* if((FragPos.x < -0.8 || FragPos.x > 0.8 || FragPos.z < -0.8 || FragPos.z > 0.8) && (FragPos.y > 0.8 || FragPos.y < -0.8))
        FragColor = vec4(1.0, 0.5, 1.0, 1.0);
     else
        FragColor = vec4(1.0, 0.0, 0.0, 1.0);*/
}

bool isTextureBinded(sampler2D tex)
{
    return textureSize(tex, 2).x  > 1  ? true : false;
}