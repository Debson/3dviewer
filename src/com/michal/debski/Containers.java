package com.michal.debski;


import java.util.ArrayList;
import java.util.List;

public class Containers
{
    public static ArrayList<Panel> panelContainer = new ArrayList<>();


    public static void AddPanelContainer(Panel panelEntity)
    {
        panelContainer.add(panelEntity);
    }

    public static ArrayList<Panel> GetPanelContainer(ArrayList<Panel> panelEntityContainer)
    {
        return panelEntityContainer;
    }
}
