//
//  PositionUtil.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 HP. All rights reserved.
//

#import "PositionUtil.h"


@implementation PositionUtil

+(CCArray*) calcMoveOrbitarrayFromPosition:(Position*) charPosi movement:(int) movement mobility:(Mobility*) mobility mapTypeMetrix:(NSDictionary*) mapTypeMetrix maxPosition:(Position*) maxPosition{
    NSMutableDictionary* moveOrbitSet = [[[NSMutableDictionary alloc] init] autorelease];
    NSMutableDictionary* processedPosiMap = [[[NSMutableDictionary alloc] init] autorelease];
    
    MoveOrbit* current = [MoveOrbit initWithPrevious:nil Position:charPosi];
    [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: current movement: movement mobility:mobility mapTypeMetrix: mapTypeMetrix maxPosition: maxPosition xoffset:0 yoffset:0];
    NSArray* sortedArray = [[moveOrbitSet allValues] sortedArrayUsingComparator:[MoveOrbit comparator]];
    CCArray* result = [CCArray arrayWithNSArray:sortedArray];
    return result;   
}


+(void) moveForProcessedposiMap:(NSMutableDictionary*) processedPosiMap moveOrbitSet:(NSMutableDictionary*) moveOrbitSet previousMoveOrbit:(MoveOrbit*) previousMoveOrbit movement:(int) movement mobility:(Mobility*)mobility mapTypeMetrix:(NSDictionary*) mapTypeMetrix maxPosition:(Position*) maxPosition xoffset:(int)xoffset yoffset:(int)yoffset{
    Position* currentPosi = [Position initWithX:previousMoveOrbit.position.x+xoffset Y:previousMoveOrbit.position.y+yoffset];
    NSNumber* remainingMovement = (NSNumber*)[processedPosiMap objectForKey:[currentPosi description]];
    if(remainingMovement!=nil&&[remainingMovement intValue]>=movement){
        return;
    }else{
        NSNumber* wrapMovement = [[[NSNumber alloc] autorelease] initWithInt:movement];
        [processedPosiMap setValue:wrapMovement forKey:[currentPosi description]];
    }
    int x = currentPosi.x;
    int y = currentPosi.y;
    
    int mapType = [(NSNumber*)[mapTypeMetrix objectForKey:[currentPosi description]] intValue];
    int moveCost = 0;
    
    //如果不是起始点
    if(xoffset!=0||yoffset!=0){
        if(mapType==1){
            moveCost = mobility.mapType1;
        }else if (mapType==2) {
            moveCost = mobility.mapType2;
        }else if(mapType==-1){
            //enemy
            return;
        }
    }
    
    if(moveCost>movement){
        //can't move this position
        return;
    }else{
        MoveOrbit* moveOrbit = [MoveOrbit initWithPrevious:previousMoveOrbit Position:currentPosi];
        if(xoffset!=0||yoffset!=0){
            [moveOrbitSet setValue:moveOrbit forKey:[moveOrbit.position description]];
        }else{
            moveOrbit.previous = nil;
        }
        
        BOOL moveLeft = YES;
        BOOL moveRight = YES;
        BOOL moveUp = YES;
        BOOL moveDown = YES;
        
        if(xoffset==-1){
            moveRight = NO;
        }else if(xoffset==1){
            moveLeft = NO;
        }else if (yoffset==-1){
            moveUp = NO;
        }else if(yoffset==1){
            moveDown = NO;
        }
        
        if(x==0){
            moveLeft = NO;
        }else if(x==maxPosition.x){
            moveRight = NO;
        }
        
        if(y==0){
            moveDown = NO;
        }else if(y==maxPosition.y){
            moveUp = NO;
        }
        
        if(moveLeft){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapTypeMetrix: mapTypeMetrix maxPosition: maxPosition xoffset:-1 yoffset:0];
        }
        
        if(moveRight){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapTypeMetrix: mapTypeMetrix maxPosition: maxPosition xoffset:1 yoffset:0];
        }
        
        if(moveUp){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapTypeMetrix: mapTypeMetrix maxPosition: maxPosition xoffset:0 yoffset:1];
        }
        
        if(moveDown){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapTypeMetrix: mapTypeMetrix maxPosition: maxPosition xoffset:0 yoffset:-1];
        }
    }
    
}

+(CGPoint) locationFromTouch:(UITouch*)touch
{
	CGPoint touchLocation = [touch locationInView: [touch view]];
	return [[CCDirector sharedDirector] convertToGL:touchLocation];
}

+(bool) isTouch:(UITouch*)touch forNode:(CCNode*) node
{
    CGPoint touchLocation = [self locationFromTouch:touch];
    return CGRectContainsPoint([node boundingBox], touchLocation);
}
@end
