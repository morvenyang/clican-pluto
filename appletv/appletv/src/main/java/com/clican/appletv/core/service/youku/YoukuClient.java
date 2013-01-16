package com.clican.appletv.core.service.youku;

import com.clican.appletv.core.service.youku.model.YoukuAlbum;

public interface YoukuClient {

	public YoukuAlbum queryAlbum(String showid);
}
