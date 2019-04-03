package com.michal.debski;

import com.michal.debski.utilities.FpsCounter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.io.File;
import java.util.ArrayList;


public class Gui extends JFrame
{
    private static int width = 380;
    private int windowsTitleBarSize = 30;

    private JPanel globalPanel = new JPanel();
    private JPanel mainPanel = new JPanel();

    JPanel indexPanelContainer = new JPanel();
    private JPanel fpsPanel = new JPanel();
    private JPanel loadedModelsPanel = new JPanel();
    private JPanel primitivesPanel = new JPanel();
    private JPanel settingsPanel = new JPanel();

    private JScrollPane settingsScrollPanel;
    private JPanel titleBar = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    private int modelLoadedCount = 0;
    private String currentCardName = "";
    private int fileChooserReturnValue = -1;

    private int settingsButtonsCounter = 0;

    public Gui()
    {
        super("Settings");
        mainPanel.setLayout(cardLayout);

        setLayout(new GridLayout(2, 1));

        // Create title bar
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(new Color(255,140,0));
        titleBar.setPreferredSize(new Dimension(width - 10, windowsTitleBarSize - 5));
        titleBar.setBorder(new BevelBorder(BevelBorder.RAISED));
        JLabel label = new JLabel("Settings", JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        titleBar.add(label, BorderLayout.CENTER);

        // Split the frame into two separate panels. One is a title bar, other one
        // is a panel that contains all buttons and settings for objects
        globalPanel.add(titleBar, BorderLayout.CENTER);
        globalPanel.add(mainPanel);

        // Adjust main panel's dimensions
        mainPanel.setPreferredSize(new Dimension(width - 10, Core.windowProperties.getHeight() - 10));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        // Set up indexPanelContainer(contains three categories, models, primitives and settings)
        indexPanelContainer.setLayout(new BoxLayout(indexPanelContainer, BoxLayout.Y_AXIS));

        fpsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel fpsLabel = new JLabel("FPS: 60.00", JLabel.CENTER);
        fpsLabel.setBorder(BorderFactory.createEtchedBorder(Color.red, Color.black));
        fpsLabel.setPreferredSize(new Dimension(120, 30));
        fpsPanel.add(fpsLabel);
        fpsPanel.setMaximumSize(new Dimension(width, 40));

        FpsCounter.SetFpsLabel(fpsLabel);

        loadedModelsPanel.setLayout(new BoxLayout(loadedModelsPanel, BoxLayout.Y_AXIS));
        loadedModelsPanel.setBorder(BorderFactory.createTitledBorder("Models"));

        primitivesPanel.setLayout(new BoxLayout(primitivesPanel, BoxLayout.Y_AXIS));
        primitivesPanel.setBorder(BorderFactory.createTitledBorder("Primitives"));

        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));

        indexPanelContainer.add(fpsPanel);
        indexPanelContainer.add(loadedModelsPanel);
        indexPanelContainer.add(settingsPanel);

        // File browser
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        JButton fileChooserButton = new JButton("Browser file");
        fileChooserButton.setMaximumSize(new Dimension((int)(Gui.GetWidth() * 0.6f), 30));
        fileChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        fileChooser.addActionListener(e -> {
            if(fileChooserReturnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                System.out.println(selectedFile.getAbsolutePath());
                fileChooserReturnValue = -1;

            }

        });
        fileChooserButton.addActionListener(e -> {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooserReturnValue = fileChooser.showOpenDialog(this);
        });


        settingsPanel.add(fileChooserButton);

        // Make it scrollable and put it in scroll pane, so if there will be
        // more buttons than panel's dimension allow, the vertical scroll bar will appear
        indexPanelContainer.setAutoscrolls(true);
        settingsScrollPanel = new JScrollPane(indexPanelContainer);

        mainPanel.add(settingsScrollPanel, "Settings");
        cardLayout.show(mainPanel, "Settings");
        currentCardName = "Settings";

        setContentPane(globalPanel);

        // Create border around the frame
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add focus listener, so it will restore model_loader window, when
        // GUI frame has focus
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

        // Set cross platform feel and look for the Java Swing
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public static int GetWidth()
    {
        return width;
    }

    public void setPosition()
    {
        setLocation(Core.windowProperties.getPosX() - width,
                Core.windowProperties.getPosY() - windowsTitleBarSize);
    }

    /*
     * Function:  createGui
     * -----------------------------------------------
     *  Function takes an ArrayList of objects that implements interface Panel
     *  and calls function, which creates a GUI for that specific objects, then
     *  puts it in a correct panel in a correct way
     *
     */

    public void createGui(ArrayList<Panel> panelEntityList)
    {
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
            settingButton.setMaximumSize(new Dimension((int)(Gui.GetWidth() * 0.6f), 30));
            settingButton.addActionListener(e -> {
                cardLayout.show(mainPanel, panelEntity.getPanelName());
                currentCardName = panelEntity.getPanelName();
            });

            settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Create bottom padding for buttons
            if(panelEntity.isModel())
            {
                if (loadedModelsPanel.getComponentCount() > 0)
                    loadedModelsPanel.add(Box.createRigidArea(new Dimension(0, 15)), Component.LEFT_ALIGNMENT);
                loadedModelsPanel.add(settingButton);
            }
            else if(panelEntity.isPrimitive())
            {
                if(indexPanelContainer.getComponentCount() < 4)
                {
                    indexPanelContainer.add(primitivesPanel, 2);
                }
                if (primitivesPanel.getComponentCount() > 0)
                    primitivesPanel.add(Box.createRigidArea(new Dimension(0, 15)), Component.LEFT_ALIGNMENT);
                primitivesPanel.add(settingButton);
            }
            else
            {
                if (settingsPanel.getComponentCount() > 0)
                    settingsPanel.add(Box.createRigidArea(new Dimension(0, 15)), Component.LEFT_ALIGNMENT, 0);
                settingsPanel.add(settingButton, 0);
            }

            // Bottom GO BACK button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            JButton goBackButton = new JButton("Go back");
            goBackButton.addActionListener(e -> {
                cardLayout.show(mainPanel, "Settings");
                currentCardName = "Settings";
            });
            buttonPanel.add(goBackButton, Component.LEFT_ALIGNMENT);

            buttonPanel.add(Box.createHorizontalGlue());

            // Delete button
            if(panelEntity.isModel())
            {
                JButton deleteObjectButton = new JButton("Delete");

                // Disable that button for now. Might enable in further development
                deleteObjectButton.setEnabled(false);

                deleteObjectButton.addActionListener(e -> {
                    if(currentCardName.equals(panelEntity.getPanelName()))
                    {
                        cardLayout.show(mainPanel, "Settings");
                    }
                    mainPanel.remove(scrollPane);
                    loadedModelsPanel.remove(settingButton);
                    for(Model model : Containers.modelContainer)
                    {
                        if(model.getName().equals(panelEntity.getPanelName()))
                        {
                            model.delete();
                        }
                    }
                    modelLoadedCount--;
                });
                buttonPanel.add(deleteObjectButton, Component.RIGHT_ALIGNMENT);
                modelLoadedCount++;
            }

            buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonPanel.setOpaque(false);
            panelEntity.getPanel().add(buttonPanel, 0);

            panelEntity.getPanel().setAutoscrolls(true);
            revalidate();
            repaint();
        }
    }

    /*
     * Function: replaceModel
     * -----------------------------------------------
     *  Function that replaces GUI for currently loaded model.
     *
     */
    public void replaceModel()
    {
        if(modelLoadedCount > 0)
        {
            //mainPanel.remove(1);
            loadedModelsPanel.remove(0);
        }

        PanelEntity panelEntity = Containers.panelContainer.get(Containers.panelContainer.size() - 1).createPanelEntity();

        panelEntity.getPanel().setBorder(BorderFactory.createTitledBorder(panelEntity.getPanelName()));

        panelEntity.getPanel().setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(panelEntity.getPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Bug
        mainPanel.add(scrollPane, panelEntity.getPanelName());


        JButton settingButton = new JButton(panelEntity.getPanelName());
        settingButton.setMaximumSize(new Dimension((int)(Gui.GetWidth() * 0.6f), 30));
        settingButton.addActionListener(e -> {
            cardLayout.show(mainPanel, panelEntity.getPanelName());
            currentCardName = panelEntity.getPanelName();
        });

        settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //loadedModelsPanel.add(Box.createRigidArea(new Dimension(0, 15)), Component.LEFT_ALIGNMENT);
        loadedModelsPanel.add(settingButton, 0);

        // Bottom GO BACK button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton goBackButton = new JButton("Go back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Settings");
            currentCardName = "Settings";
        });
        buttonPanel.add(goBackButton, Component.LEFT_ALIGNMENT);

        buttonPanel.add(Box.createHorizontalGlue());

        // Delete button
        if(panelEntity.isModel())
        {
            JButton deleteObjectButton = new JButton("Delete");

            // Disable that button for now. Might enable in further development
            deleteObjectButton.setEnabled(false);

            deleteObjectButton.addActionListener(e -> {
                if(currentCardName.equals(panelEntity.getPanelName()))
                {
                    cardLayout.show(mainPanel, "Settings");
                }
                mainPanel.remove(scrollPane);
                loadedModelsPanel.remove(settingButton);
                for(Model model : Containers.modelContainer)
                {
                    if(model.getName().equals(panelEntity.getPanelName()))
                    {
                        model.delete();
                    }
                }
                modelLoadedCount--;
                validate();
                repaint();
            });
            buttonPanel.add(deleteObjectButton, Component.RIGHT_ALIGNMENT);
            modelLoadedCount++;
        }

        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEntity.getPanel().add(buttonPanel, 0);
        panelEntity.getPanel().setAutoscrolls(true);
        validate();
        repaint();

        cardLayout.show(mainPanel, "Settings");
    }
}
