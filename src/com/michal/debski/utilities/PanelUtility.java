/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Helper class, used to reduce redundancy in code, when creating panels for a different objects
 *
 */

package com.michal.debski.utilities;

import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class PanelUtility
{
    public static void SetPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component instanceof JPanel) {
                SetPanelEnabled((JPanel) component, isEnabled);
            }
            component.setEnabled(isEnabled);
        }
    }

    public static JPanel CreatePanelFromMultipleVector3f(String[] labels, Vector3f[] vectorList, int[] minVals, int[] maxVals, int multiplier)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel slidersContainer = new JPanel();
        slidersContainer.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        // Put it into GirdLayout with 3 columns. First label, slider and slider value label
        for(int i = 0; i < labels.length; i++)
        {
            JLabel label = new JLabel(labels[i], JLabel.CENTER);

            Vector3f vector = vectorList[i];

            // X
            JLabel valueXLabel = new JLabel(String.format("%-2.2f", vector.x), JLabel.CENTER);
            valueXLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            valueXLabel.setPreferredSize(new Dimension(40, 20));

            JSlider sliderX = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.x * multiplier));
            sliderX.addChangeListener(e ->{
                vector.x = (float)sliderX.getValue() / (float)multiplier;
                valueXLabel.setText(String.format("%-2.2f", vector.x));
            });

            JLabel valueYLabel = new JLabel(String.format("%-2.2f", vector.x), JLabel.CENTER);
            valueYLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            valueYLabel.setPreferredSize(new Dimension(40, 20));


            // Y
            JSlider sliderY = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.y * multiplier));
            sliderY.addChangeListener(e ->{
                vector.y = (float)sliderY.getValue() / (float)multiplier;
                valueYLabel.setText(String.format("%-2.2f", vector.y));
            });


            // Z
            JLabel valueZLabel = new JLabel(String.format("%-2.2f", vector.x), JLabel.CENTER);
            valueZLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            valueZLabel.setPreferredSize(new Dimension(40, 20));

            JSlider sliderZ = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.z * multiplier));
            sliderZ.addChangeListener(e ->{
                vector.z = (float)sliderZ.getValue() / (float)multiplier;
                valueZLabel.setText(String.format("%-2.2f", vector.z));
            });


            // XYZ
            JLabel valueXYZLabel = new JLabel(String.format("%-2.2f", vector.x), JLabel.CENTER);
            valueXYZLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            valueXYZLabel.setPreferredSize(new Dimension(40, 20));

            JSlider sliderXYZ = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.z * multiplier));
            sliderXYZ.addChangeListener(e ->{
                vector.x = (float)sliderXYZ.getValue() / (float)multiplier;
                vector.y = (float)sliderXYZ.getValue() / (float)multiplier;
                vector.z = (float)sliderXYZ.getValue() / (float)multiplier;

                sliderX.setValue(sliderXYZ.getValue());
                sliderY.setValue(sliderXYZ.getValue());
                sliderZ.setValue(sliderXYZ.getValue());
                valueXYZLabel.setText(String.format("%-2.2f", vector.x));
            });

            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.gridx = 0;


            // X
            gbc.gridx = 1;
            slidersContainer.add(label, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            label = new JLabel("X");
            slidersContainer.add(label, gbc);
            gbc.gridx++;
            slidersContainer.add(sliderX, gbc);
            gbc.gridx++;
            slidersContainer.add(valueXLabel, gbc);

            // Y
            gbc.gridy++;
            gbc.gridx = 0;
            label = new JLabel("Y");
            slidersContainer.add(label, gbc);
            gbc.gridx++;
            slidersContainer.add(sliderY, gbc);
            gbc.gridx++;
            slidersContainer.add(valueYLabel, gbc);

            // Z
            gbc.gridy++;
            gbc.gridx = 0;
            label = new JLabel("Z");
            slidersContainer.add(label, gbc);
            gbc.gridx++;
            slidersContainer.add(sliderZ, gbc);
            gbc.gridx++;
            slidersContainer.add(valueZLabel, gbc);

            // XYZ
            gbc.gridy++;
            gbc.gridx = 0;
            label = new JLabel("XYZ");
            slidersContainer.add(label, gbc);
            gbc.gridx++;
            slidersContainer.add(sliderXYZ, gbc);
            gbc.gridx++;
            slidersContainer.add(valueXYZLabel, gbc);

            // Additional row for a new Vector
            gbc.gridy++;

            panel.add(slidersContainer);


            //panel.setPreferredSize(new Dimension(panelWidth, 400));
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.setBorder(BorderFactory.createTitledBorder("Material"));
        }

        return panel;
    }

    public static JPanel CreateSliderFloat(String labelName, float[] value, float min, float max)
    {
        int multiplier = 50;

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel(labelName);

        JLabel valueLabel = new JLabel(String.format("%-2.2f", value[0]));
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        valueLabel.setPreferredSize(new Dimension(40, 20));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, (int)(min * multiplier), (int)(max * multiplier), (int)(value[0] * multiplier));
        slider.addChangeListener(e ->{
            value[0] = (float)slider.getValue() / (float)multiplier;
            valueLabel.setText(String.format("%-2.2f", value[0]));
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0;

        panel.add(label, gbc);
        gbc.gridx++;
        panel.add(slider, gbc);
        gbc.gridx++;
        panel.add(valueLabel, gbc);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

}
