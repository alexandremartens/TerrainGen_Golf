package engineTester;

import com.badlogic.gdx.math.Vector2;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap"));

		RawModel model = OBJLoader.loadObjModel("tree", loader);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));

		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");
		//Terrain terrain2 = new Terrain(1,1, loader, texturePack, blendMap, "heightmap");

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<1000;i++){
			float x = random.nextFloat() *800 ;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x,z);
			entities.add(new Entity(staticModel, new Vector3f(x,y,z),0,0,0,2));
		}

		MasterRenderer renderer = new MasterRenderer();

		RawModel ball = OBJLoader.loadObjModel("stanfordBunny", loader);
		TexturedModel standfordBall = new TexturedModel(ball, new ModelTexture(loader.loadTexture("white")));

		Player player = new Player(standfordBall, new Vector3f(300.38553f, 0, -188f),0,0,0,0.1f); // 300,0,-200
		Camera camera = new Camera(player);

		player.setVeloVector(new Vector2(0.1f,0.1f));
		player.setCurrentSpeed((float) Math.sqrt(Math.pow(player.getVeloVector().x, 2) + Math.pow(player.getVeloVector().y, 2)));

		while(!Display.isCloseRequested()){
			camera.move();
			player.move(terrain);
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			//renderer.processTerrain(terrain3);
			//renderer.processTerrain(terrain4);

			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
