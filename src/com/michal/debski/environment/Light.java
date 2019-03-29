package com.michal.debski.environment;

import com.michal.debski.*;
import com.michal.debski.Panel;
import com.michal.debski.loader.Loader;
import com.michal.debski.utilities.Color;

import com.michal.debski.utilities.PanelUtility;
import com.michal.debski.utilities.mdTimer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;

import static org.lwjgl.opengl.GL30.*;

public class Light extends Model
{
    // This should be the parent class. Child classes: directional light, point light
    // Light method render should be called before any drawing, so it will set boolean variable
    // that manages lighting in shader to true, so the scene will be drawn with lighting.
    protected float strength;
    protected boolean lightActive = true;
    private boolean orbiting = false;
    private Vector3f orbitingPosition = null;
    private float orbitingRadius = 10.f;
    private float orbitingSpeed = 2.f;

    private boolean renderLightCube = true;
    private boolean castShadows = true;
    private Shadows shadows = null;
    private mdTimer orbitingTimer = new mdTimer();


    public Light(Vector3f position)
    {
        super("Light", Loader.PrimitiveType.Cube);
        setColor(new Color(1.f));
        this.getTransform().setPosition(position);
        shadows = new Shadows(this.getTransform().getPosition());
    }

    public Light(Vector3f position, Color color)
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
        this.orbiting = orbiting;
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
        ShaderManager.GetShader().use();
        renderLight();
        if(lightActive)
        {

            /*ShaderManager.GetShader().setVec3("light.direction", getTransform().getPosition());
            //ShaderManager.GetShader().setVec3("lightPos", position);
            ShaderManager.GetShader().setVec3("light.color", color.r, color.g, color.b);*/
        }
    }

    protected void renderLight()
    {
        if(renderLightCube)
        {
            ShaderManager.GetShader().setBool("lightActive", false);
            if(orbiting)
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
        Shader tempShader = ShaderManager.GetShader();
        shadows.fillDepthMap(scene);

        ShaderManager.SetShader(tempShader);

        // Render scene as normal
        ShaderManager.GetShader().use();
        ShaderManager.GetShader().setInt("shadows.depthMap", 6);
        ShaderManager.GetShader().setMat4("lightSpaceMatrix", shadows.getLightSpaceMatrix());
        //ShaderManager.GetShader().setBool("shadows.switchedOn", true);
        glActiveTexture(shadows.shaderTextureNum);
        glBindTexture(GL_TEXTURE_2D, shadows.getDepthMap());
    }

    @Override
    public PanelEntity createPanelEntity()
    {
        JPanel panel = new JPanel();
        String panelName = "Light";
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        // Get panel with transform settings
        JPanel transformPanel = getTransform().createPanelEntity().getPanel();

        // Change scale label to "Intensity"
        /*JLabel scaleLabel = (JLabel) transformPanel.getComponent(6);
        scaleLabel.setText("Intensity");*/

                //settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        JPanel orbitingPanel = new JPanel();
        orbitingPanel.setLayout(new BoxLayout(orbitingPanel, BoxLayout.PAGE_AXIS));


        JLabel label = new JLabel("Orbiting Radius");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        orbitingPanel.add(label);

        int sliderWidth = 200;
        int sliderHeight = 50;
        JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int)orbitingRadius);
        radiusSlider.addChangeListener(e -> {
            orbitingRadius = radiusSlider.getValue();
        });
        radiusSlider.setMajorTickSpacing(10);
        radiusSlider.setPaintTicks(true);
        radiusSlider.setPaintLabels(true);
        radiusSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        radiusSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        orbitingPanel.add(radiusSlider);

        label = new JLabel("Orbiting Speed");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        orbitingPanel.add(label);

        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int)orbitingSpeed * 10);
        speedSlider.addChangeListener(e -> {
            orbitingSpeed = (float)speedSlider.getValue() / 10.f;
        });
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setMaximumSize(new Dimension(sliderWidth, sliderHeight));
        speedSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        orbitingPanel.add(speedSlider);


        JCheckBox orbitingCheckBox = new JCheckBox("Is orbiting: ");
        orbitingCheckBox.setSelected(this.orbiting);
        PanelUtility.SetPanelEnabled(transformPanel, !this.orbiting);
        radiusSlider.setEnabled(this.orbiting);
        speedSlider.setEnabled(this.orbiting);
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

        orbitingPanel.add(orbitingCheckBox);

        // Create color picker panel
        JPanel colorPanel = getColor().createPanelEntity().getPanel();

        orbitingPanel.add(colorPanel);

        panel.add(transformPanel);
        panel.add(orbitingPanel);

        panel.setPreferredSize(new Dimension(100, 100));

        return new PanelEntity(panel, panelName);
    }
}
