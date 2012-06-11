//
//  DisplayMoveShadowAndPreMoveAction.m
//  ccdream
//
//  Created by wei zhang on 12-6-11.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "DisplayMoveShadowAndPreMoveAction.h"
#import "Character.h"
#import "MapLayer.h"

@implementation DisplayMoveShadowAndPreMoveAction

-(void) playSprite:(Session*) session istate:(IState*) previousState event:(Event*) event{
    MapLayer* mapLayer = [self getVariableValue:PARAM_MAP_LAYER variables:event.variables];
    Character* character = [self getVariableValue:PARAM_SELECTED_CHARACTER variables:event.variables];
    Position* charPosi = [Position positionWithCGPoint:character.characterSprite.position];
    
    Mobility* moblitity = [Mobility mobilityWithDefault];
    
    CCArray* posiArray = nil;
    //计算可移动范围
    
    posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:3 mobility:moblitity mapGridAttributeMap:mapLayer.mapGridAttributeMap maxPosition:mapLayer.maxPosi playerCharacterArray:mapLayer.playerCharacterArray enemyCharacterArray:mapLayer.enemyCharacterArray];
    CCLOG(@"count=%i",[posiArray count]);
    
    CCArray* shadowSpriteArray = [self getVariableValue:PARAM_SHADOW_ARRAY variables:event.variables];
    [self cleanShadowSpriteArray:shadowSpriteArray];
    
    for(int i=0;i<[posiArray count];i++){
        MoveOrbit* mo = [posiArray objectAtIndex:i];
        CCLOG(@"%@",[mo.position description]);
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE, MAP_POINT_SIZE);
        
        redSprite.position = CGPointMake(mo.position.x*MAP_POINT_SIZE+MAP_POINT_SIZE/2, mo.position.y*MAP_POINT_SIZE+MAP_POINT_SIZE/2);
        [shadowSpriteArray addObject:redSprite];
        [mapLayer addChild:redSprite];
    }
    mapLayer.movementArray = posiArray;
}

-(void) cleanShadowSpriteArray:(CCArray*) shadowSpriteArray{
    int count = [shadowSpriteArray count];
    for(int i=0;i<count;i++){
        CCSprite* shadowSprit = [shadowSpriteArray objectAtIndex:i];
        [shadowSprit removeFromParentAndCleanup:YES];
    }
}
@end
