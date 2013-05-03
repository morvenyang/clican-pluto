//
//  Mobility.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Mobility.h"


@implementation Mobility
@synthesize mapType1 = _mapType1;
@synthesize mapType2 = _mapType2;
@synthesize mapType3 = _mapType3;
-(id)init{
    self = [super init];
    if(self!=nil){
        _mapType1 = 1;
        _mapType2 = 2;
        _mapType3 = -1;
    }
    return self;
}

+(id) mobilityWithDefault{
    Mobility* mobility = [[Mobility alloc] init];
    [mobility autorelease];
    return mobility;
}

- (void)dealloc {
    [super dealloc];
}

@end
