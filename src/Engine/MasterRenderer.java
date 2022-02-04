package Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entities.Camera;
import Entities.Model;
import Entities.Light;
import Models.TexturedModel;
import Shaders.UniqueShader;
import Shaders.StaticShader;

public class MasterRenderer {
	private Map<TexturedModel, List<Model>> entities = new HashMap<TexturedModel, List<Model>>();
	
	public void render(Light sun, Camera camera, StaticShader shader) {
		Renderer renderer = new Renderer(shader);
		renderer.prepare3D();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);

		renderer.render3D(entities, shader);

		shader.stop();
		entities.clear();
	}

	public void render(Light sun, Camera camera, UniqueShader shader) {
		Renderer renderer = new Renderer(shader);

		renderer.prepare3D();
		shader.start();
		shader.loadViewMatrix(camera);

		renderer.render3D(entities, shader);

		shader.stop();
		entities.clear();
	}
	
	public void processEntity(Model model) {
		TexturedModel entityModel = model.getModel();
		List<Model> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(model);
		} else {
			List<Model> newBatch = new ArrayList<Model>();
			newBatch.add(model);
			entities.put(entityModel,newBatch);
		}
	}
	
	
	public void cleanUp(StaticShader shader) {
		shader.cleanUp();
	}

	public void cleanUp(UniqueShader shader){
		shader.cleanUp();
	}
	
	

}
