package scrum.client.collaboration;

import ilarkesto.gwt.client.LabelProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import scrum.client.common.AScrumGwtEntity;

public class Emoticon extends GEmoticon {

	public Emoticon(AScrumGwtEntity parent, String emotion) {
		setOwner(cm.getAuth().getUser());
		setParent(parent);
		setEmotion(emotion);
	}

	public Emoticon(Map data) {
		super(data);
	}

	public String getEmotionLabel() {
		return getEmotionLabel(getEmotion());
	}

	public String getTooltip() {
		return getOwner().getName() + ": " + getEmotionLabel();
	}

	@Override
	public String toString() {
		return getOwner() + ":" + getEmotion();
	}

	public static String getEmotionLabel(String emotion) {
		if (emotion == null) return "-";
		return emotion;
	}

	public static transient final List<String> EMOTIONS = Arrays.asList("smile", "sad", "surprise", "angry");

	public transient static LabelProvider<String> EMOTION_LABEL_PROVIDER = new LabelProvider<String>() {

		public String getLabel(String emotion) {
			if (emotion == null) return null;
			return getEmotionLabel(emotion);
		}
	};

}