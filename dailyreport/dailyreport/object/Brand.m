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
@synthesize date = _date;
@synthesize dayAmount = _dayAmount;
@synthesize weekAmount = _weekAmount;
@synthesize yearAmount = _yearAmount;
@synthesize weekLike = _weekLike;
@synthesize yearLike = _yearLike;

- (void) dealloc {
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_date);
    TT_RELEASE_SAFELY(_dayAmount);
    TT_RELEASE_SAFELY(_weekAmount);
    TT_RELEASE_SAFELY(_yearAmount);
    TT_RELEASE_SAFELY(_weekLike);
    TT_RELEASE_SAFELY(_yearLike);
    [super dealloc];
}

@end
