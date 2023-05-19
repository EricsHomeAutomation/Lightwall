package org.golde.dormroom.lightwall.scene.options.workarounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.golde.dormroom.lightwall.scene.options.Color;
import org.golde.dormroom.lightwall.scene.options.SceneDescriptor;
import org.golde.dormroom.lightwall.scene.options.SceneOptions;

public class SceneDescriptorWorkaround {

	public String name;
	public int type;
	public Map<String, Object> options;
	public List<String> colors;
	
	
	
	@Override
	public String toString() {
		return "SceneDescriptorWorkaround [name=" + name + ", type=" + type + ", options=" + options + ", colors="
				+ colors + "]";
	}



	public SceneDescriptor toSceneDescriptor(int id) {
		
		SceneDescriptor sd = new SceneDescriptor();
		sd.name = name;
		sd.id = id;
		sd.type = type;
		SceneOptions so  = new SceneOptions();
		
		List<Color> soColors = new ArrayList<Color>();
		for(String c : colors) {
			soColors.add(new Color(c));
		}
		
		so.colors = soColors.toArray(new Color[0]);
		so.customOptions = options;
		
		sd.options = so;
		
		return sd;
	}
	
}
