//
//  VideoItem.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoItem.h"

@implementation VideoItem

@synthesize title=_title;
@synthesize itemId=_itemId;

- (void) dealloc {
    TT_RELEASE_SAFELY(_title);
    TT_RELEASE_SAFELY(_itemId);
    [super dealloc];
}

@end
