package Entities;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;

public class Model
{
	
	private TexturedModel model;
	private Vector3f position;
	private float Rx, Ry, Rz;
	private float scale;
	
	public void translate(float Dx, float Dy, float Dz)
	{
		this.position.x += Dx;
		this.position.y += Dy;
		this.position.z += Dz;
	}
	
	public void rotate(float Dx, float Dy, float Dz)
	{
		this.Rx += Dx;
		this.Ry += Dy;
		this.Rz += Dz;
	}
	
	public Model(TexturedModel model , Vector3f position, float rx, float ry, float rz, float scale) {
		super();
		this.model = model;
		this.position = position;
		Rx = rx;
		Ry = ry;
		Rz = rz;
		this.scale = scale;
	}

	public void scale(float newScale){ scale += newScale; }
	
	public TexturedModel getModel() { return model; }
	public Vector3f getPosition() { return position; }
	public float getRx() { return Rx; }
	public float getRy() { return Ry; }
	public float getRz() { return Rz; }
	public float getScale() { return scale; }
	
}
