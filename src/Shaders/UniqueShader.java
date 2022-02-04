package Shaders;

import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Light;
import Toolbox.Maths;

public class UniqueShader extends ShaderProgram
{

    private static final String VERTEX_FILE = "src/Shaders/UniqueVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/Shaders/UniqueFragmentShader.txt";

    private int location_transformationmatrix;
    private int location_projectionmatrix;
    private int location_viewmatrix;

    public UniqueShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
        super.bindAttrib(1, "texcoords");
        super.bindAttrib(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationmatrix = super.getUniformLocations("transformationmatrix");
        location_projectionmatrix = super.getUniformLocations("projectionmatrix");
        location_viewmatrix = super.getUniformLocations("viewmatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationmatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewmatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionmatrix, matrix);
    }
}
