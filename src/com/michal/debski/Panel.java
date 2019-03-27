package com.michal.debski;

import javax.swing.*;
import java.awt.*;

public interface Panel
{
    PanelEntity createPanelEntity(JPanel mainPanel, CardLayout mainPanelCardLayout);
}
