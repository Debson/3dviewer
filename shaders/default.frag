#version 330 core
out vec4 FragColor;

in vec3 FragPos;
in vec3 Normal;
in vec2 TexCoords;
in vec3 LightPos;
in vec4 FragPosLightSpace;

vec4 calculateDirectionalLight();
float CalculateShadows(vec4 fragPosLigtSpace);

struct Material
{
    sampler2D ambientMap;
    sampler2D diffuseMap;
    sampler2D specularMap;
    sampler2D normalMap;
    sampler2D heightMap;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct DirLight
{
    vec3 direction;
    vec3 color;
};

struct Shadows
{
    bool switchedOn;
    sampler2D depthMap;
};

uniform Material material;
uniform DirLight dirLight;
uniform Shadows shadows;

uniform vec4 color = vec4(1.0);
uniform bool lightActive = false;
uniform bool shadowsActive = false;
uniform bool textureActive = false;
uniform vec3 viewPos;

void main()
{
    FragColor = color;

    if(lightActive)
    {
        FragColor *= calculateDirectionalLight();
    }

    /*if((FragPos.x < -0.8 || FragPos.x > 0.8 || FragPos.z < -0.8 || FragPos.z > 0.8) && (FragPos.y > 0.8 || FragPos.y < -0.8))
        FragColor = vec4(1.0, 0.5, 1.0, 1.0);
    else
        FragColor = vec4(1.0, 0.0, 0.0, 1.0);*/
}

vec4 calculateDirectionalLight()
{
    // ambient
    vec3 ambient;
    if(textureActive)
        ambient = material.diffuse * texture(material.diffuseMap, TexCoords).rgb;
    else
        ambient = material.ambient;

    // diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(dirLight.direction);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse;
    if(textureActive)
        diffuse = (diff * material.diffuse) * texture(material.diffuseMap, TexCoords).rgb;
    else
        diffuse = (diff * material.diffuse);

    // specular
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);;
    vec3 specular;
    if(textureActive)
        specular = (spec * material.specular) * texture(material.specularMap, TexCoords).rgb;
    else
        specular = (spec * material.specular);

    float shadow = CalculateShadows(FragPosLightSpace);

    return vec4((ambient + (1.0 - shadow * 1000.0) * (diffuse + specular)) * vec3(color), 1.0);
}


float CalculateShadows(vec4 fragPosLigtSpace)
{
    vec3 projCoords = fragPosLigtSpace.xyz / fragPosLigtSpace.w;
    projCoords = projCoords * 0.5 + 0.5;
    float closestDepth = texture(shadows.depthMap, projCoords.xy).r;
    float currentDepth = projCoords.z;

    vec3 normal = normalize(Normal);
    vec3 lightDir = normalize(dirLight.direction - FragPos);
    float bias = max(0.05 * (1.0 - dot(normal, lightDir)), 0.005);

    float shadow = 0.0;
    vec2 texelSize = 1.0 / textureSize(shadows.depthMap, 0);
    for(int x = -1; x <= 1; x++)
    {
        for(int y = -1; y <= 1; y++)
        {
            float pcfDepth = texture(shadows.depthMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += currentDepth - bias > pcfDepth ? 1.0 : 0.0;
        }
    }

    shadow /= 9.0;

    if(projCoords.z > 1.0)
        shadow = 0;


    return shadow;
}