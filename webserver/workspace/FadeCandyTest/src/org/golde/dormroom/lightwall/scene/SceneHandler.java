package org.golde.dormroom.lightwall.scene;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;

import org.golde.dormroom.lightwall.scene.options.SceneDescriptor;
import org.golde.dormroom.lightwall.scene.options.workarounds.ScenesJSONFile;
import org.golde.dormroom.lightwall.scene.options.workarounds.SceneDescriptorWorkaround;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class SceneHandler {

	private Scene currentAnimation = new SceneDummy();

	private ScenesJSONFile scenesJSONFile;
	//private List<SceneDescriptor> sceneDescriptors = new ArrayList<SceneDescriptor>();
	private static final File SCENES_FILE = new File("files" + File.separator + "scenes.json");

	private final SceneOnOff onOffManager = new SceneOnOff();

	private Object fileRWSyncObject = new Object();

	public SceneHandler() {
		try {
			reloadJSON();
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Just to make sure, I dont think I need this but it doesnt hurt
	}

	public void setScene(int id) throws IOException {
		reloadJSON();

		if(scenesJSONFile == null || scenesJSONFile.scenes == null) {
			System.out.println("[Warn] Weirdly the json file was null here. Sometimes this happens when sending requests to quickly.");
			System.out.println("[Warn] Trying to reload the JSON file one last time...");
			reloadJSON();
			return;
		}

		for(SceneDescriptor sd : scenesJSONFile.scenes) {
			if(sd.id == id) {
				setScene(createSceneFromDescriptor(sd));
			}
		}

		scenesJSONFile.lastSceneSelected = id;
		saveJSON();
	}

	public void setScene(Scene scene) {
		this.currentAnimation = onOffManager.setActualScene(scene);
	}

	public Scene getAnimation() {
		return currentAnimation;
	}

	private Scene createSceneFromDescriptor(SceneDescriptor sd){

		int type = sd.type;
		Scene scene;

		if(type == 0) {
			scene = new SceneDots();
		}
		else if(type == 1) {
			scene = new SceneRainbow();
		}
		else if(type == 2) {
			scene = new SceneCircles();
		}
		else if(type == 3) {
			scene = new SceneRaindrops();
		}
		else {
			scene = new SceneDummy();
		}

		if(sd.options != null) {
			scene.setOptions(sd.options);
		}

		return scene;
	}

	private void reloadJSON() throws JsonIOException, JsonSyntaxException, IOException {
		synchronized (fileRWSyncObject) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileReader fileReader = new FileReader(SCENES_FILE);
			scenesJSONFile = ((ScenesJSONFile)gson.fromJson(new JsonReader(fileReader), ScenesJSONFile.class));
			fileReader.close();
		}

	}

	private void saveJSON() throws JsonIOException, IOException {
		synchronized (fileRWSyncObject) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fileWriter = new FileWriter(SCENES_FILE);
			gson.toJson(scenesJSONFile, fileWriter);
			fileWriter.close();
		}
	}

	public void deleteScene(int id) throws JsonIOException, IOException {
		reloadJSON();
		int indexToRemove = -1;

		if(scenesJSONFile == null || scenesJSONFile.scenes == null) {
			System.out.println("[Warn] Weirdly the json file was null here. Sometimes this happens when sending requests to quickly.");
			return;
		}

		for(SceneDescriptor sd : scenesJSONFile.scenes) {
			if(sd.id == id) {
				indexToRemove = scenesJSONFile.scenes.indexOf(sd);
			}
		}
		if(indexToRemove != -1) {
			scenesJSONFile.scenes.remove(indexToRemove);
		}

		new File("files"+File.separator +"previmgs" + File.separator + id + ".gif").delete();

		saveJSON();
	}

	public void addScene(String json) throws JsonIOException, IOException {
		SceneDescriptor sd = createForSaveAndPreview(json);
		scenesJSONFile.scenes.add(sd);
		saveJSON();
	}

	public void previewScene(String json) throws IIOException, IOException {
		SceneDescriptor sd = createForSaveAndPreview(json);
		setScene(createSceneFromDescriptor(sd));
	}

	private SceneDescriptor createForSaveAndPreview(String json) throws IIOException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		SceneDescriptorWorkaround sdw = gson.fromJson(json, SceneDescriptorWorkaround.class);
		SceneDescriptor sd = sdw.toSceneDescriptor(getUnusedId());
		createSceneFromDescriptor(sd).toGif(sd.id);
		return sd;
	}

	private int getUnusedId() {
		int largest = 1;
		for(SceneDescriptor sd : scenesJSONFile.scenes) {
			if(sd.id > largest) {
				largest = sd.id;
			}
		}
		return largest+1;
	}

	public void setDisplaying(boolean on) {
		onOffManager.setDisplaying(on);
	}
	
	public boolean isDisplaying() {
		return onOffManager.isDisplaying();
	}

	public void setLastSceneAfterBoot() {
		if(scenesJSONFile.lastSceneSelected != -1) {
			try {
				setScene(scenesJSONFile.lastSceneSelected);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//		public void sceneData(String body) {
//			getAnimation().onRecieveData(body);
//		}

}
