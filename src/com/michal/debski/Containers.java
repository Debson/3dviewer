package com.michal.debski;


import java.util.ArrayList;
import java.util.List;

public class Containers
{
    public static List<Panel> panelContainer = new ArrayList<>();


    public static void AddPanelContainer(Panel panelEntity)
    {
        panelContainer.add(panelEntity);
    }

    public static List<Panel> GetPanelContainer(List<Panel> panelEntityContainer)
    {
        return panelEntityContainer;
    }
}
