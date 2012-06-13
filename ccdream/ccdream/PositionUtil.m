//
//  PositionUtil.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 HP. All rights reserved.
//

#import "PositionUtil.h"


@implementation PositionUtil

+(CCArray*) calcMoveOrbitarrayFromPosition:(Position*) charPosi movement:(int) movement mobility:(Mobility*) mobility mapGridAttributeMap:(NSDictionary*) mapGridAttributeMap maxPosition:(Position*) maxPosition playerCharacterArray:(CCArray*) playerCharacterArray enemyCharacterArray:(CCArray*) enemyCharacterArray{
    NSMutableDictionary* moveOrbitSet = [[[NSMutableDictionary alloc] init] autorelease];
    NSMutableDictionary* processedPosiMap = [[[NSMutableDictionary alloc] init] autorelease];
    
    MoveOrbit* current = [MoveOrbit moveOrbitWithPrevious:nil Position:charPosi];
    [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: current movement: movement mobility:mobility mapGridAttributeMap: mapGridAttributeMap maxPosition: maxPosition playerCharacterArray:playerCharacterArray enemyCharacterArray:enemyCharacterArray xoffset:0 yoffset:0];
    
    
    NSArray* sortedArray = [[moveOrbitSet allValues] sortedArrayUsingComparator:[MoveOrbit comparator]];
    CCArray* result = [CCArray arrayWithNSArray:sortedArray];
    return result;   
}


+(void) moveForProcessedposiMap:(NSMutableDictionary*) processedPosiMap moveOrbitSet:(NSMutableDictionary*) moveOrbitSet previousMoveOrbit:(MoveOrbit*) previousMoveOrbit movement:(int) movement mobility:(Mobility*)mobility mapGridAttributeMap:(NSDictionary*) mapGridAttributeMap maxPosition:(Position*) maxPosition playerCharacterArray:(CCArray*) playerCharacterArray enemyCharacterArray:(CCArray*) enemyCharacterArray xoffset:(int)xoffset yoffset:(int)yoffset{
    Position* currentPosi = [Position positionWithX:previousMoveOrbit.position.x+xoffset Y:previousMoveOrbit.position.y+yoffset];
    NSNumber* remainingMovement = (NSNumber*)[processedPosiMap objectForKey:[currentPosi description]];
    if(remainingMovement!=nil&&[remainingMovement intValue]>=movement){
        return;
    }else{
        NSNumber* wrapMovement = [[[NSNumber alloc] autorelease] initWithInt:movement];
        [processedPosiMap setValue:wrapMovement forKey:[currentPosi description]];
    }
    int x = currentPosi.x;
    int y = currentPosi.y;
    
    int mapType = [(MapGridAttribute*)[mapGridAttributeMap objectForKey:[currentPosi description]] mapType];
    int moveCost = 0;
    
    if([PositionUtil containsPosition:currentPosi forCharacterArray:enemyCharacterArray]){
        //敌人所在位置不可站立
        return;
    }
    //如果不是起始点
    if(xoffset!=0||yoffset!=0){
        if(mapType==1){
            moveCost = mobility.mapType1;
        }else if (mapType==2) {
            moveCost = mobility.mapType2;
        }else if(mapType==-1){
            //不可站立位置
            return;
        }
    }
    
    if(moveCost>movement){
        //can't move this position
        return;
    }else{
        MoveOrbit* moveOrbit = [MoveOrbit moveOrbitWithPrevious:previousMoveOrbit Position:currentPosi];
        if(xoffset!=0||yoffset!=0){
            if(![PositionUtil containsPosition:currentPosi forCharacterArray:playerCharacterArray]){
                //非友军位置可以站立
                [moveOrbitSet setValue:moveOrbit forKey:[moveOrbit.position description]];
            }

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
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapGridAttributeMap: mapGridAttributeMap maxPosition: maxPosition playerCharacterArray:playerCharacterArray enemyCharacterArray:enemyCharacterArray xoffset:-1 yoffset:0];
        }
        
        if(moveRight){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapGridAttributeMap: mapGridAttributeMap maxPosition: maxPosition playerCharacterArray:playerCharacterArray enemyCharacterArray:enemyCharacterArray xoffset:1 yoffset:0];
        }
        
        if(moveUp){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapGridAttributeMap: mapGridAttributeMap maxPosition: maxPosition playerCharacterArray:playerCharacterArray enemyCharacterArray:enemyCharacterArray xoffset:0 yoffset:1];
        }
        
        if(moveDown){
            [PositionUtil moveForProcessedposiMap: processedPosiMap moveOrbitSet: moveOrbitSet previousMoveOrbit: moveOrbit movement: movement-moveCost mobility:mobility mapGridAttributeMap: mapGridAttributeMap maxPosition: maxPosition playerCharacterArray:playerCharacterArray enemyCharacterArray:enemyCharacterArray xoffset:0 yoffset:-1];
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

+(bool) isPosition:(Position*)position forNode:(CCNode*) node
{
    CGPoint p1 = [position toCenterCGPoint];

    CGPoint p2 = node.position;

    return p1.x==p2.x&&p1.y==p2.y;
}

+(bool) containsPosition:(Position*)position forMoveOrbitArray:(CCArray*) array
{
    int count = [array count];
    for(int i=0;i<count;i++){
        Position* posi = [(MoveOrbit*)[array objectAtIndex:i] position];
        if(posi.x==position.x&&posi.y==position.y){
            return YES;
        }
    }
    return NO;
}

+(bool) containsPosition:(Position*)position forCharacterArray:(CCArray*) array
{
    int count = [array count];
    for(int i=0;i<count;i++){
        Position* posi = [(Character*)[array objectAtIndex:i] sourcePosition] ;
        if(position.x==posi.x&&position.y==posi.y){
            return YES;
        }
    }
    return NO;
}

+(int) calcPositionRange:(Position*) source dest:(Position*) dest{
    int range = abs(source.x-dest.x)+abs(source.y-dest.y);
    return range;
}

+(bool) canAttack:(Position*) attacker targetCharacterArray:(CCArray*) targetCharacterArray rangeSet:(NSSet*) rangeSet{
    for (Character* targetCharacter in targetCharacterArray) {
        Position* target = targetCharacter.sourcePosition;
        int range = [PositionUtil calcPositionRange:attacker dest:target];
        NSNumber* rangeNumber = [NSNumber numberWithInt:range];
        if([rangeSet containsObject:rangeNumber]){
            return YES;
        }
    }
    return NO;
}
@end
