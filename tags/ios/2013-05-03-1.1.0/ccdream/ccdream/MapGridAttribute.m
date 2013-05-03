//
//  MapGridAttribute.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapGridAttribute.h"


@implementation MapGridAttribute

@synthesize mapType = _mapType;
@synthesize additionalHit = _additionalHit;
@synthesize additionalAvoid = _additionalAvoid;
@synthesize position = _position;

+(id) mapGridAttribut:(NSDictionary*) properties position:(Position*) position{
    MapGridAttribute* mga = [[[MapGridAttribute alloc] init] autorelease];
    mga.position = position;
    NSString* mapTypeStr = [properties objectForKey:@"mapType"];
    NSString* additionalHitStr = [properties objectForKey:@"additionalHit"];
    NSString* additionalAvoidStr = [properties objectForKey:@"additionalAvoid"];
    mga.mapType = [mapTypeStr intValue];
    if(additionalHitStr!=nil&&[additionalHitStr length]>0){
        mga.additionalHit = [additionalHitStr intValue];
    }
    if(additionalAvoidStr!=nil&&[additionalAvoidStr length]>0){
        mga.additionalAvoid = [additionalAvoidStr intValue];
    }
    return mga;
}

- (void)dealloc {
    [_position release];
    _position = nil;
    [super dealloc];
}

@end
