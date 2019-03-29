package com.michal.debski.loader;

import com.michal.debski.Panel;
import com.michal.debski.PanelEntity;
import com.michal.debski.utilities.PanelUtility;
import org.joml.Vector3f;

import javax.swing.*;

public class mdMaterial implements Panel
{
    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;
    public float shininess;

    public mdMaterial(float initValue)
    {
        this.ambient = new Vector3f(initValue);
        this.diffuse = new Vector3f(initValue);
        this.specular = new Vector3f(initValue);
        this.shininess = 16.f;
    }

    public mdMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess)
    {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    @Override
    public PanelEntity createPanelEntity()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        /*
        * Ambient
        * slider
        * slider
        * slider
        *
        * Diffuse
        * slider
        * slider
        * slider
        *
        * Specular
        * slider
        * slider
        * slider
        *
        * Shininess
        * slider
        * */

        panel = PanelUtility.CreatePanelFromMultipleVector3f(new String[]{"Ambient", "Diffuse", "Specular"},
                new Vector3f[]{ambient, diffuse, specular},
                new int[]{0, 0, 0},
                new int[]{100, 100 , 100},
                100);

        panel.setBorder(BorderFactory.createTitledBorder("Material"));

        return new PanelEntity(panel, "Material");
    }
}
