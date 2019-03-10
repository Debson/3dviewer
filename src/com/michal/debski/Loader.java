package com.michal.debski;

import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;




public class Loader
{
    private static final String VERTEX = "v";
    private static final String VERTEX_TEXTURE_COORDINATE = "vt";
    private static final String VERTEX_NORMAL = "vn";
    private static final String FACE = "f";


    /*
    * Create vector of meshes, every mesh has its own attribues(vertices, texCoords, normals, name of mesh , texture? etc.)
    * Loader will have number of meshes etc.
    * */
    class mdMesh
    {
        public Vector vertices = new Vector();
        public Vector texCoords = new Vector();
        public Vector normals = new Vector();
        public Vector tangent = new Vector();
        public Vector bitangent = new Vector();
    }

    public mdMesh mesh;

    public Loader(String path)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] values = line.split("\\s+");

                switch(values[0])
                {
                    case VERTEX: {
                        for(int i = 1; i < values.length; i++)
                            mesh.vertices.add(Float.parseFloat(values[i]));
                        break;
                    }
                    case VERTEX_NORMAL: {
                        for(int i = 1; i < values.length; i++)
                            mesh.texCoords.add(Float.parseFloat(values[i]));
                        break;
                    }
                    case VERTEX_TEXTURE_COORDINATE: {
                        for(int i = 1; i < values.length; i++)
                            mesh.normals.add(Float.parseFloat(values[i]));
                        break;
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
