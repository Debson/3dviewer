/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   mdMesh class is used to store necessary data about mesh, which is a part of the Model.
 *                      Class also loads obtained data to the graphics memory, through OpenGL, and is responsible
 *                      for rendering a mesh.
 *
 */

package com.michal.debski.loader;

import com.michal.debski.ShaderManager;
import org.joml.Vector3i;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;
import java.util.*;

import static com.michal.debski.loader.Loader.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class mdMesh
{
    public String name                      = "";
    public List<Loader.mdVertex> vertices   = new ArrayList<Loader.mdVertex>();
    public List<Vector3i> indices           = new ArrayList<Vector3i>();
    public List<Loader.mdTexture> textures  = new ArrayList<>();
    public String material_name             = "";
    public mdMaterial material              = new mdMaterial(1.f);

    public int vao, vbo;
    public boolean hasVertices = false;
    public boolean hasTexCoods = false;
    public boolean hasNormals = false;


    public mdMesh()
    {

    }

    // Copy constructor
    public mdMesh(mdMesh other)
    {
        this.name           = other.name;
        this.vertices       = other.vertices;
        this.indices        = other.indices;
        this.textures       = other.textures;
        this.material_name  = other.material_name;
        this.hasVertices    = other.hasVertices;
        this.hasTexCoods    = other.hasTexCoods;
        this.hasNormals     = other.hasTexCoods;
        this.material       = other.material;
        this.vao = other.vao;
        this.vbo = other.vbo;
    }

    public void setupMesh()
    {
        /*
        * Send loaded data to the GPU in a specific sequence
        *
        * */

        // Multiplied by 8, because 3 position vertices, 3 normals, 2 texture coordinates
        float[] vertsArray = new float[vertices.size() * 8];

        int posIndex = 0;
        try
        {
            for (int i = 0; i < vertices.size(); i++)
            {
                vertsArray[posIndex + 0] = vertices.get(i).position.x;
                vertsArray[posIndex + 1] = vertices.get(i).position.y;
                vertsArray[posIndex + 2] = vertices.get(i).position.z;

                vertsArray[posIndex + 3] = vertices.get(i).normal.x;
                vertsArray[posIndex + 4] = vertices.get(i).normal.y;
                vertsArray[posIndex + 5] = vertices.get(i).normal.z;

                vertsArray[posIndex + 6] = vertices.get(i).texCoord.x;
                vertsArray[posIndex + 7] = vertices.get(i).texCoord.y;

                posIndex += 8;
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

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_SIZE, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_SIZE, 3 * FLOAT_SIZE);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * FLOAT_SIZE, 6 * FLOAT_SIZE);
        glEnableVertexAttribArray(2);
    }

    public void Render()
    {
        // Access global shader
        ShaderManager.GetShader().use();

        ShaderManager.GetShader().setBool("textureActive", false);
        for(int i = 0; i < textures.size(); i++)
        {
            Loader.mdTexture texture = textures.get(i);
            glActiveTexture(GL_TEXTURE0 + i);

            ShaderManager.GetShader().setInt("material." + texture.type + "Map", i);
            ShaderManager.GetShader().setBool("textureActive", true);

            // TODO: problem with accessing mdShader methods from Shader class object
            glBindTexture(GL_TEXTURE_2D, textures.get(i).id);
        }

        ShaderManager.GetShader().setVec3("material.ambient", material.ambient);
        ShaderManager.GetShader().setVec3("material.diffuse", material.diffuse);
        ShaderManager.GetShader().setVec3("material.specular", material.specular);
        ShaderManager.GetShader().setFloat("material.shininess", material.shininess);


        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertices.size());

        // Unbind VERTEX ARRAY BUFFER & TEXTURE
        glBindVertexArray(0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}