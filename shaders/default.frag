#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec3 Normal;
in vec2 TexCoords;
in vec3 LightPos;


bool isTextureBinded(sampler2D tex);
vec4 calculateDirectionalLight();

struct Material
{
    sampler2D ambientMap;
    sampler2D diffuseMap;
    sampler2D specularMap;
    sampler2D normalMap;
    sampler2D heightMap;

    float ambientStrength;
    float diffuseStrength;
    float specularStrength;
};

struct Light
{
    vec3 direction;
    vec3 color;
};

uniform Material material;
uniform Light light;
uniform vec4 color = vec4(1.0);
uniform bool lightActive = false;

void main()
{
    FragColor = color;
    /*if(isTextureBinded(material.diffuseMap))
    {
        vec4 tex = texture(material.diffuseMap, TexCoords);
        FragColor *= tex;
    }*/

    if(lightActive)
    {
        FragColor = calculateDirectionalLight();
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

vec4 calculateDirectionalLight()
{
    vec3 ambient;
    ambient = material.ambientStrength * light.color;
    //if(isTextureBinded(material.diffuseMap))
        ambient = light.color * material.ambientStrength * texture(material.diffuseMap, TexCoords).rgb;
    //else
        //ambient = material.ambientStrength * light.color;

    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.direction);
    //vec3 lightDir = normalize(LightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse;
    diffuse = (diff * material.diffuseStrength) * light.color;

    //if(isTextureBinded(material.diffuseMap))
        diffuse = light.color * material.diffuseStrength * texture(material.diffuseMap, TexCoords).rgb;
    //else
        //diffuse = (diff * material.diffuseStrength) * light.color;

    vec3 viewDir = normalize(-FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    //vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    //float spec = pow(max(dot(viewDir, halfwayDir), 0.0), 32);
    vec3 specular;
    specular = (spec * material.specularStrength) * light.color;

    //if(isTextureBinded(material.specularMap))
        specular = light.color * spec * texture(material.specularMap, TexCoords).rgb;
    //else
        //specular = (spec * material.specularStrength) * light.color;

    /*float distance = length(LightPos - FragPos);
    float attenuation = 1.0 / (1.0 + 0.09 * distance + 0.032 * (distance * distance));

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;*/

    return vec4((ambient + diffuse + specular), 1.0);
}