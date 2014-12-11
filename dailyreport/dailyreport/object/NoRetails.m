//
//  NoRetails.m
//  dailyreport
//
//  Created by zhang wei on 14-12-11.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "NoRetails.h"

@implementation NoRetails

@synthesize channel = _channel;
@synthesize stores = _stores;

- (void) dealloc {
    TT_RELEASE_SAFELY(_stores);
    TT_RELEASE_SAFELY(_channel);
    [super dealloc];
}

@end
