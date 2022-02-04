package Engine;

import Entities.Model;
import Shaders.ShaderProgram;
import Shaders.UniqueShader;
import Shaders.StaticShader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Light;
import Models.RawModel;
import Models.TexturedModel;
import Textures.ModelTexture;


public class Engine {
	
	public static void main(String[] args) {
		
		int windowWidth = 1600;
		int windowHeight = 900;
		
		DisplayManager.createDisplay(windowWidth, windowHeight,16, "Model Viewer");

		ShaderProgram shader  = new StaticShader();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		RawModel rawModel = OBJLoader.loadObjModel("cube", loader);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("plaintex"));
		texture.setShineDamper(25);
		texture.setReflectivity(1);
				
		TexturedModel texModel = new TexturedModel(rawModel, texture);
		Model modelEnt = new Model(texModel, new Vector3f(0,-4f,-15), 0,0,0,0.9f);
		
		Light light = new Light(new Vector3f(-100,250,1000), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		// main loop of game logic + rendering
		while(!Display.isCloseRequested()){
			camera.move();
			
			// rotate model using arrow keys
			modelEnt.rotate(0,-0.1f,0);
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				if (shader instanceof UniqueShader) {
					shader = changeToStatic(shader);
				} else if (shader instanceof StaticShader) {
					shader = changeToUnique(shader);
				}
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)){
				modelEnt.scale(-0.005f);
			} else if(Keyboard.isKeyDown(Keyboard.KEY_EQUALS)){
				modelEnt.scale(0.005f);
			}

			// process entity for rendering
			renderer.processEntity(modelEnt);

			// render
			if(shader instanceof UniqueShader){
				renderer.render(light, camera, (UniqueShader) shader);
			} else if(shader instanceof StaticShader){
				renderer.render(light, camera, (StaticShader) shader);
			}

			// update display
			DisplayManager.updateDisplay();
		}
		
		// clean up loader/shader and close
		if(shader instanceof UniqueShader){
			renderer.cleanUp((UniqueShader) shader);
		} else if(shader instanceof StaticShader){
			renderer.cleanUp((StaticShader) shader);
		}
		loader.cleanUp();
		
		DisplayManager.closeDisplay();

	}

	static StaticShader changeToStatic(ShaderProgram shader){
		return new StaticShader();
	}

	static UniqueShader changeToUnique(ShaderProgram shader){
		return new UniqueShader();
	}
	
}
