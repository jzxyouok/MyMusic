package zhiyuan3g.com.mymusic.utils;

import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.MusicInfo;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<MusicInfo> {

	@Override
	public int compare(MusicInfo lhs, MusicInfo rhs) {
		if (lhs.getMusic_sortLetters().equals("@")
				|| rhs.getMusic_sortLetters().equals("#")) {
			return -1;
		} else if (lhs.getMusic_sortLetters().equals("#")
				|| rhs.getMusic_sortLetters().equals("@")) {
			return 1;
		} else {
			return lhs.getMusic_sortLetters().compareTo(rhs.getMusic_sortLetters());
		}
	}
}
