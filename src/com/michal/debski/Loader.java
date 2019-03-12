package com.michal.debski;

import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memFree;


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
    class mdVertex
    {
        Vector3f position = new Vector3f();
        Vector3f normal = new Vector3f();
        Vector2f texCoord = new Vector2f();
        Vector3f tangent = new Vector3f();
        Vector3f bitangen = new Vector3f();
    }

    class mdMesh
    {
        public String name = "";
        public List<mdVertex> vertices = new ArrayList<mdVertex>();
        public List<Vector3i> indices      = new ArrayList<Vector3i>();
        private int vao, vbo, ebo;
        private boolean hasVertices = false;

        public mdMesh()
        {

        }

        // Class that
        public mdMesh(mdMesh other)
        {
            this.name       = other.name;
            this.vertices   = other.vertices;
            this.indices    = other.indices;
            this.vao = other.vao;
            this.vbo = other.vbo;
            this.ebo = other.ebo;
        }

        public void setupMesh()
        {
            float[] vertsArray = new float[vertices.size() * 3];

            int posIndex = 0;
            try
            {
                for (int i = 0; i < vertices.size(); i++)
                {
                    vertsArray[posIndex] = vertices.get(i).position.x;
                    vertsArray[posIndex + 1] = vertices.get(i).position.y;
                    vertsArray[posIndex + 2] = vertices.get(i).position.z;
                    posIndex += 3;
                }
            }
            catch(BufferOverflowException e)
            {
                System.out.println("Exception throws: " + e);
            }

            // Create a float buffer, which can passed through OpenGL API to the GPU
            FloatBuffer verts = MemoryUtil.memAllocFloat(vertsArray.length);
            verts.put(vertsArray).flip();

            vao = glGenVertexArrays();
            glBindVertexArray(vao);
            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
            memFree(verts);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
            glEnableVertexAttribArray(0);
        }

        public void Render()
        {
            glBindVertexArray(vao);
            //glDrawElements(GL_TRIANGLES,  indices.size(), GL_UNSIGNED_INT, 0);
            glDrawArrays(GL_TRIANGLES, 0, vertices.size());
            glBindVertexArray(0);

        }

    }

    public Vector<mdMesh> meshes = new Vector<mdMesh>();

    public Loader(String path)
    {
        // The idea is to read all vertices, texture coordinates and normals
        // from file and then map them to objects basing on their faces.
        // Every object has it's own data about faces
        List<Float> allVertices = new ArrayList<Float>();
        List<Float> allTexCoords = new ArrayList<Float>();
        List<Float> allNormals = new ArrayList<Float>();

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
                        for(int i = 1; i < values.length; i++) {
                            allVertices.add(Float.parseFloat(values[i]));
                            mesh.hasVertices = true;
                        }
                        break;
                    }
                    case VERTEX_TEXTURE_COORDINATE: {
                        for(int i = 1; i < values.length; i++)
                            allTexCoords.add(Float.parseFloat(values[i]));
                        break;
                    }
                    case VERTEX_NORMAL: {
                        for(int i = 1; i < values.length; i++)
                            allNormals.add(Float.parseFloat(values[i]));
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
                    case FACE: {
                        for(int i = 1; i < values.length; i++)
                        {
                            String[] temp = values[i].split("/");
                            // Save indices as a three-dimensional vector
                            // Consider that there may not be texture coordinates or normals
                            // From parsed value subtract 1, because indices in OpenGL starts at 0
                            if(temp.length == 1) {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        0,
                                        0));
                            } else if(temp.length == 2)
                            {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        Integer.parseInt(temp[1]) - 1,
                                        0));
                            } else
                            {
                                mesh.indices.add(new Vector3i(
                                        Integer.parseInt(temp[0]) - 1,
                                        Integer.parseInt(temp[1]) - 1,
                                        Integer.parseInt(temp[2]) - 1));
                            }
                        }
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

        for(int i = 0; i < meshes.size(); i++)
        {
            meshes.get(i).vertices.clear();
            for(Vector3i indice : meshes.get(i).indices)
            {
                mdVertex vertex = new mdVertex();
                // Get vertices
                vertex.position.x = allVertices.get(indice.x * 3);
                vertex.position.y = allVertices.get(indice.x * 3 + 1);
                vertex.position.z = allVertices.get(indice.x * 3 + 2);

                // Process only if any texture coordinates exists
                if(allTexCoords.isEmpty() == false)
                {
                    // Get texture coordinates(only need 2)
                    vertex.texCoord.x = allTexCoords.get((int) indice.z * 3);
                    vertex.texCoord.y = allTexCoords.get((int) indice.z * 3 + 1);
                }

                // Process only if any normals exists
                if(allNormals.isEmpty() == false)
                {
                    // Get normals
                    vertex.normal.x = allNormals.get(indice.z * 3);
                    vertex.normal.y = allNormals.get(indice.z * 3 + 1);
                    vertex.normal.z = allNormals.get(indice.z * 3 + 2);
                }

                meshes.get(i).vertices.add(vertex);
            }
            meshes.get(i).setupMesh();

            // Make sure there is no objects that have no data in the meshes container(that should never happen if .OBJ file is valid)
            if(meshes.elementAt(i).vertices.isEmpty())
                meshes.remove(i);
        }
    }
}
