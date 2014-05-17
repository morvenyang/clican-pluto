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

- (void) dealloc {
    TT_RELEASE_SAFELY(_channel);
    TT_RELEASE_SAFELY(_dayAmount);
    [super dealloc];
}

@end
