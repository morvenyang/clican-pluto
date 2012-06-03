//
//  Character.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Character.h"


@implementation Character

@synthesize airMobility = _airMobility;
@synthesize landMobility = _landMobility;

-(id)init{
    self = [super init];
    if(self !=nil){
        self.airMobility = [Mobility initWithDefault];
        self.landMobility = [Mobility initWithDefault];
    }
    return self;
}

- (void)dealloc {
    [_airMobility release];
    _airMobility = nil;
	[_landMobility release];
    _landMobility = nil;
	[super dealloc];
}
@end
