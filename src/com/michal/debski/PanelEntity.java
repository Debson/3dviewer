package com.michal.debski;

import javax.swing.*;

public class PanelEntity
{
    private String panelName;

    private JPanel panel;

    public PanelEntity(JPanel panel, String panelName)
    {
        this.panel = panel;
        this.panelName = panelName;
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
}
