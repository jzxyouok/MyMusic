package zhiyuan3g.com.mymusic.utils;

import java.util.Comparator;

import zhiyuan3g.com.mymusic.entity.AlbumInfo;
import zhiyuan3g.com.mymusic.entity.ArtistInfo;

/**
 * 
 * @author xiaanming
 *
 */
public class AlbumInfoPinyinComparator implements Comparator<AlbumInfo> {

	@Override
	public int compare(AlbumInfo lhs, AlbumInfo rhs) {
		if (lhs.getAlbum_sortLetters().equals("@")
				|| rhs.getAlbum_sortLetters().equals("#")) {
			return -1;
		} else if (lhs.getAlbum_sortLetters().equals("#")
				|| rhs.getAlbum_sortLetters().equals("@")) {
			return 1;
		} else {
			return lhs.getAlbum_sortLetters().compareTo(rhs.getAlbum_sortLetters());
		}
	}
}
