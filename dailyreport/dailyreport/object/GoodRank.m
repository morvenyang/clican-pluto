//
//  GoodRank.m
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "GoodRank.h"

@implementation GoodRank
@synthesize name= _name;
@synthesize amount = _amount;
@synthesize count = _count;
@synthesize imageLink = _imageLink;
@synthesize imageLinkMin = _imageLinkMin;
@synthesize season = _season;
@synthesize line = _line;
@synthesize wave = _wave;
@synthesize year = _year;
@synthesize colorName = _colorName;
- (void) dealloc {
    TT_RELEASE_SAFELY(_name);
    TT_RELEASE_SAFELY(_amount);
    TT_RELEASE_SAFELY(_count);
    TT_RELEASE_SAFELY(_imageLink);
    TT_RELEASE_SAFELY(_imageLinkMin);
    TT_RELEASE_SAFELY(_season);
    TT_RELEASE_SAFELY(_line);
    TT_RELEASE_SAFELY(_wave);
    TT_RELEASE_SAFELY(_year);
    TT_RELEASE_SAFELY(_colorName);
    [super dealloc];
}

@end
