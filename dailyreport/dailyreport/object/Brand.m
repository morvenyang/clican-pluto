//
//  Brand.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "Brand.h"

@implementation Brand

@synthesize brand = _brand;
@synthesize dayAmount = _dayAmount;

- (void) dealloc {
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_dayAmount);
    [super dealloc];
}

@end
