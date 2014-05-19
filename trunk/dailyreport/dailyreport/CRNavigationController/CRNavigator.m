//
//  CRNavigator.m
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "CRNavigator.h"
#import "CRNavigationController.h"
@implementation CRNavigator

+ (TTNavigator*)navigator {
    TTBaseNavigator* navigator = [TTBaseNavigator globalNavigator];
    if (nil == navigator) {
        navigator = [[[CRNavigator alloc] init] autorelease];
        // setNavigator: retains.
        [super setGlobalNavigator:navigator];
    }
    // If this asserts, it's likely that you're attempting to use two different navigator
    // implementations simultaneously. Be consistent!
    TTDASSERT([navigator isKindOfClass:[TTNavigator class]]);
    return (TTNavigator*)navigator;
}

- (Class)navigationControllerClass {
    return [CRNavigationController class];
}
@end
