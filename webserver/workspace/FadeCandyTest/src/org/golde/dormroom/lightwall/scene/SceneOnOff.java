package org.golde.dormroom.lightwall.scene;

import org.golde.dormroom.lightwall.scene.options.Color;

import lombok.Getter;

/*
 * Ok stupid way to do this
 * 
 * Its a scene that normally displays another scene
 * but can stop animating that scene and just display all black pixels
 */
public class SceneOnOff extends Scene {

	private Scene actualScene = new SceneDummy();

	@Getter boolean displaying = true;
	@Getter private boolean sendOPC = true;

	@Override
	public void init() {
		actualScene.init();
	}

	@Override
	public void draw() {

		if(displaying) {

			actualScene.draw();

			//populate our pixels
			setPixels(actualScene.getPixels());
		}
	}

	@Override
	public void reset() {
		super.reset(); //init all the pixels on the dummy scene to avoid a nullpointer
		actualScene.reset();
	}

	public Scene setActualScene(Scene actualScene) {
		this.actualScene = actualScene;
		return this;
	}

	public void setDisplaying(boolean displaying) {
		this.displaying = displaying;

		if(!this.displaying) {
			setAllPixels(Color.COLOR_BLACK);
		}
	}

}
