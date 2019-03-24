package com.michal.debski.loader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12C.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;


public class Image
{

    public static int LoadImage(String path)
    {
        ByteBuffer imageBuffer;
        int textureID = 0;
        int width, height, nrComponents;
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer components = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            imageBuffer = stbi_load(path, w, h, components, 4);
            if(imageBuffer == null)
            {
                throw new RuntimeException("Failed to load an image!" + System.lineSeparator() + stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
            nrComponents = components.get();
            int format = 0;
            if(nrComponents == 1)
                format = GL11.GL_RED;
            else if(nrComponents == 3)
                format = GL11.GL_RGB;
            else if(nrComponents == 4)
                format = GL11.GL_RGBA;

            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, imageBuffer);
            glGenerateMipmap(GL_TEXTURE_2D);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            stbi_image_free(imageBuffer);
        }

        return textureID;
    }
}