/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Gui constructor creates JFrame and all the panels that are responsible for the general layout.
 *                      Class also uses interface Panel and class PanelEntity, to create GUI components and put them into
 *                      appropriate panels.
 *
 */


package com.michal.debski;

import com.michal.debski.utilities.FpsCounter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.*;

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
    private String currentCardName = "";
    JFileChooser fileChooser = null;

    private int fileBrowserReturnValue = -1;


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

        // Before that, add panel with FPS label.
        fpsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel fpsLabel = new JLabel("FPS: 60.00", JLabel.CENTER);
        fpsLabel.setBorder(BorderFactory.createEtchedBorder(Color.red, Color.black));
        fpsLabel.setPreferredSize(new Dimension(120, 30));
        fpsPanel.add(fpsLabel);
        fpsPanel.setMaximumSize(new Dimension(width, 40));

        FpsCounter.SetFpsLabel(fpsLabel);

        // Set panels layout and border
        loadedModelsPanel.setLayout(new BoxLayout(loadedModelsPanel, BoxLayout.Y_AXIS));
        loadedModelsPanel.setBorder(BorderFactory.createTitledBorder("Models"));

        primitivesPanel.setLayout(new BoxLayout(primitivesPanel, BoxLayout.Y_AXIS));
        primitivesPanel.setBorder(BorderFactory.createTitledBorder("Primitives"));

        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));

        // Add panels to the
        indexPanelContainer.add(fpsPanel);
        indexPanelContainer.add(loadedModelsPanel);
        indexPanelContainer.add(settingsPanel);

        // File browser
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory())
                    return true;
                else
                    return f.getName().toLowerCase().endsWith(".obj");
            }

            @Override
            public String getDescription() {
                return "Wavefront .OBJ";
            }
        });
        JButton fileChooserButton = new JButton("Browser file");
        fileChooserButton.setMaximumSize(new Dimension((int)(Gui.GetWidth() * 0.6f), 30));
        fileChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        fileChooserButton.addActionListener(e -> {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);

            fileBrowserReturnValue = fileChooser.showOpenDialog(this);
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

        /*  Add focus listener, so it will restore model_loader window, when
         *  GUI frame has focus. It will prevent from situations when gui window
         *  is in front and main window is in background
         */
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                Window.FocusWindow();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });
        validate();

        //  Add some basic settings for JFrame
        setType(JFrame.Type.UTILITY);
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

    public void updatePosition()
    {
        setLocation(Core.windowProperties.getPosX() - width,
                Core.windowProperties.getPosY() - windowsTitleBarSize);
    }

    public void Update()
    {
        /*
         * Function: Update
         * -----------------------------------------------
         *  Used to create a Model from a path obtained from JFileChooser. Model cannot be created
         *  inside JFileChooser listener, because it runs on a different thread than the context
         *  created for application. Function is called every frame, and when JFileChooser
         *  action is finished, then function create a new model.
         *
         */

        if(fileBrowserReturnValue == JFileChooser.APPROVE_OPTION)
        {
            // Get selected file
            File selectedFile = fileChooser.getSelectedFile();
            // Get loaded model from the end of the model container(there is only one, but using this
            // technique, it can be easy to have more than 1 model loaded from file)
            Containers.modelContainer.get(Containers.modelContainer.size() - 1).createNew(selectedFile.getAbsolutePath());
            replaceModel();

            // Prevent entering this if statement again.
            fileBrowserReturnValue = JFileChooser.ERROR_OPTION;
        }
    }

    public void createGui(ArrayList<Panel> panelEntityList)
    {
        /*
         * Function:  createGui
         * -----------------------------------------------
         *  Function takes an ArrayList of objects that implements interface Panel
         *  and calls function, which creates a GUI for that specific objects, then
         *  puts it in a correct panel in a correct way
         *
         */

        for(Panel panel : panelEntityList)
        {
            PanelEntity panelEntity =  panel.createPanelEntity();
            panelEntity.getPanel().setBorder(BorderFactory.createTitledBorder(panelEntity.getPanelName()));

            panelEntity.getPanel().setAutoscrolls(true);
            JScrollPane scrollPane = new JScrollPane(panelEntity.getPanel(),
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


            mainPanel.add(scrollPane, panelEntity.getPanelName());


            /*  Every panel created by "createPanelEntity" has it's own setting button, allowing to access that panel.
             *  Also created panel has go back button, and if panel is a panel created from a model loaded from a file, it
             *  will also have "delete" button (currently not active, functionality may be added later on)
             */

            JButton settingButton = new JButton(panelEntity.getPanelName());
            settingButton.setMaximumSize(new Dimension((int)(Gui.GetWidth() * 0.6f), 30));
            settingButton.addActionListener(e -> {
                cardLayout.show(mainPanel, panelEntity.getPanelName());
                currentCardName = panelEntity.getPanelName();
            });

            settingButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Box.createRigidArea creates bottom padding
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

                    // Display settings panel
                    cardLayout.show(mainPanel, "Settings");

                    // Remove card of deleted panel and also delte button, that access deleted panel in a settings card
                    mainPanel.remove(scrollPane);
                    loadedModelsPanel.remove(settingButton);
                    // Find a model that corresponds to deleted panel and delete it(free memory mostly)
                    for(Model model : Containers.modelContainer)
                    {
                        if(model.getName().equals(panelEntity.getPanelName()))
                        {
                            model.delete();
                        }
                    }
                });

                buttonPanel.add(deleteObjectButton, Component.RIGHT_ALIGNMENT);
            }

            buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonPanel.setOpaque(false);
            panelEntity.getPanel().add(buttonPanel, 0);

            panelEntity.getPanel().setAutoscrolls(true);
            revalidate();
            repaint();
        }
    }

    public void replaceModel()
    {
        /*
         *  Method replaces GUI of currently loaded model to a new one
         *
         */

        //if(modelLoadedCount > 0)
        {
            loadedModelsPanel.remove(0);
        }

        // Get the most recently added model and create a panel entity from it.(Containers class provides global access to loaded models)
        PanelEntity panelEntity = Containers.modelContainer.get(Containers.modelContainer.size() - 1).createPanelEntity();
        panelEntity.getPanel().setBorder(BorderFactory.createTitledBorder(panelEntity.getPanelName()));

        // Create scroll panel in case loaded panel will have too many components
        panelEntity.getPanel().setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(panelEntity.getPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Panel is ready to add to main panel(card layout)
        mainPanel.add(scrollPane, panelEntity.getPanelName());


        /*  Every panel created by "createPanelEntity" has it's own setting button, allowing to access that panel.
         *  Also created panel has go back button, and if panel is a panel created from a model loaded from a file, it
         *  will also have "delete" button (currently not active, functionality may be added later on)
         */

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

                validate();
                repaint();
            });
            buttonPanel.add(deleteObjectButton, Component.RIGHT_ALIGNMENT);
        }

        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEntity.getPanel().add(buttonPanel, 0);
        panelEntity.getPanel().setAutoscrolls(true);

        // Remember to validate the layout. Otherwise no changes could be seen.
        validate();
        repaint();

        // Always after new model is loaded, switch to settings panel
        // TODO: switch to settings card only when current card is a previous model's card
        cardLayout.show(mainPanel, "Settings");
    }
}
