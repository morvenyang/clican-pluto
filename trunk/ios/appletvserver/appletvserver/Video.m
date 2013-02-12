//
//  Video.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "Video.h"

@implementation Video

@synthesize title = _title;
@synthesize subTitle = _subtitle;
@synthesize picUrl = _picUrl;
@synthesize vid = _vid;

- (void) dealloc {
    TT_RELEASE_SAFELY(_title);
    TT_RELEASE_SAFELY(_subtitle);
    TT_RELEASE_SAFELY(_picUrl);
    TT_RELEASE_SAFELY(_vid);
    [super dealloc];
}
@end
