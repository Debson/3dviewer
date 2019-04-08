/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   mdMaterial class is used to store necessary data about material of a mdMesh.
 *
 */

package com.michal.debski.loader;

import com.michal.debski.Panel;
import com.michal.debski.PanelEntity;
import com.michal.debski.utilities.PanelUtility;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;

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


        // Retrieve sliders container and add shininess slider to the end
        JPanel slidersContainer = (JPanel) panel.getComponent(0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        JLabel label = new JLabel("Shininess", JLabel.CENTER);

        // X
        JLabel valueLabel = new JLabel(String.format("%-2.2f", shininess), JLabel.CENTER);
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        valueLabel.setPreferredSize(new Dimension(40, 20));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 128, (int)(shininess));
        slider.addChangeListener(e ->{
            shininess = slider.getValue();
            valueLabel.setText(String.format("%-2.2f", shininess));
        });

        gbc.gridy = slidersContainer.getComponentCount();
        gbc.gridx = 1;
        slidersContainer.add(label, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        label = new JLabel("Value");
        slidersContainer.add(label, gbc);
        gbc.gridx++;
        slidersContainer.add(slider, gbc);
        gbc.gridx++;
        slidersContainer.add(valueLabel, gbc);

        panel.setBorder(BorderFactory.createTitledBorder("Material"));

        return new PanelEntity(panel, "Material", false, false);
    }
}
