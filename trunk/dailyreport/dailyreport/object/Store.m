//
//  Store.m
//  dailyreport
//
//  Created by zhang wei on 14-12-11.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "Store.h"

@implementation Store

@synthesize storeCode = _storeCode;
@synthesize storeName = _storeName;

- (void) dealloc {
    TT_RELEASE_SAFELY(_storeName);
    TT_RELEASE_SAFELY(_storeCode);
    [super dealloc];
}
@end
