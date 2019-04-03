/* Date: 03/04/2019
 * Developer: Michal Debski
 * Github: github.com/debson
 * Class description:   Camera class calculates all the properties of a 3D camera.
 *
 */

package com.michal.debski;

import com.michal.debski.utilities.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Camera implements Panel
{
    enum CameraMovement
        {
            Forward,
            Backward,
            Left,
            Right
        };

        public Transform transform = new Transform();
        private Vector3f prevPosition = new Vector3f();
        public Vector3f front = new Vector3f();
        public Vector3f up = new Vector3f();
        public Vector3f right = new Vector3f();
        public Vector3f worldUp = new Vector3f();

        public float yaw;
        public float pitch;
        public float movementSpeed;
        public float mouseSensitivity;
        public float zoom;

        private Matrix4f projection;
        private Matrix4f  view;
        private Vector2f screenRes;
        private Vector3f positionToLookAt = null;
        private boolean cameraLocked = false;

        private float cameraLockedXZFactor = 30.f;
        private float cameraLockedYFactor = 30.f;

		private float YAW			= -90.f;
        private float PITCH			= 0.f;
        private float SPEED			= 5.5f;
        private float SENSITIVITY   = 0.1f;
        private float ZOOM		    = 45.f;

        JLabel posXLabel = new JLabel();
        JLabel posYLabel = new JLabel();
        JLabel posZLabel = new JLabel();
        JLabel yawLabel = new JLabel();
        JLabel pitchLabel = new JLabel();

        public Camera(Vector2f screenRes, Vector3f position)
        {
            this.screenRes = screenRes;
            this.front = new Vector3f(0.f, 0.f, -1.f);
            this.movementSpeed = SPEED;
            this.mouseSensitivity = SENSITIVITY;
            this.zoom = ZOOM;

            this.up = new Vector3f(0.f, 1.f, 0.f);
            this.transform.setPosition(position);
            this.worldUp = up;
            this.yaw = YAW;
            this.pitch = PITCH;



            updateCameraVectors();

            Containers.panelContainer.add(this);
        }

        public Matrix4f getViewMatrix()
        {
            Vector3f pos = new Vector3f();
            transform.getPosition().add(front, pos);
            //position.add(front, pos);
            if(cameraLocked)
            {
                transform.setPosition(new Vector3f((float)Math.sin(Time.GetTicks()) * cameraLockedXZFactor,
                                            15.f,
                                        (float)Math.cos(Time.GetTicks()) * cameraLockedXZFactor));
                transform.getPosition().y = cameraLockedYFactor;
                transform.getPosition().add(front, pos);
                view = new Matrix4f().lookAt(transform.getPosition(), positionToLookAt, up);
            }
            else
                view = new Matrix4f().lookAt(transform.getPosition(), pos, up);


            // Update labels
            if(prevPosition.equals(transform.getPosition()) == false)
            {
                prevPosition = new Vector3f(transform.getPosition());
                posXLabel.setText(String.format("%-2.2f", transform.getPosition().x));
                posYLabel.setText(String.format("%-2.2f", transform.getPosition().y));
                posZLabel.setText(String.format("%-2.2f", transform.getPosition().z));
            }

            return view;
        }

        public Matrix4f getProjectionMatrix()
        {
            //projection = new Matrix4f().perspective((float)Math.toRadians(zoom), screenRes.x / screenRes.y, 0.1f, 1000.f);

            return new Matrix4f().perspective((float)Math.toRadians(zoom), screenRes.x / screenRes.y, 0.1f, 1000.f);
        }

        public void lockCameraAt(Vector3f positionToLookAt, boolean cameraLocked)
        {
            this.positionToLookAt = new Vector3f(positionToLookAt);
            this.cameraLocked = cameraLocked;
        }

        public void processKeyboard(CameraMovement dir, float dT, float speed)
        {
            // Different functionality when camera is locked at object
            float velocity = speed * dT;
            Vector3f pos = new Vector3f();
            switch(dir)
            {
                case Forward: {
                    if (cameraLocked)
                    {
                        cameraLockedYFactor += velocity;
                    }
                    else
                    {
                        front.mul(velocity, pos);
                        transform.getPosition().add(pos);
                    }
                    break;
                }
                case Backward: {
                    if(cameraLocked)
                    {
                        cameraLockedYFactor -= velocity;
                    }
                    else
                    {
                        front.mul(velocity, pos);
                        transform.getPosition().sub(pos);
                    }
                    break;
                }
                case Left: {
                    if(cameraLocked)
                    {
                        cameraLockedXZFactor += velocity;
                    }
                    else {
                        right.mul(velocity, pos);
                        transform.getPosition().sub(pos);
                    }
                    break;
                }
                case Right: {
                    if(cameraLocked)
                    {
                        cameraLockedXZFactor -= velocity;
                    }
                    else {
                        right.mul(velocity, pos);
                        transform.getPosition().add(pos);
                    }
                    break;
                }
            }
        }

        public void processMouseMovement(float offsetX, float offsetY)
        {
            if(cameraLocked)
            {
                positionToLookAt.y -= (offsetY * mouseSensitivity);
            }
            else
            {
                offsetX *= mouseSensitivity;
                offsetY *= mouseSensitivity;

                yaw += offsetX;
                pitch -= offsetY;

                if (pitch > 89.f)
                    pitch = 89.f;
                if (pitch < -89.f)
                    pitch = -89.f;

                yawLabel.setText(String.format("%-2.2f", yaw));
                pitchLabel.setText(String.format("%-2.2f", pitch));

                updateCameraVectors();
            }
        }

        public void processMouseScroll(float offsetY)
        {

        }

        private void updateCameraVectors()
        {
            Vector3f Front = new Vector3f();
            Front.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            Front.y = (float)Math.sin(Math.toRadians(pitch));
            Front.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            Front.normalize(front);

            Vector3f r = new Vector3f();
            Vector3f u = new Vector3f();
            front.cross(worldUp, r).normalize();
            right = r;
            right.cross(front, u).normalize();
            up = u;

        }

    @Override
    public PanelEntity createPanelEntity()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel transformPanel = new JPanel();
        transformPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 5, 15);
        posXLabel = new JLabel(String.format("%-2.2f", transform.getPosition().x), JLabel.CENTER);
        posXLabel.setPreferredSize(new Dimension(40, 20));
        posXLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        posYLabel = new JLabel(String.format("%-2.2f", transform.getPosition().y), JLabel.CENTER);
        posYLabel.setPreferredSize(new Dimension(40, 20));
        posYLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        posZLabel = new JLabel(String.format("%-2.2f", transform.getPosition().z), JLabel.CENTER);
        posZLabel.setPreferredSize(new Dimension(40, 20));
        posZLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        yawLabel = new JLabel(String.format("%-2.2f", yaw), JLabel.CENTER);
        yawLabel.setPreferredSize(new Dimension(40, 20));
        yawLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        pitchLabel = new JLabel(String.format("%-2.2f", pitch), JLabel.CENTER);
        pitchLabel.setPreferredSize(new Dimension(40, 20));
        pitchLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel label = new JLabel("X");
        gbc.weightx = 0;
        transformPanel.add(label, gbc);
        gbc.weightx = 0.1;
        gbc.gridx++;
        transformPanel.add(posXLabel, gbc);
        gbc.gridx++;
        label = new JLabel("Y");
        gbc.weightx = 0;
        transformPanel.add(label, gbc);
        gbc.weightx = 0.1;
        gbc.gridx++;
        transformPanel.add(posYLabel, gbc);
        gbc.gridx++;
        label = new JLabel("Z");
        gbc.weightx = 0;
        transformPanel.add(label, gbc);
        gbc.weightx = 0.1;
        gbc.gridx++;
        transformPanel.add(posZLabel, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        label = new JLabel("Yaw");
        transformPanel.add(label, gbc);
        gbc.gridx++;
        transformPanel.add(yawLabel, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        label = new JLabel("Pitch");
        transformPanel.add(label, gbc);
        gbc.gridx++;
        transformPanel.add(pitchLabel, gbc);


        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

        JCheckBox lockCameraAt = new JCheckBox("Lock camera at Model");
        lockCameraAt.addItemListener(e -> {
            cameraLocked = lockCameraAt.isSelected();
            positionToLookAt = new Vector3f(0.f, 0.f, 0.f);
            front = new Vector3f(0.f, 0.f, -1.f);
            up = new Vector3f(0.f, 1.f, 0.f);
        });

        lockCameraAt.setAlignmentX(Component.LEFT_ALIGNMENT);

        checkBoxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBoxPanel.add(lockCameraAt);

        transformPanel.setBorder(BorderFactory.createTitledBorder("Position"));
        transformPanel.setMaximumSize(new Dimension(Gui.GetWidth(), transformPanel.getPreferredSize().height));
        transformPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(transformPanel);
        panel.add(checkBoxPanel);

        return new PanelEntity(panel, "Camera", false, false);
    }
}
