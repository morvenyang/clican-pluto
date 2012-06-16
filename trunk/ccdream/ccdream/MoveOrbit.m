//
//  MoveOrbit.m
//  ccdream
//
//  Created by wei zhang on 12-6-4.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MoveOrbit.h"


@implementation MoveOrbit

@synthesize previous = _previous;
@synthesize position = _position;
@synthesize moveDistance = _moveDistance;
@synthesize spentMovement = _spentMovement;

+(id)moveOrbitWithPrevious:(MoveOrbit*) previous Position:(Position*) position{
    MoveOrbit* mo = [[MoveOrbit alloc] init];
    mo.previous = previous;
    mo.position = position;
    if(previous!=nil){
        mo.moveDistance = previous.moveDistance+1;
    }else{
        mo.moveDistance = 0;
    }
    
    [mo autorelease];
    return mo;
}

+(id)posiComparator{
    return ^NSComparisonResult(MoveOrbit* obj1, MoveOrbit* obj2) {
        if(obj1.position.x>obj2.position.x){
            return 1;
        }else if(obj1.position.x<obj2.position.x){
            return -1;
        }else {
            if(obj1.position.y>obj2.position.y){
                return 1;
            }else if(obj1.position.y<obj2.position.y){
                return -1;
            }else {
                return 0;
            }
        }
    };
}

+(id)distanceComparator{
    return ^NSComparisonResult(MoveOrbit* obj1, MoveOrbit* obj2) {
        if(obj1.moveDistance>obj2.moveDistance){
            return 1;
        }else if(obj1.moveDistance<obj2.moveDistance){
            return -1;
        }else{
            return 0;
        }
    };
}

-(NSString*) description{
    return [@"" stringByAppendingFormat:@"position:%@,spentMovement=%i",_position,_spentMovement];
}

- (void)dealloc {
    [_previous release];
    _previous = nil;
	[_position release];
    _position = nil;
    [super dealloc];
}

@end
