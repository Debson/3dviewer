package com.michal.debski.utilities;

import com.michal.debski.Panel;
import com.michal.debski.PanelEntity;
import org.joml.Vector3f;
import org.joml.Vector3i;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class Transform implements Panel
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

    public PanelEntity createPanelEntity()
    {
        /*  setMinimumSize() -- BoxLayout honors this
            setMaximumSize() -- BoxLayout honors this
            setPreferredSize() -- if X_AXIS is being used width is honored, if Y_AXIS is being used height is honored
         */
        int sliderWidth = 200;
        int sliderHeight = 50;

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JPanel slidersContainer = new JPanel();
        slidersContainer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        JLabel posXLabel = new JLabel(Float.toString(position.x));
        posXLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        posXLabel.setPreferredSize(new Dimension(40, 20));

        JSlider posXSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.x);
        posXSlider.addChangeListener(e -> {
            position.x = posXSlider.getValue();
            posXLabel.setText(String.format("%-2.2f", position.x));
        });

        JLabel posYLabel = new JLabel(Float.toString(position.y));
        posYLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        posYLabel.setPreferredSize(new Dimension(20, 20));

        JSlider posYSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.y);
        posYSlider.addChangeListener(e -> {
            position.y = posYSlider.getValue();
            posYLabel.setText(String.format("%-2.2f", position.y));
        });


        JLabel posZLabel = new JLabel(Float.toString(position.z));
        posZLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        posZLabel.setPreferredSize(new Dimension(20, 20));

        JSlider posZSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.z);
        posZSlider.addChangeListener(e -> {
            position.z = posZSlider.getValue();
            posZLabel.setText(String.format("%-2.2f", position.z));
        });


        JLabel scaleLabel = new JLabel(Float.toString(position.z));
        scaleLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        scaleLabel.setPreferredSize(new Dimension(20, 20));

        JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int)scale.x * 10);
        scaleSlider.addChangeListener(e -> {
            setScale(new Vector3f((float)scaleSlider.getValue() / 10.f));
            scaleLabel.setText(String.format("%-2.2f", getScale().x));
        });



        JLabel label = new JLabel("X");
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(2, 2, 2, 2);
        slidersContainer.add(label, gbc);
        gbc.gridx++;
        slidersContainer.add(posXSlider, gbc);
        gbc.gridx++;
        slidersContainer.add(posXLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        label = new JLabel("Y");
        slidersContainer.add(label, gbc);
        gbc.gridx++;
        slidersContainer.add(posYSlider, gbc);
        gbc.gridx++;
        slidersContainer.add(posYLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        label = new JLabel("Z");
        slidersContainer.add(label, gbc);
        gbc.gridx++;
        slidersContainer.add(posZSlider, gbc);
        gbc.gridx++;
        slidersContainer.add(posZLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        label = new JLabel("Scale");
        slidersContainer.add(label, gbc);
        gbc.gridx++;
        slidersContainer.add(scaleSlider, gbc);
        gbc.gridx++;
        slidersContainer.add(scaleLabel, gbc);


        slidersContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(slidersContainer);

        panel.setPreferredSize(new Dimension(200, 140));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createTitledBorder("Position"));

        return new PanelEntity(panel, "Transform");
    }
}
