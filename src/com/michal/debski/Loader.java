package com.michal.debski;

import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;




public class Loader
{
    private static final String VERTEX                      = "v";
    private static final String VERTEX_TEXTURE_COORDINATE   = "vt";
    private static final String VERTEX_NORMAL               = "vn";
    private static final String FACE                        = "f";
    private static final String MATERIAL                    = "usemtl";
    private static final String OBJECT                      = "o";
    private static final String GROUP                       = "g";


    /*
    * Create vector of meshes, every mesh has its own attribues(vertices, texCoords, normals, name of mesh , texture? etc.)
    * Loader will have number of meshes etc.
    * */
    class mdMesh
    {
        public String name = "";
        public Vector vertices = new Vector();
        public Vector texCoords = new Vector();
        public Vector normals = new Vector();
        public Vector tangent = new Vector();
        public Vector bitangent = new Vector();

        public mdMesh()
        {

        }

        // Class that
        public mdMesh(mdMesh other)
        {
            this.name = other.name;
            this.vertices = other.vertices;
            this.texCoords = other.texCoords;
            this.normals = other.normals;
            this.tangent = other.tangent;
            this.bitangent = other.bitangent;
        }
    }

    public Vector<mdMesh> meshes = new Vector<mdMesh>();

    public Loader(String path)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            mdMesh mesh = new mdMesh();
            while ((line = br.readLine()) != null)
            {
                // Split line into words
                String[] values = line.split("\\s+");

                switch(values[0])
                {
                    // Retrieve object vertices, texture coordinates, normals and the name
                    case VERTEX: {
                        for(int i = 1; i < values.length; i++)
                            mesh.vertices.add(Float.parseFloat(values[i]));
                        break;
                    }
                    case VERTEX_TEXTURE_COORDINATE: {
                        for(int i = 1; i < values.length; i++)
                            mesh.texCoords.add(Float.parseFloat(values[i]));
                        break;
                    }
                    case VERTEX_NORMAL: {
                        for(int i = 1; i < values.length; i++)
                            mesh.normals.add(Float.parseFloat(values[i]));
                        break;
                    }
                    case OBJECT: {
                        // New object is detected. Add mesh to meshes container only when it has any data(vertices)
                        if(!mesh.vertices.isEmpty()) {
                            meshes.add(new mdMesh(mesh));
                            mesh = new mdMesh();
                        }
                        // Get name of the object
                        for(int i = 1; i < values.length; i++) {
                            // Add divisor '_' between words
                            if(i % 2 == 0)
                                mesh.name += "_";
                            mesh.name += values[i];
                        }
                        break;
                    }
                }
            }

            // Some .obj files doesn't have object declaration (letter "o" on the start of the line),
            // so they have just one object. Add it after file is fully processed.
            if(meshes.isEmpty())
                meshes.add(mesh);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Make sure there is no objects that have no data in the meshes container.
        for(int i = 0; i < meshes.size(); i++)
        {
            if(meshes.elementAt(i).vertices.isEmpty())
                meshes.remove(i);
        }
    }
}
