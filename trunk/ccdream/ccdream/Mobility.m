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

-(id)init{
    self = [super init];
    if(self!=nil){
        _mapType1 = 1;
        _mapType2 = -1;
    }
    return self;
}

+(id) initWithDefault{
    Mobility* mobility = [[Mobility alloc] init];
    return mobility;
}

- (void)dealloc {
    [super dealloc];
}

@end
