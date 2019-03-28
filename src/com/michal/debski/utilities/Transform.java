package com.michal.debski.utilities;

import com.michal.debski.Panel;
import com.michal.debski.PanelEntity;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;

public class Transform
{
    private Vector3f position;
    private Vector3f scale;

    public Transform()
    {
        position = new Vector3f(0.f);
        scale = new Vector3f(1.f);
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }

    public JPanel createPanel()
    {
        /*  setMinimumSize() -- BoxLayout honors this
            setMaximumSize() -- BoxLayout honors this
            setPreferredSize() -- if X_AXIS is being used width is honored, if Y_AXIS is being used height is honored
         */
        int sliderWidth = 200;
        int sliderHeight = 50;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JSlider posXSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.x);
        posXSlider.addChangeListener(e -> {
            position.x = posXSlider.getValue();
        });
        posXSlider.setMajorTickSpacing(20);
        posXSlider.setPaintTicks(true);
        posXSlider.setPaintLabels(true);
        posXSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        posXSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        //posXSlider.setEnabled(false);

        JSlider posYSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int)position.y);
        posYSlider.addChangeListener(e -> {
            position.y = posYSlider.getValue();
        });
        posYSlider.setMajorTickSpacing(10);
        posYSlider.setPaintTicks(true);
        posYSlider.setPaintLabels(true);
        posYSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        posYSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider posZSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.z);
        posZSlider.addChangeListener(e -> {
            position.z = posZSlider.getValue();
        });
        posZSlider.setMajorTickSpacing(20);
        posZSlider.setPaintTicks(true);
        posZSlider.setPaintLabels(true);
        posZSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        posZSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("Position X");
        panel.add(label);
        panel.add(posXSlider);
        label = new JLabel("Position Y");
        panel.add(label);
        panel.add(posYSlider);
        label = new JLabel("Position Z");
        panel.add(label);
        panel.add(posZSlider);

        return panel;
    }
}
