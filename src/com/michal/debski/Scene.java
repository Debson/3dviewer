package com.michal.debski;

import com.michal.debski.environment.DirectionalLight;
import com.michal.debski.environment.Shadows;
import com.michal.debski.loader.Loader;
import com.michal.debski.utilities.Color;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;


import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Scene implements GameHandlerInterface, SceneInterface
{
    private Shader shader;
    private Camera camera;
    private DirectionalLight dirLight;
    private Matrix4f model;
    private float cameraMoveSpeed = 10.f;
    private Model myModel, floor;

    @Override
    public void OnWindowOpen()
    {
        System.out.println("Opened!");

        shader = new Shader("shaders//default.vert", "shaders//default.frag");
        camera = new Camera(new Vector2f(
                WindowProperties.getWidth(),
                WindowProperties.getHeight()),
                new Vector3f(0.f, 15.f, 30.f));


        // Remember to use "\\" directory delimiter! Otherwise there will be an error.
        String path = "assets\\cube.obj";
        String path2 = "assets\\nanosuit\\nanosuit.obj";
        String path3 = "assets\\teapot.obj";
        String path4 = "assets\\teddybear.obj";

        myModel = new Model(path2);
        myModel.setPosition(new Vector3f(0.f, 5.f, 0.f));
        myModel.setScale(new Vector3f(1.f));
        //myModel.setColor(new Color(1.f, 0.5f, 1.f));
        floor = new Model(Loader.PrimitiveType.Plane);
        floor.setColor(new Color(1.f, 0.f, 0.f, 1.f));

        //camera.lockCameraAt(myModel.getPosition(), true);

        dirLight = new DirectionalLight(new Vector3f(20.f, 20.f, 15.f), new Color( 3.5f));
        dirLight.setOribitng(true);
    }

    @Override
    public void OnWindowClose()
    {

    }

    @Override
    public void OnNewFrame()
    {

    }

    @Override
    public void OnFinishFrame()
    {

    }

    @Override
    public void OnRealtimeUpdate()
    {
        processCameraInput();

        if(Input.IsKeyPressed(Keycode.E))
            System.out.println("EEEEEE");
    }

    @Override
    public void OnRealTimeRender()
    {
        model = new Matrix4f();

        ShaderManager.SetShader(shader);
        updateMatrices(shader);

        dirLight.renderSceneWithShadows(this);
    }

    @Override
    public void OnFileDrop(String pathOfDroppedFile)
    {
        myModel = null;
        myModel = new Model(pathOfDroppedFile);
    }

    @Override
    public void updateMatrices(Shader shader)
    {
        shader.use();
        shader.setMat4("projection", camera.getProjectionMatrix());
        shader.setMat4("view", camera.getViewMatrix());
        shader.setMat4("model", model);
    }

    private void processCameraInput()
    {
        Vector2f relMousePos = Input.GetRelativeMousePos();

        if(Input.IsKeyDown(Keycode.MouseMiddle))
            camera.processMouseMovement(relMousePos.x, relMousePos.y);

        float speed = cameraMoveSpeed;

        if(Input.IsKeyDown(Keycode.LShift))
            speed *= 3.f;

        if(Input.IsKeyDown(Keycode.W))
            camera.processKeyboard(Camera.CameraMovement.Forward, (float)Time.deltaTime, speed);
        if(Input.IsKeyDown(Keycode.S))
            camera.processKeyboard(Camera.CameraMovement.Backward, (float)Time.deltaTime, speed);
        if(Input.IsKeyDown(Keycode.A))
            camera.processKeyboard(Camera.CameraMovement.Left, (float)Time.deltaTime, speed);
        if(Input.IsKeyDown(Keycode.D))
            camera.processKeyboard(Camera.CameraMovement.Right, (float)Time.deltaTime, speed);
    }

    @Override
    public void renderScene()
    {
        dirLight.Render(camera.position);
        floor.Render();
        myModel.Render();

    }

    public static void main(String[] args)
    {
        Scene scene = new Scene();
        Game game = new Game(scene);

        game.Open();
        game.Run();
        game.Close();
    }
}
