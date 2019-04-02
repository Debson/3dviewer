package com.michal.debski.loader;

import com.michal.debski.Vertices;

import java.io.*;
import java.util.*;
import org.joml.*;

import static org.lwjgl.opengl.GL30.*;


public class Loader
{
    public static final int    FLOAT_SIZE                  = 4;        // Float size is 4 bytes on 64bit on Windows 10 65bit platform
    private static final String VERTEX                      = "v";
    private static final String VERTEX_TEXTURE_COORDINATE   = "vt";
    private static final String VERTEX_NORMAL               = "vn";
    private static final String FACE                        = "f";
    private static final String MATERIAL                    = "usemtl";
    private static final String OBJECT                      = "o";
    private static final String GROUP                       = "g";
    private static final String MTL_FILE                    = "mtllib";

    private static final String USE_MATERIAL                = "usemtl";
    private static final String MTL_MATERIAL_BIND           = "newmtl";
    private static final String MTL_AMBIENT_MAP             = "map_Ka";
    private static final String MTL_DIFFUSE_MAP             = "map_Kd";
    private static final String MTL_SPECULAR_MAP            = "map_Ks";
    private static final String MTL_HEIGHT_MAP              = "map_Bump";

    public static final String AMBIENT_MAP                 = "ambient";
    public static final String DIFFUSE_MAP                 = "diffuse";
    public static final String SPECULAR_MAP                = "specular";
    public static final String NORMAL_MAP                  = "normal";
    public static final String HEIGHT_MAP                  = "height";

    public static final String MATERIAL_AMBIENT            = "Ka";
    public static final String MATERIAL_DIFFUSE            = "Kd";
    public static final String MATERIAL_SPECULAR           = "Ks";

    
    /*
    * Create vector of meshes, every mesh has its own attribues(vertices, texCoords, normals, name of mesh , texture? etc.)
    * Loader will have number of meshes etc.
    * */
    public enum PrimitiveType
    {
        Cube,
        Plane,
        Quad
    };


    public class mdVertex
    {
        Vector3f position = new Vector3f();
        Vector3f normal = new Vector3f();
        Vector2f texCoord = new Vector2f();
    }

    public class mdTexture
    {
        int id;
        String type;
        String path;
    }

    public Vector<mdMesh> meshes = new Vector<mdMesh>();
    private String mtlFilePath = new String();
    private String directory = new String();

    public Loader(String path)
    {
        // The idea is to read all vertices, texture coordinates and normals
        // from file and then map them to objects basing on their faces.
        // Every object has it's own data about faces
        List<Float> allVertices = new ArrayList<Float>();
        List<Float> allTexCoords = new ArrayList<Float>();
        List<Float> allNormals = new ArrayList<Float>();

        int extPos = path.lastIndexOf('.');
        String fullName = path.substring(extPos);
        fullName = fullName.toLowerCase();
        if(fullName.contains("obj") == false)
        {
            System.out.println("ERROR: File not supported. Only files with \".obj\" extension are supported.");
            return;
        }

        int dirDelimiter = path.lastIndexOf('/');
        directory = path.substring(0, dirDelimiter);
        directory += '/';

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            mdMesh mesh = new mdMesh();

            // Set default mesh material

            mesh.material = new mdMaterial(new Vector3f(0.1f),
                    new Vector3f(0.1f),
                    new Vector3f(0.25f),
                    32.f);

            while ((line = br.readLine()) != null)
            {
                // Split line into words
                String[] values = line.split("\\s+");

                switch(values[0])
                {
                    // Retrieve object vertices, texture coordinates, normals and the name
                    case VERTEX: {
                        for(int i = 1; i < values.length; i++) {
                            allVertices.add(Float.parseFloat(values[i]));
                        mesh.hasVertices = true;
                        }
                        break;
                    }
                    case VERTEX_TEXTURE_COORDINATE: {
                        for(int i = 1; i < values.length; i++)
                            allTexCoords.add(Float.parseFloat(values[i]));
                        mesh.hasTexCoods = true;
                        break;
                    }
                    case VERTEX_NORMAL: {
                        for(int i = 1; i < values.length; i++)
                            allNormals.add(Float.parseFloat(values[i]));
                        mesh.hasNormals = true;
                        break;
                    }
                    case OBJECT: {
                        // New object is detected. Add mesh to meshes container only when it has any data(vertices)
                        if(mesh.hasVertices) {
                            meshes.add(new mdMesh(mesh));
                            mesh = new mdMesh();
                        }
                        // Get name of the object
                        for(int i = 1; i < values.length; i++) {
                            // Add divisor '_' if mesh name has more than one word separated by space(shouldn't happen)
                            if(i % 2 == 0)
                                mesh.name += "_";
                            mesh.name += values[i];
                        }
                        break;
                    }
                    case GROUP: {
                        // Get name of the object
                        for(int i = 1; i < values.length; i++) {
                            // Add divisor '_' if mesh name has more than one word separated by space(shouldn't happen)
                            if(i % 2 == 0)
                                mesh.name += "_";
                            mesh.name += values[i];
                        }
                        break;
                    }
                    case FACE: {
                        for(int i = 1; i < values.length; i++)
                        {
                            String[] temp = values[i].split("/");

                            // Save indices as a three-dimensional vector
                            // Consider that there may not be texture coordinates or normals
                            // From parsed value subtract 1, because indices in OpenGL starts at 0
                            if(temp.length == 1)
                            {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        0,
                                        0));
                            }
                            else if(temp.length == 2)
                            {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        Integer.parseInt(temp[1]) - 1,
                                        0));
                            }
                            else if(allTexCoords.isEmpty())
                            {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        0,
                                        Integer.parseInt(temp[2]) - 1));
                            }
                            else
                            {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        Integer.parseInt(temp[1]) - 1,
                                        Integer.parseInt(temp[2]) - 1));
                            }
                        }
                        break;
                    }
                    case MTL_FILE: {
                        mtlFilePath =  directory + values[1];
                        break;
                    }

                    case USE_MATERIAL: {
                        mesh.material_name = values[1];
                        break;
                    }
                }
            }

            // Add the last mesh(or the only one)
            meshes.add(mesh);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Check if mtlFilePath contains any path
        if(!mtlFilePath.isEmpty()) {
            // Get info about textures used in each mesh
            try (BufferedReader br = new BufferedReader(new FileReader(mtlFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Split line into words
                    String[] values = line.split("\\s+");
                    switch (values[0]) {
                        case MTL_MATERIAL_BIND: {
                            // Find mesh that uses material with that name
                            mdMesh mesh = findMeshByMaterialName(values[1]);


                            if(mesh == null)
                                break;

                            // Find maps and their paths
                            while ((line = br.readLine()) != null && !line.contains(MTL_MATERIAL_BIND)) {
                                values = line.split("\\s+");
                                switch (values[0]) {
                                    case MTL_AMBIENT_MAP: {
                                        mdTexture texture = new mdTexture();
                                        texture.path = directory + values[1];
                                        texture.id = Image.LoadImage(texture.path);
                                        texture.type = AMBIENT_MAP;
                                        mesh.textures.add(texture);
                                        break;
                                    }
                                    case MTL_DIFFUSE_MAP: {
                                        mdTexture texture = new mdTexture();
                                        texture.path = directory + values[1];
                                        texture.id = Image.LoadImage(texture.path);
                                        texture.type = DIFFUSE_MAP;
                                        mesh.textures.add(texture);
                                        break;
                                    }
                                    case MTL_SPECULAR_MAP: {
                                        mdTexture texture = new mdTexture();
                                        texture.path = directory + values[1];
                                        texture.id = Image.LoadImage(texture.path);
                                        texture.type = SPECULAR_MAP;
                                        mesh.textures.add(texture);
                                        break;
                                    }
                                    case MTL_HEIGHT_MAP: {
                                        mdTexture texture = new mdTexture();
                                        texture.path = directory + values[1];
                                        texture.id = Image.LoadImage(texture.path);
                                        texture.type = HEIGHT_MAP;
                                        mesh.textures.add(texture);
                                        break;
                                    }
                                    case MATERIAL_AMBIENT: {
                                        mesh.material.ambient.x = Float.parseFloat(values[1]);
                                        mesh.material.ambient.y = Float.parseFloat(values[2]);
                                        mesh.material.ambient.z = Float.parseFloat(values[3]);
                                        break;
                                    }

                                    case MATERIAL_DIFFUSE: {
                                        mesh.material.diffuse.x = Float.parseFloat(values[1]);
                                        mesh.material.diffuse.y = Float.parseFloat(values[2]);
                                        mesh.material.diffuse.z = Float.parseFloat(values[3]);
                                        break;
                                    }

                                    case MATERIAL_SPECULAR: {
                                        mesh.material.specular.x = Float.parseFloat(values[1]);
                                        mesh.material.specular.y = Float.parseFloat(values[2]);
                                        mesh.material.specular.z = Float.parseFloat(values[3]);
                                        break;
                                    }
                                }

                                // Check if the next line contains name of the next material.
                                // If it contains, break out of the loop.
                                // While doing that, don't move the buffer cursor,
                                // so "mark()" and "reset()" method will be used to achieve that
                                br.mark(2000);
                                if((line = br.readLine()) == null || line.contains(MTL_MATERIAL_BIND))
                                {
                                    br.reset();
                                    break;
                                }
                                br.reset();
                            }
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Map vertices, texture coordinates and normals according to the indices of each mesh
        for(mdMesh mesh : meshes)
        {
            mesh.vertices.clear();
            for(Vector3i index : mesh.indices)
            {
                mdVertex vertex = new mdVertex();
                // Get vertices
                // Multiplied by 3, because there is *index.size()* triangles,
                // which means *index.size() * 3* vertices
                vertex.position.x = allVertices.get(index.x * 3);
                vertex.position.y = allVertices.get(index.x * 3 + 1);
                vertex.position.z = allVertices.get(index.x * 3 + 2);

                // Process only if any texture coordinates exists
                if(allTexCoords.isEmpty() == false)
                {
                    // Get texture coordinates(only need x and y)
                    vertex.texCoord.x = allTexCoords.get(index.y * 2);
                    vertex.texCoord.y = allTexCoords.get(index.y * 2 + 1);
                }

                // Process only if any normals exists
                if(allNormals.isEmpty() == false)
                {
                    // Get normals
                    vertex.normal.x = allNormals.get(index.z * 3);
                    vertex.normal.y = allNormals.get(index.z * 3 + 1);
                    vertex.normal.z = allNormals.get(index.z * 3 + 2);
                }
                else
                {
                    // TODO: Generate normals
                }

                mesh.vertices.add(vertex);
            }

            mesh.material.shininess = 32.f;

            mesh.setupMesh();
        }
    }

    public Loader(PrimitiveType type)
    {
        mdMesh mesh = new mdMesh();
        switch(type)
        {
            case Cube: {
                mesh.vertices = arrayToMDVertex(Vertices.cubeVertices, 8);
                break;
            }
            case Plane: {
                mesh.vertices = arrayToMDVertex(Vertices.planeVertices, 8);
                break;
            }
            case Quad: {
                mesh.vertices = arrayToMDVertex(Vertices.quadVertices, 8);
                break;
            }
        }
        mesh.material = new mdMaterial(new Vector3f(0.1f),
                                       new Vector3f(0.1f),
                                       new Vector3f(0.25f),
                                       2.f);
        mesh.setupMesh();
        meshes.add(mesh);
    }

    private mdMesh findMeshByMaterialName(String matName)
    {
        for(mdMesh mesh : meshes)
        {
            if(mesh.material_name.contains(matName))
                return mesh;
        }

        return null;
    }

    private List<mdVertex> arrayToMDVertex(float[] vertices, int stride)
    {
        List<mdVertex> verticesList = new ArrayList<>();

        for(int i = 0; i < vertices.length; i += stride)
        {
            mdVertex vertex = new mdVertex();

            vertex.position.x = vertices[i + 0];
            vertex.position.y = vertices[i + 1];
            vertex.position.z = vertices[i + 2];

            vertex.normal.x = vertices[i + 3];
            vertex.normal.y = vertices[i + 4];
            vertex.normal.z = vertices[i + 5];

            vertex.texCoord.x = vertices[i + 6];
            vertex.texCoord.y = vertices[i + 7];

            verticesList.add(vertex);
        }

        return verticesList;
    }

    // Free GPU memory
    public void free()
    {
        for(mdMesh mesh : meshes)
        {
            for(mdTexture tex : mesh.textures)
            {
                glDeleteTextures(tex.id);
            }
            mesh.textures.clear();

            /*glDeleteBuffers(mesh.vbo);
            glDeleteVertexArrays(mesh.vao);*/
        }
    }
}
