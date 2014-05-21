//
//  Retail.m
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "Retail.h"

@implementation Retail

@synthesize name = _name;
@synthesize dayAmount = _dayAmount;

- (void) dealloc {
    TT_RELEASE_SAFELY(_name);
    TT_RELEASE_SAFELY(_dayAmount);
    [super dealloc];
}
@end
