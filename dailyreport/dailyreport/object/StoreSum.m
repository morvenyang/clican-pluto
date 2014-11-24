//
//  StoreSum.m
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "StoreSum.h"

@implementation StoreSum
@synthesize sumType = _sumType;
@synthesize total = _total;
@synthesize selfV = _selfV;
@synthesize join = _join;
@synthesize unionV = _unionV;

- (void) dealloc {
    TT_RELEASE_SAFELY(_sumType);
    TT_RELEASE_SAFELY(_total);
    TT_RELEASE_SAFELY(_selfV);
    TT_RELEASE_SAFELY(_join);
    TT_RELEASE_SAFELY(_unionV);
    [super dealloc];
}
@end
