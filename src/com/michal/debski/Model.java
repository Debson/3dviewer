package com.michal.debski;

import com.michal.debski.loader.Loader;
import com.michal.debski.loader.mdMesh;
import com.michal.debski.utilities.Color;
import com.michal.debski.utilities.Transform;
import org.joml.Matrix4f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class Model extends Loader implements Panel
{
    private Color color = new Color(1.f);
    private String name;
    private Matrix4f matrixModel;

    private Transform transform = new Transform();

    public Model(String path)
    {
        super(path);

        name = new String(path);
        int pos = name.lastIndexOf('\\');
        int dotPos = name.lastIndexOf('.');
        name = name.substring(pos + 1, dotPos);
        Containers.AddPanelContainer(this);
    }

    public Model(String name, PrimitiveType type)
    {
        super(type);
        this.name = name;
        Containers.AddPanelContainer(this);
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void Render()
    {
        ShaderManager.GetShader().use();
        matrixModel = new Matrix4f().translate(transform.getPosition()).scale(transform.getScale());
        ShaderManager.GetShader().setVec4("color", color.r, color.g, color.b, color.a);
        ShaderManager.GetShader().setMat4("model", matrixModel);
        for(mdMesh mesh : super.meshes)
        {
            mesh.Render();
        }
    }

    @Override
    public PanelEntity createPanelEntity()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel transformPanel = getTransform().createPanelEntity().getPanel();
        JPanel colorPanel = getColor().createPanelEntity().getPanel();
        JPanel materialPanel = new JPanel();

        panel.add(transformPanel);
        panel.add(colorPanel);
        if(meshes.size() == 1) {
            materialPanel = meshes.get(0).material.createPanelEntity().getPanel();
            panel.add(materialPanel);
        }



        // MESH INFORMATION
        JPanel meshInfoPanel = new JPanel();
        meshInfoPanel.setLayout(new GridBagLayout());
        meshInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        meshInfoPanel.setBorder(BorderFactory.createTitledBorder("Mesh information"));



        int verticesCount = 0;
        int indicesCount = 0;
        int texturesCount = 0;
        for(mdMesh mesh : meshes)
        {
            verticesCount += mesh.vertices.size();
            indicesCount += mesh.indices.size();
            texturesCount += mesh.textures.size();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel("Meshes Size: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 0.5;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 0);
        meshInfoPanel.add(label, gbc);

        label = new JLabel(Integer.toString(meshes.size()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        meshInfoPanel.add(label, gbc);

        label = new JLabel("Vertices: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        meshInfoPanel.add(label, gbc);


        label = new JLabel(Integer.toString(verticesCount));
        gbc.gridx = 1;
        gbc.gridy = 1;
        meshInfoPanel.add(label, gbc);


        label = new JLabel("Indices: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        meshInfoPanel.add(label, gbc);

        label = new JLabel(Integer.toString(indicesCount));
        gbc.gridx = 1;
        gbc.gridy = 2;
        meshInfoPanel.add(label, gbc);

        label = new JLabel("Textures: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        meshInfoPanel.add(label, gbc);

        label = new JLabel(Integer.toString(texturesCount));
        gbc.gridx = 1;
        gbc.gridy = 3;
        meshInfoPanel.add(label, gbc);


        panel.add(meshInfoPanel, BorderLayout.PAGE_START);


        return new PanelEntity(panel, this.name);
    }
}
