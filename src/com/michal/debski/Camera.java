package com.michal.debski;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
        enum CameraMovement
        {
            Forward,
            Backward,
            Left,
            Right
        };

        public Vector3f position;
        public Vector3f front;
        public Vector3f up = new Vector3f();
        public Vector3f right = new Vector3f();
        public Vector3f worldUp;

        public float yaw;
        public float pitch;
        public float movementSpeed;
        public float mouseSensitivity;
        public float zoom;

        private Matrix4f projection;
        private Matrix4f  view;
        private Vector2f screenRes;

		private float YAW			= -90.f;
        private float PITCH			= 0.f;
        private float SPEED			= 5.5f;
        private float SENSITIVITY   = 0.1f;
        private float ZOOM		    = 45.f;

        public Camera(Vector2f screenRes, Vector3f position)
        {
            this.screenRes = screenRes;
            this.front = new Vector3f(0.f, 0.f, -1.f);
            this.movementSpeed = SPEED;
            this.mouseSensitivity = SENSITIVITY;
            this.zoom = ZOOM;

            this.up = new Vector3f(0.f, 1.f, 0.f);
            this.position = position;
            this.worldUp = up;
            this.yaw = YAW;
            this.pitch = PITCH;
            updateCameraVectors();
        }

        Matrix4f getViewMatrix()
        {
            Vector3f pos = new Vector3f();
            position.add(front, pos);
            view = new Matrix4f().lookAt(position, pos, up);

            return view;
        }

        Matrix4f getProjectionMatrix()
        {
            projection = new Matrix4f().perspective((float)Math.toRadians(zoom), screenRes.x / screenRes.y, 0.1f, 100.f);

            return projection;
        }

        void processKeyboard(CameraMovement dir, float dT, float speed)
        {
            speed = SPEED;
        }

        void processMouseMovement(float offsetX, float offsetY)
        {
            offsetX *= mouseSensitivity;
            offsetY *= mouseSensitivity;

            yaw += offsetX;
            pitch -= offsetY;

            if(pitch > 89.f)
                pitch = 89.f;
            if(pitch < -89.f)
                pitch = 89.f;

            updateCameraVectors();
        }

        void processMouseScroll(float offsetY)
        {

        }

        private void updateCameraVectors()
        {
            Vector3f Front = new Vector3f();
            Front.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            Front.y = (float)Math.sin(Math.toRadians(pitch));
            Front.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            Front.normalize(front);

            // Bug hmm
            /*front.cross(worldUp, right);
            right.cross(front, up);*/
            right.normalize();
            up.normalize();
        }
}
