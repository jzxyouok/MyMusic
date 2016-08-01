package zhiyuan3g.com.mymusic.utils;

import java.util.Comparator;

import zhiyuan3g.com.mymusic.entity.ArtistInfo;
import zhiyuan3g.com.mymusic.entity.MusicInfo;

/**
 * 
 * @author xiaanming
 *
 */
public class ArtistInfoPinyinComparator implements Comparator<ArtistInfo> {

	@Override
	public int compare(ArtistInfo lhs, ArtistInfo rhs) {
		if (lhs.getArtist_sortLetters().equals("@")
				|| rhs.getArtist_sortLetters().equals("#")) {
			return -1;
		} else if (lhs.getArtist_sortLetters().equals("#")
				|| rhs.getArtist_sortLetters().equals("@")) {
			return 1;
		} else {
			return lhs.getArtist_sortLetters().compareTo(rhs.getArtist_sortLetters());
		}
	}
}
