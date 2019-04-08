/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Light class has all the properties of a light and additional functionality.
 *                      Class also manages rendering scene with a shadow.
 *
 */


package com.michal.debski.environment;

import com.michal.debski.*;
import com.michal.debski.loader.Loader;
import com.michal.debski.utilities.Colour;

import com.michal.debski.utilities.PanelUtility;
import com.michal.debski.utilities.mdTimer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

import static org.lwjgl.opengl.GL30.*;

public class Light extends Model
{
    protected boolean lightActive = true;
    private boolean orbitingActive = false;
    private Vector3f orbitingPosition = null;
    private float orbitingRadius = 10.f;
    private float orbitingSpeed = 2.f;

    private boolean renderLightCube = true;
    private static boolean castShadows = true;
    private Shadows shadows = null;
    private mdTimer orbitingTimer = new mdTimer();


    public Light(Vector3f position)
    {
        super("Light", Loader.PrimitiveType.Cube);
        setColor(new Colour(1.f));
        this.getTransform().setPosition(position);
        shadows = new Shadows(this.getTransform().getPosition());
    }

    public Light(Vector3f position, Colour color)
    {
        super("Light", Loader.PrimitiveType.Cube);
        this.getTransform().setPosition(position);
        setColor(color);
        shadows = new Shadows(this.getTransform().getPosition());
    }

    public void on()
    {
        this.lightActive = true;
    }

    public void off()
    {
        this.lightActive = false;
    }

    public void setOribitng(boolean orbiting)
    {
        this.orbitingActive = orbiting;
        if(orbiting) {
            orbitingTimer.start();
            orbitingRadius = new Vector2f().distance(new Vector2f(getTransform().getPosition().x, getTransform().getPosition().z));
        }
        else {
            orbitingTimer.stop();
        }
    }

    public void setOrbitingSpeed(float orbitingSpeed)
    {
        this.orbitingSpeed = orbitingSpeed;
    }

    public void setOrbitingRadius(float orbitingRadius)
    {
        this.orbitingRadius = orbitingRadius;
    }

    public void setOrbitingAroundPosition(Vector3f orbitingPosition, float orbitingRadius, float orbitingSpeed, boolean orbiting)
    {
        setOribitng(orbiting);
        this.orbitingRadius = orbitingRadius;
        this.orbitingSpeed = orbitingSpeed;
        this.orbitingPosition = orbitingPosition;
    }

    public void renderLightCube(boolean val)
    {
        this.renderLightCube = val;
    }

    public void Render(Vector3f cameraPosition)
    {
        /*
        * This method will be always overwritten anyway.
        *
        * */

        ShaderManager.GetShader().use();
        renderLight();
        if(lightActive)
        {
            ShaderManager.GetShader().setVec3("light.direction", getTransform().getPosition());
            Colour color = getColor();
            ShaderManager.GetShader().setVec3("light.color", color.r * getTransform().getScale().x,
                    color.g * getTransform().getScale().y,
                    color.b * getTransform().getScale().z);
        }
    }

    protected void renderLight()
    {
        if(renderLightCube)
        {
            ShaderManager.GetShader().setBool("lightActive", false);
            if(orbitingActive)
            {
                getTransform().getPosition().x = (float)Math.sin(orbitingTimer.getCurrentTime() * orbitingSpeed) * orbitingRadius;
                getTransform().getPosition().z = (float)Math.cos(orbitingTimer.getCurrentTime() * orbitingSpeed) * orbitingRadius;
            }

            Render();
        }

        ShaderManager.GetShader().setBool("lightActive", lightActive);
    }

    public void renderSceneWithShadows(SceneInterface scene)
    {
        ShaderManager.GetShader().use();
        ShaderManager.GetShader().setBool("shadowsActive", castShadows);
        ShaderManager.GetShader().setBool("lightActive", lightActive);

        if(castShadows)
        {
            // Save previously used shader
            Shader tempShader = ShaderManager.GetShader();

            // Fill the shadow buffer rendering current scene into it
            shadows.fillDepthMap(scene);

            // Switch to a default shader(shadows.fillDepthMap changes the global shader to shadow's shader)
            ShaderManager.SetShader(tempShader);

            // Render scene as normal
            ShaderManager.GetShader().setInt("shadows.depthMap", 6);
            ShaderManager.GetShader().setMat4("lightSpaceMatrix", shadows.getLightSpaceMatrix());
            glActiveTexture(shadows.shaderTextureNum);
            glBindTexture(GL_TEXTURE_2D, shadows.getDepthMap());
        }
    }

    public static void CastShadows(boolean cs)
    {
        castShadows = cs;
    }

    public static boolean ShadowsEnabled()
    {
        return castShadows;
    }

    @Override
    public PanelEntity createPanelEntity()
    {
        JPanel panel = new JPanel();
        String panelName = "Light";
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Get panel with transform settings
        JPanel transformPanel = getTransform().createPanelEntity().getPanel();

        // Create panel for orbiting settings
        JPanel orbitingPanel = new JPanel();
        orbitingPanel.setLayout(new GridBagLayout());

        // Create panel for Light and Shadows settings
        JPanel environmentPanel = new JPanel();
        environmentPanel.setLayout(new GridBagLayout());

        // Configure oribiting panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(2,2,5,2);

        JLabel radiusLabel = new JLabel(String.format("%-2.2f", orbitingRadius));
        radiusLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        radiusLabel.setPreferredSize(new Dimension(20, 20));

        JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, (int)orbitingRadius);
        radiusSlider.addChangeListener(e -> {
            orbitingRadius = radiusSlider.getValue();
            radiusLabel.setText(String.format("%-2.2f", orbitingRadius));
        });

        JLabel speedLabel = new JLabel(String.format("%-2.2f", orbitingSpeed));
        speedLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        speedLabel.setPreferredSize(new Dimension(20, 20));

        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int)orbitingSpeed * 10);
        speedSlider.addChangeListener(e -> {
            orbitingSpeed = (float)speedSlider.getValue() / 10.f;
            speedLabel.setText(String.format("%-2.2f", orbitingSpeed));
        });

        JCheckBox orbitingCheckBox = new JCheckBox("Is orbiting");
        orbitingCheckBox.setSelected(this.orbitingActive);
        PanelUtility.SetPanelEnabled(transformPanel, !this.orbitingActive);
        radiusSlider.setEnabled(this.orbitingActive);
        speedSlider.setEnabled(this.orbitingActive);
        orbitingCheckBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                setOribitng(true);
                PanelUtility.SetPanelEnabled(transformPanel, false);
                radiusSlider.setEnabled(true);
                speedSlider.setEnabled(true);
            }
            else {
                setOribitng(false);
                PanelUtility.SetPanelEnabled(transformPanel, true);
                radiusSlider.setEnabled(false);
                speedSlider.setEnabled(false);
            }
        });


        JLabel label = new JLabel("Orbiting radius", JLabel.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        orbitingPanel.add(label, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        label = new JLabel("Value");
        orbitingPanel.add(label, gbc);
        gbc.gridx++;
        orbitingPanel.add(radiusSlider, gbc);
        gbc.gridx++;
        orbitingPanel.add(radiusLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy++;
        label = new JLabel("Orbiting speed", JLabel.CENTER);
        orbitingPanel.add(label, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        label = new JLabel("Value");
        orbitingPanel.add(label, gbc);
        gbc.gridx++;
        orbitingPanel.add(speedSlider, gbc);
        gbc.gridx++;
        orbitingPanel.add(speedLabel, gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        orbitingPanel.add(orbitingCheckBox, gbc);

        orbitingPanel.setMaximumSize(new Dimension(Gui.GetWidth(), 160));
        orbitingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        orbitingPanel.setBorder(BorderFactory.createTitledBorder("Orbiting"));


        // Configure environment panel
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        gbc.gridy = 0;
        gbc.insets = new Insets(2,2,5,2);
        JCheckBox shadowsCheckBox = new JCheckBox("Cast Shadows");
        shadowsCheckBox.setSelected(this.castShadows);
        shadowsCheckBox.addItemListener(e -> {
            castShadows = shadowsCheckBox.isSelected();
        });

        JCheckBox lightCheckBox = new JCheckBox("Cast Lights");
        lightCheckBox.setSelected(this.lightActive);
        lightCheckBox.addItemListener(e -> {
            lightActive = lightCheckBox.isSelected();
            shadowsCheckBox.setSelected(lightCheckBox.isSelected() && castShadows);
        });


        environmentPanel.add(shadowsCheckBox, gbc);
        gbc.gridy++;
        environmentPanel.add(lightCheckBox, gbc);

        environmentPanel.setMaximumSize(new Dimension(Gui.GetWidth(), 100));
        environmentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        environmentPanel.setBorder(BorderFactory.createTitledBorder("Environment"));

        // Create color picker panel
        JPanel colorPanel = getColor().createPanelEntity().getPanel();

        // Compose a main panel from previously created and configured panels
        panel.add(transformPanel);
        panel.add(colorPanel);
        panel.add(orbitingPanel);
        panel.add(environmentPanel);

        return new PanelEntity(panel, panelName, false, false);
    }
}
