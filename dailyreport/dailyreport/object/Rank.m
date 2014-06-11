//
//  Rank.m
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "Rank.h"

@implementation Rank

@synthesize name = _name;
@synthesize dayAmount = _dayAmount;
@synthesize rate = _rate;
- (void) dealloc {
    TT_RELEASE_SAFELY(_name);
    TT_RELEASE_SAFELY(_dayAmount);
    TT_RELEASE_SAFELY(_rate);
    [super dealloc];
}

@end
