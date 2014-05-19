//
//  IndexTableItem.m
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexTableItem.h"

@implementation IndexTableItem

@synthesize backgroundImage = _backgroundImage;

+ (id)itemWithStyledText:(TTStyledText*)styledText backgroundImage:(NSString*)backgroundImage URL:(NSString*)url{
    IndexTableItem* item = [[[self alloc] init] autorelease];
    item.text = styledText;
    item.URL = url;
    item.backgroundImage = backgroundImage;
    return item;
}

- (void)dealloc {
	TT_RELEASE_SAFELY(_backgroundImage);
	[super dealloc];
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// NSCoding

- (id)initWithCoder:(NSCoder*)decoder {
	if (self = [super initWithCoder:decoder]) {
		self.backgroundImage = [decoder decodeObjectForKey:@"backgroundImage"];
	}
	return self;
}

- (void)encodeWithCoder:(NSCoder*)encoder {
	[super encodeWithCoder:encoder];
	if (self.backgroundImage) {
		[encoder encodeObject:self.backgroundImage forKey:@"backgroundImage"];
	}
}
@end
