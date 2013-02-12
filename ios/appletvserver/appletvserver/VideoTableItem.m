//
//  VideoTableItem.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoTableItem.h"

@implementation VideoTableItem

@synthesize video = _video;


+ (id)itemWithVideo:(Video*)video URL:(NSString*)url
{
    VideoTableItem* item = [[[self alloc] init] autorelease];
    NSString* title = [NSString stringWithFormat:@"<span class=\"assetTitle\"><strong>%@</strong></span>",video.title];
    item.text = [TTStyledText textFromXHTML:title lineBreaks:YES URLs:YES];
    
    item.URL = url;
    item.video = video;
    return item;
    
}



///////////////////////////////////////////////////////////////////////////////////////////////////
// NSObject

- (id)init {
	if (self = [super init]) {
		_video = nil;
	}
	return self;
}

- (void)dealloc {
	TT_RELEASE_SAFELY(_video);
	[super dealloc];
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// NSCoding

- (id)initWithCoder:(NSCoder*)decoder {
	if (self = [super initWithCoder:decoder]) {
		self.video = [decoder decodeObjectForKey:@"video"];
	}
	return self;
}

- (void)encodeWithCoder:(NSCoder*)encoder {
	[super encodeWithCoder:encoder];
	if (self.video) {
		[encoder encodeObject:self.video forKey:@"video"];
	}
}

@end
