//
//  VideoTableItem.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoTableItem.h"

@implementation VideoTableItem

@synthesize videoList = _videoList;


+ (id)itemWithVideoList:(NSMutableArray*)videoList
{
    VideoTableItem* item = [[[self alloc] init] autorelease];
    item.videoList = videoList;
    return item;
    
}



///////////////////////////////////////////////////////////////////////////////////////////////////
// NSObject

- (id)init {
	if (self = [super init]) {
		_videoList = nil;
	}
	return self;
}

- (void)dealloc {
	TT_RELEASE_SAFELY(_videoList);
	[super dealloc];
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// NSCoding

- (id)initWithCoder:(NSCoder*)decoder {
	if (self = [super initWithCoder:decoder]) {
		self.videoList = [decoder decodeObjectForKey:@"videoList"];
	}
	return self;
}

- (void)encodeWithCoder:(NSCoder*)encoder {
	[super encodeWithCoder:encoder];
	if (self.videoList) {
		[encoder encodeObject:self.videoList forKey:@"videoList"];
	}
}

@end
