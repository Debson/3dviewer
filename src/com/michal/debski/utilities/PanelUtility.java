package com.michal.debski.utilities;

import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        int panelWidth = 200;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setMaximumSize(new Dimension(panelWidth, 0));


        // Put it into GirdLayout with 3 columns. First label, slider and slider value label
        for(int i = 0; i < labels.length; i++)
        {
            JLabel label = new JLabel(labels[i]);
            panel.add(label);

            Vector3f vector = vectorList[i];
            System.out.println(vectorList[i].x * 100);
            JSlider sliderX = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.x * multiplier));
            sliderX.setAlignmentX(Component.LEFT_ALIGNMENT);
            sliderX.addChangeListener(e ->{
                vector.x = (float)sliderX.getValue() / (float)multiplier;
            });

            panel.add(sliderX);


            JSlider sliderY = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.y * multiplier));
            sliderY.setAlignmentX(Component.LEFT_ALIGNMENT);
            sliderY.addChangeListener(e ->{
                vector.y = (float)sliderY.getValue() / (float)multiplier;
            });

            panel.add(sliderY);

            JSlider sliderZ = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.z * multiplier));
            sliderZ.setAlignmentX(Component.LEFT_ALIGNMENT);
            sliderZ.addChangeListener(e ->{
                vector.z = (float)sliderZ.getValue() / (float)multiplier;
            });

            panel.add(sliderZ);

            JSlider sliderAll = new JSlider(JSlider.HORIZONTAL, minVals[i], maxVals[i], (int)(vector.z * multiplier));
            sliderAll.setAlignmentX(Component.LEFT_ALIGNMENT);
            sliderAll.addChangeListener(e ->{
                vector.x = (float)sliderAll.getValue() / (float)multiplier;
                vector.y = (float)sliderAll.getValue() / (float)multiplier;
                vector.z = (float)sliderAll.getValue() / (float)multiplier;

                sliderX.setValue(sliderAll.getValue());
                sliderY.setValue(sliderAll.getValue());
                sliderZ.setValue(sliderAll.getValue());
            });

            panel.add(sliderAll);
        }

        return panel;
    }

}
