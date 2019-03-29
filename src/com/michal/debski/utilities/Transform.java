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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel slidersContainer = new JPanel();
        slidersContainer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        JLabel label = new JLabel("X");
        //label.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        //gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        slidersContainer.add(label, gbc);

        JLabel posXLabel = new JLabel(Float.toString(position.x));
        posXLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        //posXLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        posXLabel.setMinimumSize(new Dimension(40, 20));
        posXLabel.setPreferredSize(new Dimension(40, 20));
        posXLabel.setMaximumSize(new Dimension(40, 20));

        JSlider posXSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.x);
        posXSlider.addChangeListener(e -> {
            position.x = posXSlider.getValue();
            posXLabel.setText(String.format("%-2.2f", position.x));
        });
        /*posXSlider.setMajorTickSpacing(20);
        posXSlider.setPaintTicks(true);
        posXSlider.setPaintLabels(true);*/
        //posXSlider.setMinimumSize(new Dimension(sliderWidth, sliderHeight));
        posXSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        //gbc.anchor = GridBagConstraints.PAGE_START;
        slidersContainer.add(posXSlider);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        //gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        slidersContainer.add(posXLabel, gbc);

        label = new JLabel("Y");
        //label.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        //gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        slidersContainer.add(label, gbc);

        JLabel posYLabel = new JLabel(Float.toString(position.y));
        posYLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        //posXLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        posYLabel.setMinimumSize(new Dimension(40, 20));
        posYLabel.setPreferredSize(new Dimension(40, 20));
        posYLabel.setMaximumSize(new Dimension(40, 20));

        JSlider posYSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.y);
        posYSlider.addChangeListener(e -> {
            position.y = posYSlider.getValue();
            posYLabel.setText(String.format("%-2.2f", position.y));
        });
        /*posXSlider.setMajorTickSpacing(20);
        posXSlider.setPaintTicks(true);
        posXSlider.setPaintLabels(true);*/
        //posYSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        //posXSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        //gbc.anchor = GridBagConstraints.PAGE_START;
        slidersContainer.add(posYSlider);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        //gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        slidersContainer.add(posYLabel, gbc);



        label = new JLabel("Z");
        //label.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        //gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        slidersContainer.add(label, gbc);

        JLabel posZLabel = new JLabel(Float.toString(position.z));
        posZLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        //posXLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        posZLabel.setMinimumSize(new Dimension(40, 20));
        posZLabel.setPreferredSize(new Dimension(40, 20));
        posZLabel.setMaximumSize(new Dimension(40, 20));

        JSlider posZSlider = new JSlider(JSlider.HORIZONTAL, -50, 50, (int)position.z);
        posZSlider.addChangeListener(e -> {
            position.z = posZSlider.getValue();
            posZLabel.setText(String.format("%-2.2f", position.z));
        });
        /*posXSlider.setMajorTickSpacing(20);
        posXSlider.setPaintTicks(true);
        posXSlider.setPaintLabels(true);*/
        //posZSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        //posXSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        //gbc.anchor = GridBagConstraints.PAGE_START;
        slidersContainer.add(posZSlider);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        //gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        slidersContainer.add(posZLabel, gbc);




        JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int)scale.x * 10);
        scaleSlider.addChangeListener(e -> {
            setScale(new Vector3f((float)scaleSlider.getValue() / 10.f));
        });
        scaleSlider.setMajorTickSpacing(10);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPaintLabels(true);
        scaleSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        scaleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        /*label = new JLabel("Position X");
        panel.add(label);
        panel.add(posXSlider);*/

        slidersContainer.setMaximumSize(new Dimension(350, 100));

        panel.add(slidersContainer);


        /*label = new JLabel("Position Y");
        panel.add(label);
        panel.add(posYSlider);
        label = new JLabel("Position Z");
        panel.add(label);
        panel.add(posZSlider);*/
        /*label = new JLabel("Scale(x10)");
        panel.add(label);
        panel.add(scaleSlider);*/

        panel.setBorder(BorderFactory.createTitledBorder("Position"));

        return new PanelEntity(panel, "Transform");
    }
}
