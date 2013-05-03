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

@synthesize actors = _actors;
@synthesize directors = _directors;
@synthesize area = _area;
@synthesize score = _score;
@synthesize year = _year;
@synthesize description = _description;
@synthesize onSelect = _onSelect;
@synthesize videoItemList = _videoItemList;
- (void) dealloc {
    TT_RELEASE_SAFELY(_title);
    TT_RELEASE_SAFELY(_subtitle);
    TT_RELEASE_SAFELY(_picUrl);
    TT_RELEASE_SAFELY(_vid);
    
    TT_RELEASE_SAFELY(_actors);
    TT_RELEASE_SAFELY(_directors);
    TT_RELEASE_SAFELY(_area);
    TT_RELEASE_SAFELY(_score);
    TT_RELEASE_SAFELY(_description);
    TT_RELEASE_SAFELY(_onSelect);
    TT_RELEASE_SAFELY(_videoItemList);
    [super dealloc];
}
@end
