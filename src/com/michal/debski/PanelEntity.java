package com.michal.debski;

import javax.swing.*;

public class PanelEntity
{
    private String panelName;
    private JPanel panel;
    private boolean isModel = false;
    private boolean isPrimitive = false;

    public PanelEntity(JPanel panel, String panelName, boolean isModel, boolean isPrimitive)
    {
        this.panel = panel;
        this.panelName = panelName;
        this.isModel = isModel;
        this.isPrimitive = isPrimitive;
    }

    public String getPanelName()
    {
        return panelName;
    }

    public void setPanelName(String panelName)
    {
        this.panelName = panelName;
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public void setPanel(JPanel panel)
    {
        this.panel = panel;
    }

    public boolean isModel()
    {
        return isModel;
    }

    public boolean isPrimitive()
    {
        return isPrimitive;
    }
}
