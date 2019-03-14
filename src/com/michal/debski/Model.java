package com.michal.debski;

import com.michal.debski.loader.Loader;
import org.joml.Vector3f;

public class Model
{
    class Vertex
    {
        Vector3f position;
        Vector3f normal;
        Vector3f texCoords;
    }

    private Loader loader;

    public Model(String path)
    {
        loader = new Loader(path);
    }


    private void loadData()
    {

    }

    private void mapData()
    {

    }

    public void Render(Shader shader)
    {
        for(Loader.mdMesh mesh : loader.meshes)
            mesh.Render(shader);
    }
}
