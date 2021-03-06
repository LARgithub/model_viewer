package Engine;

import java.util.List;
import java.util.Map;

import Entities.Model;
import Shaders.UniqueShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Models.RawModel;
import Models.TexturedModel;
import Shaders.StaticShader;
import Textures.ModelTexture;
import Toolbox.Maths;

public class Renderer 
{
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	public Renderer(StaticShader shader) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		createProjectionMatrix();
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
	}

	public Renderer(UniqueShader shader){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		createProjectionMatrix();

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void prepare3D()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0,0,0,1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void prepare2D()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GL11.glOrtho(0, 1280, 0, 720, -1, 1);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void render3D(Map<TexturedModel, List<Model>> entities, StaticShader shader) {
		
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model, shader);
			List<Model> batch = entities.get(model);
			
			for(Model object :batch) {
				prepareInstance(object, shader);
				GL11.glDrawElements(GL11.GL_TRIANGLES,  model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		
	}

	public void render3D(Map<TexturedModel, List<Model>> entities, UniqueShader shader) {

		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model, shader);
			List<Model> batch = entities.get(model);

			for(Model object :batch) {
				prepareInstance(object, shader);
				GL11.glDrawElements(GL11.GL_TRIANGLES,  model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}

	}
	
	public void render2D(Map<TexturedModel, List<Model>> entities2D) {}
	
	public void prepareTexturedModel(TexturedModel model, StaticShader shader) {

		RawModel rawModel = model.getRawModel();

		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		ModelTexture texture = model.getTexture();

		shader.loadShineVars(texture.getShineDamper(), texture.getReflectivity());


		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

	}

	public void prepareTexturedModel(TexturedModel model, UniqueShader shader) {

		RawModel rawModel = model.getRawModel();

		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		ModelTexture texture = model.getTexture();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

	}
	
	public void unbindTexturedModel() {
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	
	private void prepareInstance(Model model, StaticShader shader) {
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(model.getPosition(), model.getRx(), model.getRy(), model.getRz(), model.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
	}

	private void prepareInstance(Model model, UniqueShader shader) {

		Matrix4f transformationMatrix = Maths.createTransformationMatrix(model.getPosition(), model.getRx(), model.getRy(), model.getRz(), model.getScale());
		shader.loadTransformationMatrix(transformationMatrix);

	}
	
	
//	public void render(Model entity, StaticShader shader)
//	{
//		TexturedModel model = entity.getModel();
//		RawModel rawModel = model.getRawModel();
//		
//		GL30.glBindVertexArray(rawModel.getVaoID());
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
//		GL20.glEnableVertexAttribArray(2);
//		
//		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
//		
//		shader.loadTransformationMatrix(transformationMatrix);
//		ModelTexture texture = model.getTexture();
//		shader.loadShineVars(texture.getShineDamper(), texture.getReflectivity());
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getID());
//		
//		GL11.glDrawElements(GL11.GL_TRIANGLES,  rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//		GL20.glDisableVertexAttribArray(2);
//		GL30.glBindVertexArray(0);
//	
//	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
		float x_scale = (y_scale / aspectRatio);
		float len_frustum = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / len_frustum);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / len_frustum);
		projectionMatrix.m33 = 0;		
	}

}
