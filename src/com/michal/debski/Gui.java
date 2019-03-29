package com.michal.debski;

import org.joml.Vector2i;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class Gui extends JFrame
{
    private int width = 350;
    private int height = 300;
    private int windowsTitleBarSize = 30;


    private JPanel globalPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel settingsPanel = new JPanel();
    private JScrollPane settingsScrollPanel;
    private JPanel titleBar = new JPanel();
    private CardLayout cardLayout = new CardLayout();

    private int settingsButtonsCounter = 0;

    public Gui()
    {
        super("Settings");
        mainPanel.setLayout(cardLayout);

        setLayout(new GridLayout(2, 1));

        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(new Color(255,140,0));
        titleBar.setPreferredSize(new Dimension(width - 10, windowsTitleBarSize - 5));
        titleBar.setBorder(new BevelBorder(BevelBorder.RAISED));
        JLabel label = new JLabel("Settings");
        label.setFont(new Font("Serif", Font.BOLD, 18));
        titleBar.add(label, BorderLayout.CENTER);

        globalPanel.add(titleBar, BorderLayout.CENTER);
        globalPanel.add(mainPanel);
        mainPanel.setPreferredSize(new Dimension(width - 10, Core.windowProperties.getHeight() - 10));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));

        settingsPanel.setAutoscrolls(true);
        settingsScrollPanel = new JScrollPane(settingsPanel);


        mainPanel.add(settingsScrollPanel, "Settings");
        cardLayout.show(mainPanel, "Settings");

        setContentPane(globalPanel);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                Window.FocusWindow();
                Window.RestoreWindow();
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        validate();

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
        int buttonCounter = 0;
        for(Panel panel : panelEntityList)
        {
            PanelEntity panelEntity =  panel.createPanelEntity();
            panelEntity.getPanel().setBorder(BorderFactory.createTitledBorder(panelEntity.getPanelName()));

            panelEntity.getPanel().setAutoscrolls(true);
            JScrollPane scrollPane = new JScrollPane(panelEntity.getPanel(),
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            mainPanel.add(scrollPane, panelEntity.getPanelName());

            JButton settingButton = new JButton(panelEntity.getPanelName());
            settingButton.setMaximumSize(new Dimension(200, 30));
            settingButton.addActionListener(e -> {
                cardLayout.show(mainPanel, panelEntity.getPanelName());
            });

            settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Create bottom padding for buttons
            if(buttonCounter > 0)
                settingsPanel.add(Box.createRigidArea(new Dimension(0, 15)), Component.LEFT_ALIGNMENT);
            settingsPanel.add(settingButton);
            buttonCounter++;

            // Bottom GO BACK button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            JButton goBackButton = new JButton("Go back");
            goBackButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Settings");
            });
            buttonPanel.add(goBackButton);
            buttonPanel.setOpaque(false);


            panelEntity.getPanel().add(buttonPanel, 0);
            panelEntity.getPanel().setAutoscrolls(true);
            validate();
            repaint();
        }
    }


    public void replaceModel(String modelName, Model newModel)
    {
        mainPanel.remove(1);
        settingsPanel.remove(0);

        PanelEntity panelEntity = Containers.panelContainer.get(Containers.panelContainer.size() - 1).createPanelEntity();

        panelEntity.getPanel().setBorder(BorderFactory.createTitledBorder(panelEntity.getPanelName()));

        panelEntity.getPanel().setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(panelEntity.getPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, panelEntity.getPanelName());

        JButton settingButton = new JButton(panelEntity.getPanelName());
        settingButton.setMaximumSize(new Dimension(200, 30));
        settingButton.addActionListener(e -> {
            cardLayout.show(mainPanel, panelEntity.getPanelName());
        });

        settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Create bottom padding for buttons

        settingsPanel.add(Box.createRigidArea(new Dimension(0, 15)), Component.LEFT_ALIGNMENT);
        settingsPanel.add(settingButton, 0);

        // Bottom GO BACK button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton goBackButton = new JButton("Go back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Settings");
        });
        buttonPanel.add(goBackButton);
        buttonPanel.setOpaque(false);


        panelEntity.getPanel().add(buttonPanel, 0);
        panelEntity.getPanel().setAutoscrolls(true);
        validate();
        repaint();

        validate();
        repaint();
    }
}
