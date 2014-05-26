//
//  ChannelRank.m
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "ChannelRank.h"

@implementation ChannelRank

@synthesize channel = _channel;
@synthesize ranks = _ranks;

- (void) dealloc {
    TT_RELEASE_SAFELY(_channel);
    TT_RELEASE_SAFELY(_ranks);
    [super dealloc];
}

@end
