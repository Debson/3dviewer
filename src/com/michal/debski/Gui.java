package com.michal.debski;

import org.joml.Vector2i;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class Gui extends JFrame
{
    private int width = 300;
    private int height = 300;
    private int windowsTitleBarSize = 30;


    private JPanel globalPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel settingsPanel = new JPanel();
    private JScrollPane settingsScrollPanel = new JScrollPane();
    private JPanel titleBar = new JPanel();
    private CardLayout cardLayout = new CardLayout();

    private int settingsButtonsCounter = 0;

    public Gui()
    {
        super("Settings");
        mainPanel.setLayout(cardLayout);

        setLayout(new BoxLayout(globalPanel, BoxLayout.Y_AXIS));

        titleBar.setBackground(Color.GREEN);
        titleBar.setPreferredSize(new Dimension(width, windowsTitleBarSize));
        JLabel label = new JLabel("Settings");
        label.setFont(new Font("Serif", Font.BOLD, 18));
        titleBar.add(label);



        globalPanel.add(titleBar);
        globalPanel.add(mainPanel);


        GridBagConstraints g = new GridBagConstraints();
        settingsPanel.setLayout(new GridBagLayout());
        /*g.insets = new Insets(1000, 0, 0, 0);
        for(int i = 0; i < 5; i++) {
            JButton button = new JButton(String.valueOf(i));

            g.gridy++;
            g.insets = new Insets((i != 0) ? 5 : 0, -width / 2, 0 ,0);

            button.setPreferredSize(new Dimension(200,50));
            button.addActionListener(e -> {
                System.out.println(button.getName());
            });
            settingsPanel.add(button, g);
        }

        // Push the buttons to the top using JPanel as a filler
        g.gridy++;
        g.weighty = 1.f;
        g.weightx = 1.f;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        settingsPanel.add(filler, g);*/

        settingsPanel.setAutoscrolls(true);
        settingsScrollPanel = new JScrollPane(settingsPanel);
        settingsScrollPanel.setPreferredSize(new Dimension(width, Core.windowProperties.getHeight() - 5));


        mainPanel.add(settingsScrollPanel, "Settings");
        cardLayout.show(mainPanel, "Settings");

        

        setContentPane(globalPanel);

        setType(JFrame.Type.UTILITY);
        //setAlwaysOnTop(true);
        setUndecorated(true);

        setBounds(Core.windowProperties.getPosX() - width,
                Core.windowProperties.getPosY() - windowsTitleBarSize,
                width, Core.windowProperties.getHeight() + windowsTitleBarSize);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public void addToMainPanel(JPanel panel, String panelName)
    {
        /*mainPanel.add(panel, panelName);
        settingsButtonsCounter++;
        //cardLayout.show(mainPanel, panelName);
        validate();*/
    }

    public void setPosition()
    {
        setLocation(Core.windowProperties.getPosX() - width,
                Core.windowProperties.getPosY() - windowsTitleBarSize);
    }

    public void createGui(ArrayList<Panel> panelEntityList)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        for(Panel panel : panelEntityList)
        {
            PanelEntity panelEntity =  panel.createPanelEntity(mainPanel, cardLayout);
            mainPanel.add(panelEntity.getPanel(), panelEntity.getPanelName());

            JButton settingButton = new JButton(panelEntity.getPanelName());
            settingButton.addActionListener(e -> {
                cardLayout.show(mainPanel, panelEntity.getPanelName());
            });

            gbc.gridy++;
            gbc.insets = new Insets((settingsButtonsCounter != 0) ? 5 : 0, -width / 2, 0 ,0);
            settingsPanel.add(settingButton, gbc);
        }
        gbc.gridy++;
        gbc.weighty = 1.f;
        gbc.weightx = 1.f;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        settingsPanel.add(filler, gbc);

    }
}
