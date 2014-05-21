//
//  Channel.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "Channel.h"

@implementation Channel
@synthesize channel = _channel;
@synthesize dayAmount = _dayAmount;
@synthesize docNumber = _docNumber;
@synthesize avgDocCount = _avgDocCount;
@synthesize avgPrice = _avgPrice;
@synthesize aps = _aps;
- (void) dealloc {
    TT_RELEASE_SAFELY(_channel);
    TT_RELEASE_SAFELY(_dayAmount);
    TT_RELEASE_SAFELY(_docNumber);
    TT_RELEASE_SAFELY(_avgDocCount);
    TT_RELEASE_SAFELY(_avgPrice);
    TT_RELEASE_SAFELY(_aps);
    [super dealloc];
}

@end
